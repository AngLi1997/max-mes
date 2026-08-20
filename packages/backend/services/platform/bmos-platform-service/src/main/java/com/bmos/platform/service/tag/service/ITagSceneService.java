package com.bmos.platform.service.tag.service;

import com.bmos.platform.service.tag.vo.TagSceneDetailVO;
import com.bmos.platform.service.tag.vo.TagSceneVO;

import java.util.List;

/**
 * 标签场景service
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:20
 */
public interface ITagSceneService {

    /**
     * 查询所有的标签场景
     *
     * @return 标签场景列表
     */
    List<TagSceneVO> listAllTagScene();

    /**
     * 根据标签类型id查询标签场景列表
     *
     * @param typeId 标签类型id
     * @return
     */
    List<TagSceneVO> listTagSceneByTypeId(Long typeId);


    /**
     * 根据id查询标签场景详情
     *
     * @param tagSceneId
     * @return
     */
    TagSceneDetailVO queryInfoById(Long tagSceneId);
}
