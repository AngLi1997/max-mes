package com.bmos.platform.service.tag.service.impl;

import com.bmos.platform.service.tag.convert.TagTypeConvert;
import com.bmos.platform.service.tag.mapper.ITagTypeMapper;
import com.bmos.platform.service.tag.service.ITagTypeService;
import com.bmos.platform.service.tag.vo.TagTypeVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:20
 */
@Service
public class TagTypeServiceImpl implements ITagTypeService {

    @Resource
    private ITagTypeMapper tagTypeMapper;

    @Override
    public List<TagTypeVO> listAllTagType() {
        return TagTypeConvert.INSTANCE.convertToVO(tagTypeMapper.listAllSorted());
    }
}
