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
 * 血源服务FeignClient
 *
 * @author <a href="mailto:shujinjing@bmos.net.cn">Mojo</a>
 */
@FeignClient(name = "bmos-plasma-service", contextId = "bmos-plasma-message")
@ServiceMessageAnnotation("BSMS")
public interface PlasmaMessageFeignClient extends IMessageFeignClient {

    /**
     * 消息通知列表
     */
    @PostMapping("/api/bmos-plasma/notice/list")
    ResponseInfo<List<SystemNoticeVO>> getNoticeList(@RequestBody NoticePageDTO req, @RequestHeader("language") String language);

    /**
     * 获取待办任务列表
     */
    @GetMapping("/api/bmos-plasma/audit/tasks")
    ResponseInfo<List<MessageVO>> waitTaskCount();

    /**
     * 血源系统激活状态
     */
    @PostMapping("/api/bmos-plasma/license/activated")
    ResponseInfo<RsaVO> actived();


    /**
     * TODO 未读预警通知数量
     */
    @GetMapping("/api/bmos-plasma/notice/warning/unread")
    ResponseInfo<Long> unreadWarningCount();

    /**
     * TODO 消息标记已读
     */
    @PostMapping("/api/bmos-plasma/notice/read/{noticeId}")
    ResponseInfo<Boolean> insertReadNotice(@PathVariable("noticeId") Long noticeId);

    /**
     * TODO 标记所有消息已读
     */
    @PostMapping("/api/bmos-plasma/notice/all-read/{type}")
    ResponseInfo<Boolean> readAll(@PathVariable("type") Integer type);
}
