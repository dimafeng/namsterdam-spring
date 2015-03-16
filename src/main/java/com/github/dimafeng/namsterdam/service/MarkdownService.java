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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MarkdownService {

    public static final Pattern INSTAGRAM_PATTERN = Pattern.compile("(inst\\{([^\\}]*?)\\})");
    public static final Pattern MAP_PATTERN = Pattern.compile("(map\\{([^\\}]*?)\\})");
    public static final Pattern FLICKR_PATTERN = Pattern.compile("(fl\\{([^\\}]*?)\\})");

    public static final Pattern SLIDER_PATTERN = Pattern.compile("(img\\{([^\\}]*?)\\})");

    @Autowired
    private Markdown markdown;

    public String processSliders(String body) throws Exception {
        String result = body;

        Matcher m = SLIDER_PATTERN.matcher(body);
        while (m.find()) {
            String images = m.group(2);

            StringBuilder sb = new StringBuilder();

            if (images.contains(",")) {
                List<String> imagesList = Arrays.asList(images.split(","));
                sb.append("<div class=\"article-slider\">");
                imagesList.stream().forEach(e -> {
                    sb.append("<a href=\"/images/1000/")
                            .append(e)
                            .append(".jpg\"><img src=\"/images/300/")
                            .append(e)
                            .append(".jpg\" alt=\"\" /></a>");
                });
                sb.append("</div>");
            } else {
                sb.append("<img class=\"article-image\" src=\"/images/1000/").append(images).append(".jpg\">");
            }

            result = result.replace(m.group(1), sb.toString());
        }

        return result;
    }

    public String processInstagram(String body) throws Exception {
        String result = body;

        Matcher m = INSTAGRAM_PATTERN.matcher(body);
        while (m.find()) {
            String url = m.group(2);
            String description = null;
            if (url.contains("|")) {
                String[] data = url.split("\\|");
                url = data[0].trim();
                description = data[1].trim();
            }

            URL content = new URL("http://api.instagram.com/oembed?url=" + url);

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };

            HashMap<String, Object> o = mapper.readValue(content, typeRef);

            result = result.replace(m.group(1), "<div class=\"article-image\"><img src=\"" + o.get("url").toString() + "\">" +
                    (description != null ? ("<div class=\"description description640\">" + description + "</div>") : "")
                    + "</div>");
        }

        return result;
    }

    public String processFlickr(String body) throws Exception {
        String result = body;

        Matcher m = FLICKR_PATTERN.matcher(body);
        while (m.find()) {
            String url = m.group(2);
            String description = null;
            if (url.contains("|")) {
                String[] data = url.split("\\|");
                url = data[0].trim();
                description = data[1].trim();
            }

            String images = null;
            Matcher m2 = Pattern.compile("img src=\"([^\"]*?)\"").matcher(url);
            if (m2.find()) {
                images = m2.group(1);
            }

            String url2 = null;
            Matcher m3 = Pattern.compile("href=\"([^\"]*?)\"").matcher(url);
            if (m3.find()) {
                url2 = m3.group(1);
            }

            result = result.replace(m.group(1), "<div class=\"article-image\"><a target=\"_blank\" href=\"" + url2 + "\"><img src=\"" + images + "\"></a>" +
                    (description != null ? ("<div class=\"description description800\">" + description + "</div>") : "")
                    + "</div>");
        }

        return result;
    }

    public String processMap(String body) throws Exception {
        String result = body;

        Matcher m = MAP_PATTERN.matcher(body);
        while (m.find()) {
            String url = m.group(2);

            result = result.replace(m.group(1), "<div class=\"article-map\"><div class=\"article-map-wrapper\"><iframe width=\"100%\" height=\"450\" frameborder=\"0\" style=\"border:0\"\n" +
                    "src=\"" + url.toString() + "&key=AIzaSyCfwXzKm-L9CGIRBHTVXBylk2el1sZ-iYw\"></iframe></div></div>");
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
        result = processSliders(result);
        result = processMap(result);
        result = processInstagram(result);
        result = processFlickr(result);

        return result;
    }
}
