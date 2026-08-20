package com.bmos.platform.service.tag.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.tag.model.TagDefine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 14:52
 */
@Mapper
public interface ITagDefineMapper extends BaseMapperX<TagDefine> {

    default List<TagDefine> listAllSorted() {
        return selectList(Wrappers.lambdaQuery(TagDefine.class)
                .orderByAsc(TagDefine::getCreateTime)
        );
    }
}
