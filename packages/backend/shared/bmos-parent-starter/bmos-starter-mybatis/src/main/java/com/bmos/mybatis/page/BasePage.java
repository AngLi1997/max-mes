package com.bmos.mybatis.page;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;

/**
 * 基础分页类
 */
@Getter
@Setter
public class BasePage implements IPage {
    @ApiModelProperty(value = "页码，从 1 开始", required = true,example = "1")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页条数，最大值为 100", required = true, example = "10")
    @Max(value = 100, message = "每页条数最大值为 100")
    private Integer pageSize = 20;

    // 单字段排序
    @ApiModelProperty(value = "排序",example = "create_time")
    private String orderBy;

    // 单字段排序
    @ApiModelProperty(value = "排序",example = "asc")
    private String dir;

    // 自定义多字段排序
    @ApiModelProperty(value = "自定义多字段排序",example = "name asc,age desc")
    private String orderSql;

    public static int getStart(BasePage pageParam) {
        return (pageParam.getPageNum() - 1) * pageParam.getPageSize();
    }

    public String getOrderSql() {

        // 传了自定义排序 优先使用
        if (StrUtil.isNotBlank(orderSql)){
            return StrUtil.toUnderlineCase(orderSql);
        }

        // 根据单字段排序拼装
        if (StrUtil.isBlank(orderBy)) {
            return null;
        }
        if (StrUtil.isBlank(dir)) {
            orderSql = orderBy;
        } else {
            orderSql = orderBy + " " + dir;
        }
        return StrUtil.toUnderlineCase(orderSql);
    }
}
