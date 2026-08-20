package com.bmos.platform.service.tag.service;

import com.bmos.platform.service.tag.vo.TagDefineVO;
import com.bmos.platform.service.tag.vo.TagSceneDetailVO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 标签定义service
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 14:51
 */
public interface ITagDefineService {

    /**
     * 查询标签定义列表
     * @return 标签定义列表
     */
    List<TagDefineVO> listAllTagDefine();

    /**
     * 根据id查询标签定义详情
     *
     * @param tagDefineId 标签定义id
     * @return
     */
    @Nullable
    TagSceneDetailVO queryInfoById(Long tagDefineId);
}
