package com.bmos.lims2.web.inspect.scheme;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionAuditQueryDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionCopyDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeBasicSaveDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeSamplingUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionFullConfigDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeVersionAuditDTO;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeVersionService;
import com.bmos.lims2.web.inspect.scheme.vo.request.InspectionSchemeVersionQueryReqVO;
import com.bmos.lims2.web.inspect.scheme.vo.request.InspectionSchemeVersionAuditQueryReqVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeVersionBasicConfigRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeVersionFullConfigRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeVersionRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeVersionAuditRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.request.InspectionSchemeVersionCopyReqVO;
import com.bmos.lims2.web.inspect.scheme.vo.request.InspectionSchemeVersionCompleteReqVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验方案版本Controller
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@RestController
@RequestMapping("/inspection-scheme/version")
@Api(tags = "检验方案版本-接口")
@Validated
public class InspectionSchemeVersionController {

    @Autowired
    private InspectionSchemeVersionService inspectionSchemeVersionService;

    @GetMapping("/page")
    @ApiOperation("分页查询检验方案版本")
    public ResponseInfo<CommonPage<InspectionSchemeVersionRespVO>> pageInspectionSchemeVersion(@Validated InspectionSchemeVersionQueryReqVO reqVO) {
        InspectionSchemeVersionQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, InspectionSchemeVersionQueryDTO.class);
        CommonPage<InspectionSchemeVersionDTO> page = inspectionSchemeVersionService.pageInspectionSchemeVersion(queryDTO);
        CommonPage<InspectionSchemeVersionRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), InspectionSchemeVersionRespVO.class));
        return ResponseInfo.success(respPage);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取检验方案版本详情")
    public ResponseInfo<InspectionSchemeVersionRespVO> getInspectionSchemeVersion(@PathVariable Long id) {
        InspectionSchemeVersionDTO dto = inspectionSchemeVersionService.getInspectionSchemeVersion(id);
        return ResponseInfo.success(BeanUtil.copyProperties(dto, InspectionSchemeVersionRespVO.class));
    }

    @GetMapping("/{id}/full-config")
    @ApiOperation("获取检验方案版本完整配置（包含所有详细信息，用于复制、编辑和查看明细）")
    public ResponseInfo<InspectionSchemeVersionFullConfigRespVO> getInspectionSchemeVersionFullConfig(@PathVariable Long id) {
        InspectionSchemeVersionFullConfigDTO dto = inspectionSchemeVersionService.getInspectionSchemeVersionFullConfig(id);
        InspectionSchemeVersionFullConfigRespVO vo = BeanUtil.copyProperties(dto, InspectionSchemeVersionFullConfigRespVO.class);
        // 判定异常标识为 List<JudgmentConfigDTO> 中的布尔字段，BeanUtil 已可映射；确保分析项层级聚合标识同步
        if (dto.getInspectionItems() != null && vo.getInspectionItems() != null) {
            for (int i = 0; i < dto.getInspectionItems().size(); i++) {
                InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO itemDto = dto.getInspectionItems().get(i);
                InspectionSchemeVersionFullConfigRespVO.InspectionItemConfigRespVO itemVo = vo.getInspectionItems().get(i);
                if (itemDto.getInspectionParameters() != null && itemVo.getInspectionParameters() != null) {
                    for (int j = 0; j < itemDto.getInspectionParameters().size(); j++) {
                        InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO paramDto = itemDto.getInspectionParameters().get(j);
                        InspectionSchemeVersionFullConfigRespVO.AnalyzeItemConfigRespVO paramVo = itemVo.getInspectionParameters().get(j);
                        paramVo.setJudgmentDataPointDeleted(paramDto.getJudgmentDataPointDeleted());
                        paramVo.setJudgmentDataPointBindingMissing(paramDto.getJudgmentDataPointBindingMissing());
                        paramVo.setJudgmentDataPointTypeChanged(paramDto.getJudgmentDataPointTypeChanged());
                        paramVo.setJudgmentDataPointOptionInvalid(paramDto.getJudgmentDataPointOptionInvalid());
                        paramVo.setJudgmentConfigError(paramDto.getJudgmentConfigError());
                    }
                }
            }
        }
        return ResponseInfo.success(vo);
    }

    @GetMapping("/{id}/basic")
    @ApiOperation("获取检验方案版本基础信息（版本信息、物料、实验包）")
    public ResponseInfo<InspectionSchemeVersionBasicConfigRespVO> getVersionBasicConfig(@PathVariable Long id) {
        InspectionSchemeVersionFullConfigDTO dto = inspectionSchemeVersionService.getVersionBasicConfig(id);
        InspectionSchemeVersionBasicConfigRespVO vo = new InspectionSchemeVersionBasicConfigRespVO();
        vo.setId(dto.getId());
        vo.setSchemeId(dto.getSchemeId());
        vo.setSchemeName(dto.getSchemeName());
        vo.setDescription(dto.getDescription());
        vo.setVersionNo(dto.getVersionNo());
        vo.setStatus(dto.getStatus());
        vo.setParentVersionId(dto.getParentVersionId());
        vo.setParentVersionNo(dto.getParentVersionNo());
        vo.setEffectiveDate(dto.getEffectiveDate());
        vo.setCreateTime(dto.getCreateTime());
        if (dto.getMaterial() != null) {
            InspectionSchemeVersionBasicConfigRespVO.MaterialInfoRespVO materialVO =
                    BeanUtil.copyProperties(dto.getMaterial(), InspectionSchemeVersionBasicConfigRespVO.MaterialInfoRespVO.class);
            vo.setMaterial(materialVO);
        }
        if (dto.getPackageInfo() != null) {
            InspectionSchemeVersionBasicConfigRespVO.PackageInfoRespVO packageVO =
                    BeanUtil.copyProperties(dto.getPackageInfo(), InspectionSchemeVersionBasicConfigRespVO.PackageInfoRespVO.class);
            vo.setPackageInfo(packageVO);
        }
        return ResponseInfo.success(vo);
    }

    @GetMapping("/{id}/items")
    @ApiOperation("获取检验方案版本分析项信息（检验项目列表，含分析项、数据点、判定）")
    public ResponseInfo<List<InspectionSchemeVersionFullConfigRespVO.InspectionItemConfigRespVO>> getVersionItemsConfig(@PathVariable Long id) {
        List<InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO> dtoList =
                inspectionSchemeVersionService.getVersionItemsConfig(id);
        List<InspectionSchemeVersionFullConfigRespVO.InspectionItemConfigRespVO> voList =
                BeanUtil.copyToList(dtoList, InspectionSchemeVersionFullConfigRespVO.InspectionItemConfigRespVO.class);
        // 同步判定一致性标识（BeanUtil 不能自动映射同名布尔字段到内嵌列表）
        for (int i = 0; i < dtoList.size(); i++) {
            InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO itemDto = dtoList.get(i);
            InspectionSchemeVersionFullConfigRespVO.InspectionItemConfigRespVO itemVo = voList.get(i);
            if (itemDto.getInspectionParameters() != null && itemVo.getInspectionParameters() != null) {
                for (int j = 0; j < itemDto.getInspectionParameters().size(); j++) {
                    InspectionSchemeVersionFullConfigDTO.InspectParameterConfigDTO paramDto =
                            itemDto.getInspectionParameters().get(j);
                    InspectionSchemeVersionFullConfigRespVO.AnalyzeItemConfigRespVO paramVo =
                            itemVo.getInspectionParameters().get(j);
                    paramVo.setJudgmentDataPointDeleted(paramDto.getJudgmentDataPointDeleted());
                    paramVo.setJudgmentDataPointBindingMissing(paramDto.getJudgmentDataPointBindingMissing());
                    paramVo.setJudgmentDataPointTypeChanged(paramDto.getJudgmentDataPointTypeChanged());
                    paramVo.setJudgmentDataPointOptionInvalid(paramDto.getJudgmentDataPointOptionInvalid());
                    paramVo.setJudgmentConfigError(paramDto.getJudgmentConfigError());
                }
            }
        }
        return ResponseInfo.success(voList);
    }

    @GetMapping("/{id}/sampling")
    @ApiOperation("获取检验方案版本取样配置信息")
    public ResponseInfo<List<InspectionSchemeVersionFullConfigRespVO.SamplingConfigRespVO>> getVersionSamplingConfig(@PathVariable Long id) {
        List<InspectionSchemeVersionFullConfigDTO.SamplingConfigDTO> dtoList =
                inspectionSchemeVersionService.getVersionSamplingConfig(id);
        return ResponseInfo.success(BeanUtil.copyToList(dtoList, InspectionSchemeVersionFullConfigRespVO.SamplingConfigRespVO.class));
    }

    @PostMapping("/{id}/complete")
    @ApiOperation("完成检验方案版本（编辑中→已完成）：提交全量数据，后台校验必填项并置为已完成")
    public ResponseInfo<Void> completeInspectionSchemeVersion(@PathVariable Long id,
            @RequestBody @Validated InspectionSchemeVersionCompleteReqVO reqVO) {
        InspectionSchemeBasicSaveDTO basicDTO = BeanUtil.copyProperties(reqVO.getBasic(), InspectionSchemeBasicSaveDTO.class);
        if (reqVO.getBasic().getMaterial() != null) {
            InspectionSchemeBasicSaveDTO.MaterialInfoDTO materialDTO = new InspectionSchemeBasicSaveDTO.MaterialInfoDTO();
            materialDTO.setMaterialId(reqVO.getBasic().getMaterial().getMaterialId());
            materialDTO.setMaterialName(reqVO.getBasic().getMaterial().getMaterialName());
            materialDTO.setMaterialCode(reqVO.getBasic().getMaterial().getMaterialCode());
            basicDTO.setMaterial(materialDTO);
        }
        List<InspectionSchemeItemUpdateDTO> itemUpdates = null;
        if (reqVO.getItemUpdates() != null) {
            itemUpdates = BeanUtil.copyToList(reqVO.getItemUpdates(), InspectionSchemeItemUpdateDTO.class);
        }
        List<InspectionSchemeSamplingUpdateDTO> samplingUpdates = null;
        if (reqVO.getSamplingUpdates() != null) {
            samplingUpdates = BeanUtil.copyToList(reqVO.getSamplingUpdates(), InspectionSchemeSamplingUpdateDTO.class);
        }
        inspectionSchemeVersionService.completeInspectionSchemeVersion(id, basicDTO, itemUpdates, samplingUpdates);
        return ResponseInfo.success();
    }

    @PostMapping("/{id}/activate")
    @ApiOperation("启用检验方案版本")
    public ResponseInfo<Void> activateInspectionSchemeVersion(@PathVariable Long id) {
        inspectionSchemeVersionService.activateInspectionSchemeVersion(id);
        return ResponseInfo.success();
    }

    @PostMapping("/{id}/deactivate")
    @ApiOperation("停用检验方案版本")
    public ResponseInfo<Void> deactivateInspectionSchemeVersion(@PathVariable Long id) {
        inspectionSchemeVersionService.deactivateInspectionSchemeVersion(id);
        return ResponseInfo.success();
    }

    @PostMapping("/{id}/void")
    @ApiOperation("作废检验方案版本")
    public ResponseInfo<Void> voidInspectionSchemeVersion(@PathVariable Long id) {
        inspectionSchemeVersionService.voidInspectionSchemeVersion(id);
        return ResponseInfo.success();
    }

    // ========== 审批相关接口 ==========

    @PostMapping("/{id}/audit")
    @ApiOperation("提交审批")
    public ResponseInfo<Void> auditSchemeVersion(@PathVariable Long id) {
        inspectionSchemeVersionService.activateInspectionSchemeVersion(id);
        return ResponseInfo.success();
    }

    @GetMapping("/audit/page")
    @ApiOperation("审核待办列表")
    public ResponseInfo<CommonPage<InspectionSchemeVersionAuditRespVO>> getAuditTodoPage(@Validated InspectionSchemeVersionAuditQueryReqVO reqVO) {
        InspectionSchemeVersionAuditQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, InspectionSchemeVersionAuditQueryDTO.class);
        CommonPage<InspectionSchemeVersionAuditDTO> page = inspectionSchemeVersionService.pageAuditSchemeVersions(queryDTO);
        CommonPage<InspectionSchemeVersionAuditRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), InspectionSchemeVersionAuditRespVO.class));
        return ResponseInfo.success(respPage);
    }

    // 注意：审批操作（通过、不通过、退回）请使用通用的审批接口：
    // POST /audit/complete - 审批通过
    // POST /audit/complete/not/approve - 审批不通过  
    // POST /audit/back/to/prev - 审批退回

    @PostMapping("/copy")
    @ApiOperation("新增方案版本（从源版本复制配置），返回新版本ID")
    public ResponseInfo<Long> copyVersionFromSource(@RequestBody @Validated InspectionSchemeVersionCopyReqVO reqVO) {
        InspectionSchemeVersionCopyDTO dto = BeanUtil.copyProperties(reqVO, InspectionSchemeVersionCopyDTO.class);
        return ResponseInfo.success(inspectionSchemeVersionService.copyVersionFromSource(dto));
    }

    @GetMapping("/{id}/operate-rules")
    @ApiOperation("查询方案版本关联的操作规程列表")
    public ResponseInfo<java.util.List<com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOperateRuleDTO>> listOperateRules(@PathVariable Long id) {
        return ResponseInfo.success(inspectionSchemeVersionService.listOperateRulesByVersionId(id));
    }
} 