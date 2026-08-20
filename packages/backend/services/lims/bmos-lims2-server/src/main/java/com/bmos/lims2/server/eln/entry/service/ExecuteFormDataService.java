package com.bmos.lims2.server.eln.entry.service;

import com.bmos.lims2.server.eln.entry.dto.*;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.vo.AttachmentVO;
import com.bmos.lims2.server.eln.entry.vo.FormDataItemVO;
import com.bmos.lims2.server.eln.entry.vo.FormDataVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ExecuteFormDataService {

    void saveBatch(FormDataBatchSaveDTO dto);

    void modify(FormDataModifyDTO dto);

    List<ExecuteFormData> calculateData(List<ExecuteFormData> saveData, CalculateDataQueryDTO query);


    String getServerTime();



    void insertBatch(List<ExecuteFormData> results);


    /**
     * 更新组件数据值 操作记录为更新
     * 提供给 签名组件的继续签名操作使用
     * @param dto
     */
    void update(FormDataUpdateDTO dto);

    AttachmentVO upload(MultipartFile file);


    /**
     * 获取公式计算预览结果
     * @param dto
     * @return
     */
    List<FormDataItemVO> getCalculationPreview(FormDataBatchSaveDTO dto);

    List<FormDataVO> getFieldList(FormDataListQueryDTO dto);

    List<FormDataItemVO> getRecordItemLatestData(RecordItemLatestDataQueryDTO dto);
//
//    /**
//     * 保存业务组件数据
//     * @param dto
//     */
//    void saveBusinessComponentsData(BusinessComponentBatchSaveDTO dto);

    /**
     * 保存业务组件结果数据并处理关联公式组件数据
     * @param results 业务组件结果集
     * @param dto
     */
    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto);


    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto, boolean filterNull);

    /**
     * @param results 需要保存的业务组件批记录结果
     * @param inspectionOrderId 生产计划id
     * @param parameterConfigId 工步模型id
     */
    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long inspectionOrderId,
                                                   Long parameterConfigId);

    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long inspectionOrderId,
                                                   Long parameterConfigId,  boolean filterNull);

    String pictureList(String value);
}
