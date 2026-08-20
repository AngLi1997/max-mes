package com.bmos.lims2.server.inspect.document.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单配置数据
 */
@Getter
@Setter
@TableName("lm_document_config_field")
public class DocumentConfigField extends BaseDO {

    /**
     * 请验单配置表id lm_inspection_config表主键
     */
    private Long configId;
    /**
     * 请验单展示数据名称
     */
    private String showName;
    /**
     * 请验单数据code 内置数据code前端定义，若是字典数据code则为字典的value
     */
    private String code;
    /**
     * 请验单数据名称
     */
    private String dataName;
    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 排序 同一个config_id下的请验单数据在前端的显示顺序
     */
    private Integer sort;

    /**
     * 字段来源
     */
    private String fieldSource;

}

