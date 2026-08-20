package com.bmos.mes.service.formula.service;

import com.bmos.mes.service.formula.dto.*;
import com.bmos.mes.service.formula.model.ProductFormula;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.model.ProductFormulaVersion;
import com.bmos.mes.service.formula.vo.*;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.requisition.vo.RequisitionPlanMaterialVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProductFormulaConfigureService {


    void saveProductFormula(ProductFormulaSaveDTO dto);

    CommonPage<ProductFormulaPageVO> getProductFormulaPage(ProductFormulaPageQueryDTO dto);

    void saveProductFormulaVersion(ProductFormulaSaveVersionDTO versionId);

    ProductFormulaVersionDetailVO getProductFormulaVersionDetail(Long versionId);

    CommonPage<ProductFormulaVersionPageVO> getProductFormulaVersionPage(ProductFormulaVersionPageQueryDTO dto);

    void changeProductFormulaVersionState(ProductFormulaVersionChangeStateDTO dto);

    void editProductFormulaVersion(ProductFormulaVersionEditDTO dto);

    void auditProductFormulaVersion(Long versionId);

    /**
     * 审核成功回调
     *
     * @param processInstanceId processInstanceId
     */
    void auditSuccess(String processInstanceId, String comment, String userId);

    /**
     * 审核失败回调
     *
     * @param processInstanceId processInstanceId
     */
    void auditTermination(String processInstanceId, String comment,String remark,String userId,String nodeName);

    CommonPage<ProductFormulaAuditPageVO> getProductFormulaAuditPage(ProductFormulaAuditPageQueryDTO dto);

    boolean existedProductFormula(Long id);

    boolean existedFormulaMaterial(Long id);

    List<ProductFormulaListVO> getEnableProductFormulaList(Long productId);

    List<ProductFormulaMaterialListVO> getProductFormulaMaterialPullDownList(Long versionId);
    List<ProductFormulaMaterial> getProductFormulaMaterialList(Long versionId);

    ProductFormulaInfo getProductFormulaInfo(Long formulaVersionId);

    void auditNodeProductLog(String businessKey, String remark, String userId, String name,String comment);

    List<String> getAuditBusinessKey(List<Long> deptIdList);
    List<ProductFormulaMaterialListVO> getFormulaMaterialVOListByProcedureModelId(ListProcedureMaterialDTO dto);
    List<ProductFormulaMaterial> getFormulaMaterialListByProcedureModelId(Long procedureId);

    List<ProductFormulaMaterial> getFormulaMaterialListByIds(List<Long> formulaMaterialIdList);

    ProductFormulaMaterial getFormulaMaterialById(Long formulaMaterialId);

    ProductFormulaInfo getProductFormulaInfoByPlanId(Long productPlanId);

    List<ProductFormulaMaterial> selectByIds(Collection<Long> ids);

    ProductFormulaVersion getVersionById(Long versionId);

    Map<Long, ProductFormulaInfo> getProductFormulaInfoByPlanIds(List<Long> planIds);

    List<ProductFormulaListVO> getProcessEnableProductFormulaList(Long productId, Long processVersionId);

    List<ProductFormulaMaterialListVO> getModelMaterialList(Long versionId, Long procedureModelId);

    /**
     * 根据生产计划id获取配方物料列表
     * 已根据生产批量计算理论量
     * @param productPlanId
     * @return
     */
    List<RequisitionPlanMaterialVO> getFormulaMaterialListByPlanId(Long productPlanId);

    ProductFormula getOneByVersionId(Long businessId);

    /**
     * 根据工艺版本查询配方物料列表
     * @param dto
     * @return
     */
    List<ProductFormulaMaterialListVO> getFormulaMaterialListByProcessVersionId(ListProcessMaterialDTO dto);

    /**
     * 根据配方物料id查询
     * @param id
     * @return
     */
    ProductFormulaMaterial selectById(Long id);

    /**
     * 根据工艺版本查询配方详情
     * @param detailByProcessDTO
     * @return
     */
    ProductFormulaVersionDetailVO getProductFormulaVersionDetailByProcess(FormulaVersionDetailByProcessDTO detailByProcessDTO);

}
