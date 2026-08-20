package com.bmos.mes.service.audit.complete;

import com.bmos.audit.engine.core.context.RuntimeContext;

/**
 * 或签
 *
 * @author renjinguang
 */
public class OrViseComplete implements TaskComplete {
    @Override
    public Boolean completed(RuntimeContext context) {
        return Boolean.TRUE;
    }
}
