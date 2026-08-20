package com.bmos.lims2.server.platform.expression;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.platform.expression.vo.ExpressionTreeNodeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-platform-expression")
public interface PlatformExpressionFeignClient {

    @GetMapping("/api/app/platform/expression/fullExpressionList")
    ResponseInfo<List<ExpressionTreeNodeVO>> getFullExpressionAndCategoryList(@RequestParam("tree") Boolean tree);

}
