package com.bmos.platform.service.system.code.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.common.enums.StatusEnum;
import com.bmos.platform.common.enums.system.code.VersionStatusEnum;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionPageDTO;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeRuleVersionMapper extends BaseMapperX<CodeRuleVersion> {
    /**
     * 分页查询
     * @param dto dto
     * @return List<CodeRuleVersionPageVO>
     */
    List<CodeRuleVersionPageVO> page(CodeRuleVersionPageDTO dto);

    default boolean existsVersionByCode(Long id, String version, String code) {
        return exists(new LambdaQueryWrapperX<CodeRuleVersion>()
            .eq(CodeRuleVersion::getRuleCode, code)
            .eq(CodeRuleVersion::getVersion, version)
            .neIfPresent(CodeRuleVersion::getId, id)
            .last("limit 1"));
    }

    default void confirm(Long id) {
        CodeRuleVersion codeRuleVersion = new CodeRuleVersion();
        codeRuleVersion.setVersionStatus(VersionStatusEnum.CONFIRM);
        codeRuleVersion.setId(id);
        updateById(codeRuleVersion);
    }

    default boolean existsEnabled(String ruleCode) {
        return exists(new LambdaQueryWrapperX<CodeRuleVersion>()
            .eq(CodeRuleVersion::getRuleCode, ruleCode)
            .eq(CodeRuleVersion::getStatus, StatusEnum.ON.getValue())
            .last("limit 1"));
    }

    default void updateStatus(Long id, StatusEnum statusEnum) {
        CodeRuleVersion codeRuleVersion = new CodeRuleVersion();
        codeRuleVersion.setStatus(statusEnum.getValue());
        codeRuleVersion.setId(id);
        updateById(codeRuleVersion);
    }

    void delete(@Param("id") Long id, @Param("userId") String userId);

    default CodeRuleVersion getEnableVersion(String code) {
        return selectOne(new LambdaQueryWrapperX<CodeRuleVersion>()
            .eq(CodeRuleVersion::getRuleCode, code)
            .eq(CodeRuleVersion::getStatus, StatusEnum.ON.getValue()));
    }
}
