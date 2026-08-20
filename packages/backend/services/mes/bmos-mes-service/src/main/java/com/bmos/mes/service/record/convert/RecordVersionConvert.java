package com.bmos.mes.service.record.convert;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.mes.service.record.dto.CopyVersionDTO;
import com.bmos.mes.service.record.dto.RecordVersionDTO;
import com.bmos.mes.service.record.model.BatchRecord;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import com.bmos.mes.service.record.vo.CiteRecordVO;
import com.bmos.mes.service.record.vo.PageRecordAuditVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface RecordVersionConvert {
    RecordVersionConvert INSTANCE = Mappers.getMapper(RecordVersionConvert.class);


    BatchRecordVersion convertToDo(RecordVersionDTO dto);

    BatchRecordVersion convertCopyToDo(CopyVersionDTO dto);

    default List<PageRecordAuditVO> convertList(List<TaskListResp> dataList, Map<Long, PageRecordAuditVO> map) {
        List<PageRecordAuditVO> list = new ArrayList<>();
        dataList.forEach(data -> {
            PageRecordAuditVO vo = map.get(Long.valueOf(data.getBusinessKey()));
            if (ObjectUtil.isNotEmpty(vo)) {
                vo.setNodeName(data.getElementName());
                vo.setProcessStartBy(data.getProcessStartBy());
                vo.setProcessStartTime(data.getProcessStartTime());
                vo.setPayload(data.getPayload());
                vo.setTaskId(data.getTaskId());
                vo.setDeploymentId(data.getDeploymentId());
                vo.setExecutionId(data.getExecutionId());
                vo.setNodeId(data.getElementKey());
                list.add(vo);
            }
        });
        return list;
    }

}
