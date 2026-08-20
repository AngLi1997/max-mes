package com.bmos.wms.service.inspect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseItem;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.common.enums.inspect.InspectProgramResultEnum;
import com.bmos.wms.common.enums.inspect.InspectResultCloseEnum;
import com.bmos.wms.common.enums.inspect.InspectStatusEnum;
import com.bmos.wms.common.enums.inspect.InspectStorageMaterialCodeEnum;
import com.bmos.wms.common.enums.inspect.MaterialQualityStatusEnum;
import com.bmos.wms.common.exception.WmsResponseCode;
import com.bmos.wms.inspect.dto.InspectRejectDTO;
import com.bmos.wms.inspect.dto.InspectResultCallBackDTO;
import com.bmos.wms.inspect.dto.InspectResultItemDTO;
import com.bmos.wms.service.cargo.mapper.ICargoMapper;
import com.bmos.wms.service.cargo.model.Cargo;
import com.bmos.wms.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectInfoVO;
import com.bmos.wms.service.inspect.controller.vo.InspectPageVO;
import com.bmos.wms.service.inspect.controller.vo.InspectProgramResultVO;
import com.bmos.wms.service.inspect.controller.vo.InspectSchemeVO;
import com.bmos.wms.service.inspect.convert.InspectConvert;
import com.bmos.wms.service.inspect.lims.InitiateInspectContext;
import com.bmos.wms.service.inspect.lims.LimsGatewaySelector;
import com.bmos.wms.service.inspect.lims.LimsInspectGateway;
import com.bmos.wms.service.inspect.lims.RetryInspectContext;
import com.bmos.wms.service.inspect.mapper.InspectInfoMapper;
import com.bmos.wms.service.inspect.mapper.InspectMapper;
import com.bmos.wms.service.inspect.mapper.InspectResultMapper;
import com.bmos.wms.service.inspect.model.Inspect;
import com.bmos.wms.service.inspect.model.InspectInfo;
import com.bmos.wms.service.inspect.model.InspectResult;
import com.bmos.wms.service.inspect.service.IInspectService;
import com.bmos.wms.service.inspect.service.dto.InitiateInspectDTO;
import com.bmos.wms.service.inspect.service.dto.InitiateInspectInfoDTO;
import com.bmos.wms.service.inspect.service.dto.InitiateRetryInspectDTO;
import com.bmos.wms.service.inspect.service.dto.InspectPageDTO;
import com.bmos.wms.service.inventory.mapper.IInventoryBatchMapper;
import com.bmos.wms.service.inventory.model.InventoryBatch;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * WMS 请验主服务实现（严格 mirror MES InspectServiceImpl 行为）。
 *
 * <p>关键约束：
 * <ul>
 *   <li>状态机两条线：{@code Inspect.status} (Integer) 与 {@code InventoryBatch.qualityStatus} (String)。</li>
 *   <li>callback 只写 hydration / noHydrationContent / qualityStatus，永不动 available。</li>
 *   <li>不在业务层拦截重复 PENDING（与 MES 一致，仅靠分布式锁防并发）。</li>
 * </ul>
 */
@Service
@Slf4j
public class InspectServiceImpl implements IInspectService {

    @Resource
    private InspectMapper inspectMapper;
    @Resource
    private InspectInfoMapper inspectInfoMapper;
    @Resource
    private InspectResultMapper inspectResultMapper;
    @Resource
    private IInventoryBatchMapper inventoryBatchMapper;
    @Resource
    private ICargoMapper cargoMapper;
    @Resource
    private LimsGatewaySelector limsGatewaySelector;

    // ====================== 发起 ======================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String initiateInspect(InitiateInspectDTO dto) {
        // 1) 校验开关 & 拿网关
        LimsInspectGateway gateway = limsGatewaySelector.require();

        // 2) 批次 + 货品（平台物料id）由 inventoryBatchId 反查
        BatchCargo bc = resolveBatchCargo(dto.getInventoryBatchId());
        InventoryBatch batch = bc.batch;
        Cargo cargo = bc.cargo;

        // 3) 校验请验单字段
        validateInspectInfo(dto.getInitiateInspectInfoDTOList());

        // 4) 落 Inspect
        Inspect inspect = buildInspect(dto, batch, cargo);
        inspectMapper.insert(inspect);

        // 5) 落 InspectInfo
        List<InspectInfo> inspectInfos = toInspectInfos(dto.getInitiateInspectInfoDTOList(), inspect.getId());
        if (CollUtil.isNotEmpty(inspectInfos)) {
            inspectInfoMapper.insertBatch(inspectInfos);
        }

