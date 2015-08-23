package com.brotherjing.danmakubay.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Brotherjing on 2015/8/23.
 */
public class TextUtil {

    public static String removeTags(String raw){
        String reg = "<[^>]*>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(raw);
        return matcher.replaceAll("");
    }

}
