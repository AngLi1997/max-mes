package com.bmos.platform.service.tag.convert;

import com.bmos.platform.service.tag.model.TagType;
import com.bmos.platform.service.tag.vo.TagTypeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:26
 */
@Mapper
public interface TagTypeConvert {

    TagTypeConvert INSTANCE = Mappers.getMapper(TagTypeConvert.class);

    List<TagTypeVO> convertToVO(List<TagType> list);
}
