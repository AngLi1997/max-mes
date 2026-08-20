package com.bmos.lims2.server.eln.record.service;

import com.bmos.lims2.server.eln.record.dto.*;
import com.bmos.lims2.server.eln.record.vo.*;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BatchRecordService {

    RecordUploadVo fileUpload(MultipartFile file);

    BatchRecordSaveVO saveRecord(BatchRecordSaveDTO dto);

    CommonPage<RecordListVO> getRecordPage(RecordListQueryDTO dto);

    Boolean updateVersion(RecordVersionDTO dto);

    RecordUploadItemVO recordItemUpload(MultipartFile file);

    RecordItemDetailVO copyRecordItem(Long itemId, String itemName);

    RecordItemDetailVO deleteRecordItem(Long itemId);

    List<SelectRecorVO> queryListRecordByProductId(Long productId);

    /**
     * 单个记录项保存
     * @param dto
     */
    SaveSingleItemVO saveSingleItem(RecordItemSingleSaveDTO dto);

    /**
     * 单个记录项编辑，组件保存
     * @param dto
     */
    void editSingleItem(RecordItemSingleEditDTO dto);

    /**
     * 获取公式树(根据记录id获取绑定信息)
     * @param id
     * @return
     */
    List<RecordExpressionBindTreeNodeVO> getExpressionTreeByRecordId(Long id);

    /**
     * 记录绑定公式
     * @param dto
     */
    void bindExpression(RecordBindExpressionDTO dto);

    /**
     * 公式绑定记录
     * @param dto
     */
    void expressionBindBatchRecord(ExpressionBindRecordDTO dto);

    /**
     * 获取记录树(根据公式id获取绑定信息)
     * @param expressionId
     * @return
     */
    List<BatchRecordTreeNodeVO> getRecordTreeByExpressionId(Long expressionId);

    /**
     * 根据记录id获取绑定的公式id列表
     * @param id
     * @return
     */
    List<Long> getRecordBoundExpressionIdList(Long id);

    /**
     * 根据分析项id查询批记录（替代根据产品查询）
     */
    java.util.List<com.bmos.lims2.server.eln.record.vo.SelectRecorVO> queryListRecordByParameterId(Long parameterId);

    /**
     * 根据公式id获取绑定的记录id列表
     * @param expressionId
     * @return
     */
    List<Long> getBoundRecordIdList(Long expressionId);

    void downloadByUrl(HttpServletResponse response, String url) throws Exception;
}
