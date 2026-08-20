package com.bmos.lims2.server.inspect.document.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单配置
 */
@Getter
@Setter
@TableName("lm_document_config")
public class DocumentConfig extends BaseDO {

    /**
     * 名称
     */
    private String name;
    /**
     * 备注信息
     */
    private String remark;

    /**
     * 启停状态
     */
    private Boolean status;

}
