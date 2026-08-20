package com.bmos.mes.service.equipment.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.execute.AcquisitionPictureExtInfo;
import com.bmos.mes.common.utils.Base64Util;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.config.minio.constants.MinioBucket;
import com.bmos.mes.service.equipment.service.EquipmentDataAcquisitionPictureService;
import com.bmos.mes.service.equipment.service.dto.AcquisitionPictureRangeDTO;
import com.bmos.mes.service.equipment.service.dto.AcquisitionPictureSaveDTO;
import com.bmos.mes.service.equipment.vo.AcquisitionPictureRangeVO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.constant.ProcessConstant;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.stepconfig.EquipmentPictureDataConfig;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

@Service
public class EquipmentDataDataAcquisitionPictureServiceImpl implements EquipmentDataAcquisitionPictureService {

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private MinioFileClient minioFileClient;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ExecuteFormDataService executeFormDataService;

    /**
     * 设备数采绘图组件在组件配置中的属性字段
     */
    private final String EQUIPMENT_PICTURE_CONFIG = "equipmentPictureConfigList";

    @Override
    public AcquisitionPictureRangeVO getAcquisitionPictureRange(AcquisitionPictureRangeDTO dto) {
        // 获取组件配置
        String configJson = procedureStepConfigService.getStepComponentConfigJson(dto.getProcedureStepModelId(), dto.getComponentId());
        JSONObject jsonObject = JSONUtil.parseObj(configJson);
        JSONArray jsonArray = jsonObject.getJSONArray(EQUIPMENT_PICTURE_CONFIG);
        if (CollUtil.isEmpty(jsonArray)) {
            throw new BmosException(MesResponseCode.EQUIPMENT_PICTURE_CONFIG_UNCOMPLETED);
        }
        List<EquipmentPictureDataConfig> configList = jsonArray.toList(EquipmentPictureDataConfig.class);
        Map<String, EquipmentPictureDataConfig> configMap = CollectionUtils.convertMap(configList, EquipmentPictureDataConfig::getAcquisitionDataCode);
        EquipmentPictureDataConfig equipmentPictureDataConfig = configMap.get(dto.getAcquisitionDataCode());
        if (equipmentPictureDataConfig == null) {
            throw new BmosException(MesResponseCode.EQUIPMENT_PICTURE_CONFIG_UNCOMPLETED);
        }
        // 根据组件配置和传入值计算纵轴上下限
        EquipmentPictureDataConfig.LineCalculateResult lineResult = equipmentPictureDataConfig.getLineResult(dto.getMinValue(), dto.getMaxValue());
        AcquisitionPictureRangeVO result = new AcquisitionPictureRangeVO();
        result.setLowerValue(lineResult.getLowerValue());
        result.setUpperValue(lineResult.getUpperValue());
        return result;
    }

    @Override
    public void saveAcquisitionPicture(AcquisitionPictureSaveDTO dto) {
        try {
            File file = Base64Util.convertFile(dto.getPicture().split("base64,")[1], dto.getSuffix());
            String path = String.format("/AcquisitionPicture/%s/%s/%s", LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), UUID.randomUUID() + dto.getSuffix());
            String url = minioFileClient.uploadFile(MinioBucket.BMOS_PRODUCT, file, path);
            ExecuteFormData save = buildExecuteFormData(dto, url);
            executeFormDataService.saveResultsAndHandleRelationComponentData(Collections.singletonList(save), dto.getProductPlanId(), dto.getProcedureStepModelId(), dto.getCopyVersion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private ExecuteFormData buildExecuteFormData(AcquisitionPictureSaveDTO dto, String url) {
        ExecuteFormData executeFormData = new ExecuteFormData();
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        executeFormData.setProductPlanId(dto.getProductPlanId());
        executeFormData.setBatchNo(plan.getBatchNo());
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        executeFormData.setProcedureStepModelId(dto.getProcedureStepModelId());
        executeFormData.setReuse(procedureStepModel.getReusable());
        executeFormData.setProcedureStepId(procedureStepModel.getReusable() ? ProcessConstant.REUSE_PROCEDURE_STEP_ID : procedureStepModel.getProcedureStepId());
        executeFormData.setValue(JSONUtil.toJsonStr(buildExtInfo(dto, url)));
        executeFormData.setExtInfo(executeFormData.getValueExtension());
        executeFormData.setRecordItemId(procedureStepModel.getRecordItemId());
        executeFormData.setProcessId(plan.getProcessId());
        executeFormData.setProcessVersion(plan.getProcessVersion());
        executeFormData.setComponentType(BusinessComponentTypeEnum.EQUIPMENT_DATA_DRAW.getValue());
        executeFormData.setFieldId(dto.getFieldId());
        executeFormData.setCopyVersion(dto.getCopyVersion());
        executeFormData.setOperationUser(SysUserHolder.getUser().getUserId());
        executeFormData.setOperationTime(LocalDateTime.now());
        executeFormData.setEmptyValue(false);
        executeFormData.setDiscard(false);
        executeFormData.setSystemCreate(false);
        return executeFormData;
    }

    private AcquisitionPictureExtInfo buildExtInfo(AcquisitionPictureSaveDTO dto, String url) {
        AcquisitionPictureExtInfo acquisitionPictureExtInfo = new AcquisitionPictureExtInfo();
        acquisitionPictureExtInfo.setEquipmentData(dto.getEquipmentData());
        acquisitionPictureExtInfo.setEquipmentInfo(dto.getEquipmentInfo());
        acquisitionPictureExtInfo.setEquipmentId(dto.getEquipmentId());
        acquisitionPictureExtInfo.setAcquisitionUser(SysUserHolder.getUser().getLoginName() + StrUtil.DASHED + SysUserHolder.getUser().getUserName());
        acquisitionPictureExtInfo.setAcquisitionTime(LocalDateTimeUtil.format(LocalDateTime.now(), NORM_DATETIME_PATTERN));
        acquisitionPictureExtInfo.setUrl(minioFileClient.getBucketName(MinioBucket.BMOS_PRODUCT) + url);
        return acquisitionPictureExtInfo;
    }


}
