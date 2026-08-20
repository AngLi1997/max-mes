package com.bmos.mes.service.operate.dto.version;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "操作规程版本管理列表查询dto")
public class VersionPageDTO extends BasePage {

    @ApiModelProperty("上级id")
    @NotNull
    private Long parentId;

}
