package com.bmos.platform.service.tag.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.platform.service.tag.convert.TagDefineConvert;
import com.bmos.platform.service.tag.mapper.ITagDefineMapper;
import com.bmos.platform.service.tag.mapper.ITagSceneFieldMapper;
import com.bmos.platform.service.tag.model.TagDefine;
import com.bmos.platform.service.tag.model.TagSceneField;
import com.bmos.platform.service.tag.service.ITagDefineService;
import com.bmos.platform.service.tag.util.TagUtil;
import com.bmos.platform.service.tag.vo.TagDefineVO;
import com.bmos.platform.service.tag.vo.TagSceneDetailVO;
import com.bmos.platform.service.tag.vo.TagSceneFieldVO;
import com.google.zxing.BarcodeFormat;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 14:51
 */
@Service
public class TagDefineServiceImpl implements ITagDefineService {

    @Resource
    private ITagDefineMapper tagDefineMapper;

    @Resource
    private ITagSceneFieldMapper tagDefineFieldMapper;

    @Override
    public List<TagDefineVO> listAllTagDefine() {
        List<TagDefine> list = tagDefineMapper.listAllSorted();
        list.forEach(item -> item.setPreviewHtml(TagUtil.renderHtmlBarCodeOnly(item.getPreviewHtml(), BarcodeFormat.valueOf(item.getBarcodeFormat().getValue()),"BMOS", item.getTagWidth(), item.getTagHeight())));
        return TagDefineConvert.INSTANCE.convertToVO(list);
    }

    @Nullable
    @Override
    public TagSceneDetailVO queryInfoById(Long tagDefineId) {
        if (tagDefineId == null) {
            return null;
        }
        TagDefine tagDefine = tagDefineMapper.selectById(tagDefineId);
        if (tagDefine == null) {
            return null;
        }
        TagSceneDetailVO tagSceneDetailVO = TagDefineConvert.INSTANCE.convertToDetailVO(tagDefine);
        List<TagSceneField> tagSceneFields = tagDefineFieldMapper.listTagDefineFieldsBySceneId(tagDefineId);
        List<TagSceneFieldVO> availableFields = TagDefineConvert.INSTANCE.convertToDefineFieldVO(tagSceneFields);
        if (CollectionUtil.isNotEmpty(availableFields)) {
            tagSceneDetailVO.setAvailableFields(availableFields);
        }
        return tagSceneDetailVO;
    }
}
