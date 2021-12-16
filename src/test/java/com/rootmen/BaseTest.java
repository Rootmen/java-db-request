package com.rootmen;

import java.util.HashMap;
import java.util.Locale;

public class BaseTest {

    private static final String reset = "\u001B[0m";
    public static HashMap<String, String> colorMap = new HashMap<String, String>() {{
        put("black", "\u001B[30m");
        put("red", "\u001B[31m");
        put("green", "\u001B[32m");
        put("yellow", "\u001B[33m");
        put("blue", "\u001B[34m");
        put("purple", "\u001B[35m");
        put("cyan", "\u001B[36m");
        put("white", "\u001B[37m");
    }};

    public static void printText(String text) {
        System.out.println(text);
    }


    public static void printText(String text, String color) {
        System.out.println(colorMap.get(color) + text + reset);
    }
}
