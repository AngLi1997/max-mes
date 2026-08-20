package com.bmos.platform.service.material.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import com.bmos.platform.service.material.dto.*;
import com.bmos.platform.service.material.model.Material;
import com.bmos.platform.service.material.vo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MaterialService {

    List<MaterialCategoryTreeNodeVO> getCategoryTree();

    Long save(MaterialSaveDTO dto);

    CommonPage<MaterialPageVO> getPage(MaterialPageQueryDTO dto);

    void changeStatus(MaterialChangeStatusDTO dto);

    void update(MaterialUpdateDTO dto);

    void deleteById(Long id);

    List<MaterialVO> getPrincipalList(MaterialPrincipalQueryDTO dto);

    MaterialDetailVO getDetail(Long id);

    List<Material> queryByUnitExtendId(Long id);

    List<Material> queryByUnitId(Long id);

    Boolean checkMergeCodeExisted(String code, Long id, Long categoryId);

    void issueMaterial(MaterialIssueDTO dto);

    List<IssueTreeNodeVO> getIssueTree(Long parentId, String keyword);

    List<IssueBusinessVO> getIssueBusinesses();

    RemoteSyncDTO getSyncList(SyncMaterialInfoDTO dto);

    void unregisterMaterial(UnregisterMaterialDTO dto);

    /**
     * 获取全量物料树
     * @return
     */
    List<MaterialTreeNodeVO> getMaterialTree();

    void getImportTemplate(HttpServletResponse response);

    void importMaterial(HttpServletResponse response, MultipartFile file);

    void exportMaterial(HttpServletResponse response, MaterialExportDTO dto);
}
