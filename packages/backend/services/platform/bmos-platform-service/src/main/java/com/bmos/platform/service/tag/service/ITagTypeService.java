package com.bmos.platform.service.tag.service;

import com.bmos.platform.service.tag.vo.TagTypeVO;

import java.util.List;

/**
 * 标签类型service
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:20
 */
public interface ITagTypeService {

    /**
     * 查询所有的标签类型
     *
     * @return
     */
    List<TagTypeVO> listAllTagType();
}
