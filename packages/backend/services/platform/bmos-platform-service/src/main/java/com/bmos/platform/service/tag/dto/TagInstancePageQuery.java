package com.bmos.platform.service.tag.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("标签实例分页查询条件")
public class TagInstancePageQuery extends BasePage {

    /**
     * 标签类型id
     */
    @ApiModelProperty(value = "标签类型id", example = "1")
    private Long tagTypeId;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", example = "原辅包物料接收打码")
    @Length(max = 100)
    private String tagName;

    /**
     * 业务场景
     */
    @ApiModelProperty(value = "业务场景", example = "配料称量")
    @Length(max = 100)
    private String tagSceneName;
}
