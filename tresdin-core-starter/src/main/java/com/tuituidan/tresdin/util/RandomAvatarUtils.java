package com.tuituidan.tresdin.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import javax.imageio.ImageIO;
import lombok.experimental.UtilityClass;

/**
 * RandomAvatarUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2025/6/3
 */
@UtilityClass
public class RandomAvatarUtils {

    private static final String BASE64_PREFIX = "data:image/png;base64,";

    /**
     * 生成头像的base64编码
     *
     * @param id id
     * @return String
     */
    public static String createBase64Avatar(String id) {
        int code = id.hashCode();
        if (code < 0) {
            return createBase64Avatar(Math.abs(code));
        }
        return createBase64Avatar(code);
    }

    /**
     * 生成头像的base64编码
     *
     * @param id id
     * @return String
     */
    public static String createBase64Avatar(int id) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedImage img = create(id);
            ImageIO.write(img, "png", outputStream);
            return BASE64_PREFIX + new String(Base64.getEncoder().encode(outputStream.toByteArray()));
        } catch (Exception ex) {
            throw new IllegalArgumentException("随机头像生成失败", ex);
        }
    }

    /**
     * createAvatar
     *
     * @param path path
     * @param id id
     */
    public static void createAvatar(String path, String id) {
        int code = id.hashCode();
        if (code < 0) {
            createAvatar(path, Math.abs(code));
            return;
        }
        createAvatar(path, code);
    }

    /**
     * createAvatar
     *
     * @param path path
     * @param id id
     */
    public static void createAvatar(String path, int id) {
        try {
            BufferedImage img = create(id);
            ImageIO.write(img, "png", new FileOutputStream(path));
        } catch (Exception ex) {
            throw new IllegalArgumentException("随机头像生成失败", ex);
        }
    }

    private static BufferedImage create(int id) throws NoSuchAlgorithmException {
        int width = 20;
        int height = 20;
        int grid = 5;
        int padding = width / 2;
        int size = width * grid + width;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.setColor(new Color(240, 240, 240));
        graphics2D.fillRect(0, 0, size, size);
        graphics2D.setColor(randomColor());
        char[] idchars = createIdent(id);
        int i = idchars.length;
        for (int x = 0; x < Math.ceil(grid / 2.0); x++) {
            for (int y = 0; y < grid; y++) {
                if (idchars[--i] < 53) {
                    graphics2D.fillRect((padding + x * width), (padding + y * width), width, height);
                    if (x < Math.floor(grid / 2.0)) {
                        graphics2D.fillRect((padding + ((grid - 1) - x) * width), (padding + y * width), width, height);
                    }
                }
            }
        }
        graphics2D.dispose();
        return img;
    }

    private static Color randomColor() throws NoSuchAlgorithmException {
        Random random = SecureRandom.getInstanceStrong();
        int fc = 80;
        int bc = 200;
        int r = fc + random.nextInt(Math.abs(bc - fc));
        int g = fc + random.nextInt(Math.abs(bc - fc));
        int b = fc + random.nextInt(Math.abs(bc - fc));
        return new Color(r, g, b);
    }

    private static char[] createIdent(int id) {
        BigInteger content = new BigInteger((id + "").getBytes());
        BigInteger bi = new BigInteger(id + "identicon" + id, 36);
        bi = bi.xor(content);
        return bi.toString(10).toCharArray();
    }

}
