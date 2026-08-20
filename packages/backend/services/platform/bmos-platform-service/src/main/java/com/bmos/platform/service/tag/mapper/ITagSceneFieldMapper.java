package com.bmos.platform.service.tag.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.tag.model.TagSceneField;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 14:52
 */
@Mapper
public interface ITagSceneFieldMapper extends BaseMapperX<TagSceneField> {

    default List<TagSceneField> listTagDefineFieldsBySceneId(Long tagSceneId) {
        if (tagSceneId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(TagSceneField.class)
                .eq(TagSceneField::getTagSceneId, tagSceneId)
                .orderByAsc(TagSceneField::getId)
        );
    }

    /**
     * 判断字段是否在定义中合法
     *
     * @param tagSceneId  场景ID
     * @param defineFields 字段集合
     * @return 是否合法 true:合法 false:不合法
     */
    default boolean checkIncludeFields(Long tagSceneId, Set<String> defineFields) {
        if (tagSceneId == null) {
            return false;
        }
        if (CollectionUtil.isEmpty(defineFields)) {
            return false;
        }
        Long count = selectCount(Wrappers.lambdaQuery(TagSceneField.class)
                .eq(TagSceneField::getTagSceneId, tagSceneId)
                .in(TagSceneField::getField, defineFields)
        );
        return count != null && count.equals((long) defineFields.size());
    }
}
