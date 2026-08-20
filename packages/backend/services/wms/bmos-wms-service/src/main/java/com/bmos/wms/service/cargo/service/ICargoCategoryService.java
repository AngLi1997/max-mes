package com.bmos.wms.service.cargo.service;

import com.bmos.wms.service.cargo.dto.CargoCategoryCreateDTO;
import com.bmos.wms.service.cargo.dto.CategoryIssueFeignDTO;
import com.bmos.wms.service.cargo.model.CargoCategory;
import com.bmos.wms.service.cargo.vo.CargoCategoryVO;
import com.bmos.wms.service.cargo.vo.CargoTreeVO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 货品分类服务接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 17:37
 */
public interface ICargoCategoryService {

    /**
     * 查询所有货品分类树
     *
     * @return 货品分类树
     */
    List<CargoCategoryVO> queryTree();

    /**
     * 根据id查询货品分类
     *
     * @param id 货品分类id
     * @return 货品分类
     */
    @Nullable
    CargoCategoryVO queryById(Long id);

    /**
     * 根据code查询货品分类
     *
     * @param code 货品分类code
     * @return 货品分类
     */
    @Nullable
    CargoCategoryVO queryByCode(String code);

    /**
     * 创建货品分类
     *
     * @param dto 货品分类创建DTO
     */
    void createCargoCategory(CargoCategoryCreateDTO dto);

    /**
     * 删除货品分类
     *
     * @param id 货品分类id
     */
    void deleteCargoCategory(Long id);

    /**
     * 根据父级id查询子集
     *
     * @param parentId 父级id
     * @return 货品分类
     */
    List<CargoCategory> queryAllChildren(Long parentId);

    @Nullable
    CargoCategory selectById(Long cargoCategoryId);

    List<CargoTreeVO> queryTreeWithCargo();

    List<CargoCategory> selectAllList();

    void issueCategory(List<CategoryIssueFeignDTO> categoryList);
}
