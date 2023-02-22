package com.android.jidian.extension.base.permissionManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.permissionx.guolindev.PermissionX;

import java.util.List;

public class PermissionManager {

    public interface PermissionListener {
        void granted(List<String> grantedList);
        void refused(List<String> refusedList);
    }

    private volatile static PermissionManager permissionManager;
    public PermissionManager(){};
    public static PermissionManager getInstance() {
        if (permissionManager == null) {
            synchronized (PermissionManager.class) {
                if (permissionManager == null) {
                    permissionManager = new PermissionManager();
                }
            }
        }
        return permissionManager;
    }

    private Context context;

    public void init(Context context) {
        this.context = context;
    }

    public void getCamera(Activity activity, PermissionListener listener) {
        XXPermissions.with(activity)
            .permission(Permission.CAMERA)
            .request(new OnPermissionCallback() {

                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    listener.granted(permissions);
                }

                @Override
                public void onDenied(List<String> permissions, boolean never) {
                    listener.refused(permissions);
                }
            });
    }


    public void getWrite(FragmentActivity activity, PermissionListener listener) {
        PermissionX.init(activity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
//                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        listener.granted(grantedList);
                    } else {
                        listener.refused(deniedList);
                    }
                });
    }

    public void getLocal(FragmentActivity activity, PermissionListener listener) {
        PermissionX.init(activity)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
//                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        listener.granted(grantedList);
                    } else {
                        listener.refused(deniedList);
                    }
                });
    }

    public void getLocalAndWrite(FragmentActivity activity, PermissionListener listener) {
        PermissionX.init(activity)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
//                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (grantedList.size() > 0) {
                        listener.granted(grantedList);
                    }
                    if (deniedList.size() > 0) {
                        listener.refused(deniedList);
                    }
                });
    }

    public void getCameraAndWriteAndRead(FragmentActivity activity, PermissionListener listener) {
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
//                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (grantedList.size() > 0) {
                        listener.granted(grantedList);
                    }
                    if (deniedList.size() > 0) {
                        listener.refused(deniedList);
                    }
                });
    }
}
