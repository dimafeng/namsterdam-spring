package com.github.dimafeng.namsterdam.service;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by dimafeng on 3/17/15.
 */
public class ImageServiceTest {
    @Test
    public void test() throws Exception {
        Files.write(Paths.get("/Users/dimafeng/Documents/test.jpg"),
                new ImageService().gridImage(Files.readAllBytes(
                        Paths.get("/Users/dimafeng/Downloads/15729589577_8261354419_h.jpg")), 400));
    }
}
