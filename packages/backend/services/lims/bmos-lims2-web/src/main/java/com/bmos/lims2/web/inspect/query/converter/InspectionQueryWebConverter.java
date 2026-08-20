package com.bmos.lims2.web.inspect.query.converter;

import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerPageQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.EntryByItemQueryDTO;
import com.bmos.lims2.web.inspect.query.vo.req.EntryByItemQueryVO;
import com.bmos.lims2.web.inspect.query.vo.req.SampleLedgerPageQueryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 检验查询Web层对象转换器
 * @Author: yigaohui
 * @Date: 2025/09/05 11:42
 */
@Mapper(componentModel = "spring")
public interface InspectionQueryWebConverter {

    InspectionQueryWebConverter INSTANCE = Mappers.getMapper(InspectionQueryWebConverter.class);

    @Mapping(target = "sampleStatuses", source = "sampleStatuses", qualifiedByName = "normalizeStatuses")
    SampleLedgerPageQueryDTO voToSampleLedgerPageQueryDTO(SampleLedgerPageQueryVO vo);

    @Named("normalizeStatuses")
    default List<String> normalizeStatuses(List<String> statuses) {
        if (statuses == null) {
            return null;
        }
        List<String> result = new ArrayList<>(statuses.size());
        for (String s : statuses) {
            if (s == null) { continue; }
            String t = s.trim();
            // 兼容前端传入的数字编码（与返回值 status 对齐：1-6）
            switch (t) {
                case "1":
                    result.add("SAMPLED");
                    continue;
                case "2":
                    result.add("RECEIVED");
                    continue;
                case "3":
                    result.add("DIVIDED");
                    continue;
                case "4":
                    result.add("COLLECTED");
                    continue;
                case "5":
                    result.add("RECYCLED");
                    continue;
                case "6":
                    result.add("PROCESSED");
                    continue;
                default:
                    // fallthrough to text mapping
            }
            switch (t) {
                case "已取样":
                case "SAMPLED":
                    result.add("SAMPLED");
                    break;
                case "已接收":
                case "RECEIVED":
                    result.add("RECEIVED");
                    break;
                case "已分样":
                case "DIVIDED":
                    result.add("DIVIDED");
                    break;
                case "已领取":
                case "COLLECTED":
                    result.add("COLLECTED");
                    break;
                case "已回收":
                case "RECYCLED":
                    result.add("RECYCLED");
                    break;
                case "已处理":
                case "PROCESSED":
                    result.add("PROCESSED");
                    break;
                default:
                    // ignore unknown
            }
        }
        return result;
    }

    EntryByItemQueryDTO voToEntryByItemQueryDTO(EntryByItemQueryVO vo);
}


