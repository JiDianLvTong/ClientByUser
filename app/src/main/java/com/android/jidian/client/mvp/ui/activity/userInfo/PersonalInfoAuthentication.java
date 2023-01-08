package com.android.jidian.client.mvp.ui.activity.userInfo;

import static com.android.jidian.client.http.HeaderTypeData.getLocalVersion;
import static com.android.jidian.client.http.HeaderTypeData.getLocalVersionName;

import android.Manifest;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import com.android.jidian.client.MainAuthentication;
import com.android.jidian.client.R;
import com.android.jidian.client.application.MyApplication;
import com.android.jidian.client.base.PermissionManager.PermissionManager;
import com.android.jidian.client.base.U6BaseActivity;
import com.android.jidian.client.bean.HttpGetUpImageUrlBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.dialog.AutenticationFragment;
import com.android.jidian.client.mvp.ui.dialog.DialogByEnter;
import com.android.jidian.client.mvp.ui.dialog.DialogByIdCardChoice;
import com.android.jidian.client.pub.Md5;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.UserInfoHelper;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.util.bitmap.BitmapManager;
import com.android.jidian.client.widgets.MyToast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;
import com.wildma.idcardcamera.camera.IDCardCamera;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.UiThread;
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

import butterknife.BindView;
import butterknife.OnClick;

//todo: 代码待优化

public class PersonalInfoAuthentication extends U6BaseActivity {



    @BindView(R.id.userNameView)
    public TextView userNameView;
    @BindView(R.id.userIdView)
    public TextView userIdView;
    @BindView(R.id.userCardFrontImgView)
    public ImageView userCardFrontImgView;
    @BindView(R.id.userCardBackImgView)
    public ImageView userCardBackImgView;
    @BindView(R.id.submitView)
    public TextView submitView;

    private int uploadImageType = -1;

    private static final String TAG = "MainAuthentication1";

