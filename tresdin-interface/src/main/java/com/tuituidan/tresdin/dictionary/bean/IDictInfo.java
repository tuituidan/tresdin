package com.tuituidan.tresdin.dictionary.bean;

/**
 * IDictInfo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/9
 */
public interface IDictInfo {

    /**
     * getId.
     *
     * @return String
     */
    String getId();

    /**
     * getPid.
     *
     * @return String
     */
    String getPid();

    /**
     * getName.
     *
     * @return String
     */
    String getName();

    /**
     * getLevel.
     *
     * @return String
     */
    String getLevel();

    /**
     * getOrder.
     *
     * @return String
     */
    Integer getOrder();

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
     * @return IDictInfo
     */
    IDictInfo setId(String id);

    /**
     * setPid.
     *
     * @param pid pid
     * @return IDictInfo
     */
    IDictInfo setPid(String pid);

    /**
     * setName.
     *
     * @param name name
     * @return IDictInfo
     */
    IDictInfo setName(String name);

    /**
     * setLevel.
     *
     * @param level level
     * @return IDictInfo
     */
    IDictInfo setLevel(String level);

    /**
     * setOrder.
     *
     * @param order order
     * @return IDictInfo
     */
    IDictInfo setOrder(Integer order);

    /**
     * setValid.
     *
     * @param valid valid
     * @return IDictInfo
     */
    IDictInfo setValid(String valid);
}
