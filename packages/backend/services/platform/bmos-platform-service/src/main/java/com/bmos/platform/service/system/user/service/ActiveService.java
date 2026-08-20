package com.bmos.platform.service.system.user.service;

import com.bmos.adaptor.active.ActiveApiAdaptor;
import com.bmos.platform.facade.active.dto.LicenseActiveDTO;
import com.bmos.platform.facade.active.dto.LicenseParamDTO;

public interface ActiveService extends ActiveApiAdaptor {
    String save(String activeStr);

    /**
     * 判断激活码是否生效
     *
     * @param paramDTO
     * @return
     */
    LicenseActiveDTO valid(LicenseParamDTO paramDTO);
}
