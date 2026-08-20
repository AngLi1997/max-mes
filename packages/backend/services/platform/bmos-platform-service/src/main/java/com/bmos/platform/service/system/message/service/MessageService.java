package com.bmos.platform.service.system.message.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.system.message.dto.NoticePageDTO;
import com.bmos.platform.service.system.message.vo.MessageVO;
import com.bmos.platform.service.system.message.vo.SystemNoticeVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MessageService {

    /**
     * 消息通知分页
     */
    CommonPage<SystemNoticeVO> getNoticePage(NoticePageDTO req, HttpServletRequest request);

    /**
     * 待办任务数量
     */
    List<MessageVO> waitTaskCount();

    /**
     * 未读预警通知数量
     */
    Long unreadWarningCount();

    /**
     * 消息标记已读
     */
    Boolean read(String serviceName, Long noticeId);

    /**
     * 消息标记全部已读
     */
    Boolean readWarningAll();

}
