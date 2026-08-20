package com.bmos.platform.service.signature.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.signature.dto.SignatureExportDTO;
import com.bmos.platform.service.signature.dto.SignatureQueryPageDTO;
import com.bmos.platform.service.signature.model.Signature;
import com.bmos.platform.service.signature.vo.SignatureExcelVO;
import com.bmos.platform.service.signature.vo.SignaturePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SignatureMapper extends BaseMapperX<Signature> {

    List<SignaturePageVO> selectPageList(SignatureQueryPageDTO dto);

    List<SignatureExcelVO> selectExcelVOByIds(@Param("list") List<Long> selectIds);

    List<SignatureExcelVO> selectByCondition(SignatureExportDTO dto);
}
