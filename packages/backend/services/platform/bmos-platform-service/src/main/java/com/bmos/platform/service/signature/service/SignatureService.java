package com.bmos.platform.service.signature.service;


import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.signature.dto.SignatureDTO;
import com.bmos.platform.service.signature.dto.SignatureExportDTO;
import com.bmos.platform.service.signature.dto.SignatureQueryPageDTO;
import com.bmos.platform.service.signature.vo.SignaturePageVO;
import com.bmos.platform.service.signature.vo.SignatureValidateResVO;
import com.bmos.platform.service.system.user.dto.CheckSignaturePasswordConfigDTO;
import com.bmos.platform.service.system.user.dto.UpdateSignaturePasswordDTO;
import com.bmos.platform.service.system.user.vo.CheckSignaturePasswordConfigResultVO;

import java.io.IOException;
import java.util.List;

public interface SignatureService {
    Boolean validate(SignatureDTO dto);

    CommonPage<SignaturePageVO> getPage(SignatureQueryPageDTO dto);

    void exportSignatureLog(SignatureExportDTO dto) throws IOException;

    SignatureValidateResVO validateV2(SignatureDTO dto);

    /**
     * 校验当前用户是否配置了签名密码
     * @return
     */
    List<CheckSignaturePasswordConfigResultVO> checkSignaturePasswordConfig(CheckSignaturePasswordConfigDTO dto);

    /**
     * 校验当前用户的签名密码
     * @param dto
     * @return
     */
    SignatureValidateResVO validateV3(SignatureDTO dto);

    /**
     * 更新当前用户签名密码
     * @param dto
     */
    void updateSignaturePassword(UpdateSignaturePasswordDTO dto);
}
