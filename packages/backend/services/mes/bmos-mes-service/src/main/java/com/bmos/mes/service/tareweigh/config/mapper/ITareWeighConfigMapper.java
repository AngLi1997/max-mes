package com.bmos.mes.service.tareweigh.config.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigQuery;
import com.bmos.mes.service.tareweigh.config.model.TareWeighConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:22
 */
@Mapper
public interface ITareWeighConfigMapper extends BaseMapper<TareWeighConfig> {

    default List<TareWeighConfig> queryPage(TareWeighConfigQuery query) {
        return selectList(new LambdaQueryWrapper<TareWeighConfig>()
                .like(query.getTareWeigh() != null, TareWeighConfig::getTareWeigh, query.getTareWeigh())
                .like(StringUtils.isNotBlank(query.getDescribeInfo()), TareWeighConfig::getDescribeInfo, query.getDescribeInfo())
                .orderByDesc(TareWeighConfig::getCreateTime)
        );
    }
}
