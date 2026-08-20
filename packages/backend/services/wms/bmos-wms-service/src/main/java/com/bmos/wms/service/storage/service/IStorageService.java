package com.bmos.wms.service.storage.service;


import com.bmos.wms.service.storage.dto.StorageCreateDTO;
import com.bmos.wms.service.storage.dto.StorageEditDTO;
import com.bmos.wms.service.storage.vo.StorageVO;

import java.util.List;

/**
 * 存储区域 service
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:30
 */
public interface IStorageService {

    /**
     * 新增存储区域
     *
     * @param dto
     */
    void createStorage(StorageCreateDTO dto);

    /**
     * 编辑存储区域
     *
     * @param dto
     */
    void editStorage(StorageEditDTO dto);

    /**
     * 删除存储区域
     *
     * @param id 存储区域id
     */
    void deleteStorage(Long id);

    /**
     * 查询存储区域节点树
     *
     * @param parentId 父级id
     * @return 存储区域树
     */
    List<StorageVO> queryTree(Long parentId);

    /**
     * 查询存储区域节点树(包含货位)
     *
     * @param parentId 父级id
     * @return
     */
    List<StorageVO> queryTreeWithCargoPosition(Long parentId);
}
