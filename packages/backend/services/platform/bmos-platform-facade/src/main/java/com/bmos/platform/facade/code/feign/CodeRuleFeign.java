package com.bmos.platform.facade.code.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.code.dto.BatchConfirmByCodeDTO;
import com.bmos.platform.facade.code.dto.BatchCodeNoDTO;
import com.bmos.platform.facade.code.dto.ReleaseConfirmedNoDTO;
import com.bmos.platform.facade.code.vo.BatchCodeNoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 编码规则相关feign接口
 */
@FeignClient(name = "bmos-platform-service", contextId = "platform-code-rule")
public interface CodeRuleFeign {

    @PostMapping("/api/app/platform/codeRule/feign/batchConfirmNoByIdList")
    @ApiOperation("根据id批量确认编号")
    ResponseInfo<Void> batchConfirmByIdList(List<Long> idList);

    @PostMapping("/api/app/platform/codeRule/feign/batchConfirmNoByCodeList")
    @ApiOperation("根据code批量确认编号")
    ResponseInfo<Void> batchConfirmByCodeList(@RequestBody BatchConfirmByCodeDTO dto);

    @PostMapping("/api/app/platform/codeRule/feign/releaseNo")
    @ApiOperation("释放已确认的编号")
    ResponseInfo<Void> releaseConfirmedNO(@RequestBody @Validated ReleaseConfirmedNoDTO dto);

    @PostMapping("/api/app/platform/codeRule/feign/sameTypeBatchNo")
    @ApiOperation("批量获取同类型编号")
    ResponseInfo<BatchCodeNoVO> batchGetSameTypeNo(@RequestBody @Validated BatchCodeNoDTO dto) ;

}
