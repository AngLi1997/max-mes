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

@FeignClient(name = "bmos-mes-service", contextId = "bmos-mes-message")
@ServiceMessageAnnotation("MES")
public interface MesMessageFeignClient extends IMessageFeignClient {

    /**
     * TODO 消息通知列表
     */
    @PostMapping("/api/mes/notice/list")
    ResponseInfo<List<SystemNoticeVO>> getNoticeList(@RequestBody NoticePageDTO req, @RequestHeader("language") String language);

    @GetMapping("/api/app/mes/plan/info/wait/task/count")
    ResponseInfo<List<MessageVO>> waitTaskCount();

    @PostMapping("/api/app/mes/user/actived")
    ResponseInfo<RsaVO> actived();

    /**
     * TODO 未读预警通知数量
     */
    @GetMapping("/api/mes/notice/warning/unread")
    ResponseInfo<Long> unreadWarningCount();

    /**
     * TODO 消息标记已读
     */
    @PostMapping("/api/mes/notice/read/{noticeId}")
    ResponseInfo<Boolean> insertReadNotice(@PathVariable("noticeId") Long noticeId);

    /**
     * TODO 标记所有消息已读
     */
    @PostMapping("/api/mes/notice/all-read/{type}")
    ResponseInfo<Boolean> readAll(@PathVariable("type") Integer type);
}
