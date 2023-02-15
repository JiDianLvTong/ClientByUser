package com.android.jidian.repair.utils.picture;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class PictureUtil {
    private static String TAG = "PictureUtil";

    //  获得缓存文件路径
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    //  获得缓存文件
    public static File getCacheFile(Context context, String child) {/// storage/emulated/0/Android/data/com.kejiakeji.buddhas/cache/www.png
        String parent = getDiskCacheDir(context);
        // 创建File对象，用于存储拍照后的图片
        File file = new File(parent, child);

        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getRootPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return "/mnt/sdcard";
        }
    }

//    public static File getSavePathFile(String fileName) {
//        return new File(getSaveFilePath("images", fileName));
//    }

//    public static String getSaveFilePath(String catalog, String fileName) {/// storage/emulated/0/guanchan/image/www.png
//        String dir = getRootPath() + Constants.BASE_FILE + catalog + File.separator;
//
//        if (!TextUtils.isEmpty(fileName))// fileName为空获取文件夹路径
//        {// 创建File对象，用于存储拍照后的图片
//            File file = new File(dir, fileName);
//
//            if (file.exists()) {
//                file.delete();
//            }
//            try {
//                if (!file.getParentFile().exists()) {// 父目录不存在 创建父目录
//                    file.getParentFile().mkdirs();
//                }
//
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            dir = file.getAbsolutePath();
//        }
//
//        return dir;
//    }
//
//    public static String getExternalFilesDir(Context context, String fileName) {/// storage/emulated/0/Android/data/com.kejiakeji.buddhas/files/Download/gift/
//        String fileDir = "";
//        if (context == null)
//            fileDir = HelloApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName + File.separator;
//        else
//            fileDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName + File.separator;
//        return fileDir;
//    }

    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath) {
        try {
            Bitmap bm = getSmallBitmap(filePath);
            FileOutputStream fos = new FileOutputStream(filePath);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int inSampleSize) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = inSampleSize;
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_4444;
        ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, newOpts);
    }

    public static String compressBitmap(Context context, Bitmap bitmap, int size) {
        try {
            Bitmap newbitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            newbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if (!newbitmap.isRecycled()) {
                newbitmap.recycle();
            }
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String compressBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = 2;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
        Bitmap newbitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        try {
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            newbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if (!newbitmap.isRecycled()) {
                newbitmap.recycle();
            }
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // 下面是对外公开的重载的方法
    ///////////////////////////////////////////////////////////////////////////

    public static void notifyScanDcim(Context context, String filePath) {
        scanFile(context, filePath);
    }

    public static void insertVideoToMediaStore(Context context, String filePath, long dateTaken, long duration) {
        insertVideoToMediaStore(context, filePath, dateTaken, 0, 0, duration);
    }

    public static void insertImageToMediaStore(Context context, String filePath, long createTime) {
        insertImageToMediaStore(context, filePath, createTime, 0, 0);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 扫描系统相册核心方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 针对系统文夹只需要扫描,不用插入内容提供者,不然会重复
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void scanFile(Context context, String filePath) {
        if (!checkFile(filePath))
            return;
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(intent);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 非系统相册像MediaContent中插入数据，核心方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 针对非系统文件夹下的文件,使用该方法 插入时初始化公共字段
     *
     * @param filePath 文件
     * @param time     ms
     * @return ContentValues
     */
    private static ContentValues initCommonContentValues(String filePath, long time) {
        ContentValues values = new ContentValues();
        File saveFile = new File(filePath);
        long timeMillis = getTimeWrap(time);
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, timeMillis);
        values.put(MediaStore.MediaColumns.DATE_ADDED, timeMillis);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        return values;
    }

    /**
     * 保存到照片到本地，并插入MediaStore以保证相册可以查看到,这是更优化的方法，防止读取的照片获取不到宽高
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param createTime 创建时间 <=0时为当前时间 ms
     * @param width      宽度
     * @param height     高度
     */
    public static void insertImageToMediaStore(Context context, String filePath, long createTime, int width, int height) {
        if (!checkFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, createTime);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0)
                values.put(MediaStore.Images.ImageColumns.WIDTH, 0);
            if (height > 0)
                values.put(MediaStore.Images.ImageColumns.HEIGHT, 0);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getPhotoMimeType(filePath));
        context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void insertImageToMediaStore(Context context, String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), path, file.getName(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存到视频到本地，并插入MediaStore以保证相册可以查看到,这是更优化的方法，防止读取的视频获取不到宽高
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param createTime 创建时间 <=0时为当前时间 ms
     * @param duration   视频长度 ms
     * @param width      宽度
     * @param height     高度
     */
    public static void insertVideoToMediaStore(Context context, String filePath, long createTime, int width, int height, long duration) {
        if (!checkFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, createTime);
        if (duration > 0)
            values.put(MediaStore.Video.VideoColumns.DURATION, duration);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0)
                values.put(MediaStore.Video.VideoColumns.WIDTH, width);
            if (height > 0)
                values.put(MediaStore.Video.VideoColumns.HEIGHT, height);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getVideoMimeType(filePath));
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    // 是不是系统相册
    private static boolean isSystemDcim(String path) {
        return path.toLowerCase().contains("dcim") || path.toLowerCase().contains("camera");
    }

    // 获取照片的mine_type
    private static String getPhotoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("jpg") || lowerPath.endsWith("jpeg")) {
            return "image/jpeg";
        } else if (lowerPath.endsWith("png")) {
            return "image/png";
        } else if (lowerPath.endsWith("gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }

    // 获取video的mine_type,暂时只支持mp4,3gp
    private static String getVideoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("mp4") || lowerPath.endsWith("mpeg4")) {
            return "video/mp4";
        } else if (lowerPath.endsWith("3gp")) {
            return "video/3gp";
        }
        return "video/mp4";
    }

    // 获得转化后的时间
    private static long getTimeWrap(long time) {
        if (time <= 0) {
            return System.currentTimeMillis();
        }
        return time;
    }

    // 检测文件存在
    private static boolean checkFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            return true;
        }
        return false;
    }

    public static long getVideoDuration(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        try {
            return Long.parseLong(duration);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVideoThumbnail(Context context, String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = 2;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
        Bitmap newbitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

        try {
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            newbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            if (!newbitmap.isRecycled()) {
                newbitmap.recycle();
            }
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取指定Activity的截屏，保存到png文件
     *
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        savePic(b, "/sdcard/screen_test.png");
        return b;
    }

    /**
     * 保存到sdcard
     *
     * @param b
     * @param strFileName
     */
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把View对象转换成bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    public static Bitmap getBitmapByView(View vBottomCode) {

        Bitmap bottom = Bitmap.createBitmap(vBottomCode.getWidth(), vBottomCode.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bottom);
        vBottomCode.draw(canvas2);

        return bottom;
    }

//    /**
//     * 截取scrollview的屏幕
//     */
//    public static Bitmap getBitmapByView(ScrollView scrollView, View vBottomCode) {
//        int h = 0;
//        Bitmap bitmap = null;
//        // 获取listView实际高度
//        for (int i = 0; i < scrollView.getChildCount(); i++) {
//            h += scrollView.getChildAt(i).getHeight();
//            // scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//        Log.d(TAG, "实际高度:" + h);
//        Log.d(TAG, " 高度:" + scrollView.getHeight());
//        // 创建对应大小的bitmap
//        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        scrollView.draw(canvas);
//
//        Bitmap bottom = Bitmap.createBitmap(vBottomCode.getWidth(), vBottomCode.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas2 = new Canvas(bottom);
//        vBottomCode.draw(canvas2);
//
//        int disparity = DisplayUtils.dp2px(scrollView.getContext(), 5);
//        return splitVertical(bitmap, bottom, disparity);
//    }

    public static Bitmap splitVertical(Bitmap first, Bitmap second, int disparity) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight() - disparity;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight() - disparity, null);
        return result;
    }

    /**
     * ListView 截屏
     *
     * @param listView
     * @param context
     * @return
     */
    public static Bitmap createBitmap(ListView listView, Context context) {
        int titleHeight, width, height, rootHeight = 0;
        Bitmap bitmap;
        Canvas canvas;
        int yPos = 0;
        int listItemNum;
        List<View> childViews = null;

        width = getDisplayMetrics(context)[0];// 宽度等于屏幕宽

        ListAdapter listAdapter = listView.getAdapter();
        listItemNum = listAdapter.getCount();
        childViews = new ArrayList<View>(listItemNum);
        View itemView;
        // 计算整体高度:
        for (int pos = 0; pos < listItemNum; ++pos) {
            itemView = listAdapter.getView(pos, null, listView);
            // measure过程
            itemView.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childViews.add(itemView);
            rootHeight += itemView.getMeasuredHeight();
        }

        height = rootHeight;
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), height, Bitmap.Config.ARGB_8888);
        // bitmap = BitmapUtil.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        Bitmap itemBitmap;
        int childHeight;
        // 把每个ItemView生成图片，并画到背景画布上
        for (int pos = 0; pos < childViews.size(); ++pos) {
            itemView = childViews.get(pos);
            childHeight = itemView.getMeasuredHeight();
            itemBitmap = viewToBitmap(itemView, width, childHeight);
            if (itemBitmap != null) {
                canvas.drawBitmap(itemBitmap, 0, yPos, null);
            }
            yPos = childHeight + yPos;
        }

