package com.hansung.logrove.storage.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Component
public class ImageConverter {

    public byte[] toWebP(byte[] inputBytes, int maxDimension) {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(inputBytes));
            if (original == null) {
                return inputBytes;
            }

            int targetWidth = original.getWidth();
            int targetHeight = original.getHeight();

            // Downscale oversized images only. Small images must not be enlarged.
            if (targetWidth > maxDimension || targetHeight > maxDimension) {
                double ratio = Math.min(
                        (double) maxDimension / targetWidth,
                        (double) maxDimension / targetHeight
                );
                targetWidth = Math.max(1, (int) Math.round(targetWidth * ratio));
                targetHeight = Math.max(1, (int) Math.round(targetHeight * ratio));
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(original)
                    .size(targetWidth, targetHeight)
                    .keepAspectRatio(true)
                    .outputFormat("webp")
                    .toOutputStream(out);
            return out.toByteArray();
        } catch (Exception e) {
            return inputBytes;
        }
    }
}
