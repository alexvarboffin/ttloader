//package com.walhalla.mvp;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.media.MediaScannerConnection;
//import android.os.Build;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.walhalla.abcsharedlib.SharedNetwork;
//import com.walhalla.ui.BuildConfig;
//import com.walhalla.ui.DLog;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//
//public class MainPresenter {
//
//    public static final int PERMISSION_SAVE_CANVAS = 1014;
//    public static final boolean MODE_ENABLED = true;
//
//    private final MainPresenterCallback callback;
//
//    private final String fileName;
//    /**
//     * Bitmap buffer
//     */
//    private static Bitmap bitmap;
//
//    public MainPresenter(String name, MainPresenterCallback callback) {
//        this.fileName = name;
//        this.callback = callback;
//    }
//
//    public static boolean checkExternalStoragePermission(Context context) {
//        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static void restartApplication(Activity activity) {
//        //activity.finish();
//        activity.finish();
//        Intent intent = new Intent(activity, activity.getClass());
//        activity.startActivity(intent);
//    }
//
////    public static File saveCanvasToCacheFolder(View view, String fileName) {
////
////        DLog.d("Canvas Width: " + view.getWidth());
////        DLog.d("Canvas Height: " + view.getHeight());
////
////        if (bitmap == null) {
////            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
////        }
////        Canvas canvas = new Canvas(bitmap);
////
////        //create directory if not exist
//////        File dir = new File(ssssdddddd.getPath());
//////        if (!dir.exists()) {
//////            boolean res = dir.mkdirs();
//////        }
////
////
////        // save bitmap to cache directory
////        File dir = SharedObjects.imageCacheDir(view.getContext());
////        File file = new File(dir, fileName);
////
////
////        try {
////            // Output the file
////            FileOutputStream stream = new FileOutputStream(file);
////            view.draw(canvas);
////
////            // Convert the file file to Image such as .png
////            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
////            stream.flush();
////            stream.close();
////            return (file.exists() && !file.isDirectory()) ? file : null;
////
////        } catch (FileNotFoundException e) {
////            DLog.handleException(e);
////        } catch (IOException e) {
////            DLog.handleException(e);
////        }
////        bitmap = null;
////        return null;
////    }
////
////    public static File saveCanvasToExternalFolder(View view, String fileName) {
////        Context context = view.getContext().getApplicationContext();
//////        DLog.d("Canvas Width: " + view.getWidth());
//////        DLog.d("Canvas Height: " + view.getHeight());
////
////
////        if (bitmap == null) {
////            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
////        }
////        Canvas canvas = new Canvas(bitmap);
////
////        File dir = SharedObjects.externalMemory();
////        if (!dir.exists()) {
////            boolean res = dir.mkdirs();
////        }
////
////        File file = new File(dir, fileName);
////        //OutputStream os;
////
////
////        try {
////            DLog.d("@@@" + new File(dir, fileName).createNewFile());
////        } catch (Exception e) {
////            DLog.handleException(e);
////        }
////
////        try {
////            // Output the file
////            FileOutputStream stream = new FileOutputStream(file);
////            view.draw(canvas);
////
////            // Convert the file file to Image such as .png
////            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
////            stream.flush();
////            stream.close();
////
//////            os = new FileOutputStream(file);
//////            if (bitmap != null) {
//////                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//////            }
//////            os.flush();
//////            os.close();
////
////            //this code will scan the image so that it will appear in your gallery when you open next time
////            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
////                    (path, uri) -> DLog.d("image is saved in gallery and gallery is refreshed.")
////            );
////        } catch (Exception e) {
////            DLog.d("store: " + e.getLocalizedMessage());
////        }
////        return dir;
////    }
//
//    private static String urlEncode(String s) {
//        try {
//            return URLEncoder.encode(s, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            Log.wtf("TAG", "UTF-8 should always be supported", e);
//            return "";
//        }
//    }
//
////    public void shareFile(View mView, Activity activity, final String text, String network) {
////        if (!text.trim().isEmpty()) {
////            String mounted = Environment.getExternalStorageState();
////            if (Environment.MEDIA_MOUNTED.equals(mounted)) {
////
////                if (BuildConfig.DEBUG) {
////                    DLog.d("SDCard Mounted? "
////                            + Environment.MEDIA_MOUNTED
////                            + " " + Environment.getExternalStorageState());
////                }
////
////                if (Build.VERSION.SDK_INT >= 23) {
////                    if (checkExternalStoragePermission(activity)) {
////
////                        File dir = SharedObjects.externalMemory();
////                        File file = new File(dir, fileName);
////
////                        if (BuildConfig.DEBUG) {
////                            DLog.d("[Selected] - [External memory], " + file.getAbsolutePath());
////                        }
////
////                        if (!file.exists()) {
////                            file = saveCanvasToExternalFolder(mView, fileName);
////                        }
////                        if (file != null) {
////                            Toast.makeText(activity, "Successfully Saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
////                            SharedObjects.shareeeeeeeeeef(activity, file, text, network);
////                        }
////
////                    } else {
////                        if (MODE_ENABLED) {
////                            requestPermission(activity);//Request permission
////                        } else {
////                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
////                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
////                                if (BuildConfig.DEBUG) {
////                                    DLog.d("[Selected] - [Internal memory]");
////                                }
////                                File file = new File(SharedObjects.imageCacheDir(activity), fileName);
////                                if (!file.exists()) {
////                                    file = saveCanvasToCacheFolder(mView, fileName);
////                                }
////                                if (file != null) {
////                                    SharedObjects.shareeeeeeeeeef(activity, file, text, network);
////                                }
////                            } else {
////                                ActivityCompat.requestPermissions(activity, new String[]{
////                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
////                                }, PERMISSION_SAVE_CANVAS);
////                            }
////                        }
////                    }
////                } else {
////                    if (BuildConfig.DEBUG) {
////                        DLog.d("[Selected] - [External memory]");
////                    }
////                    File file = new File(SharedObjects.externalMemory(), fileName);
////                    if (!file.exists()) {
////                        file = saveCanvasToExternalFolder(mView, fileName);
////                    }
////                    if (file != null) {
////                        Toast.makeText(activity, "Successfully Saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
////                        SharedObjects.shareeeeeeeeeef(activity, file, text, network);
////                    }
////                }
////            } else {
////                if (BuildConfig.DEBUG) {
////                    DLog.d("[External] - [" + mounted + "]");
////                    DLog.d("[Selected] - [Internal memory]");
////                }
////                File file = new File(SharedObjects.imageCacheDir(activity), fileName);
////                if (!file.exists()) {
////                    file = saveCanvasToCacheFolder(mView, fileName);
////                }
////                if (file != null) {
////                    SharedObjects.shareeeeeeeeeef(activity, file, text, network);
////                }
////            }
////        }
////    }
//
//    /**
//     * Share file to other application or twitter.
//     */
//
//
////    private void requestPermission(Activity activity) {
////        String perm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
////        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)) {
////            callback.showNoStoragePermissionSnackbar();
////
//////            Toast.makeText(getActivity(), "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
//////
//////            AlertDialog.Builder builder = new AlertDialog.Builder(fragment);
//////            builder.setTitle("Важное сообщение!")
//////                    .setMessage("Write External Storage permission allows us to create files. Please allow this permission in App Settings.")
//////                    .setIcon(R.mipmap.ic_launcher)
//////                    .setCancelable(false)
//////                    .setNegativeButton(android.R.string.cancel,
//////                            new DialogInterface.OnClickListener() {
//////                                public void onClick(DialogInterface dialog, int id) {
//////                                    dialog.cancel();
//////                                }
//////                            })
//////                    .setPositiveButton("ok 2", new DialogInterface.OnClickListener() {
//////                        @Override
//////                        public void onClick(DialogInterface dialog, int which) {
//////                            try {
//////                                Intent intent = new Intent();
//////                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//////                                Uri uri = Uri.fromParts("package", fragment.getPackageName(), null);
//////                                intent.setData(uri);
//////                                startActivity(intent);
//////                            } catch (Exception e) {
//////                                //
//////                            }
//////                        }
//////                    });
//////            AlertDialog alert = builder.create();
//////            alert.show();
////        } else {
////            ActivityCompat.requestPermissions(activity, new String[]{perm}, PERMISSION_SAVE_CANVAS);
////        }
////    }
//}