        // 6) 调网关下发
        InitiateInspectContext ctx = InitiateInspectContext.builder()
                .platformMaterialId(cargo.getPlatformMaterialId())
                .inspectConfigId(dto.getInspectConfigId())
                .schemeId(dto.getSchemeId())
                .schemeVersionId(dto.getSchemeVersionId())
                .materialBatchNo(batch.getBatchNo())
                .inspectInfos(inspectInfos)
                .build();
        String inspectNo = gateway.initiate(ctx);

        // 7) 回写 inspectNo
        if (StrUtil.isNotBlank(inspectNo)) {
            inspect.setInspectNo(inspectNo);
            inspectMapper.updateById(inspect);
        }

        // mirror MES：不动 batch.qualityStatus（保持 QUARANTINE 等回调）
        return inspectNo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String retryInitiateInspect(InitiateRetryInspectDTO dto) {
        LimsInspectGateway gateway = limsGatewaySelector.require();

        Inspect origin = inspectMapper.selectById(dto.getId());
        if (Objects.isNull(origin)) {
            throw new BmosException(ResponseItem.from(83_11_005, "原检验单不存在", "bmosWms"));
        }
        if (Objects.equals(origin.getStatus(), InspectStatusEnum.FINISHED)) {
            throw new BmosException(ResponseItem.from(83_11_006, "已完成的检验单不允许重新发起", "bmosWms"));
        }

        // 作废原单
        origin.setStatus(InspectStatusEnum.REJECTED);
        if (StrUtil.isNotBlank(dto.getReason())) {
            origin.setReason(dto.getReason());
        }
        inspectMapper.updateById(origin);

        // 批次 + 货品由原单的 batchId 反查（重发起针对同一批次）
        BatchCargo bc = resolveBatchCargo(origin.getBatchId());
        InventoryBatch batch = bc.batch;
        Cargo cargo = bc.cargo;

        // 字段：dto 提供了就用新的，否则沿用原单
        List<InitiateInspectInfoDTO> infoDTOs = dto.getInitiateInspectInfoDTOList();
        if (CollUtil.isEmpty(infoDTOs)) {
            // 复用原单字段
            List<InspectInfo> oldInfos = inspectInfoMapper.selectByInspectId(origin.getId());
            infoDTOs = oldInfos.stream().map(this::infoToDto).collect(Collectors.toList());
        } else {
            validateInspectInfo(infoDTOs);
        }

        // 新建 inspect（沿用 origin 大部分字段，重置状态 / 时间 / inspectNo）
        Inspect retry = buildRetryInspect(dto, origin, batch, cargo);
        inspectMapper.insert(retry);

        List<InspectInfo> inspectInfos = toInspectInfos(infoDTOs, retry.getId());
        if (CollUtil.isNotEmpty(inspectInfos)) {
            inspectInfoMapper.insertBatch(inspectInfos);
        }

        // 下发 LIMS（方案 B：携带原 inspectNo 由 LIMS 作废）
        RetryInspectContext ctx = RetryInspectContext.builder()
                .originInspectNo(origin.getInspectNo())
                .platformMaterialId(cargo.getPlatformMaterialId())
                .inspectConfigId(dto.getInspectConfigId())
                .schemeId(dto.getSchemeId())
                .schemeVersionId(dto.getSchemeVersionId())
                .materialBatchNo(batch.getBatchNo())
                .inspectInfos(inspectInfos)
                .build();
        String newOrderNo = gateway.retry(ctx);
        if (StrUtil.isNotBlank(newOrderNo)) {
            retry.setInspectNo(newOrderNo);
            inspectMapper.updateById(retry);
        }
        return newOrderNo;
    }

    // ====================== 查询 ======================

    @Override
    public CommonPage<InspectPageVO> queryPage(InspectPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<Inspect> list = inspectMapper.selectList(Wrappers.lambdaQuery(Inspect.class)
                .eq(Objects.nonNull(dto.getInventoryBatchId()), Inspect::getBatchId, dto.getInventoryBatchId())
                .eq(StrUtil.isNotBlank(dto.getInspectNo()), Inspect::getInspectNo, dto.getInspectNo())
                .eq(Objects.nonNull(dto.getStatus()), Inspect::getStatus, dto.getStatus() == null ? null : InspectStatusEnumLookup.byCode(dto.getStatus()))
                .orderByDesc(Inspect::getCreateTime));
        return CommonPage.convertPage(list, InspectConvert.INSTANCE::toPageVO);
    }

