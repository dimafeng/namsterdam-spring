package com.github.dimafeng.namsterdam.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextServiceTest{

    @Test
    public void test() {
        HTMLService ts = new HTMLService();
        assertEquals("novosti_novsti_poka_test__test", ts.translit("Новости \"новсти\" пока test !@#$%@#!$^$#^&%^&(* test"));
    }

    @Test
    public void test2() {
        HTMLService ts = new HTMLService();
        assertEquals("http://photos-h.ak.instagram.com/1568025698.jpg", ts.getFirstImage("<img src=\"http://photos-h.ak.instagram.com/1568025698.jpg\">"));
        assertEquals("http://photos-h.ak.instagram.com/3245324558.jpg", ts.getFirstImage("<h1>Новая крутая статья</h1>\n" +
                "\n" +
                "<img src=\"http://photos-h.ak.instagram.com/3245324558.jpg\">\n" +
                "<img src=\"http://photos-h.ak.instagram.com/3234444558.jpg\">"));
    }
}
