package com.tuituidan.tresdin.dict.bean;

import com.tuituidan.tresdin.dictionary.bean.IDictInfo;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * DictInfo.
 *
 * @author tuituidan
 * @version 1.0
 * @date 2021/9/23
 */
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "T_DICT_INFO", schema = "DB_SYS")
public class DictInfo implements IDictInfo, Serializable {

    private static final long serialVersionUID = -5607490582181271949L;

    @Id
    @Column(name = "C_ID", length = 100)
    private String id;

    @Column(name = "C_PID", length = 100)
    private String pid;

    @Column(name = "C_NAME", length = 300)
    private String name;

    @Column(name = "C_LEVEL", length = 100)
    private String level;

    @Column(name = "N_ORDER")
    private Integer order;

    @Column(name = "C_VALID", length = 100)
    private String valid;

    @Column(name = "DT_CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "DT_UPDATE_TIME")
    private LocalDateTime updateTime;
}

