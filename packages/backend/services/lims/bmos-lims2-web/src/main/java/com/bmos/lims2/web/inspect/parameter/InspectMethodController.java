package com.bmos.lims2.web.inspect.parameter;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.parameter.dto.*;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;
import com.bmos.lims2.server.inspect.parameter.service.InspectMethodBindService;
import com.bmos.lims2.server.inspect.parameter.service.InspectMethodService;
import com.bmos.lims2.web.inspect.parameter.vo.resp.InspectMethodEffectiveRespVO;
import com.bmos.lims2.web.inspect.parameter.vo.resp.MethodBoundOperateEffectiveRespVO;
import com.bmos.lims2.web.inspect.parameter.vo.resp.InspectMethodListRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * @Description: 分析方法-接口
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@RestController
@RequestMapping("/inspect/method")
@Api(tags = "分析方法-接口")
@Validated
public class InspectMethodController {

    @Autowired
    private InspectMethodService inspectMethodService;

    @Autowired
    private com.bmos.lims2.server.inspect.parameter.service.InspectMethodOperateBindService inspectMethodOperateBindService;

    @GetMapping("/list-by-parameter/{parameterId}")
    @ApiOperation("按分析项ID查询方法列表")
    public ResponseInfo<List<InspectMethod>> listByParameter(@PathVariable Long parameterId) {
        List<InspectMethod> list = inspectMethodService.listByParameterId(parameterId);
        return ResponseInfo.success(list);
    }

    @GetMapping("/effective/{parameterId}")
    @ApiOperation("按分析项ID查询有生效版本的方法列表")
    public ResponseInfo<List<InspectMethodEffectiveRespVO>> listEffectiveByParameter(@PathVariable Long parameterId) {
        List<InspectMethodEffectiveDTO> dtos = inspectMethodService.listEffectiveMethodsByParameterId(parameterId);
        List<InspectMethodEffectiveRespVO> vos = BeanUtil.copyToList(dtos, InspectMethodEffectiveRespVO.class);
        return ResponseInfo.success(vos);
    }


    @Autowired
    private InspectMethodBindService inspectMethodBindService;

    @PostMapping("/batch-save-by-parameter")
    @ApiOperation("通过分析项ID批量绑定方法（覆盖式）")
    public ResponseInfo<Void> batchSaveByParameter(@RequestBody @Valid com.bmos.lims2.web.inspect.parameter.vo.req.InspectMethodBindBatchReqVO reqVO) {
        InspectMethodBindBatchSaveDTO dto = BeanUtil.copyProperties(reqVO, InspectMethodBindBatchSaveDTO.class);
        inspectMethodBindService.saveBindings(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/bind/operate/batch-save-by-method")
    @ApiOperation("按方法批量绑定操作规程（覆盖式）")
    public ResponseInfo<Void> batchSaveOperateByMethod(@RequestBody @Valid com.bmos.lims2.web.inspect.parameter.vo.req.InspectMethodOperateBindBatchReqVO reqVO) {
        com.bmos.lims2.server.inspect.parameter.dto.InspectMethodOperateBindBatchSaveDTO dto =
                BeanUtil.copyProperties(reqVO, com.bmos.lims2.server.inspect.parameter.dto.InspectMethodOperateBindBatchSaveDTO.class);
        inspectMethodOperateBindService.saveOperateBindingsForMethod(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/bind/operate/effective/{recordId}")
    @ApiOperation("根据方法ID查询已绑定且启用版本的操作规程（含下载路径）")
    public ResponseInfo<List<MethodBoundOperateEffectiveRespVO>> listEffectiveOperateByMethod(@PathVariable Long recordId) {
        List<com.bmos.lims2.server.inspect.parameter.dto.MethodBoundOperateEffectiveDTO> dtos =
                inspectMethodOperateBindService.listEffectiveOperateByMethod(recordId);
        List<MethodBoundOperateEffectiveRespVO> vos = cn.hutool.core.bean.BeanUtil.copyToList(dtos, MethodBoundOperateEffectiveRespVO.class);
        return ResponseInfo.success(vos);
    }
}


