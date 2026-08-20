package com.bmos.lims2.server.eln.signature.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.adaptor.platform.dto.ValidatePwd;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.config.minio.MinioFileClient;
import com.bmos.lims2.server.eln.entry.dto.BusinessComponentBatchSaveDTO;
import com.bmos.lims2.server.eln.entry.dto.BusinessDataHandleBaseDTO;
import com.bmos.lims2.server.eln.entry.dto.ElnEntryContext;
import com.bmos.lims2.server.eln.entry.dto.HandleSignInfo;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataService;
import com.bmos.lims2.server.eln.record.component.BusinessComponentStrategy;
import com.bmos.lims2.server.eln.record.service.BatchRecordComponentService;
import com.bmos.lims2.server.eln.record.vo.ComponentListVO;
import com.bmos.lims2.server.eln.signature.dto.SignatureValidateDTO;
import com.bmos.lims2.server.eln.signature.dto.UserSignComponentSaveDTO;
import com.bmos.lims2.server.eln.signature.dto.UserSignSaveDTO;
import com.bmos.lims2.server.eln.signature.service.SignatureService;
import com.bmos.lims2.server.platform.system.code.PlatformCodeFeign;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.facade.system.execute.parameter.feign.BusinessParameterFeign;
import com.bmos.platform.facade.system.user.dto.UserSignSaveFeignDTO;
import com.bmos.platform.facade.system.user.feign.UserSignFeign;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new BmosException(LimsResponseCode.USER_SIGN_SAVE_FAIL);
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
            throw new BmosException(LimsResponseCode.COMPONENT_NOT_EXIST);
        }
        // 前端已经做了权限校验，无需在进行二次校验
        // 获取当前人的签名地址
        String url = StrUtil.EMPTY;
        if (Objects.nonNull(dto.getUserId())){
            ResponseInfo<String> stringResponseInfo = FeignUtils.handleRequest(data -> userSignFeign.getUserSign(data), dto.getUserId());
            if (StrUtil.isEmpty(stringResponseInfo.getData())){
                throw new BmosException(LimsResponseCode.USER_SIGN_NOT_EXIST);
            }
            url = stringResponseInfo.getData();
        } else {
            ResponseInfo<String> responseData = FeignUtils.handleRequest(data -> userSignFeign.getUserSign(data), BusinessParameterCodeConstants.MES_RECORD_EMPTY_DATA);
            url = responseData.getData();
        }
        // 生成表单数据
        List<ExecuteFormData> formDataList = this.generateFormData(componentListVO, new HandleSignInfo(url, dto.getUserId()), dto);
        for (ExecuteFormData executeFormData : formDataList) {
            executeFormData.setRemark(dto.getRemark());
            executeFormData.setParameterId(dto.getParameterId());
            executeFormData.setParameterConfigId(dto.getParameterConfigId());
            executeFormData.setReviewUser(dto.getReviewUserId());
            executeFormData.setRecordItemId(dto.getRecordItemId());
        }
        try{
            executeFormDataService.saveResultsAndHandleRelationComponentData(formDataList, dto);
        } catch (Exception e){
            log.error("保存手写签名组件信息失败", e);
            throw new BmosException(LimsResponseCode.USER_HANDLE_SIGN_ERROR);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveOrUpdateComponentSignature(List<UserSignComponentSaveDTO> dtoList) {
        if (ObjectUtil.isEmpty(dtoList)) {
            return;
        }
        for (UserSignComponentSaveDTO dto : dtoList) {
            this.saveOrUpdateComponentSignature(dto);
        }
    }

    private List<ExecuteFormData> generateFormData(ComponentListVO componentListVO, HandleSignInfo signInfo, BusinessDataHandleBaseDTO dto) {
        List<ExecuteFormData> executeFormDataList = Lists.newArrayList();
        ElnEntryContext productionDetailInfo = new ElnEntryContext();
        BusinessComponentBatchSaveDTO batchSaveDTO = BeanUtil.toBean(dto, BusinessComponentBatchSaveDTO.class);
        productionDetailInfo.setDto(batchSaveDTO);
        productionDetailInfo.setSignInfo(signInfo);
        componentStrategyMap.get(componentListVO.getComponentType())
                .handleBusinessComponent(executeFormDataList, componentListVO, productionDetailInfo);
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
