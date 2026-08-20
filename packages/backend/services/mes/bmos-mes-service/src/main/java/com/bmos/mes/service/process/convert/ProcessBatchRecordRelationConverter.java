package com.bmos.mes.service.process.convert;

import com.bmos.mes.service.process.dto.modify.ProcessCopyDTO;
import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.dto.save.ProcessSaveDTO;
import com.bmos.mes.service.process.dto.RelationBatchRecordItemDTO;
import com.bmos.mes.service.process.model.ProcessBatchRecordRelation;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.vo.RelationBatchRecordItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProcessBatchRecordRelationConverter {
    ProcessBatchRecordRelationConverter INSTANCE = Mappers.getMapper(ProcessBatchRecordRelationConverter.class);


    default List<ProcessBatchRecordRelation> convert(ProcessSaveDTO dto, ProcessVersion processVersion) {

        return dto.getBatchRecordItems()
                .stream()
                .map(this::convert)
                .peek(relation -> {
                    relation.setProcessVersion(processVersion.getVersion());
                    relation.setProcessVersionId(processVersion.getId());
                }).collect(Collectors.toList());
    }

    ProcessBatchRecordRelation convert(RelationBatchRecordItemDTO item);

    @Mapping(source = "version", target = "batchRecordVersion")
    List<RelationBatchRecordItemVO> convertList(List<ProcessBatchRecordRelation> relations);

    default List<ProcessBatchRecordRelation> convertList(ProcessModifyDTO dto) {
        return dto.getBatchRecordItems()
                .stream()
                .map(this::convert)
                .peek(e -> {
                    e.setProcessVersionId(dto.getId());
                    e.setProcessVersion(dto.getVersion());
                }).collect(Collectors.toList());
    }

    default List<ProcessBatchRecordRelation> convertList(ProcessVersion processVersion, List<RelationBatchRecordItemDTO> batchRecordItems){
        return batchRecordItems.stream().map(e -> {
            ProcessBatchRecordRelation recordRelation = new ProcessBatchRecordRelation();
            recordRelation.setBatchRecordId(e.getBatchRecordId());
            recordRelation.setBatchRecordVersionId(e.getBatchRecordVersionId());
            recordRelation.setBatchRecordVersion(e.getBatchRecordVersion());
            recordRelation.setProcessVersionId(processVersion.getId());
            recordRelation.setProcessVersion(processVersion.getVersion());
            return recordRelation;
        }).collect(Collectors.toList());
    }
}
