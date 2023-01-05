package com.android.jidian.client.http;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.LoggingInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpConnect {
    private static final String TAG = "OkHttpConnect";
    private Activity activity;
    private String path;
    private List<ParamTypeData> paramList;
    private String[][] headerArray;
    private String result_str = "";
    private String result_type = "";
    private ResultListener listener;
    private Thread httpThread = new Thread() {
        @Override
        public void run() {
            super.run();
            if (!PubFunction.isConnect(activity, true)) {
                result_str = "无网络链接，请检查您的网络设置！";
                result_type = "0";
                listener.onSuccessResult(result_str, result_type);
            } else {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.addInterceptor(new LoggingInterceptor());
                builder.connectTimeout(60, TimeUnit.SECONDS);
                builder.readTimeout(60, TimeUnit.SECONDS);
//                X509TrustManager trustManager;
//                SSLSocketFactory sslSocketFactory;
//                try {
//                    trustManager = trustManagerForCertificates(trustedCertificatesInputStream(activity, "alls_halouhuandian_com.cer"));
//                    SSLContext sslContext = SSLContext.getInstance("TLS");
//                    sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
//                    sslSocketFactory = sslContext.getSocketFactory();
//                } catch (GeneralSecurityException e) {
//                    throw new RuntimeException(e);
//                }
//                builder.sslSocketFactory(sslSocketFactory, trustManager);
//                builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        Certificate[] localCertificates = new Certificate[0];
//                        try {
//                            //获取证书链中的所有证书
//                            localCertificates = session.getPeerCertificates();
//                        } catch (SSLPeerUnverifiedException e) {
//                             System.out.println(e.getLocalizedMessage());
//                        }
//                        //打印所有证书内容
////                        for (Certificate c : localCertificates) {
////                            Log.d(TAG, "verify: " + c.toString());
////                        }
//                        //校验hostname是否正确，如果正确则建立连接
//                        Log.d(TAG, "hostname: " + hostname);
//                        return true;
//                    }
//                });
                OkHttpClient okHttpClient = builder.build();
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (int i = 0; i < paramList.size(); i++) {
                    ParamTypeData paramTypeData = paramList.get(i);
                    if (paramTypeData.getValue() != null && paramTypeData.getName() != null) {
                        formBodyBuilder.add(paramTypeData.getName(), paramTypeData.getValue());
                    }
                }
                RequestBody requestBody = formBodyBuilder.build();

//                Request request = new Request.Builder()
//                        .url("https://api.github.com/markdown/raw")
//                        .post(requestBody)
//                        .build();

                Request.Builder requestBuilder = new Request.Builder();
                requestBuilder.url(path);
                if (headerArray.length > 0) {
                    for (int i = 0; i < headerArray.length; i++) {
                        final String[] arrays = headerArray[i];
                        requestBuilder.addHeader(arrays[0], arrays[1]);
                    }
                }
                requestBuilder.post(requestBody);
                Request request = requestBuilder.build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        result_str = "Error：" + e.toString();
                        result_type = "0";
                        listener.onSuccessResult(result_str, result_type);
                        System.out.println(result_str);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        result_str = response.body().string();
                        result_type = "1";
                        listener.onSuccessResult(result_str, result_type);
                        System.out.println(result_str);
                    }
                });
            }

        }
    };

    public OkHttpConnect(Activity activity, String path, List<ParamTypeData> paramList, String[][] headerArray, ResultListener listener) {
        this.activity = activity;
        this.path = path;
        this.paramList = paramList;
        this.headerArray = headerArray;
        this.listener = listener;

        for (int i = 0; i < paramList.size(); i++) {

            ParamTypeData paramTypeData = paramList.get(i);
            System.out.println(paramTypeData.getName() + "aaaaaaa" + paramTypeData.getValue());
        }
    }

    public OkHttpConnect(Activity activity, String path, List<ParamTypeData> paramList, ResultListener listener) {
        this.activity = activity;
        this.path = path;
        this.paramList = paramList;
        this.listener = listener;

        for (int i = 0; i < paramList.size(); i++) {

            ParamTypeData paramTypeData = paramList.get(i);
            System.out.println(paramTypeData.getName() + "aaaaaaa" + paramTypeData.getValue());

        }
    }

    public void startHttpThread() {
        httpThread.start();
    }

    private X509TrustManager trustManagerForCertificates(InputStream in) throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        char[] password = "password".toCharArray();
        // Put the certificates a key store.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
//            Log.d(TAG, "trustManagerForCertificates: " + certificate.toString());
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }
        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null;
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private InputStream trustedCertificatesInputStream(Context context, String fileName) {
        InputStream inputStream;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            return inputStream;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }

    public interface ResultListener {
        void onSuccessResult(String response, String type);
    }


}
