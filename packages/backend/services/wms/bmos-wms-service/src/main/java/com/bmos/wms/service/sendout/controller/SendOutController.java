package com.bmos.wms.service.sendout.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.sendout.dto.SendOutDTO;
import com.bmos.wms.service.sendout.dto.SendPageQuery;
import com.bmos.wms.service.sendout.dto.SendSubmitDTO;
import com.bmos.wms.service.sendout.service.ISendOutOrderService;
import com.bmos.wms.service.sendout.vo.SendOutOrderDetailVO;
import com.bmos.wms.service.sendout.vo.SendOutOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * wms发料接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 17:11
 */
@RestController
@RequestMapping("/sendOut")
@Api(tags = "wms发料接口")
public class SendOutController {

    @Resource
    private ISendOutOrderService sendOutOrderService;

    @PostMapping("/submit")
    @ApiOperation("提交发料工单")
    @OperationLog
    public ResponseInfo<Void> submitSendOutOrderByBatch(@Validated @RequestBody SendSubmitDTO dto) {
        sendOutOrderService.submitSendOutOrderByBatch(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/queryPage")
    @ApiOperation("查询仓库发料分页")
    public ResponseInfo<CommonPage<SendOutOrderVO>> queryPage(@Validated SendPageQuery pageQuery) {
        return ResponseInfo.success(sendOutOrderService.queryPage(pageQuery));
    }

    @PutMapping("/cancel")
    @ApiOperation("取消发料")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "发料工单id", required = true)
    public ResponseInfo<Void> cancelSendOut(@RequestParam Long id) {
        sendOutOrderService.cancelSendOut(id);
        return ResponseInfo.success();
    }

    @GetMapping("/queryDetail")
    @ApiOperation("根据发料工单id查询申请发料列表")
    @ApiImplicitParam(name = "id", value = "发料工单id", required = true)
    public ResponseInfo<SendOutOrderDetailVO> queryDetail(@RequestParam Long id) {
        return ResponseInfo.success(sendOutOrderService.queryDetail(id));
    }

    @PostMapping("/sendout")
    @ApiOperation("发料")
    @OperationLog
    public ResponseInfo<Void> sendOut(@Validated @RequestBody SendOutDTO dto) {
        sendOutOrderService.sendOut(dto);
        return ResponseInfo.success();
    }
}
