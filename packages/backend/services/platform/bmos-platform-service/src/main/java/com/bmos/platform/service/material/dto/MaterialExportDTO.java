package com.bmos.platform.service.material.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName MaterialExportDTO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/12/25 13:43
 */
@Setter
@Getter
@ToString
public class MaterialExportDTO extends BasePage {

    @ApiModelProperty("物料分类id")
    private Long materialCategoryId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("是否导出全部:true-是,false-否")
    @NotNull
    private Boolean allFlay;

}
