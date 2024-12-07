package com.tuituidan.tresdin.mybatis.crypto.util;

import com.tuituidan.tresdin.mybatis.crypto.exception.EncryptException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

/**
 * AesUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/11/18
 */
@Slf4j
@UtilityClass
public class AesUtils {

    private static final String KEY_ALGORITHM = "AES";

    private static final int KEY_LEN = 16;

    /**
     * AES 加密操作
     *
     * @param content 明文
     * @return 密文
     */
    public static String encrypt(String content, String secretKey) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        try {
            return Base64.getEncoder().encodeToString(getCipher(Cipher.ENCRYPT_MODE, secretKey)
                    .doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EncryptException("AES加密失败", e);
        }
    }

    /**
     * AES 解密操作
     *
     * @param content 密文
     * @return 明文
     */
    public static String decrypt(String content, String secretKey) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        try {
            return new String(getCipher(Cipher.DECRYPT_MODE, secretKey)
                    .doFinal(Base64.getDecoder().decode(content)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EncryptException("AES解密失败", e);
        }
    }

    private static Cipher getCipher(final int decryptMode, String secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher result = Cipher.getInstance(KEY_ALGORITHM);
        result.init(decryptMode, new SecretKeySpec(Arrays.copyOf(DigestUtils.md5Digest(
                secretKey.getBytes(StandardCharsets.UTF_8)), KEY_LEN), KEY_ALGORITHM));
        return result;
    }

}
