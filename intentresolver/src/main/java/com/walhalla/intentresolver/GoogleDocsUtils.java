package com.walhalla.intentresolver;

import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;

public class GoogleDocsUtils {


    public static void send(Intent intent1, Uri uri) {//Google docs disc

        String to = "YourEmail@somewhere.com";
        String subject = "Backup";
        String message = "Your backup is attached";
        //Intent inten1 = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        intent1.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent1.putExtra(Intent.EXTRA_TEXT, message);
        intent1.putExtra("accountName", "zzz@gmail.com");
        intent1.putExtra("deleteOriginalFile", true);
        intent1.putExtra("documentTitle", "11111");


        //intent1.setType(mime);//Not work if false mimitype

        //Uri uRi = Uri.parse("file://" + file);
        //DLog.d("@@file://" + file + "  " + Uri.fromFile(file) + " " + uri.getScheme());
        //Exception intent1.setDataAndType(Uri.fromFile(file), "*/*");

        intent1.putParcelableArrayListExtra(Intent.EXTRA_STREAM, new ArrayList<>(Arrays.asList(uri, uri, uri)));
        intent1.setType("text/*");
        //intent1.setDataAndType(uri, "*/*");

        //send, send_multiple */*
        // or
        //send <data android:scheme="file"/>

//                        // Old Approach
//                        install.setDataAndType(Uri.fromFile(file), mimeType);
//// End Old approach
//// New Approach
//                        Uri apkURI = FileProvider.getUriForFile(
//                                context,
//                                context.getApplicationContext()
//                                        .getPackageName() + ".provider", file);
//                        install.setDataAndType(apkURI, mimeType);


    }
}
