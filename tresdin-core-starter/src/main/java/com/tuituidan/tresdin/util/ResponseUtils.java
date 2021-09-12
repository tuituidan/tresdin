package com.tuituidan.tresdin.util;

import com.tuituidan.tresdin.exception.DownloadFailException;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ResponseUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
@UtilityClass
public class ResponseUtils {

    /**
     * 文件下载.
     *
     * @param fileName fileName
     * @param inputStream inputStream
     */
    public static void download(String fileName, InputStream inputStream) {
        try (InputStream in = inputStream;
                OutputStream outputStream = getHttpResponse(fileName).getOutputStream()) {
            IOUtils.copy(in, outputStream);
        } catch (Exception ex) {
            throw new DownloadFailException("下载失败", ex);
        }
    }

    /**
     * 批量下载.
     *
     * @param fileName fileName
     * @param files files
     */
    public static void batchDownload(String fileName, Map<String, InputStream> files) {
        try (OutputStream outputStream = getHttpResponse(fileName).getOutputStream();
                ZipOutputStream zipOut = new ZipOutputStream(outputStream);
                DataOutputStream data = new DataOutputStream(zipOut)) {
            for (Map.Entry<String, InputStream> entry : files.entrySet()) {
                zipOut.putNextEntry(new ZipEntry(entry.getKey()));
                try (InputStream in = entry.getValue()) {
                    IOUtils.copy(in, data);
                } finally {
                    zipOut.closeEntry();
                }
            }
        } catch (Exception ex) {
            throw new DownloadFailException("批量下载失败", ex);
        }
    }

    /**
     * 获取下载头信息.
     *
     * @param fileName fileName
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpResponse(String fileName) {
        HttpServletResponse response = getHttpResponse();
        Assert.notNull(response, "请在web上下文中使用");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        fileName = fileName.replaceAll("\\s*", "");
        fileName = StringExtUtils.urlEncode(fileName);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        return response;
    }

    /**
     * 得到reponse.
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpResponse() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return attrs == null ? null : attrs.getResponse();
    }

}
