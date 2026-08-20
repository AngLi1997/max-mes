package com.bmos.platform.service.log.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.logging.enums.OperationTypeEnum;
import com.bmos.logging.model.LogModel;
import com.bmos.platform.service.log.model.LoginLogModel;
import com.bmos.platform.service.log.model.OperationLogModel;
import com.bmos.platform.service.log.vo.*;
import com.bmos.platform.service.system.user.enums.LoginActionEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface PlatformLogConvert {

    PlatformLogConvert INSTANCE = Mappers.getMapper(PlatformLogConvert.class);

    default List<LoginLogExcelVO> ConvertToExcelVO(List<LoginLogModel> list, HttpServletRequest request){
        List<LoginLogExcelVO> listVOs = new ArrayList<>();
        if (CollUtil.isEmpty(list)) {
            return listVOs;
        }
        for (LoginLogModel loginLogModel : list) {
            LoginLogExcelVO loginLogExcelVO = new LoginLogExcelVO();
            loginLogExcelVO.setLoginName(loginLogModel.getLoginName());
            loginLogExcelVO.setUserName(loginLogModel.getUserName());
            loginLogExcelVO.setIp(loginLogModel.getIp());
            loginLogExcelVO.setOperationAction(I18nUtils.getEnumMessage(CommonEnum.getEnumByValue(LoginActionEnum.class, loginLogModel.getOperationAction())));
            loginLogExcelVO.setOperationState(loginLogModel.getOperationState());
            loginLogExcelVO.setCreateTime(loginLogModel.getCreateTime());
            Object[] args = null;
            if (StrUtil.isNotEmpty(loginLogModel.getDescriptionParam())){
                args = loginLogModel.getDescriptionParam().split(StrUtil.COMMA);
            }
            loginLogExcelVO.setDescription(I18nUtils.getResponseMessage(loginLogModel.getDescriptionCode(), String.valueOf(loginLogModel.getDescriptionCode()),args, request));
            listVOs.add(loginLogExcelVO);
        }
        return listVOs;
    }

    default OperationLogDetailVO convert2DetailInfoVO(OperationLogModel operationLogDetailInfo){
        if (ObjectUtil.isNull(operationLogDetailInfo)) {
            return null;
        }
        OperationLogDetailVO operationLogDetailVO = new OperationLogDetailVO();
        operationLogDetailVO.setId(operationLogDetailInfo.getId());
        operationLogDetailVO.setOperationType(CommonEnum.getEnumByValue(OperationTypeEnum.class, operationLogDetailInfo.getOperationType()));
        operationLogDetailVO.setOperationBusiness(operationLogDetailInfo.getOperationBusiness());
        operationLogDetailVO.setOperationTime(operationLogDetailInfo.getCreateTime());
        operationLogDetailVO.setRemark(operationLogDetailInfo.getRemark());
        operationLogDetailVO.setOperationObject(operationLogDetailInfo.getOperationObject());
        operationLogDetailVO.setLoginName(operationLogDetailInfo.getLoginName());
        operationLogDetailVO.setUserName(operationLogDetailInfo.getUserName());
        operationLogDetailVO.setMenuId(operationLogDetailInfo.getMenuId());
        operationLogDetailVO.setUserId(operationLogDetailInfo.getUserId());
        return operationLogDetailVO;
    }

    OperationLogModel convert2OperationLogModel(LogModel logModel);
}
