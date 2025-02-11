package com.walhalla.intentresolver.utils

import android.content.Context
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.Collections
import java.util.Scanner
import java.util.regex.Pattern

object TextUtilz {
    @JvmStatic
    fun dec0(intArray: IntArray): String {
        val strArray = CharArray(intArray.size)
        for (i in intArray.indices) {
            strArray[i] = intArray[i].toChar()
        }
        return StringBuilder((String(strArray))).reverse().toString()
    }

    fun makeFileName(s: String): String {
        var fileName = s.replace(":".toRegex(), "_")
        fileName = fileName
            .replace(" ".toRegex(), "_")
            .replace("\\*".toRegex(), "_")
            .replace("\\.".toRegex(), "_")
            .replace("@".toRegex(), "_")
            .replace("/".toRegex(), "_")
            .replace(":".toRegex(), "_")
        return fileName
    }

    //    public static List<String> readAllLines(String title) {
    //        try {
    //            return Files.readAllLines(new File(title).toPath());
    //        } catch (IOException e) {
    //            return new ArrayList<>();
    //        }
    //    }
    //    public static List<String> loadUrlsList(File baseFile) {
    //        try {
    //            List<String> urls = Files.readAllLines(baseFile.toPath());
    //            return urls;
    //        } catch (IOException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
    //IslamWallpapers
    //    public static Set<String> readUrls(String aaa0) {
    //        Set<String> set = new LinkedHashSet<>();
    //        try {
    //            File file = new File(aaa0);
    //            List<String> aaa = Files.readAllLines(file.toPath());
    //            set.addAll(aaa);
    //        } catch (IOException e) {
    //        }
    //        return set;
    //    }
    fun loadFileList(file: File): List<String> {
        val list: MutableList<String> = ArrayList()
        val mm = file.listFiles { pathname: File ->
            pathname.name.endsWith(
                ".mp4"
            )
        }
        for (file1 in mm!!) {
            list.add(file1.absolutePath)
        }
        return list
    }

    @JvmStatic
    fun readAllLines(context: Context, fileName: String): List<String> {
        val lines: MutableList<String> = ArrayList()
        var `is`: InputStream? = null
        var reader: BufferedReader? = null

        try {
            `is` = context.assets.open(fileName)
            reader = BufferedReader(InputStreamReader(`is`))

            var line: String
            while ((reader.readLine().also { line = it }) != null) {
                lines.add(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return lines
    }

    //    public static List<String> readUniqShuffleUrls(List<String> aaa0) {
    //        List<String> list = new ArrayList<String>();
    //
    //        for (String s : aaa0) {
    //            Set<String> set = new LinkedHashSet<>();
    //            try {
    //                File file = new File(s);
    //                List<String> aaa = Files.readAllLines(file.toPath());
    //                set.addAll(aaa);
    //            } catch (IOException e) {
    //            }
    //            list.addAll(set);
    //        }
    //        Collections.shuffle(list);
    //        return list;
    //    }
    @Throws(IOException::class)
    private fun deletingDuplcateLines(filePath: String) {
        var input: String
        //Instantiating the Scanner class
        val sc = Scanner(File(filePath))
        //Instantiating the FileWriter class
        val writer = FileWriter(filePath)
        //Instantiating the Set class
        val set: MutableSet<String> = HashSet<String>()
        while (sc.hasNextLine()) {
            input = sc.nextLine()
            if (set.add(input)) {
                writer.append(input + "\n")
            }
        }
        writer.flush()
        println("Contents added............")
    }

    fun saveToFile(fileNew: File?, m: List<String?>) {
        val tmp = java.lang.String.join("\n", m)
        saveToFile(fileNew, tmp)
    }

    fun saveToFile(fileNew: File?, tmp: String?) {
        try {
            val fileWriter = BufferedWriter(
                OutputStreamWriter(
                    FileOutputStream(fileNew, false),
                    StandardCharsets.UTF_8
                )
            )
            fileWriter.append(tmp)
            fileWriter.flush()
            fileWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun divideString(input: String, lines: Int): List<String> {
        val dividedStrings: MutableList<String> = ArrayList()
        val wordsPerLine = input.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray().size / lines // ������� ��������� ���������� ���� �� ������ ������
        val words = input.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var currentLine = StringBuilder()
        var wordCount = 0
        for (word in words) {
            if (wordCount == wordsPerLine) {
                dividedStrings.add(currentLine.toString())
                currentLine = StringBuilder()
                wordCount = 0
            }
            currentLine.append(word).append(" ")
            wordCount++
        }
        dividedStrings.add(currentLine.toString()) // ��������� ���������� �����
        return dividedStrings
    }

    @JvmStatic
    fun extractTextBetween(input: String): String {
        val regex = "\\d+_(.*?)\\.mp4"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        return if (matcher.find()) {
            matcher.group(1)?:""
        } else {
            input
        }
    }

    fun selectAndShuffleTags(tags: List<String>, maxLength: Int): String {
        Collections.shuffle(tags)
        val selectedTags = StringBuilder()
        for (tag in tags) {
            if (selectedTags.length + tag.length + 1 > maxLength) {
                break
            }
            if (selectedTags.length > 0) {
                selectedTags.append(" ")
            }
            selectedTags.append(tag)
        }
        return selectedTags.toString()
    }

    @Throws(IOException::class)
    fun makeFile(outFile: File) {
        outFile.createNewFile()
    }
}