package com.bmos.platform.service.tag.convert;

import com.bmos.platform.service.tag.model.TagSceneField;
import com.bmos.platform.service.tag.vo.TagSceneFieldVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 14:54
 */
@Mapper
public interface TagDefineFieldConvert {

    TagDefineFieldConvert INSTANCE = Mappers.getMapper(TagDefineFieldConvert.class);

    List<TagSceneFieldVO> convertToVO(List<TagSceneField> list);
}
