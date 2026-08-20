package com.bmos.lims2.server.audit.message.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.report.entity.ReportGenerateTask;
import com.bmos.lims2.server.report.mapper.ReportGenerateTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 报告审批消息策略
 * @Author: yigaohui
 * @Date: 2025/09/09 11:20
 */
@Service
public class ReportAuditStrategy extends AbstractAuditMessageStrategy {

    @Autowired
    private ReportGenerateTaskMapper reportGenerateTaskMapper;

    public ReportAuditStrategy(PlatformApiAdaptor platformApiAdaptor) {
        super(platformApiAdaptor);
    }

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        // 查询处于审批中的报告任务，返回其业务键（任务ID）
        List<ReportGenerateTask> tasks = reportGenerateTaskMapper.selectList(
                new LambdaQueryWrapper<ReportGenerateTask>()
                        .eq(ReportGenerateTask::getLifecycleStatus, com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.APPROVING)
        );
        return tasks.stream()
                .map(ReportGenerateTask::getId)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
        ReportGenerateTask task = reportGenerateTaskMapper.selectById(businessId);
        if (task == null) {
            return null;
        }
        AuditMessageVO vo = new AuditMessageVO();
        vo.setBusinessId(task.getId());
        vo.setUserIdList(new ArrayList<>());
        return vo;
    }
}


