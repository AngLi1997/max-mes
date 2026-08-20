package com.bmos.mes.service.storage.log.dto;

import com.bmos.mes.common.enums.storage.StorageOperateTypeShowEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 货位分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("货位分页查询参数")
public class StorageMaterialPositionLogPageQuery extends BasePage {

    /**
     * 物料信息 名称/编码
     */
    @ApiModelProperty(value = "物料信息 名称/编码", example = "碳酸氢钠")
    @Length(max = 100)
    private String materialKeyWords;

    /**
     * 产品信息id
     */
    @ApiModelProperty(value = "产品信息id", example = "1")
    private Long productId;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "人血白蛋白")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码", example = "123")
    private String productCode;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    @Length(max = 100)
    private String materialBatchNo;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long materialPositionId;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String productBatchNo;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "000000002")
    private String materialNo;

    /**
     * 操作类型
     * {@link StorageOperateTypeShowEnum}
     */
    @ApiModelProperty(value = "操作类型(INBOUND：入库 OUTBOUND：出库 PLUS：盘增 MINUS：盘减)", example = "INBOUND")
    private String operationType;

    /**
     * 操作时间开始
     */
    @ApiModelProperty(value = "操作时间开始", example = "2024-04-10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 操作时间结束
     */
    @ApiModelProperty(value = "操作时间结束", example = "2024-04-10")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
