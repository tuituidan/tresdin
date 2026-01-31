package com.tuituidan.tresdin.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * PropertiesUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/31
 */
@UtilityClass
public class PropertiesUtils {

    /**
     * 获取Properties
     *
     * @param path path
     * @return Properties
     */
    public static Properties getProperties(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return new Properties();
        }
        try {
            EncodedResource encodedResource = new EncodedResource(new FileSystemResource(file),
                    StandardCharsets.UTF_8);
            return PropertiesLoaderUtils.loadProperties(encodedResource);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Properties【" + path + "】加载失败", ex);
        }
    }

    /**
     * 保存Properties
     *
     * @param path path
     * @param properties properties
     */
    public static void saveProperties(String path, Properties properties) {
        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(path),
                StandardCharsets.UTF_8)) {
            properties.store(writer, "Properties");
        } catch (IOException ex) {
            throw new IllegalArgumentException("Properties【" + path + "】保存失败", ex);
        }
    }

}
