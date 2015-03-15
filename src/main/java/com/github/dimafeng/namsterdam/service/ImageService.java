package com.github.dimafeng.namsterdam.service;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageService {

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
}
