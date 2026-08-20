package com.bmos.mes.service.dataset.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.dataset.dto.DatasetPointCreateDTO;
import com.bmos.mes.service.dataset.enums.DatasetDynamicReportDataType;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.*;

/**
 * 数据点
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 14:29
 */
@TableName("bm_dataset_point")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatasetPoint extends BaseDO {

    /**
     * 数据集id
     */
    private Long datasetId;

    /**
     * 数据集key
     */
    private String datasetKey;

    /**
     * 数据点名称
     */
    private String name;

    /**
     * 数据点key(暂时用流水号)
     */
    private String datasetPointKey;

    /**
     * 数据点类型 POINT 批记录数据(数据点) LOT_RELEASE_LINK 批签发引用 DYNAMIC_REPORT 动态数据填报
     * {@link DatasetType}
     */
    private DatasetType type;

    // 批记录数据字段

    /**
     * 工步id
     */
    private Long procedureStepId;

    /**
     * 字段id
     */
    private Long fieldId;

    /**
     * 前端扩展字段(json)
     */
    private String extra;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件关联表格最大下标值
     */
    private Long componentNumber;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录项名称
     */
    private String recordItemName;

    // 批签发引用

    /**
     * 批签发模板id
     */
    private Long lotReleaseTemplateId;

    /**
     * 批签发版本
     */
    private String lotReleaseVersion;

    /**
     * 批签发引用参数范围(P15:S19)
     */
    private String linkArea;

    /**
     * 批签发引用模版
     */
    private String templateUrl;

    // 动态填报

    /**
     * 动态填报数据类型
     */
    private DatasetDynamicReportDataType dynamicDataType;

    /**
     * 动态填报默认值
     */
    private String defaultValue;


    public DatasetPoint(Long datasetId, String datasetKey, DatasetType type, String datasetPointKey) {
        this.datasetId = datasetId;
        this.datasetKey = datasetKey;
        this.type = type;
        this.datasetPointKey = datasetPointKey;
    }


    /**
     * 构造批记录数据点
     *
     * @param datasetId       数据集id
     * @param datasetKey      数据集索引
     * @param name            数据点名称
     * @param datasetPointKey 数据点索引
     * @param dto             参数
     * @return
     */
    public static DatasetPoint buildRecord(Long datasetId, String datasetKey, String name, String datasetPointKey, DatasetPointCreateDTO dto) {
        return DatasetPoint.builder()
                .datasetId(datasetId)
                .datasetKey(datasetKey)
                .name(name)
                .datasetPointKey(datasetPointKey)
                .type(DatasetType.POINT)
                .procedureStepId(dto.getProcedureStepId())
                .fieldId(dto.getFieldId())
                .extra(dto.getExtra())
                .componentId(dto.getComponentId())
                .componentName(dto.getComponentName())
                .componentNumber(dto.getComponentNumber())
                .recordItemId(dto.getRecordItemId())
                .recordItemName(dto.getRecordItemName())
                .build();
    }

    /**
     * 构造动态填报数据点
     *
     * @param datasetId       数据集id
     * @param datasetKey      数据集索引
     * @param name            数据点名称
     * @param datasetPointKey 数据点索引
     * @param dataType        填报数据类型
     * @param defaultValue    填报数据默认值
     * @return
     */
    public static DatasetPoint buildDynamicReportRecord(Long datasetId, String datasetKey, String name, String datasetPointKey, DatasetDynamicReportDataType dataType, String defaultValue) {
        return DatasetPoint.builder()
                .datasetId(datasetId)
                .datasetKey(datasetKey)
                .name(name)
                .datasetPointKey(datasetPointKey)
                .type(DatasetType.DYNAMIC_REPORT)
                .dynamicDataType(dataType)
                .defaultValue(defaultValue)
                .build();
    }

    /**
     * 构造批签发引用数据点
     *
     * @param datasetId            数据集id
     * @param datasetKey           数据集索引
     * @param name                 数据点名称
     * @param datasetPointKey      数据点索引
     * @param lotReleaseTemplateId 批签发引用模板id
     * @param lotReleaseVersion    批签发模板版本
     * @param linkArea             批签发引用参数范围
     * @return
     */
    public static DatasetPoint buildLotReleaseLink(Long datasetId, String datasetKey, String name, String datasetPointKey, Long lotReleaseTemplateId, String lotReleaseVersion, String linkArea) {
        return DatasetPoint.builder()
                .datasetId(datasetId)
                .datasetKey(datasetKey)
                .name(name)
                .datasetPointKey(datasetPointKey)
                .type(DatasetType.LOT_RELEASE_LINK)
                .lotReleaseTemplateId(lotReleaseTemplateId)
                .lotReleaseVersion(lotReleaseVersion)
                .linkArea(linkArea)
                .build();
    }

}
