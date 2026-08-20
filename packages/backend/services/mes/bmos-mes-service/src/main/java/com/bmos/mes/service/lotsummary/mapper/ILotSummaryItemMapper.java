package com.bmos.mes.service.lotsummary.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.lotsummary.model.LotSummaryItem;
import com.bmos.mes.service.lotsummary.vo.LotSummaryDetailItemVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:33
 */
@Mapper
public interface ILotSummaryItemMapper extends BaseMapperX<LotSummaryItem> {

    /**
     * 根据批次id查询批次明细
     * @param lotSummaryId 批次明细列表
     * @return
     */
    List<LotSummaryDetailItemVO> selectVOByLotSummaryId(@Param("lotSummaryId") Long lotSummaryId);


    /**
     * 根据批次id查询批次明细
     * @param lotSummaryId 批次明细列表
     * @return
     */
    default List<LotSummaryItem> selectByLotSummaryId(Long lotSummaryId){
        if (lotSummaryId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotSummaryItem>()
                .eq(LotSummaryItem::getLotSummaryId, lotSummaryId)
        );
    }

    default void deleteByLotSummaryId(Long lotSummaryId){
        if (lotSummaryId == null){
            return;
        }
        delete(new LambdaQueryWrapper<LotSummaryItem>()
                .eq(LotSummaryItem::getLotSummaryId, lotSummaryId)
        );
    }
}