    @Override
    public InspectDetailVO queryDetail(Long id) {
        Inspect inspect = inspectMapper.selectById(id);
        if (Objects.isNull(inspect)) {
            throw new BmosException(ResponseItem.from(83_11_005, "检验单不存在", "bmosWms"));
        }
        InspectDetailVO vo = InspectConvert.INSTANCE.toDetailVO(inspect);
        List<InspectInfo> infos = inspectInfoMapper.selectByInspectId(id);
        List<InspectInfoVO> infoVOs = InspectConvert.INSTANCE.toInfoVOList(infos);
        vo.setInspectInfoVOList(infoVOs);
        List<InspectResult> results = inspectResultMapper.selectByInspectId(id);
        List<InspectProgramResultVO> resultVOs = InspectConvert.INSTANCE.toProgramVOList(results);
        vo.setInspectProgramResultVOList(resultVOs);
        return vo;
    }

    @Override
    public List<InspectPageVO> queryHistory(Long inventoryBatchId) {
        List<Inspect> list = inspectMapper.selectByBatchId(inventoryBatchId);
        return InspectConvert.INSTANCE.toPageVO(list);
    }

    @Override
    public List<InspectConfigDetailVO> queryConfigByBatchId(Long inventoryBatchId) {
        LimsInspectGateway gateway = limsGatewaySelector.require();
        Long platformMaterialId = resolveBatchCargo(inventoryBatchId).cargo.getPlatformMaterialId();
        return gateway.queryConfig(platformMaterialId);
    }

    @Override
    public List<InspectSchemeVO> querySchemesByBatchId(Long inventoryBatchId) {
        LimsInspectGateway gateway = limsGatewaySelector.require();
        Long platformMaterialId = resolveBatchCargo(inventoryBatchId).cargo.getPlatformMaterialId();
        return gateway.querySchemes(platformMaterialId);
    }

