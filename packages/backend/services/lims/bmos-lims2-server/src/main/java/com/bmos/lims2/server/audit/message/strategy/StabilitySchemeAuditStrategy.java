package com.bmos.lims2.server.audit.message.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.stability.scheme.entity.StabilityScheme;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 稳定性方案审批策略
 */
@Service
public class StabilitySchemeAuditStrategy extends AbstractAuditMessageStrategy {

    @Autowired
    private StabilitySchemeMapper stabilitySchemeMapper;

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    public StabilitySchemeAuditStrategy(PlatformApiAdaptor platformApiAdaptor) {
        super(platformApiAdaptor);
    }

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        List<StabilitySchemeVersion> versions = stabilitySchemeVersionMapper.selectList(
                new LambdaQueryWrapper<StabilitySchemeVersion>()
                        .eq(StabilitySchemeVersion::getStatus, StabilitySchemeVersionStatusEnum.APPROVING)
        );
        return versions.stream()
                .map(StabilitySchemeVersion::getId)
                .distinct().map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
        StabilitySchemeVersion version = stabilitySchemeVersionMapper.selectById(businessId);
        if (version == null) {
            return null;
        }
        StabilityScheme scheme = stabilitySchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            return null;
        }
        AuditMessageVO messageVO = new AuditMessageVO();
        messageVO.setBusinessId(businessId);
        messageVO.setUserIdList(new ArrayList<>());
        return messageVO;
    }
}
