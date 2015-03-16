package com.github.dimafeng.namsterdam.service;

import com.github.dimafeng.namsterdam.dao.ImageRepository;
import com.google.common.base.Preconditions;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private Path tempDir;

    @Autowired
    private ImageRepository imageRepository;

    @PostConstruct
    public void init() throws IOException {
        tempDir = Paths.get(System.getProperty("java.io.tmpdir")).resolve("images");
        if (Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
    }

    public byte[] convert(byte[] image, Integer size) throws InterruptedException, IOException, IM4JavaException {

        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Pipe pipeIn = new Pipe(bais, null);
        Pipe pipeOut = new Pipe(null, baos);

        IMOperation op = new IMOperation();
        op.addImage("-");
        if (size != null) {
            op.sharpen(1.2);
            op.resize(size);
            op.quality(95.);
        }
        op.addImage("jpeg:-");

        ConvertCmd cmd = new ConvertCmd();
        cmd.setInputProvider(pipeIn);
        cmd.setOutputConsumer(pipeOut);
        cmd.run(op);
        return baos.toByteArray();
    }

    public byte[] convertToJpgOriginalSize(byte[] image) throws InterruptedException, IOException, IM4JavaException {
        return convert(image, null);
    }

    public byte[] getImage(int size, String id) throws Exception {
        Path folder = tempDir.resolve(Integer.toString(size));
        Path image = folder.resolve(id + ".jpg");
        if (Files.exists(image)) {
            return Files.readAllBytes(image);
        } else {
            Preconditions.checkArgument(size % 100 == 0);
            Preconditions.checkArgument(size > 0);
            Preconditions.checkArgument(size < 2000);
            Files.createDirectories(folder);
            byte[] convert = convert(imageRepository.findOne(id).getData(), size);
            Files.write(image, convert);

            return convert;
        }
    }
}
