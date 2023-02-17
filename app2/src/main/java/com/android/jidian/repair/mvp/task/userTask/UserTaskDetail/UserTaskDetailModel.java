package com.android.jidian.repair.mvp.task.userTask.UserTaskDetail;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;
import com.android.jidian.repair.net.RetrofitClient;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author : xiaoming
 * date: 2023/1/12 17:19
 * description:
 */
public class UserTaskDetailModel implements UserTaskDetailContract.Model {
    @Override
    public Flowable<WorktaskDetailBean> requestWorktaskDetail(String wtid) {
        return RetrofitClient.getInstance().getApeService().requestWorktaskDetail(wtid);
    }

    @Override
    public Flowable<UploadUploadUrlSetBean> requestUploadUploadUrlSet(String token) {
        return RetrofitClient.getInstance().getApeService().requestUploadUploadUrlSet(token);
    }

    @Override
    public Observable<UploadImageBean> requestUpLoadImg(String url, File file, String upToken, String companyid) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upFile", file.getName(), requestFile);
        RequestBody projectNameRequest = RequestBody.create(MediaType.parse("text/plain"), "ape");
        return RetrofitClient.getInstance().getAppUploadImgService().requestUpLoadImg(url, body, projectNameRequest, upToken, companyid);
    }

    @Override
    public Flowable<BaseBean> requestWorktaskResolve(String wtid, String ustat, String content, String img1, String img2, String img3, String img4) {
        return RetrofitClient.getInstance().getApeService().requestWorktaskResolve(wtid, ustat, content, img1, img2, img3, img4);
    }
}
