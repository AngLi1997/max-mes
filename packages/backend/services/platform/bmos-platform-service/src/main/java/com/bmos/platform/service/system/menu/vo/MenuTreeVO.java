package com.bmos.platform.service.system.menu.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.common.util.i18n.I18nUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiModel("菜单树全量VO")
@Getter
@Setter
@ToString
public class MenuTreeVO implements TreeNode<MenuTreeVO, Long, Long> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("父级菜单名称")
    private String parentName;

    @ApiModelProperty("是否是菜单")
    private Integer isMenu;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("子数据")
    private List<MenuTreeVO> children;

    @ApiModelProperty("是否外部链接")
    private Integer isOutside;
    @ApiModelProperty("外部链接URL")
    private String outsideUrl;
    @ApiModelProperty("别名")
    private String alias;

    @ApiModelProperty("图标")
    private String icon;

    @Override
    public Long sort() {
        return sort;
    }

}
