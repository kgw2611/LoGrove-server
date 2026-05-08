package com.hansung.logrove.storage.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ImageConverter {

    public byte[] toWebP(byte[] inputBytes, int maxDimension) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(inputBytes))
                    .size(maxDimension, maxDimension)
                    .keepAspectRatio(true)
                    .outputFormat("webp")
                    .toOutputStream(out);
            return out.toByteArray();
        } catch (Exception e) {
            return inputBytes;
        }
    }
}
