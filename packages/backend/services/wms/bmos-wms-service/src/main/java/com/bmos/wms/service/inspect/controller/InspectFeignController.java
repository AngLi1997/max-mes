package com.bmos.wms.service.inspect.controller;

import com.alibaba.fastjson.JSON;
import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.inspect.dto.InspectRejectDTO;
import com.bmos.wms.inspect.dto.InspectResultCallBackDTO;
import com.bmos.wms.inspect.feign.InspectFeign;
import com.bmos.wms.service.inspect.service.IInspectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * WMS 检验回传 feign 端点（被 LIMS 反向调用）。
 *
 * <p>结构镜像 MES InspectFeignController：仅做协议转接，业务逻辑全部走 IInspectService。
 */
@RestController
@RequestMapping("/feign/inspect")
@Slf4j
public class InspectFeignController implements InspectFeign {

    @Resource
    private IInspectService inspectService;

    @Override
    @PostMapping("/callback")
    public ResponseInfo<Void> inspectCallBack(@RequestBody InspectResultCallBackDTO dto) {
        log.info("WMS 收到 LIMS 检验结果回传：{}", JSON.toJSONString(dto));
        inspectService.inspectCallback(dto);
        return ResponseInfo.success();
    }

    @Override
    @PostMapping("/reject")
    public ResponseInfo<Void> rejectInspect(@RequestBody List<InspectRejectDTO> dtoList) {
        log.info("WMS 收到 LIMS 检验单退回：{}", JSON.toJSONString(dtoList));
        inspectService.rejectInspect(dtoList);
        return ResponseInfo.success();
    }
}
