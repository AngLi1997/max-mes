package com.bmos.platform.service.tag.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.tag.model.TagScene;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:17
 */
@Mapper
public interface ITagSceneMapper extends BaseMapperX<TagScene> {

    /**
     * 排序 查询列表
     *
     * @return
     */
    default List<TagScene> listAllSorted() {
        return selectList(Wrappers.lambdaQuery(TagScene.class)
                .orderByAsc(TagScene::getSort)
        );
    }

    /**
     * 排序 查询列表
     *
     * @return
     */
    default List<TagScene> listByTypeId(Long typeId) {
        return selectList(Wrappers.lambdaQuery(TagScene.class)
                .eq(typeId != null, TagScene::getTagTypeId, typeId)
                .orderByAsc(TagScene::getSort)
        );
    }
}
