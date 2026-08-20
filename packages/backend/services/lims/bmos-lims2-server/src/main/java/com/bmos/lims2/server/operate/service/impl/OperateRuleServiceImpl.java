package com.bmos.lims2.server.operate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.config.minio.MinioFileClient;
import com.bmos.lims2.server.config.minio.constants.MinioBucket;
import com.bmos.lims2.server.operate.convert.OperateRuleConvert;
import com.bmos.lims2.server.operate.convert.OperateRuleVersionConvert;
import com.bmos.lims2.server.operate.dto.OperateRulePageDTO;
import com.bmos.lims2.server.operate.dto.SaveOperateRuleDTO;
import com.bmos.lims2.server.operate.enums.OperateRuleVersionStateEnum;
import com.bmos.lims2.server.operate.mapper.OperateRuleCategoryMapper;
import com.bmos.lims2.server.operate.mapper.OperateRuleMapper;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.model.OperateRuleCategory;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.service.OperateRuleService;
import com.bmos.lims2.server.operate.service.OperateRuleVersionService;
import com.bmos.lims2.server.operate.vo.OperateRulePageVO;
import com.bmos.lims2.server.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.lims2.server.permission.service.ResourcePermissionService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodOperateBindMapper;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethodOperateBind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
@Slf4j
public class OperateRuleServiceImpl implements OperateRuleService {
    private static final String OPERATE_PATH = "/%s.pdf";
    @Autowired
    private OperateRuleMapper ruleMapper;

    @Autowired
    private OperateRuleCategoryMapper categoryMapper;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private OperateRuleVersionService versionService;

    @Resource
    private MinioFileClient minioFileClient;

    @Autowired
    private ResourcePermissionService resourcePermissionService;

    @Autowired
    private InspectMethodOperateBindMapper inspectMethodOperateBindMapper;

    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @Override
    public List<OperateRule> getListByCategoryId(Long id) {
        return ruleMapper.getListByCategoryId(id);
    }

    @Override
    public CommonPage<OperateRulePageVO> getRecordPage(OperateRulePageDTO dto) {
        if (ObjectUtil.isNotNull(dto.getCategoryId())) {
            List<OperateRuleCategory> listCategory = categoryMapper.getListCategory();
            List<Long> categoryIdList = CollectionUtils.filterList(listCategory,
                    item -> item.getCode().contains(String.valueOf(dto.getCategoryId())))
                    .stream().map(OperateRuleCategory::getId).collect(Collectors.toList());
            dto.setCategoryIdList(categoryIdList);
        }
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
        }
        dto.setDeptIdList(deptIds);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<OperateRulePageVO> pageList = ruleMapper.getRecordPage(dto);
        if (CollUtil.isNotEmpty(pageList)) {
            List<OperateRuleVersion> versions = versionService.getListByParentIdAndState(
                    CollectionUtils.convertList(pageList, OperateRulePageVO::getId),
                    OperateRuleVersionStateEnum.VALID.getCode());
            Map<Long, OperateRuleVersion> versionMap = CollectionUtils.convertMap(versions, OperateRuleVersion::getOperateId);
            // 批量查询并回填每条记录绑定的方法记录ID集合
            List<Long> operateIds = CollectionUtils.convertList(pageList, OperateRulePageVO::getId);
            List<InspectMethodOperateBind> binds = inspectMethodOperateBindMapper.listByOperateIds(operateIds);
            Map<Long, List<Long>> operateIdToMethodIds = binds.stream()
                    .collect(Collectors.groupingBy(InspectMethodOperateBind::getOperateId,
                            Collectors.mapping(InspectMethodOperateBind::getRecordId, Collectors.toList())));
            pageList.forEach(item -> {
                OperateRuleVersion operateRuleVersion = versionMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(operateRuleVersion)) {
                    item.setVersion(operateRuleVersion.getVersion());
                }
                List<Long> methodIds = operateIdToMethodIds.get(item.getId());
                if (CollUtil.isNotEmpty(methodIds)) {
                    item.setRecordIdList(methodIds);
                } else {
                    item.setRecordIdList(Collections.emptyList());
                }
            });
        }
        return CommonPage.convertPage(pageList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SaveOperateRuleDTO dto) {
        List<OperateRule> operateRuleList = CollectionUtils.filterList(ruleMapper.selectList(),
                item -> StrUtil.equals(item.getCode(), dto.getCode()));
        if (CollUtil.isNotEmpty(operateRuleList)) {
            throw new BmosException(LimsResponseCode.OPERATE_CODE_ERROR);
        }
        Long operateRuleId = IdUtils.getSnowflake();
        OperateRule operateRule = OperateRuleConvert.INSTANCE.convertToSave(dto);
        operateRule.setId(operateRuleId);
        ruleMapper.saveOrUpdate(operateRule);
        if (CollUtil.isNotEmpty(dto.getDeptIds())) {
            resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                    .deptIds(dto.getDeptIds())
                    .resourceId(operateRuleId)
                    .build());
        }
        OperateRuleVersion version = OperateRuleVersionConvert.INSTANCE.convertToSave(dto);
        version.setOperateId(operateRuleId);
        version.setId(IdUtils.getSnowflake());
        versionService.save(version);
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.OPERATE_RULE.name())
                .businessId(version.getId())
                .operationType(OperationType.SAVE.getValue())
                .remark(dto.getRemark())
                .createBy(SysUserHolder.getUser().getUserId())
                .build());
    }

    @Override
    public List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return ruleMapper.getAuditBusinessKey(deptIdList);
    }

    @Override
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.contains(RecordConstant.UPLOAD)) {
            throw new BmosException(LimsResponseCode.OPERATE_UPLOAD_ERROR);
        }
        try {
            File files = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, RecordConstant.UPLOAD);
            file.transferTo(files);
            String path = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis() + StrUtil.subBefore(file.getOriginalFilename(),".",true);
            return minioFileClient.uploadFile(MinioBucket.OPERATE_RULE_SOP, files, String.format(OPERATE_PATH,path));
        }catch (Exception e){
            log.error("上传文件失败",e);
            throw new BmosException(LimsResponseCode.OPERATE_UPLOAD_ERROR);
        }
    }

    @Override
    public OperateRule getOneByVersionId(Long businessId) {
        OperateRuleVersion operateRuleVersion = versionService.selectById(businessId);
        OperateRule operateRule = ruleMapper.selectById(operateRuleVersion.getOperateId());
        operateRule.setVersion(operateRuleVersion.getVersion());
        return operateRule;
    }
}
