package com.bmos.platform.service.tag.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.tag.dto.*;
import com.bmos.platform.service.tag.vo.TagInstanceDetailVO;
import com.bmos.platform.service.tag.vo.TagInstancePageVO;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标签实例service接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:32
 */
public interface ITagInstanceService {

    /**
     * 查询标签实例分页
     *
     * @param pageQuery
     * @return
     */
    CommonPage<TagInstancePageVO> queryPage(TagInstancePageQuery pageQuery);

    /**
     * 创建标签实例
     *
     * @param dto
     */
    void createTagInstance(TagInstanceDTO dto);

    /**
     * 编辑标签实例
     *
     * @param dto
     */
    void editTagInstance(TagInstanceDTO dto);

    /**
     * 查询标签详情
     *
     * @param id 标签实例id
     * @return
     */
    @Nullable
    TagInstanceDetailVO queryInfoById(Long id);

    /**
     * 启用标签实例
     *
     * @param id 标签实例id
     */
    void enableTagInstance(Long id);

    /**
     * 禁用标签实例
     *
     * @param id 标签实例id
     */
    void disableTagInstance(Long id);

    /**
     * 删除标签实例
     *
     * @param id 标签实例id
     */
    void deleteTagInstance(Long id);

    /**
     * 打印标签
     *
     * @param printerDTO
     */
    void printTag(PrintCommonDTO printerDTO);

    /**
     * 批量打印标签
     * @param printerDTOList
     */
    void printBatchTag(List<PrintCommonDTO> printerDTOList);

    void printBatchTags(PrintBatchDTO printBatchDTO);

    /**
     * 标签预览
     * @param previewDTO
     * @param response
     * @return
     */
    void previewTag(PrintPreviewDTO previewDTO,  HttpServletResponse response);
}
