package com.bmos.platform.service.system.message.feign;

import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.service.system.message.IMessageFeignClient;
import com.bmos.platform.service.system.message.annotation.ServiceMessageAnnotation;
import com.bmos.platform.service.system.message.dto.NoticePageDTO;
import com.bmos.platform.service.system.message.vo.MessageVO;
import com.bmos.platform.service.system.message.vo.SystemNoticeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 浆站集中化LIMS服务FeignClient
 *
 * @author <a href="mailto:shujinjing@bmos.net.cn">Mojo</a>
 */
@FeignClient(name = "bmos-centralization-lims-service", contextId = "bmos-centralization-lims-message")
@ServiceMessageAnnotation("LISMS")
public interface LimsMessageFeignClient extends IMessageFeignClient {

    /**
     * 消息通知列表
     */
    @PostMapping("/api/centralized-lims/notice/list")
    ResponseInfo<List<SystemNoticeVO>> getNoticeList(@RequestBody NoticePageDTO req, @RequestHeader("language") String language);

    /**
     * 获取待办任务列表
     */
    @GetMapping("/api/centralized-lims/audit/tasks")
    ResponseInfo<List<MessageVO>> waitTaskCount();

    /**
     * 集中化LIMS系统激活状态
     */
    @PostMapping("/api/centralized-lims/license/activated")
    ResponseInfo<RsaVO> actived();

    /**
     * 未读预警通知数量
     */
    @GetMapping("/api/centralized-lims/notice/warning/unread")
    ResponseInfo<Long> unreadWarningCount();

    /**
     * 消息标记已读
     */
    @PostMapping("/api/centralized-lims/notice/read/{noticeId}")
    ResponseInfo<Boolean> insertReadNotice(@PathVariable("noticeId") Long noticeId);

    /**
     * 标记所有消息已读
     */
    @PostMapping("/api/centralized-lims/notice/all-read/{type}")
    ResponseInfo<Boolean> readAll(@PathVariable("type") Integer type);
}
