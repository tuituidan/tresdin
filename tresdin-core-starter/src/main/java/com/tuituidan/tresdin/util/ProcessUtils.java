package com.tuituidan.tresdin.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * ProcessUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2026/1/31
 */
@UtilityClass
public class ProcessUtils {

    /**
     * 执行命令
     *
     * @param directory directory
     * @param command command
     */
    public static void process(File directory, String... command) {
        try {
            Process process = new ProcessBuilder(command).directory(directory)
                    .redirectErrorStream(true).start();
            int exitCode = process.waitFor();
            Assert.isTrue(exitCode == 0,
                    IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8));
        } catch (InterruptedException | IOException ex) {
            Thread.currentThread().interrupt();
            throw new UnsupportedOperationException("执行命令【"
                    + StringUtils.join(command, " ") + "】失败", ex);
        }
    }

    /**
     * 执行命令
     *
     * @param directory directory
     * @param command command
     * @return String
     */
    public static String processStr(File directory, String... command) {
        try {
            Process process = new ProcessBuilder(command).directory(directory)
                    .redirectErrorStream(true).start();
            return IOUtils.toString(process.getInputStream(), "GBK");
        } catch (IOException ex) {
            throw new UnsupportedOperationException("执行命令【"
                    + StringUtils.join(command, " ") + "】失败", ex);
        }
    }

}
