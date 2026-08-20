package com.bmos.mes.service.platform.plan;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.plan.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.ConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.plan.vo.BatchNextCodeVO;
import com.bmos.mes.service.platform.plan.vo.CodeRuleVO;
import com.bmos.mes.service.platform.plan.vo.NextCodeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-productplan-coderule")
public interface PlatformCodeRuleClient {
    /**
     * 获取下一个编号 未确认使用的不包含
     *
     * @param dto dto
     * @return ResponseInfo<NextCodeVO>
     */
    @PostMapping("/api/app/platform/codeRule/getNextNo")
    ResponseInfo<NextCodeVO> getNextNo(@RequestBody NextUseCodeDTO dto);

    /**
     * 获取下一个编号 未确认使用的编号会重复返回
     *
     * @param dto dto
     * @return ResponseInfo<NextCodeVO>
     */
    @PostMapping("/api/app/platform/codeRule/getNextUseNo")
    ResponseInfo<NextCodeVO> getNextUseNo(@RequestBody NextUseCodeDTO dto);

    @PostMapping("/api/app/platform/codeRule/getBatchNextUseNo")
    ResponseInfo<BatchNextCodeVO> getBatchNextUseNo(@RequestBody BatchNextUseCodeDTO dto);

    @PostMapping("/api/app/platform/codeRule/confirmNo")
    ResponseInfo<Void> confirmNo(@RequestBody ConfirmNextUseCodeDTO dto);

    @PostMapping("/api/app/platform/codeRule/batchConfirmNo")
    ResponseInfo<Void> batchConfirmNo(@RequestBody BatchConfirmNextUseCodeDTO dto);

    /**
     * 无入参 占位符
     * @param object object
     * @return ResponseInfo<List<CodeRuleVO>>
     */
    @GetMapping("/api/app/platform/codeRule/list")
    ResponseInfo<List<CodeRuleVO>> codeRuleList(Object object);
}
