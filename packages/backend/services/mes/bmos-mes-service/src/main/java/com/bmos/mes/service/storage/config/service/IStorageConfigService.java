package com.bmos.mes.service.storage.config.service;

import com.bmos.mes.service.storage.config.dto.StorageCreateDTO;
import com.bmos.mes.service.storage.config.dto.StorageEditDTO;
import com.bmos.mes.service.storage.config.model.Storage;
import com.bmos.mes.service.storage.config.vo.StorageVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 暂存间 service
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:30
 */
public interface IStorageConfigService {

    /**
     * 新增暂存间
     *
     * @param dto
     */
    void createStorage(StorageCreateDTO dto);

    /**
     * 编辑暂存间
     *
     * @param dto
     */
    void editStorage(StorageEditDTO dto);

    /**
     * 删除暂存间
     *
     * @param id 暂存间id
     */
    void deleteStorage(Long id);

    /**
     * 查询暂存间节点列表(单层)
     *
     * @param parentId 父级id
     * @param keyword  查询关键字 模糊搜索
     * @return 暂存间列表
     */
    List<StorageVO> queryList(Long parentId, String keyword);


    /**
     * 查询暂存间节点树
     *
     * @param parentId 父级id
     * @return 暂存间树
     */
    List<StorageVO> queryTree(Long parentId);

    /**
     * 查询暂存间节点树(包含货位)
     *
     * @param parentId 父级id
     * @return
     */
    List<StorageVO> queryTreeWithCargoPosition(Long parentId);

    /**
     * 批量查询暂存间节点
     * @param storageIdList
     * @return
     */
    Map<Long, Storage> selectBatchIds(Set<Long> storageIdList);
}
