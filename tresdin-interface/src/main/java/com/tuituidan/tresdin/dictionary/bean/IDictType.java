package com.tuituidan.tresdin.dictionary.bean;

/**
 * IDictType.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
public interface IDictType {

    /**
     * getId.
     *
     * @return String
     */
    String getId();

    /**
     * getName.
     *
     * @return String
     */
    String getName();

    /**
     * getSystem.
     *
     * @return String
     */
    String getSystem();

    /**
     * getValid.
     *
     * @return String
     */
    String getValid();

    /**
     * setId.
     *
     * @param id id
     * @return IDictType
     */
    IDictType setId(String id);

    /**
     * setName.
     *
     * @param name name
     * @return IDictType
     */
    IDictType setName(String name);

    /**
     * setSystem.
     *
     * @param system system
     * @return IDictType
     */
    IDictType setSystem(String system);

    /**
     * setValid.
     *
     * @param valid valid
     * @return IDictType
     */
    IDictType setValid(String valid);
}
