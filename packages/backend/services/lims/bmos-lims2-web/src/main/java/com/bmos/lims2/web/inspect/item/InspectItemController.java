package com.bmos.lims2.web.inspect.item;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.item.dto.InspectItemPageReqDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemWithParameterDTO;
import com.bmos.lims2.server.inspect.item.service.InspectItemService;
import com.bmos.lims2.web.inspect.item.vo.req.InspectItemCreateReqVO;
import com.bmos.lims2.web.inspect.item.vo.req.InspectItemParameterVO;
import com.bmos.lims2.web.inspect.item.vo.req.InspectItemUpdateReqVO;
import com.bmos.lims2.web.inspect.item.vo.req.InspectProgramPageReqVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import com.bmos.common.tree.TreeUtil;
import com.bmos.lims2.web.inspect.item.vo.resp.InspectItemDetailRespVO;
import com.bmos.lims2.web.inspect.item.vo.resp.InspectItemListRespVO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemListDTO;
import com.bmos.lims2.web.inspect.parameter.vo.resp.InspectParameterDataPointRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeItemTreeVO;

/**
 * 检验项目 controller
 */
@RestController
@RequestMapping("/inspect/item")
@Api(tags = "检验项目-接口")
@Validated
public class InspectItemController {

    @Autowired
    InspectItemService inspectItemService;

    @PostMapping("/save")
    @ApiOperation("新增检验项目")
    @OperationLog
    public ResponseInfo<List<Long>> saveInspectProgram(@RequestBody @Validated InspectItemCreateReqVO reqVO) {
        return ResponseInfo.success(inspectItemService.saveInspectProgram(BeanUtil.copyProperties(reqVO, InspectItemWithParameterDTO.class)));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除检验项目")
    @OperationLog
    public ResponseInfo<Void> deleteInspectProgram(@PathVariable Long id) {
        inspectItemService.deleteInspectProgram(id);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑检验项目")
    @OperationLog
    public ResponseInfo<List<Long>> updateInspectProgram(@RequestBody @Validated InspectItemUpdateReqVO reqVO) {
        return ResponseInfo.success(inspectItemService.updateInspectProgram(BeanUtil.copyProperties(reqVO, InspectItemWithParameterDTO.class)));
    }

    @GetMapping("/page")
    @ApiOperation("检验项目分页查询")
    public ResponseInfo<CommonPage<InspectItemWithParameterDTO>> inspectProgramPage(@Validated InspectProgramPageReqVO reqVO) {
        return ResponseInfo.success(inspectItemService.inspectProgramPage(BeanUtil.copyProperties(reqVO, InspectItemPageReqDTO.class)));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("检验项目详情查询")
    public ResponseInfo<InspectItemDetailRespVO> inspectProgramInfo(@PathVariable Long id) {
        InspectItemWithParameterDTO dto = inspectItemService.inspectProgramInfo(id);
        InspectItemDetailRespVO respVO = convertToDetailRespVO(dto);
        return ResponseInfo.success(respVO);
    }

    /**
     * 转换DTO到VO
     */
    private InspectItemDetailRespVO convertToDetailRespVO(InspectItemWithParameterDTO dto) {
        InspectItemDetailRespVO respVO = BeanUtil.copyProperties(dto, InspectItemDetailRespVO.class);
        if (dto.getParameterList() != null) {
            List<InspectItemDetailRespVO.InspectItemAnalysisItemVO> parameterVOList = dto.getParameterList().stream()
                .map(paramDTO -> {
                    InspectItemDetailRespVO.InspectItemAnalysisItemVO paramVO = new InspectItemDetailRespVO.InspectItemAnalysisItemVO();
                    paramVO.setInspectParameterId(paramDTO.getInspectParameterId());
                    paramVO.setCode(paramDTO.getCode());
                    paramVO.setName(paramDTO.getName());
                    paramVO.setStandard(paramDTO.getStandard());
                    if (paramDTO.getDataPoints() != null) {
                        paramVO.setDataPoints(BeanUtil.copyToList(paramDTO.getDataPoints(), InspectParameterDataPointRespVO.class));
                    }
                    return paramVO;
                })
                .collect(Collectors.toList());
            respVO.setParameterList(parameterVOList);
        }
        return respVO;
    }

    @GetMapping("/list")
    @ApiOperation("查询检验项目列表 - 用于下拉选择")
    public ResponseInfo<List<InspectItemListRespVO>> getInspectItemList() {
        List<InspectItemListDTO> dtoList = inspectItemService.getList();
        List<InspectItemListRespVO> voList = BeanUtil.copyToList(dtoList, InspectItemListRespVO.class);
        return ResponseInfo.success(voList);
    }

    @GetMapping("/item-parameter-tree")
    @ApiOperation("查询全量检验项目-分析项树（用于方案版本编辑时选择）")
    public ResponseInfo<List<InspectionSchemeItemTreeVO>> listItemParameterTree() {
        List<InspectItemWithParameterDTO> items = inspectItemService.listAllWithParameters();
        List<InspectionSchemeItemTreeVO> flatList = new ArrayList<>();
        if (items != null) {
            for (InspectItemWithParameterDTO item : items) {
                // 检验项目节点，parentId 设为根节点
                InspectionSchemeItemTreeVO itemNode = new InspectionSchemeItemTreeVO();
                itemNode.setId(item.getId());
                itemNode.setParentId(TreeUtil.parentId);
                itemNode.setName(item.getName());
                itemNode.setCode(item.getCode());
                itemNode.setNodeType("ITEM");
                flatList.add(itemNode);
                // 分析项节点，parentId 设为所属检验项目ID
                if (item.getParameterList() != null) {
                    for (com.bmos.lims2.server.inspect.item.dto.InspectItemParameterDTO p : item.getParameterList()) {
                        InspectionSchemeItemTreeVO paramNode = new InspectionSchemeItemTreeVO();
                        paramNode.setId(p.getInspectParameterId());
                        paramNode.setParentId(item.getId());
                        paramNode.setName(p.getName());
                        paramNode.setCode(p.getCode());
                        paramNode.setNodeType("PARAMETER");
                        flatList.add(paramNode);
                    }
                }
            }
        }
        return ResponseInfo.success(TreeUtil.buildTree(flatList, false));
    }

}
