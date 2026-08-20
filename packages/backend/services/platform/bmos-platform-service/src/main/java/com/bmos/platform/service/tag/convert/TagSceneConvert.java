package com.bmos.platform.service.tag.convert;

import com.bmos.platform.service.tag.model.TagScene;
import com.bmos.platform.service.tag.vo.TagSceneDetailVO;
import com.bmos.platform.service.tag.vo.TagSceneVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:26
 */
@Mapper
public interface TagSceneConvert {

    TagSceneConvert INSTANCE = Mappers.getMapper(TagSceneConvert.class);

    List<TagSceneVO> convertToVO(List<TagScene> list);

    TagSceneDetailVO convertToDetailVO(TagScene tagScene);
}
