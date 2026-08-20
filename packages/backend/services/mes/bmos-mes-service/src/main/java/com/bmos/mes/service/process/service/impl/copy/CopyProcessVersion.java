package com.bmos.mes.service.process.service.impl.copy;

import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.service.process.model.ProcessVersion;
import lombok.Data;

@Data
public class CopyProcessVersion {

    private Long processVersionId;

    private String processVersion;

    private Long processId;

    private String processModelId;

    public CopyProcedure initCopyProcedure() {
        CopyProcedure copyProcedure = new CopyProcedure();
        copyProcedure.setProcessVersionId(processVersionId);
        copyProcedure.setProcessVersion(processVersion);
        copyProcedure.setProcessId(processId);
        copyProcedure.setProcessModelId(processModelId);
        // id初始化
        copyProcedure.setProcedureModelId(IdUtils.getSnowflake());
        return copyProcedure;
    }

    public void convertFromVersion(ProcessVersion processVersion) {
        this.processVersionId = processVersion.getId();
        this.processVersion = processVersion.getVersion();
        this.processId = processVersion.getProcessId();
        this.processModelId = processVersion.getProcessModelId();
    }
}
