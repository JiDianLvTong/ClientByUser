package com.android.jidian.client.util;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.android.jidian.client.util.Util.unicodeToUtf8;

/**
 * create by tianyanyu at 20190909 runningpig66@163.com
 * func: the interceptor of okhttp used to display logs
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "LoggingInterceptor";

    /**
     * 请求body转String
     * */
    private static String requestBodyToString(RequestBody requestBody) throws IOException {
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        }
        return "";
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Charset UTF8 = Charset.forName("UTF-8");
        // 打印请求报文
        Request request = chain.request();
        HttpUrl url = request.url();
//        String scheme = url.scheme();
//        String host = url.host();
//        int port = url.port();
//        String path = url.encodedPath();
//        String query = url.encodedQuery();
//        Util.d(TAG, "scheme: " + scheme);
//        Util.d(TAG, "host: " + host);
//        Util.d(TAG, "port: " + port);
//        Util.d(TAG, "path: " + path);
//        Util.d(TAG, "query: " + query);
        RequestBody requestBody = request.body();
        String bodyToString = null;
        if (requestBody != null) {
            bodyToString = requestBodyToString(requestBody);
        }
        Util.d(TAG, unicodeToUtf8(String.format("发送请求:\nmethod: %s\nheaders: %surl: %s\nbody: %s",
                request.method(), request.headers(), url, bodyToString)));
        // 打印返回报文
        // 先执行请求，才能获取报文
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        String bodyString = "";
        String bodySize = "";
        if (responseBody != null) {
            //不能使用responseBody.string()，否则response会close，在onResponse()回调获取不到response
//            bodyString = responseBody.string();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();// text/plain;charset=UTF-8
            if (contentType != null) {
                charset = contentType.charset(UTF8);// UTF-8, charset might be null
            }
            bodyString = buffer.clone().readString(charset == null ? UTF8 : charset);
            long contentLength = responseBody.contentLength();
            bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        }
        Util.d(TAG, unicodeToUtf8(String.format("收到响应:\n%s %s\nheaders: %s请求url: %s\n请求body: %s\n" +
                        "响应body: %s\n响应bodySize: %s",
                response.code(), response.message(), response.headers(), response.request().url(),
                requestBodyToString(response.request().body()), bodyString, bodySize)));
        return response;
    }
}