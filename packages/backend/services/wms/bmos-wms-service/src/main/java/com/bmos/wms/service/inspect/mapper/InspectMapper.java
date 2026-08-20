package com.bmos.wms.service.inspect.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.wms.service.inspect.model.Inspect;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * WMS 请验单 Mapper（mirror MES InspectMapper）。
 */
@Mapper
public interface InspectMapper extends BaseMapperX<Inspect> {

    /** 按 LIMS 检验单号查询。 */
    default Inspect selectByInspectNo(String inspectNo) {
        return selectOne(new LambdaQueryWrapperX<Inspect>().eq(Inspect::getInspectNo, inspectNo));
    }

    /** 按库存批次id查询历史（最新在前）。 */
    default List<Inspect> selectByBatchId(Long batchId) {
        return selectList(Wrappers.lambdaQuery(Inspect.class)
                .eq(Inspect::getBatchId, batchId)
                .orderByDesc(Inspect::getCreateTime));
    }

    /** 按 batchNo+cargoId 查询历史。 */
    default List<Inspect> selectByBatchAndCargo(String materialBatchNo, Long cargoId) {
        return selectList(Wrappers.lambdaQuery(Inspect.class)
                .eq(Inspect::getMaterialBatchNo, materialBatchNo)
                .eq(Inspect::getCargoId, cargoId)
                .orderByDesc(Inspect::getCreateTime));
    }

    /** 按一批 inspectNo 查询。 */
    default List<Inspect> selectByInspectNoList(Collection<String> inspectNoList) {
        return selectList(new LambdaQueryWrapperX<Inspect>().in(Inspect::getInspectNo, inspectNoList));
    }
}
