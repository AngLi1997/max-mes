package com.bmos.mes.service.process.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.process.dto.save.ProcessRecordOrderSaveDTO;
import com.bmos.mes.service.process.model.ProcessRecordOrder;
import com.bmos.mes.service.process.vo.ProcessRecordOrderVO;
import com.bmos.mes.service.process.vo.ProcessRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface ProcessRecordOrderConverter {
    ProcessRecordOrderConverter INSTANCE = Mappers.getMapper(ProcessRecordOrderConverter.class);

    default List<ProcessRecordOrderVO> convertList(List<ProcessRecordVO> records,
                                                   List<ProcessRecordOrder> relations) {

        Map<Long, Map<Boolean, List<ProcessRecordVO>>> recordsMap = records.stream()
                .collect(Collectors.groupingBy(ProcessRecordVO::getRecordItemId,
                        Collectors.groupingBy(ProcessRecordVO::getReusable)));

        Map<Long, Map<Long, Long>> orderMap = relations.stream()
                .collect(Collectors.groupingBy(ProcessRecordOrder::getRecordItemId,
                                Collectors.groupingBy(ProcessRecordOrder::getProcedureStepModelId,
                                        Collectors.collectingAndThen(
                                                Collectors.collectingAndThen(
                                                        Collectors.maxBy(Comparator.comparing(ProcessRecordOrder::getRecordItemOrder)),
                                                        Optional::get),
                                                ProcessRecordOrder::getRecordItemOrder)
                                )
                        )
                );


        List<ProcessRecordOrderVO> result = new ArrayList<>();
        recordsMap.forEach((k, v) ->
                v.forEach((reuse, data) -> {
                    if (reuse) {
                        ProcessRecordOrderVO vo = new ProcessRecordOrderVO();
                        String name = data.stream().map(e -> e.getProcedureName() + StrUtil.DASHED + e.getProcedureStepName()).collect(Collectors.joining(StrUtil.COMMA));
                        vo.setProcedureName(name);
                        vo.setRecordItemId(k);
                        // 先更改归档顺序再添加节点 查看顺序会报错 需要给新增的节点加默认顺序 如下已修改 需要测试
                        ProcessRecordVO recordVO = data.stream().min(Comparator.comparing(ProcessRecordVO::getId)).get();
                        vo.setRecordItemOrder(Optional.ofNullable(orderMap.get(k)).orElse(new HashMap<>(1)).getOrDefault(0L, recordVO.getId()));
                        vo.setId(recordVO.getId());
                        vo.setRecordVersionId(recordVO.getRecordVersionId());
                        vo.setRecordItemName(recordVO.getRecordItemName());
                        vo.setCreateTime(recordVO.getCreateTime());
                        vo.setReusable(reuse);
                        vo.setProcedureStepId(recordVO.getProcedureStepId());
                        result.add(vo);
                    } else {
                        data.forEach(d -> {
                            ProcessRecordOrderVO vo = new ProcessRecordOrderVO();
                            vo.setProcedureName(d.getProcedureName() + StrUtil.DASHED + d.getProcedureStepName());
                            vo.setRecordItemId(k);
                            vo.setRecordItemOrder(Optional.ofNullable(orderMap.get(k)).orElse(new HashMap<>(1)).getOrDefault(d.getId(), d.getId()));
                            vo.setId(d.getId());
                            vo.setRecordVersionId(d.getRecordVersionId());
                            vo.setRecordItemName(d.getRecordItemName());
                            vo.setCreateTime(d.getCreateTime());
                            vo.setReusable(reuse);
                            vo.setProcedureStepId(d.getProcedureStepId());
                            result.add(vo);
                        });
                    }

                }));

        List<ProcessRecordOrderVO> orderedList = result.stream().sorted((o1, o2) -> {
            if (Objects.equals(o1.getRecordItemOrder(), o2.getRecordItemOrder())) {
                return o1.getId().compareTo(o2.getId());
            }
            return o1.getRecordItemOrder().compareTo(o2.getRecordItemOrder());
        }).collect(Collectors.toList());
        //orderMap为空意味着还没排过序
        if (CollUtil.isNotEmpty(orderMap)) {
            return orderedList;
        }
        long count = 0L;
        for (ProcessRecordOrderVO vo : orderedList) {
            vo.setRecordItemOrder(count++);
        }
        return orderedList;
    }

    default List<ProcessRecordOrder> convertList(ProcessRecordOrderSaveDTO dto) {
        return dto.getRecordOrders().stream().map(e -> {
            ProcessRecordOrder processRecordOrder = new ProcessRecordOrder();
            processRecordOrder.setProcessId(dto.getProcessId());
            processRecordOrder.setProcessVersion(dto.getProcessVersion());
            processRecordOrder.setProcessVersionId(dto.getProcessVersionId());
            processRecordOrder.setRecordItemId(e.getRecordItemId());
            processRecordOrder.setRecordItemOrder(e.getRecordItemOrder());
            processRecordOrder.setRecordVersionId(e.getRecordVersionId());
            processRecordOrder.setReusable(e.getReusable());
            processRecordOrder.setProcedureStepModelId(e.getReusable() ? 0L : e.getId());
            return processRecordOrder;
        }).collect(Collectors.toList());
    }
}
