package com.android.jidian.extension.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.jidian.extension.BuildConfig;
import com.android.jidian.extension.util.BitmapManager;
import com.android.jidian.extension.util.Md5;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUploadImage {

    public interface HttpUploadImageListener{
        void returnListener(int status ,String msg, String filePath);

    }

    private static volatile HttpUploadImage httpUploadImage;
    private void HttpUploadImage(){};
    public static HttpUploadImage getInstance(){
        if(httpUploadImage == null){
            synchronized (HttpUploadImage.class){
                if(httpUploadImage == null){
                    httpUploadImage = new HttpUploadImage();
                }
            }
        }
        return httpUploadImage;
    }

    public void upload (String url , String uid , Map<String, String> fileMap , HttpUploadImageListener httpUploadImageListener){
        String res = "";
        HttpURLConnection conn = null;
        //boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        //填写头文件
        try {
            URL hUrl = new URL(url);
            conn = (HttpURLConnection) hUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            // file
            if (fileMap != null) {
                for (Map.Entry<String, String> entry : fileMap.entrySet()) {
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    //图片上传，需要压缩一下
                    int requestWidth = (int) (1024 / 2.625);//计算1024像素的dp
                    //首先使用inJustDecodeBounds = true进行解码以检查尺寸
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(inputValue, options);
                    //计算inSampleSize
                    options.inSampleSize = BitmapManager.calculateInSampleSize(options, requestWidth, requestWidth);
                    //使用inSampleSize集解码位图
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(inputValue, options);
                    File file = BitmapManager.saveBitmapFile(new File(inputValue), bitmap);
                    //TODO 对图片进行压缩
                    String filename = file.getName();
                    String strBuf = "\r\n" + "--" + BOUNDARY + "\r\n" +
                            "Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n" +
                            "Content-Type:" + "image/jpeg" + "\r\n\r\n";
                    out.write(strBuf.getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuilder strBuf = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();

            JSONObject jsonObject = new JSONObject(res);
            String status = jsonObject.getString("status");
            String msg = jsonObject.getString("msg");

            System.out.println("out: okHttpSuccess - " + res.toString());

            //回传数据
            if(status.equals("1")){
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                String filePath = jsonObjectData.getString("furl");
                httpUploadImageListener.returnListener(1,msg,filePath);
            }else{
                String data = jsonObject.getString(res);
                httpUploadImageListener.returnListener(0,msg,data);
            }
        } catch (Exception e) {
            httpUploadImageListener.returnListener(-1,e.toString(),"");
            System.out.println("out: okHttpError - " + e.toString());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
