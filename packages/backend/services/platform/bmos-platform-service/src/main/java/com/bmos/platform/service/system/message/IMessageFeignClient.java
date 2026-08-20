package com.bmos.platform.service.system.message;

import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.service.system.message.dto.NoticePageDTO;
import com.bmos.platform.service.system.message.vo.MessageVO;
import com.bmos.platform.service.system.message.vo.SystemNoticeVO;

import java.util.List;

/**
 * @className: 消息feign客户端接口
 * @author: yigaohui
 * @date: 2024/11/5 16:46
 * @Version: 1.0
 * @description:
 */

public interface IMessageFeignClient {

    /**
     * 消息通知分页
     */
    ResponseInfo<List<SystemNoticeVO>> getNoticeList(NoticePageDTO req, String language);

    /**
     * 获取待办数量
     */
    ResponseInfo<List<MessageVO>> waitTaskCount();

    /**
     * 获取激活状态
     */
    ResponseInfo<RsaVO> actived();

    /**
     * 获取未读预警消息数量
     */
    ResponseInfo<Long> unreadWarningCount();

    /**
     * 消息标记已读
     */
    ResponseInfo<Boolean> insertReadNotice(Long noticeId);

    /**
     * 标记所有消息已读
     */
    ResponseInfo<Boolean> readAll(Integer type);
}
