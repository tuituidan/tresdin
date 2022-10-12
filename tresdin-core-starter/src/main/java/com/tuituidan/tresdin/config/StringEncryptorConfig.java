package com.tuituidan.tresdin.config;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import com.ulisesbocchio.jasyptspringboot.detector.DefaultPropertyDetector;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * StringEncryptorConfig.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/10
 */
@Configuration
public class StringEncryptorConfig {

    public static void main(String[] args) {
        StringEncryptor encryptor = new StringEncryptorConfig().stringEncryptor();
        String username = encryptor.encrypt("root");
        System.out.println(username);
    }

    /**
     * 设置加解密算法.
     *
     * @return StringEncryptor
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("pp57@dxd");
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }

    /**
     * 修改前后缀.
     *
     * @return EncryptablePropertyDetector
     */
    @Bean
    public EncryptablePropertyDetector encryptablePropertyDetector() {
        return new DefaultPropertyDetector("ENC#", "");
    }

}
