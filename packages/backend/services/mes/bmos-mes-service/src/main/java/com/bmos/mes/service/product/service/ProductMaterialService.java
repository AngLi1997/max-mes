package com.bmos.mes.service.product.service;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.product.dto.*;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.vo.*;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProductMaterialService {
    void save(ProductMaterialSaveDTO dto);

    void update(ProductMaterialUpdateDTO dto);

    void delete(Long id);

    CommonPage<ProductMaterialPageVO> getPage(ProductMaterialPageQueryDTO pageVO);

    ProductMaterialDetailVO getDetail(Long id);

    void changeStatus(ProductMaterialChangeStatusDTO dto);

    boolean existsCategoryMaterial(Long id);
    void issueMaterialAndCategory(RemoteIssueDTO dto);

    List<MaterialTreeNodeVO> getSyncTree(SyncTreeQueryDTO dto);

    void syncMaterialAndCategory(SyncMaterialInfoDTO dto);

    List<PrincipalMaterialVO> getPrincipalList(MaterialPrincipalQueryDTO dto);

    List<ProductListVO> getProductList(Integer categoryType);

    List<ProductCategoryTreeNodeVO> getProductTree(Integer categoryType);

    List<SyncTreeNodeVO> getSyncTreeAll();

    void bindBatchRecords(RecordSaveDTO dto);

    List<Long> getProductBindRecordIds(Long productId);

    List<Long> getAllChildCategory(Long parentId);

    List<ProductMaterial> getListByTypeAndIds(CategoryInfoTypeEnum category, Set<Long> ids);

    List<ProductListVO> getFinishProductList(Integer categoryType);

    List<ProductCategoryTreeNodeVO> getaLLProductTree(List<Integer> types);

    List<Long> getProductIdList(CategoryInfoTypeEnum categoryInfoType,Long categoryId,Boolean finished);

    List<ProductCategoryTreeNodeVO> getFinishProductTree(FinishProductTreeQueryDTO dto);

    ProductMaterial selectById(Long productId);

    List<ProductMaterial> getByIds(Collection<Long> ids);

    List<Long> getIdListByCategoryIdList(List<Long> categoryIdList);

    List<ProductMaterial> getSubMaterial(Long materialId);

    List<ProductMaterial> getSubMaterialByIdList(Collection<Long> materialIds);

    List<ProductCategoryTreeNodeVO> queryTreeNodeByCategoryTypeAndProcessId(Integer categoryType, List<Long> processIdList);
    List<ProcessProductVO> getByProcessIds(Collection<Long> processIds);

    /**
     * 查询前置条件(产品)
     *
     * @param productId         产品id
     * @param productCategoryId 产品分类id
     * @return 分类下所有的产品/某个产品信息
     */
    List<ProductCategoryTreeNodeVO> getProductListCondition(Long productId, Long productCategoryId);
}
