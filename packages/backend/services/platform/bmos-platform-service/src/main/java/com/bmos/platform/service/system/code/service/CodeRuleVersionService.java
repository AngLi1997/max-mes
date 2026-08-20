package com.bmos.platform.service.system.code.service;

import com.bmos.platform.service.system.code.dto.CodeRuleSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleUpdateDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionPageDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionUpdateDTO;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.vo.CodeRuleUseDetailVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionDetailVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionPageVO;

import java.util.List;

public interface CodeRuleVersionService {
    /**
     * 分页查询
     * @param dto dto
     * @return List<CodeRuleVersionPageVO>
     */
    List<CodeRuleVersionPageVO> page(CodeRuleVersionPageDTO dto);

    /**
     * 详情
     * @param versionId 版本ID
     * @param dictId 字典ID
     * @return List<CodeRuleVersionDetailVO>
     */
    List<CodeRuleVersionDetailVO> detail(Long versionId, Long dictId);

    /**
     * 获取规则数据 (加入参数编码 关联表查询表)
     * @param versionId 版本id
     * @return List<CodeRuleUseDetailVO>
     */
    List<CodeRuleUseDetailVO> listDetail(Long versionId);

    /**
     * 获取启用的版本
     * @param code code
     * @return CodeRuleVersion
     */
    CodeRuleVersion getEnableVersion(String code);

    /**
     * 编码规则保存
     * @param dto dto
     */
    void save(CodeRuleSaveDTO dto);

    /**
     * 编码规则更新
     * @param dto dto
     */
    void update(CodeRuleUpdateDTO dto);

    /**
     * 编码规则保存
     * @param dto dto
     */
    void save(CodeRuleVersionSaveDTO dto);

    /**
     * 编码规则更新
     * @param dto dto
     */
    void update(CodeRuleVersionUpdateDTO dto);

    /**
     * 确认
     * @param id id
     */
    void confirm(Long id);

    /**
     * 停用
     * @param id id
     */
    void disabled(Long id);

    /**
     * 启用
     * @param id id
     */
    void enabled(Long id);

    /**
     * 删除
     * @param id id
     */
    void delete(Long id);
}
