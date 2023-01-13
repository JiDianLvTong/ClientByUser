package com.android.jidian.repair.mvp.main.FailureFragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseFragmentByMvp;
import com.android.jidian.repair.base.BindEventBus;
import com.android.jidian.repair.dialog.TakePhotoDialog;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FailureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@BindEventBus
public class FailureFragment extends BaseFragmentByMvp<FailurePresenter> implements FailureContract.View {

    @BindView(R.id.tv_test)
    public TextView tvTest;
    @BindView(R.id.iv_test)
    public ImageView ivTest;

    private static final int RESULT_PICTURE_SELECT = 101;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FailureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FailureGragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FailureFragment newInstance(String param1, String param2) {
        FailureFragment fragment = new FailureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_failure_fragment;
    }

    @Override
    public void initView(View view) {

    }

    @OnClick(R.id.tv_test)
    public void OnClickRoot(){
        showTakePhotoDialog();
    };

    private void showTakePhotoDialog() {
        TakePhotoDialog dialog = new TakePhotoDialog(getActivity(), new TakePhotoDialog.OnDialogClickListener() {
            @Override
            public void onTakePhoto() {
                PictureSelectorUtils.addPhotoByCamera(getActivity(), RESULT_PICTURE_SELECT);
            }
        });
        dialog.showPopupWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FailureEvent event) {
        if (event != null) {
            LocalMedia media = event.getMedia();
            String path = media.getCompressPath();
            if (event.getEvent() == FailureEvent.FAILURE_TAKE_PHOTO) {
                Glide.with(getActivity()).load(PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed() ? Uri.parse(path)
                        : path).into(ivTest);
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

    }
}