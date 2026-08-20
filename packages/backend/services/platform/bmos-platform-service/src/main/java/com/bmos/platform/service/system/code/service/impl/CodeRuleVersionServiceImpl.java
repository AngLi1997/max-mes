package com.bmos.platform.service.system.code.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.common.enums.system.code.VersionStatusEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.system.code.convert.CodeRuleVersionConverter;
import com.bmos.platform.service.system.code.dto.CodeRuleSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleUpdateDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionPageDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionUpdateDTO;
import com.bmos.platform.service.system.code.mapper.CodeRuleVersionDetailMapper;
import com.bmos.platform.service.system.code.mapper.CodeRuleVersionMapper;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.service.CodeRuleVersionDetailService;
import com.bmos.platform.service.system.code.service.CodeRuleVersionService;
import com.bmos.platform.service.system.code.vo.CodeRuleUseDetailVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionDetailVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodeRuleVersionServiceImpl implements CodeRuleVersionService {
    @Autowired
    private CodeRuleVersionMapper codeRuleVersionMapper;
    @Autowired
    private CodeRuleVersionDetailService codeRuleVersionDetailService;

    @Override
    public List<CodeRuleVersionPageVO> page(CodeRuleVersionPageDTO dto) {
        return codeRuleVersionMapper.page(dto);
    }

    @Override
    public List<CodeRuleVersionDetailVO> detail(Long versionId, Long dictId) {
        return codeRuleVersionDetailService.detail(versionId, dictId);
    }

    @Override
    public List<CodeRuleUseDetailVO> listDetail(Long versionId) {
        return codeRuleVersionDetailService.listDetail(versionId);
    }

    @Override
    public CodeRuleVersion getEnableVersion(String code) {
        return codeRuleVersionMapper.getEnableVersion(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CodeRuleSaveDTO dto) {
        Assert.isFalse(
            codeRuleVersionMapper.existsVersionByCode(null, dto.getVersion(), dto.getCode()),
            () -> new BmosException(PlatformResponseCode.VERSION_EXISTS)
        );
        CodeRuleVersion codeRuleVersion = CodeRuleVersionConverter.INSTANCE.convertDO(dto);
        codeRuleVersionMapper.insert(codeRuleVersion);
        codeRuleVersionDetailService.save(codeRuleVersion, dto.getCodeRuleVersionDetails());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CodeRuleUpdateDTO dto) {
        CodeRuleVersion codeRuleVersion = codeRuleVersionMapper.selectById(dto.getVersionId());
        validatedVersionStatus(codeRuleVersion, VersionStatusEnum.EDIT);
        Assert.isFalse(
            codeRuleVersionMapper.existsVersionByCode(dto.getVersionId(), dto.getVersion(), codeRuleVersion.getRuleCode()),
            () -> new BmosException(PlatformResponseCode.VERSION_EXISTS)
        );
        codeRuleVersionMapper.updateById(CodeRuleVersionConverter.INSTANCE.convertDO(dto));
        codeRuleVersionDetailService.update(codeRuleVersion, dto.getCodeRuleVersionDetails());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CodeRuleVersionSaveDTO dto) {
        CodeRuleSaveDTO codeRuleSaveDTO = CodeRuleVersionConverter.INSTANCE.convertDTO(dto);
        save(codeRuleSaveDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CodeRuleVersionUpdateDTO dto) {
        CodeRuleUpdateDTO codeRuleUpdateDTO = CodeRuleVersionConverter.INSTANCE.convertDTO(dto);
        update(codeRuleUpdateDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        CodeRuleVersion codeRuleVersion = codeRuleVersionMapper.selectById(id);
        validatedVersionStatus(codeRuleVersion, VersionStatusEnum.EDIT);
        codeRuleVersionMapper.confirm(id);
    }

    private static void validatedVersionStatus(CodeRuleVersion codeRuleVersion, VersionStatusEnum statusEnum) {
        Assert.isTrue(
            statusEnum == codeRuleVersion.getVersionStatus(),
            () -> new BmosException(PlatformResponseCode.PLEASE_CHOOSE_EDIT_VERSION)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disabled(Long id) {
        codeRuleVersionMapper.updateStatus(id, StatusEnum.OFF);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enabled(Long id) {
        CodeRuleVersion codeRuleVersion = codeRuleVersionMapper.selectById(id);
        if(ObjectUtil.notEqual(codeRuleVersion.getVersionStatus(), VersionStatusEnum.CONFIRM)){
            throw new BmosException(PlatformResponseCode.COLD_RULE_NOT_CONFIRM);
        }
        Assert.isFalse(
            codeRuleVersionMapper.existsEnabled(codeRuleVersion.getRuleCode()),
            () -> new BmosException(PlatformResponseCode.EXISTS_ENABLED_VERSION)
        );
        codeRuleVersionMapper.updateStatus(id, StatusEnum.ON);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        CodeRuleVersion codeRuleVersion = codeRuleVersionMapper.selectById(id);
        validatedVersionStatus(codeRuleVersion, VersionStatusEnum.EDIT);
        codeRuleVersionMapper.delete(id, SysUserHolder.getUser().getUserId());
        codeRuleVersionDetailService.delete(id);
    }
}
