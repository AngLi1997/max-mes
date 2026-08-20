package com.bmos.mes.service.inspect.mapper;


import com.bmos.mes.service.inspect.model.InspectConfig;
import com.bmos.mes.service.inspect.service.dto.InspectConfigPageDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 请验单配置表(BmInspectConfig)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-13 13:39:17
 */
@Mapper
public interface InspectConfigMapper extends BaseMapperX<InspectConfig> {

    /**
     * 校验请验单名称是否存在查询
     * @param name
     * @return
     */
    default InspectConfig selectByName(String name){
        return selectOne(new LambdaQueryWrapperX<InspectConfig>().eq(InspectConfig::getName, name));
    }

    default List<InspectConfig> selectPageList(InspectConfigPageDTO dto){
        return selectList(new LambdaQueryWrapperX<InspectConfig>()
                .likeIfPresent(InspectConfig::getName, dto.getName())
                .orderByDesc(InspectConfig::getUpdateTime)
        );
    }
}

