package com.android.jidian.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.application.MyApplication;
import com.android.jidian.client.bean.HttpGetUpImageUrlBean;
import com.android.jidian.client.mvp.ui.dialog.AutenticationFragment;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.pub.Md5;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.util.Util;
import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.jidian.client.http.HeaderTypeData.getLocalVersion;
import static com.android.jidian.client.http.HeaderTypeData.getLocalVersionName;

/**
 * Modified by fight on 2019/11/18
 */
@SuppressLint("Registered")
@EActivity(R.layout.main_authentication)
public class MainAuthentication extends BaseActivity {
    private static final String TAG = "MainAuthentication1";
    private static final int HEADS_CAMERA = 8083;//正面拍照选择
    private static final int HEADS_ALBUM = 8081;//正面相册选择
    private static final int TAILS_CAMERA = 8084;//反面拍照选择
    private static final int TAILS_ALBUM = 8082;//反面相册选择
    @ViewById
    ImageView user_card_1_img, user_card_2_img;
    @ViewById
    Button submit;
    @ViewById
    TextView user_id;
    @ViewById
    EditText user_name;
    private String img_1_path = "", img_2_path = "";
    private String img_1_path_int = "", img_2_path_int = "";
    private String str_name = "", str_id = "";
    private String selectType = "身份证";
    private Uri imageUri;
    private File outputImage;
    private boolean isFirstPic = true;//判断是正面照还是反面照，用于照片信息错误后清空imageview
    //上传正面和反面给阿里使用的都是uplaodAliyunFaceSuccessHandler；上传正面给服务器是upLoadImage_1SuccessHandler，反面是upLoadImage_2SuccessHandler
    private Handler errorHandler, upLoadImage_1SuccessHandler, upLoadImage_2SuccessHandler;

    String ali_face_url = "";
    String ali_face_appcode = "";

    @AfterViews
    void afterview() {
        activity = this;
        handler();
        lacksPermission();
        user_id.setEnabled(false);
    }

