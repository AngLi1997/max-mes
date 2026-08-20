package com.bmos.mes.service.execute.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.execute.convert.ExecuteRecordCopyConverter;
import com.bmos.mes.service.execute.dto.*;
import com.bmos.mes.service.execute.mapper.ExecuteRecordCopyMapper;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.service.ExecuteRecordCopyService;
import com.bmos.mes.service.execute.vo.ChangeTeamRecordCopyChangeTeamVO;
import com.bmos.mes.service.execute.vo.CopyRecordItemVO;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExecuteRecordCopyServiceImpl implements ExecuteRecordCopyService {

    @Autowired
    private ExecuteRecordCopyMapper executionRecordCopyMapper;

    @Resource
    @Lazy
    private BusinessComponentManager businessComponentManager;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecuteRecordCopy copyRecordItem(RecordCopySaveDTO dto) {
        Long maxVersion = executionRecordCopyMapper.getMaxVersion(dto);
        maxVersion = ObjectUtil.isNull(maxVersion) ? 0 : maxVersion + 1;
        ExecuteRecordCopy executeRecordCopy = ExecuteRecordCopyConverter.INSTANCE.convert(dto, maxVersion);
        //如果复用，记录工序步骤id 0
        if (executeRecordCopy.getReuse()) {
            executeRecordCopy.setProcedureStepId(0L);
        }
        executionRecordCopyMapper.insert(executeRecordCopy);

        businessComponentManager.copyComponentInstance(dto.getProductPlanId(),
                dto.getProcedureStepId(),
                dto.getRecordItemId(),
                dto.getRecordVersionId(),
                dto.getReuse(),
                dto.getCopyVersion(), maxVersion);
        return executeRecordCopy;
    }

    @Override
    public List<CopyRecordItemVO> getCopyVersionList(RecordCopyQueryDTO dto) {
        return executionRecordCopyMapper.getCopyVersionList(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ExecuteRecordCopy copy) {
        executionRecordCopyMapper.insert(copy);
    }

    @Override
    public Boolean existCopy(FormDataBatchSaveDTO dto) {
        return executionRecordCopyMapper.existsCopy(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void discardRecordItem(FormDataDiscardDTO dto) {
        executionRecordCopyMapper.discardRecordItem(dto);
    }

    @Override
    public List<ExecuteRecordCopy> getList(Long productPlanId, Set<Long> stepIds) {
        return executionRecordCopyMapper.selectListBySteps(productPlanId, stepIds);
    }

    @Override
    public List<ExecuteRecordCopy> getCurrentStepCopies(CopiesQueryDTO build) {
        return executionRecordCopyMapper.selectCurrentStepCopies(build);
    }

    @Override
    public List<ExecuteRecordCopy> getListByRecordItemIds(Long productPlanId, Collection<Long> recordItems) {
        if (CollUtil.isEmpty(recordItems)) {
            return new ArrayList<>();
        }
        return executionRecordCopyMapper.selectByRecordItemIds(productPlanId, recordItems);
    }

    @Override
    public List<ExecuteRecordCopy> getListByRecordVersion(Long productPlanId, Long recordVersionId) {
        return executionRecordCopyMapper.selectByRecordVersionId(productPlanId, recordVersionId);
    }

    @Override
    public Long getVersionMaxValue(RecordCopyQueryDTO dto) {
        return executionRecordCopyMapper.getMaxVersion(BeanUtil.toBean(dto,RecordCopySaveDTO.class));
    }

    @Override
    public List<ChangeTeamRecordCopyChangeTeamVO> queryStepChangeTeamList(ProcedureStepChangeNumberQueryDTO dto) {
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        RecordCopyQueryDTO copyQueryDTO = new RecordCopyQueryDTO();
        copyQueryDTO.setProductPlanId(dto.getProductPlanId());
        copyQueryDTO.setReuse(procedureStepModel.getReusable());
        copyQueryDTO.setRecordItemId(procedureStepModel.getRecordItemId());
        copyQueryDTO.setProcedureStepId(procedureStepModel.getProcedureStepId());
        List<CopyRecordItemVO> copyVersionList = executionRecordCopyMapper.getCopyVersionList(copyQueryDTO);
        if (CollUtil.isEmpty(copyVersionList)) {
            return new ArrayList<>();
        }
        Map<Integer, List<CopyRecordItemVO>> processNumberMap = CollectionUtils.convertMultiMap(copyVersionList,
                CopyRecordItemVO::getProcessChangeNumber);
        // 先按工艺换班次数分组 再按工序换班次数分组
        List<ChangeTeamRecordCopyChangeTeamVO> result = processNumberMap.keySet().stream().sorted().map(e -> {
            ChangeTeamRecordCopyChangeTeamVO vo = new ChangeTeamRecordCopyChangeTeamVO();
            vo.setProcessChangeNumber(e);
            List<CopyRecordItemVO> copyRecordItemVOS = processNumberMap.getOrDefault(e, new ArrayList<>());
            Map<Integer, List<Long>> procedureNumberMap =
                    CollectionUtils.convertMultiMap(copyRecordItemVOS, CopyRecordItemVO::getProcedureChangeNumber,
                            CopyRecordItemVO::getVersion);
            vo.setProcedureChangeList(procedureNumberMap.keySet().stream().sorted().map(i -> {
                ChangeTeamRecordCopyChangeTeamVO.ProcedureChangeVO pVO =
                        new ChangeTeamRecordCopyChangeTeamVO.ProcedureChangeVO();
                pVO.setProcedureChangeNumber(i);
                pVO.setCopyVersionList(procedureNumberMap.getOrDefault(i, new ArrayList<>()));
                return pVO;
            }).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public ExecuteRecordCopy getCurrentChangeRecord(Long planId, Long procedureStepModelId, Long copyVersion) {
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        return executionRecordCopyMapper.selectOne(new LambdaQueryWrapperX<ExecuteRecordCopy>()
                .eq(ExecuteRecordCopy::getProductPlanId, planId)
                .eq(ExecuteRecordCopy::getVersion, copyVersion)
                .eq(ExecuteRecordCopy::getRecordItemId, procedureStepModel.getRecordItemId())
                .eq(ExecuteRecordCopy::getReuse, procedureStepModel.getReusable())
                .eq(!procedureStepModel.getReusable(), ExecuteRecordCopy::getProcedureStepId, procedureStepModel.getProcedureStepId()));
    }

    @Override
    public List<ExecuteRecordCopy> getByPlanIdList(List<Long> planIdList) {
        if (CollUtil.isEmpty(planIdList)){
            return new ArrayList<>();
        }
        return executionRecordCopyMapper.selectByPlanIdList(planIdList);
    }

}
