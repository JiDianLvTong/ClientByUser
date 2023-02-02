package com.android.jidian.repair.mvp.patrol;

import com.android.jidian.repair.base.BaseBean;
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
 * date: 2023/2/1 17:05
 * description:
 */
public class PatrolDetailModel implements PatrolDetailContract.Model {
    @Override
    public Flowable<BaseBean> requestPatrolAddpatrol(String cabid, String img1, String img2, String img3,
                                                     String img4_1, String img4_2, String img4_3, String img4_4,
                                                     String img5_1, String img5_2, String img5_3, String img6_1,
                                                     String isnetdbm, String isdixian, String isopenbtn) {
        return RetrofitClient.getInstance().getApeService().requestPatrolAddpatrol(cabid, img1, img2, img3, img4_1, img4_2, img4_3, img4_4, img5_1, img5_2, img5_3, img6_1, isnetdbm, isdixian, isopenbtn);
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
}
