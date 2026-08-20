package com.bmos.lims2.server.audit.message.strategy;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordVersion;
import com.bmos.lims2.server.eln.record.service.BatchRecordVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author renjinguang
 */

@Service
public class RecordAuditStrategy extends AbstractAuditMessageStrategy {

    public RecordAuditStrategy(PlatformApiAdaptor platformApiAdaptor) {
        super(platformApiAdaptor);
    }

    @Autowired
    BatchRecordVersionService versionService;

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return versionService.getAuditBusinessKey(deptIdList);
    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
        BatchRecordVersion version = versionService.selectById(businessId);
        AuditMessageVO vo = new AuditMessageVO();
        vo.setBusinessText("记录名称："+ version.getName() + "，版本号：" + version.getVersion());
        vo.setBusinessId(version.getRecordId());
        return vo;
    }
}
