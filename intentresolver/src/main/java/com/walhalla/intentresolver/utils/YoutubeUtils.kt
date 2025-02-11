package com.walhalla.intentresolver.utils

import android.content.Context
import com.walhalla.intentresolver.R
import com.walhalla.intentresolver.utils.TextUtilz.divideString
import com.walhalla.intentresolver.utils.TextUtilz.selectAndShuffleTags
import java.io.File
import java.util.Arrays
import java.util.Collections


object YoutubeUtils {
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
    @JvmStatic
    fun generateTitle(file: File): String {
        val simpleTitle = file.name
        val clearTmpl = simpleTitle
            .replace("__", "/")
        return clearTmpl.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

    private fun format1(qq: List<String?>): String {
        val xx = java.lang.String.join(", ", qq).replace("#", "")
        val m1 = divideString(xx, 10)
        return java.lang.String.join("\n", m1).trim { it <= ' ' } + "."
    }

    fun generateDescriptionFromTemplate(
        context: Context,
        tags: List<String>,
        simpleTitle: String
    ): String {
        val tagText = selectAndShuffleTags(tags, 450)
        val mm = tagText.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val qq = Arrays.asList(*mm)
        Collections.shuffle(qq)
        val xx = format1(qq)

        val m1 = divideString(tagText, 10)

        val tagzz = java.lang.String.join("\n", m1)
        val txt =
            String.format(context.getString(R.string.yt_share_description), simpleTitle, tagzz, xx)
        return txt
    }
}
