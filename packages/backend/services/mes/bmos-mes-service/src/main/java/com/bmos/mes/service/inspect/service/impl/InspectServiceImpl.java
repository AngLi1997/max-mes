package com.bmos.mes.service.inspect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.TimeUnitEnum;
import com.bmos.mes.common.enums.inpspect.*;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.inspect.dto.InspectRejectDTO;
import com.bmos.mes.inspect.dto.InspectResultCallBackDTO;
import com.bmos.mes.inspect.dto.InspectResultItemDTO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.inspect.controller.vo.InspectDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectPageVO;
import com.bmos.mes.service.inspect.controller.vo.InspectResultVO;
import com.bmos.mes.service.inspect.convert.InspectConvert;
import com.bmos.mes.service.inspect.mapper.InspectInfoMapper;
import com.bmos.mes.service.inspect.mapper.InspectMapper;
import com.bmos.mes.service.inspect.mapper.InspectResultMapper;
import com.bmos.mes.service.inspect.model.Inspect;
import com.bmos.mes.service.inspect.model.InspectInfo;
import com.bmos.mes.service.inspect.model.InspectResult;
import com.bmos.mes.service.inspect.service.InspectConfigService;
import com.bmos.mes.service.inspect.service.InspectService;
import com.bmos.mes.service.inspect.service.dto.InitiateInspectDTO;
import com.bmos.mes.service.inspect.service.dto.InitiateInspectInfoDTO;
import com.bmos.mes.service.inspect.service.dto.InitiateRetryInspectDTO;
import com.bmos.mes.service.inspect.service.dto.InspectPageDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.utils.BigDecimalFormatUtil;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.notify.MessageNotifyFeign;
import com.bmos.platform.facade.notify.dto.MaterialForeWarningMessage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InspectServiceImpl implements InspectService {

    @Autowired
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Autowired
    private InspectConfigService inspectConfigService;

    @Autowired
    private PlanService planService;

    @Autowired
    InspectMapper inspectMapper;

    @Autowired
    InspectInfoMapper inspectInfoMapper;

    @Autowired
    InspectResultMapper inspectResultMapper;

    @Autowired
    UnitCache unitCache;

    @Autowired
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Autowired
    private ProductMaterialService productMaterialService;

    @Autowired
    MessageNotifyFeign messageNotifyFeign;

    @Autowired
    private com.bmos.mes.service.inspect.lims.LimsGatewaySelector limsGatewaySelector;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initiateInspect(InitiateInspectDTO dto) {
        Plan plan = planService.getById(dto.getPlanId());
        if (Objects.isNull(plan)) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProductFormulaMaterial productFormulaMaterial = productFormulaConfigureService.selectById(dto.getFormulaMaterialId());
        if (Objects.isNull(productFormulaMaterial)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        // 三方/本地路径：inspectNo 由前端传入，需校验唯一；自研路径号由 LIMS 生成，发起前 MES 无号，跳过
        if (!limsGatewaySelector.isBmos() && StrUtil.isNotBlank(dto.getInspectNo())) {
            Inspect oldInspect = inspectMapper.selectByInspectNo(dto.getInspectNo());
            if (Objects.nonNull(oldInspect)) {
                throw new BmosException(MesResponseCode.INSPECT_NO_ALREADY_EXIST, dto.getInspectNo());
            }
        }
        // 校验请验单信息
        this.validateInspectInfo(dto.getInitiateInspectInfoDTOList());
        // 请验
        this.doInitiateInspect(dto, plan, productFormulaMaterial);
    }

    @Override
    public CommonPage<InspectPageVO> queryPage(InspectPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<Inspect> inspects = inspectMapper.queryPage(dto);
        return CommonPage.convertPage(inspects, InspectConvert.INSTANCE::convert2PageVO);
    }

    @Override
    public InspectDetailVO queryDetail(Long id) {
        Inspect inspect = inspectMapper.selectById(id);
        if (Objects.isNull(inspect)) {
            throw new BmosException(MesResponseCode.INSPECT_NOT_EXIST);
        }
        InspectDetailVO inspectDetailVO = InspectConvert.INSTANCE.convert2InspectDetailVO(inspect);
        ProductFormulaMaterial productFormulaMaterial = productFormulaConfigureService.selectById(inspect.getFormulaMaterialId());
        if (Objects.nonNull(productFormulaMaterial)) {
            inspectDetailVO.setMaterialId(productFormulaMaterial.getMaterialId());
        }
        inspectDetailVO.setUnitName(unitCache.getGlobalUnitName(inspect.getUnitId()));
        List<InspectInfo> inspectInfos = inspectInfoMapper.selectByInspectId(id);
        inspectDetailVO.setInspectInfoVOList(InspectConvert.INSTANCE.convert2InspectInfoVOList(inspectInfos));
        return inspectDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retryInitiateInspect(InitiateRetryInspectDTO dto) {
        Inspect inspect = inspectMapper.selectById(dto.getId());
        if (Objects.isNull(inspect)) {
            throw new BmosException(MesResponseCode.INSPECT_NOT_EXIST);
        }
        inspect.setFormulaMaterialId(dto.getFormulaMaterialId());
        if (!Objects.equals(inspect.getStatus().getValue(), InspectStatusEnum.REJECTED.getValue())) {
            throw new BmosException(MesResponseCode.INSPECT_REJECTED_STATUS_ERROR);
        }
        if (!Objects.equals(inspect.getFormulaMaterialId(), dto.getFormulaMaterialId())) {
            ProductFormulaMaterial productFormulaMaterial = productFormulaConfigureService.selectById(dto.getFormulaMaterialId());
            inspect.setFormulaMaterialId(dto.getFormulaMaterialId());
            inspect.setMaterialId(productFormulaMaterial.getMaterialId());
            inspect.setMaterialType(productFormulaMaterial.getMaterialType());
            inspect.setMaterialMergeCode(productFormulaMaterial.getMaterialMergeCode());
            inspect.setMaterialName(productFormulaMaterial.getMaterialName());
            inspect.setInspectConfigId(dto.getInspectConfigId());
            // 创建物料批次
            this.createMaterialBatch(dto.getMaterialBatchNo(), dto.getInitiateInspectInfoDTOList(), productFormulaMaterial);
        }
        inspect.setReason(null);
        inspect.setStatus(InspectStatusEnum.PENDING);
        inspect.setInspectTime(LocalDateTime.now());
        SysUser user = SysUserHolder.getUser();
        inspect.setInspectorId(user.getUserId());
        inspect.setInspector(StrUtil.format("{}-{}", user.getUserName(), user.getLoginName()));
        inspectMapper.updateById(inspect);
        inspectInfoMapper.deleteByInspectId(inspect.getId());
        List<InspectInfo> inspectInfos = InspectConvert.INSTANCE.convert2InspectInfoList(dto.getInitiateInspectInfoDTOList(), inspect);
        inspectInfoMapper.saveOrUpdateBatch(inspectInfos);

        // 重新下发 LIMS（自研路径方案B：作废原单+新建，返回新检验单号）；三方/未启用路径维持现状不下发
        if (limsGatewaySelector.enabled()) {
            com.bmos.mes.service.inspect.lims.LimsInspectGateway gateway = limsGatewaySelector.current();
            if (gateway != null) {
                inspect.setSchemeId(dto.getSchemeId());
                inspect.setSchemeVersionId(dto.getSchemeVersionId());
                com.bmos.mes.service.inspect.lims.RetryInspectContext ctx =
                        com.bmos.mes.service.inspect.lims.RetryInspectContext.builder()
                                .originInspectNo(inspect.getInspectNo())
                                .platformMaterialId(resolvePlatformMaterialId(inspect.getMaterialId()))
                                .inspectConfigId(inspect.getInspectConfigId())
                                .schemeId(dto.getSchemeId())
                                .schemeVersionId(dto.getSchemeVersionId())
                                .materialBatchNo(dto.getMaterialBatchNo())
                                .inspectInfos(inspectInfos)
                                .build();
                String newOrderNo = gateway.retry(ctx);
                if (StrUtil.isNotBlank(newOrderNo)) {
                    inspect.setInspectNo(newOrderNo);
                }
                inspectMapper.updateById(inspect);
            }
        }
    }

    @Override
    public void rejectInspect(List<InspectRejectDTO> dtoList) {
        if (CollUtil.isEmpty(dtoList)) {
            return;
        }
        Map<String, InspectRejectDTO> inspectNoDtoMap = CollectionUtils.convertMap(dtoList, InspectRejectDTO::getInspectNo);
        List<Inspect> inspectList = inspectMapper.selectByInspectNoList(Lists.newArrayList(inspectNoDtoMap.keySet()));
        if (CollUtil.isEmpty(inspectList)) {
            return;
        }
        for (Inspect inspect : inspectList) {
            InspectRejectDTO dto = inspectNoDtoMap.get(inspect.getInspectNo());
            inspect.setStatus(InspectStatusEnum.REJECTED);
            inspect.setReason(dto.getReason());
        }
        inspectMapper.updateBatch(inspectList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inspectCallback(InspectResultCallBackDTO inspectResultCallBackDTO) {
        if (CollUtil.isEmpty(inspectResultCallBackDTO.getInspectResultItemDTOS())) {
            return;
        }

        Inspect inspect = inspectMapper.selectByInspectNo(inspectResultCallBackDTO.getInspectNo());
        if (inspect == null) {
            return;
        }
        // 根据检验单号查询检验项结果
        List<InspectResult> inspectResults = inspectResultMapper.selectByInspectId(inspect.getId());
        Map<Long, List<InspectResult>> inspectResultMap = CollUtil.isNotEmpty(inspectResults) ? CollectionUtils.convertMultiMap(inspectResults, InspectResult::getInspectId) : new HashMap<>();
        List<Inspect> alreadyUpdateInspect = new ArrayList<>();
        List<InspectResultItemDTO> inspectResultCallBackDTOS = inspectResultCallBackDTO.getInspectResultItemDTOS();
        if (CollUtil.isEmpty(inspectResultCallBackDTOS)) {
            return;
        }
        if (!Objects.equals(inspect.getStatus(), InspectStatusEnum.PENDING)) {
            return;
        }
        if (doFillInspectResult(inspect, inspectResultCallBackDTO, inspectResultMap.get(inspect.getId()))) {
            alreadyUpdateInspect.add(inspect);
        }

        if (CollUtil.isNotEmpty(alreadyUpdateInspect)) {
            inspectMapper.updateById(inspect);
        }

    }

    @Override
    public InspectResultVO queryInspectResult(Long id) {
        Inspect inspect = inspectMapper.selectById(id);
        if (Objects.isNull(inspect)) {
            throw new BmosException(MesResponseCode.INSPECT_NOT_EXIST);
        }
        InspectResultVO inspectResultVO = InspectConvert.INSTANCE.convert2InspectResultVO(inspect);
        List<InspectResult> inspectResultList = inspectResultMapper.selectByInspectId(inspect.getId());
        inspectResultVO.setInspectProgramResultVOList(InspectConvert.INSTANCE.convert2InspectResultVOList(inspectResultList));
        return inspectResultVO;
    }

    @Override
    public InspectResultVO queryInspectResultByMaterialBatchNoAndMaterialId(String materialBatchNo, Long materialId) {
        List<Inspect> inspects = inspectMapper.selectByMaterialBatchNoAndMaterialId(materialBatchNo, materialId);
        // 获取最新的检验
        Inspect inspect = inspects.stream().max(Comparator.comparing(Inspect::getCreateTime)).orElse(null);
        if (Objects.isNull(inspect)) {
            return null;
        }
        return this.queryInspectResult(inspect.getId());
    }

    @Override
    public List<com.bmos.mes.service.inspect.controller.vo.InspectSchemeVO> querySchemes(Long formulaMaterialId) {
        ProductFormulaMaterial formulaMaterial = productFormulaConfigureService.selectById(formulaMaterialId);
        if (Objects.isNull(formulaMaterial)) {
            throw new BmosException(MesResponseCode.PRODUCT_FORMULA_MATERIAL_NOT_EXISTS);
        }
        if (!limsGatewaySelector.isBmos()) {
            return java.util.Collections.emptyList();
        }
        return limsGatewaySelector.current().querySchemes(resolvePlatformMaterialId(formulaMaterial.getMaterialId()));
    }

    private void validateInspectInfo(List<InitiateInspectInfoDTO> initiateInspectInfoDTOList) {
        if (CollUtil.isEmpty(initiateInspectInfoDTOList)) {
            throw new BmosException(MesResponseCode.INSPECT_INFO_NOT_EXISTS);
        }

        for (InitiateInspectInfoDTO initiateInspectInfoDTO : initiateInspectInfoDTOList) {
            // 校验必填
            if (initiateInspectInfoDTO.getRequired() && Objects.isNull(initiateInspectInfoDTO.getValue())) {
                throw new BmosException(MesResponseCode.INSPECT_INFO_NOT_EMPTY, initiateInspectInfoDTO.getShowName());
            }
        }
    }

    private void doInitiateInspect(InitiateInspectDTO dto, Plan plan, ProductFormulaMaterial productFormulaMaterial) {
        SysUser user = SysUserHolder.getUser();
        Inspect inspect = InspectConvert.INSTANCE.convert2Inspect(dto, plan, productFormulaMaterial, user);
        // 自研路径方案字段
        inspect.setSchemeId(dto.getSchemeId());
        inspect.setSchemeVersionId(dto.getSchemeVersionId());
        inspectMapper.insert(inspect);
        List<InspectInfo> inspectInfos = InspectConvert.INSTANCE.convert2InspectInfoList(dto.getInitiateInspectInfoDTOList(), inspect);
        inspectInfoMapper.saveOrUpdateBatch(inspectInfos);
        createMaterialBatch(dto.getMaterialBatchNo(), dto.getInitiateInspectInfoDTOList(), productFormulaMaterial);
        // 发起请验（自研路径返回 LIMS 检验单号）
        String limsOrderNo = inspectToLims(inspect, inspectInfos);
        if (StrUtil.isNotBlank(limsOrderNo)) {
            inspect.setInspectNo(limsOrderNo);
            inspectMapper.updateById(inspect);
        }
    }

    /**
     * 发起请验：按开关下发到对应 LIMS。
     *
     * @return LIMS 生成的检验单号（三方/未启用返回 null，调用方沿用本地 inspectNo）
     */
    private String inspectToLims(Inspect inspect, List<InspectInfo> inspectInfos) {
        if (!limsGatewaySelector.enabled()) {
            return null;
        }
        com.bmos.mes.service.inspect.lims.LimsInspectGateway gateway = limsGatewaySelector.current();
        if (gateway == null || CollUtil.isEmpty(inspectInfos)) {
            return null;
        }
        com.bmos.mes.service.inspect.lims.InitiateInspectContext ctx =
                com.bmos.mes.service.inspect.lims.InitiateInspectContext.builder()
                        .platformMaterialId(resolvePlatformMaterialId(inspect.getMaterialId()))
                        .inspectConfigId(inspect.getInspectConfigId())
                        .schemeId(inspect.getSchemeId())
                        .schemeVersionId(inspect.getSchemeVersionId())
                        .materialBatchNo(inspect.getMaterialBatchNo())
                        .inspectInfos(inspectInfos)
                        .build();
        return gateway.initiate(ctx);
    }

    /**
     * MES 物料id → 平台物料id（全系统统一标识，用于与 LIMS 交互）。
     */
    private Long resolvePlatformMaterialId(Long mesMaterialId) {
        ProductMaterial productMaterial = productMaterialService.selectById(mesMaterialId);
        if (Objects.isNull(productMaterial) || Objects.isNull(productMaterial.getPlatformMaterialId())) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        return productMaterial.getPlatformMaterialId();
    }

    private void createMaterialBatch(String materialBatchNo, List<InitiateInspectInfoDTO> inspectInfoDTOS, ProductFormulaMaterial productFormulaMaterial) {
        // 查看物料批次是否存在，若不存在，则直接创建物料批次
        StorageMaterialBatch preStorageMaterialBatch = storageMaterialBatchService.queryMaterialBatchByNoAndMaterialId(productFormulaMaterial.getMaterialId(), materialBatchNo);
        if (Objects.nonNull(preStorageMaterialBatch)) {
            return;
        }
        // 创建物料批次
        ProductMaterial productMaterial = productMaterialService.selectById(productFormulaMaterial.getMaterialId());
        if (Objects.isNull(productMaterial)) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        StorageMaterialBatch storageMaterialBatch = new StorageMaterialBatch();
        storageMaterialBatch.setMaterialId(productMaterial.getId());
        storageMaterialBatch.setMaterialBatchNo(materialBatchNo);
        // 构建物料有效期
        if (!Objects.isNull(productMaterial.getExpandInfo())&&!Objects.isNull(productMaterial.getExpandInfo().getDefaultExpiration())) {
            Integer defaultExpiration = productMaterial.getExpandInfo().getDefaultExpiration();
            TimeUnitEnum timeUnitEnum = CommonEnum.getEnumByValue(TimeUnitEnum.class, productMaterial.getExpandInfo().getTimeUnit());
            storageMaterialBatch.setAvailable(true);
            if (Objects.nonNull(defaultExpiration)) {
                switch (timeUnitEnum) {
                    case HOUR:
                        storageMaterialBatch.setExpiredDate(LocalDateTime.now().plusHours(defaultExpiration.longValue()).toLocalDate());
                        break;
                    case DAY:
                        storageMaterialBatch.setExpiredDate(LocalDate.now().plusDays(defaultExpiration));
                        break;
                    case MONTH:
                        storageMaterialBatch.setExpiredDate(LocalDate.now().plusMonths(defaultExpiration));
                        break;
                    default:
                        break;
                }
                storageMaterialBatch.setAvailable(!LocalDate.now().isAfter(storageMaterialBatch.getExpiredDate()));
            }
        }
        // 判断是否临期提醒
        storageMaterialBatch.setExpireWarningFlag(false);
        if (Objects.nonNull(productMaterial.getDyingPeriod()) && Objects.nonNull(storageMaterialBatch.getExpiredDate())
                && !LocalDate.now().plusDays(productMaterial.getDyingPeriod()).isBefore(storageMaterialBatch.getExpiredDate())) {
            // 判断当前时间 + dyingPeriod 是否大于有效期
            storageMaterialBatch.setExpireWarningFlag(true);
            // 发送临期提醒
            MaterialForeWarningMessage materialForeWarningMessage = new MaterialForeWarningMessage();
            materialForeWarningMessage.setMaterialCode(productMaterial.getMergeCode());
            materialForeWarningMessage.setBatchNo(storageMaterialBatch.getMaterialBatchNo());
            materialForeWarningMessage.setMaterialName(productMaterial.getName());
            materialForeWarningMessage.setTime(LocalDateTime.now());
            messageNotifyFeign.materialExpireForeWarning(materialForeWarningMessage);
        }
        storageMaterialBatch.setUnitId(productFormulaMaterial.getUnitId());
        storageMaterialBatch.setQualityStatus(MaterialQualityStatusEnum.QUARANTINE.getValue());
        storageMaterialBatchService.createMaterialBatch(storageMaterialBatch);

    }

    private boolean doFillInspectResult(Inspect inspect, InspectResultCallBackDTO inspectResultCallBackDTO, List<InspectResult> inspectResults) {
        // 判断是否是最后一次
        boolean close = false;
        if (Objects.equals(CommonEnum.getEnumByValue(InspectResultCloseEnum.class, inspectResultCallBackDTO.getClosed()).getValue(), InspectResultCloseEnum.FINISHED.getValue())) {
            close = true;
        }
        Map<String, InspectResult> inspectResultMap = CollUtil.isNotEmpty(inspectResults) ? CollectionUtils.convertMap(inspectResults, InspectResult::getInspectProgramNo) : Maps.newHashMap();
        List<InspectResult> updateResult = Lists.newArrayList();
        List<InspectResult> addResult = Lists.newArrayList();
        BigDecimal hydration = null;
        BigDecimal noHydrationContent = null;
        String storageMaterialInspectResult = null;
        for (InspectResultItemDTO inspectResultItemDTO : inspectResultCallBackDTO.getInspectResultItemDTOS()) {
            if (close && Objects.isNull(inspect.getInspectResult())) {
                // 只要是最后一次 所有的检验项汇总结果都是一样的
                inspect.setInspectResult(CommonEnum.getEnumByName(MaterialQualityStatusEnum.class, inspectResultCallBackDTO.getResult()));
                inspect.setStatus(InspectStatusEnum.FINISHED);
                storageMaterialInspectResult = inspect.getInspectResult().getValue();
            }
            if (StrUtil.equals(inspectResultItemDTO.getAlreadyConvertProgramNo(), InspectStorageMaterialCodeEnum.HYDRATION.getValue()) && StrUtil.isNotEmpty(inspectResultItemDTO.getInspectResult())) {
                // 代表水分 可能含有%
                String hydrationStr = inspectResultItemDTO.getInspectResult().replace("%", "");
                // 确定转换后为数字
                hydration = BigDecimalFormatUtil.convertBigDecimal(hydrationStr);
            }
            if (StrUtil.equals(inspectResultItemDTO.getAlreadyConvertProgramNo(), InspectStorageMaterialCodeEnum.NO_HYDRATION_CONTENT.getValue()) && StrUtil.isNotEmpty(inspectResultItemDTO.getInspectResult())) {
                // 代表无水含量 可能含有%
                String noHydrationContentStr = inspectResultItemDTO.getInspectResult().replace("%", "");
                // 确定转换后为数字
                noHydrationContent = BigDecimalFormatUtil.convertBigDecimal(noHydrationContentStr);
            }
            // 获取检验项结果
            InspectResult inspectResult = inspectResultMap.get(inspectResultItemDTO.getInspectProgramNo());
            if (Objects.nonNull(inspectResult)) {
                inspectResult.setInspectConclusion(CommonEnum.getEnumByName(InspectProgramResultEnum.class, inspectResultItemDTO.getInspectConclusion()));
                inspectResult.setInspectResult(inspectResultItemDTO.getInspectResult());
                inspectResult.setInspectProgramName(inspectResultItemDTO.getInspectProgramName());
                updateResult.add(inspectResult);
                continue;
            }
            addResult.add(InspectConvert.INSTANCE.convert2InspectResult(inspectResultItemDTO, inspect));
        }
        if (CollUtil.isNotEmpty(updateResult)) {
            inspectResultMapper.updateBatch(updateResult);
        }
        if (CollUtil.isNotEmpty(addResult)) {
            inspectResultMapper.insertBatch(addResult);
        }
        this.updateStorageMaterialBatch(inspect, hydration, noHydrationContent, storageMaterialInspectResult);
        return close;
    }

    private void updateStorageMaterialBatch(Inspect inspect, BigDecimal hydration, BigDecimal noHydrationContent, String storageMaterialInspectResult) {
        if (Objects.isNull(hydration) && Objects.isNull(noHydrationContent) && Objects.isNull(storageMaterialInspectResult)) {
            return;
        }
        ProductFormulaMaterial formulaMaterial = productFormulaConfigureService.selectById(inspect.getFormulaMaterialId());
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.queryMaterialBatchByNoAndMaterialId(formulaMaterial.getMaterialId(), inspect.getMaterialBatchNo());
        if (Objects.isNull(storageMaterialBatch)) {
            return;
        }
        if (Objects.nonNull(hydration)) {
            storageMaterialBatch.setHydration(hydration);
        }
        if (Objects.nonNull(noHydrationContent)) {
            storageMaterialBatch.setNoHydrationContent(noHydrationContent);
        }
        if (Objects.nonNull(storageMaterialInspectResult)) {
            storageMaterialBatch.setQualityStatus(storageMaterialInspectResult);
        }
        storageMaterialBatchService.updateById(storageMaterialBatch);
    }

}
