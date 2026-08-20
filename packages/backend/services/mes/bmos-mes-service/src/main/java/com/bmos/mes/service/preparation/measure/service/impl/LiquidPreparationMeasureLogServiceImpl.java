package com.bmos.mes.service.preparation.measure.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.preparation.measure.convert.LiquidPreparationMeasureConverter;
import com.bmos.mes.service.preparation.measure.dto.LiquidMeasureLogPageQueryDTO;
import com.bmos.mes.service.preparation.measure.dto.LiquidPreparationMeasureLogSaveDTO;
import com.bmos.mes.service.preparation.measure.mapper.LiquidPreparationMeasureLogMapper;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureLog;
import com.bmos.mes.service.preparation.measure.service.LiquidPreparationMeasureLogService;
import com.bmos.mes.service.preparation.measure.vo.LiquidMeasureLogPageVO;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LiquidPreparationMeasureLogServiceImpl implements LiquidPreparationMeasureLogService {

    @Autowired
    private LiquidPreparationMeasureLogMapper logMapper;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private ProductMaterialMapper materialMapper;

    @Override
    public void saveLog(LiquidPreparationMeasureLogSaveDTO dto) {
        LiquidPreparationMeasureLog log = LiquidPreparationMeasureConverter.INSTANCE.convertToLog(dto);
        log.setId(IdUtils.getSnowflake());
        log.setUnitName(unitCache.getGlobalUnitName(log.getUnitId()));
        log.setMeasureTime(LocalDateTime.now());
        handleUser(log);
        handlePlanInfo(dto, log);
        handleMaterialInfo(dto, log);
        logMapper.insert(log);
    }

    @Override
    public CommonPage<LiquidMeasureLogPageVO> queryMeasureLogPage(LiquidMeasureLogPageQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<LiquidPreparationMeasureLog> list = logMapper.selectPageByParam(dto);
        return CommonPage.convertPage(list, LiquidPreparationMeasureConverter.INSTANCE::convertToLogPage);
    }

    private void handleMaterialInfo(LiquidPreparationMeasureLogSaveDTO dto, LiquidPreparationMeasureLog log) {
        if (dto.getMaterialId() != null && (dto.getMaterialName() == null || dto.getMaterialMergeCode() == null)) {
            ProductMaterial productMaterial =
                    Optional.ofNullable(materialMapper.selectAllInfoById(dto.getMaterialId())).orElse(new ProductMaterial());
            log.setMaterialName(productMaterial.getName());
            log.setMaterialMergeCode(productMaterial.getMergeCode());
        }
    }

    private void handlePlanInfo(LiquidPreparationMeasureLogSaveDTO dto, LiquidPreparationMeasureLog log) {
        if (dto.getProductPlanId() != null
                && (dto.getProductBatchNo() == null || dto.getProductName() == null
                || dto.getProductMergeCode() == null)) {
            Plan plan = planMapper.selectById(dto.getProductPlanId());
            log.setProductId(plan.getProductId());
            log.setProductBatchNo(plan.getBatchNo());
            log.setProductName(plan.getProductName());
            log.setProductMergeCode(plan.getProductMergeCode());
        }
    }

    private static void handleUser(LiquidPreparationMeasureLog log) {
        BaseUserDO measurer = UserUtils.getUser(log.getMeasurerId());
        log.setMeasurerName(measurer.getUserName());
        log.setMeasurerLoginName(measurer.getLoginName());
        BaseUserDO reChecker = UserUtils.getUser(log.getReCheckerId());
        log.setReCheckerName(reChecker.getUserName());
        log.setReCheckerLoginName(reChecker.getLoginName());
    }
}
