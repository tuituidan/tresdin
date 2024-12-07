package com.tuituidan.tresdin.mybatis.crypto.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * CryptoConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/11/16
 */
@Getter
@Setter
public class CryptoConfig {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 加解密钥
     */
    private String encryptKey;

    /**
     * 密文前缀
     */
    private String encryptPrefix;

    /**
     * getEncryptPrefix
     *
     * @return String
     */
    public String getEncryptPrefix() {
        if (StringUtils.isBlank(this.encryptPrefix)) {
            return "ENC#";
        }
        return this.encryptPrefix;
    }

}
