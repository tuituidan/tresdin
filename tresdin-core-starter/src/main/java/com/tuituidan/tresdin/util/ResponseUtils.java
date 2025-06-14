package com.tuituidan.tresdin.util;

import com.tuituidan.tresdin.exception.DownloadFailException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.client.ResourceAccessException;
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
     * 文件预览.
     *
     * @param fileName fileName
     * @param inputStream inputStream
     */
    public static void preview(String fileName, InputStream inputStream) {
        try (InputStream in = inputStream;
                OutputStream outputStream = getHttpResponse("inline", fileName).getOutputStream()) {
            IOUtils.copy(in, outputStream);
        } catch (IOException ex) {
            throw new ResourceAccessException("预览失败", ex);
        }
    }

    /**
     * 文件下载.
     *
     * @param fileName fileName
     * @param inputStream inputStream
     */
    public static void download(String fileName, InputStream inputStream) {
        try (InputStream in = inputStream;
                OutputStream outputStream = getHttpResponse("attachment", fileName).getOutputStream()) {
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
        try (ZipOutputStream zipOut = new ZipOutputStream(getHttpResponse("attachment", fileName).getOutputStream())) {
            for (Map.Entry<String, InputStream> entry : files.entrySet()) {
                zipOut.putNextEntry(new ZipEntry(entry.getKey()));
                try (InputStream in = entry.getValue()) {
                    IOUtils.copy(in, zipOut);
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
    public static HttpServletResponse getHttpResponse(String type, String fileName) {
        HttpServletResponse response = getHttpResponse();
        response.setContentType(MediaTypeFactory.getMediaType(fileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM).toString());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        fileName = fileName.replaceAll("\\s*", "");
        fileName = StringExtUtils.urlEncode(fileName);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, type + ";filename*=utf-8''" + fileName);
        return response;
    }

    /**
     * 得到reponse.
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpResponse() {
        ServletRequestAttributes attrs = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        return Optional.ofNullable(attrs)
                .map(ServletRequestAttributes::getResponse)
                .orElseThrow(() -> new UnsupportedOperationException("请在web上下文中获取HttpServletResponse"));
    }

}
