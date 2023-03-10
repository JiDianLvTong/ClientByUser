package com.android.jidian.client;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.android.jidian.client.mvp.ui.activity.pub.ScanCodeActivity;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.android.jidian.client.base.AppLocationManager;
import com.android.jidian.client.bean.ChargeSiteMapInfoBean;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.bean.PullActivityIndexBean;
import com.android.jidian.client.bluetooth.BluetoothDeviceList;
import com.android.jidian.client.bluetooth.BluetoothSpp;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.mvp.ui.activity.login.LoginActivity;
import com.android.jidian.client.mvp.ui.adapter.MainSceneDialogSecondAdapter;
import com.android.jidian.client.adapter.SimpleAdapters;
import com.android.jidian.client.bean.AdvicesNumsV2Bean;
import com.android.jidian.client.bean.AdvicesReadBean;
import com.android.jidian.client.bean.BluetoothEvent;
import com.android.jidian.client.bean.CabinetDetailBean;
import com.android.jidian.client.bean.ContactV2Bean;
import com.android.jidian.client.bean.MapJwduV5Bean;
import com.android.jidian.client.bean.MapTopTabBean;
import com.android.jidian.client.mvp.contract.MainContract;
import com.android.jidian.client.mvp.ui.dialog.BluetoothDialogFragment;
import com.android.jidian.client.service.BluetoothChatService;
import com.android.jidian.client.util.CalcUtils;
import com.android.jidian.client.util.MakerUtils;
import com.android.jidian.client.util.MapUtil;
import com.android.jidian.client.widgets.AlignLeftFlowLayout;
import com.android.jidian.client.mvp.ui.dialog.NoLocalFragment;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.jpush.ExampleUtil;
import com.android.jidian.client.jpush.LocalBroadcastManager;
import com.android.jidian.client.net.RetrofitClient;
import com.android.jidian.client.net.RxScheduler;
import com.android.jidian.client.mvp.presenter.MainPresenter;
import com.android.jidian.client.pub.CreateFile;
import com.android.jidian.client.widgets.ChargeSiteShowInfo;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;
import com.android.jidian.client.service.BluetoothLeServiceMain;
import com.android.jidian.client.util.Util;
import com.android.jidian.client.util.ViewUtil;
import com.android.jidian.client.mvp.ui.activity.message.AdvicesListsActivity;
import com.android.jidian.client.mvp.ui.dialog.CommonPopupWindow;
import com.android.jidian.client.widgets.MyScrollView;
import com.android.jidian.client.widgets.PersonalInfoDrawerMenu;
import com.android.jidian.client.widgets.ViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;
import com.timmy.tdialog.TDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.android.jidian.client.util.Util.drawableToBitmap;
import static java.lang.Thread.sleep;

@SuppressLint("Registered")
@EActivity(R.layout.main)
public class Main extends U6BaseActivityByMvp<MainPresenter> implements MainContract.View, AMap.OnMapClickListener,
        AMap.CancelableCallback, AMap.OnMapLongClickListener {

    private static final String TAG = "MainTAG";
    /**
     * ????????????????????????????????????
     */
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private static final int PERMISSON_REQUESTCODE = 0;
    private static final int REQUEST_PERMISSION_SETTING = 1;
    /**
     * ??????????????????????????????????????????????????????marker
     */
    public static Handler handleRefreshMarker, qibeiHandler;

    @ViewById
    LinearLayout ll_point_container, site0_linearLayout, site1_linearLayout,
            site2_linearLayout, site4_linearLayout, layout_sites,page_return;
    @ViewById
    FrameLayout nor_read_red_point_panel, blue_electric;
    @ViewById
    MapView mMapView;
    @ViewById
    MyScrollView myScrollView;
    @ViewById
    ViewPager viewPager, home_viewPager;
    @ViewById
    TabLayout home_tabLayout;
    @ViewById
    TextView nor_read_red_point, site0_textView, site1_textView, site2_textView, site4_textView, blue_electricNum;
    @ViewById
    ImageView site0_imageView, site1_imageView, site2_imageView, site4_imageView, lists, main_battery_img, tv_main_invite_small;
    @ViewById
    CircleImageView iv_user_headPortrait;
    @ViewById
    RelativeLayout touch;
    MyLocationStyle myLocationStyle;
    private NoLocalFragment noLocalFragment = NoLocalFragment.getInstance();
    /**
     * ???????????????????????????????????????????????????
     */
    private boolean positioned = false;
    private Bundle savedInstanceState;
    private LatLng location1;

    /**
     * ??????
     */
    private AMap aMap;
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    /**
     * ??????????????????marker??????????????????????????????????????????????????????
     */
    private boolean isRequestMarkerNow = false;
    /**
     * ?????????????????????
     */
    private int viewpager_count = 0;
    private List<View> viewList = new ArrayList<>();// ?????????????????????View???????????????
    private ImageView[] imageViews = new ImageView[viewpager_count];
    private boolean isFirstIn = true;
    private boolean isExit = false;
    /**
     * ????????????????????????????????????????????????
     */
    private boolean isNeedCheck = true;
    /**
     * ?????????????????????????????????????????????????????????????????????marker
     */
    public static Double longitude = 0.0;
    public static Double latitude = 0.0;
    /**
     * ????????????????????????0?????????1????????????2????????????3????????????-1?????????
     */
    private int type = 0;
    private int tab = 1;
    private List<Map<String, Object>> markerList = new LinkedList<>();
    private List<String> imgList;
    private BitmapDescriptor bitmapDescriptor;
    private String repair = "";
    /**
     * ????????????????????????fragment????????????tablayout????????????
     */
    private int currentPosition = 0;
    public static Handler downloadHandler;
    private AlertDialog mAlertDialog;
    private CommonPopupWindow popupWindow;
    public static Handler handleViewpagerVisiable;
    /**
     * ?????????????????????????????????????????? onResume() ??? onPause() ?????????
     */
    public static boolean isForeground = false;

    /**
     * for receive customer msg from jpush server ??????????????????
     */
    public static final String MESSAGE_RECEIVED_ACTION = "com.android.jidian.client.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static MediaPlayer mediaPlayer = null;
    private boolean playBeep;

    /**
     * ??????APP????????????????????????????????????
     */
    private boolean isRequestLocation = false;
    /**
     * ????????????????????????
     */
    private List<AdvicesNumsV2Bean.ListBean> listAdvicesBean = new ArrayList<>();
    /**
     * ?????????10????????????
     */
    private TDialog dialogSceneFirst = null;
    /**
     * ?????????11????????????
     */
    private TDialog dialogSceneSecond = null;
    /**
     * ?????????12????????????
     */
    private TDialog dialogSceneThird = null;
    /**
     * ????????????21 ????????????20
     */
    private TDialog dialogActivityMessage = null;

    private PersonalInfoDrawerMenu drawerMenu;

    //????????????
    private ArrayList<MapJwduV5Bean.DataBean> dataArrayList;

    /**
     * ??????
     */
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler scanLeDeviceHandler;
    private static BluetoothGattCharacteristic target_chara = null;
    private static final long SCAN_PERIOD = 10000;
    private String mDeviceAddress;
    private static BluetoothLeServiceMain mBluetoothLeServicemain;
    private static final int REQUEST_ENABLE_BLUETOOTH = 11111;
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private Handler Characteristichandler = new Handler();
    private int bluetooth_num = 0;
    private String battery = "", electric = "", wendu = "", vdif1 = "", vdif2 = "", vdif3 = "";
    private Handler batteryRealtimeHandler;
    private boolean needClose = false;
    private BluetoothChatService mChatService = null;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private String show_bluetooth;

    public void registerMessageReceiver() {
        MessageReceiver mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void setCostomMsg(String msg) {
        Toast.makeText(this, "????????????????????????" + msg, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("HandlerLeak")
    private void handle() {
        handleViewpagerVisiable = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (home_viewPager.getVisibility() == View.VISIBLE) {
                    home_viewPager.setVisibility(View.GONE);
                } else {
                    home_viewPager.setVisibility(View.VISIBLE);
                }
            }
        };
        batteryRealtimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (!uid.isEmpty()) {
                        HttpBatteryRealtime();
                    }
                } else if (msg.what == 2) {
                    battery = "";
                    electric = "";
                    wendu = "";
                    vdif1 = "";
                    vdif2 = "";
                    vdif3 = "";
                }
            }
        };
    }

    //??????????????????
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode??????0????????????????????????????????????????????????????????????????????????????????????????????????
                if (location.getErrorCode() == 0) {
                    Log.d("051101", "????????????1");
                    if (!positioned) {
                        positioned = true;
                        //??????????????????????????????
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        //????????????????????????
                        if (myLocationStyle != null) {
                            if (aMap != null) {
                                aMap.setMyLocationStyle(myLocationStyle);
                            }
                        } else {
                            setupLocationStyle();
                        }
                    }
                    //???????????????????????????
                    location1 = new LatLng(location.getLatitude(), location.getLongitude());
                    aMap.animateCamera(
                            CameraUpdateFactory.newCameraPosition(new CameraPosition(location1, 18, 0, 0)),
                            888,
                            Main.this);
                } else {
                    //????????????
                    positioned = false;
                    Log.d("051101", "????????????2");
                }
            } else {
                positioned = false;
                Log.d("051101", "????????????3");
            }
            if (!positioned) {
                //????????????????????????????????????????????????GPS??????????????????????????????????????????GPS????????????
                if (!isRequestLocation) {
                    isRequestLocation = true;
                    if (!AppLocationManager.isLocationEnabled(Main.this)) {
                        show_no_local();
                    }
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private void handler() {
        handleRefreshMarker = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!isDestroy(Main.this)) {
                    Glide.with(Main.this).load(R.drawable.icon_main_user).into(iv_user_headPortrait);
                    type = 0;
                    resetSitesBackground();
                    uid = sharedPreferences.getString("id", "");
                    if (PubFunction.isConnect(activity, true)) {
                        /*
                         * ??????????????????????????????????????????????????????????????????????????????????????????????????????
                         * onResume()?????????????????????isForeground?????????false???
                         * ???????????????onResume()???????????????????????????isForeground???true???
                         * isForeground???startDrawMarker()??????????????????
                         */
                        isForeground = true;
                        startDrawMarker();
                        Log.d(TAG, "handleMessage: 1");
                        //???????????????????????????????????????
                        if (!TextUtils.isEmpty(uid)) {//??????????????????
                            List<ParamTypeData> dataList = new ArrayList<>();
                            dataList.add(new ParamTypeData("title", "????????????"));
                            dataList.add(new ParamTypeData("sourceType", "5"));
                            dataList.add(new ParamTypeData("sourceId", uid));
                            dataList.add(new ParamTypeData("type", "100"));
                            dataList.add(new ParamTypeData("client", "Android"));
                            HttpVisitLogs(dataList);
                            setHeadPortrait();
                        }
                    }
                    if (home_viewPager != null) {
                        home_viewPager.setVisibility(View.GONE);
                    }
                }
            }
        };

        qibeiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String status = msg.getData().getString("status");
                /*????????????????????????????????????????????????
                if ("1".equals(status)) {
                    MyApplication.mBridgeCallback.onPayResult(1, "????????????");
                } else if ("0".equals(status)) {
                    MyApplication.mBridgeCallback.onPayResult(0, "????????????");
                }*/
            }
        };
        downloadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String Android_url = msg.getData().getString("url");
                LayoutInflater inflater = LayoutInflater.from(activity);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                mAlertDialog = builder.create();
                View view = inflater.inflate(R.layout.alertdialog_update1, null);
