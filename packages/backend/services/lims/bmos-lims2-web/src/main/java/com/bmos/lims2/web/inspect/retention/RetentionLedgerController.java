package com.bmos.lims2.web.inspect.retention;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.retention.dto.*;
import com.bmos.lims2.server.inspect.retention.service.RetentionLedgerService;
import com.bmos.lims2.web.inspect.retention.vo.req.*;
import com.bmos.lims2.web.inspect.retention.vo.resp.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 留样台账控制器
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@RestController
@RequestMapping("/retention-ledger")
@Api(tags = "留样台账-接口")
@Validated
@Slf4j
public class RetentionLedgerController {

    @Autowired
    private RetentionLedgerService retentionLedgerService;

    @ApiOperation("分页查询留样接收台账列表")
    @PostMapping("/receive/page")
    public ResponseInfo<CommonPage<RetentionReceiveLedgerListRespVO>> getReceiveLedgerPageList(
            @RequestBody @Valid RetentionReceiveLedgerPageReqVO reqVO) {

        // 转换参数
        RetentionReceiveLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionReceiveLedgerPageQueryDTO.class);

        // 查询列表
        CommonPage<RetentionReceiveLedgerListDTO> pageData =
            retentionLedgerService.getReceiveLedgerPageList(queryDTO);

        // 转换结果
        List<RetentionReceiveLedgerListRespVO> respList = BeanUtil.copyToList(
            pageData.getList(), RetentionReceiveLedgerListRespVO.class);

        CommonPage<RetentionReceiveLedgerListRespVO> result = new CommonPage<>();
        result.setTotal(pageData.getTotal());
        result.setPageNum(pageData.getPageNum());
        result.setPageSize(pageData.getPageSize());
        result.setList(respList);

        return ResponseInfo.success(result);
    }

    @ApiOperation("导出留样接收台账")
    @PostMapping("/receive/export")
    public void exportReceiveLedger(
            @RequestBody @Valid RetentionReceiveLedgerPageReqVO reqVO,
            HttpServletResponse response) {

        // 转换参数
        RetentionReceiveLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionReceiveLedgerPageQueryDTO.class);

        // 导出
        retentionLedgerService.exportReceiveLedger(queryDTO, response);
    }

    @ApiOperation("分页查询留样销毁台账列表")
    @PostMapping("/destruction/page")
    public ResponseInfo<CommonPage<RetentionDestructionLedgerListRespVO>> getDestructionLedgerPageList(
            @RequestBody @Valid RetentionDestructionLedgerPageReqVO reqVO) {

        // 转换参数
        RetentionDestructionLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionDestructionLedgerPageQueryDTO.class);

        // 查询列表
        CommonPage<RetentionDestructionLedgerListDTO> pageData =
            retentionLedgerService.getDestructionLedgerPageList(queryDTO);

        // 转换结果
        List<RetentionDestructionLedgerListRespVO> respList = BeanUtil.copyToList(
            pageData.getList(), RetentionDestructionLedgerListRespVO.class);

        CommonPage<RetentionDestructionLedgerListRespVO> result = new CommonPage<>();
        result.setTotal(pageData.getTotal());
        result.setPageNum(pageData.getPageNum());
        result.setPageSize(pageData.getPageSize());
        result.setList(respList);

        return ResponseInfo.success(result);
    }

    @ApiOperation("导出留样销毁台账")
    @PostMapping("/destruction/export")
    public void exportDestructionLedger(
            @RequestBody @Valid RetentionDestructionLedgerPageReqVO reqVO,
            HttpServletResponse response) {

        // 转换参数
        RetentionDestructionLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionDestructionLedgerPageQueryDTO.class);

        // 导出
        retentionLedgerService.exportDestructionLedger(queryDTO, response);
    }

    @ApiOperation("分页查询留样领用台账列表")
    @PostMapping("/collection/page")
    public ResponseInfo<CommonPage<SampleCollectionLedgerListRespVO>> getCollectionLedgerPageList(
            @RequestBody @Valid SampleCollectionLedgerPageReqVO reqVO) {

        // 转换参数
        SampleCollectionLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, SampleCollectionLedgerPageQueryDTO.class);

        // 查询列表
        CommonPage<SampleCollectionLedgerListDTO> pageData =
            retentionLedgerService.getCollectionLedgerPageList(queryDTO);

        // 转换结果
        List<SampleCollectionLedgerListRespVO> respList = BeanUtil.copyToList(
            pageData.getList(), SampleCollectionLedgerListRespVO.class);

        CommonPage<SampleCollectionLedgerListRespVO> result = new CommonPage<>();
        result.setTotal(pageData.getTotal());
        result.setPageNum(pageData.getPageNum());
        result.setPageSize(pageData.getPageSize());
        result.setList(respList);

        return ResponseInfo.success(result);
    }

    @ApiOperation("导出留样领用台账")
    @PostMapping("/collection/export")
    public void exportCollectionLedger(
            @RequestBody @Valid SampleCollectionLedgerPageReqVO reqVO,
            HttpServletResponse response) {

        // 转换参数
        SampleCollectionLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, SampleCollectionLedgerPageQueryDTO.class);

        // 导出
        retentionLedgerService.exportCollectionLedger(queryDTO, response);
    }

    @ApiOperation("分页查询留样观察台账列表")
    @PostMapping("/observation/page")
    public ResponseInfo<CommonPage<RetentionObservationLedgerListRespVO>> getObservationLedgerPageList(
            @RequestBody @Valid RetentionObservationLedgerPageReqVO reqVO) {

        // 转换参数
        RetentionObservationLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionObservationLedgerPageQueryDTO.class);

        // 查询列表
        CommonPage<RetentionObservationLedgerListDTO> pageData =
            retentionLedgerService.getObservationLedgerPageList(queryDTO);

        // 转换结果
        List<RetentionObservationLedgerListRespVO> respList = BeanUtil.copyToList(
            pageData.getList(), RetentionObservationLedgerListRespVO.class);

        CommonPage<RetentionObservationLedgerListRespVO> result = new CommonPage<>();
        result.setTotal(pageData.getTotal());
        result.setPageNum(pageData.getPageNum());
        result.setPageSize(pageData.getPageSize());
        result.setList(respList);

        return ResponseInfo.success(result);
    }

    @ApiOperation("导出留样观察台账")
    @PostMapping("/observation/export")
    public void exportObservationLedger(
            @RequestBody @Valid RetentionObservationLedgerPageReqVO reqVO,
            HttpServletResponse response) {

        // 转换参数
        RetentionObservationLedgerPageQueryDTO queryDTO = BeanUtil.copyProperties(
            reqVO, RetentionObservationLedgerPageQueryDTO.class);

        // 导出
        retentionLedgerService.exportObservationLedger(queryDTO, response);
    }
}
