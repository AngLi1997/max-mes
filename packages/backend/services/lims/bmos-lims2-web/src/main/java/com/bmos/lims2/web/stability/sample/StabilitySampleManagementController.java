package com.bmos.lims2.web.stability.sample;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleDestroyDTO;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleManagementQueryDTO;
import com.bmos.lims2.server.stability.sample.dto.request.StabilitySampleMoveDTO;
import com.bmos.lims2.server.stability.sample.dto.response.StabilitySampleGroupDTO;
import com.bmos.lims2.server.stability.sample.service.StabilitySampleManagementService;
import com.bmos.lims2.web.stability.sample.vo.request.StabilitySampleDestroyReqVO;
import com.bmos.lims2.web.stability.sample.vo.request.StabilitySampleManagementQueryReqVO;
import com.bmos.lims2.web.stability.sample.vo.request.StabilitySampleMoveReqVO;
import com.bmos.lims2.web.stability.sample.vo.response.StabilitySampleGroupRespVO;
import com.bmos.lims2.web.stability.sample.vo.response.StabilitySampleRowRespVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.common.holder.SysUserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 稳定性样品管理Controller
 */
@RestController
@RequestMapping("/stability-sample-management")
@Api(tags = "稳定性样品管理-接口")
@Validated
public class StabilitySampleManagementController {

    @Autowired
    private StabilitySampleManagementService stabilitySampleManagementService;
    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @PostMapping("/page")
    @ApiOperation("分页查询稳定性样品（按考察计划分组）")
    public ResponseInfo<CommonPage<StabilitySampleGroupRespVO>> page(
            @Validated @RequestBody StabilitySampleManagementQueryReqVO reqVO) {
        StabilitySampleManagementQueryDTO queryDTO =
                BeanUtil.copyProperties(reqVO, StabilitySampleManagementQueryDTO.class);
        CommonPage<StabilitySampleGroupDTO> dtoPage =
                stabilitySampleManagementService.pageStabilitySamples(queryDTO);

        CommonPage<StabilitySampleGroupRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(dtoPage.getPageNum());
        respPage.setPageSize(dtoPage.getPageSize());
        respPage.setTotal(dtoPage.getTotal());
        respPage.setList(dtoPage.getList().stream().map(dto -> {
            StabilitySampleGroupRespVO vo = BeanUtil.copyProperties(dto, StabilitySampleGroupRespVO.class);
            vo.setSamples(BeanUtil.copyToList(dto.getSamples(), StabilitySampleRowRespVO.class));
            return vo;
        }).collect(Collectors.toList()));

        return ResponseInfo.success(respPage);
    }

    @PutMapping("/{id}/move")
    @ApiOperation("移动样品储存位置")
    public ResponseInfo<Void> move(@PathVariable Long id,
            @RequestBody @Validated StabilitySampleMoveReqVO reqVO) {
        StabilitySampleMoveDTO dto = new StabilitySampleMoveDTO();
        dto.setId(id);
        dto.setStorageLocation(reqVO.getStorageLocation());
        dto.setMoveReason(reqVO.getMoveReason());
        stabilitySampleManagementService.moveSampleLocation(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/batch-destroy")
    @ApiOperation("批量销毁样品（仅待销毁状态可操作）")
    public ResponseInfo<Void> batchDestroy(@RequestBody @Validated StabilitySampleDestroyReqVO reqVO) {
        StabilitySampleDestroyDTO dto = new StabilitySampleDestroyDTO();
        dto.setDestructionReason(reqVO.getDestructionReason());
        dto.setDestructionMethod(reqVO.getDestructionMethod());
        dto.setDestructionTime(reqVO.getDestructionTime());
        dto.setDestructionLocation(reqVO.getDestructionLocation());
        dto.setRemark(reqVO.getRemark());
        dto.setSupervisorId(reqVO.getSupervisorId());
        dto.setSupervisorName(reqVO.getSupervisorName());

        if (reqVO.getDestructorId() != null && !reqVO.getDestructorId().isEmpty()) {
            dto.setDestructorId(reqVO.getDestructorId());
            dto.setDestructorName(reqVO.getDestructorName());
        } else {
            dto.setDestructorId(SysUserHolder.getUser().getUserId());
            dto.setDestructorName(SysUserHolder.getUser().getUserName());
        }

        stabilitySampleManagementService.batchDestroySamples(reqVO.getIds(), dto);
        return ResponseInfo.success();
    }

    @GetMapping("/{id}/history")
    @ApiOperation("查询样品操作历史")
    public ResponseInfo<List<ListLogVO>> history(@PathVariable Long id) {
        return ResponseInfo.success(auditOperationLogService.listRecordLog(id));
    }
}
