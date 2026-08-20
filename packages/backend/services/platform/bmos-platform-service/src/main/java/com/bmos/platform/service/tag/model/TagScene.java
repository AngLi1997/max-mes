package com.bmos.platform.service.tag.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签场景
 * (脚本控制数据)
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/29 19:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bp_tag_scene")
public class TagScene extends BaseDO {

    /**
     * 标签场景名称
     */
    private String tagSceneName;

    /**
     * 标签场景描述
     */
    private String tagSceneDesc;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 标签类型id
     */
    private Long tagTypeId;

    /**
     * 数据源服务名称
     */
    private String dataSourceServiceName;

    /**
     * 数据源接口地址
     */
    private String dataSourceInterface;

    /**
     * 二维码字段（标签显示的二维码信息， 来源与数据源接口）
     */
    private String qrCodeField;

    @JsonIgnore
    public String getQrCodePrefix() {
        if (this.tagTypeId == null){
            return null;
        }
        return String.format("%02d",tagTypeId);
    }
}
