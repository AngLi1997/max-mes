package com.bmos.mes.service.operate.service;

import com.bmos.mes.service.operate.dto.OperateRulePageDTO;
import com.bmos.mes.service.operate.dto.SaveOperateRuleDTO;
import com.bmos.mes.service.operate.model.OperateRule;
import com.bmos.mes.service.operate.vo.OperateRulePageVO;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author renjinguang
 */
public interface OperateRuleService {

    List<OperateRule> getListByCategoryId(Long id);

    CommonPage<OperateRulePageVO> getRecordPage(OperateRulePageDTO dto);

    void save(SaveOperateRuleDTO dto);

    List<String> getAuditBusinessKey(List<Long> deptIdList);

    String upload(MultipartFile file);

    OperateRule getOneByVersionId(Long businessId);

}
