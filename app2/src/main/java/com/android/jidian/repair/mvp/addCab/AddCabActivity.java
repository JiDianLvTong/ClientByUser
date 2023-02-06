package com.android.jidian.repair.mvp.addCab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jidian.repair.PubFunction;
import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivity;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.PermissionManager.PermissionManager;
import com.android.jidian.repair.dialog.DialogByEnter;
import com.android.jidian.repair.mvp.addCab.AddCabActivity;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.utils.Md5;
import com.android.jidian.repair.utils.RSAUtil;
import com.android.jidian.repair.utils.UserInfoHelper;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.permissionx.guolindev.PermissionX;
import com.timmy.tdialog.TDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;

public class AddCabActivity extends BaseActivityByMvp<AddCabPresenter> implements AddCabContract.View {

    private static final int ADD_IMAGE_IMG_1 = 101;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.information_webView)
    DWebView information_webView;
    private String baseUrl;
    private String tid = null;//机柜id，站点编辑用
    private String id = null;//查看巡检（出厂或每周）用的id
    private String title;
    private Object data1;
    private CompletionHandler<String> callback1;
    private File cameraFile;
    private Uri imageUri;
    private List<String> imagelist = new ArrayList<>();
    private String imagePath;

    //定位相关
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    public double[] coordinates = new double[]{0, 0};
    private AMapLocation aMapLocation;
    private boolean mPositioned = false;

    //权限相关
    private boolean permissionShow = false;
    private Double mLat, mLng;

    private String mWtid = "";
    private String mPath, mProj, mUpToken, mCompanyid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_cab;
    }

    @Override
    public void initView() {
        mPresenter = new AddCabPresenter();
        mPresenter.attachView(this);
        Intent intent = getIntent();
        baseUrl = PubFunction.ape + "Cabinet/cabinet/companyid/3";
//        tid = intent.getStringExtra("tid");
//        id = intent.getStringExtra("id");
        title = "上传电柜";

        mPresenter.requestUploadUploadUrlSet(Md5.getAptk());

        tv_title.setText(title);

        information_webView.addJavascriptObject(new JsApi(), null);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("phone", UserInfoHelper.getInstance().getPhone());
            jsonObject.put("name", UserInfoHelper.getInstance().getAdname());
            jsonObject.put("time", String.valueOf(System.currentTimeMillis()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String codeSrc = jsonObject.toString();
        try {
            //RSA公钥字符串转换为RSAPublicKey公钥
            RSAPublicKey rsaPublicKey = (RSAPublicKey) RSAUtil.convertToRSAPublicKey();
            //公钥加密数据
            byte[] resultSrc = RSAUtil.publicEncrpty(codeSrc.getBytes(), rsaPublicKey);
            //base64进行2次编码，服务器格式要求
            byte[] resultDes = RSAUtil.byte2Base64(resultSrc);
            byte[] resultDesReal = RSAUtil.byte2Base64(resultDes);
//                initWebSettings();
//                information_webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                        return true;
//                    }
//                });
            //服务器要求的URL格式
            String builder;
            if (id == null) {
                if (tid == null) {
                    builder = baseUrl + "/code/" + new String(resultDesReal);
                } else {
                    builder = baseUrl + "/tid/" + tid + "/code/" + new String(resultDesReal);
                }
            } else {
                if (tid == null) {
                    builder = baseUrl + "/id/" + id + "/code/" + new String(resultDesReal);
                } else {
                    builder = baseUrl + "/id/" + id + "/tid/" + tid + "/code/" + new String(resultDesReal);
                }
            }
//                information_webView.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                            return true;
//                        }
//                        return false;
//                    }
//                });
            information_webView.getSettings().setGeolocationEnabled(true);
            information_webView.getSettings().setJavaScriptEnabled(true);
//                information_webView.setWebChromeClient(new WebChromeClient());
            information_webView.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient());
//                information_webView.setWebChromeClient(new WebChromeClient(){
//                    @Override
//                    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                        callback.invoke(origin, true, false);
//                        super.onGeolocationPermissionsShowPrompt(origin, callback);
//                    }
//
//                });
            information_webView.loadUrl(builder);
            information_webView.setWebContentsDebuggingEnabled(true);
            Log.e("enenenenn", builder);
//                Toast.makeText(activity, "id: " + id + ", tid: " + tid, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn() {
        finish();
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @Override
    public void requestUploadUploadUrlSetSuccess(UploadUploadUrlSetBean.DataBean bean) {
        PubFunction.upload = bean.getDomain();
        mPath = bean.getPath();
        mCompanyid = bean.getCompanyid();
        mProj = bean.getProj();
        mUpToken = bean.getUpToken();
    }

    @Override
    public void requestUpLoadImgSuccess(UploadImageBean bean, int index) {
        if (bean.getData() != null) {
            if (ADD_IMAGE_IMG_1 == index) {
                callback1.complete(bean.getData().getFurl());
//                mSolveImageList[0] = bean.getData().getFurl();
//                Glide.with(AddCabActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg1);
            }
//            else if (ADD_SOLVE_IMG_2 == index) {
//                mSolveImageList[1] = bean.getData().getFurl();
//                Glide.with(AddCabActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg2);
//            } else if (ADD_SOLVE_IMG_3 == index) {
//                mSolveImageList[2] = bean.getData().getFurl();
//                Glide.with(AddCabActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg3);
//            } else if (ADD_SOLVE_IMG_4 == index) {
//                mSolveImageList[3] = bean.getData().getFurl();
//                Glide.with(AddCabActivity.this).load(bean.getData().getFurl()).error(R.drawable.icon_image_broken).into(ivTaskSolveImg4);
//            }
        } else {
            showMessage(bean.getMsg());
        }
    }

    private void initLocation() {
        try {
            mLocationClient = new AMapLocationClient(AddCabActivity.this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //设置定位监听
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation mAMapLocation) {
                    if (mAMapLocation != null) {
                        aMapLocation = mAMapLocation;
                        if (aMapLocation.getErrorCode() == 0) {
                            //定位成功回调信息，设置相关消息
                            mPositioned = true;
                            aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                            mLng = aMapLocation.getLongitude();//获取经度
                            mLat = aMapLocation.getLatitude();//获取纬度
                            Log.d("xiaoming0203", "onLocationChanged:  aMapLocation.getCity(): " + aMapLocation.getAddress() + "aMapLocation.getCityCode(): " + aMapLocation.getCityCode() + "aMapLocation.getLatitude() :" + aMapLocation.getLatitude() + "aMapLocation.getLongitude():" + aMapLocation.getLongitude());
                        } else {
                            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                            Log.e(Tag, "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
            //启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {
//            Log.e(Tag, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(0);
                    if (media != null) {
                        String compressPath = media.getCompressPath();
                        if (mPresenter != null) {
                            if (!TextUtils.isEmpty(mPath)) {
                                mPresenter.requestUpLoadImg(mPath, compressPath, mUpToken, mCompanyid, requestCode);
                            } else {
                                showMessage("出错了，请重新选择~");
                                mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        initLocation();

                    } else {
                        DialogByEnter dialog = new DialogByEnter(activity, "当前应用缺少必要权限,会影响部分功能使用！");
                        dialog.showPopupWindow();
                    }
                });
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }

    private void pushPhotoUrl(Object data, CompletionHandler<String> callback) {
        PictureSelectorUtils.addPhotoByCameraAndAlbum(AddCabActivity.this, ADD_IMAGE_IMG_1);
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_avatar, null);
//        TextView tv_open_album = view.findViewById(R.id.tv_open_album);
//        View line2 = view.findViewById(R.id.line2);
//        if (baseUrl.equals(DOMAIN_STRING + "Patrol/addweek") || baseUrl.equals(DOMAIN_STRING + "Patrol/addfactory")) {
//            //每周巡检不准相册选择
//            tv_open_album.setVisibility(View.GONE);
//            line2.setVisibility(View.GONE);
//        } else {
//            tv_open_album.setVisibility(View.VISIBLE);
//            line2.setVisibility(View.VISIBLE);
//        }
//        new TDialog.Builder(getSupportFragmentManager())
//                .setDialogView(view)
////                .setLayoutRes(R.layout.dialog_change_avatar)
//                .setScreenWidthAspect(this, 1.0f)
//                .setGravity(Gravity.BOTTOM)
//                .setDialogAnimationRes(R.style.animate_dialog)
//                .addOnClickListener(R.id.tv_open_camera, R.id.tv_open_album, R.id.tv_cancel)
//                .setOnViewClickListener(new OnViewClickListener() {
//                    @Override
//                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                        switch (view.getId()) {
//                            case R.id.tv_open_camera:
//                                cameraMethod();
//                                tDialog.dismiss();
//                                break;
//                            case R.id.tv_open_album:
//                                photoMethod();
//                                tDialog.dismiss();
//                                break;
//                            case R.id.tv_cancel:
//                                tDialog.dismiss();
//                                break;
//                        }
//                    }
//                })
//                .create()
//                .show();
    }

    public class JsApi {
        @JavascriptInterface
        public void testObjcCallback(Object msg, CompletionHandler<String> handler) throws JSONException {
//            data1 = data;
            callback1 = handler;

            PermissionX.init(activity)
                    .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                    .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                    .request((allGranted, grantedList, deniedList) -> {
                        if (allGranted) {
                            pushPhotoUrl(data1, callback1);
                        } else {
                            DialogByEnter dialog = new DialogByEnter(activity, "当前应用缺少必要权限,会影响部分功能使用！");
                            dialog.showPopupWindow();
                        }
                    });
        }

        @JavascriptInterface
        public void fanhui(Object msg, CompletionHandler<String> handler) throws JSONException {
            finish();
        }

        @JavascriptInterface
        public void mapCallback(Object msg, CompletionHandler<JSONObject> handler) throws JSONException {
            Map map = new HashMap();
            map.put("lon", mLng);
            map.put("lat", mLat);
            JSONObject jsonObject1 = new JSONObject(map);
            handler.complete(jsonObject1);
        }

    }
}