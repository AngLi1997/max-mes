package com.bmos.platform.service.system.code.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.platform.common.enums.system.code.RuleTypeEnum;
import com.bmos.platform.service.dict.service.DictService;
import com.bmos.platform.service.dict.vo.DictVO;
import com.bmos.platform.service.system.code.convert.CodeRuleVersionDetailConverter;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionDetailSaveDTO;
import com.bmos.platform.service.system.code.mapper.CodeRuleVersionDetailMapper;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.model.CodeRuleVersionDetail;
import com.bmos.platform.service.system.code.service.CodeRuleVersionDetailService;
import com.bmos.platform.service.system.code.vo.CodeRuleUseDetailVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CodeRuleVersionDetailServiceImpl implements CodeRuleVersionDetailService {
    @Autowired
    private CodeRuleVersionDetailMapper codeRuleVersionDetailMapper;
    @Autowired
    private DictService dictService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CodeRuleVersion codeRuleVersion, List<CodeRuleVersionDetailSaveDTO> details) {
        details.forEach(
                CodeRuleVersionDetailSaveDTO::isShowNotBoolean
        );
        codeRuleVersionDetailMapper.insertBatch(
            CodeRuleVersionDetailConverter.INSTANCE.convertList(codeRuleVersion.getId(), details)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CodeRuleVersion codeRuleVersion, List<CodeRuleVersionDetailSaveDTO> details) {
        delete(codeRuleVersion.getId());
        save(codeRuleVersion, details);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        codeRuleVersionDetailMapper.deleteByCodeRuleVersionId(id);
    }

    @Override
    public List<CodeRuleVersionDetailVO> detail(Long versionId, Long dictId) {
        List<CodeRuleVersionDetailVO> details = codeRuleVersionDetailMapper.detail(versionId);
        List<CodeRuleVersionDetailVO> filterList = details.stream()
            .filter(detail -> RuleTypeEnum.PARAMETER == detail.getType())
            .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(filterList)) {
            Map<Long, String> collect = dictService.listDictDown(dictId)
                .stream()
                .collect(Collectors.toMap(DictVO::getId, DictVO::getLabel));
            filterList.forEach(detail -> detail.setValue(collect.get(detail.getParameterId())));
        }
        return details;
    }

    @Override
    public List<CodeRuleUseDetailVO> listDetail(Long versionId) {
        return codeRuleVersionDetailMapper.listDetail(versionId);
    }
}
