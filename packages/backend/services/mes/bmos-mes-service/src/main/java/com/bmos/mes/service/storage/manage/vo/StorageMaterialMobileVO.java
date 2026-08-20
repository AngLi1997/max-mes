package com.bmos.mes.service.storage.manage.vo;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 暂存物料件信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 11:58
 */
@Data
@ApiModel("暂存物料件信息")
public class StorageMaterialMobileVO {

    /**
     * 暂存物料件id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialCode;

    /**
     * 合并编码
     */
    @ApiModelProperty("合并编码")
    private String mergeCode;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    private String materialBatchNo;

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long materialBatchId;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "000000002")
    private String materialNo;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 扩展单位id
     */
    @ApiModelProperty(value = "扩展单位id", example = "1")
    private Long unitExtendId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 单位转换率
     */
    @ApiModelProperty(value = "单位转换率", example = "1000")
    private String rate;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private String availableQuantity;

    /**
     * 预订量
     */
    @ApiModelProperty(value = "预订量", example = "1.000")
    @PrecisionValue
    private String reserveQuantity;

    /**
     * 消耗量
     */
    @ApiModelProperty(value = "消耗量", example = "1.000")
    @PrecisionValue
    private String consumeQuantity;

    /**
     * 初始量
     */
    @ApiModelProperty(value = "初始量", example = "1.000")
    @PrecisionValue
    private String initQuantity;

    /**
     * 物料量（可用+预定）
     */
    @ApiModelProperty(value = "物料量（可用+预定）", example = "1.000")
    @PrecisionValue
    private String quantity;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-02-06")
    private LocalDate expiredDate;

    /**
     * 暂存货位id
     */
    @ApiModelProperty(value = "暂存货位id", example = "1")
    private Long materialPositionId;

    /**
     * 暂存货位名称
     */
    @ApiModelProperty(value = "暂存货位名称", example = "货位名称")
    private String materialPositionName;

    /**
     * 暂存货位编码
     */
    @ApiModelProperty(value = "暂存货位编码", example = "暂存货位编码")
    private String materialPositionCode;

    /**
     * 容器
     */
    @ApiModelProperty(value = "容器", example = "不锈钢桶-F1-0231")
    private String container;

    /**
     * 容器编码
     */
    @ApiModelProperty(value = "容器编码", example = "01")
    private String containerNo;

    /**
     * 预定生产批次id
     */
    @ApiModelProperty(value = "预定生产批次id", example = "1")
    private Long batchId;

    /**
     * 预定生产批次号
     */
    @ApiModelProperty(value = "预定生产批次号", example = "WH050101")
    private String batchNo;

    /**
     * 预定产品id
     */
    @ApiModelProperty(value = "预定产品id", example = "1")
    private Long productId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "预定产品名称", example = "产品")
    private String productName;

    /**
     * 产品合并编码
     */
    @ApiModelProperty(value = "预定产品合并编码", example = "WH03")
    private String productMergeCode;

    /**
     * 预定工艺id
     */
    @ApiModelProperty(value = "预定工艺id", example = "1")
    private Long processId;

    /**
     * 工艺名称
     */
    @ApiModelProperty(value = "预定工艺名称", example = "工艺")
    private String processName;

    /**
     * 预定人姓名
     */
    @ApiModelProperty(value = "预定人姓名", example = "张三")
    private String reserveUserName;

    /**
     * 预定时间
     */
    @ApiModelProperty(value = "预定时间", example = "2024-02-06 12:00:00")
    private LocalDateTime reserveTime;

    /**
     * 原始编码
     */
    @ApiModelProperty(value = "原始编码", example = "WH050101")
    private String originalCode;

    /**
     * 原厂批号 来源为领料接收（仓库来的信息）
     */
    @ApiModelProperty(value = "原厂批号", example = "2310001")
    private String factoryBatchNo;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "供应商")
    private String supplier;

    /**
     * 生产商
     */
    @ApiModelProperty(value = "生产商", example = "生产商")
    private String producer;

    /**
     * 最终暴露的单位(有扩展单位优先显示扩展单位 否则显示标准单位)
     *
     * @return
     */
    @PrecisionUnitId
    public Long finalUnitId;

    public Long getFinalUnitId() {
        return unitExtendId == null ? unitId : unitExtendId;
    }

    /**
     * 单位是否为扩展单位
     *
     * @return
     */
    @JsonIgnore
    public boolean unitIsExtend() {
        return unitExtendId != null;
    }

    /**
     * 皮重(带单位)
     */
    @ApiModelProperty(value = "皮重", example = "15.780kg")
    private BigDecimal tareWeight;

    /**
     * 毛重(带单位)
     */
    @ApiModelProperty(value = "毛重", example = "15.780kg")
    private BigDecimal grossWeight;

    /**
     * 净重(带单位)
     */
    @ApiModelProperty(value = "净重", example = "15.780kg")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "物料自定义字段")
    private List<MaterialBatchFieldVO> materialCustomFields = new ArrayList<>();

    @ApiModelProperty(value = "物料件自定义字段")
    private List<MaterialBatchFieldVO> materialPieceCustomFields = new ArrayList<>();

    @ApiModelProperty(value = "物料批次自定义字段")
    private List<MaterialBatchFieldVO> materialBatchCustomFields = new ArrayList<>();

    @ApiModelEnumProperty(value = "物料类别", enumClass = CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum categoryType;

    /**
     * 物料批次质量状态
     */
    private MaterialQualityStatusEnum qualityStatus;

    @ApiModelProperty(value = "是否可用")
    public Boolean isAvailable;
}
