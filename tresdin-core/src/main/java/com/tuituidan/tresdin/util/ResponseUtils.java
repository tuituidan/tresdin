package com.tuituidan.tresdin.util;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

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
     * 获取下载头信息.
     *
     * @param fileName fileName
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpResponse(String fileName) {
        HttpServletResponse response = RequestUtils.getResponse();
        Assert.notNull(response, "请在web上下文中使用");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        fileName = fileName.replaceAll("\\s*", "");
        fileName = StringExtUtils.urlEncode(fileName);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        return response;
    }
}
