package com.bmos.lims2.web.inspect.pack.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 实验包新增请求参数
 */
@Getter
@Setter
@ApiModel("实验包新增请求参数VO")
public class PackageCreateReqVO {

    /**
     * 实验包编码
     */
    @ApiModelProperty(value = "实验包编码", required = true)
    @Length(max = 30)
    @NotBlank
    private String code;

    /**
     * 实验包名称
     */
    @ApiModelProperty(value = "实验包名称", required = true)
    @Length(max = 30)
    @NotBlank
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 100)
    private String remark;

    /**
     * 实验包下检验项目
     */
    @ApiModelProperty(value = "实验包下检验项目")
    private List<InspectPackageItemVO> packageItemList;

}
