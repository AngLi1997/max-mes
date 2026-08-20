package com.bmos.mes.service.plan.rule.service;

import com.bmos.mes.service.plan.rule.dto.CodeRulePageDTO;
import com.bmos.mes.service.plan.rule.dto.CodeRuleSaveDTO;
import com.bmos.mes.service.plan.rule.dto.CodeRuleUpdateDTO;
import com.bmos.mes.service.plan.rule.model.CodeRule;
import com.bmos.mes.service.plan.rule.vo.CodeRulePageVO;
import com.bmos.mes.service.platform.plan.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.plan.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.plan.vo.BatchNextCodeVO;
import com.bmos.mes.service.platform.plan.vo.NextCodeVO;

import java.util.List;
import java.util.Set;

public interface CodeRuleService {
    /**
     * 分页查询
     * @param dto dto
     * @return List<CodeRulePageVO>
     */
    List<CodeRulePageVO> page(CodeRulePageDTO dto);

    /**
     * 根据编码查询配置的工序集合
     * @param code 编码规则code
     * @return List<Long>
     */
    List<Long> detailCode(String code);

    /**
     *
     * @param processId
     * @param type
     * @return
     */
    CodeRule selectByProcessIdAndType(Long processId, String type);

    /**
     * 保存
     * @param dto dto
     */
    void save(CodeRuleSaveDTO dto);

    /**
     * 更新
     * @param dto dto
     */
    void update(CodeRuleUpdateDTO dto);

    /**
     * 获取编号
     * @param dto
     * @return
     */
    NextCodeVO getNextUseNo(NextUseCodeDTO dto);

    /**
     * 批量获取下一个编号 未确认使用的编号会重复返回
     * @param dto
     * @return
     */
    BatchNextCodeVO getBatchNextUserNo(BatchNextUseCodeDTO dto);

    List<CodeRule> getCodeRuleListByProcessIdAndType(Set<Long> processIdList, List<String> codes);
}
