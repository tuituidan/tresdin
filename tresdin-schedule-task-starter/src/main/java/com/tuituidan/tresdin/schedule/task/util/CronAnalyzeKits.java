package com.tuituidan.tresdin.schedule.task.util;

import com.tuituidan.tresdin.util.StringExtUtils;
import lombok.experimental.UtilityClass;

/**
 * CronAnalyzeKits.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2024/9/15
 */
@UtilityClass
public class CronAnalyzeKits {

    private static final String XINHAO = "*";

    private static final String WENHAO = "?";

    private static final String DAO = "-";

    private static final String MEI = "/";

    private static final String HUO = ",";

    private static final String ONE = "1/1";

    private static final String TWO_ZERO = "0/0";

    private static final String ZERO = "0";

    private static final int CRON_SIZE = 6;

    private static final int MEI_SIZE = 2;

    private static final int MONTH_ADDR = 4;

    private static final int DAY_ADDR = 3;

    private static final String SPACE = " ";

    /**
     * 翻译cron.
     *
     * @param cronExp String
     * @return String
     */
    public static String analyzeCron(String cronExp) {
        String[] tmpCrons = cronExp.split(SPACE);
        if (tmpCrons.length != CRON_SIZE) {
            return "无法解析cron表达式";
        }

        StringBuilder result = new StringBuilder();
        //没有解析年和周
        // 解析月
        analyze(tmpCrons[MONTH_ADDR], result, "月");

        // 解析日
        analyze(tmpCrons[DAY_ADDR], result, "天");

        // 解析时
        analyze(tmpCrons[2], result, "小时");

        // 解析分
        analyze(tmpCrons[1], result, "分钟");

        // 解析秒
        analyze(tmpCrons[0], result, "秒");
        result.append("触发");
        return result.toString();
    }

    /**
     * analyze.
     *
     * @param s String
     * @param result StringBuilder
     * @param unit String
     */
    private static void analyze(String s, StringBuilder result, String unit) {
        if (ONE.equals(s)) {
            s = XINHAO;
        }
        if (TWO_ZERO.equals(s)) {
            s = ZERO;
        }
        if (XINHAO.equals(s)) {
            result.append("每").append(unit);
            return;
        }
        if (WENHAO.equals(s)) {
            return;
        }
        if (s.contains(HUO)) {
            huo(s, result, unit);
            return;
        }
        if (s.contains(DAO)) {
            dao(s, result, unit);
            return;
        }

        if (s.contains(MEI)) {
            mei(s, result, unit);
            return;
        }
        result.append("第").append(s).append(unit);
    }

    /**
     * 拼接 “每” 之类的字符串.
     *
     * @param s String
     * @param result StringBuilder
     * @param unit String
     */
    private static void mei(String s, StringBuilder result, String unit) {
        String[] arr = s.split(MEI);
        if (arr.length != MEI_SIZE) {
            throw new IllegalArgumentException(StringExtUtils.format("表达式{}错误", s));
        }

        if (arr[0].equals(arr[1]) || ZERO.equals(arr[0])) {
            result.append("每").append(arr[1]).append(unit);
        } else {
            result.append("每").append(arr[1]).append(unit)
                    .append("的第").append(arr[0]).append(unit);
        }
    }

    /**
     * 拼接 “到” 之类的字符串..
     *
     * @param s String
     * @param result StringBuilder
     * @param unit String
     */
    private static void dao(String s, StringBuilder result, String unit) {
        String[] arr = s.split(DAO);
        if (arr.length != MEI_SIZE) {
            throw new IllegalArgumentException(StringExtUtils.format("表达式{}错误", s));
        }

        result.append("从第").append(arr[0]).append(unit).append("到第")
                .append(arr[1]).append(unit).append("每").append(unit);
        result.append("的");
    }

    /**
     * 拼接 “和” 之类的字符串.
     *
     * @param s String
     * @param result StringBuilder
     * @param unit String
     */
    private static void huo(String s, StringBuilder result, String unit) {
        String[] arr = s.split(HUO);
        for (String value : arr) {
            if (value.length() != 0) {
                result.append("第").append(value).append(unit).append("和");
            }
        }

        result.deleteCharAt(result.length() - 1);
        result.append("的");
    }

}

