package com.bmos.lims2.server.material.service;


import com.bmos.lims2.server.material.dto.*;

import java.util.List;

public interface MaterialFieldService {


    /**
     * 检品自定义字段保存
     *
     * @param id
     * @param fieldSaveDTOList
     */
    void saveBasicProductsFields(Long id, List<MaterialFieldSaveDTO> fieldSaveDTOList);

    /**
     * 根据检品id获取自定义字段
     *
     * @param id
     * @return
     */
    List<MaterialFieldDTO> getByProductsId(Long id);

    /**
     * 根据检品id删除自定义字段绑定关系
     *
     * @param id
     */
    void deleteByProductsId(Long id);

    List<MaterialFieldTypeDTO> getMaterialFieldList();

    List<MaterialFieldInfoDTO> getMaterialFieldInfo(Long materialId);


    /**
     * 根据物料id获取自定义字段信息
     * @param dto
     * @return
     */
    List<MaterialFieldInfoDTO> getMaterialFieldInfo(MaterialFieldQueryDTO dto);
}
