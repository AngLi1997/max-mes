package com.bmos.wms.service.sendout.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 发料分页参数
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 17:11
 */
@Data
@ApiModel("发料分页参数")
@EqualsAndHashCode(callSuper = true)
public class SendPageQuery extends BasePage {

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "人血白蛋白")
    @Length(max = 100)
    private String productName;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "20230401")
    @Length(max = 100)
    private String batchNo;

    /**
     * 领料单号
     */
    @ApiModelProperty(value = "领料单号", example = "20230401")
    @Length(max = 100)
    private String pullOrderNo;
}
