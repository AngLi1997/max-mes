package com.bmos.lims2.web.inspect.parameter;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.parameter.dto.*;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterService;
import com.bmos.lims2.web.inspect.parameter.vo.req.*;
import com.bmos.lims2.web.inspect.parameter.vo.resp.*;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分析项
 */
@RestController
@RequestMapping("/inspect/parameter")
@Api(tags = "分析项-接口")
@Validated
public class InspectParameterController {

    @Autowired
    InspectParameterService inspectParameterService;

    @PostMapping("/save")
    @ApiOperation("新增分析项")
    @OperationLog
    public ResponseInfo<Void> saveAnalyzeProgram(@RequestBody @Validated InspectParameterSaveReqVO reqVO) {
        inspectParameterService.save(BeanUtil.copyProperties(reqVO, InspectParameterDTO.class));
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除分析项")
    @OperationLog
    public ResponseInfo<Void> deleteAnalyzeProgram(@PathVariable Long id) {
        inspectParameterService.delete(id);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑分析项")
    @OperationLog
    public ResponseInfo<Void> updateAnalyzeProgram(@RequestBody @Validated InspectParameterUpdateReqVO reqVO) {
        inspectParameterService.update(reqVO.getId(),BeanUtil.copyProperties(reqVO, InspectParameterDTO.class));
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("分析项分页查询")
    public ResponseInfo<CommonPage<InspectParameterRespVO>> analyzeProgramPage(@Validated InspectParameterPageReqVO reqVO) {
        CommonPage<InspectParameterDTO> page = inspectParameterService.getPage(BeanUtil.copyProperties(reqVO, InspectParameterPageReqDTO.class));
        CommonPage<InspectParameterRespVO> resultPage = BeanUtil.copyProperties(page, CommonPage.class);
        List<InspectParameterRespVO> records = BeanUtil.copyToList(page.getList(), InspectParameterRespVO.class);
        resultPage.setList(records);
        return ResponseInfo.success(resultPage);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("根据分析项ID查询分析项详情")
    public ResponseInfo<InspectParameterRespVO> getParameterDetail(@PathVariable Long id) {
        InspectParameterDTO parameterDTO = inspectParameterService.getById(id);
        InspectParameterRespVO respVO = BeanUtil.copyProperties(parameterDTO, InspectParameterRespVO.class);
        return ResponseInfo.success(respVO);
    }

    @GetMapping("/list")
    @ApiOperation("查询分析项列表 - 用于下拉选择")
    public ResponseInfo<List<InspectParameterListRespVO>> getParameterList() {
        List<InspectParameterListDTO> dtoList = inspectParameterService.getList();
        List<InspectParameterListRespVO> voList = BeanUtil.copyToList(dtoList, InspectParameterListRespVO.class);
        return ResponseInfo.success(voList);
    }

}