    @SuppressLint("HandlerLeak")
    private void handler() {
        errorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = msg.getData().getString("msg");
                MyToast.showTheToast(activity, str);
                progressDialog.dismiss();
            }
        };

        upLoadImage_1SuccessHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = msg.getData().getString("data");
                try {
                    JSONTokener jsonTokener = new JSONTokener(data);
                    //{"status":1,"msg":"\u4e0a\u4f20\u6210\u529f\uff01","data":null}
                    // data为空无法转换
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    img_1_path_int = jsonObject.getString("big_url");
                    str_name = user_name.getText().toString();
                    str_id = user_id.getText().toString();
                    Log.d(TAG, "上传正面完成: " + img_1_path_int + ", " + str_name + ", " + str_id);
                    //实名认证跟ios一样  210
                    HttpUplaodImage1(img_1_path_int);
                } catch (JSONException e) {
                    System.out.println(e.getLocalizedMessage());
                    progressDialog.dismiss();
                }
            }
        };
        upLoadImage_2SuccessHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = msg.getData().getString("data");
                try {
                    JSONTokener jsonTokener = new JSONTokener(data);
                    JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                    img_2_path_int = jsonObject.getString("big_url");
                    //实名认证跟ios一样  229
                    HttpUplaodImage2(img_2_path_int);
                    Log.d(TAG, "上传反面完成: " + img_2_path_int + ", " + str_name + ", " + str_id);
                } catch (JSONException e) {
                    System.out.println(e.getLocalizedMessage());
                    progressDialog.dismiss();
                }
            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ali_face_url = intent.getStringExtra("ali_face_url");
        ali_face_appcode = intent.getStringExtra("ali_face_appcode");
    }

    @Click
    void page_return() {
        this.finish();
    }

    /**
     * func:用户点击了‘身份证正面照片’进行上面照片选择和上传动作
     */
    @Click
    void user_card_1() {
        lacksPermission();
        AutenticationFragment autenticationFragment = AutenticationFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        autenticationFragment.setOnDialogItemClickListener(new AutenticationFragment.OnDialogItemClickListener() {
            @Override
            public void onLeftClick() {//正面拍照
                outputImage = new File(Environment.getExternalStorageDirectory(), "face.jpg");
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    //通过FileProvider.getUriForFile获取URL，参数2应该与Provider在AndroidManifest.xml中定义的authorities标签一致
                    imageUri = FileProvider.getUriForFile(MainAuthentication.this, "com.android.jidian.client.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //MediaStore.ACTION_IMAGE_CAPTURE对应android.media.action.IMAGE_CAPTURE，用于打开系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, HEADS_CAMERA);
            }

            @Override
            public void onRightClick() {//正面相册选择
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, HEADS_ALBUM);
            }
        });
        autenticationFragment.show(fragmentTransaction, "AUTHEN");
    }

    /**
     * func:用户点击了‘身份证反面照片’进行反面照片选择上传动作
     */
    @Click
    void user_card_2() {
        lacksPermission();
        AutenticationFragment autenticationFragment = AutenticationFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        autenticationFragment.setOnDialogItemClickListener(new AutenticationFragment.OnDialogItemClickListener() {
            @Override
            public void onLeftClick() {//反面拍照
                outputImage = new File(Environment.getExternalStorageDirectory(), "back.jpg");
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    //通过FileProvider.getUriForFile获取URL，参数2应该与Provider在AndroidManifest.xml中定义的authorities标签一致
                    imageUri = FileProvider.getUriForFile(MainAuthentication.this, "com.android.jidian.client.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                //MediaStore.ACTION_IMAGE_CAPTURE对应android.media.action.IMAGE_CAPTURE，用于打开系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAILS_CAMERA);
            }

            @Override
            public void onRightClick() {//反面相册
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, TAILS_ALBUM);
            }
        });
        autenticationFragment.show(fragmentTransaction, "AUTHEN");
    }

    @Click
    void submit() {
        str_name = user_name.getText().toString();
        str_id = user_id.getText().toString();
        //实名认证跟ios一样  579-582
        if (!TextUtils.isEmpty(img_1_path_int) && !TextUtils.isEmpty(img_2_path_int)) {
            HttpUplaodUserInfo(uid, str_name, selectType, str_id, img_1_path_int, img_2_path_int);
        }
    }

    // 判断是否缺少权限
    private void lacksPermission() {
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            int a = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (a == -1) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(activity, mPermissionList, 1);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionX.init(MainAuthentication.this)
                .permissions(Manifest.permission.CAMERA)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {

                    } else {
                        MyToast.showTheToast(this, "当前应用缺少必要权限 ");
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HEADS_ALBUM && resultCode == RESULT_OK && null != data) {
            Log.d(TAG, "HEADS_ALBUM");
            user_name.setText("");
            user_id.setText("");
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            img_1_path = cursor.getString(columnIndex);

            //图片上传，需要压缩一下
            int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(img_1_path, options);
            //计算inSampleSize
            options.inSampleSize = Util.calculateInSampleSize(options, requestWidth, requestWidth);
            //使用inSampleSize集解码位图
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(img_1_path, options);

            Matrix matrix = new Matrix();
            int width = bm.getWidth();
            int height = bm.getHeight();
            if (width < height) {
                matrix.postRotate(270); /*翻转90度*/
            }
            Bitmap img = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            user_card_1_img.setImageBitmap(img);
            isFirstPic = true;
            //实名认证跟ios一样   643
            HttpGetUpImageUrl(img_1_path, uid, upLoadImage_1SuccessHandler, errorHandler);
            cursor.close();
            progressDialog.show();
        }

        if (requestCode == TAILS_ALBUM && resultCode == RESULT_OK && null != data) {
            Log.d(TAG, "TAILS_ALBUM");
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            img_2_path = cursor.getString(columnIndex);

            //图片上传，需要压缩一下
            int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(img_2_path, options);
            //计算inSampleSize
            options.inSampleSize = Util.calculateInSampleSize(options, requestWidth, requestWidth);
            //使用inSampleSize集解码位图
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(img_2_path, options);

            Matrix matrix = new Matrix();
            int width = bm.getWidth();
            int height = bm.getHeight();
            if (width < height) {
                matrix.postRotate(270); /*翻转90度*/
            }
            Bitmap img = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            user_card_2_img.setImageBitmap(img);
            isFirstPic = false;
            //实名认证跟ios一样 682
            HttpGetUpImageUrl(img_2_path, uid, upLoadImage_2SuccessHandler, errorHandler);
            cursor.close();
            progressDialog.show();
        }

        if (requestCode == HEADS_CAMERA && resultCode == RESULT_OK) {
            Log.d(TAG, "HEADS_CAMERA");
            user_name.setText("");
            user_id.setText("");
            if (outputImage != null) {
                img_1_path = outputImage.getAbsolutePath();
                //图片上传，需要压缩一下
                int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(img_1_path, options);
                //计算inSampleSize
                options.inSampleSize = Util.calculateInSampleSize(options, requestWidth, requestWidth);
                //使用inSampleSize集解码位图
                options.inJustDecodeBounds = false;
                Bitmap bm = BitmapFactory.decodeFile(img_1_path, options);

                Matrix matrix = new Matrix();
                int width = bm.getWidth();
                int height = bm.getHeight();
                if (width < height) {
                    matrix.postRotate(270); /*翻转90度*/
                }
                Bitmap img = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
                user_card_1_img.setImageBitmap(img);
                isFirstPic = true;
                //实名认证跟ios一样  720
                HttpGetUpImageUrl(img_1_path, uid, upLoadImage_1SuccessHandler, errorHandler);
                progressDialog.show();
            }
        }

        if (requestCode == TAILS_CAMERA && resultCode == RESULT_OK) {
            Log.d(TAG, "TAILS_CAMERA");
            if (outputImage != null) {
                img_2_path = outputImage.getAbsolutePath();
                //图片上传，需要压缩一下
                int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(img_2_path, options);
                //计算inSampleSize
                options.inSampleSize = Util.calculateInSampleSize(options, requestWidth, requestWidth);
                //使用inSampleSize集解码位图
                options.inJustDecodeBounds = false;
                Bitmap bm = BitmapFactory.decodeFile(img_2_path, options);

                Matrix matrix = new Matrix();
                int width = bm.getWidth();
                int height = bm.getHeight();
                if (width < height) {
                    matrix.postRotate(270); /*翻转90度*/
                }
                Bitmap img = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
                user_card_2_img.setImageBitmap(img);
                isFirstPic = false;
                //实名认证跟ios一样 753
                HttpGetUpImageUrl(img_2_path, uid, upLoadImage_2SuccessHandler, errorHandler);
                progressDialog.show();
            }
        }
    }

    private void sendMessage(Handler handler, String str) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("msg", str);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void sendMessage(Handler handler, String str, String data) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("msg", str);
        bundle.putString("data", data);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Background
    void HttpGetUpImageUrl(String imageUrl, String uid, Handler successHandler, Handler errorHandler) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("token", Md5.getAptk()));
        new OkHttpConnect(activity, "https://appx.mixiangx.com/Upload/uploadUrlSet.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetUpImageUrl(response, type, imageUrl, uid, successHandler, errorHandler);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetUpImageUrl(String response, String type, String imageUrl, String uid, Handler successHandler, Handler errorHandler) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            HttpGetUpImageUrlBean bean = new Gson().fromJson(response, HttpGetUpImageUrlBean.class);
            if (bean != null) {
                if ("1".equals(bean.getStatus())) {
                    if (bean.getData() != null) {
                        if (!TextUtils.isEmpty(bean.getData().getUpfurl())) {
                            UploadImage_A(imageUrl, uid, successHandler, errorHandler, bean.getData().getUpfurl());
                        }
                    }
                } else {
                    MyToast.showTheToast(activity, bean.getMsg());
                }
            }
        }
    }

    @Background
    void UploadImage_A(String imageUrl, String uid, Handler successHandler, Handler errorHandler, String url) {
//        String urlStr = "https://uploader.halouhuandian.com/Upload/upImage.html";
        Map<String, String> textMap = new HashMap<>();
        textMap.put("uid", uid);
        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("upFile", imageUrl);
        String ret = formUpload(url, textMap, fileMap);
        Util.d("2020020501", ret);
        JSONTokener jsonTokener = new JSONTokener(ret);
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonTokener.nextValue();
            String code = jsonObject.getString("status");
            String messageString = jsonObject.getString("msg");
            if ("1".equals(code)) {
                if (jsonObject.has("data")) {
                    String data = jsonObject.getString("data");
                    sendMessage(successHandler, messageString, data);
                } else {
                    sendMessage(successHandler, messageString);
                }
            } else {
                sendMessage(errorHandler, messageString);
            }
        } catch (JSONException e) {
            System.out.println(e.getLocalizedMessage());
            progressDialog.dismiss();
        }
    }

    public String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.setRequestProperty("apud", UserInfoHelper.getInstance().getUid());
            conn.setRequestProperty("access", UserInfoHelper.getInstance().getAccess());
            conn.setRequestProperty("aptk", Md5.getAptk());
            conn.setRequestProperty("verName", getLocalVersionName(MyApplication.getContext()));
            conn.setRequestProperty("version", String.valueOf(getLocalVersion(MyApplication.getContext())));
            conn.setRequestProperty("osname", "Android");
            conn.setRequestProperty("proname", "app");
            conn.setRequestProperty("company", "jidianlvtong");

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuilder strBuf = new StringBuilder();
                for (Map.Entry<String, String> entry : textMap.entrySet()) {
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"\r\n\r\n");
                    strBuf.append(inputValue);
                    Util.d("2020020601", strBuf.toString());
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    //图片上传，需要压缩一下
                    int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
                    //首先使用inJustDecodeBounds = true进行解码以检查尺寸
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(inputValue, options);
                    //计算inSampleSize
                    options.inSampleSize = Util.calculateInSampleSize(options, requestWidth, requestWidth);
                    //使用inSampleSize集解码位图
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(inputValue, options);
                    File file = saveBitmapFile(new File(inputValue), bitmap);
                    //TODO 对图片进行压缩
                    String filename = file.getName();
                    String strBuf = "\r\n" + "--" + BOUNDARY + "\r\n" +
                            "Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n" +
                            "Content-Type:" + "image/jpeg" + "\r\n\r\n";
                    Util.d("2020020602", strBuf);
                    out.write(strBuf.getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            Util.d("2020020603", new String(endData));
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuilder strBuf = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
        } catch (Exception e) {
            sendMessage(errorHandler, "发送POST请求出错。" + urlStr);
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    public File saveBitmapFile(File file, Bitmap bitmap) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
        }
        return file;
    }

    @Background
    void HttpUplaodUserInfo(String uid, String name, String typer, String id, String front, String reverse) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("real_name", name));
        dataList.add(new ParamTypeData("typer", typer));
        dataList.add(new ParamTypeData("card", id));
        dataList.add(new ParamTypeData("front", front));
        dataList.add(new ParamTypeData("reverse", reverse));
        new OkHttpConnect(activity, PubFunction.app + "User/identity.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onHttpUplaodUserInfo(response, type);
            }
        }).startHttpThread();
    }

    @UiThread
    void onHttpUplaodUserInfo(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
            progressDialog.dismiss();
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    this.finish();
                    Log.d(TAG, "上传用户信息成功: " + msg);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth", "1");
                    editor.apply();
                }
                MyToast.showTheToast(activity, msg);
            } catch (Exception e) {
                MyToast.showTheToast(activity, "上传用户信息失败：" + e.getLocalizedMessage());
            } finally {
                progressDialog.dismiss();
            }
        }
    }

    /**
     * func:上传身份证号码和用户姓名给聚合进行身份信息验证
     *
     * @param num  身份证号
     * @param name 身份证姓名
     */
    @Background
    void HttpUplaodIdName(String num, String name) {
        List<ParamTypeData> dataList = new ArrayList<>();
        String path = "http://op.juhe.cn/idcard/query?key=138f605e59efab7fc41fabc38e568e6e&idcard=" + num + "&realname=" + name;
        new OkHttpConnect(activity, path, dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpUplaodIdName(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpUplaodIdName(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.getString("error_code");
                if ("0".equals(code)) {
                    JSONObject dataObject = jsonObject.getJSONObject("result");
                    String res = dataObject.getString("res");
                    if ("1".equals(res)) {
                        MyToast.showTheToast(activity, "身份证信息正确！");
                    } else {
                        MyToast.showTheToast(activity, "身份证信息错误！");
                        user_name.setText("");
                        user_id.setText("");
                        if (isFirstPic) {
                            user_card_1_img.setImageBitmap(null);
                            img_1_path = "";
                        } else {
                            user_card_2_img.setImageBitmap(null);
                            img_2_path = "";
                        }
                    }
                } else {
                    String res = jsonObject.getString("reason");
                    MyToast.showTheToast(activity, "身份证信息错误！" + res);
                    user_name.setText("");
                    user_id.setText("");
                    if (isFirstPic) {
                        user_card_1_img.setImageBitmap(null);
                        img_1_path = "";
                    } else {
                        user_card_2_img.setImageBitmap(null);
                        img_2_path = "";
                    }
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON：" + e.toString());
                user_name.setText("");
                user_id.setText("");
                if (isFirstPic) {
                    user_card_1_img.setImageBitmap(null);
                    img_1_path = "";
                } else {
                    user_card_2_img.setImageBitmap(null);
                    img_2_path = "";
                }
            }
        }
    }

    //实名认证跟ios一样  1041-1132
    @Background
    void HttpUplaodImage1(String big_url) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("file", big_url));
        new OkHttpConnect(activity, PubFunction.api + "User/authFaceCard.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onHttpUplaodImage1(response, type);
            }
        }).startHttpThread();
    }

    @UiThread
    void onHttpUplaodImage1(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
            progressDialog.dismiss();
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    String realname = jsonObject_response.getString("realname");
                    String idcard = jsonObject_response.getString("idcard");
                    user_name.setText(realname);
                    user_id.setText(idcard);
                    user_name.setEnabled(true);
                    user_id.setEnabled(true);
                    Log.d(TAG, "上传正面成功: " + msg);
                }
                MyToast.showTheToast(activity, msg);
            } catch (Exception e) {
                MyToast.showTheToast(activity, "上传正面失败：" + e.getLocalizedMessage());
            } finally {
                progressDialog.dismiss();
            }
        }
    }

    @Background
    void HttpUplaodImage2(String big_url) {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("file", big_url));
        new OkHttpConnect(activity, PubFunction.api + "User/authBackCard.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onHttpUplaodImage2(response, type);
            }
        }).startHttpThread();
    }

    @UiThread
    void onHttpUplaodImage2(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
            progressDialog.dismiss();
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    Log.d(TAG, "上传背面成功: " + msg);
                }
                MyToast.showTheToast(activity, msg);
            } catch (Exception e) {
                MyToast.showTheToast(activity, "上传背面失败：" + e.getLocalizedMessage());
            } finally {
                progressDialog.dismiss();
            }
        }
    }
}