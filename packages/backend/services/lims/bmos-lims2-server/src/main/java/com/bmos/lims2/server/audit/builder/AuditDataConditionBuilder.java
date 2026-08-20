package com.bmos.lims2.server.audit.builder;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.message.strategy.AbstractAuditMessageStrategy;

/**
 * @author renjinguang
 */
public class AuditDataConditionBuilder {

    public static AbstractAuditMessageStrategy build(String category) {
        if (StrUtil.isEmpty(category) || !AuditCategoryServiceEnum.codes().contains(category)) {
            throw new BmosException(LimsResponseCode.FLOW_PAYLOAD_ERROR);
        }
        return SpringUtil.getBean(AuditCategoryServiceEnum.getService(category));
    }
}
