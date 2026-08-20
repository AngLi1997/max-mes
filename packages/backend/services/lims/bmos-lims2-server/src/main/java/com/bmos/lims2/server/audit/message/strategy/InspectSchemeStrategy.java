package com.bmos.lims2.server.audit.message.strategy;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 检验方案审批策略
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Service
public class InspectSchemeStrategy extends AbstractAuditMessageStrategy {

    @Autowired
    private InspectionSchemeMapper inspectionSchemeMapper;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    public InspectSchemeStrategy(PlatformApiAdaptor platformApiAdaptor) {
        super(platformApiAdaptor);
    }

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        // 查询所有审批中的版本
        List<InspectionSchemeVersion> versions = inspectionSchemeVersionMapper.selectList(
                new LambdaQueryWrapper<InspectionSchemeVersion>()
                        .eq(InspectionSchemeVersion::getStatus, InspectionSchemeVersionStatusEnum.APPROVING.getValue())
        );

        return versions.stream()
                .map(InspectionSchemeVersion::getId)
                .distinct().map(String::valueOf)
                .collect(Collectors.toList());

    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
        // 查询方案版本
        InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(businessId);
        if (version == null) {
            return null;
        }

        // 查询方案
        InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
        if (scheme == null) {
            return null;
        }

        // 构建消息
        AuditMessageVO messageVO = new AuditMessageVO();
        messageVO.setBusinessId(businessId);
        messageVO.setUserIdList(new ArrayList<>());

        return messageVO;
    }
}
