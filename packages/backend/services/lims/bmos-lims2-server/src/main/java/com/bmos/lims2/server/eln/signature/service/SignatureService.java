package com.bmos.lims2.server.eln.signature.service;


import com.bmos.lims2.server.eln.signature.dto.SignatureValidateDTO;
import com.bmos.lims2.server.eln.signature.dto.UserSignComponentSaveDTO;
import com.bmos.lims2.server.eln.signature.dto.UserSignSaveDTO;

import java.util.List;

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

    /**
     * 批量保存/修改手写签名组件值
     * @param dtoList
     */
    void batchSaveOrUpdateComponentSignature(List<UserSignComponentSaveDTO> dtoList);
}
