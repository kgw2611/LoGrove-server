package com.hansung.logrove.storage.util;

import com.hansung.logrove.storage.dto.ConvertedImage;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ImageConverter {

    public ConvertedImage toWebP(byte[] inputBytes, int maxDimension) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(inputBytes))
                    .size(maxDimension, maxDimension)
                    .keepAspectRatio(true)
                    .useExifOrientation(true)
                    .outputFormat("webp")
                    .toOutputStream(out);

            byte[] convertedBytes = out.toByteArray();
            try {
                var convertedImage = javax.imageio.ImageIO.read(new ByteArrayInputStream(convertedBytes));
                if (convertedImage != null) {
                    return new ConvertedImage(convertedBytes, convertedImage.getWidth(), convertedImage.getHeight());
                }
            } catch (Exception ignored) {
            }

            return new ConvertedImage(convertedBytes, null, null);
        } catch (Exception e) {
            return new ConvertedImage(inputBytes, null, null);
        }
    }
}
