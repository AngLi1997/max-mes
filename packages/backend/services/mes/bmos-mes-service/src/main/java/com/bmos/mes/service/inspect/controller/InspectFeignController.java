package com.bmos.mes.service.inspect.controller;

import com.alibaba.fastjson.JSON;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.inspect.dto.InspectRejectDTO;
import com.bmos.mes.inspect.dto.InspectResultCallBackDTO;
import com.bmos.mes.inspect.feign.InspectFeign;
import com.bmos.mes.service.inspect.service.InspectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feign/inspect")
@Slf4j
public class InspectFeignController implements InspectFeign {

    @Autowired
    InspectService inspectService;

    @Override
    @PostMapping("/callback")
    public ResponseInfo<Void> inspectCallBack(@RequestBody InspectResultCallBackDTO inspectResultCallBackDTO) {
        log.info("收到检验结果回传{}", JSON.toJSONString(inspectResultCallBackDTO));
        inspectService.inspectCallback(inspectResultCallBackDTO);
        return ResponseInfo.success();
    }

    @Override
        @PostMapping("/reject")
    public ResponseInfo<Void> rejectInspect(@RequestBody List<InspectRejectDTO> dtoList) {
        inspectService.rejectInspect(dtoList);
        return ResponseInfo.success();
    }
}
