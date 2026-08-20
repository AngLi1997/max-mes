package com.bmos.mes.service.schedules;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.operate.OperateRuleVersionStateEnum;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.service.operate.model.OperateRuleVersion;
import com.bmos.mes.service.operate.service.OperateRuleVersionService;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.weigh.centre.execute.service.IWeighExecuteService;
import com.bmos.mes.service.weigh.centre.requirement.mapper.IWeighRequirementMapper;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.scheduler.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务
 * @author liang
 * @version 1.0.0
 * @date 2024/2/28 15:37
 */
@Component
@Slf4j
public class ScheduleJobs {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Autowired
    private OperateRuleVersionService versionService;

    @Resource
    private IWeighRequirementMapper weighRequirementMapper;

    @Resource
    private IWeighExecuteService weighExecuteService;

    @Autowired
    private ProcessService processService;

    /**
     * 定时任务
     * 修改物料批次可用性
     */
    @XxlJob("updateStorageBatchAvailable")
    public void updateStorageBatchAvailable() {
        log.info("[定时任务] - 物料批次可用性 - 开始");
        List<StorageMaterialBatch> list = storageMaterialBatchMapper.queryAvailableBatch(LocalDate.now());
        if (CollectionUtil.isNotEmpty(list)) {
            log.info("可用的批次:{}", list);
            list.forEach(item -> item.setAvailable(false));
            storageMaterialBatchMapper.updateBatch(list);
        }
        log.info("[定时任务] - 物料批次可用性 - 结束");
    }

    /**
     * 定时任务
     * 判断称量中心需求是否过期
     */
    @XxlJob("updateWeighCenterRequirementExpired")
    public void updateWeighCenterRequirementExpired() {
        log.info("[定时任务] - 判断称量中心需求是否过期 - 开始");
        // 查询过期的 且未处于称量中的需求
        List<WeighRequirement> list = weighRequirementMapper.selectExpiredRequirement(LocalDate.now());
        if (CollectionUtil.isNotEmpty(list)){
            log.info("过期的未开始称量的需求:{}", list);
            list.forEach(item -> item.setRequirementStatus(RequirementStatusEnum.EXPIRED));
            weighRequirementMapper.updateBatch(list);
            // 刷新任务状态
            List<Long> taskIds = list.stream()
                    .map(WeighRequirement::getWeighRequirementTaskId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(taskIds)){
                weighExecuteService.refreshTaskStatus(taskIds);
            }
        }
        log.info("[定时任务] - 判断称量中心需求是否过期 - 结束");
    }

    /**
     * 定时任务
     * 修改操作规程状态
     */
//    @Scheduled(cron = "0 5 0 * * ?")
    @XxlJob("updateOperateRuleVersion")
    @Transactional(rollbackFor = Exception.class)
    public void updateOperateRuleVersion() {
        log.info("[定时任务] - 修改操作规程版本状态 - 开始");
        List<OperateRuleVersion> versionList = versionService.getWaitValidVersionByDateAndState(FORMATTER.format(new Date()),
                OperateRuleVersionStateEnum.WAIT_VALID.getCode());
        if (CollUtil.isNotEmpty(versionList)) {
            List<Long> idList = CollectionUtils.convertList(versionList, OperateRuleVersion::getOperateId);
            List<OperateRuleVersion> vaLidVersionList = versionService.getListByParentIdAndState(idList,
                    OperateRuleVersionStateEnum.VALID.getCode());
            if (CollUtil.isNotEmpty(vaLidVersionList)){
                vaLidVersionList.forEach(item->item.setState(OperateRuleVersionStateEnum.INVALID.getCode()));
                versionService.updateOperateRuleVersion(vaLidVersionList);
            }
            versionList.forEach(item-> item.setState(OperateRuleVersionStateEnum.VALID.getCode()));
            versionService.updateOperateRuleVersion(versionList);
        }
        log.info("[定时任务] - 修改操作规程版本状态 - 结束");
    }

    /**
     * 定时任务
     * 修改工艺版本生效时间
     */
//    @Scheduled(cron = "0 8 0 * * ?")
    @XxlJob("updateProcessVersionActionState")
    public void updateProcessVersionActionState() {
        log.info("[定时任务] - 修改工艺版本状态 - 开始");
        processService.updateProcessVersionActionState();
        log.info("[定时任务] - 修改操作规程版本状态 - 结束");
    }
}
