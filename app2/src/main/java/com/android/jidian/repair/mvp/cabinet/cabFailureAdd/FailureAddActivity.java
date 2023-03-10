package com.android.jidian.repair.mvp.cabinet.cabFailureAdd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.jidian.repair.PubFunction;
import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.utils.Md5;
import com.android.jidian.repair.utils.picture.BitmapManager;
import com.android.jidian.repair.utils.picture.PictureSelectorUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FailureAddActivity extends BaseActivityByMvp<FailurePresenter> implements FailureContract.View {

    private static final int FAILURE_TAKT_PHOTO_1 = 101;
    private static final int FAILURE_TAKT_PHOTO_2 = 102;
    private static final int FAILURE_TAKT_PHOTO_3 = 103;
    private static final int FAILURE_TAKT_PHOTO_4 = 104;

    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.inputEdit)
    public EditText inputEdit;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.commitText)
    public TextView commitText;

    private FailureImgAdapter mAdapter;
    private List<String> mList = new ArrayList<>();
    private String mCabid, mPath, mCompanyid, mProj, mUpToken;

    @Override
    public int getLayoutId() {
        return R.layout.activity_failure_add;
    }

    @Override
    public void initView() {
        mCabid = getIntent().getStringExtra("id");
        mPresenter = new FailurePresenter();
        mPresenter.attachView(this);
        recyclerView.setLayoutManager(new GridLayoutManager(FailureAddActivity.this, 3));
        mAdapter = new FailureImgAdapter();
        recyclerView.setAdapter(mAdapter);
        mList.add("");
        mAdapter.setNewData(mList);
        mAdapter.setListener(new FailureImgAdapter.OnClickListener() {
            @Override
            public void onClickDelete(int position) {
                mList.remove(position);
                if (mList.size() < 4 && !"".equals(mList.get(mList.size() - 1))) {
                    mList.add("");
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickItem(int position) {
                if (position == 0) {
                    PictureSelectorUtils.addPhotoByCamera(FailureAddActivity.this, FAILURE_TAKT_PHOTO_1);
                } else if (position == 1) {
                    PictureSelectorUtils.addPhotoByCamera(FailureAddActivity.this, FAILURE_TAKT_PHOTO_2);
                } else if (position == 2) {
                    PictureSelectorUtils.addPhotoByCamera(FailureAddActivity.this, FAILURE_TAKT_PHOTO_3);
                } else if (position == 3) {
                    PictureSelectorUtils.addPhotoByCamera(FailureAddActivity.this, FAILURE_TAKT_PHOTO_4);
                }

            }
        });
        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 199) {
                    showMessage("????????????200??????~");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            String filePath = "";
            if (Build.VERSION.SDK_INT >= 29) {
                filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg" ;
            }else {
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(mProj)) {
            mPresenter.requestUploadUploadUrlSet(Md5.getAptk());
        }
    }

    @OnClick(R.id.commitText)
    public void OnClickCommitText() {
        if (TextUtils.isEmpty(inputEdit.getText().toString().trim())) {
            showMessage("????????????????????????");
            return;
        }
        if (mList.size() < 2) {
            showMessage("????????????????????????");
            return;
        }
        if (mList.size() == 2) {
            mPresenter.requestWorktaskAddFault(mCabid, inputEdit.getText().toString().trim(), mList.get(0), mList.get(1), null, null);
        }
        if (mList.size() == 3) {
            mPresenter.requestWorktaskAddFault(mCabid, inputEdit.getText().toString().trim(), mList.get(0), mList.get(1), mList.get(2), null);
        }
        if (mList.size() == 4) {
            mPresenter.requestWorktaskAddFault(mCabid, inputEdit.getText().toString().trim(), mList.get(0), mList.get(1), mList.get(2), mList.get(3));
        }
    }

    ;

    @OnClick(R.id.pageReturn)
    public void OnClickPageReturn() {
        finish();
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

    @Override
    public void requestWorktaskAddFaultSuccess(BaseBean bean) {
        showMessage(bean.getMsg());
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
            switch (index) {
                case FAILURE_TAKT_PHOTO_1:
                    mList.set(0, bean.getData().getFurl());
                    mList.add("");
                    mAdapter.notifyDataSetChanged();
                    break;
                case FAILURE_TAKT_PHOTO_2:
                    mList.set(1, bean.getData().getFurl());
                    mList.add("");
                    mAdapter.notifyDataSetChanged();
                    break;
                case FAILURE_TAKT_PHOTO_3:
                    mList.set(2, bean.getData().getFurl());
                    mList.add("");
                    mAdapter.notifyDataSetChanged();
                    break;
                case FAILURE_TAKT_PHOTO_4:
                    mList.set(3, bean.getData().getFurl());
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        } else {
            showMessage(bean.getMsg());
        }
    }
}