package com.bmos.lims2.server.material.service;

import com.bmos.lims2.feign.material.dto.RemoteIssueFeignDTO;
import com.bmos.lims2.server.material.dto.*;
import com.bmos.lims2.server.platform.material.dto.MaterialPrincipalQueryDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 检品信息业务接口
 */
public interface MaterialService {

    /**
     * 同步物料(包含物料分类)
     *
     * @param materialSyncDTO
     */
    void syncMaterialAndCategory(MaterialSyncDTO materialSyncDTO);

    /**
     * 删除检品 包含检品与实验包之间的关联关系
     *
     * @param id
     */
    void deleteProducts(Long id);

    /**
     * 更新检品信息 包含与实验包之间的关联关系
     *
     * @param materialUpdateDTO
     * @return
     */
    void updateProducts(MaterialUpdateDTO materialUpdateDTO);

    /**
     * 查询检品列表分页信息
     *
     * @param materialPageQueryDTO
     * @return
     */
    CommonPage<MaterialDTO> inspectionProductsPage(MaterialPageQueryDTO materialPageQueryDTO);

    /**
     * 当前分类下是否存在检品信息
     *
     * @param categoryId
     * @return
     */
    boolean existsProducts(Long categoryId);

    /**
     * 根据检品id查询检品详情信息 包含检品下的实验包详情信息
     *
     * @param id
     * @return
     */
    MaterialWithFieldDTO productsInfo(Long id);

    /**
     * 根据参数查询检品信息
     *
     * @param param
     * @return
     */
    List<MaterialDTO> selectByParam(MaterialParamDTO param);

    /**
     * 全量查询检品简单信息
     *
     * @return
     */
    List<MaterialDTO> productsEasyInfo(String keyword);

    /**
     * 物料信息同步
     *
     * @param dto
     */
    void issueMaterialAndCategory(RemoteIssueFeignDTO dto);

    /**
     * 启用检品
     *
     * @param id
     */
    void enableInspectProduct(Long id);

    /**
     * 停用检品
     *
     * @param id
     */
    void disableInspectProduct(Long id);

    /**
     * 新增检品
     *
     * @param dto
     */
    void saveInspectionProduct(MaterialSaveDTO dto);

    /**
     * 查询分类及其下所有子集分类的检品
     *
     * @param productCategoryId
     * @return
     */
    List<MaterialDTO> getAllByCategoryId(Long productCategoryId);


    /**
     * 查询能被关联的物料列表
     * @param dto 查询参数
     * @return
     */
    List<MaterialDTO> getPrincipalList(MaterialPrincipalQueryDTO dto);

    /**
     * 查询检品分类和检品的完整树形结构
     * @param categoryType 分类类型，可为空
     * @return 树形结构列表
     */
    List<MaterialInspectTreeNodeDTO> getMaterialInspectTree(Integer categoryType);

    /**
     * 查询绑定了启用请验单的检品树（检品已绑定启用的请验单），树上节点带请验单的ID
     * @param categoryType 分类类型，可为空
     * @return 带请验单信息的树形结构列表
     */
    List<MaterialInspectTreeWithDocumentDTO> getMaterialInspectTreeWithDocument(Integer categoryType);

    /**
     * 查询物料树（子节点为检验方案），节点包含类型标识
     * @param categoryType 分类类型，可为空
     * @return 树形结构
     */
    List<MaterialTreeWithSchemeNodeDTO> getMaterialTreeWithSchemes(Integer categoryType);
}
