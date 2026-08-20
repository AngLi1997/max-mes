package com.bmos.platform.service.system.code.service;

import com.bmos.platform.facade.code.dto.BatchCodeNoDTO;
import com.bmos.platform.facade.code.dto.BatchConfirmByCodeDTO;
import com.bmos.platform.facade.code.dto.ConfirmNoInfoDTO;
import com.bmos.platform.facade.code.dto.ReleaseConfirmedNoDTO;
import com.bmos.platform.facade.code.vo.BatchCodeNoVO;
import com.bmos.platform.service.system.code.dto.*;
import com.bmos.platform.service.system.code.vo.BatchNextCodeVO;
import com.bmos.platform.service.system.code.vo.CodeRulePageVO;
import com.bmos.platform.service.system.code.vo.DetailCodeRuleVersionDetailVO;
import com.bmos.platform.service.system.code.vo.NextCodeVO;

import java.util.List;

public interface CodeRuleService {
    /**
     * 分页查询
     * @param dto dto
     * @return List<CodeRulePageVO>
     */
    List<CodeRulePageVO> page(CodeRulePageDTO dto);

    /**
     * 列表查询
     * @param dto dto
     * @return CodeRulePageVO
     */
    List<CodeRulePageVO> list(CodeRuleListDTO dto);

    /**
     * 详情
     * @param versionId 规则版本id
     * @return DetailCodeRuleVersionDetailVO
     */
    DetailCodeRuleVersionDetailVO detail(Long versionId);

    /**
     * 数据权限配置
     * @param dto dto
     */
    void permission(CodeRulePermissionDTO dto);

    /**
     * 数据权限查询
     * @param id id
     * @return List<Long>
     */
    List<Long> permissionDetail(Long id);

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
     * 获取下一个编号 未确认使用的不包含（需加事务）
     * @param dto dto
     * @return String
     */
    NextCodeVO getNextNo(NextUseCodeDTO dto);

    /**
     * 获取下一个编号 未确认使用的包含
     * @param dto dto
     * @return String
     */
    NextCodeVO getNextUseNo(NextUseCodeDTO dto);

    /**
     * 批量获取下一个编号 未确认使用的包含
     * @param dto dto
     * @return String
     */
    BatchNextCodeVO getBatchNextUseNo(BatchNextUseCodeDTO dto);

    void confirmNo(ConfirmNoInfoDTO dto);

    void batchConfirmNo(BatchConfirmNextUseCodeDTO dto);

    /**
     * 释放已确认的编号
     * @param dto
     */
    void releaseConfirmedNO(ReleaseConfirmedNoDTO dto);

    /**
     * 根据id批量确认编号
     * @param list
     */
    void batchConfirmByIdList(List<Long> list);

    /**
     * 根据code等信息批量确认编号
     * @param dto
     */
    void batchConfirmByCodeList(BatchConfirmByCodeDTO dto);

    /**
     * 批量获取同类型编码
     * 未确认的包含
     * @param dto
     * @return
     */
    BatchCodeNoVO batchGetSameTypeNo(BatchCodeNoDTO dto);
}
