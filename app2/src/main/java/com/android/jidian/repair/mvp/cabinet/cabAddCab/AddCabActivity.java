package com.android.jidian.repair.mvp.cabinet.cabAddCab;

import static com.android.jidian.repair.utils.picture.PictureUtil.calculateInSampleSize;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.jidian.repair.PubFunction;
import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BindEventBus;
import com.android.jidian.repair.dao.sp.UserInfoSp;
import com.android.jidian.repair.utils.picture.BitmapManager;
import com.android.jidian.repair.widgets.dialog.DialogByEnter;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.utils.Md5;
import com.android.jidian.repair.utils.RSAUtil;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;
import com.permissionx.guolindev.PermissionX;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;

@BindEventBus
public class AddCabActivity extends BaseActivityByMvp<AddCabPresenter> implements AddCabContract.View {

    public static final int ADD_IMAGE_IMG_CAMERA = 101;
    public static final int ADD_IMAGE_ABLBUM = 102;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.information_webView)
    DWebView information_webView;
    private String baseUrl;
    private String tid = null;//??????id??????????????????
    private String id = null;//???????????????????????????????????????id
    private String title;
    private Object data1;
    private CompletionHandler<String> callback1;
    private File cameraFile;
    private Uri imageUri;
    private List<String> imagelist = new ArrayList<>();
    private String imagePath;

    //????????????
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption = null;
    public double[] coordinates = new double[]{0, 0};
    private AMapLocation aMapLocation;
    private boolean mPositioned = false;

    //????????????
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
        baseUrl = intent.getStringExtra("baseUrl") + "/companyid/3";
        tid = intent.getStringExtra("tid");
//        id = intent.getStringExtra("id");
        title = "????????????";

        mPresenter.requestUploadUploadUrlSet(Md5.getAptk());

        tv_title.setText(title);

        information_webView.addJavascriptObject(new JsApi(), null);
        initLocation();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("phone", UserInfoSp.getInstance().getPhone());
            jsonObject.put("name", UserInfoSp.getInstance().getName());
            jsonObject.put("time", String.valueOf(System.currentTimeMillis()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String codeSrc = jsonObject.toString();
        try {
            //RSA????????????????????????RSAPublicKey??????
            RSAPublicKey rsaPublicKey = (RSAPublicKey) RSAUtil.convertToRSAPublicKey();
            //??????????????????
            byte[] resultSrc = RSAUtil.publicEncrpty(codeSrc.getBytes(), rsaPublicKey);
            //base64??????2?????????????????????????????????
            byte[] resultDes = RSAUtil.byte2Base64(resultSrc);
            byte[] resultDesReal = RSAUtil.byte2Base64(resultDes);
//                initWebSettings();
//                information_webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                        return true;
//                    }
//                });
            //??????????????????URL??????
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddCabEvent event) {
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
//                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        PictureSelectorUtils.addPhotoByCameraAndAlbum(AddCabActivity.this);
//                        pushPhotoUrl(data1, callback1);
                    } else {
                        DialogByEnter dialog = new DialogByEnter(activity, "??????????????????????????????,??????????????????????????????");
                        dialog.showPopupWindow();
                    }
                });
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
//            if (ADD_IMAGE_IMG_1 == index) {
            callback1.complete(bean.getData().getFurl());
//            }
        } else {
            showMessage(bean.getMsg());
        }
    }

    private void initLocation() {
        try {
            mLocationClient = new AMapLocationClient(AddCabActivity.this);
            //?????????????????????
            mLocationOption = new AMapLocationClientOption();
            //???????????????????????????????????????Battery_Saving?????????????????????Device_Sensors??????????????????
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //??????????????????,????????????,?????????2000ms
            mLocationOption.setInterval(2000);
            //??????????????????
            mLocationClient.setLocationOption(mLocationOption);
            //??????????????????
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation mAMapLocation) {
                    if (mAMapLocation != null) {
                        aMapLocation = mAMapLocation;
                        if (aMapLocation.getErrorCode() == 0) {
                            //?????????????????????????????????????????????
                            mPositioned = true;
                            aMapLocation.getLocationType();//??????????????????????????????????????????????????????????????????????????????
                            mLng = aMapLocation.getLongitude();//????????????
                            mLat = aMapLocation.getLatitude();//????????????
                            Log.d("xiaoming0203", "onLocationChanged:  aMapLocation.getCity(): " + aMapLocation.getAddress() + "aMapLocation.getCityCode(): " + aMapLocation.getCityCode() + "aMapLocation.getLatitude() :" + aMapLocation.getLatitude() + "aMapLocation.getLongitude():" + aMapLocation.getLongitude());
                        } else {
                            //??????????????????ErrCode???????????????errInfo???????????????????????????????????????
                            Log.d("xiaoming0203", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
            //????????????
            mLocationClient.startLocation();
        } catch (Exception e) {
            Log.e("xiaoming0203", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (ADD_IMAGE_IMG_CAMERA == requestCode) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                String filePath = "";
                if (Build.VERSION.SDK_INT >= 29) {
                    filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
                } else {
                    filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
                }
                BitmapManager.saveBitmapFile(new File(filePath), bitmap);
                if (mPresenter != null) {
                    if (!TextUtils.isEmpty(mPath)) {
                        mPresenter.requestUpLoadImg(mPath, filePath, mUpToken, mCompanyid, requestCode);
                    } else {
                        showMessage("???????????????????????????~");
                        mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
                    }
                }
            } else if (ADD_IMAGE_ABLBUM == requestCode) {
                Uri uri = data.getData();
                String filePath = getRealPathFromURI(uri);
                //?????????????????????????????????
                int requestWidth = (int) (1024 / 2.625);//??????1024?????????dp
                //????????????inJustDecodeBounds = true???????????????????????????
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);
                //??????inSampleSize
                options.inSampleSize = calculateInSampleSize(options, requestWidth, requestWidth);
                //??????inSampleSize???????????????
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                String bitmapFilePath = "";
                if (Build.VERSION.SDK_INT >= 29) {
                    bitmapFilePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
                } else {
                    bitmapFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
                }
                BitmapManager.saveBitmapFile(new File(bitmapFilePath), bitmap);
                if (mPresenter != null) {
                    if (!TextUtils.isEmpty(mPath)) {
                        mPresenter.requestUpLoadImg(mPath, bitmapFilePath, mUpToken, mCompanyid, requestCode);
                    } else {
                        showMessage("???????????????????????????~");
                        mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
                    }
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionX.init(activity)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
//                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        initLocation();
                    } else {
                        DialogByEnter dialog = new DialogByEnter(activity, "??????????????????????????????,??????????????????????????????");
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
        showMessage("????????????????????????????????????????????????");
    }

    private void pushPhotoUrl(Object data, CompletionHandler<String> callback) {
//        PictureSelectorUtils.addPhotoByCameraAndAlbum(AddCabActivity.this, ADD_IMAGE_IMG_1);
//        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_avatar, null);
//        TextView tv_open_album = view.findViewById(R.id.tv_open_album);
//        View line2 = view.findViewById(R.id.line2);
//        if (baseUrl.equals(DOMAIN_STRING + "Patrol/addweek") || baseUrl.equals(DOMAIN_STRING + "Patrol/addfactory")) {
//            //??????????????????????????????
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
            EventBus.getDefault().post(new AddCabEvent());

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