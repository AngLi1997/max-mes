package com.bmos.platform.service.system.message.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知分页查询参数对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class NoticePageDTO extends BasePage {

    /**
     * 通知类型：0-审核信息，1-预警信息
     */
    @ApiModelProperty(value = "通知类型")
    private Integer noticeType;

    @ApiModelProperty(value = "是否已读")
    private Boolean readFlag;

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;

}