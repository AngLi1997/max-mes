package com.bmos.platform.service.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mq.listener.Event.StateEvent;
import com.bmos.platform.service.material.dto.RemoteIssueDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionBindRecordDTO;
import com.bmos.platform.service.system.expression.vo.MesRecordTreeNodeVO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "bmos-mes-service", contextId = "bmos-mes-service")
public interface MesFeignClient extends CommonFeignClient {

    @PostMapping("/api/app/mes/product/material/issueMaterialAndCategory")
    ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueDTO dto);

    @PostMapping("/api/app/mes/procedure/expression/condition/update")
    ResponseInfo<Void> conditionUpdate(@RequestBody StateEvent event);

    @GetMapping("/api/app/mes/record/feign/expressionBindTree")
    ResponseInfo<List<MesRecordTreeNodeVO>> getExpressionBindTree(@RequestParam("expressionId") @NotNull Long expressionId);

    @GetMapping("//api/app/mes/record/feign/boundRecordIdList")
    ResponseInfo<List<Long>> getBoundRecordIdList(@RequestParam("expressionId") @NotNull Long expressionId);

    @PostMapping("/api/app/mes/record/feign/expressionBindRecord")
    ResponseInfo<Void> expressionBindBatchRecord(@Validated @RequestBody ExpressionBindRecordDTO dto);

}
