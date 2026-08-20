package com.bmos.wms.service.businessLog.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mybatis.page.BasePage;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.wms.common.enums.inventory.CargoInventoryOperateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

/**
 * 货品日志查询参数
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 18:24
 */
@Data
@ApiModel("货品日志查询参数")
@EqualsAndHashCode(callSuper = true)
public class CargoLogPageQuery extends BasePage {

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1772538333008891904")
    private Long cargoId;

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "WH030102231001")
    @Length(max = 100)
    private String inventoryBatchNo;

    /**
     * 货品件号
     */
    @ApiModelProperty(value = "货品件号", example = "001")
    @Length(max = 100)
    private String inventoryNo;

    /**
     * 操作类型
     */
    @ApiModelEnumProperty(value = "操作类型", enumClass = CargoInventoryOperateType.class)
    @EnumValidate(CargoInventoryOperateType.class)
    private Integer operateType;

    /**
     * 操作时间开始
     */
    @ApiModelProperty(value = "操作时间开始", example = "2024-04-10")
    private LocalDate startDate;

    /**
     * 操作时间结束
     */
    @ApiModelProperty(value = "操作时间结束", example = "2024-04-10")
    private LocalDate endDate;
}
