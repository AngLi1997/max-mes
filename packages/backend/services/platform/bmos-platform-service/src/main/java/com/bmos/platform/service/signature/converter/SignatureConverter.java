package com.bmos.platform.service.signature.converter;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;
import com.bmos.platform.common.enums.signature.SignatureTypeEnum;
import com.bmos.platform.service.signature.dto.SignatureDTO;
import com.bmos.platform.service.signature.model.Signature;
import com.bmos.platform.service.signature.vo.SignatureExcelVO;
import com.bmos.platform.service.signature.vo.SignaturePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface SignatureConverter {
    SignatureConverter INSTANCE = Mappers.getMapper(SignatureConverter.class);


    Signature convertToSignature(SignatureDTO dto);

    List<SignatureExcelVO> convertVO2ExcelVO(List<SignaturePageVO> signaturePageVOS);

}
