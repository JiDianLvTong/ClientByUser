package com.android.jidian.repair.base.PermissionManager;

import android.Manifest;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.permissionx.guolindev.PermissionX;

import java.util.List;

public class PermissionManager {

    public interface PermissionListener{
        void granted(List<String> grantedList);
        void refused(List<String> refusedList);
    }

    public final String local_1 = "android.permission.ACCESS_FINE_LOCATION";
    public final String local_2 = "android.permission.ACCESS_COARSE_LOCATION";
    public final String write = "android.permission.WRITE_EXTERNAL_STORAGE";


    private volatile static PermissionManager permissionManager;
    private PermissionManager(){};
    public static PermissionManager getInstance(){
        if(permissionManager == null){
            synchronized (PermissionManager.class){
                if(permissionManager == null){
                    permissionManager = new PermissionManager();
                }
            }
        }
        return permissionManager;
    }

    private Context context;

    public void init(Context context){
        this.context = context;
    }

    public void getCamera(FragmentActivity activity , PermissionListener listener){
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        listener.granted(grantedList);
                    } else {
                        listener.refused(deniedList);
                    }
                });
    }

    public void getWrite(FragmentActivity activity , PermissionListener listener){
        PermissionX.init(activity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        listener.granted(grantedList);
                    } else {
                        listener.refused(deniedList);
                    }
                });
    }

    public void getLocal(FragmentActivity activity , PermissionListener listener){
        PermissionX.init(activity)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        listener.granted(grantedList);
                    } else {
                        listener.refused(deniedList);
                    }
                });
    }

    public void getLocalAndWrite(FragmentActivity activity , PermissionListener listener){
        PermissionX.init(activity)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if(grantedList.size()>0){
                        listener.granted(grantedList);
                    }
                    if(deniedList.size()>0){
                        listener.refused(deniedList);
                    }
                });
    }

    public void getCameraAndWrite(FragmentActivity activity , PermissionListener listener){
        PermissionX.init(activity)
                .permissions(Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "确认", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "当前应用缺少必要权限，您需要去应用程序设置当中手动开启权限", "确认", "取消"))
                .request((allGranted, grantedList, deniedList) -> {
                    if(grantedList.size()>0){
                        listener.granted(grantedList);
                    }
                    if(deniedList.size()>0){
                        listener.refused(deniedList);
                    }
                });
    }
}
