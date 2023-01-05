package com.android.jidian.client.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.MainShop_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.QrCodeScanBean;
import com.android.jidian.client.bean.ScanCodeEventBean;
import com.android.jidian.client.mvp.contract.ScanCodeContract;
import com.android.jidian.client.mvp.presenter.ScanCodePresenter;
import com.android.jidian.client.mvp.ui.dialog.ScanCodeBindingDialog;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.widgets.MyToast;
//import com.luck.picture.lib.PictureSelector;
//import com.luck.picture.lib.config.PictureConfig;
//import com.luck.picture.lib.config.PictureMimeType;
//import com.luck.picture.lib.entity.LocalMedia;
import com.permissionx.guolindev.PermissionX;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * @author : PTT
 * date: 2020/9/23 14:04
 * company: 兴达智联
 * description: 二维码扫描
 */
public class ScanCodeNewActivityU6 extends U6BaseActivityByMvp<ScanCodePresenter> implements ScanCodeContract.View, QRCodeView.Delegate {
    @BindView(R.id.page_return)
    LinearLayout page_return;
    @BindView(R.id.tv_scan_qr_hint)
    TextView tvScanQrHint;
    @BindView(R.id.edt_scan_qr_code)
    EditText edtInputCode;
    @BindView(R.id.zxingview)
    ZXingView zXingview;

    private String mIsInputBox = "0";
    public static final String SCAN_CODE_IS_INPUT_BOX = "SCAN_CODE_IS_INPUT_BOX";
    private String mType = null;
    public static final String TYPE = "SCAN_CODE_TYPE";
    /**
     * 是否允许相机权限
     */
    private boolean isPermissionX = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scan_qr_code);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new ScanCodePresenter();
        mPresenter.attachView(this);

        mIsInputBox = getIntent().getStringExtra(SCAN_CODE_IS_INPUT_BOX);
        mType = getIntent().getStringExtra(TYPE);

        if ("1".equals(mIsInputBox)) {
            tvScanQrHint.setText("请将取景框对准设备号\n扫描后即可绑定车辆");
        }
        if ("1".equals(mIsInputBox) || "2".equals(mIsInputBox)) {
            //首页扫一扫2、我的钱包-绑定模块1 这两个地方进扫描界面可带输入框，其他则不显示
            findViewById(R.id.edt_scan_qr_code).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_scan_qr_sure).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.edt_scan_qr_code).setVisibility(View.GONE);
            findViewById(R.id.btn_scan_qr_sure).setVisibility(View.GONE);
        }

        zXingview.setDelegate(this);
    }

    @OnClick({R.id.page_return, R.id.btn_scan_qr_light, R.id.btn_scan_qr_sure, R.id.btn_scan_qr_album})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.page_return:
                finish();
                break;
            case R.id.btn_scan_qr_light:
                //点击闪光灯
                if (isPermissionX) {
                    setFlashStyle();
                } else {
                    showMessage("您未开启相机权限");
                }
                break;
            case R.id.btn_scan_qr_sure:
                //点击确定
                String str = edtInputCode.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    MyToast.showTheToast(this, "输入信息不能为空！");
                } else {
                    if ("1".equals(mIsInputBox) || "2".equals(mIsInputBox)) {
                        //钱包1、首页2
                        if (mPresenter != null) {
                            mResult = str;
                            if (str.contains("=")) {
                                String s = str.split("=")[1];
                                mPresenter.requestQrCodeScan(uid, s, "", mType, "1");
                            }else {
                                mPresenter.requestQrCodeScan(uid, "", str, mType, "1");
                            }
                        }
                    }
                }
                break;
            case R.id.btn_scan_qr_album:
                //点击相册
                PermissionX.init(ScanCodeNewActivityU6.this)
                        .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, HEADS_ALBUM);

//                                PictureSelector.create(this)
//                                        // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                                        .openGallery(PictureMimeType.ofImage())
////                        .loadImageEngine(GlideEngine.createGlideEngine())
////                        // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
////                        .theme(com.phjt.commmons.R.style.picture_default_style)
//                                        // 最大图片选择数量
//                                        .maxSelectNum(1)
//                                        // 最小选择数量
//                                        .minSelectNum(1)
//                                        // 每行显示个数
//                                        .imageSpanCount(4)
//                                        // 多选 or 单选
//                                        .selectionMode(PictureConfig.SINGLE)
//                                        // 是否可预览图片
//                                        .previewImage(true)// 是否显示拍照按钮
//                                        .isCamera(true)
//                                        // 图片列表点击 缩放效果 默认true
//                                        .isZoomAnim(false)
//                                        // 是否裁剪
//                                        .enableCrop(false)
//                                        // 是否压缩
//                                        .compress(true)
//                                        //同步true或异步false 压缩 默认同步
//                                        .synOrAsy(true)
//                                        // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                                        .glideOverride(160, 160)
//                                        // 是否开启点击声音
//                                        .openClickSound(false)
//                                        // 小于100kb的图片不压缩
//                                        .minimumCompressSize(100)
//                                        .forResult(PictureConfig.CHOOSE_REQUEST);
                            } else {
                                MyToast.showTheToast(this, "当前应用缺少必要权限 ");
                            }
                        });
                break;
            default:
                break;
        }
    }
    private static final int HEADS_ALBUM = 8081;//正面相册选择
    private String img_1_path = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HEADS_ALBUM && resultCode == RESULT_OK && null != data){
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
//            user_card_1_img.setImageBitmap(img);
//            isFirstPic = true;
//            //实名认证跟ios一样   643
//            UploadImage_A(img_1_path, uid, upLoadImage_1SuccessHandler, errorHandler);
//            cursor.close();
//            progressDialog.show();
            zXingview.decodeQRCode(img_1_path);
        }



