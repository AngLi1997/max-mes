package com.bmos.mes.service.storage.manage.service;

import com.bmos.mes.service.storage.manage.dto.*;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageBatchVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/27 11:09
 */
public interface IStorageMaterialManageService {

    /**
     * 查询物料批次分页
     *
     * @param pageQuery
     * @return
     */
    CommonPage<StorageMaterialManageBatchVO> queryBatchPage(StorageMaterialBatchManagePageQuery pageQuery);

    /**
     * 查询物料件分页
     *
     * @param pageQuery
     * @return
     */
    CommonPage<StorageMaterialManageVO> queryPage(StorageMaterialManagePageQuery pageQuery);

    /**
     * 新增物料批次
     *
     * @param dto
     */
    StorageMaterialBatch addBatch(StorageMaterialManageBatchCreateDTO dto);

    /**
     * 编辑物料批次
     *
     * @param dto
     */
    void editBatch(StorageMaterialManageEditBatchDTO dto);

    /**
     * 新增物料件
     *
     * @param dto
     */
    List<StorageMaterial> add(StorageMaterialManageCreateDTO dto);


    /**
     * 查询物料批次详情
     *
     * @param id
     * @return
     */
    StorageMaterialManageBatchVO queryBatchDetail(Long id);

    /**
     * 保存物料件组件值
     *
     * @param dto
     */
    void saveMaterialComponentValue(StorageMaterialComponentDTO dto);


    /**
     * 查询临期物料
     *
     * @return 查询结果
     */
    List<StorageMaterialManageBatchVO> queryExpireWarningList();

    /**
     * 更新临期提醒标志
     *
     * @param batchIds 批次id集合
     * @param flag     是否已经提醒
     */
    void updateBatchExpireFlag(List<Long> batchIds, boolean flag);
}
