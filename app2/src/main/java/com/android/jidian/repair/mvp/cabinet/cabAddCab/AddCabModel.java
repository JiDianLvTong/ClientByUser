package com.android.jidian.repair.mvp.cabinet.cabAddCab;

import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.net.RetrofitClient;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author : xiaoming
 * date: 2023/2/3 17:59
 * description:
 */
public class AddCabModel implements AddCabContract.Model{
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
}
