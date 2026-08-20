package com.bmos.platform.service.tag.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.platform.service.tag.convert.TagDefineConvert;
import com.bmos.platform.service.tag.convert.TagSceneConvert;
import com.bmos.platform.service.tag.mapper.ITagSceneFieldMapper;
import com.bmos.platform.service.tag.mapper.ITagSceneMapper;
import com.bmos.platform.service.tag.model.TagScene;
import com.bmos.platform.service.tag.model.TagSceneField;
import com.bmos.platform.service.tag.service.ITagSceneService;
import com.bmos.platform.service.tag.vo.TagSceneDetailVO;
import com.bmos.platform.service.tag.vo.TagSceneFieldVO;
import com.bmos.platform.service.tag.vo.TagSceneVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 11:00
 */
@Service
public class TagSceneServiceImpl implements ITagSceneService {

    @Resource
    private ITagSceneMapper sceneMapper;

    @Resource
    private ITagSceneFieldMapper tagSceneFieldMapper;

    @Override
    public List<TagSceneVO> listAllTagScene() {
        List<TagScene> list = sceneMapper.listAllSorted();
        return TagSceneConvert.INSTANCE.convertToVO(list);
    }

    @Override
    public List<TagSceneVO> listTagSceneByTypeId(Long typeId) {
        if (typeId == null) {
            return new ArrayList<>();
        }
        List<TagScene> list = sceneMapper.listByTypeId(typeId);
        return TagSceneConvert.INSTANCE.convertToVO(list);
    }

    @Override
    public TagSceneDetailVO queryInfoById(Long tagSceneId) {
        TagScene tagScene = sceneMapper.selectById(tagSceneId);
        if (tagScene == null) {
            return null;
        }
        TagSceneDetailVO tagSceneDetailVO = TagSceneConvert.INSTANCE.convertToDetailVO(tagScene);
        List<TagSceneField> tagSceneFields = tagSceneFieldMapper.listTagDefineFieldsBySceneId(tagSceneId);
        List<TagSceneFieldVO> availableFields = TagDefineConvert.INSTANCE.convertToDefineFieldVO(tagSceneFields);
        if (CollectionUtil.isNotEmpty(availableFields)) {
            tagSceneDetailVO.setAvailableFields(availableFields);
        }
        return tagSceneDetailVO;
    }
}
