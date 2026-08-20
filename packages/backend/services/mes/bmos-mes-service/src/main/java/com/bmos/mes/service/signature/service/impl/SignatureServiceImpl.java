package com.bmos.mes.service.signature.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.adaptor.platform.dto.ValidatePwd;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioFileClient;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.HandleSignInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.signature.controller.dto.SignatureValidateDTO;
import com.bmos.mes.service.signature.controller.dto.UserSignComponentSaveDTO;
import com.bmos.mes.service.signature.controller.dto.UserSignSaveDTO;
import com.bmos.mes.service.signature.service.SignatureService;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.user.dto.UserSignSaveFeignDTO;
import com.bmos.platform.facade.system.user.feign.UserSignFeign;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SignatureServiceImpl implements SignatureService {

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    MinioFileClient minioFileClient;

    @Autowired
    UserSignFeign userSignFeign;

    @Autowired
    private Map<String, BusinessComponentStrategy> componentStrategyMap;

    @Autowired
    private BatchRecordComponentService recordComponentService;

    @Autowired
    ExecuteFormDataService executeFormDataService;
    @Autowired
    private PlatformCodeFeign platformCodeFeign;
    @Autowired
    private BusinessParameterFeign businessParameterFeign;


    @Override
    public Boolean validate(SignatureValidateDTO dto) {
        UserInfoVO operator = platformApiAdaptor.validatePassword(new ValidatePwd(dto.getLoginName(), dto.getPassword()));
        return ObjectUtil.isNull(operator);
    }

    @Override
    public String save(UserSignSaveDTO dto) {
        UserSignSaveFeignDTO feignDTO = new UserSignSaveFeignDTO();
        feignDTO.setUserId(dto.getUserId());
        feignDTO.setFileBase64Content(dto.getFileBase64Content());
        feignDTO.setSuffix(dto.getSuffix());
        ResponseInfo<String> responseInfo = FeignUtils.handleRequest(data -> userSignFeign.saveUserSign(data), feignDTO);
        if (!responseInfo.isSuccess()){
            throw new BmosException(MesResponseCode.USER_SIGN_SAVE_FAIL);
        }
        return responseInfo.getMessage();
    }

    @Override
    public String getUserSignature(String userId) {
        ResponseInfo<String> responseInfo = FeignUtils.handleRequest(data -> userSignFeign.getUserSign(data), userId);
        if (responseInfo.isSuccess()){
            return responseInfo.getData();
        }
        return StrUtil.EMPTY;
    }

    @Override
    public void saveOrUpdateComponentSignature(UserSignComponentSaveDTO dto) {
        ComponentListVO componentListVO = findComponentList(dto.getRecordVersionId(), dto.getRecordItemId(), dto.getComponentId());
        if (ObjectUtil.isNull(componentListVO)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        // 前端已经做了权限校验，无需在进行二次校验
        // 获取当前人的签名地址
        String url = StrUtil.EMPTY;
        if (Objects.nonNull(dto.getUserId())){
            ResponseInfo<String> stringResponseInfo = FeignUtils.handleRequest(data -> userSignFeign.getUserSign(data), dto.getUserId());
            if (StrUtil.isEmpty(stringResponseInfo.getData())){
                throw new BmosException(MesResponseCode.USER_SIGN_NOT_EXIST);
            }
            url = stringResponseInfo.getData();
        } else {
            ResponseInfo<String> responseData = FeignUtils.handleRequest(data -> userSignFeign.getUserSign(data), BusinessParameterCodeConstants.MES_RECORD_EMPTY_DATA);
            url = responseData.getData();
        }
        // 生成表单数据
        List<ExecuteFormData> formDataList = this.generateFormData(componentListVO, new HandleSignInfo(url, dto.getUserId()), dto);
        for (ExecuteFormData executeFormData : formDataList) {
            executeFormData.setReuse(dto.getReuse());
            executeFormData.setRemark(dto.getRemark());
            executeFormData.setProcedureStepId(dto.getProcedureStepId());
            executeFormData.setCopyVersion(dto.getCopyVersion());
            executeFormData.setReviewUser(dto.getReviewUserId());
            executeFormData.setRecordItemId(dto.getRecordItemId());
        }
        try{
            executeFormDataService.saveResultsAndHandleRelationComponentData(formDataList, dto);
        } catch (Exception e){
            log.error("保存手写签名组件信息失败", e);
            throw new BmosException(MesResponseCode.USER_HANDLE_SIGN_ERROR);
        }

    }

    private List<ExecuteFormData> generateFormData(ComponentListVO componentListVO, HandleSignInfo signInfo, BusinessDataHandleBaseDTO dto) {
        List<ExecuteFormData> executeFormDataList = Lists.newArrayList();
        ProductionDetailInfo productionDetailInfo = new ProductionDetailInfo();
        BusinessComponentBatchSaveDTO batchSaveDTO = BeanUtil.toBean(dto, BusinessComponentBatchSaveDTO.class);
        productionDetailInfo.setDto(batchSaveDTO);
        productionDetailInfo.setSignInfo(signInfo);
        componentStrategyMap.get(componentListVO.getComponentType())
                .handleBusinessComponent(executeFormDataList, componentListVO, productionDetailInfo, new HashMap<>(), null);
        return executeFormDataList;
    }


    /**
     * 根据组件相关信息查询其所属的组件树
     * @param recordVersionId
     * @param recordItemId
     * @param componentId
     * @return
     */
    private ComponentListVO findComponentList(Long recordVersionId, Long recordItemId, Long componentId) {
        // 查询当前componentId
        ComponentListVO componentListVO =
                recordComponentService.selectUsedComponentDetail(recordVersionId,
                        recordItemId,
                        componentId);
        return componentListVO;
    }
}
