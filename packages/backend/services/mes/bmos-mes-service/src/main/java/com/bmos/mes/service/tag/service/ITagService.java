package com.bmos.mes.service.tag.service;

import com.bmos.mes.service.tag.dto.CargoPositionTagQuery;
import com.bmos.mes.service.tag.dto.ScanTareWeighDTO;
import com.bmos.mes.service.tag.dto.StorageMaterialTagQuery;
import com.bmos.mes.service.tag.vo.CargoPositionTag;
import com.bmos.mes.service.tag.vo.PreparationProduceStorageMaterialTag;
import com.bmos.mes.service.tag.vo.StorageMaterialTag;
import com.bmos.mes.service.tag.vo.TareWeighTag;

import javax.annotation.Nullable;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 19:52
 */
public interface ITagService {

    /**
     * 打印物料件标签
     *
     * @param query
     * @return
     */
    @Nullable
    StorageMaterialTag queryStorageMaterialByStorageMaterialNo(StorageMaterialTagQuery query);

    /**
     * 打印货位标签
     * @param query
     * @return
     */
    @Nullable
    CargoPositionTag queryCargoPositionByPositionNo(CargoPositionTagQuery query);

    /**
     * 配液产出物料信息
     *
     * @param query
     * @return
     */
    PreparationProduceStorageMaterialTag queryPreparationProduceStorageMaterial(StorageMaterialTagQuery query);

    /**
     * 打印皮重标签
     * @param query 皮重查询参数
     * @return
     */
    @Nullable
    TareWeighTag queryTareWeighByTareWeighId(ScanTareWeighDTO query);
}
