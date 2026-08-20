package com.bmos.wms.service.inspect.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * WMS 请验单字段值（mirror MES bm_inspect_info）
 */
@TableName("bw_inspect_info")
@Getter
@Setter
public class InspectInfo extends BaseDO {

    /** 请验单主键id（bw_inspect.id） */
    private Long inspectId;
    /** 请验单配置数据id（LIMS document_config_field 主键，由 BmosLimsGateway 转发到 LIMS） */
    private Long inspectConfigDataId;
    /** 字段 code（字典 value 或前端约定的内置 code） */
    private String code;
    /** 展示名称 */
    private String showName;
    /** 数据名称 */
    private String dataName;
    /** 是否必填 */
    private Boolean required;
    /** 所填的值 */
    private String value;
    /** 排序 */
    private Integer sort;
}
