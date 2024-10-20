package com.walhalla;

import android.content.Context;


public class Lesson {
    public Lesson(Context context) {

//        DownloadFile dd = new DownloadFile();
//        dd.make(
//                context,
//                "https://raw.githubusercontent.com/HexHive/FirmFuzz/master/framework/kernel_firmfuzz_armel/Documentation/input/ff.txt",
//                "HELLO_WORLD",
//                ".txt"
//        );
//
//
////        try {
////            String filename = "filename.txt";
////            String fileContents = "Text in file.";
////            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
////            fos.write(fileContents.getBytes());
////            fos.close();
////        } catch (Exception e) {
////            DLog.handleException(e);
////        }
//
//        Set<File> test = new HashSet<>();
////        //storage/sdcard/Android/data/com.walhalla.ttloader/files
//        test.add(context.getExternalFilesDir(null));
////        //storage/sdcard/Android/data/com.walhalla.ttloader/cache
////        test.add(context.getExternalCacheDir());
////        test.add(Environment.getExternalStorageDirectory());
//        test.add(new File(SharedObjects.externalMemory() + File.separator + Constants.DOWNLOAD_DIRECTORY));
//        test.add(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
//
//        for (File file : test) {
//            DLog.d("_______________" + file.getAbsolutePath() + "______________");
//            File[] mm = file.listFiles();
//            for (int i = 0; i < mm.length; i++) {
//                DLog.d("##################" + mm[i].getAbsolutePath());
//            }
//        }
//        try {
//            File[] tmp0 = new File[0];
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                tmp0 = context.getExternalMediaDirs();
//                DLog.d("@@@@@@@@@" + Arrays.toString(tmp0));
//                DLog.d("@@@@@@@@@" + Arrays.toString(context.getExternalMediaDirs()));
//                DLog.d("@@@@@@@@@" + Arrays.toString(context.getExternalCacheDirs()));
//                DLog.d("@@@@@@@@@" + Arrays.toString(context.getExternalFilesDirs(null)));
//            }
//
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//            DLog.d("@@@@@@@@@@@@@@@+" + Environment.DIRECTORY_DOWNLOADS);
//            DLog.d("@@@@@@@@@@@@@@@+" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//
//
//        } catch (Exception e) {
//            DLog.handleException(e);
//        }
//
//
//        File tmp00 = new File(SharedObjects.externalMemory() + File.separator + Constants.DOWNLOAD_DIRECTORY);
//        if (!tmp00.exists()) {
//            boolean res = tmp00.mkdirs();
//            DLog.d("\uD83D\uDE0D CREATE FOLDER -> " + res + tmp00.getAbsolutePath());
//            ///storage/sdcard/TTDwn
//        }
    }
}
