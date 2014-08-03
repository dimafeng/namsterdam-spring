package com.github.dimafeng.namsterdam.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HTMLService {
    final Map<Character, String> map = new HashMap<Character, String>() {
        {
            put('а', "a");
            put('б', "b");
            put('в', "v");
            put('г', "g");
            put('д', "d");
            put('е', "e");
            put('ё', "yo");
            put('ж', "zh");
            put('з', "z");
            put('и', "i");
            put('й', "j");
            put('к', "k");
            put('л', "l");
            put('м', "m");
            put('н', "n");
            put('о', "o");
            put('п', "p");
            put('р', "r");
            put('с', "s");
            put('т', "t");
            put('у', "u");
            put('ф', "f");
            put('х', "h");
            put('ц', "ts");
            put('ч', "ch");
            put('ш', "sh");
            put('щ', "sh'");
            put('ъ', "");
            put('ы', "y");
            put('ь', "");
            put('э', "e");
            put('ю', "yu");
            put('я', "ya");
            put(' ', "_");
        }
    };
    public static final Pattern IMAGE = Pattern.compile("(<img([^>]*?)src=\"([^\"]*?)\"([^>]*?)>)");

    public String translit(String text) {
        int len = text.length();
        if (len == 0)
            return text;
        StringBuilder sb = new StringBuilder();
        String _text = text.toLowerCase();
        for (int i = 0; i < _text.length(); i++) {
            char nextChar = _text.charAt(i);

            String tr = map.get(nextChar);
            if (tr != null) {
                sb.append(tr);
            } else if (nextChar >= 'a' && nextChar <= 'z') {
                sb.append(nextChar);
            }
        }

        return sb.toString();
    }

    public String getFirstImage(String html) {
        Matcher matcher = IMAGE.matcher(html);
        if (matcher.find()) {
            return matcher.group(3);
        }
        return null;
    }
}
