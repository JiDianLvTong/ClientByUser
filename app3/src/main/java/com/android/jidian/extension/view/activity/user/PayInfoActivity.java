package com.android.jidian.extension.view.activity.user;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.jidian.extension.R;
import com.android.jidian.extension.base.BaseActivity;
import com.android.jidian.extension.bean.LoginGetUserInfoBean;
import com.android.jidian.extension.bean.PayInfoGetUploadImagePathBean;
import com.android.jidian.extension.bean.PayInfoUploadImageBean;
import com.android.jidian.extension.dao.sp.UserInfoSp;
import com.android.jidian.extension.net.BaseHttp;
import com.android.jidian.extension.net.BaseHttpParameterFormat;
import com.android.jidian.extension.net.HttpUploadImage;
import com.android.jidian.extension.net.HttpUrlMap;
import com.android.jidian.extension.util.BitmapManager;
import com.android.jidian.extension.view.activity.login.LoginActivity;
import com.android.jidian.extension.view.activity.main.MainActivity;
import com.android.jidian.extension.view.commonPlug.dialog.DialogByEnter;
import com.android.jidian.extension.view.commonPlug.dialog.DialogCardChoice;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayInfoActivity extends BaseActivity {

    @BindView(R.id.infoPanel)
    public LinearLayout infoPanel;
    @BindView(R.id.editPanel)
    public LinearLayout editPanel;

    @BindView(R.id.i_t_0)
    public TextView i_t_0;
    @BindView(R.id.i_t_1)
    public TextView i_t_1;
    @BindView(R.id.i_t_2)
    public TextView i_t_2;
    @BindView(R.id.i_t_3)
    public TextView i_t_3;
    @BindView(R.id.i_l_1_image)
    public ImageView i_l_1_image;
    @BindView(R.id.i_l_2_image)
    public ImageView i_l_2_image;
    @BindView(R.id.intoEdit)
    public ImageView intoEdit;


    @BindView(R.id.l_1_add)
    public FrameLayout l_1_add;
    @BindView(R.id.l_2_add)
    public FrameLayout l_2_add;
    @BindView(R.id.l_1_image)
    public ImageView l_1_image;
    @BindView(R.id.l_2_image)
    public ImageView l_2_image;
    @BindView(R.id.infoSubmit)
    public TextView infoSubmit;
    @BindView(R.id.t_1)
    public EditText t_1;
    @BindView(R.id.t_2)
    public EditText t_2;
    @BindView(R.id.t_3)
    public EditText t_3;

    private PayInfoGetUploadImagePathBean payInfoGetUploadImagePathBean;

    private Handler showLoadingHandler;
    private Handler dismissLoadingHandler;
    private String fontImageUrl = "";
    private String backImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_info);
        ButterKnife.bind(activity);
        init();
    }

    private void init(){
        String status = UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.avater);
        if(status.equals("3")){
            editPanel.setVisibility(View.VISIBLE);
            infoPanel.setVisibility(View.GONE);
        }else{

            if(status.equals("-1") && status.equals("1")){
                intoEdit.setVisibility(View.VISIBLE);
            }else{
                intoEdit.setVisibility(View.GONE);
            }

            editPanel.setVisibility(View.GONE);
            infoPanel.setVisibility(View.VISIBLE);
            i_t_0.setText(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.astater));
            i_t_1.setText(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.realname));
            i_t_2.setText(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.idcard));
            i_t_3.setText(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.zfb));
            Picasso.get().load(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.ucardfurl)).into(i_l_1_image);
            Picasso.get().load(UserInfoSp.getInstance().getUserInfoData(UserInfoSp.UserInfoEnum.ucardzurl)).into(i_l_2_image);
        }

        showLoadingHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(dialogLoading!=null){
                    dialogLoading.showPopupWindow();
                }
            }
        };
        dismissLoadingHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(dialogLoading!=null){
                    dialogLoading.dismiss();
                }
            }
        };
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn(){
        activity.finish();
    };

    @OnClick({R.id.l_1_add})
    public void onClickL_1_Add(){
        new DialogCardChoice(activity, new DialogCardChoice.DialogCardChoiceListener() {
            @Override
            public void cameraReturn() {
                IDCardCamera.create(activity).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
            }

            @Override
            public void albumReturn() {
                openImageUtils(3);
            }
        }).showPopupWindow();
    }

    @OnClick({R.id.l_2_add})
    public void onClickL_2_Add(){
        new DialogCardChoice(activity, new DialogCardChoice.DialogCardChoiceListener() {
            @Override
            public void cameraReturn() {
                IDCardCamera.create(activity).openCamera(IDCardCamera.TYPE_IDCARD_BACK);
            }

            @Override
            public void albumReturn() {
                openImageUtils(4);
            }
        }).showPopupWindow();
    }

    @OnClick({R.id.infoSubmit})
    public void onClickInfoSubmit(){
        dialogLoading.showPopupWindow();
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid",UserInfoSp.getInstance().getId()));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("realname",t_1.getText().toString()));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("idcard",t_2.getText().toString()));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("zhifubao",t_3.getText().toString()));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("ucardzurl",fontImageUrl));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("ucardfurl",backImageUrl));
        BaseHttp baseHttp = new BaseHttp(activity, HttpUrlMap.getUploadUserInfo, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogLoading.dismiss();
                        if(code == 1){
                            new DialogByEnter(activity, message, new DialogByEnter.DialogChoiceListener() {
                                @Override
                                public void enterReturn() {
                                    activity.finish();
                                }
                            }).showPopupWindow();

                        }else{
                            new DialogByEnter(activity,message).showPopupWindow();
                        }
                    }
                });
            }
        });
        baseHttp.onStart();
    }

    @OnClick({R.id.intoEdit})
    public void onClickIntoEdit(){
        editPanel.setVisibility(View.VISIBLE);
        infoPanel.setVisibility(View.GONE);
    }

    private void openImageUtils(int code) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
                    setImage(1, BitmapFactory.decodeFile(path));
                    uploadImage(path,0);
                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
                    setImage(2,BitmapFactory.decodeFile(path));
                    uploadImage(path,1);
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
                uploadImage(imgPath , 0);
            }else if(requestCode == 4){
                setImage(2,img);
                uploadImage(imgPath , 1);
            }
            cursor.close();
        }
    }

    private void setImage(int type,Bitmap bitmap){
        if(type == 1){
            l_1_image.setImageBitmap(bitmap);
            l_1_image.setVisibility(View.VISIBLE);
        }else if(type == 2){
            l_2_image.setImageBitmap(bitmap);
            l_2_image.setVisibility(View.VISIBLE);
        }
    }

    private void uploadImage(String imageUrl, int type) {
        showLoadingHandler.sendMessage(new Message());
        List<BaseHttpParameterFormat> baseHttpParameterFormats = new ArrayList<>();
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("uid", UserInfoSp.getInstance().getId()));
        baseHttpParameterFormats.add(new BaseHttpParameterFormat("token",UserInfoSp.getInstance().getUpToken()));
        BaseHttp baseHttp = new BaseHttp(activity, HttpUrlMap.getUploadImagePath, baseHttpParameterFormats, new BaseHttp.BaseHttpListener() {
            @Override
            public void dataReturn(int code, String errorMessage , String message , String data) {
                if(code == 1){
                    payInfoGetUploadImagePathBean = new Gson().fromJson(data , PayInfoGetUploadImagePathBean.class);
                    String uid = UserInfoSp.getInstance().getId();
                    Map<String, String> fileMap = new HashMap<>();
                    fileMap.put("upFile", imageUrl);
                    HttpUploadImage.getInstance().upload(payInfoGetUploadImagePathBean.getUpfurl(), uid, fileMap, new HttpUploadImage.HttpUploadImageListener() {
                        @Override
                        public void returnListener(int status, String msg, String data) {
                            dismissLoadingHandler.sendMessage(new Message());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(status == 1) {
                                        if (type == 0) {
                                            fontImageUrl = data;
                                        } else if (type == 1) {
                                            backImageUrl = data;
                                        }
                                        new DialogByEnter(activity, "上传成功").showPopupWindow();
                                    } else{
                                        if(type == 0){
                                            l_1_image.setImageBitmap(null);
                                            l_1_image.setVisibility(View.GONE);
                                        }else{
                                            l_2_image.setImageBitmap(null);
                                            l_2_image.setVisibility(View.GONE);
                                        }
                                        new DialogByEnter(activity , msg).showPopupWindow();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    new DialogByEnter(activity , "获取上传路径失败，请稍候再试").showPopupWindow();
                }
            }
        });
        baseHttp.onStart();
    }
}
