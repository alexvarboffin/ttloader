package com.walhalla.libcore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinterestRegexp {
    public static void main(String[] args) {

        String[] texts = new String[]{
                "Взгляните на это… 👀 https://pin.it/5vVVwr5fM",
                "https://pin.it/5vVVwr5fM",
                "https://pin.it/5vVVwr5fM 777 nnnn",
                "aaa https://pin.it/5vVVwr5fM ddd"
        };

        for (String text : texts) {
            String regex = "\\bhttps?://\\S+\\b";
            Pattern pattern = Pattern.compile(regex);

            // Создаем объект Matcher для текста
            Matcher matcher = pattern.matcher(text);

            // Поиск ссылок в тексте и вывод их
            while (matcher.find()) {
                String url = matcher.group();
                System.out.println("Найденная ссылка: " + url);
            }
        }


    }
}
