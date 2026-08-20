package com.bmos.platform.service.system.message.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.system.message.dto.NoticePageDTO;
import com.bmos.platform.service.system.message.service.MessageService;
import com.bmos.platform.service.system.message.vo.MessageVO;
import com.bmos.platform.service.system.message.vo.SystemNoticeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/message")
@Api(tags = "消息接口")
@Validated
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "消息通知分页")
    @PostMapping("/page")
    public ResponseInfo<CommonPage<SystemNoticeVO>> getNoticePage(@RequestBody NoticePageDTO req, HttpServletRequest request) {
        CommonPage<SystemNoticeVO> noticePage = messageService.getNoticePage(req, request);
        return ResponseInfo.success(noticePage);
    }

    @ApiOperation("待办任务数量")
    @GetMapping("/wait/task/count")
    public ResponseInfo<List<MessageVO>> waitTaskCount() {
        return ResponseInfo.success(messageService.waitTaskCount());
    }

    @ApiOperation("未读预警通知数量")
    @GetMapping("/unread/warning/count")
    public ResponseInfo<Long> unreadWarningCount() {
        return ResponseInfo.success(messageService.unreadWarningCount());
    }

    /**
     * 消息标记已读
     *
     * @param serviceName 指定服务
     * @param noticeId    指定某条消息标记已读
     */
    @ApiOperation("消息标记已读")
    @PostMapping("/read")
    public ResponseInfo<Boolean> read(@RequestParam String serviceName, @RequestParam Long noticeId) {
        return ResponseInfo.success(messageService.read(serviceName, noticeId));
    }

    @ApiOperation("预警消息全部已读")
    @PostMapping("/read/all")
    public ResponseInfo<Boolean> readWarningAll() {
        return ResponseInfo.success(messageService.readWarningAll());
    }
}
