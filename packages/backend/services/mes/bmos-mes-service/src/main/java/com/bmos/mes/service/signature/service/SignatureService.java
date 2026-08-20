package com.bmos.mes.service.signature.service;

import com.bmos.mes.service.signature.controller.dto.SignatureValidateDTO;
import com.bmos.mes.service.signature.controller.dto.UserSignComponentSaveDTO;
import com.bmos.mes.service.signature.controller.dto.UserSignSaveDTO;

public interface SignatureService {
    Boolean validate(SignatureValidateDTO dto);

    /**
     * 保存用户签名
     *
     * @param dto
     * @return
     */
    String save(UserSignSaveDTO dto);

    /**
     * 获取当前登陆人签名
     * @return
     */
    String getUserSignature(String userId);

    /**
     * 保存/修改手写签名组件值
     * @param dto
     * @return
     */
    void saveOrUpdateComponentSignature(UserSignComponentSaveDTO dto);
}
