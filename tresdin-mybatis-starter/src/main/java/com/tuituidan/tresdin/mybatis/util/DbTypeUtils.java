package com.tuituidan.tresdin.mybatis.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.ClassUtils;

/**
 * DataBaseTypeUtils.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2022/10/12
 */
@UtilityClass
public class DbTypeUtils {

    private static final String PG_OBJECT_CLASS = "org.postgresql.util.PGobject";

    private static final String KB_OBJECT_CLASS = "com.kingbase8.util.KBobject";

    private static final boolean IS_PG = hasClass(PG_OBJECT_CLASS);

    private static final boolean IS_KB = hasClass(KB_OBJECT_CLASS);

    /**
     * isPostgresql
     *
     * @return boolean
     */
    public static boolean isPostgresql() {
        return IS_PG;
    }

    /**
     * isPostgresql
     *
     * @return boolean
     */
    public static boolean isKingbase() {
        return IS_KB;
    }

    private static boolean hasClass(String clsName) {
        try {
            ClassUtils.forName(clsName, DbTypeUtils.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
    }

}
