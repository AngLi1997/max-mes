package com.bmos.mes.service.plan.rule.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.enums.plan.CodeRuleTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.rule.convert.CodeRuleConverter;
import com.bmos.mes.service.plan.rule.dto.CodeRulePageDTO;
import com.bmos.mes.service.plan.rule.dto.CodeRuleSaveDTO;
import com.bmos.mes.service.plan.rule.dto.CodeRuleUpdateDTO;
import com.bmos.mes.service.plan.rule.mapper.CodeRuleMapper;
import com.bmos.mes.service.plan.rule.model.CodeRule;
import com.bmos.mes.service.plan.rule.service.CodeRuleService;
import com.bmos.mes.service.plan.rule.vo.CodeRulePageVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.plan.PlatformCodeRuleClient;
import com.bmos.mes.service.platform.plan.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.plan.vo.BatchNextCodeVO;
import com.bmos.mes.service.platform.plan.vo.NextCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CodeRuleServiceImpl implements CodeRuleService {

    @Autowired
    private CodeRuleMapper codeRuleMapper;

    @Autowired
    private PlatformCodeRuleClient platformCodeRuleClient;

    @Override
    public List<CodeRulePageVO> page(CodeRulePageDTO dto) {
        return codeRuleMapper.page(dto);
    }

    @Override
    public List<Long> detailCode(String code) {
        return codeRuleMapper.detailCode(code);
    }

    @Override
    public CodeRule selectByProcessIdAndType(Long processId, String type) {
        return codeRuleMapper.selectByProcessIdAndType(processId, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CodeRuleSaveDTO dto) {
        try {
            codeRuleMapper.deleteByCodeRuleCode(dto.getCodeRuleCode());
            codeRuleMapper.deleteByProcessIdAndType(dto.getProcessIds(), dto.getType());
            codeRuleMapper.insertBatch(CodeRuleConverter.INSTANCE.convertList(dto));
        } catch (DuplicateKeyException exception) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_CODE_RULE_EXISTS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CodeRuleUpdateDTO dto) {
        try {
            if (Objects.nonNull(dto.getId())) {
                codeRuleMapper.updateById(CodeRuleConverter.INSTANCE.convertDO(dto));
                return;
            }
            codeRuleMapper.insert(CodeRuleConverter.INSTANCE.convertDO(dto));
        } catch (DuplicateKeyException exception) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_CODE_RULE_EXISTS);
        }
    }

    @Override
    public NextCodeVO getNextUseNo(NextUseCodeDTO dto) {
        CodeRule codeRuleField = getCodeRuleField(dto.getProcessId(), dto.getType());
        if (ObjectUtil.isNull(codeRuleField)){
            return null;
        }
        dto.setCode(codeRuleField.getCodeRuleCode());
        ResponseInfo<NextCodeVO> nextCodeVOResponseInfo = FeignUtils.handleRequest(data -> platformCodeRuleClient.getNextUseNo(data), dto);
        if (nextCodeVOResponseInfo.isError()){
            String format = String.format("%s-%s", codeRuleField.getCodeRuleCode(), codeRuleField.getCodeRuleName());
            if (CodeRuleTypeEnum.PRODUCT_PLAN_NO.getValue().equals(dto.getType())) {
                throw new BmosException(MesResponseCode.PLAN_CODE_RULE_NOT_EXIST, format);
            }
            if (CodeRuleTypeEnum.PRODUCT_PLAN_BATCH_NO.getValue().equals(dto.getType())) {
                throw new BmosException(MesResponseCode.BATCH_NO_CODE_RULE_NOT_EXIST, format);
            }
        }
        return nextCodeVOResponseInfo.getData();
    }

    @Override
    public BatchNextCodeVO getBatchNextUserNo(BatchNextUseCodeDTO dto) {
        CodeRule codeRuleField = getCodeRuleField(dto.getProcessId(), dto.getType());
        if (ObjectUtil.isNull(codeRuleField)){
            return null;
        }
        dto.setCode(codeRuleField.getCodeRuleCode());
        ResponseInfo<BatchNextCodeVO> batchNextCodeVOResponseInfo = FeignUtils.handleRequest(data -> platformCodeRuleClient.getBatchNextUseNo(data), dto);
        return batchNextCodeVOResponseInfo.getData();
    }

    @Override
    public List<CodeRule> getCodeRuleListByProcessIdAndType(Set<Long> processIdList, List<String> codes) {
        return codeRuleMapper.getCodeRuleListByProcessIdAndType(processIdList,codes);
    }

    public CodeRule getCodeRuleField(Long processId, String type) {
        CodeRule codeRule = this.selectByProcessIdAndType(processId, type);
        if (Objects.isNull(codeRule)) {
            if (CodeRuleTypeEnum.PRODUCT_PLAN_NO.getValue().equals(type)) {
                return null;
            }
            if (CodeRuleTypeEnum.PRODUCT_PLAN_BATCH_NO.getValue().equals(type)) {
               return null;
            }
            throw new BmosException(MesResponseCode.CODE_RULE_TYPE_ERROR);
        }
        return codeRule;
    }
}
