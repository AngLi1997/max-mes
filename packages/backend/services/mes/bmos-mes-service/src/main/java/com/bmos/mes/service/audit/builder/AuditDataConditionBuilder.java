package com.bmos.mes.service.audit.builder;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.audit.condition.AbstractAuditDataCondition;

/**
 * @author renjinguang
 */
public class AuditDataConditionBuilder {

    public static AbstractAuditDataCondition build(String category) {
        if (StrUtil.isEmpty(category) || !AuditCategoryServiceEnum.codes().contains(category)) {
            throw new BmosException(MesResponseCode.FLOW_PAYLOAD_ERROR);
        }
        return SpringUtil.getBean(AuditCategoryServiceEnum.getService(category));
    }
}
