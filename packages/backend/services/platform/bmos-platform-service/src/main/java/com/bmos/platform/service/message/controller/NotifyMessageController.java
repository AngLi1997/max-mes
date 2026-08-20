package com.bmos.platform.service.message.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.message.controller.vo.MessagePageRequestVO;
import com.bmos.platform.service.message.controller.vo.MessageReadVO;
import com.bmos.platform.service.message.dto.MessageInfoDTO;
import com.bmos.platform.service.message.persistence.IMessagePersistence;
import com.bmos.platform.service.message.service.INotifyMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @className: MessageController
 * @author: yigaohui
 * @date: 2025/1/8 17:35
 * @Version: 1.0
 * @description:
 */

@RestController
@Api(tags = {"消息接口"})
@RequestMapping("/notifyMessage")
public class NotifyMessageController {

    @Autowired
    private IMessagePersistence messagePersistence;

    @Autowired
    private INotifyMessageService messageService;

    /**
     * 分页当前用户未读消息
     */
    @ApiOperation("查询当前用户的消息")
    @PostMapping("/page")
    public ResponseInfo<CommonPage<MessageInfoDTO>> getMessagePage(@RequestBody MessagePageRequestVO messagePageRequestVO) {
        CommonPage<MessageInfoDTO> commonPage = messagePersistence.selectMessagePage(SysUserHolder.getUser().getUserId(),
                messagePageRequestVO.getMessageType(), messagePageRequestVO.getMessageStatus(), BeanUtil.copyProperties(messagePageRequestVO, BasePage.class));
        return ResponseInfo.success(commonPage);
    }

    @ApiOperation("消息已读")
    @PostMapping("/read")
    public ResponseInfo<Void> readMessage(@RequestBody MessageReadVO messageReadVO) {
        messageService.read(messageReadVO.getIds(), messageReadVO.isAll(),messageReadVO.getMessageType());
        return ResponseInfo.success();
    }
}