//		canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    private static Bitmap viewToBitmap(View view, int viewWidth, int viewHeight) {
        view.layout(0, 0, viewWidth, viewHeight);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static final Integer[] getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return new Integer[]{screenWidth, screenHeight};
    }

    /**
     * 保存图片并插入图库
     *
     * @param context
     * @param bmp
     */
//    public static String saveImageToGallery(Context context, Bitmap bmp) {
//        // 首先保存图片
//        File appDir = new File(Environment.getExternalStorageDirectory(), Constants.BASE_FILE + "com.android.helloElectricity/cache/");
//
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        String fileName = "output_image"+System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 最后通知图库更新
//        String path = file.getAbsolutePath();
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//
//        return path;
//    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static File rotaingImageView(Context context, int angle, File oldfile) {
        //旋转图片 动作
        BitmapFactory.Options opts = new BitmapFactory.Options();//获取缩略图显示到屏幕上
        opts.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(oldfile.getAbsolutePath(), opts);

        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        try {
            File file = getCacheFile(context, "screenshot" + System.currentTimeMillis() + ".png");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (!resizedBitmap.isRecycled()) {
                resizedBitmap.recycle();
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAlbumPicturePath(Context mContext, Intent data) {
        // 判断手机系统版本号
        Uri uri = data.getData();
        //Build.VERSION.SDK_INT >= 19? 4.4及以上系统使用这个方法处理图片:4.4以下系统使用这个方法处理图片
        return Build.VERSION.SDK_INT >= 19 ? uriToPath(mContext, uri) : getImagePath(mContext, uri, null);
    }

    private static String uriToPath(Context mContext, Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(mContext, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(mContext, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(mContext, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    private static String getImagePath(Context mContext, Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = mContext.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
