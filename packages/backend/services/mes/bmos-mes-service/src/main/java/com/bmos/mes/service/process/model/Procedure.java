package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 工序信息实体
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure")
public class Procedure {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 名称
     */
    private String name;

    @TableField(exist = false)
    /**
     * 历史基础数据id
     */
    private Long historyId;
}
