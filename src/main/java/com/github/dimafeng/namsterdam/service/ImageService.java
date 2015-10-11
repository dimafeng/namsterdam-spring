package com.github.dimafeng.namsterdam.service;

import com.github.dimafeng.namsterdam.dao.ImageRepository;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class ImageService {
    a
    static final Logger log = LoggerFactory.getLogger(ImageService.class);

    private Path tempDir;

    @Autowired
    private ImageRepository imageRepository;

    private Path watermark;

    private ExecutorService pool = Executors.newFixedThreadPool(1);

    @PostConstruct
    public void init() throws IOException {
        tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("images");
        if (Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
    }

    public byte[] gridImage(byte[] image, int size) throws Exception {
        return convert(image, false, Optional.of(sb -> {
            sb.append(" -brightness-contrast -20x-40 ");
            sb.append(" -resize ").append(size).append(" ");
            sb.append(" -adaptive-blur 10.5");
        }));
    }

    public byte[] resize(byte[] image, int size) throws Exception {
        return convert(image, size > 399, Optional.of(sb -> {
            sb.append(" -sharpen 1.2 ");
            sb.append(" -resize ").append(size).append(" ");
            sb.append(" -quality 95 ");
        }));
    }

    public synchronized byte[] convert(byte[] image, boolean addWatermark, Optional<Consumer<StringBuilder>> operations) throws Exception {

        File source = File.createTempFile("image_conversion_source", "");
        File output = File.createTempFile("image_conversion_output", ".jpg");

        try {
            com.google.common.io.Files.write(image, source);

            Runtime rt = Runtime.getRuntime();
            StringBuilder sb = new StringBuilder("convert ");
            sb.append(source.getAbsolutePath());

            operations.ifPresent(o -> o.accept(sb));
            sb.append(" ").append(output.getAbsolutePath());

            log.info(sb.toString());

            Process pr = rt.exec(sb.toString());
            pr.waitFor();

            if (addWatermark) {
                output = addWatermark(output);
            }

            return com.google.common.io.Files.toByteArray(output);
        } finally {
            source.delete();
            output.delete();
        }
    }

    private File addWatermark(File input) throws Exception {
        File output = File.createTempFile("image_conversion_output", ".jpg");
        try {
            Runtime rt = Runtime.getRuntime();
            StringBuilder sb = new StringBuilder("convert ");

            Process pr = rt.exec("composite -gravity southeast " + getWatermarkPath()
                    + " " + input.getAbsolutePath() + " " + output.getAbsolutePath());
            pr.waitFor();

            return output;
        } finally {
            input.delete();
        }
    }

    private String getWatermarkPath() throws IOException {
        if (watermark == null || !Files.exists(watermark)) {
            watermark = Files.createTempFile("watermark", "");
            Files.copy(ImageService.class.getResourceAsStream("/public/images/watermark.png"), watermark, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
        }
        return watermark.toAbsolutePath().toString();
    }

    public byte[] convertToJpgOriginalSize(byte[] image) throws Exception {
        return convert(image, false, Optional.<Consumer<StringBuilder>>empty());
    }

    public byte[] getImage(int size, String id, boolean gridImage) throws Exception {
        Path folder = tempDir.resolve(Integer.toString(size));
        Path image = folder.resolve(id + (gridImage ? "grid" : "") + ".jpg");
        if (Files.exists(image)) {
            return Files.readAllBytes(image);
        } else {
            Preconditions.checkArgument(size % 100 == 0);
            Preconditions.checkArgument(size > 0);
            Preconditions.checkArgument(size < 2000);
            Files.createDirectories(folder);
            byte[] data = imageRepository.findOne(id).getData();
            byte[] convert;
            if (gridImage) {
                convert = gridImage(data, size);
            } else {
                convert = resize(data, size);
            }
            Files.write(image, convert);

            return convert;
        }
    }

    public int[] getSize(String gridImageId) throws IOException {
        BufferedImage read = ImageIO.read(new ByteArrayInputStream(imageRepository.findOne(gridImageId).getDataJPG()));
        return new int[]{read.getWidth(), read.getHeight()};
    }
}
