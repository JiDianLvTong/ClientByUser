package com.android.jidian.repair.utils.picture;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

import com.android.jidian.repair.widgets.dialog.DialogByChoiceImg;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:33
 * description:
 */
public class PictureSelectorUtils {

    public static void addPhotoByCamera(Activity activity, int activityResultCallbackCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        activity.startActivityForResult(intent, activityResultCallbackCode);
    }

    public static void addPhotoByCameraAndAlbum(Activity activity, int activityResultCallbackCode) {
        DialogByChoiceImg dialogByIdCardChoice = new DialogByChoiceImg(activity, new DialogByChoiceImg.DialogCardChoiceListener() {
            @Override
            public void cameraReturn() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("autofocus", true); // 自动对焦
                intent.putExtra("fullScreen", false); // 全屏
                intent.putExtra("showActionIcons", false);
                activity.startActivityForResult(intent, activityResultCallbackCode);
            }

            @Override
            public void albumReturn() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");//相片类型
                activity.startActivityForResult(intent, activityResultCallbackCode);
            }
        });
        dialogByIdCardChoice.showPopupWindow();
    }
}
