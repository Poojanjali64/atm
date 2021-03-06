package com.ppol.atm.integration.impl;

import com.ppol.atm.integration.api.model.Document;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * Converts JPG to PNG.
 */
@Component
public class JpgToPngTransformer {

    private static final String OUT_TYPE = "image/png";

    @Transformer
    public Message<Document> transform(final Message<Document> input) {

        try {
            final BufferedImage bufferedImage = ImageIO.read(
                new ByteArrayInputStream(input.getPayload().getContent())
            );
            final BufferedImage newBufferedImage = new BufferedImage(
                bufferedImage.getWidth(),
                bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );
            newBufferedImage.createGraphics().drawImage(
                bufferedImage, 0, 0, Color.WHITE, null
            );

            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(newBufferedImage, "png", out);

            return new GenericMessage<>(
                new Document(
                    input.getPayload().getName(),
                    out.toByteArray(),
                    JpgToPngTransformer.OUT_TYPE
                )
            );
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
