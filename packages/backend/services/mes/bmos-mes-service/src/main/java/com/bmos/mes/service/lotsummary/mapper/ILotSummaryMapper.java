package com.bmos.mes.service.lotsummary.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.lotsummary.dto.LotSummaryPageQuery;
import com.bmos.mes.service.lotsummary.model.LotSummary;
import com.bmos.mes.service.lotsummary.vo.LotSummaryPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:33
 */
@Mapper
public interface ILotSummaryMapper extends BaseMapperX<LotSummary> {

    default boolean existsName(String name){
        if (StrUtil.isBlank(name)){
            return false;
        }
        return exists(new LambdaUpdateWrapper<LotSummary>().eq(LotSummary::getName, name));
    }

    List<LotSummaryPageVO> queryPage(@Param("pageQuery") LotSummaryPageQuery pageQuery,
                                     @Param("productIds") List<Long> productIds);
}
