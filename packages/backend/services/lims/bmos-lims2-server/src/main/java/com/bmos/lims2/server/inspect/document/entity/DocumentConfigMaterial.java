package com.bmos.lims2.server.inspect.document.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 请验单配置检品绑定表
 */
@Getter
@Setter
@TableName("lm_document_material")
public class DocumentConfigMaterial {

    /**
     * 请验单配置id
     */
    private Long configId;

    /**
     * 检品id
     */
    private Long productId;

}

