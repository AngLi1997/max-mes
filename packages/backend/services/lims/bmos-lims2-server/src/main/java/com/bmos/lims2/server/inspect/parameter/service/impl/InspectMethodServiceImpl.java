package com.bmos.lims2.server.inspect.parameter.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.RecordItemTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.entity.BatchRecordItem;
import com.bmos.lims2.server.eln.record.service.BatchRecordItemService;
import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodEffectiveDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodMapper;
import com.bmos.lims2.server.inspect.parameter.service.InspectMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 分析项-方法关联记录Service实现
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@Service
public class InspectMethodServiceImpl implements InspectMethodService {

    @Autowired
    private InspectMethodMapper inspectMethodMapper;

    @Autowired
    private BatchRecordItemService batchRecordItemService;

    @Override
    public List<InspectMethod> listByParameterId(Long parameterId) {
        return inspectMethodMapper.listByParameterId(parameterId);
    }

    @Override
    public List<InspectMethodEffectiveDTO> listEffectiveMethodsByParameterId(Long parameterId) {
        List<InspectMethodEffectiveDTO> list = inspectMethodMapper.selectEffectiveMethodsByParameterId(parameterId);
        if (list == null || list.isEmpty()) {
            return list;
        }
        // 按【方法ID#版本ID】去重，避免异常重复
        Map<String, InspectMethodEffectiveDTO> uniqueByVersion = list.stream()
                .collect(Collectors.toMap(e -> e.getRecordId() + "#" + e.getRecordVersionId(), e -> e, (a, b) -> a));

        // 批量查询这些版本下的记录项，并校验“一个版本仅一个记录项”
        List<Long> versionIds = uniqueByVersion.values().stream()
                .map(InspectMethodEffectiveDTO::getRecordVersionId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<BatchRecordItem> items = batchRecordItemService.queryItemListByVersionIdList(versionIds);
        Map<Long, List<BatchRecordItem>> versionIdToContentItems = items.stream()
                .filter(i -> Objects.equals(i.getItemType(), RecordItemTypeEnum.CONTENT.getType()))
                .collect(Collectors.groupingBy(BatchRecordItem::getRecordVersionId));

        uniqueByVersion.values().forEach(dto -> {
            List<BatchRecordItem> contentItems = versionIdToContentItems.getOrDefault(dto.getRecordVersionId(), null);
            if (contentItems == null || contentItems.isEmpty()) {
                // 无记录项则不赋值（保持为null）；如需强制，亦可抛业务异常
                dto.setRecordItemId(null);
                return;
            }
            long distinctCount = contentItems.stream().map(BatchRecordItem::getItemId).distinct().count();
            if (distinctCount > 1) {
                throw new BmosException(LimsResponseCode.METHOD_ITEM_COUNT_ERROR);
            }
            dto.setRecordItemId(contentItems.get(0).getItemId());
        });

        return uniqueByVersion.values().stream().collect(Collectors.toList());
    }
}


