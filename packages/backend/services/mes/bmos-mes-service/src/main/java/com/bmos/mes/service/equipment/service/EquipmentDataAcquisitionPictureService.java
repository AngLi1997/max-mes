package com.bmos.mes.service.equipment.service;

import com.bmos.mes.service.equipment.service.dto.AcquisitionPictureRangeDTO;
import com.bmos.mes.service.equipment.service.dto.AcquisitionPictureSaveDTO;
import com.bmos.mes.service.equipment.vo.AcquisitionPictureRangeVO;

/**
 * 设备数采绘图服务
 **/
public interface EquipmentDataAcquisitionPictureService {


    /**
     * 获取数采绘图区间
     * 纠偏线 警戒线 标准线
     * @param dto
     * @return
     */
    AcquisitionPictureRangeVO getAcquisitionPictureRange(AcquisitionPictureRangeDTO dto);

    /**
     * 保存设备数采绘图
     * @param dto
     */
    void saveAcquisitionPicture(AcquisitionPictureSaveDTO dto);
}
