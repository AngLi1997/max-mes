package com.bmos.mes.service.ingredient.weigh.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.ingredient.weigh.convert.IngredientWeighProcessConvert;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogQueryDTO;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.mapper.WeighLogMapper;
import com.bmos.mes.service.ingredient.weigh.model.WeighLog;
import com.bmos.mes.service.ingredient.weigh.service.WeighLogService;
import com.bmos.mes.service.ingredient.weigh.vo.WeighLogPageVO;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeighLogServiceImpl implements WeighLogService {

    @Resource
    private WeighLogMapper weighLogMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private PlanService planService;

    @Override
    public CommonPage<WeighLogPageVO> queryWeighLogPage(WeighLogQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return CommonPage.convertPage(weighLogMapper.selectPageList(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(WeighLogSaveDTO dto) {
        WeighLog log = IngredientWeighProcessConvert.INSTANCE.convertToLog(dto);
        String unitName = unitCache.getGlobalUnitName(log.getUnitId());
        log.setUnitName(unitName);
        BaseUserDO user = UserUtils.getUser(dto.getWeigherId());
        BaseUserDO recheckUser = UserUtils.getUser(dto.getReCheckerId());
        if (user != null){
            log.setWeigherName(user.getUserName());
            log.setWeigherLoginName(user.getLoginName());
        }
        if (recheckUser != null){
            log.setReCheckerName(recheckUser.getUserName());
            log.setReCheckerLoginName(recheckUser.getLoginName());
        }
        if(StrUtil.isEmpty(log.getProductBatchNo())){
            if (dto.getProductPlanId() != null){
                Plan plan = planService.getById(dto.getProductPlanId());
                log.setProductName(plan.getProductName());
                log.setProductMergeCode(plan.getProductMergeCode());
                log.setProductBatchNo(plan.getBatchNo());
            }
        }
        if (dto.getGrossWeight() != null) {
            log.setGrossWeight(unitCache.toExt(dto.getGrossWeight(), dto.getUnitId()).stripTrailingZeros());
        }
        if (dto.getNetWeight() != null) {
            log.setNetWeight(unitCache.toExt(dto.getNetWeight(), dto.getUnitId()).stripTrailingZeros());
        }
        if (dto.getTareWeight() != null){
            log.setTareWeight(unitCache.toExt(dto.getTareWeight(), dto.getUnitId()).stripTrailingZeros());
        }
        weighLogMapper.insert(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLogs(List<WeighLogSaveDTO> dtos) {
        List<WeighLog> list = new ArrayList<>();
        for (WeighLogSaveDTO dto : dtos) {
            WeighLog log = IngredientWeighProcessConvert.INSTANCE.convertToLog(dto);
            String unitName = unitCache.getGlobalUnitName(log.getUnitId());
            log.setUnitName(unitName);
            BaseUserDO user = UserUtils.getUser(dto.getWeigherId());
            BaseUserDO recheckUser = UserUtils.getUser(dto.getReCheckerId());
            if (user != null){
                log.setWeigherName(user.getUserName());
                log.setWeigherLoginName(user.getLoginName());
            }
            if (recheckUser != null){
                log.setReCheckerName(recheckUser.getUserName());
                log.setReCheckerLoginName(recheckUser.getLoginName());
            }
            if(StrUtil.isEmpty(log.getProductBatchNo())){
                Plan plan = planService.getById(dto.getProductPlanId());
                log.setProductName(plan.getProductName());
                log.setProductMergeCode(plan.getProductMergeCode());
                log.setProductBatchNo(plan.getBatchNo());
            }
            if (dto.getGrossWeight() != null) {
                log.setGrossWeight(unitCache.toExt(dto.getGrossWeight(), dto.getUnitId()).stripTrailingZeros());
            }
            if (dto.getNetWeight() != null) {
                log.setNetWeight(unitCache.toExt(dto.getNetWeight(), dto.getUnitId()).stripTrailingZeros());
            }
            if (dto.getTareWeight() != null){
                log.setTareWeight(unitCache.toExt(dto.getTareWeight(), dto.getUnitId()).stripTrailingZeros());
            }
            list.add(log);
        }
        if (CollectionUtil.isNotEmpty(list)){
            weighLogMapper.insertBatch(list);
        }
    }
}
