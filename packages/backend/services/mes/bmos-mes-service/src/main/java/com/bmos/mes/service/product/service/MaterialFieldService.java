package com.bmos.mes.service.product.service;


import com.bmos.mes.material.vo.MaterialFieldInfoFeignVO;
import com.bmos.mes.service.product.dto.MaterialFieldQueryDTO;
import com.bmos.mes.service.product.dto.MaterialFieldSaveDTO;
import com.bmos.mes.service.product.vo.MaterialFieldInfoVO;
import com.bmos.mes.service.product.vo.MaterialFieldTypeVO;

import java.util.Collection;
import java.util.List;

/**
 * 生产物料自定义字段
 */
public interface MaterialFieldService {

    /**
     * 绑定生产物料的物料自定义字段
     * @param materialId
     * @param fieldSaveDTOList
     */
    void saveMaterialFields(Long materialId, List<MaterialFieldSaveDTO> fieldSaveDTOList);

    /**
     * 根据生产物料id删除绑定关系
     * @param materialId
     */
    void deleteByMaterialId(Long materialId);

    /**
     * 获取需要生产物料配置的自定义字段信息 从平台的字典中获取
     * @return
     */
    List<MaterialFieldTypeVO> getMaterialFieldList();

    /**
     * 根据生产物料id获取自定义字段信息
     * @param materialId
     * @return
     */
    List<MaterialFieldInfoVO> getMaterialFieldInfo(Long materialId);

    List<MaterialFieldInfoVO> getMaterialFieldInfo(Collection<Long> materialIds);

    /**
     * feign接口根据生产物料id获取自定义字段信息
     *
     * @param materialId
     * @return
     */
    List<MaterialFieldInfoFeignVO> getMaterialFieldFeignInfo(Long materialId);

    /**
     * 根据物料id获取自定义字段信息
     * @param dto
     * @return
     */
    List<MaterialFieldInfoVO> getMaterialFieldInfo(MaterialFieldQueryDTO dto);
}
