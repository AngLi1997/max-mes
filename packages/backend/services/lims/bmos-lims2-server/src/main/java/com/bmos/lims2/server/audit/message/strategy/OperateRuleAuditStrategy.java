package com.bmos.lims2.server.audit.message.strategy;

import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.service.OperateRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author renjinguang
 */

@Service
public class OperateRuleAuditStrategy extends AbstractAuditMessageStrategy {

    @Autowired
    protected OperateRuleService operateRuleService;

    public OperateRuleAuditStrategy(PlatformApiAdaptor platformApiAdaptor) {
        super(platformApiAdaptor);
    }

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return operateRuleService.getAuditBusinessKey(deptIdList);
    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
        OperateRule rule = operateRuleService.getOneByVersionId(businessId);
        AuditMessageVO vo = new AuditMessageVO();
        vo.setBusinessText("文件信息：" + rule.getCode() + StrUtil.DASHED + rule.getName() + "，版本号：" + rule.getVersion());
        vo.setBusinessId(rule.getId());
        return vo;
    }
}
