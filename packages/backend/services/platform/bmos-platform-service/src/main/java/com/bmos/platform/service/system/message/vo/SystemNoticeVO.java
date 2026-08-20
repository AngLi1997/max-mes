package com.bmos.platform.service.system.message.vo;

import com.bmos.platform.service.system.message.enums.NoticeTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统通知
 * </p>
 *
 * @author zht
 * @since 2024-12-17
 */
@Data
public class SystemNoticeVO {

    /**
     * 通知记录ID
     */
    private Long id;

    /**
     * 通知类型：0-审核通知，1-预警通知
     */
    private NoticeTypeEnum type;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容详情
     */
    private String content;

    /**
     * 资源标识符ID，平台目录表 system_menu ID
     */
    private String identifierId;

    /**
     * 发送人/创建人id
     */
    private String sender;

    /**
     * 是否已读：false-未读，true-已读
     */
    private Boolean readFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 服务名
     */
    private String serviceName;

}
