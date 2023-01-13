package com.android.jidian.repair.utils.picture;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:33
 * description:
 */
public class PictureSelectorUtils {

    public static void addPhotoByCamera(Activity activity, int activityResultCallbackCode) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
//                .isEnableCrop(true)// 是否裁剪
                .isCompress(true)// 是否压缩
//                .withAspectRatio(800, 500)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                .forResult(activityResultCallbackCode);
    }

    public static void addPhotoByCameraAndAlbum(Activity activity, int activityResultCallbackCode) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
//                .isEnableCrop(true)// 是否裁剪
                .isCompress(true)// 是否压缩
//                .withAspectRatio(800, 500)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
                .forResult(activityResultCallbackCode);
    }
}
