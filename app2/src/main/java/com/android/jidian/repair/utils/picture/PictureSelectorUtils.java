package com.android.jidian.repair.utils.picture;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.jidian.repair.base.permissionManager.PermissionManager;
import com.android.jidian.repair.widgets.dialog.DialogByChoiceImg;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/1/11 17:33
 * description:
 */
public class PictureSelectorUtils {

    public static void addPhotoByCamera(FragmentActivity activity, int activityResultCallbackCode) {
        PermissionManager.getInstance().getCamera(activity, new PermissionManager.PermissionListener() {
            @Override
            public void granted(List<String> grantedList) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("autofocus", true); // 自动对焦
                intent.putExtra("fullScreen", false); // 全屏
                intent.putExtra("showActionIcons", false);
                activity.startActivityForResult(intent, activityResultCallbackCode);
            }

            @Override
            public void refused(List<String> refusedList) {
                Toast.makeText(activity , "缺少权限~" , Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void addPhotoByCameraAndAlbum(FragmentActivity activity, int activityResultCallbackCode) {

        PermissionManager.getInstance().getCamera(activity, new PermissionManager.PermissionListener() {
            @Override
            public void granted(List<String> grantedList) {
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

            @Override
            public void refused(List<String> refusedList) {
                Toast.makeText(activity , "缺少权限~" , Toast.LENGTH_LONG).show();
            }
        });
    }
}
