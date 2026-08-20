package com.bmos.lims2.server.inspect.parameter.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodOperateBindBatchSaveDTO;
import com.bmos.lims2.server.inspect.parameter.dto.OperateRuleMethodBindBatchSaveDTO;
import com.bmos.lims2.server.inspect.parameter.dto.MethodBoundOperateEffectiveDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethodOperateBind;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodOperateBindMapper;
import com.bmos.lims2.server.inspect.parameter.service.InspectMethodOperateBindService;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.lims2.server.operate.mapper.OperateRuleMapper;
import com.bmos.lims2.server.operate.mapper.OperateRuleVersionMapper;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.enums.OperateRuleVersionStateEnum;
import com.bmos.lims2.server.config.minio.MinioFileClient;
import com.bmos.lims2.server.config.minio.constants.MinioBucket;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Description: 方法-操作规程 绑定服务实现
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Service
public class InspectMethodOperateBindServiceImpl implements InspectMethodOperateBindService {

    @Autowired
    private InspectMethodOperateBindMapper bindMapper;

    @Autowired
    private OperateRuleVersionMapper operateRuleVersionMapper;

    @Autowired
    private OperateRuleMapper operateRuleMapper;

    @Autowired
    private MinioFileClient minioFileClient;

    @Value("${minio.appDownloadUrl}")
    private String appDownloadUrl;

    @Override
    @OperationLog
    @Transactional(rollbackFor = Exception.class)
    public void saveOperateBindingsForMethod(InspectMethodOperateBindBatchSaveDTO dto) {
        Long recordId = dto.getRecordId();
        // 清旧
        bindMapper.deleteByRecordId(recordId);
        // 空则结束
        if (CollUtil.isEmpty(dto.getOperateIdList())) {
            return;
        }
        // 去重保存
        LinkedHashSet<Long> ids = new LinkedHashSet<>(dto.getOperateIdList());
        List<InspectMethodOperateBind> list = new ArrayList<>(ids.size());
        for (Long operateId : ids) {
            InspectMethodOperateBind bind = new InspectMethodOperateBind();
            bind.setRecordId(recordId);
            bind.setOperateId(operateId);
            list.add(bind);
        }
        bindMapper.saveBatch(list);
    }

    @Override
    @OperationLog
    @Transactional(rollbackFor = Exception.class)
    public void saveMethodBindingsForOperate(OperateRuleMethodBindBatchSaveDTO dto) {
        Long operateId = dto.getOperateId();
        // 清旧
        bindMapper.deleteByOperateId(operateId);
        // 空则结束
        if (CollUtil.isEmpty(dto.getRecordIdList())) {
            return;
        }
        // 去重保存
        LinkedHashSet<Long> ids = new LinkedHashSet<>(dto.getRecordIdList());
        List<InspectMethodOperateBind> list = new ArrayList<>(ids.size());
        for (Long recordId : ids) {
            InspectMethodOperateBind bind = new InspectMethodOperateBind();
            bind.setRecordId(recordId);
            bind.setOperateId(operateId);
            list.add(bind);
        }
        bindMapper.saveBatch(list);
    }

    @Override
    public List<Long> listOperateIdsByMethod(Long recordId) {
        return bindMapper.listOperateIdsByRecordId(recordId);
    }

    @Override
    public List<Long> listMethodRecordIdsByOperate(Long operateId) {
        return bindMapper.listRecordIdsByOperateId(operateId);
    }

    @Override
    public List<MethodBoundOperateEffectiveDTO> listEffectiveOperateByMethod(Long recordId) {
        List<Long> operateIds = bindMapper.listOperateIdsByRecordId(recordId);
        if (CollUtil.isEmpty(operateIds)) {
            return new ArrayList<>();
        }
        // 生效版本
        List<OperateRuleVersion> versions = operateRuleVersionMapper.getListByParentIdAndState(
                operateIds, OperateRuleVersionStateEnum.VALID.getCode());
        if (CollUtil.isEmpty(versions)) {
            return new ArrayList<>();
        }
        Map<Long, OperateRule> operateMap = operateRuleMapper.selectList().stream()
                .filter(o -> operateIds.contains(o.getId()))
                .collect(Collectors.toMap(OperateRule::getId, o -> o, (a,b)->a));
        String bucket = minioFileClient.getBucketName(MinioBucket.OPERATE_RULE_SOP);
        List<MethodBoundOperateEffectiveDTO> list = new ArrayList<>(versions.size());
        for (OperateRuleVersion v : versions) {
            MethodBoundOperateEffectiveDTO dto = new MethodBoundOperateEffectiveDTO();
            dto.setOperateId(v.getOperateId());
            OperateRule rule = operateMap.get(v.getOperateId());
            dto.setOperateName(rule != null ? rule.getName() : null);
            dto.setVersionId(v.getId());
            dto.setVersionNo(v.getVersion());
            String url = appDownloadUrl + StrUtil.SLASH + bucket + v.getUrl();
            dto.setDownloadUrl(url);
            list.add(dto);
        }
        return list;
    }
}


