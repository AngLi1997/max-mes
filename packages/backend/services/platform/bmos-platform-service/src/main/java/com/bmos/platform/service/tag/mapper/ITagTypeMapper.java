package com.bmos.platform.service.tag.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.tag.model.TagType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:17
 */
@Mapper
public interface ITagTypeMapper extends BaseMapperX<TagType> {

    /**
     * 排序 查询列表
     *
     * @return
     */
    default List<TagType> listAllSorted() {
        return selectList(Wrappers.lambdaQuery(TagType.class).orderByAsc(TagType::getSort));
    }
}
