package com.bmos.mes.service.inspect.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.component.ComponentDetail;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.components.comps.InspectResultComponentFromDataOPT;
import com.bmos.mes.service.components.dto.FormDataOPT;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.formula.mapper.ProductFormulaMaterialMapper;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.inspect.controller.vo.InspectPageVO;
import com.bmos.mes.service.inspect.convert.InspectConvert;
import com.bmos.mes.service.inspect.mapper.InspectMapper;
import com.bmos.mes.service.inspect.mapper.InspectResultMapper;
import com.bmos.mes.service.inspect.model.Inspect;
import com.bmos.mes.service.inspect.model.InspectResult;
import com.bmos.mes.service.inspect.service.InspectComponentService;
import com.bmos.mes.service.inspect.service.dto.InspectComponentConfirmFillDTO;
import com.bmos.mes.service.inspect.service.dto.InspectResultPageQueryDTO;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchFieldVO;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.CUSTOM_FIELD;

@Service
public class InspectComponentServiceImpl implements InspectComponentService {

    @Resource
    private InspectMapper inspectMapper;

    @Resource
    private BusinessComponentManager businessComponentManager;

    @Resource
    private InspectResultMapper inspectResultMapper;

    @Resource
    private MaterialBatchFieldService materialBatchFieldService;

    @Resource
    private IStorageMaterialBatchService storageMaterialBatchService;

    @Resource
    private ProductFormulaMaterialMapper formulaMaterialMapper;

    @Override
    public CommonPage<InspectPageVO> queryNotRejectInspectResultPage(InspectResultPageQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<Inspect> list = inspectMapper.queryNotRejectInspectResultPage(dto.getProductPlanId());
        return CommonPage.convertPage(list, InspectConvert.INSTANCE::convert2PageVO);
    }

    @Override
    public void confirmFillFormData(InspectComponentConfirmFillDTO dto) {
        BusinessComponentInstance instance = businessComponentManager.getComponentInstanceById(dto.getInstanceId());
        if (instance == null) {
            throw new BmosException(MesResponseCode.INSPECT_RESULT_COMPONENT_INSTANCE_NOT_EXIST);
        }
        Inspect inspect = inspectMapper.selectById(dto.getInspectId());
        if (inspect == null) {
            throw new BmosException(MesResponseCode.INSPECT_NOT_EXIST);
        }
        FormDataOPT formDataOPTTree = businessComponentManager.getFormDataOPTTree(instance.getId());
        // 过滤出当前执行回填的分组
        FormDataOPT group = formDataOPTTree.getChildren()
                .stream()
                .filter(opt -> Objects.equals(opt.getComponentId(), dto.getGroupComponentId()))
                .findFirst().get();
        List<FormDataOPT> children = group.getChildren();
        if (CollUtil.isEmpty(children)) {
            return;
        }
        // 处理非自定义字段组件
        ProductFormulaMaterial formulaMaterial = formulaMaterialMapper.selectById(inspect.getFormulaMaterialId());
        InspectResultComponentFromDataOPT opt = buildOpt(inspect, formulaMaterial);
        businessComponentManager.fillFormDataOPT(opt, children);
        // 处理自定义组件
        handleCustomField(inspect, children);
        // 保存结果
        businessComponentManager.saveFormDataOPT(children, instance);
    }

    private void handleCustomField(Inspect inspect, List<FormDataOPT> children) {
        List<InspectResult> inspectResults = inspectResultMapper.selectByInspectId(inspect.getId());
        Map<String, String> resultMap = CollectionUtils.convertMap(inspectResults, InspectResult::getInspectDictNo, InspectResult::getInspectResult);

        StorageMaterialBatch batch = storageMaterialBatchService.queryMaterialBatchByNoAndMaterialId(inspect.getMaterialId(), inspect.getMaterialBatchNo());
        if (batch == null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        List<MaterialBatchFieldVO> materialField = materialBatchFieldService.queryMaterialField(batch.getId());
        resultMap.putAll(CollectionUtils.convertMap(materialField, MaterialBatchFieldVO::getField, MaterialBatchFieldVO::getFieldValue));
        if (CollUtil.isNotEmpty(resultMap)) {
            for (FormDataOPT formDataOPT : children) {
                if (StringUtils.equals(formDataOPT.getComponentType(), CUSTOM_FIELD.getValue())) {
                    String componentDetailStr = formDataOPT.getComponentDetail();
                    if (StringUtils.isNotBlank(componentDetailStr)) {
                        ComponentDetail componentDetail = JSON.parseObject(componentDetailStr, ComponentDetail.class);
                        String newValue = resultMap.get(componentDetail.getFieldData());
                        formDataOPT.setValue(newValue);
                    }
                }
            }
        }
    }

    private InspectResultComponentFromDataOPT buildOpt(Inspect inspect, ProductFormulaMaterial formulaMaterial) {
        InspectResultComponentFromDataOPT opt = new InspectResultComponentFromDataOPT();
        opt.setInspectNo(inspect.getInspectNo());
        opt.setMaterialName(inspect.getMaterialName());
        opt.setMaterialCode(inspect.getMaterialMergeCode());
        opt.setMaterialSpecification(formulaMaterial.getMaterialSpecification());
        opt.setMaterialBatchNo(inspect.getMaterialBatchNo());
        opt.setVerifyUser(inspect.getInspector());
        opt.setVerifyDate(DateUtil.format(inspect.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
        return opt;
    }


}
