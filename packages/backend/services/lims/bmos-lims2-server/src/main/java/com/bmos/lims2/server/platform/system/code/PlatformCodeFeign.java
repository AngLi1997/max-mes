package com.bmos.lims2.server.platform.system.code;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.platform.system.code.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-productplan-coderule")
public interface PlatformCodeFeign {

    /**
     * 获取下一个编号 未确认使用的不包含
     *
     * @param dto dto
     * @return ResponseInfo<NextCodeVO>
     */
    @PostMapping("/api/app/platform/codeRule/getNextNo")
    ResponseInfo<NextCodeVO> getNextNo(NextUseCodeDTO dto);

    /**
     * 获取下一个编号 未确认使用的编号会重复返回
     *
     * @param dto dto
     * @return ResponseInfo<NextCodeVO>
     */
    @PostMapping("/api/app/platform/codeRule/getNextUseNo")
    ResponseInfo<NextCodeVO> getNextUseNo(NextUseCodeDTO dto);

    /**
     * 批量获取下一个编号
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/codeRule/getBatchNextUseNo")
    ResponseInfo<BatchNextCodeVO> getBatchNextUseNo(BatchNextUseCodeDTO dto);

    /**
     * 回调确定编号已被使用
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/codeRule/confirmNo")
    ResponseInfo<Void> confirmNo(ConfirmNextUseCodeDTO dto);

    /**
     * 批量确定编号已被使用
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/codeRule/batchConfirmNo")
    ResponseInfo<Void> batchConfirmNo( BatchConfirmNextUseCodeDTO dto);


}
