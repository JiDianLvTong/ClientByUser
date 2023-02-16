package com.android.jidian.repair.utils.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private static volatile FileManager fileManager;
    private FileManager(){};
    public static FileManager getInstance(){
        if(fileManager == null){
                synchronized (FileManager.class){
                if(fileManager == null){
                    fileManager = new FileManager();
                }
            }
        }
        return fileManager;
    }

    private Context context;
    //根目录
    private String rootPath = "";
    //样式目录
    private String mapStylePath = "";
    //sn码目录
    private String snPath = "";
    //身份证目录
    private String cardPath = "";
    //目录数组
    private String[] folderPaths = null;

    //sn码文件
    private String snFilePath = "";
    //文件数组
    private String[] filePaths = null;

    public void init(Context context){
        this.context = context;

        rootPath = Environment.getExternalStorageDirectory().getPath() + "/MiXiang";
        mapStylePath = rootPath + "/MapStyle";
        snPath = rootPath + "/Sn";
        cardPath = rootPath + "/Card";

        folderPaths = new String[]{rootPath , mapStylePath , snPath , cardPath};

        for(int i = 0 ; i < folderPaths.length ; i++){
            File dirFile = new File(folderPaths[i]);
            if (!dirFile.exists()) {
                boolean mkdirs = dirFile.mkdirs();
                if (!mkdirs) {
                    Log.e("TAG", "文件夹"+folderPaths[i]+"创建失败");
                } else {
                    Log.e("TAG", "文件夹"+folderPaths[i]+"创建成功");
                }
            }else{
                Log.e("TAG", "文件夹"+folderPaths[i]+" 已经存在");
            }
        }

        snFilePath = snPath + "/sn.txt";
        filePaths = new String[]{snFilePath};
        for(int i = 0 ; i < filePaths.length ; i++){
            File dirFile = new File(filePaths[i]);
            if (!dirFile.exists()) {
                boolean mkdirs = false;
                try {
                    mkdirs = dirFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!mkdirs) {
                    Log.e("TAG", "文件"+folderPaths[i]+"创建失败");
                } else {
                    Log.e("TAG", "文件"+folderPaths[i]+"创建成功");
                }
            }else{
                Log.e("TAG", "文件"+folderPaths[i]+" 已经存在");
            }
        }
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getMapStylePath() {
        return mapStylePath;
    }

    public String getCardPath(){ return cardPath;}


    public String getSnFileContent() {
        File file = new File(snFilePath);
        String fileStr = FileUtils.getInstance().getFileContent(file);
        return fileStr;
    }

    public void setSnFileContent(String str) {
        FileUtils.getInstance().writeTxtToFile(str , snFilePath);
    }
}
