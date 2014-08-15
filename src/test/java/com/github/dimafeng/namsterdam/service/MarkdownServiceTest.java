package com.github.dimafeng.namsterdam.service;

import static org.junit.Assert.*;
import org.junit.Test;

public class MarkdownServiceTest {

    @Test
    public void test() throws Exception {
        MarkdownService ms = new MarkdownService();

        assertEquals("<div class=\"article-image\"><img src=\"http://photos-d.ak.instagram.com/hphotos-ak-xpa1/10520159_1442085232731227_2084925353_n.jpg\"></div>", ms.processInstagram("inst{http://instagram.com/p/q7i3yjtNpl/}"));
        assertEquals("<div class=\"article-image\"><img src=\"http://photos-d.ak.instagram.com/hphotos-ak-xpa1/10520159_1442085232731227_2084925353_n.jpg\"></div> test <div class=\"article-image\"><img src=\"http://photos-e.ak.instagram.com/hphotos-ak-xfa1/10584680_904095322937372_872298668_n.jpg\"></div>", ms.processInstagram("inst{http://instagram.com/p/q7i3yjtNpl/} test inst{http://instagram.com/p/rDZdOkw6DA/}"));
    }

}
