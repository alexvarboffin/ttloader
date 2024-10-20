package com.walhalla.intentresolver.utils;


import android.content.Context;


import com.walhalla.intentresolver.R;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class YoutubeUtils {

//    static String _description_ = "%1s\n" +
//            "\n" +
//            "\n" +
//            "\n" +
//            "\n" +
//            "%1s\n" +
//            "\n" +
//            "\n" +
//            "Your Queries:\n" +
//            "%3s"
////             +"\n"
////            +"COPYRIGHT DISCLAIMER:\n" +
////            "copyright Act 1976, allowance is mad for FAIR USE for purpose such a as criticism, comment, news reporting, teaching, scholarship and research. Fair use is a use permitted by copyright statues that might otherwise be infringing. Non- Profit, educational or personal use tips the balance in favor of FAIR USE."
//            ;

    public static String generateTitle(File file) {
        String simpleTitle = file.getName();
        String clearTmpl = simpleTitle
                .replace("__", "/");
        return clearTmpl.split("_")[1];
    }

    private static String format1(List<String> qq) {
        String xx = String.join(", ", qq).replace("#", "");
        List<String> m1 = TextUtilz.divideString(xx, 10);
        return String.join("\n", m1).trim() + ".";
    }

    public static String generateDescriptionFromTemplate(Context context, List<String> tags, String simpleTitle) {
        String tagText = TextUtilz.selectAndShuffleTags(tags, 450);
        String[] mm = tagText.split(" ");
        List<String> qq = Arrays.asList(mm);
        Collections.shuffle(qq);
        String xx = format1(qq);

        List<String> m1 = TextUtilz.divideString(tagText, 10);

        String tagzz = String.join("\n", m1);
        String txt = String.format(context.getString(R.string.yt_share_description), simpleTitle, tagzz, xx);
        return txt;
    }
}
