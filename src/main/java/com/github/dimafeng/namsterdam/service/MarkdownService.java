package com.github.dimafeng.namsterdam.service;


import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tautua.markdownpapers.Markdown;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MarkdownService {

    public static final Pattern INSTAGRAM_PATTERN = Pattern.compile("(inst\\{([^\\}]*?)\\})");
    public static final Pattern MAP_PATTERN = Pattern.compile("(map\\{([^\\}]*?)\\})");

    @Autowired
    private Markdown markdown;

    public String processInstagram(String body) throws Exception {
        String result = body;

        Matcher m = INSTAGRAM_PATTERN.matcher(body);
        while (m.find()) {
            String url = m.group(2);

            URL content = new URL("http://api.instagram.com/oembed?url=" + url);

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };

            HashMap<String, Object> o = mapper.readValue(content, typeRef);

            result = result.replace(m.group(1), "<div class=\"article-image\"><img src=\""+o.get("url").toString()+"\"></div>");
        }

        return result;
    }

    public String processMap(String body) throws Exception {
        String result = body;

        Matcher m = MAP_PATTERN.matcher(body);
        while (m.find()) {
            String url = m.group(2);

            result = result.replace(m.group(1), "<div class=\"article-map\"><div class=\"article-map-wrapper\"><iframe width=\"100%\" height=\"450\" frameborder=\"0\" style=\"border:0\"\n" +
                    "src=\""+url.toString()+"&key=AIzaSyCfwXzKm-L9CGIRBHTVXBylk2el1sZ-iYw\"></iframe></div></div>");
        }

        return result;
    }

    public String processALL(String body) throws Exception {
        String result = body;
        /**
         * Markdown
         */
        StringWriter sw = new StringWriter();
        markdown.transform(new InputStreamReader(new ByteArrayInputStream(result.getBytes())), sw);

        result = sw.toString();
        result = processMap(result);
        result = processInstagram(result);

        return result;
    }
}
