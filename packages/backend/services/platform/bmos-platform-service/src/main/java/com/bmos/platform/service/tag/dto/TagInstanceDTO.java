package com.bmos.platform.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * 标签实例参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 16:49
 */
@Data
@ApiModel("标签实例参数")
public class TagInstanceDTO {

    /**
     * 标签实例id
     */
    @ApiModelProperty(value = "标签实例id", example = "1")
    @NotNull(groups = {Update.class})
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", example = "原辅包标签", required = true)
    @Length(max = 100)
    @NotBlank
    private String tagName;

    /**
     * 标签类型id
     */
    @ApiModelProperty(value = "标签类型id", example = "1", required = true)
    @NotNull
    private Long tagTypeId;

    /**
     * 标签场景id
     */
    @ApiModelProperty(value = "标签场景id", example = "1", required = true)
    @NotNull
    private Long tagSceneId;

    /**
     * 标签定义id
     */
    @ApiModelProperty(value = "标签定义id", example = "1", required = true)
    @NotNull
    private Long tagDefineId;

    /**
     * 标签实例字段参数列表
     */
    @Valid
    @NotEmpty(message = "标签需至少包含一个标签字段")
    @ApiModelProperty(value = "标签实例字段参数列表", required = true)
    private List<TagInstanceField> fields;

    public interface Create extends Default {
    }

    public interface Update extends Default {
    }
}