    /**
     * 库存批次id → (批次 + 货品)。统一在这里把 货品id / 平台物料id 反查出来：
     * 库存批次列表接口只返回批次id，故所有发起 / 查询入口都以批次id为准绕道查询。
     */
    private BatchCargo resolveBatchCargo(Long inventoryBatchId) {
        if (Objects.isNull(inventoryBatchId)) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        InventoryBatch batch = inventoryBatchMapper.selectById(inventoryBatchId);
        if (Objects.isNull(batch)) {
            throw new BmosException(WmsResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        Cargo cargo = cargoMapper.selectById(batch.getCargoId());
        if (Objects.isNull(cargo)) {
            throw new BmosException(WmsResponseCode.CARGO_NOT_EXIST);
        }
        if (Objects.isNull(cargo.getPlatformMaterialId())) {
            throw new BmosException(ResponseItem.from(83_11_004, "货品未关联平台物料，无法发起检验", "bmosWms"));
        }
        return new BatchCargo(batch, cargo);
    }

    /** 批次 + 货品 解析结果。 */
    private static final class BatchCargo {
        private final InventoryBatch batch;
        private final Cargo cargo;

        private BatchCargo(InventoryBatch batch, Cargo cargo) {
            this.batch = batch;
            this.cargo = cargo;
        }
    }

    // ====================== 回传（被 LIMS 反向调用） ======================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void inspectCallback(InspectResultCallBackDTO dto) {
        if (CollUtil.isEmpty(dto.getInspectResultItemDTOS())) {
            return;
        }
        Inspect inspect = inspectMapper.selectByInspectNo(dto.getInspectNo());
        if (Objects.isNull(inspect)) {
            log.warn("WMS 收到 LIMS 回传，但本地未找到 inspectNo={}，忽略", dto.getInspectNo());
            return;
        }
        if (!Objects.equals(inspect.getStatus(), InspectStatusEnum.PENDING)) {
            log.info("WMS 检验单 {} 状态非 PENDING，忽略回传", dto.getInspectNo());
            return;
        }

        List<InspectResult> existing = inspectResultMapper.selectByInspectId(inspect.getId());
        Map<String, InspectResult> existingMap = CollUtil.isNotEmpty(existing)
                ? CollectionUtils.convertMap(existing, InspectResult::getInspectProgramNo)
                : Maps.newHashMap();

        boolean closed = StrUtil.equals(InspectResultCloseEnum.FINISHED.getValue(), dto.getClosed());

        BigDecimal hydration = null;
        BigDecimal noHydrationContent = null;
        String batchQualityStatus = null;

        List<InspectResult> updateList = Lists.newArrayList();
        List<InspectResult> insertList = Lists.newArrayList();
        for (InspectResultItemDTO item : dto.getInspectResultItemDTOS()) {
            // 仅最后一次回写汇总
            if (closed && Objects.isNull(inspect.getInspectResult())) {
                MaterialQualityStatusEnum quality = CommonEnum.getEnumByName(MaterialQualityStatusEnum.class, dto.getResult());
                inspect.setInspectResult(quality);
                inspect.setStatus(InspectStatusEnum.FINISHED);
                batchQualityStatus = Objects.nonNull(quality) ? quality.getValue() : null;
            }
            // 抽取水分 / 无水含量
            if (StrUtil.equals(item.getAlreadyConvertProgramNo(), InspectStorageMaterialCodeEnum.HYDRATION.getValue())
                    && StrUtil.isNotEmpty(item.getInspectResult())) {
                hydration = parseDecimal(item.getInspectResult());
            }
            if (StrUtil.equals(item.getAlreadyConvertProgramNo(), InspectStorageMaterialCodeEnum.NO_HYDRATION_CONTENT.getValue())
                    && StrUtil.isNotEmpty(item.getInspectResult())) {
                noHydrationContent = parseDecimal(item.getInspectResult());
            }

            InspectResult exist = existingMap.get(item.getInspectProgramNo());
            if (Objects.nonNull(exist)) {
                exist.setInspectConclusion(safeConclusion(item.getInspectConclusion()));
                exist.setInspectResult(item.getInspectResult());
                exist.setInspectProgramName(item.getInspectProgramName());
                updateList.add(exist);
            } else {
                InspectResult ir = new InspectResult();
                ir.setInspectId(inspect.getId());
                ir.setInspectProgramNo(item.getInspectProgramNo());
                ir.setInspectProgramName(item.getInspectProgramName());
                ir.setInspectResult(item.getInspectResult());
                ir.setInspectDictNo(item.getAlreadyConvertProgramNo());
                ir.setInspectConclusion(safeConclusion(item.getInspectConclusion()));
                insertList.add(ir);
            }
        }
        if (CollUtil.isNotEmpty(updateList)) {
            inspectResultMapper.updateBatch(updateList);
        }
        if (CollUtil.isNotEmpty(insertList)) {
            inspectResultMapper.insertBatch(insertList);
        }

        if (closed) {
            inspectMapper.updateById(inspect);
        }
        // 回写 inventory 批次（mirror MES updateStorageMaterialBatch：仅写 hydration / noHydrationContent / qualityStatus）
        updateInventoryBatch(inspect, hydration, noHydrationContent, batchQualityStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectInspect(List<InspectRejectDTO> dtoList) {
        if (CollUtil.isEmpty(dtoList)) {
            return;
        }
        Map<String, InspectRejectDTO> noToDto = CollectionUtils.convertMap(dtoList, InspectRejectDTO::getInspectNo);
        List<Inspect> list = inspectMapper.selectByInspectNoList(noToDto.keySet());
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (Inspect inspect : list) {
            InspectRejectDTO d = noToDto.get(inspect.getInspectNo());
            inspect.setStatus(InspectStatusEnum.REJECTED);
            inspect.setReason(d == null ? null : d.getReason());
        }
        inspectMapper.updateBatch(list);
        // mirror MES：reject 不修改 inventory_batch.quality_status
    }

    // ====================== 辅助 ======================

    private void updateInventoryBatch(Inspect inspect, BigDecimal hydration, BigDecimal noHydrationContent, String qualityStatus) {
        if (Objects.isNull(hydration) && Objects.isNull(noHydrationContent) && StrUtil.isBlank(qualityStatus)) {
            return;
        }
        InventoryBatch batch = inventoryBatchMapper.selectByCargoIdAndBatchNo(inspect.getCargoId(), inspect.getMaterialBatchNo());
        if (Objects.isNull(batch)) {
            log.warn("WMS 检验回传时批次已不存在：cargoId={}, batchNo={}", inspect.getCargoId(), inspect.getMaterialBatchNo());
            return;
        }
        if (Objects.nonNull(hydration)) {
            batch.setHydration(hydration);
        }
        if (Objects.nonNull(noHydrationContent)) {
            batch.setNoHydrationContent(noHydrationContent);
        }
        if (StrUtil.isNotBlank(qualityStatus)) {
            batch.setQualityStatus(qualityStatus);
        }
        // 不动 available（mirror MES）
        inventoryBatchMapper.updateById(batch);
    }

    private void validateInspectInfo(List<InitiateInspectInfoDTO> list) {
        if (CollUtil.isEmpty(list)) {
            throw new BmosException(ResponseItem.from(83_11_007, "请验单字段不能为空", "bmosWms"));
        }
        for (InitiateInspectInfoDTO d : list) {
            if (Boolean.TRUE.equals(d.getRequired()) && StrUtil.isBlank(d.getValue())) {
                throw new BmosException(ResponseItem.from(83_11_008, "请验单字段[" + d.getShowName() + "]不能为空", "bmosWms"));
            }
        }
    }

    private Inspect buildInspect(InitiateInspectDTO dto, InventoryBatch batch, Cargo cargo) {
        SysUser user = SysUserHolder.getUser();
        Inspect inspect = new Inspect();
        inspect.setStatus(InspectStatusEnum.PENDING);
        inspect.setInspectConfigId(dto.getInspectConfigId());
        inspect.setSchemeId(dto.getSchemeId());
        inspect.setSchemeVersionId(dto.getSchemeVersionId());
        inspect.setCargoId(cargo.getId());
        inspect.setBatchId(batch.getId());
        inspect.setCargoName(cargo.getCargoName());
        inspect.setMergeCode(cargo.getMergeCode());
        inspect.setMaterialBatchNo(batch.getBatchNo());
        inspect.setFactoryBatchNo(batch.getFactoryBatchNo());
        inspect.setUnitId(batch.getUnitId());
        inspect.setInspectTime(LocalDateTime.now());
        if (Objects.nonNull(user)) {
            inspect.setInspectorId(user.getUserId());
            inspect.setInspector(StrUtil.format("{}-{}", user.getUserName(), user.getLoginName()));
        }
        return inspect;
    }

    private Inspect buildRetryInspect(InitiateRetryInspectDTO dto, Inspect origin, InventoryBatch batch, Cargo cargo) {
        SysUser user = SysUserHolder.getUser();
        Inspect retry = new Inspect();
        retry.setStatus(InspectStatusEnum.PENDING);
        retry.setInspectConfigId(dto.getInspectConfigId());
        retry.setSchemeId(dto.getSchemeId());
        retry.setSchemeVersionId(dto.getSchemeVersionId());
        retry.setCargoId(cargo.getId());
        retry.setBatchId(batch.getId());
        retry.setCargoName(cargo.getCargoName());
        retry.setMergeCode(cargo.getMergeCode());
        retry.setMaterialBatchNo(batch.getBatchNo());
        retry.setFactoryBatchNo(batch.getFactoryBatchNo());
        retry.setUnitId(batch.getUnitId());
        retry.setReason(dto.getReason());
        retry.setInspectTime(LocalDateTime.now());
        if (Objects.nonNull(user)) {
            retry.setInspectorId(user.getUserId());
            retry.setInspector(StrUtil.format("{}-{}", user.getUserName(), user.getLoginName()));
        }
        return retry;
    }

    private List<InspectInfo> toInspectInfos(List<InitiateInspectInfoDTO> list, Long inspectId) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<InspectInfo> result = new ArrayList<>(list.size());
        for (InitiateInspectInfoDTO d : list) {
            InspectInfo info = new InspectInfo();
            info.setInspectId(inspectId);
            info.setInspectConfigDataId(d.getInspectConfigDataId());
            info.setCode(d.getCode());
            info.setShowName(d.getShowName());
            info.setDataName(d.getDataName());
            info.setRequired(d.getRequired());
            info.setValue(d.getValue());
            info.setSort(d.getSort());
            result.add(info);
        }
        return result;
    }

    private InitiateInspectInfoDTO infoToDto(InspectInfo info) {
        InitiateInspectInfoDTO d = new InitiateInspectInfoDTO();
        d.setInspectConfigDataId(info.getInspectConfigDataId());
        d.setCode(info.getCode());
        d.setShowName(info.getShowName());
        d.setDataName(info.getDataName());
        d.setRequired(info.getRequired());
        d.setValue(info.getValue());
        d.setSort(info.getSort());
        return d;
    }

    private static String safeConclusion(String raw) {
        InspectProgramResultEnum e = CommonEnum.getEnumByName(InspectProgramResultEnum.class, raw);
        return Objects.isNull(e) ? raw : e.getValue();
    }

    private static BigDecimal parseDecimal(String raw) {
        try {
            return new BigDecimal(raw.replace("%", "").trim());
        } catch (Exception e) {
            return null;
        }
    }

    /** 状态枚举到 code 的小工具，避免在 Wrapper 里写 {@code .eq(status, dto.status)} 时 enum/int 类型不匹配。 */
    static final class InspectStatusEnumLookup {
        static InspectStatusEnum byCode(Integer code) {
            if (code == null) {
                return null;
            }
            for (InspectStatusEnum e : InspectStatusEnum.values()) {
                if (Objects.equals(e.getValue(), code)) {
                    return e;
                }
            }
            return null;
        }
    }
}
