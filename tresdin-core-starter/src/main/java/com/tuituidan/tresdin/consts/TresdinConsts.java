package com.tuituidan.tresdin.consts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * TresdinConsts.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/10/5
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TresdinConsts {

    /**
     * API_V1
     */
    public static final String API_V1 = "/api/v1";

    /**
     * 与jar同级目录，jar如果放在系统根目录，比如docker环境
     * System.getProperty("user.dir")获取到的是"/"，直接去掉.
     */
    public static final String ROOT_DIR = "/".equals(System.getProperty("user.dir")) ? ""
            : System.getProperty("user.dir");

}
