package com.bmos.lims2.server.audit.complete;

import com.bmos.audit.engine.core.context.RuntimeContext;

public interface TaskComplete {

    Boolean completed(RuntimeContext context);
}
