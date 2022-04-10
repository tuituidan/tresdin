package com.tuituidan.tresdin.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * DateUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/12
 */
@UtilityClass
public class DateExtUtils {

    /**
     * 根据出生日期计算年龄.
     *
     * @param birthDate birthDate
     * @return Integer
     */
    public static Integer calcAge(LocalDate birthDate) {
        if (null == birthDate) {
            return null;
        }
        LocalDate curDate = LocalDate.now();
        Assert.isTrue(curDate.compareTo(birthDate) >= 0, "出生日期不能大于当前日期");
        int age = curDate.getYear() - birthDate.getYear();
        if (curDate.getMonthValue() < birthDate.getMonthValue()
                || (curDate.getMonthValue() == birthDate.getMonthValue()
                && curDate.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }
        return age;
    }

    /**
     * 日期转换，支持各种乱起八糟的格式，如：
     * 2021年03月04日
     * 2021年3月4日
     * 2021.03.04
     * 2021.3.4
     * 2021-03-04
     * 2021-3-4
     * 2021/03/04
     * 2021/3/4
     *
     * @param source source
     * @return LocalDate
     */
    public static LocalDate parseLocalDate(String source) {
        Assert.hasText(source, "请传入要转换日期的字符串");
        String time = source.trim().replaceAll("\\D", "-");
        String[] split = Arrays.stream(time.split("-"))
                .filter(StringUtils::isNoneBlank).toArray(String[]::new);
        Assert.isTrue(split.length > 2, "日期格式错误：" + source);
        return LocalDate.of(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]));
    }

    /**
     * parseLocalDateTime
     *
     * @param source source
     * @return LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String source) {
        Assert.hasText(source, "请传入要转换时间的字符串");
        String time = source.trim().replaceAll("\\D", "-");
        String[] split = Arrays.stream(time.split("-"))
                .filter(StringUtils::isNoneBlank).toArray(String[]::new);
        Assert.isTrue(split.length > 5, "时间格式错误：" + source);
        return LocalDateTime.of(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                Integer.parseInt(split[4]),
                Integer.parseInt(split[5]));
    }

}
