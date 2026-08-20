package com.bmos.lims2.web.inspect.retention;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManageListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManagePageQueryDTO;
import com.bmos.lims2.server.inspect.retention.dto.SampleDestructionDTO;
import com.bmos.lims2.server.inspect.retention.service.RetentionSampleManageService;
import com.bmos.lims2.web.inspect.retention.vo.req.BatchSampleDestructionReqVO;
import com.bmos.lims2.web.inspect.retention.vo.req.RetentionCollectReqVO;
import com.bmos.lims2.web.inspect.retention.vo.req.RetentionExtendReqVO;
import com.bmos.lims2.web.inspect.retention.vo.req.RetentionSampleManagePageReqVO;
import com.bmos.lims2.web.inspect.retention.vo.req.SampleDestructionReqVO;
import com.bmos.lims2.web.inspect.retention.vo.resp.RetentionSampleManageListRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Description: 留样样品管理控制器
 * @Author: yigaohui
 * @Date: 2026/02/09
 */
@RestController
@RequestMapping("/retention-sample-manage")
@Api(tags = "留样样品管理-接口")
@Validated
@Slf4j
public class RetentionSampleManageController {

    @Autowired
    private RetentionSampleManageService retentionSampleManageService;

    @ApiOperation("分页查询留样样品管理列表")
    @PostMapping("/page")
    public ResponseInfo<CommonPage<RetentionSampleManageListRespVO>> getManagePageList(
            @RequestBody @Valid RetentionSampleManagePageReqVO reqVO) {

        // 转换参数
        RetentionSampleManagePageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionSampleManagePageQueryDTO.class);

        // 查询列表
        CommonPage<RetentionSampleManageListDTO> pageData =
            retentionSampleManageService.getManagePageList(queryDTO);

        // 转换结果
        List<RetentionSampleManageListRespVO> respList = BeanUtil.copyToList(
            pageData.getList(), RetentionSampleManageListRespVO.class);

        CommonPage<RetentionSampleManageListRespVO> result = new CommonPage<>();
        result.setTotal(pageData.getTotal());
        result.setPageNum(pageData.getPageNum());
        result.setPageSize(pageData.getPageSize());
        result.setList(respList);

        return ResponseInfo.success(result);
    }

    @ApiOperation("留样延期")
    @PostMapping("/{sampleId}/extend")
    public ResponseInfo<Void> extendRetention(
            @PathVariable @NotNull(message = "样品ID不能为空") Long sampleId,
            @RequestBody @Valid RetentionExtendReqVO reqVO) {

        retentionSampleManageService.extendRetention(sampleId, reqVO.getNewExpiryDate());
        return ResponseInfo.success();
    }

    @ApiOperation("留样样品领用")
    @PostMapping("/{sampleId}/collect")
    public ResponseInfo<Void> collectSample(
            @PathVariable @NotNull(message = "样品ID不能为空") Long sampleId,
            @RequestBody @Valid RetentionCollectReqVO reqVO) {

        retentionSampleManageService.collectSample(
            sampleId,
            reqVO.getCollectQuantity(),
            reqVO.getUnitId(),
            reqVO.getCollectReason(),
            reqVO.getCollectorId(),
            reqVO.getCollectorName()
        );
        return ResponseInfo.success();
    }

    @ApiOperation("查询样品操作历史")
    @GetMapping("/{sampleId}/history")
    public ResponseInfo<List<ListLogVO>> getOperationHistory(
            @PathVariable @NotNull(message = "样品ID不能为空") Long sampleId) {

        List<ListLogVO> history = retentionSampleManageService.getOperationHistory(sampleId);
        return ResponseInfo.success(history);
    }

    @ApiOperation("销毁样品（单个）")
    @PostMapping("/{sampleId}/destroy")
    public ResponseInfo<Void> destroySample(
            @PathVariable @NotNull(message = "样品ID不能为空") Long sampleId,
            @RequestBody @Valid SampleDestructionReqVO reqVO) {

        // 转换参数
        SampleDestructionDTO destructionDTO = BeanUtil.copyProperties(reqVO, SampleDestructionDTO.class);
        destructionDTO.setSampleId(sampleId);

        // 如果销毁人未填写，使用当前登录人
        if (destructionDTO.getDestructorId() == null || destructionDTO.getDestructorId().isEmpty()) {
            destructionDTO.setDestructorId(SysUserHolder.getUser().getUserId());
            destructionDTO.setDestructorName(SysUserHolder.getUser().getLoginName());
        }

        retentionSampleManageService.destroySample(destructionDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("批量销毁样品")
    @PostMapping("/batch-destroy")
    public ResponseInfo<Void> batchDestroySamples(@RequestBody @Valid BatchSampleDestructionReqVO reqVO) {

        // 转换参数
        SampleDestructionDTO destructionDTO = new SampleDestructionDTO();
        destructionDTO.setDestructionReason(reqVO.getDestructionReason());
        destructionDTO.setDestructionMethod(reqVO.getDestructionMethod());
        destructionDTO.setDestructionTime(reqVO.getDestructionTime());
        destructionDTO.setDestructionLocation(reqVO.getDestructionLocation());
        destructionDTO.setRemark(reqVO.getRemark());
        destructionDTO.setDestructorId(reqVO.getDestructorId());
        destructionDTO.setDestructorName(reqVO.getDestructorName());
        destructionDTO.setSupervisorId(reqVO.getSupervisorId());
        destructionDTO.setSupervisorName(reqVO.getSupervisorName());

        // 如果销毁人未填写，使用当前登录人
        if (destructionDTO.getDestructorId() == null || destructionDTO.getDestructorId().isEmpty()) {
            destructionDTO.setDestructorId(SysUserHolder.getUser().getUserId());
            destructionDTO.setDestructorName(SysUserHolder.getUser().getLoginName());
        }

        retentionSampleManageService.batchDestroySamples(reqVO.getSampleIds(), destructionDTO);
        return ResponseInfo.success();
    }
}
