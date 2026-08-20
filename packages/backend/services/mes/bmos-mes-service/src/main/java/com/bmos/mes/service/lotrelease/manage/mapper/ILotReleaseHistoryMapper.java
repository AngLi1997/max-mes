package com.bmos.mes.service.lotrelease.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.lotrelease.manage.model.LotReleaseHistory;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/2 09:26
 */
@Mapper
public interface ILotReleaseHistoryMapper extends BaseMapperX<LotReleaseHistory> {

    default List<LotReleaseHistory> queryHistoryById(Long id){
        if (id == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotReleaseHistory>()
                .eq(LotReleaseHistory::getLotReleaseId, id)
                .orderByDesc(LotReleaseHistory::getCreateTime)
        );
    }
}
