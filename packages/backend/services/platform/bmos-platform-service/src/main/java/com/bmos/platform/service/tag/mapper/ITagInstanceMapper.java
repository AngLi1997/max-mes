package com.bmos.platform.service.tag.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.service.tag.dto.TagInstancePageQuery;
import com.bmos.platform.service.tag.model.TagInstance;
import com.bmos.platform.service.tag.vo.TagInstancePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:17
 */
@Mapper
public interface ITagInstanceMapper extends BaseMapperX<TagInstance> {

    /**
     * 查询标签实例分页
     *
     * @param pageQuery
     * @return
     */
    List<TagInstancePageVO> queryPage(@Param("pageQuery") TagInstancePageQuery pageQuery);


    default boolean existsTagName(String tagName) {
        if (StrUtil.isBlank(tagName)) {
            return false;
        }
        return exists(Wrappers.lambdaQuery(TagInstance.class)
                .eq(TagInstance::getTagName, tagName)
        );
    }

    default TagInstance getEnabledTagInstanceBySceneId(Long tagSceneId) {
        if (tagSceneId == null) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(TagInstance.class)
                .eq(TagInstance::getTagSceneId, tagSceneId)
                .eq(TagInstance::getEnable, BooleanEnum.TRUE.getValue())
        );
    }
}
