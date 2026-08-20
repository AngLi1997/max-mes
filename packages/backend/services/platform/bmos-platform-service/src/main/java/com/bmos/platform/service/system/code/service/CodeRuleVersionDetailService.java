package com.bmos.platform.service.system.code.service;

import com.bmos.platform.service.system.code.dto.CodeRuleSaveDTO;
import com.bmos.platform.service.system.code.dto.CodeRuleVersionDetailSaveDTO;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.vo.CodeRuleUseDetailVO;
import com.bmos.platform.service.system.code.vo.CodeRuleVersionDetailVO;

import java.util.List;

public interface CodeRuleVersionDetailService {
    /**
     * 保存
     * @param codeRuleVersion codeRuleVersion
     * @param details details
     */
    void save(CodeRuleVersion codeRuleVersion, List<CodeRuleVersionDetailSaveDTO> details);

    /**
     * 更新
     * @param codeRuleVersion codeRuleVersion
     * @param details details
     */
    void update(CodeRuleVersion codeRuleVersion, List<CodeRuleVersionDetailSaveDTO> details);

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
     * 删除
     * @param id id
     */
    void delete(Long id);
}
