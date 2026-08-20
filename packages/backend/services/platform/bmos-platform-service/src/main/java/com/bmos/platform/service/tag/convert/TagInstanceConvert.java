package com.bmos.platform.service.tag.convert;

import com.bmos.platform.service.tag.model.TagInstance;
import com.bmos.platform.service.tag.vo.TagInstanceDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/8 14:25
 */
@Mapper
public interface TagInstanceConvert {

    TagInstanceConvert INSTANCE = Mappers.getMapper(TagInstanceConvert.class);

    TagInstanceDetailVO convertToDetailVO(TagInstance tagInstance);

}
