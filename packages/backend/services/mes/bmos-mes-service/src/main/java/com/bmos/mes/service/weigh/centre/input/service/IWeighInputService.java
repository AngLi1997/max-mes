package com.bmos.mes.service.weigh.centre.input.service;

import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.dto.ScanWeighMaterialCodeWithMaterialWeighComponentId;
import com.bmos.mes.service.weigh.centre.input.dto.WeighInputDTO;
import com.bmos.mes.service.weigh.centre.input.vo.WeighInputRecordResultVO;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 16:01
 */
public interface IWeighInputService {

    /**
     * 根据业务组件实例id查询物料投入列表
     * @param componentInstanceId 业务组件实例id
     * @return 物料投入列表
     */
    WeighInputRecordResultVO getInputList(Long componentInstanceId);

    /**
     * 物料投入
     * @param dto 投料参数
     */
    void input(WeighInputDTO dto);

    StorageMaterialVO scanWeighMaterialCodeWithMaterialWeighComponentId(ScanWeighMaterialCodeWithMaterialWeighComponentId scanQuery);

    /**
     * 完成物料投入
     * @param componentInstanceId 业务组件实例id
     */
    void finishInput(Long componentInstanceId);
}
