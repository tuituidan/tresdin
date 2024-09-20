package com.tuituidan.tresdin.util;

import com.tuituidan.tresdin.consts.Separator;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.slf4j.helpers.MessageFormatter;

/**
 * StringExtUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/8
 */
@UtilityClass
public class StringExtUtils {

    /**
     * 得到uuid, 默认小写.
     *
     * @return String
     */
    public static String getUuid() {
        return getUuid(false);
    }

    /**
     * 得到uuid.
     *
     * @param upperCase 是否大写
     * @return String
     */
    public static String getUuid(boolean upperCase) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return upperCase ? uuid.toUpperCase() : uuid;
    }

    /**
     * 使用 Slf4j 中的字符串格式化方式来格式化字符串.
     *
     * @param pattern 待格式化的字符串
     * @param args 参数
     * @return 格式化后的字符串
     */
    public static String format(String pattern, Object... args) {
        return pattern == null ? "" : MessageFormatter.arrayFormat(pattern, args).getMessage();
    }

    /**
     * urlEncode.
     *
     * @param source source
     * @return String
     */
    public static String urlEncode(String source) {
        try {
            return URLEncoder.encode(source, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalArgumentException(var2);
        }
    }

    /**
     * urlDecode.
     *
     * @param source source
     * @return String
     */
    public static String urlDecode(String source) {
        try {
            return URLDecoder.decode(source, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalArgumentException(var2);
        }
    }

    /**
     * 模版替换.
     *
     * @param source source
     * @param params params
     * @return String
     */
    public static String template(String source, Map<String, Object> params) {
        if (source.contains(Separator.POUND)) {
            return ExpParserUtils.template(source, params);
        }
        for (Entry<String, Object> entry : params.entrySet()) {
            source = source.replace("{" + entry.getKey() + "}", Objects.toString(entry.getValue()));
        }
        return source;
    }

}