    private String img_1_path = "", img_2_path = "";
    private String img_1_path_int = "", img_2_path_int = "";
    private String str_name = "", str_id = "";
    private String selectType = "身份证";
    private Uri imageUri;
    private File outputImage;
    private boolean isFirstPic = true;//判断是正面照还是反面照，用于照片信息错误后清空imageview
    //上传正面和反面给阿里使用的都是uplaodAliyunFaceSuccessHandler；上传正面给服务器是upLoadImage_1SuccessHandler，反面是upLoadImage_2SuccessHandler
    private Handler errorHandler, upLoadImage_1SuccessHandler, upLoadImage_2SuccessHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        setContentView(R.layout.u6_activity_personal_info_authentication);
        super.onCreate(savedInstanceState);
        init();
    }


    private void init(){
        handler();
    }

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
                    str_name = userNameView.getText().toString();
                    str_id = userIdView.getText().toString();
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

    @OnClick(R.id.user_card_1)
    void onClickUserCard1(){
        pickPicType(1);
    }

    @OnClick(R.id.user_card_2)
    void onClickUserCard2(){
        pickPicType(2);
    }

    @OnClick(R.id.submitView)
    void onClickSubmitView(){
        str_name = userNameView.getText().toString();
        str_id = userIdView.getText().toString();
        if (!TextUtils.isEmpty(img_1_path_int) && !TextUtils.isEmpty(img_2_path_int)) {
            HttpUplaodUserInfo(uid, str_name, selectType, str_id, img_1_path_int, img_2_path_int);
        }else{
            DialogByEnter dialog = new DialogByEnter(activity , "请先上传照片完善信息");
            dialog.showPopupWindow();
        }
    }


    private void pickPicType(int type){
        uploadImageType = type;
        PermissionManager.getInstance().getCameraAndWrite(activity, new PermissionManager.PermissionListener() {
            @Override
            public void granted(List<String> grantedList) {
                DialogByIdCardChoice dialogByIdCardChoice = new DialogByIdCardChoice(activity, new DialogByIdCardChoice.DialogCardChoiceListener() {
                    @Override
                    public void cameraReturn() {
                        if(type == 1){
                            IDCardCamera.create(activity).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                        }else if(type == 2){
                            IDCardCamera.create(activity).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
                        }
                    }

                    @Override
                    public void albumReturn() {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if(type == 1){
                            startActivityForResult(i, 3);
                        }else if(type == 2){
                            startActivityForResult(i, 4);
                        }
                    }
                });
                dialogByIdCardChoice.showPopupWindow();
            }

            @Override
            public void refused(List<String> refusedList) {
                DialogByEnter dialog = new DialogByEnter(activity, "当前功能缺少必要权限");
                dialog.showPopupWindow();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    setImage(1,BitmapFactory.decodeFile(path));
                    HttpGetUpImageUrl(path, uid);
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    setImage(2,BitmapFactory.decodeFile(path));
                    HttpGetUpImageUrl(path, uid);
                }
            }
        }
        if ((requestCode == 3 || requestCode == 4) && resultCode == RESULT_OK && null != data) {

            //获取图片
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);

            //图片上传，需要压缩一下
            int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgPath, options);
            //计算inSampleSize
            options.inSampleSize = BitmapManager.calculateInSampleSize(options, requestWidth, requestWidth);
            //使用inSampleSize集解码位图
            options.inJustDecodeBounds = false;
            Bitmap bm = BitmapFactory.decodeFile(imgPath, options);

            /*翻转90度*/
            Matrix matrix = new Matrix();
            int width = bm.getWidth();
            int height = bm.getHeight();
            if (width < height) {
                matrix.postRotate(270);
            }
            Bitmap img = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

            if(requestCode == 3){
                setImage(1,img);
                HttpGetUpImageUrl(imgPath, uid);
            }else if(requestCode == 4){
                setImage(2,img);
                HttpGetUpImageUrl(imgPath, uid);
            }
            cursor.close();
        }
    }

    private void setImage(int type,Bitmap bitmap){
        if(type == 1){
            userCardFrontImgView.setImageBitmap(bitmap);
            userCardFrontImgView.setVisibility(View.VISIBLE);
        }else if(type == 2){
            userCardBackImgView.setImageBitmap(bitmap);
            userCardBackImgView.setVisibility(View.VISIBLE);
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


    private void HttpGetUpImageUrl(String imageUrl, String uid){
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("token", Md5.getAptk()));
        new OkHttpConnect(activity, "https://appx.mixiangx.com/Upload/uploadUrlSet.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetUpImageUrl(response, type, imageUrl, uid);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }


    private void onDataHttpGetUpImageUrl(String response, String type, String imageUrl, String uid) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            HttpGetUpImageUrlBean bean = new Gson().fromJson(response, HttpGetUpImageUrlBean.class);
            if (bean != null) {
                if ("1".equals(bean.getStatus())) {
                    if (bean.getData() != null) {
                        if (!TextUtils.isEmpty(bean.getData().getUpfurl())) {
                            UploadImage_A(imageUrl, uid, bean.getData().getUpfurl());
                        }
                    }
                } else {
                    MyToast.showTheToast(activity, bean.getMsg());
                }
            }
        }
    }

    void UploadImage_A(String imageUrl, String uid, String url) {
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
                    if(uploadImageType == 1){
                        sendMessage(upLoadImage_1SuccessHandler, messageString, data);
                    }else if(uploadImageType == 2){
                        sendMessage(upLoadImage_2SuccessHandler, messageString, data);
                    }
                } else {
                    if(uploadImageType == 1){
                        sendMessage(upLoadImage_1SuccessHandler, messageString);
                    }else if(uploadImageType == 2){
                        sendMessage(upLoadImage_2SuccessHandler, messageString);
                    }
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

    //实名认证跟ios一样  1041-1132
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

    void onHttpUplaodImage1(String response, String type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                            userNameView.setText(realname);
                            userIdView.setText(idcard);
                            userNameView.setEnabled(true);
                            userIdView.setEnabled(true);
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
        });


    }

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

    void onHttpUplaodImage2(String response, String type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    @OnClick(R.id.pageReturn)
    void onClickPageReturn(){
        activity.finish();
    }

}
