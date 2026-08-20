package com.bmos.mes.service.operate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.operate.OperateRuleVersionStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.operate.convert.OperateRuleConvert;
import com.bmos.mes.service.operate.convert.OperateRuleVersionConvert;
import com.bmos.mes.service.operate.dto.OperateRulePageDTO;
import com.bmos.mes.service.operate.dto.SaveOperateRuleDTO;
import com.bmos.mes.service.operate.mapper.OperateRuleCategoryMapper;
import com.bmos.mes.service.operate.mapper.OperateRuleMapper;
import com.bmos.mes.service.operate.model.OperateRule;
import com.bmos.mes.service.operate.model.OperateRuleCategory;
import com.bmos.mes.service.operate.model.OperateRuleVersion;
import com.bmos.mes.service.operate.service.OperateRuleService;
import com.bmos.mes.service.operate.service.OperateRuleVersionService;
import com.bmos.mes.service.operate.vo.OperateRulePageVO;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
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
            pageList.forEach(item -> {
                OperateRuleVersion operateRuleVersion = versionMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(operateRuleVersion)) {
                    item.setVersion(operateRuleVersion.getVersion());
                }
            });
        }
        return CommonPage.convertPage(pageList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.OPERATE_RULE, operationType = OperationType.SAVE, remark = "#dto.remark", businessId = "#getId")
    public void save(SaveOperateRuleDTO dto) {
        List<OperateRule> operateRuleList = CollectionUtils.filterList(ruleMapper.selectList(),
                item -> StrUtil.equals(item.getCode(), dto.getCode()));
        if (CollUtil.isNotEmpty(operateRuleList)) {
            throw new BmosException(MesResponseCode.OPERATE_CODE_ERROR);
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
        OperationHistoryContext.putVariable(version, OperateRuleVersion::getId);
        versionService.save(version);
    }

    @Override
    public List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return ruleMapper.getAuditBusinessKey(deptIdList);
    }

    @Override
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.contains(RecordConstant.UPLOAD)) {
            throw new BmosException(MesResponseCode.OPERATE_UPLOAD_ERROR);
        }
        try {
            File files = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, RecordConstant.UPLOAD);
            file.transferTo(files);
            String path = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis() + StrUtil.subBefore(file.getOriginalFilename(),".",true);
            return minioFileClient.uploadFile(MinioBucket.OPERATE_RULE_SOP, files, String.format(OPERATE_PATH,path));
        }catch (Exception e){
            throw new BmosException(MesResponseCode.OPERATE_UPLOAD_ERROR);
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
