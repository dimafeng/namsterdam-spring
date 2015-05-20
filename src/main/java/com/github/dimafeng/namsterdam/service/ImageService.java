package com.github.dimafeng.namsterdam.service;

import com.github.dimafeng.namsterdam.dao.ImageRepository;
import com.google.common.base.Preconditions;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

@Service
public class ImageService {

    private Path tempDir;

    @Autowired
    private ImageRepository imageRepository;

    private Path watermark;

    @PostConstruct
    public void init() throws IOException {
        tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("images");
        if (Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
    }

    public byte[] gridImage(byte[] image, int size) throws Exception {
        return convert(image, false, op -> {
            op.brightnessContrast(-20., -40.);
            op.resize(size);
            //op.adaptiveBlur(10.5);
        });
    }

    public byte[] resize(byte[] image, int size) throws Exception {
        return convert(image, size > 399, op -> {
            op.sharpen(1.2);
            op.resize(size);
            op.quality(95.);
        });
    }

    public byte[] convert(byte[] image, boolean addWatermark, Consumer<IMOperation> operations) throws Exception {

        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Pipe pipeIn = new Pipe(bais, null);
        Pipe pipeOut = new Pipe(null, baos);

        IMOperation op = new IMOperation();
        op.addImage("-");
        if (operations != null) {
            operations.accept(op);
        }
        op.addImage("jpeg:-");

        ConvertCmd cmd = new ConvertCmd();
        cmd.setInputProvider(pipeIn);
        cmd.setOutputConsumer(pipeOut);
        cmd.run(op);

        byte[] imageOutput = baos.toByteArray();
        return addWatermark ? addWatermark(imageOutput) : imageOutput;
    }

    private byte[] addWatermark(byte[] image) throws Exception {
        Path tempFileWithImage = Files.createTempFile("image", "");
        Files.write(tempFileWithImage, image);

        Path output = Files.createTempFile("image", "");

        try {
            IMOperation op = new IMOperation();
            op.gravity("southeast");
            op.addImage(getWatermarkPath());
            op.addImage(tempFileWithImage.toAbsolutePath().toString());
            op.addImage("jpeg:" + output.toAbsolutePath().toString());
            CompositeCmd compositeCmd = new CompositeCmd();
            compositeCmd.run(op);

            return Files.readAllBytes(output);
        } finally {
            Files.deleteIfExists(tempFileWithImage);
            Files.deleteIfExists(output);
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
        return convert(image, false, null);
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