//                TextView payAlertdialogSuccess = view.findViewById(R.id.payAlertdialogSuccess);
//                TextView text_panel = view.findViewById(R.id.text_panel);
//                text_panel.setText("???????????????????????????????????????????????????");
//                payAlertdialogSuccess.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PermissionX.init(Main.this)
//                                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
//                                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
//                                .request((allGranted, grantedList, deniedList) -> {
//                                    if (allGranted) {
//                                        payAlertdialogSuccess.setVisibility(View.GONE);
//                                        new DownLoadAndInstallApp(activity, Android_url, "myApp.apk");
//                                        MyToast.showTheToast(activity, "????????????APP???????????????");
//                                    } else {
//                                        MyToast.showTheToast(Main.this, "?????????????????????????????? ");
//                                    }
//                                });
//                    }
//                });
                mAlertDialog.setCancelable(false);
                mAlertDialog.show();
                mAlertDialog.getWindow().setContentView(view);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //??????????????????mMapView????????????null???????????????savedInstanceState???
        //?????????afterViews()??????mMapView.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState;
        handle();
        // used for receive msg
        registerMessageReceiver();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetStateChangeReceiver(), intentFilter);

        //?????????????????????????????????????????????????????????
        Disposable d = RetrofitClient.getInstance().getApiService().getContactV2()
                .compose(RxScheduler.Flo_io_main())
                .subscribe(this::onSuccess, throwable -> throwable.printStackTrace(System.err));
        EventBus.getDefault().register(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //??????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_HOME_PAGE);
            }
        }, 500);
        //???????????????????????????????????????????????? ???????????????????????????   stoppush    ????????????
        JPushInterface.stopPush(this);
    }

    private void onSuccess(ContactV2Bean contactV2Bean) {
        if (contactV2Bean.getStatus() == 1) {
            List<ContactV2Bean.DataBean> dataBeans = contactV2Bean.getData();
            ContactV2Bean.DataBean dataBean0 = dataBeans.get(0);

            SharedPreferences sharedPreferencesAlias = getSharedPreferences("contact_Info", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferencesAlias.edit();
            editor.clear();
            if (dataBean0 != null && dataBean0.getIs_show() == 1) {
                String phone0 = dataBean0.getName() + "???" + dataBean0.getContact();
                String phone0_contact = dataBean0.get_contact();
                editor.putString("phone0", phone0);
                editor.putString("phone0_contact", phone0_contact);
            }
            ContactV2Bean.DataBean dataBean1 = dataBeans.get(1);
            if (dataBean1 != null && dataBean1.getIs_show() == 1) {
                String phone1 = dataBean1.getName() + "???" + dataBean1.getContact();
                String phone1_contact = dataBean1.get_contact();
                editor.putString("phone1", phone1);
                editor.putString("phone1_contact", phone1_contact);
            }
            editor.apply();
        }
    }

    @AfterViews
    void afterViews() {
        handler();
        //???????????????
        new CreateFile(activity);
        //map???????????????
        InitMap();
        //??????????????????
        HttpGetVer();
        setHeadPortrait();
//        ((TextView)findViewById(R.id.tv_title)).setText("??????");
//        initBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        Log.d(TAG, "onResume: set_isForeground_true");
        //?????????????????? ????????????
        //???activity??????onResume?????????mMapView.onResume()???????????????????????????
        mMapView.onResume();
        if (!positioned) {
            Log.d("locationTest", "onResume: " + "????????????");
            startLocation();//????????????
        }
        if (isFirstIn && !isRequestMarkerNow) {
            if (PubFunction.isConnect(activity, true)) {
                startDrawMarker();//????????????marker
                Log.d(TAG, "handleMessage: 2");
            }
        }
        //??????????????????
        if (!TextUtils.isEmpty(uid)) {
//            HttpGetMainInfo();
        } else {
            nor_read_red_point_panel.setVisibility(View.GONE);
        }
        System.gc();
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();

        //?????????????????????
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeServicemain != null) {
            //?????????????????????????????????
            final boolean result = mBluetoothLeServicemain.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }

        if (mPresenter != null) {
            apptoken = sharedPreferences.getString("apptoken", "");
            String appsn = Util.getFileContent(new File("/sdcard/Gyt/userAppSn.txt"));
            if (!TextUtils.isEmpty(apptoken) && !TextUtils.isEmpty(appsn)) {
                mPresenter.requestCheckAccv2(apptoken, appsn);
            }
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.sound_notify);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private String[] titleStrings;

    private void initPopupWindowView(MapTopTabBean mapTopTabBean) {
        progressDialog.dismiss();
        List<MapTopTabBean.TopTabBean> topTab = mapTopTabBean.getTopTab();
        titleStrings = new String[topTab.size()];
        for (int i = 0; i < topTab.size(); i++) {
            titleStrings[i] = topTab.get(i).getName();
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.popup_down, null);
        view.findViewById(R.id.exitpop).setOnClickListener(v -> {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        });
        GridView pop_recy = view.findViewById(R.id.pop_recy);
        SimpleAdapters mAdapter = new SimpleAdapters(this, topTab);
        pop_recy.setAdapter(mAdapter);
        pop_recy.setOnItemClickListener((parent, view1, position, id) -> {
            home_viewPager.setCurrentItem(position);
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        });
        popupWindow = new CommonPopupWindow.Builder(activity)
                .setView(view)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setBackGroundLevel(1.0f)
                .setOutsideTouchable(true)
                .create();
        initViewpager();
    }

//    private BlankFragment1 blankFragment1;
//    private BlankFragment2 blankFragment2;
//    private BlankFragment3 blankFragment3;
//    private BlankFragment4 blankFragment4;

    private void initViewpager() {
        String jingdu = longitude == 0 ? "" : longitude.toString();
        String weidu = latitude == 0 ? "" : latitude.toString();
        List<Fragment> fragments = new ArrayList<>();
//        blankFragment1 = BlankFragment1.newInstance(jingdu, weidu, type + "");
//        blankFragment2 = BlankFragment2.newInstance(jingdu, weidu);
//        blankFragment3 = BlankFragment3.newInstance(jingdu, weidu);
//        blankFragment4 = BlankFragment4.newInstance(jingdu, weidu);
//        fragments.add(blankFragment1);
//        fragments.add(blankFragment2);
//        fragments.add(blankFragment3);
//        fragments.add(blankFragment4);
        fragments.add(new DialogFragment());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titleStrings);
        home_viewPager.setAdapter(adapter);
        home_tabLayout.setupWithViewPager(home_viewPager);
        // ????????? tab ????????????????????? tab ?????????
        TabLayout.Tab tabAt = home_tabLayout.getTabAt(home_tabLayout.getTabCount() - 1);//TODO H5 PLAY
        if (tabAt != null) {
            Class c = tabAt.getClass();
            try {
                Field field = c.getDeclaredField("mView");
                if (field != null) {
                    field.setAccessible(true);
                    View view = (View) field.get(tabAt);
                    if (view != null) {
                        view.setOnClickListener(v -> {
                            if (currentPosition == titleStrings.length - 1) {
                                MyToast.showTheToast(activity, "???????????????????????????");
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        TabLayout.Tab tabAt2 = home_tabLayout.getTabAt(home_tabLayout.getTabCount() - 2);//TODO H5 PLAY
        if (tabAt2 != null) {
            Class c = tabAt2.getClass();
            try {
                Field field = c.getDeclaredField("mView");
                if (field != null) {
                    field.setAccessible(true);
                    View view = (View) field.get(tabAt2);
                    if (view != null) {
                        view.setOnClickListener(v -> {
                            if (currentPosition == titleStrings.length - 2) {
                                MyToast.showTheToast(activity, "???????????????????????????");
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        home_viewPager.setOffscreenPageLimit(4);
        home_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        //??????????????????
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_CAR_RENTAL);
                        if (home_viewPager != null && !(home_viewPager.getVisibility() == View.VISIBLE)) {
                            //?????????????????????
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_CAR_RENTAL_MAP);
                        }
                        break;
                    case 2:
                        //??????????????????
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_BUY_A_CAR);
                        if (home_viewPager != null && !(home_viewPager.getVisibility() == View.VISIBLE)) {
                            //?????????????????????
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_CAR_BUY_MAP);
                        }
                        break;
                    case 3:
                        //??????????????????
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_RETURN_THE_CAR);
                        if (home_viewPager != null && !(home_viewPager.getVisibility() == View.VISIBLE)) {
                            //?????????????????????
                            BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_CAR_RETURN_MAP);
                        }
                        break;
                    case 4:
                        //??????????????????
                        BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_REPAIR);
                        if (home_viewPager != null) {
                            if (!(home_viewPager.getVisibility() == View.VISIBLE)) {
                                //?????????????????????
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_CAR_REPAIR_MAP);
                            } else {
                                //?????????????????????
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_REPAIR_LIST);
                            }
                        }
                        break;
                    default:
                        break;
                }

                if (currentPosition != position) {
                    myScrollView.A_1_close();
                }
                currentPosition = position;
                if (position == 0) {
                    site0_linearLayout.setVisibility(View.VISIBLE);
                    layout_sites.setVisibility(View.VISIBLE);
                } else {
                    site0_linearLayout.setVisibility(View.GONE);
                    layout_sites.setVisibility(View.GONE);
                }
                // ?????????????????? fragment ???????????????
                if ((position == fragments.size() - 1) || (position == fragments.size() - 2)) {
                    home_viewPager.setVisibility(View.GONE);
                    lists.setVisibility(View.GONE);
                } else {
                    lists.setVisibility(View.VISIBLE);
                }
                tab = position + 1;
                if (PubFunction.isConnect(activity, true)) {
                    startDrawMarker();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * ????????????
     */
    private void startLocation() {
        if (mLocationClient == null) {
            initLocation();
        }
        mLocationClient.startLocation();
    }

    private boolean isInviteDialogShowed = false;

    /**
     * ????????????marker
     */
    @Background
    void startDrawMarker() {
        Log.d(TAG, "startDrawMarker:isForeground: " + isForeground);
        if (isForeground) {
            if (home_viewPager != null && !(home_viewPager.getVisibility() == View.VISIBLE)) {
                new Handler(getMainLooper()).post(() -> Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show());
            }
            isRequestMarkerNow = true;
            //?????????????????????5???
            //??????5?????????????????????????????????????????????????????????marker
            //5??????????????????????????????????????????uid????????????marker
            int until = 0;
            while (until < 6) {
                if (positioned || until >= 5) {
                    //????????????marker
                    if (PubFunction.isConnect(activity, true)) {
                        httpGetAllMarker();
                        if (!isInviteDialogShowed) {
                            HttpGetPullActivityIndex();
                            isInviteDialogShowed = true;
                        }
                    }
                    break;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                }
                until++;
            }
        }
    }

    private void InitMap() {
        //??????????????????
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(myScrollView, "translationY", 0, ViewUtil.dp2px(this, 240));
        objectAnimator.setDuration(0);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                myScrollView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        objectAnimator.start();
        mMapView.onCreate(savedInstanceState);//?????????????????????
        if (aMap == null) {
            aMap = mMapView.getMap();
            //?????????????????????   https://lbs.amap.com/api/android-sdk/guide/create-map/custom
            CustomMapStyleOptions customMapStyleOptions = getCustomMapStyleOptions(
                    getApplicationContext(), "style.data");
            if (customMapStyleOptions != null) {
                customMapStyleOptions.setEnable(true);
                aMap.setCustomMapStyle(customMapStyleOptions);
            }
            //???????????????????????????
            setupLocationStyle();
            UiSettings mUiSettings = aMap.getUiSettings();
            //?????????????????????????????????????????????
            mUiSettings.setZoomControlsEnabled(false);
            //??????????????????????????????
            mUiSettings.setRotateGesturesEnabled(false);
            //??????????????????????????????
            mUiSettings.setTiltGesturesEnabled(false);
            //??????logo????????????
            mUiSettings.setLogoBottomMargin(-100);
            //?????????????????????????????????????????????????????????
            aMap.setMyLocationEnabled(true);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            //???????????????
            initLocation();
            //??????????????????
            aMap.setOnMapClickListener(this);//??????map?????????????????????
            aMap.setOnMapLongClickListener(this);//??????map?????????????????????
            aMap.setOnMarkerClickListener(marker -> {
                //????????????
//                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_SITE);
//                Map<String, Object> innerMap = (Map<String, Object>) marker.getObject();
//                String id = (String) innerMap.get("id");
//                repair = (String) innerMap.get("repair");
//                if (PubFunction.isConnect(Main.this, true)) {
//                    //???????????????????????????????????????????????????????????????
//                    mPresenter.cabinetDeatil(Integer.parseInt(id), repair, longitude + "", latitude + "");
//                }
//                return true;
                LatLng latLng = marker.getPosition();
                for(int i = 0 ; i < dataArrayList.size() ; i++){
                    String jingdu = dataArrayList.get(i).getJingdu() +"";
                    String weidu = dataArrayList.get(i).getWeidu() +"";
                    if(jingdu.equals(latLng.longitude + "") && weidu.equals(latLng.latitude + "")){
                        MapJwduV5Bean.DataBean data = dataArrayList.get(i);
//                        ChargeSiteShowInfo chargeSiteShowInfo = new ChargeSiteShowInfo(activity,data , coordinates[1] , coordinates[0]);
//                        ChargeSiteShowInfo chargeSiteShowInfo = new ChargeSiteShowInfo(activity,data , longitude , latitude);
//                        chargeSiteShowInfo.showPopupWindow();
                        new ChargeSiteShowInfo().init(activity, data, longitude, latitude, new ChargeSiteShowInfo.OnSubViewClickListener() {
                            @Override
                            public void onClickGuide(ChargeSiteMapInfoBean chargeSiteMapInfoBean) {
                                MapUtil.showNavigationDialog(Main.this, getSupportFragmentManager()
                                        , getResources().getString(R.string.siteinfo_textview3, chargeSiteMapInfoBean.getName(), chargeSiteMapInfoBean.getCabno())
                                        , chargeSiteMapInfoBean.getWeidu(), chargeSiteMapInfoBean.getJingdu());
                            }
                        }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(true).setKeyBackCancel(true).show(getSupportFragmentManager());
                    }
                }
                return false;
            });
        }
    }

    /**
     * ???????????????
     */
    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        //??????????????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????
        mLocationOption.setOnceLocation(true);
        //??????????????????
        mLocationClient.setLocationOption(mLocationOption);
        //??????????????????
        mLocationClient.setLocationListener(locationListener);
    }

    /**
     * ?????????????????????????????????
     */
    private void setupLocationStyle() {
        //??????????????????
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_marker);
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        float newWidthSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, metrics);
//        float newHeightSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, metrics);
//        //??????????????????
//        float scaleWidth = newWidthSize / width;
//        float scaleHeight = newHeightSize / height;
//        //?????????????????????matrix??????
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        //??????????????????
//        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        //???????????????????????????
        myLocationStyle = new MyLocationStyle();
        //???????????????????????????
//        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(newBitmap);
        myLocationStyle.myLocationIcon(new MakerUtils().localMarker(activity));
        //??????????????????????????????????????????
        myLocationStyle.strokeColor(0x00000000);
        //??????????????????????????????????????????
        myLocationStyle.strokeWidth(0.0f);
        //???????????????????????????
        myLocationStyle.radiusFillColor(FILL_COLOR);
        //??????????????????????????????????????????????????????
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //?????????????????????Style
        if (aMap != null) {
            if (myLocationStyle != null) {
                aMap.setMyLocationStyle(myLocationStyle);
            }
        }
    }

    /**
     * ??????assets?????????????????????????????????style.data
     *
     * @param context  Context
     * @param fileName assets?????????????????????
     * @return CustomMapStyleOptions
     */
    public CustomMapStyleOptions getCustomMapStyleOptions(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            CustomMapStyleOptions mapStyleOptions = new CustomMapStyleOptions();
            mapStyleOptions.setStyleData(b);
            return mapStyleOptions;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
        return null;
    }

    @Click({R.id.user_info, R.id.shop, R.id.set_location, R.id.phone, R.id.scan_code, R.id.page_close,
            R.id.site0_linearLayout, R.id.site1_linearLayout, R.id.site2_linearLayout, R.id.lists,
            R.id.site4_linearLayout, R.id.refresh_icon, R.id.bind_bettery, R.id.outline, R.id.mustRead,
            R.id.blue_electric,R.id.page_return})
    void click(View bt) {
        if (CalcUtils.isFastDoubleClick()) {
            return;
        }
        switch (bt.getId()) {
            case R.id.page_return:
                finish();
                break;
//                PermissionX.init(Main.this)
//                        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
//                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
//                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
//                        .request((allGranted, grantedList, deniedList) -> {
//                            if (allGranted) {
//                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_STATION_CHANG);
//                                if (PubFunction.isConnect(activity, true)) {
//                                    if (!positioned) {
//                                        Log.d("locationTest", "onResume: " + "????????????");
//                                        startLocation();//????????????
//                                    }
//                                    if (isRequestMarkerNow) {
//                                        MyToast.showTheToast(this, "??????????????????????????????????????????");
//                                    } else {
//                                        type = -1;
//                                        tab = 2;
//                                        startDrawMarker();
//                                        Log.d(TAG, "handleMessage: 5");
//                                        if (!uid.isEmpty()) {
//                                            List<ParamTypeData> maidian_dataList = new ArrayList<>();
//                                            maidian_dataList.add(new ParamTypeData("title", "???????????????"));
//                                            maidian_dataList.add(new ParamTypeData("sourceType", "5"));
//                                            maidian_dataList.add(new ParamTypeData("sourceId", uid));
//                                            maidian_dataList.add(new ParamTypeData("type", "104"));
//                                            maidian_dataList.add(new ParamTypeData("client", "Android"));
//                                            HttpVisitLogs(maidian_dataList);
//                                        }
//                                    }
//                                }
//                            } else {
//                                MyToast.showTheToast(this, "?????????????????????????????? ");
//                            }
//                        });
            case R.id.mustRead:
                //???????????????????????????
//                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_NEW_USERS_MUST_READ);
//                Intent intent00 = new Intent(this, CustomH5Page.class);
//                intent00.putExtra("url", "https://h5x.mixiangx.com/Help/guide2.html");
//                startActivity(intent00);
                break;
            case R.id.lists:
                //??????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_LIST);
                if (PubFunction.isConnect(activity, true)) {
                    if (home_viewPager.getVisibility() == View.VISIBLE) {
                        home_viewPager.setVisibility(View.GONE);
                    } else {
                        home_viewPager.setVisibility(View.VISIBLE);
//                        if (blankFragment1 != null) {
//                            blankFragment1.refreshData(type + "");
//                        }
//                        if (blankFragment2 != null) {
//                            blankFragment2.refreshData();
//                        }
//                        if (blankFragment3 != null) {
//                            blankFragment3.refreshData();
//                        }
//                        if (blankFragment4 != null) {
//                            blankFragment4.refreshData();
//                        }
                    }
                }
                break;
            case R.id.outline:
                //????????????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_ALL_SERVICES);
                if (popupWindow != null) {
                    popupWindow.showAsDropDown(touch);
                }
                break;
            case R.id.user_info:
                //????????????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_PERSONAL_CENTER);
                if (PubFunction.isConnect(activity, true)) {
                    if (TextUtils.isEmpty(uid)) {
                        startActivity(new Intent(activity, LoginActivity.class));
                    } else {
                        drawerMenu = new PersonalInfoDrawerMenu(this, new PersonalInfoDrawerMenu.OnClickItemListener() {
                            @Override
                            public void onClick(int position) {
                                //??????????????????????????????????????????????????????
//                                unregisterReceiver(mGattUpdateReceiver);
//                                if (mBluetoothLeServicemain != null) {
//                                    mBluetoothLeServicemain.disconnect();
//                                }
                                if (BluetoothSpp.BluetoothSetVolHandler != null) {
                                    Message message = new Message();
                                    message.what = 2;
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("vol", 5);
                                    message.setData(bundle);
                                    BluetoothSpp.BluetoothSetVolHandler.sendMessage(message);
                                }
                                bluetooth_num = 0;
                            }
                        });
                        drawerMenu.showMenu();
                    }
                }
                break;
            case R.id.shop:
                //??????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_MESSAGE);
                if (PubFunction.isConnect(activity, true)) {
                    if (TextUtils.isEmpty(uid)) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    } else {
                        activity.startActivity(new Intent(activity, AdvicesListsActivity.class));
                    }
                }
                break;
            case R.id.set_location:
                //??????????????????
                PermissionX.init(Main.this)
                        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_NAVIGATION);
                                positioned = false;
                                startLocation();
                            } else {
                                MyToast.showTheToast(this, "?????????????????????????????? ");
                            }
                        });
                break;
            case R.id.phone:
                //??????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_CUSTOMER_SERVICE);
                if (PubFunction.isConnect(activity, true)) {
                    if (TextUtils.isEmpty(uid)) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    } else {
//                        activity.startActivity(new Intent(activity, MainCustomer_.class));
                    }
                }
                break;
            case R.id.scan_code:
                //??????HELLO????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_HELLO_MALL);
                if (PubFunction.isConnect(activity, true)) {
                    if (TextUtils.isEmpty(uid)) {
                        MyToast.showTheToast(activity, "?????????????????????");
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    } else {
//                        Intent intent = new Intent(activity, MainShop_.class);
//                        activity.startActivity(intent);
                        //????????????????????????
                        List<ParamTypeData> dataList1 = new ArrayList<>();
                        dataList1.add(new ParamTypeData("title", "????????????"));
                        dataList1.add(new ParamTypeData("sourceType", "5"));
                        dataList1.add(new ParamTypeData("sourceId", uid));
                        dataList1.add(new ParamTypeData("type", "101"));
                        dataList1.add(new ParamTypeData("client", "Android"));
                        HttpVisitLogs(dataList1);
                    }
                }
                break;
            case R.id.page_close:
                myScrollView.A_1_close();
                break;
            case R.id.site0_linearLayout:
                //??????????????????
                PermissionX.init(Main.this)
                        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_ALL);
                                if (PubFunction.isConnect(activity, true)) {
                                    if (!positioned) {
                                        Log.d("locationTest", "onResume: " + "????????????");
                                        startLocation();//????????????
                                    }
                                    if (isRequestMarkerNow) {
                                        MyToast.showTheToast(this, "??????????????????????????????????????????");
                                    } else {
                                        resetSitesBackground();
                                        site0_linearLayout.setBackgroundResource(R.drawable.site0_checked);
                                        site0_imageView.setBackgroundResource(R.mipmap.site0_checked);
                                        site0_textView.setTextColor(getResources().getColor(android.R.color.white));
                                        type = 0;
                                        tab = 1;
                                        startDrawMarker();
                                        Log.d(TAG, "handleMessage: 4");
                                        if (!uid.isEmpty()) {
                                            List<ParamTypeData> maidian_dataList = new ArrayList<>();
                                            maidian_dataList.add(new ParamTypeData("title", "??????????????????"));
                                            maidian_dataList.add(new ParamTypeData("sourceType", "5"));
                                            maidian_dataList.add(new ParamTypeData("sourceId", uid));
                                            maidian_dataList.add(new ParamTypeData("type", "102"));
                                            maidian_dataList.add(new ParamTypeData("client", "Android"));
                                            HttpVisitLogs(maidian_dataList);
                                        }
                                    }
                                }
                            } else {
                                MyToast.showTheToast(this, "?????????????????????????????? ");
                            }
                        });
                break;
            case R.id.site1_linearLayout:
                //?????????????????????
                PermissionX.init(Main.this)
                        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_STATION_CHANG);
                                if (PubFunction.isConnect(activity, true)) {
                                    if (!positioned) {
                                        Log.d("locationTest", "onResume: " + "????????????");
                                        startLocation();//????????????
                                    }
                                    if (isRequestMarkerNow) {
                                        MyToast.showTheToast(this, "??????????????????????????????????????????");
                                    } else {
                                        resetSitesBackground();
                                        site1_linearLayout.setBackgroundResource(R.drawable.site1_checked);
                                        site1_imageView.setBackgroundResource(R.mipmap.site1_white);
                                        site1_textView.setTextColor(getResources().getColor(android.R.color.white));
                                        type = -1;
                                        tab = 1;
                                        startDrawMarker();
                                        Log.d(TAG, "handleMessage: 5");
                                        if (!uid.isEmpty()) {
                                            List<ParamTypeData> maidian_dataList = new ArrayList<>();
                                            maidian_dataList.add(new ParamTypeData("title", "???????????????"));
                                            maidian_dataList.add(new ParamTypeData("sourceType", "5"));
                                            maidian_dataList.add(new ParamTypeData("sourceId", uid));
                                            maidian_dataList.add(new ParamTypeData("type", "104"));
                                            maidian_dataList.add(new ParamTypeData("client", "Android"));
                                            HttpVisitLogs(maidian_dataList);
                                        }
                                    }
                                }
                            } else {
                                MyToast.showTheToast(this, "?????????????????????????????? ");
                            }
                        });
                break;
            case R.id.site2_linearLayout:
                //??????????????????
                PermissionX.init(Main.this)
                        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_CAR);
                                if (PubFunction.isConnect(activity, true)) {
                                    if (!positioned) {
                                        Log.d("locationTest", "onResume: " + "????????????");
                                        startLocation();//????????????
                                    }
                                    if (isRequestMarkerNow) {
                                        MyToast.showTheToast(this, "??????????????????????????????????????????");
                                    } else {
                                        resetSitesBackground();
                                        site2_linearLayout.setBackgroundColor(Color.parseColor("#FFA800"));
                                        site2_imageView.setBackgroundResource(R.mipmap.site2_white);
                                        site2_textView.setTextColor(getResources().getColor(android.R.color.white));
                                        type = 2;
                                        tab = 1;
                                        startDrawMarker();
                                        Log.d(TAG, "handleMessage: 6");
                                        if (!uid.isEmpty()) {
                                            List<ParamTypeData> maidian_dataList = new ArrayList<>();
                                            maidian_dataList.add(new ParamTypeData("title", "???????????????"));
                                            maidian_dataList.add(new ParamTypeData("sourceType", "5"));
                                            maidian_dataList.add(new ParamTypeData("sourceId", uid));
                                            maidian_dataList.add(new ParamTypeData("type", "103"));
                                            maidian_dataList.add(new ParamTypeData("client", "Android"));
                                            HttpVisitLogs(maidian_dataList);
                                        }
                                    }
                                }
                            } else {
                                MyToast.showTheToast(this, "?????????????????????????????? ");
                            }
                        });
                break;
            case R.id.site4_linearLayout:
                //?????????????????????
                PermissionX.init(Main.this)
                        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_CONVENIENCE_STORE);
                                if (PubFunction.isConnect(activity, true)) {
                                    if (!positioned) {
                                        Log.d("locationTest", "onResume: " + "????????????");
                                        startLocation();//????????????
                                    }
                                    if (isRequestMarkerNow) {
                                        MyToast.showTheToast(this, "??????????????????????????????????????????");
                                    } else {
                                        resetSitesBackground();
                                        site4_linearLayout.setBackgroundResource(R.drawable.site4_checked);
                                        site4_imageView.setBackgroundResource(R.mipmap.site4_white);
                                        site4_textView.setTextColor(getResources().getColor(android.R.color.white));
                                        type = 3;
                                        tab = 1;
                                        startDrawMarker();
                                        Log.d(TAG, "handleMessage: 7");
                                        if (!uid.isEmpty()) {
                                            List<ParamTypeData> maidian_dataList = new ArrayList<>();
                                            maidian_dataList.add(new ParamTypeData("title", "???????????????"));
                                            maidian_dataList.add(new ParamTypeData("sourceType", "5"));
                                            maidian_dataList.add(new ParamTypeData("sourceId", uid));
                                            maidian_dataList.add(new ParamTypeData("type", "106"));
                                            maidian_dataList.add(new ParamTypeData("client", "Android"));
                                            HttpVisitLogs(maidian_dataList);
                                        }
                                    }
                                }
                            } else {
                                MyToast.showTheToast(this, "?????????????????????????????? ");
                            }
                        });
                break;
            case R.id.refresh_icon:
                //??????????????????
                PermissionX.init(Main.this)
                        .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                        .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                        .request((allGranted, grantedList, deniedList) -> {
                            if (allGranted) {
                                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_REFRESH);
                                if (!positioned) {
                                    Log.d("locationTest", "onResume: " + "????????????");
                                    startLocation();//????????????
                                }
                                if (PubFunction.isConnect(activity, true)) {
                                    if (isRequestMarkerNow) {
                                        MyToast.showTheToast(this, "??????????????????????????????????????????");
                                    } else {
                                        startDrawMarker();
                                        Log.d(TAG, "handleMessage: 8");
                                    }
                                }
                            } else {
                                MyToast.showTheToast(this, "?????????????????????????????? ");
                            }
                        });
                break;
            case R.id.bind_bettery:
                //?????????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HOME_PAGE_SCAN);
                if (PubFunction.isConnect(activity, true)) {
                    if (TextUtils.isEmpty(uid)) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    } else {
                        Intent mIntent = new Intent(activity, ScanCodeActivity.class);
                        mIntent.putExtra(ScanCodeActivity.SCAN_CODE_IS_INPUT_BOX, "2");
                        startActivity(mIntent);
                    }
                }
                break;
            case R.id.blue_electric:
                // ??????????????????
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
                }
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitByDoubleClick();
//        }
//        return false;
//    }

