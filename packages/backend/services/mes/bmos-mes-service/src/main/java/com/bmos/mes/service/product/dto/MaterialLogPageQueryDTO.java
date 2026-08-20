package com.bmos.mes.service.product.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mybatis.page.BasePage;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@ApiModel("物料日志分页查询DTO")
@Getter
@Setter
public class MaterialLogPageQueryDTO extends BasePage {

    @ApiModelEnumProperty(value = "物料信息类型",required = true,enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    private Integer categoryType;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    @ApiModelProperty("物料件id")
    private Long storageMaterialId;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 操作类型
     * {@link com.bmos.mes.common.enums.material.MaterialOperationTypeShowEnum}
     */
    @ApiModelProperty(value = "操作类型(INBOUND 入库 OUTBOUND 出库 CHECK 盘点 RESERVE 预定 CANCEL_RESERVE 取消预定 WEIGH 称量 ADD 新增 CHARGE 投料 RECYCLE 回收)", example = "INBOUND")
    private String operationType;

    private LocalDateTime startTimeDate;

    private LocalDateTime endTimeDate;

    public void convert2Date(){
        setStartTimeDate(LocalDateTimeUtil.parse(startTime + " 00:00:00", TimeUtil.F_DATETIME));
        setEndTimeDate(LocalDateTimeUtil.parse(endTime + " 23:59:59", TimeUtil.F_DATETIME));
    }



}