//        if (resultCode == RESULT_OK) {
//            // 图片选择结果回调
//            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
//                List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
//                if (!localMedia.isEmpty()) {
//                    zXingview.decodeQRCode(localMedia.get(0).getCompressPath());
//                }
//            }
//        }
    }

    @Override
    public void requestQrCodeScanSuccess(QrCodeScanBean data) {
        if (!TextUtils.isEmpty(data.getSync_msg()) && !TextUtils.isEmpty(data.getSync_btn())) {
            new ScanCodeBindingDialog().init(data.getMsg(), data.getSync_msg(), data.getSync_btn(), new ScanCodeBindingDialog.OnDismissListener() {
                @Override
                public void onSureDismiss() {
                    zXingview.startSpotAndShowRect();
                }

                @Override
                public void onEndBindingDismiss() {
                    if (mPresenter != null) {
                        mPresenter.requestEndBindBike(uid);
                    }
                }
            }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(false).show(getSupportFragmentManager());
        } else {
            if ("1".equals(mIsInputBox)) {
                EventBus.getDefault().post(new ScanCodeEventBean(ScanCodeEventBean.REFRESH_E_BIKE));
            } else if ("2".equals(mIsInputBox)) {
                String scantype = data.getType();
                switch (scantype) {
                    case "1":
                        //商家二维码
                        Intent intent = new Intent(activity, MainShop_.class);
                        intent.putExtra("code", mResult);
                        startActivity(intent);
                        break;
                    case "2":
                        //电池二维码
//                        Thread thread = new Thread() {
//                            @Override
//                            public void run() {
//                                super.run();
//                                try {
//                                    sleep(3000);
                                    Intent intent1 = new Intent(activity, MainActivityU6.class);
                                    activity.startActivity(intent1);
//                                } catch (InterruptedException e) {
//                                    System.out.println(e.getLocalizedMessage());
//                                }
//                            }
//                        };
//                        thread.start();
                        break;
                    case "3":
                        //优惠券二维码
                        if (data.getData() != null) {
                            Intent intent2 = new Intent(this, CouponCashv2ActivityU6.class);
                            intent2.putExtra("name", data.getData().getName());
                            intent2.putExtra("type", data.getData().getType());
                            intent2.putExtra("fval", data.getData().getFval());
                            intent2.putExtra("expire", data.getData().getExpire());
                            intent2.putExtra("code", data.getData().getCode());
                            intent2.putExtra("code_info", data.getData().getCode_info());
                            intent2.putExtra("rule", data.getData().getRule());
                            intent2.putExtra("alert", data.getData().getAlert());
                            startActivity(intent2);
                        }
                        break;
                    case "4":
                        //车辆二维码
                        Intent intent3 = new Intent(activity, MainActivityU6.class);
                        startActivity(intent3);
                        break;
                }
            }
            MyToast.showTheToast(activity, data.getMsg());
            finish();
        }
    }

    @Override
    public void requestQrCodeScanError(String msg) {
        showMessage(msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (zXingview != null) {
                    zXingview.startSpotAndShowRect();
                }
            }
        }, 2000);
    }

    @Override
    public void requestEndBindBikeSuccess(QrCodeScanBean qrCodeScanBean) {
        if ("2".equals(mIsInputBox)) {
            Intent intent3 = new Intent(activity, MainActivityU6.class);
            startActivity(intent3);
        }
        finish();
    }

    @Override
    public void requestCouponScanSuccess(BaseBean bean) {
        EventBus.getDefault().post(new ScanCodeEventBean(mResult, ScanCodeEventBean.REFRESH_DISCOUNT));
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
        showMessage(throwable.getLocalizedMessage());
    }

    private String mResult = "";

    /**
     * 二维码识别成功回调
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        if ("1".equals(mIsInputBox) || "2".equals(mIsInputBox)) {
            //钱包1、首页2
            vibrate();
            zXingview.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
            if (null != result && !result.isEmpty()) {
                if (mPresenter != null) {
                    mResult = result;
                    if (result.contains("=")) {
                        String s = result.split("=")[1];
                        mPresenter.requestQrCodeScan(uid, s, "", mType, "1");
                    }
                } else {
                    zXingview.startSpotAndShowRect();
                }
            } else {
                MyToast.showTheToast(this, getString(R.string.str_error_address));
                zXingview.startSpotAndShowRect();
            }
        } else if ("3".equals(mIsInputBox)) {
            //我的优惠券
            if (mPresenter != null) {
                mResult = result;
                mPresenter.requestCouponScan(uid, result);
            }
        } else {
            //商城
            Intent intent = getIntent();
            intent.putExtra("codedContent", result);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionX.init(ScanCodeNewActivityU6.this)
                .permissions(Manifest.permission.CAMERA)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        if (PubFunction.isConnect(activity, true)) {
                            isPermissionX = true;
                            zXingview.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                            zXingview.startSpotAndShowRect(); // 显示扫描框，并开始识别
                        }
                    } else {
                        MyToast.showTheToast(this, "当前应用缺少必要权限 ");
                    }
                });
    }

    @Override
    protected void onStop() {
        if (isPermissionX) {
            zXingview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (isPermissionX) {
            zXingview.onDestroy(); // 销毁二维码扫描控件
        }
        super.onDestroy();
    }

    private boolean isFlashOpen;

    /**
     * 设置闪光灯开启或关闭样式
     */
    private void setFlashStyle() {
        if (isFlashOpen) {
            zXingview.closeFlashlight(); // 关闭闪光灯
        } else {
            zXingview.openFlashlight(); // 打开闪光灯
        }
        isFlashOpen = !isFlashOpen;
    }

    /**
     * 震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(200);
        }
    }
}