//    private void exitByDoubleClick() {
//        Timer tExit;
//        if (!isExit) {
//            isExit = true;
//            MyToast.showTheToast(activity, "????????????????????????!");
//            tExit = new Timer();
//            tExit.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    isExit = false;//????????????
//                }
//            }, 2000);// ??????2?????????????????????????????????????????????????????????????????????????????????
//        } else {
//            finish();
//            System.exit(0);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //???activity??????onDestroy?????????mMapView.onDestroy()???????????????
        mMapView.onDestroy();
        destroyLocation();
        aMap.clear();
        aMap = null;
//        unregisterReceiver(mGattUpdateReceiver);
        bluetooth_num = 0;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
    }

    /**
     * ????????????
     */
    private void destroyLocation() {
        if (mLocationClient != null) {
            /*
             * ??????AMapLocationClient????????????Activity???????????????
             * ???Activity???onDestroy??????????????????AMapLocationClient???onDestroy
             */
            //??????????????????????????????????????????????????????
            mLocationClient.stopLocation();
            //?????????????????????????????????????????????????????????
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        Log.d(TAG, "onPause: set_isForeground_false");
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void show_no_local() {
        if (!noLocalFragment.isVisible()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            noLocalFragment.setOnDialogItemClickListener(new NoLocalFragment.OnDialogItemClickListener() {
                @Override
                public void onSuccessClick() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

                @Override
                public void onErrorClick() {
                }
            });
            noLocalFragment.show(fragmentTransaction, "NOLOCAL");
        }
    }

    /**
     * http?????????App/version.html   ?????????????????????
     */
    @Background
    void HttpGetVer() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.api + "AppVer/upgrade.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetVer(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
        //??????uid???????????????????????????????????????
        //???????????????????????????????????????uid????????????????????????handleRefreshMarker???????????????????????????
        //????????????????????????????????????app?????????????????????????????????
        if (!uid.isEmpty()) {
            List<ParamTypeData> dataList1 = new ArrayList<>();
            dataList1.add(new ParamTypeData("title", "????????????"));
            dataList1.add(new ParamTypeData("sourceType", "5"));
            dataList1.add(new ParamTypeData("sourceId", uid));
            dataList1.add(new ParamTypeData("type", "100"));
            dataList1.add(new ParamTypeData("client", "Android"));
            HttpVisitLogs(dataList1);
        }
    }

    @UiThread
    void onDataHttpGetVer(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String status = jsonObject_response.getString("status");
                if ("1".equals(status)) {
                    JSONObject jsonObject = jsonObject_response.getJSONObject("data");
                    String Android_code = jsonObject.getString("Android_code");
                    final String Android_url = jsonObject.getString("Android_url");
                    String Android_force = jsonObject.getString("Android_force");
                    int Android_code_int = Integer.parseInt(Android_code);
                    if ("0".equals(Android_force)) {
                        PackageManager manager = activity.getPackageManager();
                        PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
                        final int versionCodeLocal = info.versionCode;
                        if (Android_code_int > versionCodeLocal) {
                            LayoutInflater inflater = LayoutInflater.from(activity);
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            final AlertDialog mAlertDialog = builder.create();
                            View view = inflater.inflate(R.layout.alertdialog_update, null);
                            TextView success = view.findViewById(R.id.payAlertdialogSuccess);
                            success.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    PermissionX.init(Main.this)
                                            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                                            .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                                            .request((allGranted, grantedList, deniedList) -> {
                                                if (allGranted) {
                                                    new DownLoadAndInstallApp(activity, Android_url, "myApp.apk");
                                                    MyToast.showTheToast(activity, "???????????????????????????????????????");
                                                    mAlertDialog.dismiss();
                                                } else {
                                                    MyToast.showTheToast(Main.this, "?????????????????????????????? ");
                                                }
                                            });
                                }
                            });
                            TextView error = view.findViewById(R.id.payAlertdialogError);
                            error.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    mAlertDialog.dismiss();
                                }
                            });
                            mAlertDialog.setCancelable(false);
                            mAlertDialog.show();
                            mAlertDialog.getWindow().setContentView(view);
                        }
                    } else {
                        PackageManager manager = activity.getPackageManager();
                        PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
                        final int versionCodeLocal = info.versionCode;
                        if (Android_code_int > versionCodeLocal) {
                            PermissionX.init(Main.this)
                                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "???????????????????????????????????????????????????", "??????", "??????"))
                                    .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "???????????????????????????????????????????????????????????????????????????????????????", "??????", "??????"))
                                    .request((allGranted, grantedList, deniedList) -> {
                                        if (allGranted) {
                                            new DownLoadAndInstallApp(activity, Android_url, "myApp.apk");
                                            MyToast.showTheToast(activity, "????????????APP???????????????");

                                            LayoutInflater inflater = LayoutInflater.from(activity);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                            final AlertDialog mAlertDialog = builder.create();
                                            View view = inflater.inflate(R.layout.alertdialog_update1, null);
                                            mAlertDialog.setCancelable(false);
                                            mAlertDialog.show();
                                            mAlertDialog.getWindow().setContentView(view);
                                        } else {
                                            MyToast.showTheToast(Main.this, "?????????????????????????????? ");
                                        }
                                    });
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    @UiThread
    void httpGetAllMarker() {
        Log.d(TAG, "??????httpGetAllMarker??????????????????: ");
        String jingdu = longitude == 0 ? "" : longitude.toString();
        String weidu = latitude == 0 ? "" : latitude.toString();
        String type_ = String.valueOf(type);
        String tab_ = String.valueOf(tab);
        Disposable disposable = RetrofitClient.getInstance().getApiService().mapJwduV5(jingdu, weidu, type_, tab_)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(this::onDataHttpGetAllMarker, throwable -> throwable.printStackTrace(System.err));
    }
    private ArrayList<Marker> markers = new ArrayList<>();
    @UiThread
    void onDataHttpGetAllMarker(MapJwduV5Bean mapBean) {
//        show_bluetooth = mapBean.getHorn();
//        blue_electric.setVisibility("1".equals(mapBean.getHorn()) ? View.VISIBLE : View.GONE);
//        if (!TextUtils.isEmpty(uid)) {
//            HttpGetMainInfo();
//        }
        dataArrayList = new ArrayList<>();
        if (mapBean != null && mapBean.getData() != null) {
            dataArrayList.addAll(mapBean.getData());
        }
        clearMarkers();
        //????????????????????????
        if (myLocationStyle != null && aMap != null) {
            aMap.setMyLocationStyle(myLocationStyle);
        } else {
            setupLocationStyle();
        }
        int status = mapBean.getStatus();
        String msg = mapBean.getMsg();
        if (status == 1) {
            isFirstIn = false;
            markerList = new LinkedList<>();
            imgList = mapBean.getImg_list();
            List<MapJwduV5Bean.DataBean> dataBeanList = mapBean.getData();
            for(int i = 0 ; i < dataBeanList.size() ; i++){
//                String jingdu = dataBeanList.get(i).getJingdu() +"";
//                String weidu = dataBeanList.get(i).getWeidu() +"";
//                ArrayList<MapJwduV5Bean.DataBean.ListBean> mapSiteItemArrayList = new ArrayList<>(dataArrayList.get(i).getList());
//
//                MarkerOptions markerOption = new MarkerOptions();
//                markerOption.position(new LatLng(Double.parseDouble(weidu),Double.parseDouble(jingdu)));
//                markerOption.draggable(false);//??????Marker?????????
//
//                markerOption.icon(new MakerUtils().chargeSiteMarker(activity,mapSiteItemArrayList));
//                markerOption.setFlat(true);//??????marker??????????????????
//                Marker marker = aMap.addMarker(markerOption);
//                markers.add(marker);
            }
//            for (int i = 0; i < dataBeanList.size(); i++) {
//                MapJwduV5Bean.DataBean dataBean = dataBeanList.get(i);
//                List<MapJwduV5Bean.DataBean.ListBean> listBeanList = dataBean.getList();
//                int lint_type = dataBean.getLint_type();
//                double jingdu = dataBean.getJingdu();
//                double weidu = dataBean.getWeidu();
//                //???????????????marker???
//                int currentCount = listBeanList.size();
//                for (int j = 0; j < currentCount; j++) {
//                    MapJwduV5Bean.DataBean.ListBean listBean = listBeanList.get(j);
//                    Map<String, Object> innerMap = new LinkedHashMap<>();
//                    innerMap.put("id", listBean.getId());
//                    innerMap.put("repair", listBean.getRepair());
//                    innerMap.put("jingdu", listBean.getJingdu());
//                    innerMap.put("weidu", listBean.getWeidu());
//                    markerList.add(innerMap);
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.setFlat(true);
//                    markerOptions.anchor(0.5f, 1.0f);
//                    LatLng latLng = new LatLng(weidu, jingdu);
//                    markerOptions.position(latLng);
//                    float angle = (currentCount - 1) * (-36f) + j * 72;
//                    markerOptions.rotateAngle(angle);
//                    markerOptions.zIndex(-lint_type);
//                    markerOptions.infoWindowEnable(false);
//                    markerOptions.setFlat(true);
//                    customizeMarkerIcon(listBean, view -> {
//                        if (aMap != null) {
//                            markerOptions.icon(bitmapDescriptor);
//                            Marker marker = aMap.addMarker(markerOptions);
//                            marker.setObject(innerMap);
//                        }
//                    });
//                }
//            }
            //????????????
            if (!isDestroy(Main.this)) {
                if (TextUtils.isEmpty(uid)) {
                    Glide.with(this).load(R.drawable.icon_main_user).into(iv_user_headPortrait);
                    if (!TextUtils.isEmpty(mapBean.getAvater())) {
                        Glide.with(this).load(mapBean.getAvater()).into(iv_user_headPortrait);
                    }
                }
            }
        } else {
            MyToast.showTheToast(this, msg);
        }
        isRequestMarkerNow = false;
    }

    /**
     * ??????Activity??????Destroy
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * by tianyanyu on 2019/08/23
     * func:??????marker??????
     */
    private void customizeMarkerIcon(MapJwduV5Bean.DataBean.ListBean listBean, final OnMarkerIconLoadListener listener) {
        int open_door = listBean.getOpen_door();
        int usable = listBean.getUsable();
        int btype = listBean.getBtype();
        int img_idx = listBean.getImg_idx();
        if (open_door == 0) {
            img_idx = 2;
            usable = 0;
        }
        View markerView = LayoutInflater.from(this).inflate(R.layout.marker_view, null);
        TextView marker_layout_textView = markerView.findViewById(R.id.marker_layout_textView);
        ImageView marker_layout_imageView = markerView.findViewById(R.id.marker_layout_imageView);
        //??????????????????
        marker_layout_textView.setText(String.valueOf(usable));
        if (img_idx == 2 || img_idx == 5 || img_idx == 6) {
            marker_layout_textView.setText("");
        }
        if (btype == 1) {//?????????
            marker_layout_textView.setTextColor(Color.parseColor("#ff9320"));
        } else if (btype == 2) {//?????????
            marker_layout_textView.setTextColor(Color.parseColor("#79512a"));
        } else if (btype == 3) {//?????????
            marker_layout_textView.setTextColor(Color.parseColor("#4f558f"));
        } else {//?????????
            marker_layout_textView.setTextColor(Color.parseColor("#fb2d08"));
        }
        //??????????????????
        Glide.with(getApplicationContext())
                .load(imgList.get(img_idx))
                .thumbnail(0.2f)
                .centerCrop()
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        try {
                            Bitmap bitmap = drawableToBitmap(resource);
                            marker_layout_imageView.setImageBitmap(bitmap);
                            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewUtil.convertViewToBitmap(markerView));
                            listener.markerIconLoadingFinished(markerView);
                        } catch (Exception e) {
                        }
                    }
                });
        /*???????????????????????????????????????????????? Glide.with(getApplicationContext())
                .load(imgList.get(img_idx))
                .thumbnail(0.2f)
                .centerCrop()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        try {
                            Bitmap bitmap = drawableToBitmap(resource);
                            marker_layout_imageView.setImageBitmap(bitmap);
                            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewUtil.convertViewToBitmap(markerView));
                            listener.markerIconLoadingFinished(markerView);
                        } catch (Exception e) {
                        }
                    }
                });*/
    }

    /**
     * ??????
     */
    private void clearMarkers() {
        if (aMap != null) {
            aMap.clear();
        }
        if (markerList != null) {
            markerList.clear();
        }
    }

    /**
     * ??????????????????
     * http?????????Advices/numsV3.html
     */
    @Background
    void HttpGetPullActivityIndex() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("lng", longitude == 0 ? "" : longitude.toString()));
        dataList.add(new ParamTypeData("lat", latitude == 0 ? "" : latitude.toString()));
        new OkHttpConnect(activity, PubFunction.app + "PullActivity/index.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetPullActivityIndex(response, type);
            }
        }).startHttpThread();
    }

    /**
     * ?????????????????????????????? UI??????
     */
    @UiThread
    void onDataHttpGetPullActivityIndex(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            PullActivityIndexBean pullActivityIndexBean = new Gson().fromJson(response, PullActivityIndexBean.class);
            if (pullActivityIndexBean != null) {
                if ("1".equals(pullActivityIndexBean.getStatus())) {
                    PullActivityIndexBean.ActivityBean bean = pullActivityIndexBean.getActivity();
                    if (bean != null) {
                        if (bean.getAid() != null) {
                            //????????????????????????
                            if ("1".equals(bean.getBig_show())) {
                                initInviteDialog(bean);
                            } else {
                                HttpGetMainInfo();
                                if ("1".equals(bean.getSmall_show())) {
                                    tv_main_invite_small.setVisibility(View.VISIBLE);
                                    Glide.with(Main.this).load(bean.getSmall_url()).into(tv_main_invite_small);
                                    tv_main_invite_small.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (TextUtils.isEmpty(uid)) {
//                                                Intent intent = new Intent(Main.this, InviteActivity.class);
//                                                intent.putExtra("url", bean.getBig_jump());
//                                                intent.putExtra("inviteId", bean.getAid());
//                                                startActivity(intent);
                                            } else {
                                                isInviteDialogShowed = false;
                                                startActivity(new Intent(Main.this, LoginActivity.class));
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            //????????????
                            HttpGetMainInfo();
                        }
                    }
                } else {
                    HttpGetMainInfo();
                }
            }
        }
    }

    /**
     * ??????????????????
     * http?????????Advices/numsV3.html
     */
    @Background
    void HttpGetMainInfo() {
        if (TextUtils.isEmpty(uid)) {
            return;
        }
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("phone", sharedPreferences.getString("phone", "")));
        new OkHttpConnect(activity, PubFunction.app + "Advices/numsV3.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetMainInfo(response, type);
            }
        }).startHttpThread();
    }

    /**
     * ?????????????????????????????? UI??????
     */
    @UiThread
    void onDataHttpGetMainInfo(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
            nor_read_red_point_panel.setVisibility(View.GONE);
        } else {
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                AdvicesNumsV2Bean mAdvicesNumsV2Bean = new Gson().fromJson(response, AdvicesNumsV2Bean.class);
                if (mAdvicesNumsV2Bean != null) {
                    if ("1".equals(mAdvicesNumsV2Bean.getStatus())) {
                        AdvicesNumsV2Bean.DataBean data = mAdvicesNumsV2Bean.getData();
                        if (data != null) {
                            //?????????????????????
                            setNotReadPoint(data.getNoReadNum());
                            //????????????????????????
                            if (data.getList() != null) {
                                listAdvicesBean.clear();
                                listAdvicesBean.addAll(data.getList());
                                initDialogManager();
                            }
                        }
                    } else {
                        if (jsonObjectResponse.has("is_exit")) {
                            String isExit = jsonObjectResponse.getString("is_exit");
                            if ("1".equals(isExit)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                            }
                        }
                        MyToast.showTheToast(activity, mAdvicesNumsV2Bean.getMsg());
                        nor_read_red_point_panel.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                nor_read_red_point_panel.setVisibility(View.GONE);
            }
        }
    }

    /**
     * ?????????????????????
     */
    private void setNotReadPoint(String noReadNum) {
        int advicesNum = 0;
        if (!TextUtils.isEmpty(noReadNum)) {
            advicesNum = Integer.parseInt(noReadNum);
        }
        if (advicesNum > 0) {
            nor_read_red_point_panel.setVisibility(View.VISIBLE);
            String showNumStr;
            if (advicesNum > 99) {
                showNumStr = "99+";
            } else {
                showNumStr = advicesNum + "";
            }
            nor_read_red_point.setText(showNumStr);
        } else {
            nor_read_red_point_panel.setVisibility(View.GONE);
        }
    }

    /**
     * ????????????????????????
     * ?????????????????????20????????????10????????????11????????????12
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????5????????????
     */
    private void initDialogManager() {
        for (int i = 0; i < listAdvicesBean.size(); i++) {
            String scene = listAdvicesBean.get(i).getScene();
            if ("20".equals(scene)) {
                showActivityMessageDialog(listAdvicesBean.get(i), i);
                return;
            }
            if ("10".equals(scene)) {
                showSceneFirstDialog(listAdvicesBean.get(i), i);
                return;
            }
            if ("11".equals(scene)) {
                showSceneSecondDialog(listAdvicesBean.get(i), i);
                return;
            }
            if ("12".equals(scene)) {
                showSceneThirdDialog(listAdvicesBean.get(i), i);
                return;
            }
        }
    }

    /**
     * ????????????
     */
    private void dismissDialogManager(TDialog tDialog, int position) {
        tDialog.dismiss();
        if (listAdvicesBean != null) {
            if (listAdvicesBean.size() >= position + 1) {
                listAdvicesBean.remove(position);
            }
        }
        initDialogManager();
    }

    /**
     * ????????????????????????
     */
    private void sendMsgRead(String msgIds) {
        Disposable disposable = RetrofitClient.getInstance().getApiService().advicesRead(msgIds)
                .compose(RxScheduler.Flo_io_main()).subscribe(new Consumer<AdvicesReadBean>() {
                    @Override
                    public void accept(AdvicesReadBean bean) throws Exception {
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                if (bean.getData() != null) {
                                    setNotReadPoint(bean.getData().getNo_read_num());
                                }
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private void initInviteDialog(PullActivityIndexBean.ActivityBean bean) {
        TDialog inviteDialog = new TDialog.Builder(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_main_invite)
                .setScreenHeightAspect(this, 0.75f)
                .setScreenWidthAspect(this, 0.9f)
                .setDialogAnimationRes(R.style.animate_dialog_scale)
                .setOnBindViewListener(viewHolder -> {
                    ImageView tvIconDialogInvite = viewHolder.getView(R.id.tv_icon_dialog_invite);
                    if (bean != null) {
                        if (!TextUtils.isEmpty(bean.getBig_url())) {
                            Glide.with(this).load(bean.getBig_url()).into(tvIconDialogInvite);
                        }
                    }
                })
                .addOnClickListener(R.id.tv_icon_dialog_invite, R.id.iv_icon_dialog_invite_close)
                .setOnViewClickListener((viewHolder, view, tDialog) -> {
                    switch (view.getId()) {
                        case R.id.tv_icon_dialog_invite:
                            if (!TextUtils.isEmpty(uid)) {
                                if (bean != null) {
                                    if ("1".equals(bean.getBig_click())) {
//                                        Intent intent = new Intent(this, InviteActivity.class);
//                                        intent.putExtra("url", bean.getBig_jump());
//                                        intent.putExtra("inviteId", bean.getAid());
//                                        startActivity(intent);
                                    }
                                }
                            } else {
                                isInviteDialogShowed = false;
                                startActivity(new Intent(Main.this, LoginActivity.class));
                            }
                            tDialog.dismiss();
                            break;
                        case R.id.iv_icon_dialog_invite_close:
                            tDialog.dismiss();
                            break;
                        default:
                            break;
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if ("1".equals(bean.getSmall_show())) {
                            tv_main_invite_small.setVisibility(View.VISIBLE);
                            Glide.with(Main.this).load(bean.getSmall_url()).into(tv_main_invite_small);
                            tv_main_invite_small.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (!TextUtils.isEmpty(uid)) {
//                                        Intent intent = new Intent(Main.this, InviteActivity.class);
//                                        intent.putExtra("url", bean.getBig_jump());
//                                        intent.putExtra("inviteId", bean.getAid());
//                                        startActivity(intent);
                                    } else {
                                        isInviteDialogShowed = false;
                                        startActivity(new Intent(Main.this, LoginActivity.class));
                                    }
                                }
                            });
                        }
                        HttpGetMainInfo();
                    }
                })
                .setDimAmount(0.34f)
                .setCancelableOutside(false)
                .create()
                .show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PubFunction.isForeground(Main.this, "Main")) {
                    inviteDialog.dismiss();
                }
            }
        }, Integer.parseInt(bean.getDtime()) * 1000);
    }

    /**
     * ??????
     * ????????????20
     */
    public void showActivityMessageDialog(AdvicesNumsV2Bean.ListBean bean, int position) {
        if (dialogActivityMessage != null) {
            if (!dialogActivityMessage.getShowsDialog()) {
                dialogActivityMessage.show();
            }
        } else {
            dialogActivityMessage = new TDialog.Builder(getSupportFragmentManager())
                    .setLayoutRes(R.layout.dialog_home_ad)
                    .setScreenHeightAspect(this, 0.75f)
                    .setScreenWidthAspect(this, 0.9f)
                    .setDialogAnimationRes(R.style.animate_dialog_scale)
                    .setOnBindViewListener(viewHolder -> {
                        ImageView iv = viewHolder.getView(R.id.imageView);
                        if (bean != null) {
                            if (!TextUtils.isEmpty(bean.getImages())) {
                                Glide.with(this).load(bean.getImages()).into(iv);
                            }
                        }
                    })
                    .addOnClickListener(R.id.button, R.id.btn_str_see_others)
                    .setOnViewClickListener((viewHolder, view, tDialog) -> {
                        switch (view.getId()) {
                            case R.id.button:
                                if (bean != null) {
                                    if ("1".equals(bean.getIs_jump())) {
                                        if ("0".equals(bean.getIs_read())) {
                                            sendMsgRead(new Gson().toJson(bean.getIds()));
                                        }
                                        Intent intent = new Intent(this, MainAdv.class);
                                        intent.putExtra("url", bean.getJump_to());
                                        startActivity(intent);
                                    }
                                }
                                dismissDialogManager(tDialog, position);
                                break;
                            case R.id.btn_str_see_others:
                                clickSceneCancel(bean, tDialog, position);
                                break;
                            default:
                                break;

                        }
                    })
                    .setDimAmount(0.34f)
                    .setCancelableOutside(false)
                    .create()
                    .show();
        }
    }

    /**
     * ??????????????????
     * ????????? ???????????????????????????
     */
    public void showSceneFirstDialog(AdvicesNumsV2Bean.ListBean bean, int position) {
        if (dialogSceneFirst != null) {
            if (!dialogSceneFirst.getShowsDialog()) {
                dialogSceneFirst.show();
            }
        } else {
            View view1 = View.inflate(this, R.layout.dialog_main_scene_first_layout, null);
            TextView tvTitle = view1.findViewById(R.id.tv_main_scene_first_title);
            TextView tvContent = view1.findViewById(R.id.tv_main_scene_first_content);
            tvTitle.setText(bean.getTitle());
            if (bean.getContent() != null) {
                tvContent.setText(bean.getContent().get(0));
            }
            dialogSceneFirst = new TDialog.Builder(getSupportFragmentManager())
                    .setDialogView(view1)
                    .setScreenWidthAspect(this, 1.0f)
                    .addOnClickListener(R.id.btn_i_got_it)
                    .setOnViewClickListener((viewHolder, view, tDialog) -> {
                        if ("0".equals(bean.getIs_read())) {
                            sendMsgRead(new Gson().toJson(bean.getIds()));
                        }
                        if ("1".equals(bean.getIs_jump())) {
                            Intent intent = new Intent(this, MainAdv.class);
                            intent.putExtra("url", bean.getJump_to());
                            startActivity(intent);
                        }
                        dismissDialogManager(tDialog, position);
                    })
                    .setDimAmount(0.34f)
                    .setCancelableOutside(false)
                    .create()
                    .show();
        }
    }

    /**
     * ??????????????????
     * ??????????????????????????? ?????????/??????/????????????????????? ????????????
     */
    public void showSceneSecondDialog(AdvicesNumsV2Bean.ListBean bean, int position) {
        if (dialogSceneSecond != null) {
            if (!dialogSceneSecond.getShowsDialog()) {
                dialogSceneSecond.show();
            }
        } else {
            View view1 = View.inflate(this, R.layout.dialog_main_scene_second_layout, null);
            TextView tvTitle = view1.findViewById(R.id.tv_main_scene_second_title);
            RecyclerView rvContent = view1.findViewById(R.id.rv_main_scene_second_content);
            rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            tvTitle.setText(bean.getTitle());
            MainSceneDialogSecondAdapter rvAdapter = new MainSceneDialogSecondAdapter(bean.getContent());
            rvContent.setAdapter(rvAdapter);
            dialogSceneSecond = new TDialog.Builder(getSupportFragmentManager())
                    .setDialogView(view1)
                    .setScreenWidthAspect(this, 1.0f)
                    .addOnClickListener(R.id.btn_str_see_others, R.id.btn_right_press)
                    .setOnViewClickListener((viewHolder, view, tDialog) -> {
                        switch (view.getId()) {
                            case R.id.btn_str_see_others:
                                clickSceneCancel(bean, tDialog, position);
                                break;
                            case R.id.btn_right_press:
                                initJumpEvent(tDialog, bean);
                                break;
                            default:
                                break;
                        }
                    })
                    .setDimAmount(0.34f)
                    .setCancelableOutside(false)
                    .create()
                    .show();
        }
    }

    /**
     * ??????????????????
     * ??????????????????????????? ????????????????????????????????? ????????????
     */
    public void showSceneThirdDialog(AdvicesNumsV2Bean.ListBean bean, int position) {
        if (dialogSceneThird != null) {
            if (!dialogSceneThird.getShowsDialog()) {
                dialogSceneThird.show();
            }
        } else {
            View view1 = View.inflate(this, R.layout.dialog_main_scene_third_layout, null);
            TextView tvTitle = view1.findViewById(R.id.tv_main_scene_third_title);
            TextView tvContent = view1.findViewById(R.id.tv_main_scene_third_content);
            tvTitle.setText(bean.getTitle());
            if (bean.getContent() != null) {
                tvContent.setText(bean.getContent().get(0));
            }
            dialogSceneThird = new TDialog.Builder(getSupportFragmentManager())
                    .setDialogView(view1)
                    .setScreenWidthAspect(this, 1.0f)
                    .addOnClickListener(R.id.btn_str_see_others, R.id.btn_right_press)
                    .setOnViewClickListener((viewHolder, view, tDialog) -> {
                        switch (view.getId()) {
                            case R.id.btn_str_see_others:
                                clickSceneCancel(bean, tDialog, position);
                                break;
                            case R.id.btn_right_press:
                                initJumpEvent(tDialog, bean);
                                break;
                            default:
                                break;
                        }
                    })
                    .setDimAmount(0.34f)
                    .setCancelableOutside(false)
                    .create()
                    .show();
        }
    }

    /**
     * ?????????Event????????????
     * ???????????????ialogd?????????????????????????????????????????????
     */
    public void initJumpEvent(TDialog tDialog, AdvicesNumsV2Bean.ListBean bean) {
        if ("0".equals(bean.getIs_read())) {
            sendMsgRead(new Gson().toJson(bean.getIds()));
        }
        intentMainAdv(bean.getBtn_right());
        tDialog.dismiss();
    }

    /**
     * ??????????????????
     */
    private void intentMainAdv(AdvicesNumsV2Bean.ClickBtnBean rightBtnBean) {
        if (rightBtnBean != null) {
            if ("1".equals(rightBtnBean.getIs_jump())) {
                if ("Wallet/v".equals(rightBtnBean.getJump_to())) {
//                    startActivity(new Intent(activity, NewWalletActivity.class));
                } else if ("Goods/buy".equals(rightBtnBean.getJump_to())) {
//                    Intent intent = new Intent(activity, MainShop_.class);
//                    AdvicesNumsV2Bean.ParamBean paramBean = rightBtnBean.getParam();
//                    if (paramBean != null) {
//                        intent.putExtra(MainShop.MSG_OTYPE, paramBean.getOtype());
//                        intent.putExtra(MainShop.MSG_FROMS, paramBean.getFroms());
//                        intent.putExtra(MainShop.MSG_MERID, paramBean.getMerid());//"376"|paramBean.getMerid()
//                    }
//                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, MainAdv.class);
                    intent.putExtra("url", rightBtnBean.getJump_to());
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    private void clickSceneCancel(AdvicesNumsV2Bean.ListBean bean, TDialog tDialog, int position) {
        if (bean != null) {
            if ("0".equals(bean.getIs_read())) {
                sendMsgRead(new Gson().toJson(bean.getIds()));
            }
        }
        dismissDialogManager(tDialog, position);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     */
    @Override
    public void onSuccess(CabinetDetailBean bean) {
        if (bean.getStatus() == 1) {
            //?????????????????????
            BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_SITE_DETAILS);

            CabinetDetailBean.DataBean dataBean = bean.getData();
            int is_skip = bean.getIs_skip();
            String tips = bean.getTips();
            //number
            TextView tvSiteInfoNumber = findViewById(R.id.tv_siteInfo_number);
            tvSiteInfoNumber.setText(String.valueOf(dataBean.getUsable()));
            //name
            TextView tvSiteInfoName = findViewById(R.id.tv_siteInfo_name);
            tvSiteInfoName.setText(getResources().getString(R.string.siteinfo_textview3, dataBean.getName(), dataBean.getCabno()));
            //otime
            TextView tvSiteInfoTime = findViewById(R.id.tv_siteInfo_time);
            tvSiteInfoTime.setText(dataBean.getOtime());
            //address
            TextView tvSiteInfoAddress = findViewById(R.id.tv_site_info_address);
            tvSiteInfoAddress.setText(dataBean.getAddress() + " ???");

            //tags
            AlignLeftFlowLayout siteInfo_flowLayout = findViewById(R.id.siteInfo_flowLayout);
            //??????tag
            siteInfo_flowLayout.removeAllViews();
            List<CabinetDetailBean.DataBean.TagsBean> tagsBeans = dataBean.getTags();
            for (CabinetDetailBean.DataBean.TagsBean tagsBean : tagsBeans) {
                TextView tagsbean_item_textView1 = (TextView) View.inflate(Main.this, R.layout.tagsbean_item, null);
                tagsbean_item_textView1.setText(tagsBean.getName());
                tagsbean_item_textView1.setTextColor(Color.parseColor("#" + tagsBean.getColor()));
                //??????????????????
                Glide.with(this)
                        .load(tagsBean.getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                Drawable newResource = Util.zoomDrawable(resource, ViewUtil.dp2px(Main.this, 30), ViewUtil.dp2px(Main.this, 30));
                                tagsbean_item_textView1.setCompoundDrawablesWithIntrinsicBounds(newResource, null, null, null);
                            }
                        });
                /*????????????????????????????????????????????????
                Glide.with(this)
                        .load(tagsBean.getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                Drawable newResource = Util.zoomDrawable(resource, ViewUtil.dp2px(Main.this, 30), ViewUtil.dp2px(Main.this, 30));
                                tagsbean_item_textView1.setCompoundDrawablesWithIntrinsicBounds(newResource, null, null, null);
                            }
                        });*/
                siteInfo_flowLayout.addView(tagsbean_item_textView1);
            }
            //????????????tag
            TextView tagsbean_item_textView1 = (TextView) View.inflate(Main.this, R.layout.tagsbean_item, null);
            tagsbean_item_textView1.setText(getString(R.string.km, dataBean.getDistance() + ""));
            Drawable drawableLeft = getResources().getDrawable(R.mipmap.distance);
            Drawable newResource = Util.zoomDrawable(drawableLeft, ViewUtil.dp2px(Main.this, 30), ViewUtil.dp2px(Main.this, 30));
            tagsbean_item_textView1.setCompoundDrawablesWithIntrinsicBounds(newResource, null, null, null);
            siteInfo_flowLayout.addView(tagsbean_item_textView1);

            //bty_rate
            TextView siteInfo_textView6 = findViewById(R.id.siteInfo_textView6);
            TextView siteInfo_textView7 = findViewById(R.id.siteInfo_textView7);
            TextView siteInfo_textView8 = findViewById(R.id.siteInfo_textView8);
            TextView siteInfo_textView6_1 = findViewById(R.id.siteInfo_textView6_1);
            TextView siteInfo_textView7_1 = findViewById(R.id.siteInfo_textView7_1);
            TextView siteInfo_textView8_1 = findViewById(R.id.siteInfo_textView8_1);
            List<CabinetDetailBean.DataBean.BtyRateBean> btyRateBeans = dataBean.getBty_rate();
            siteInfo_textView6.setText(String.valueOf(btyRateBeans.get(0).getNum()));
            siteInfo_textView6_1.setText(btyRateBeans.get(0).getName());
            siteInfo_textView7.setText(String.valueOf(btyRateBeans.get(1).getNum()));
            siteInfo_textView7_1.setText(btyRateBeans.get(1).getName());
            siteInfo_textView8.setText(String.valueOf(btyRateBeans.get(2).getNum()));
            siteInfo_textView8_1.setText(btyRateBeans.get(2).getName());
            findViewById(R.id.line_siteInfo_textView9).setVisibility(View.GONE);
            findViewById(R.id.li_siteInfo_textView9).setVisibility(View.GONE);

            myScrollView.A_1();

            if (viewList.size() > 0) {
                ll_point_container.removeAllViews();
                viewList.clear();
            }
            List<String> imagesList = dataBean.getImages();
            if (imagesList.size() > 0) {
                ll_point_container.setVisibility(View.VISIBLE);
            } else {
                ll_point_container.setVisibility(View.INVISIBLE);
            }
            viewpager_count = imagesList.size();
            imageViews = new ImageView[viewpager_count];
            for (int i = 0; i < imagesList.size(); i++) {
                View view_flipper = LayoutInflater.from(getApplicationContext()).inflate(R.layout.main_index_top, null);
                ImageView imageView = view_flipper.findViewById(R.id.banner);
                Glide.with(this).load(imagesList.get(i)).into(imageView);
                viewList.add(view_flipper);
                View point_panel = LayoutInflater.from(getApplicationContext()).inflate(R.layout.main_index_top_point, null);
                imageViews[i] = point_panel.findViewById(R.id.point);
                if (i == 0) {
                    imageViews[i].setAlpha(1f);
                } else {
                    imageViews[i].setAlpha(0.5f);
                }
                ll_point_container.addView(point_panel);
            }
            viewPager.setAdapter(new MyViewPagerAdapter(viewList));
            viewPager.setCurrentItem(0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    for (int i = 0; i < viewpager_count; i++) {
                        if (viewPager.getCurrentItem() == i) {
                            imageViews[i].setAlpha(1f);
                        } else {
                            imageViews[i].setAlpha(0.5f);
                        }
                    }
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            findViewById(R.id.btn_site_info_comment).setOnClickListener(v -> {
                //??????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SITE_DETAILS_COMMENT);
                if (!uid.isEmpty()) {
                    requestHttpVisitLogs("????????????-??????", "114");
//                    Intent intent = new Intent(Main.this, EvaluationListsActivity.class);
//                    intent.putExtra("cabid", dataBean.getCabid());
//                    intent.putExtra("name", dataBean.getName());
//                    intent.putExtra("repair", repair);
//                    startActivity(intent);
                } else {
                    showMessage("?????????????????????");
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
            });

            findViewById(R.id.btn_site_info_shop).setOnClickListener(v -> {
                //??????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SITE_DETAILS_SHOPPING_MALL);
                if (!uid.isEmpty()) {
                    requestHttpVisitLogs("????????????-??????", "112");
                    if (0 == is_skip || 1 == is_skip) {
                        //is_skip 0???????????????1????????????
//                        Intent intent = new Intent(Main.this, MainShopFromCabinet_.class);
//                        intent.putExtra("merid", dataBean.getMerid());
//                        intent.putExtra("is_skip", is_skip + "");
//                        intent.putExtra("froms", "10");
//                        startActivity(intent);
                    } else {
                        //?????????????????????
                        MyToast.showTheToast(activity, tips);
                    }
                } else {
                    showMessage("?????????????????????");
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
            });

            findViewById(R.id.btn_site_info_mobile).setOnClickListener(v -> {
                //??????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SITE_DETAILS_TELEPHONE);
                phoneDialog(dataBean.getMphone());
                requestHttpVisitLogs("????????????-??????", "115");
            });

            findViewById(R.id.btn_site_info_navigation).setOnClickListener(v -> {
                //??????????????????
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_SITE_DETAILS_NAVIGATION);
                MapUtil.showNavigationDialog(Main.this, getSupportFragmentManager()
                        , getResources().getString(R.string.siteinfo_textview3, dataBean.getName(), dataBean.getCabno())
                        , dataBean.getWeidu(), dataBean.getJingdu());
            });

            tvSiteInfoAddress.setOnClickListener(v -> {
                MapUtil.showNavigationDialog(Main.this, getSupportFragmentManager()
                        , getResources().getString(R.string.siteinfo_textview3, dataBean.getName(), dataBean.getCabno())
                        , dataBean.getWeidu(), dataBean.getJingdu());
            });
        } else {
            showMessage(bean.getMsg());
        }
    }

    @Override
    public void requestCheckAccv2Success(LoginCheckAccv2Bean bean) {
        MyToast.showTheToast(this, bean.getMsg());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_avatar", "");
        editor.clear();
        editor.apply();
        if (drawerMenu != null) {
            if (drawerMenu.isShowing()) {
                drawerMenu.dismiss();
            }
        }
        //????????????????????????
        Main.handleRefreshMarker.sendEmptyMessage(1);
//        MyToast.showTheToast(this, "????????????????????????????????????");
        startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    public void requestCheckAccv2Error(String msg) {
    }

    private void requestHttpVisitLogs(String titleStr, String typeStr) {
        if (!uid.isEmpty()) {
            List<ParamTypeData> dataList1 = new ArrayList<>();
            dataList1.add(new ParamTypeData("title", titleStr));
            dataList1.add(new ParamTypeData("sourceType", "5"));
            dataList1.add(new ParamTypeData("sourceId", uid));
            dataList1.add(new ParamTypeData("type", typeStr));
            dataList1.add(new ParamTypeData("client", "Android"));
            HttpVisitLogs(dataList1);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //TODO ??????????????????
        myScrollView.A_1_close();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //TODO ??????????????????
        myScrollView.A_1_close();
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //????????????
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (Build.VERSION.SDK_INT >= 23) {
                isNeedCheck = true;
//                checkPermissions(needPermissions);
            } else {
                startLocation();
            }
        } else if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "afterview: ??????????????????");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        scanLeDevice(true);
                        Log.d(TAG, "onReceive: 456456456456456456");
                        new BluetoothDeviceList(Main.this);
//                        new BluetoothMain(Main.this);
                    }
                }, 2000);
            }
        }
    }

    @Override
    public void onFinish() {
        Log.i("052003", "onFinish");
    }

    @Override
    public void onCancel() {
        Log.i("052003", "onCancel");
        aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location1, 18, 0, 0)), 999, Main.this);
    }

    public void resetSitesBackground() {
        site0_linearLayout.setBackgroundResource(R.drawable.button_corners_white_gray_radius_5_width_1);
        site0_imageView.setBackgroundResource(R.mipmap.site0);
        site0_textView.setTextColor(Color.parseColor("#FC481E"));
        //?????????
        site1_linearLayout.setBackgroundResource(R.drawable.site1_normal);
        site1_imageView.setBackgroundResource(R.mipmap.site1);
        site1_textView.setTextColor(Color.parseColor("#FC481E"));
        //??????
        site2_linearLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        site2_imageView.setBackgroundResource(R.mipmap.site2);
        site2_textView.setTextColor(Color.parseColor("#FFA800"));
        //?????????
        site4_linearLayout.setBackgroundResource(R.drawable.site4_normal);
        site4_imageView.setBackgroundResource(R.mipmap.site4);
        site4_textView.setTextColor(Color.parseColor("#4E5791"));
    }

    @Background
    void HttpVisitLogs(List<ParamTypeData> maidian_dataList) {
        new OkHttpConnect(activity, PubFunction.api + "VisitLogs/index.html", maidian_dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onVisitLogs(response, type);
            }
        }).startHttpThread();
    }

    @UiThread
    void onVisitLogs(String response, String type) {
        if ("0".equals(type)) {
            System.out.println(response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                System.out.println(msg);
            } catch (JSONException e) {
                System.out.println(e.toString());
            }
        }
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

    /**
     * by tianyanyu on 2019/08/23
     * func:???????????????????????????marker???icon???????????????????????????marker??????
     */
    public interface OnMarkerIconLoadListener {
        void markerIconLoadingFinished(View view);
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        MyViewPagerAdapter(List<View> mListViews) {
            //???????????????????????????????????????????????????????????????
            this.mListViews = mListViews;
        }

        /**
         * ????????????PagerAdapter????????????????????????????????????????????????????????????
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (position < mListViews.size()) {
                //????????????
                container.removeView(mListViews.get(position));
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //????????????????????????????????? ????????????
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            //?????????????????????
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            //?????????????????????
            return arg0 == arg1;
        }
    }

    public void phoneDialog(String mphone) {
        View view1 = View.inflate(this, R.layout.layout_phone, null);
        TextView tv_open_album = view1.findViewById(R.id.tv_open_album);
        tv_open_album.setText(mphone);
        new TDialog.Builder(getSupportFragmentManager())
                .setDialogView(view1)
                .setScreenWidthAspect(this, 1.0f)
                .setGravity(Gravity.BOTTOM)
                .setDialogAnimationRes(R.style.animate_dialog)
                .addOnClickListener(R.id.tv_open_camera, R.id.tv_open_album, R.id.tv_cancel)
                .setOnViewClickListener((viewHolder, view, tDialog) -> {
                    switch (view.getId()) {
                        case R.id.tv_open_camera:
                        case R.id.tv_open_album:
                            phoneDialog2(mphone);
                        case R.id.tv_cancel:
                            tDialog.dismiss();
                            break;
                    }
                })
                .create()
                .show();
    }

    public void phoneDialog2(String mphone) {
        View view1 = View.inflate(this, R.layout.layout_phone2, null);
        TextView dialog_call_textview1 = view1.findViewById(R.id.dialog_call_textview1);
        dialog_call_textview1.setText(mphone);
        new TDialog.Builder(getSupportFragmentManager())
                .setDialogView(view1)
                .setScreenWidthAspect(this, 0.7f)
                .addOnClickListener(R.id.dialog_call_confirm, R.id.dialog_call_cancel)
                .setOnViewClickListener((viewHolder, view, tDialog) -> {
                    switch (view.getId()) {
                        case R.id.dialog_call_cancel:
                            tDialog.dismiss();
                            break;
                        case R.id.dialog_call_confirm:
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + mphone));
                            startActivity(intent);
                            tDialog.dismiss();
                            break;
                    }
                })
                .create()
                .show();
    }

    public class NetStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    if (popupWindow == null) {
                        Log.d(TAG, "onReceive: ??????????????????popupWindow111111111111111");
                        Disposable disposable = RetrofitClient.getInstance().getApiService().mapTopTab()
                                .compose(RxScheduler.Flo_io_main())
                                .subscribe(Main.this::initPopupWindowView, throwable -> {
                                    progressDialog.dismiss();
                                    throwable.printStackTrace(System.err);
                                });
                    }
                    if (!positioned) {
                        Log.d(TAG, "onReceive: ????????????positioned2222222222222222");
                        startLocation();//????????????
                    }
                    if (isFirstIn) {//?????????isFirstIn???????????????????????????????????????????????????????????????????????????
                        Log.d(TAG, "onReceive: ????????????marker???????????????????????????33333333333333333333");
                        if (isRequestMarkerNow) {
                            MyToast.showTheToast(Main.this, "??????????????????????????????????????????");
                        } else {
                            startDrawMarker();
                            Log.d(TAG, "handleMessage: 9");
                        }
                    }
                }
            }
        }
    }

    private void initBluetooth() {
        //????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        // ????????????????????????
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "?????????BLE", Toast.LENGTH_SHORT).show();
            return;
        }
        // Initializes Bluetooth adapter.
        // ????????????????????????????????????
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mStatusReceive, statusFilter);
        scanLeDeviceHandler = new Handler();
////        scanLeDevice(true);
        // ??????????????????
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            Log.d(TAG, "onReceive: 0000000000000000000");
            new BluetoothDeviceList(this);
//            new BluetoothMain(Main.this);
        }
    }

    /**
     * @param enable (???????????????true:????????????,false:????????????)
     * @return void
     * @throws
     * @Title: scanLeDevice
     * @Description: TODO(??????????????????)
     */
//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            // Stops scanning after a pre-defined scan period.
//            scanLeDeviceHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e(TAG, "stop.....................");
//                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    dialogByLoading.dismiss();
//                    if (target_chara == null) {
////                        MyToast.showTheToast(activity, "????????????????????????");
//                    }
//                }
//            }, SCAN_PERIOD);
//            /* ??????????????????????????????mLeScanCallback ???????????? */
//            Log.e(TAG, "begin.....................");
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
//        } else {
//            Log.e(TAG, "stoping................");
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//        }
//    }

    /**
     * ???????????????????????? ???????????????????????????????????????BluetoothDevice???????????????name MAC?????????
     **/
//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
//
//        @Override
//        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
//            // TODO Auto-generated method stub
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String device_name = "";
//                    if (device.getName() != null) {
//                        device_name = device.getName();
//                    }
//                    Log.d("xiaoming1119", "run: " + device_name);
//                    if (device_name.equals("XD-01")) {
////                    if (device_name.equals("HC-08")) {
////                        scanLeDevice(false);
////                        dialogByLoading.dismiss();
////                        MyToast.showTheToast(activity, "?????????????????????");
//                        mDeviceAddress = device.getAddress();
////                        mDeviceName = device_name;
//                        ConnectBlueTooth();
//                    }
//                }
//            });
//        }
//    };

//    /**
//     * ????????????
//     **/
//    private void ConnectBlueTooth() {
//        Intent gattServiceIntent = new Intent(this, BluetoothLeServiceMain.class);
//        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
//    }

//    /* BluetoothLeService????????????????????? */
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            mBluetoothLeServicemain = ((BluetoothLeServiceMain.LocalBinder) service).getService();
//            if (!mBluetoothLeServicemain.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
////                finish();
//            }
//            // Automatically connects to the device upon successful start-up
//            // initialization.
//            // ?????????????????????????????????
//            mBluetoothLeServicemain.connect(mDeviceAddress);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            mBluetoothLeServicemain = null;
//        }
//    };

    /**
     * ??????????????????????????????BluetoothLeService??????????????????
     */
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (BluetoothLeServiceMain.ACTION_GATT_CONNECTED.equals(action)) {//Gatt????????????
//                Log.d(TAG, "mGattUpdateReceiver: " + "Gatt????????????");
////                mConnected = true;
////                status = "???????????????";
//                //??????????????????
////                updateConnectionState(status);
//            } else if (BluetoothLeServiceMain.ACTION_GATT_DISCONNECTED.equals(action)) {//Gatt????????????
////                mConnected = false;
////                status = "???????????????";
//                //??????????????????
////                updateConnectionState(status);
//                Log.d(TAG, "mGattUpdateReceiver: " + "Gatt????????????");
//                if (needClose) {
//                    mBluetoothLeServicemain.close();
//                    needClose = false;
//                }
//                blue_electricNum.setText("????????????");
//                main_battery_img.setImageResource(R.drawable.main_batteryele2);
//                if (mBluetoothAdapter.isEnabled()) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            scanLeDevice(true);
//                        }
//                    }, 2000);
//                }
//            } else if (BluetoothLeServiceMain.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //??????GATT?????????
//                // Show all the supported services and characteristics on the
//                // user interface.
//                //?????????????????????????????????
//                displayGattServices(mBluetoothLeServicemain.getSupportedGattServices());
//            } else if (BluetoothLeServiceMain.ACTION_DATA_AVAILABLE.equals(action)) { //????????????
//                //???????????????????????????
//                displayBluetoothData(intent.getStringExtra(BluetoothLeServiceMain.EXTRA_DATA));
//            }
//        }
//    };

//    /* ??????????????? */
//    private static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeServiceMain.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeServiceMain.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeServiceMain.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeServiceMain.ACTION_DATA_AVAILABLE);
//        return intentFilter;
//    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: displayGattServices
     * @Description: TODO(??????????????????)
     */
//    private void displayGattServices(List<BluetoothGattService> gattServices) {
//        if (gattServices == null)
//            return;
//        String uuid = null;
//        String unknownServiceString = "unknown_service";
//        String unknownCharaString = "unknown_characteristic";
//        // ????????????,???????????????????????????????????????
//        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
//        // ??????????????????????????????????????????????????????????????????
//        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();
//        // ????????????????????????????????????
//        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
//        // Loops through available GATT Services.
//        for (BluetoothGattService gattService : gattServices) {
//            // ??????????????????
//            HashMap<String, String> currentServiceData = new HashMap<String, String>();
//            uuid = gattService.getUuid().toString();
//            // ??????????????????uuid??????????????????????????????SampleGattAttributes???????????????????????????
//            gattServiceData.add(currentServiceData);
//            System.out.println("Service uuid:" + uuid);
//            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
//            // ?????????????????????????????????????????????????????????
//            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
//            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();
//            // Loops through available Characteristics.
//            // ????????????????????????????????????????????????????????????
//            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//                charas.add(gattCharacteristic);
//                HashMap<String, String> currentCharaData = new HashMap<>();
//                uuid = gattCharacteristic.getUuid().toString();
//
//                if (gattCharacteristic.getUuid().toString()
//                        .equals(HEART_RATE_MEASUREMENT)) {
//                    // ??????????????????Characteristic??????????????????mOnDataAvailable.onCharacteristicRead()
//                    Characteristichandler.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method stub
//                            mBluetoothLeServicemain
//                                    .readCharacteristic(gattCharacteristic);
//                        }
//                    }, 200);
//                    // ??????Characteristic???????????????,???????????????????????????????????????mOnDataAvailable.onCharacteristicWrite()
//                    mBluetoothLeServicemain.setCharacteristicNotification(
//                            gattCharacteristic, true);
//                    target_chara = gattCharacteristic;
//                    // ??????????????????
//                    // ???????????????????????????
//                    // mBluetoothLeServicemain.writeCharacteristic(gattCharacteristic);
//                }
//                List<BluetoothGattDescriptor> descriptors = gattCharacteristic
//                        .getDescriptors();
//                for (BluetoothGattDescriptor descriptor : descriptors) {
//                    System.out.println("---descriptor UUID:"
//                            + descriptor.getUuid());
//                    // ????????????????????????
//                    mBluetoothLeServicemain.getCharacteristicDescriptor(descriptor);
//                    // mBluetoothLeServicemain.setCharacteristicNotification(gattCharacteristic,
//                    // true);
//                }
//                gattCharacteristicGroupData.add(currentCharaData);
//            }
//            // ?????????????????????????????????????????????????????????????????????
//            mGattCharacteristics.add(charas);
//            // ?????????????????????????????????????????????????????????
//            gattCharacteristicData.add(gattCharacteristicGroupData);
//        }
//    }
    private void displayBluetoothData(String returnStr) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BluetoothEvent bluetoothEvent) {
        String msg = bluetoothEvent.getMsg();
        if (target_chara != null && mBluetoothLeServicemain != null) {
            if (BluetoothEvent.BATTERTVOLL == bluetoothEvent.getEventMode()) {
                byte[] vol_v1 = new byte[]{(byte) 0x3A, (byte) 0x16, (byte) 0x07, (byte) 0x02, (byte) 0xFE, (byte) 0xE1, (byte) 0xFE, (byte) 0x01, (byte) 0x0D, (byte) 0x0A};
                target_chara.setValue(vol_v1);
                mBluetoothLeServicemain.writeCharacteristic(target_chara);
            } else if (BluetoothEvent.BATTERTVOLM == bluetoothEvent.getEventMode()) {
                byte[] vol_v9 = new byte[]{(byte) 0x3A, (byte) 0x16, (byte) 0x07, (byte) 0x02, (byte) 0xFE, (byte) 0xE9, (byte) 0x06, (byte) 0x01, (byte) 0x0D, (byte) 0x0A};
                target_chara.setValue(vol_v9);
                mBluetoothLeServicemain.writeCharacteristic(target_chara);
            } else if (BluetoothEvent.BATTERTVOLH == bluetoothEvent.getEventMode()) {
                byte[] vol_v15 = new byte[]{(byte) 0x3A, (byte) 0x16, (byte) 0x07, (byte) 0x02, (byte) 0xFE, (byte) 0xEF, (byte) 0x0C, (byte) 0x01, (byte) 0x0D, (byte) 0x0A};
                target_chara.setValue(vol_v15);
                mBluetoothLeServicemain.writeCharacteristic(target_chara);
            }
        }
        if (BluetoothEvent.RECEIVE_DATA == bluetoothEvent.getEventMode() && show_bluetooth.equals("1")) {
//        if (BluetoothEvent.RECEIVE_DATA == bluetoothEvent.getEventMode()) {
//        if (BluetoothEvent.RECEIVE_DATA == bluetoothEvent.getEventMode() ) {
            String str = bluetoothEvent.getMsg();
            int count = str.length() / 2;
            String[] RX = new String[count];
            for (int i = 0; i < count; i++) {
                RX[i] = str.substring(i * 2, (i + 1) * 2);
            }
            System.out.println("bluetooth_return : " + bluetoothEvent.getMsg());
            //????????????
            if (10 == RX.length) {
                if (RX[0].equals("3A") && RX[1].equals("16") && RX[2].equals("0D") && RX[3].equals("02")) {
                    if (bluetooth_num == 0) {
                        bluetooth_num++;
                    }
                    int valid_data = Integer.parseInt(RX[5] + RX[4], 16);
                    if (30 == valid_data) {
                        main_battery_img.setImageResource(R.drawable.main_batteryele1);
                    } else if (70 == valid_data) {
                        main_battery_img.setImageResource(R.drawable.main_batteryele2);
                    } else if (99 == valid_data) {
                        main_battery_img.setImageResource(R.drawable.main_batteryele3);
                    } else if (100 == valid_data) {
                        main_battery_img.setImageResource(R.drawable.main_batteryele4);
                    }
                    blue_electricNum.setText(valid_data + "%");
                    electric = 0 > valid_data ? "0" : valid_data + "";
                    EventBus.getDefault().post(new BluetoothEvent(valid_data + "", BluetoothEvent.BATTERTELE));
                }
            }

            //????????????
            if (24 == RX.length) {
                if (RX[0].equals("3A") && RX[1].equals("16") && RX[2].equals("7E") && RX[3].equals("10")) {
                    StringBuilder stringBuffer = new StringBuilder();
                    for (int i = 4; i < 20; i++) {
                        stringBuffer.append(Integer.parseInt(RX[i], 16));
                        stringBuffer.append(",");
                    }
                    battery = asciiToString(stringBuffer.toString());
                    EventBus.getDefault().post(new BluetoothEvent(asciiToString(stringBuffer.toString()), BluetoothEvent.BATTERYNUM));
                }
            }

            //????????????    -2731/10
            if (10 == RX.length) {
                if (RX[0].equals("3A") && RX[1].equals("16") && RX[2].equals("08") && RX[3].equals("02")) {
                    int tem = (Integer.parseInt(RX[5] + RX[4], 16) - 2731) / 10;
                    wendu = 0 > tem ? "0" : tem + "";
                    EventBus.getDefault().post(new BluetoothEvent(tem + "", BluetoothEvent.BATTERTTEM));
                }
            }

            //??????
            if (22 == RX.length) {
                // ????????????1-7
                if (RX[0].equals("3A") && RX[1].equals("16") && RX[2].equals("24")) {
                    StringBuilder stringBuffer = new StringBuilder();
                    for (String rx : RX) {
                        stringBuffer.append(rx);
                    }
                    vdif1 = stringBuffer.toString();
                }
            }
            if (24 == RX.length) {
                // ????????????8-15
                if (RX[0].equals("3A") && RX[1].equals("16") && RX[2].equals("25")) {
                    StringBuilder stringBuffer = new StringBuilder();
                    for (String rx : RX) {
                        stringBuffer.append(rx);
                    }
                    vdif2 = stringBuffer.toString();
                }
            }
            if (16 == RX.length) {
                // ????????????16-19
                if (RX[0].equals("3A") && RX[1].equals("16") && RX[2].equals("26")) {
                    StringBuilder stringBuffer = new StringBuilder();
                    for (String rx : RX) {
                        stringBuffer.append(rx);
                    }
                    vdif3 = stringBuffer.toString();
                }
            }

            if (!"".equals(battery) && !"".equals(electric) && !"".equals(wendu) && !"".equals(vdif1) && !"".equals(vdif2) && !"".equals(vdif3)) {
                batteryRealtimeHandler.sendEmptyMessage(1);
            }
        }
        if (BluetoothEvent.BLUETOOTH_FOUND == bluetoothEvent.getEventMode()) {
            BluetoothDialogFragment logoutDialogFragment = new BluetoothDialogFragment();
            logoutDialogFragment.setTitle("??????????????????\"1234\"");
            logoutDialogFragment.setOnDialogItemClickListener(new BluetoothDialogFragment.OnDialogItemClickListener() {
                @Override
                public void onSuccessClick() {
                    new BluetoothSpp(activity, bluetoothEvent.getDevice(), "SPP");
                }

                @Override
                public void onErrorClick() {
                }
            });
            logoutDialogFragment.show(getSupportFragmentManager(), "");
        }
        if (BluetoothEvent.CONNECT_FAILD == bluetoothEvent.getEventMode()) {
            BluetoothDialogFragment logoutDialogFragment = new BluetoothDialogFragment();
            logoutDialogFragment.setTitle("?????????????????????????????????");
            logoutDialogFragment.setOnDialogItemClickListener(new BluetoothDialogFragment.OnDialogItemClickListener() {
                @Override
                public void onSuccessClick() {
                    if (BluetoothSpp.BluetoothReConnectHandler != null) {
                        BluetoothSpp.BluetoothReConnectHandler.sendEmptyMessage(1);
                    }
                }

                @Override
                public void onErrorClick() {
                }
            });
            logoutDialogFragment.show(getSupportFragmentManager(), "");
        }
    }

    @Background
    void HttpBatteryRealtime() {
        List<ParamTypeData> list = new ArrayList<>();
        list.add(new ParamTypeData("uid", uid));
        list.add(new ParamTypeData("battery", battery));
        list.add(new ParamTypeData("electric", electric));
        list.add(new ParamTypeData("wendu", wendu));
        list.add(new ParamTypeData("vdif", vdif1 + "-" + vdif2 + "-" + vdif3));
        Log.d(TAG, "handleMessage: HttpBatteryRealtime1 battery: " + battery + " electric :" + electric + " wendu :" + wendu + " vdif1 :" + vdif1 + " vdif2 :" + vdif2 + " vdif3 :" + vdif3);
        new OkHttpConnect(activity, PubFunction.app + "Battery/realtime.html", list, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onHttpBatteryRealtime(response, type);
            }
        }).startHttpThread();
    }

    @UiThread
    void onHttpBatteryRealtime(String response, String type) {
        Log.d(TAG, "onHttpBatteryRealtime: " + response);
        batteryRealtimeHandler.sendEmptyMessage(2);
    }

    public static String asciiToString(String value) {
        StringBuilder sbu = new StringBuilder();
        String[] chars = value.split(",");
        for (String aChar : chars) {
            sbu.append((char) Integer.parseInt(aChar));
        }
        return sbu.toString();
    }

    private BroadcastReceiver mStatusReceive = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d(TAG, "onReceive---------?????????????????????: ");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.d(TAG, "onReceive---------????????????: ");
                            //?????????????????????????????????
                            if (BluetoothSpp.BluetoothSetVolHandler != null) {
                                Message message = new Message();
                                message.what = 2;
                                Bundle bundle = new Bundle();
                                bundle.putInt("vol", 5);
                                message.setData(bundle);
                                BluetoothSpp.BluetoothSetVolHandler.sendMessage(message);
                            } else {
                                Log.d(TAG, "onReceive: 123123123123123123");
                                new BluetoothDeviceList(Main.this);
//                                new BluetoothMain(Main.this);
                            }
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.d(TAG, "onReceive---------????????????: ");
                            needClose = true;
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Log.d(TAG, "onReceive---------????????????: ");
                            blue_electricNum.setText("????????????");
                            main_battery_img.setImageResource(R.drawable.main_batteryele2);
                            break;
                    }
                    break;
            }
        }
    };

    /**
     * ????????????
     */
    private void setHeadPortrait() {
        if (!TextUtils.isEmpty(uid)) {
            HttpGetUserInfo();
        }
    }

    /**
     * http?????????User/personal.html   ??????????????????
     */
    @Background
    void HttpGetUserInfo() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(activity, PubFunction.app + "User/personal.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetUserInfo(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpGetUserInfo(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                String msg = jsonObjectResponse.getString("msg");
                String status = jsonObjectResponse.getString("status");
                System.out.println(jsonObjectResponse);
                if ("1".equals(status)) {
                    JSONObject jsonObject = jsonObjectResponse.getJSONObject("data");
                    String img = jsonObject.getString("avater");
                    String phone = jsonObject.getString("phone");
                    Glide.with(this).load(img).into(iv_user_headPortrait);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_avatar", img);
                    editor.putString("phone", phone);
                    editor.apply();
                } else {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (Exception e) {
                MyToast.showTheToast(activity, "JSON???" + e.toString());
            }
        }
    }
}