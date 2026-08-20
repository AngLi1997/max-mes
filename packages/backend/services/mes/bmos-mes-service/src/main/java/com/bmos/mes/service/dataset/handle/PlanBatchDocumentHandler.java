package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.dataset.GenerateSourceEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.dataset.handle.data.AssembleCompleteData;
import com.bmos.mes.service.dataset.handle.data.PlanBatchDocumentData;
import com.bmos.mes.service.lotrelease.manage.service.ILotReleaseService;
import com.bmos.mes.service.plan.document.service.BatchRecordArchiveService;
import com.bmos.mes.service.plan.document.service.dto.BatchRecordArchiveSaveDTO;
import com.bmos.mes.service.plan.document.service.dto.GenerateBatchRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 批记录/批签发处理
 */
@Component
public class PlanBatchDocumentHandler {

    private static final Logger log = LoggerFactory.getLogger(PlanBatchDocumentHandler.class);

    @Resource
    private ILotReleaseService lotReleaseService;

    @Autowired
    private AssembleDataBuilder assembleDataBuilder;

    @Autowired
    private BatchRecordArchiveService batchRecordArchiveService;

    /**
     * 批记录/批签发生成
     * @param data
     * @return 文件地址列表
     */
    public List<String> handle(PlanBatchDocumentData data) {
        try{
            // 构建所需要的数据
            AssembleCompleteData assembleCompleteData = assembleDataBuilder.build(data);
            // 模板渲染
            Map<String, String> map = lotReleaseService.renderTemplate(assembleCompleteData);
            // 只有批记录生成需要回调，批签发生辰无需回调
            return this.callBack(map, data.getSourceEnum());
        } catch (Exception e) {
            log.error("批记录/批签发生成异常", e);
            this.callBackErrorPath(data);
            throw new BmosException(MesResponseCode.BATCH_GENERATE_FAIL);
        }
    }

    private void callBackErrorPath(PlanBatchDocumentData data) {
        GenerateBatchRecordDTO generateBatchRecordDTO = new GenerateBatchRecordDTO();
        generateBatchRecordDTO.setBatchRecordArchiveSaveDTOList(data.getRenderTemplateDataList().stream().map(f -> {
            BatchRecordArchiveSaveDTO batchRecordArchiveSaveDTO = new BatchRecordArchiveSaveDTO();
            batchRecordArchiveSaveDTO.setExtInfo(f.getExtInfo());
            batchRecordArchiveSaveDTO.setPath(RecordConstant.ERROR_PATH);
            return batchRecordArchiveSaveDTO;
        }).collect(Collectors.toList()));
        if (GenerateSourceEnum.BATCH_RECORD.equals(data.getSourceEnum()) && CollUtil.isNotEmpty(generateBatchRecordDTO.getBatchRecordArchiveSaveDTOList())) {
            batchRecordArchiveService.generateCallBack(generateBatchRecordDTO);
        }
    }

    private List<String> callBack(Map<String, String> map, GenerateSourceEnum sourceEnum) {
        GenerateBatchRecordDTO generateBatchRecordDTO = new GenerateBatchRecordDTO();
        generateBatchRecordDTO.setBatchRecordArchiveSaveDTOList(map.entrySet().stream()
                .map((e) -> {
                    BatchRecordArchiveSaveDTO batchRecordArchiveSaveDTO = new BatchRecordArchiveSaveDTO();
                    batchRecordArchiveSaveDTO.setExtInfo(e.getKey());
                    batchRecordArchiveSaveDTO.setPath(e.getValue());
                    return batchRecordArchiveSaveDTO;
                })
                .collect(Collectors.toList()));
        if (GenerateSourceEnum.BATCH_RECORD.equals(sourceEnum) && CollUtil.isNotEmpty(generateBatchRecordDTO.getBatchRecordArchiveSaveDTOList())){
            batchRecordArchiveService.generateCallBack(generateBatchRecordDTO);
        }
        if (CollectionUtil.isEmpty(map.values())){
            return new ArrayList<>();
        }
        return new ArrayList<>(map.values());
    }

}
