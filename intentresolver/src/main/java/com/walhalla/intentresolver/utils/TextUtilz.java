package com.walhalla.intentresolver.utils;

import android.content.Context;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtilz {



    public static String dec0(int[] intArray) {
        char[] strArray = new char[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            strArray[i] = (char) intArray[i];
        }
        return new StringBuilder((String.valueOf(strArray))).reverse().toString();
    }

    public static String makeFileName(String s) {
        String fileName = s.replaceAll(":", "_");
        fileName = fileName
                .replaceAll(" ", "_")
                .replaceAll("\\*", "_")
                .replaceAll("\\.", "_")
                .replaceAll("@", "_")
                .replaceAll("/", "_")
                .replaceAll(":", "_");
        return fileName;
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

    public static List<String> loadFileList(File file) {
        List<String> list = new ArrayList<String>();
        File[] mm = file.listFiles(pathname -> pathname.getName().endsWith(".mp4"));
        for (File file1 : mm) {
            list.add(file1.getAbsolutePath());
        }
        return list;
    }
    public static List<String> readAllLines(Context context, String fileName) {
        List<String> lines = new ArrayList<>();
        InputStream is = null;
        BufferedReader reader = null;

        try {
            is = context.getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lines;
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

    private static void deletingDuplcateLines(String filePath) throws IOException {
        String input = null;
        //Instantiating the Scanner class
        Scanner sc = new Scanner(new File(filePath));
        //Instantiating the FileWriter class
        FileWriter writer = new FileWriter(filePath);
        //Instantiating the Set class
        Set set = new HashSet();
        while (sc.hasNextLine()) {
            input = sc.nextLine();
            if (set.add(input)) {
                writer.append(input + "\n");
            }
        }
        writer.flush();
        System.out.println("Contents added............");
    }

    public static void saveToFile(File fileNew, List<String> m) {
        String tmp = String.join("\n", m);
        saveToFile(fileNew, tmp);
    }

    public static void saveToFile(File fileNew, String tmp) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNew, false), StandardCharsets.UTF_8));
            fileWriter.append(tmp);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> divideString(String input, int lines) {
        List<String> dividedStrings = new ArrayList<>();
        int wordsPerLine = input.split("\\s+").length / lines; // ������� ��������� ���������� ���� �� ������ ������
        String[] words = input.split("\\s+");
        StringBuilder currentLine = new StringBuilder();
        int wordCount = 0;
        for (String word : words) {
            if (wordCount == wordsPerLine) {
                dividedStrings.add(currentLine.toString());
                currentLine = new StringBuilder();
                wordCount = 0;
            }
            currentLine.append(word).append(" ");
            wordCount++;
        }
        dividedStrings.add(currentLine.toString()); // ��������� ���������� �����
        return dividedStrings;
    }
    public static String extractTextBetween(String input) {
        String regex = "\\d+_(.*?)\\.mp4";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return input;
        }
    }
    public static String selectAndShuffleTags(List<String> tags, int maxLength) {
        Collections.shuffle(tags);
        StringBuilder selectedTags = new StringBuilder();
        for (String tag : tags) {
            if (selectedTags.length() + tag.length() + 1 > maxLength) {
                break;
            }
            if (selectedTags.length() > 0) {
                selectedTags.append(" ");
            }
            selectedTags.append(tag);
        }
        return selectedTags.toString();
    }

    public static void makeFile(File outFile) throws IOException {
        outFile.createNewFile();
    }
}