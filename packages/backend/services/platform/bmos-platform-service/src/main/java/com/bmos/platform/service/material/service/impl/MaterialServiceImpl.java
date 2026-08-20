package com.bmos.platform.service.material.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.ExcelReaderUtils;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.OptionBo;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.logging.annotation.defined.OperationUserDefined;
import com.bmos.logging.aspect.defined.OperationUserDefinedContext;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.material.IsSubMaterialEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.service.config.swagger.BusinessesConfig;
import com.bmos.platform.service.execute.parameter.service.BusinessParameterService;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.feign.CommonFeignClient;
import com.bmos.platform.service.feign.CommonFeignClientFactory;
import com.bmos.platform.service.feign.FeignUtils;
import com.bmos.platform.service.material.constant.MaterialTemplateConstant;
import com.bmos.platform.service.material.convert.MaterialConverter;
import com.bmos.platform.service.material.dto.*;
import com.bmos.platform.service.material.mapper.MaterialMapper;
import com.bmos.platform.service.material.model.Material;
import com.bmos.platform.service.material.model.MaterialCategory;
import com.bmos.platform.service.material.service.MaterialCategoryService;
import com.bmos.platform.service.material.service.MaterialExtendUnitService;
import com.bmos.platform.service.material.service.MaterialService;
import com.bmos.platform.service.material.vo.*;
import com.bmos.platform.service.unit.model.Unit;
import com.bmos.platform.service.unit.service.UnitExtendService;
import com.bmos.platform.service.unit.vo.UnitExtendListVO;
import com.bmos.unit.service.UnitCache;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialCategoryService materialCategoryService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private BusinessesConfig businessesConfig;

    @Autowired
    private MaterialExtendUnitService materialExtendUnitService;

    @Autowired
    private CommonFeignClientFactory commonFeignClientFactory;

    @Autowired
    private UnitExtendService unitExtendService;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private BusinessParameterService businessParameterService;

    @Override
    public List<MaterialCategoryTreeNodeVO> getCategoryTree() {
        return materialCategoryService.getCategoryTree();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public Long save(MaterialSaveDTO dto) {
        MaterialCategory materialCategory = materialCategoryService.selectById(dto.getMaterialCategoryId());
        if (ObjectUtil.isNull(materialCategory)) {
            throw new BmosException(PlatformResponseCode.MATERIAL_CATEGORY_NOT_EXIST);
        }
        // 成员物料必须选择所属物料
        if(BooleanUtil.isTrue(dto.getSubMaterial())){
            if(ObjectUtil.isNull(dto.getPrincipalMaterialId())){
                throw new BmosException(PlatformResponseCode.SUB_MATERIAL_MUST_HAS_PRINCIPAL);
            }
            Material material = materialMapper.selectById(dto.getPrincipalMaterialId());
            if(ObjectUtil.notEqual(dto.getUnitId(), material.getUnitId())){
                throw new BmosException(PlatformResponseCode.SUB_MATERIAL_UNIT_MISMATCH);
            }
        }
        // 合并编码校验
        // todo 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
//        String categoryMergeCode = materialCategory.getMergeCode();
//        String mergeCode = categoryMergeCode + dto.getCode();
        String mergeCode = dto.getCode();
        if (checkMergeCodeExisted(mergeCode, null, null)) {
            throw new BmosException(PlatformResponseCode.MERGE_CODE_EXISTED);
        }
        Material material = MaterialConverter.INSTANCE.convert(dto);
        material.setMergeCode(mergeCode);
        // 业务向平台注册的物料为启用状态
        if (dto.isBusinessRegister()) {
            material.setStatus(true);
            material.setDispenseRecord(dto.getBusinessName());
        }
        materialMapper.insert(material);
        // 物料绑定单位配置
        Long unitId = dto.getUnitId();
        List<UnitExtendListVO> extendUnits = unitExtendService.getExtendUnitListByUnitId(Collections.singletonList(unitId));
        if(CollUtil.isNotEmpty(extendUnits)){
            List<Long> longs = CollectionUtils.convertList(extendUnits, UnitExtendListVO::getId);
            MaterialBindExtendUnitDTO bindDTO = new MaterialBindExtendUnitDTO();
            bindDTO.setMaterialId(material.getId());
            bindDTO.setExtendUnitIdList(longs);
            materialExtendUnitService.bindExtendUnit(bindDTO);
        }
        return material.getId();
    }

    @Override
    public CommonPage<MaterialPageVO> getPage(MaterialPageQueryDTO dto) {
        List<Long> allChildCategoryIds = materialCategoryService.getAllChildCategoryIds(dto.getMaterialCategoryId());
        dto.setMaterialCategoryIds(allChildCategoryIds);
        CommonPage<MaterialPageVO> result = CommonPage.convertPage(materialMapper.selectPageList(dto));
        result.getList().forEach(item -> {
            Optional.ofNullable(item.getDispenseRecord()).ifPresent(e -> {
                List<String> split = StrUtil.split(e, "/");
                item.setDispenseRecord(StrUtil.join("/", split.stream()
                        .map(s -> I18nUtils.getCodeMessage(s, s, null))
                        .collect(Collectors.toList())));
            });
        });
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void changeStatus(MaterialChangeStatusDTO dto) {
        if (!dto.getStatus()) {
            Material material = materialMapper.selectById(dto.getId());
            //校验 物料信息已下发至XX，不允许停用
            if (ObjectUtil.isNotEmpty(material.getDispenseRecord())) {
                throw new BmosException(PlatformResponseCode.MATERIAL_ISSUED);
            }
            // 校验物料关联
            if (!material.getSubMaterial() && materialMapper.existsRelatedMaterial(material.getId())) {
                throw new BmosException(PlatformResponseCode.MATERIAL_ASSOCIATED_WITH_MEMBER);
            }

        }
        materialMapper.updateStatus(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void update(MaterialUpdateDTO dto) {
        Material material = materialMapper.selectById(dto.getId());
        validateMaterialEnabled(material);
        MaterialCategory materialCategory = materialCategoryService.selectById(dto.getMaterialCategoryId());
        if (ObjectUtil.isNull(materialCategory)) {
            throw new BmosException(PlatformResponseCode.MATERIAL_CATEGORY_NOT_EXIST);
        }
        // 若为成员物料校验单位是否和主物料一致
        if (dto.getSubMaterial()) {
            if (dto.getPrincipalMaterialId() == null) {
                throw new BmosException(PlatformResponseCode.SUB_MATERIAL_MUST_HAS_PRINCIPAL);
            }
            Material principal = materialMapper.selectById(dto.getPrincipalMaterialId());
            if (ObjectUtil.notEqual(dto.getUnitId(), principal.getUnitId())) {
                throw new BmosException(PlatformResponseCode.SUB_MATERIAL_UNIT_MISMATCH);
            }
        }
        Long unitId = material.getUnitId();
        if (ObjectUtil.notEqual(dto.getUnitId(), unitId)) {
            List<UnitExtendListVO> extendUnits = unitExtendService.getExtendUnitListByUnitId(Collections.singletonList(dto.getUnitId()));
            MaterialBindExtendUnitDTO bindDTO = new MaterialBindExtendUnitDTO();
            bindDTO.setMaterialId(material.getId());
            bindDTO.setExtendUnitIdList(CollectionUtils.convertList(extendUnits, UnitExtendListVO::getId));
            materialExtendUnitService.bindExtendUnit(bindDTO);
        }
        // 联环需求，合并编码不再进行拼接，合并编码和编码保持一致
//        String mergeCode = materialCategory.getMergeCode() + dto.getCode();
        String mergeCode = dto.getCode();
        if (checkMergeCodeExisted(mergeCode, dto.getId(), null)) {
            throw new BmosException(PlatformResponseCode.MERGE_CODE_EXISTED);
        }
        Material newMaterial = MaterialConverter.INSTANCE.convertUpdate(dto);
        newMaterial.setMergeCode(mergeCode);
        materialMapper.updateById(newMaterial);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void deleteById(Long id) {
        Material material = materialMapper.selectById(id);
        if (material.getStatus()) {
            throw new BmosException(PlatformResponseCode.MATERIAL_ENABLED);
        }
        materialMapper.deleteById(id);
    }

    @Override
    public List<MaterialVO> getPrincipalList(MaterialPrincipalQueryDTO dto) {
        List<Material> materials = materialMapper.selectPrincipalList(dto);
        List<MaterialVO> result = MaterialConverter.INSTANCE.convertPrincipalList(materials);
        result.forEach(e -> e.setUnitName(unitCache.getGlobalUnitName(e.getUnitId())));
        return result;
    }

    @Override
    public MaterialDetailVO getDetail(Long id) {
        Material material = materialMapper.selectById(id);
        MaterialCategory category = materialCategoryService.getById(material.getMaterialCategoryId());
        MaterialDetailVO detail = MaterialConverter.INSTANCE.convertDetail(material);
        detail.setCategoryCode(category.getCode());
        return detail;
    }

    @Override
    public List<Material> queryByUnitExtendId(Long id) {
        return materialMapper.queryByUnitExtendId(id);
    }

    @Override
    public List<Material> queryByUnitId(Long id) {
        return materialMapper.queryByUnitExtendId(id);
    }

    @Override
    public Boolean checkMergeCodeExisted(String code, Long materialId, Long categoryId) {
        return materialMapper.existsCode(code, materialId) || materialCategoryService.checkCategoryExisted(code, categoryId);
    }

    @Override
    @OperationLog
    public void issueMaterial(MaterialIssueDTO dto) {
        List<Long> mIds = dto.getMaterialIds();
        List<Long> materialCategoryIds = dto.getMaterialCategoryIds();
        List<MaterialCategory> materialCategories = materialCategoryService.selectByIds(materialCategoryIds);
        List<Material> materials = CollUtil.isEmpty(mIds) ? new ArrayList<>() : materialMapper.selectByIds(mIds);
        // 处理未勾选但是关联到的主物料
        List<Material> principalMaterials = getPrincipalMaterialList(materials);
        materials.addAll(principalMaterials);
        // 下发到的业务
        List<String> names = new ArrayList<>();
        List<MaterialIssueBusinessDTO> businesses = new ArrayList<>();
        for (MaterialIssueBusinessDTO business : dto.getBusinesses()) {
            List<Integer> childCodeList = business.getChildCodeList();
            for (Integer i : childCodeList) {
                MaterialIssueBusinessDTO child = new MaterialIssueBusinessDTO();
                child.setPlatformName(business.getPlatformName());
                child.setChildCodeList(Collections.singletonList(i));
                businesses.add(child);
            }
        }
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterService.detailByCode(BusinessParameterCodeConstants.MES_MATERIAL_DYING_PERIOD);
        Integer dyingPeriod = null;
        if (Objects.nonNull(businessParameterDetailVO) && Objects.nonNull(businessParameterDetailVO.getValue())){
            dyingPeriod = Integer.parseInt(businessParameterDetailVO.getValue());
        }
        for (MaterialIssueBusinessDTO business : businesses) {
            issueToEveryBusiness(business, materials, materialCategories, names, dyingPeriod);
        }
        if (CollUtil.isNotEmpty(names)) {
            if (CollUtil.isNotEmpty(materials)) {
                updateMaterialDispense(materials, names);
            }
            if (CollUtil.isNotEmpty(materialCategoryIds)) {
                updateCategoryDispense(materialCategories, names);
            }
        }
    }

    private void updateCategoryDispense(List<MaterialCategory> materialCategories, List<String> names) {
        materialCategories.forEach(materialCategory -> {
            String dispenseRecord = materialCategory.getDispenseRecord();
            String dispense;
            if (StrUtil.isEmpty(dispenseRecord)) {
                dispense = StrUtil.join(StrUtil.SLASH, names);
            } else {
                List<String> original = StrUtil.split(dispenseRecord, StrUtil.SLASH);
                original.addAll(names);
                List<String> collect = original.stream().distinct().sorted().collect(Collectors.toList());
                dispense = StrUtil.join(StrUtil.SLASH, collect);
            }
            materialCategory.setDispenseRecord(dispense);
        });
        materialCategoryService.updateBatch(materialCategories);
    }

    private void updateMaterialDispense(List<Material> materials, List<String> names) {
        materials.forEach(material -> {
            String dispenseRecord = material.getDispenseRecord();
            String dispense;
            if (StrUtil.isEmpty(dispenseRecord)) {
                dispense = StrUtil.join(StrUtil.SLASH, names);
            } else {
                List<String> original = StrUtil.split(dispenseRecord, StrUtil.SLASH);
                original.addAll(names);
                List<String> collect = original.stream().distinct().sorted().collect(Collectors.toList());
                dispense = StrUtil.join(StrUtil.SLASH, collect);
            }
            material.setDispenseRecord(dispense);
        });
        materialMapper.updateBatch(materials);
    }

    private void issueToEveryBusiness(MaterialIssueBusinessDTO business, List<Material> materials, List<MaterialCategory> materialCategories, List<String> names, Integer dyingPeriod) {
        List<IssueBusinessVO> platforms = businessesConfig.getPlatforms();
        Map<String, IssueBusinessVO> platMap = CollectionUtils.convertMap(platforms, IssueBusinessVO::getPlatformName);
        log.info("业务平台已配置:{}\n,下发:{}", platMap, business.getPlatformName());
        CommonFeignClient feignClient = commonFeignClientFactory.getFeignClient(business.getPlatformName());
        RemoteIssueDTO remoteIssueDTO = getRemoteIssueDTO(business, materials, materialCategories, dyingPeriod);
        IssueBusinessVO issueBusiness = platMap.get(business.getPlatformName());
        List<ChildBusinessVO> children = issueBusiness.getChildren();
        Map<Integer, ChildBusinessVO> childMap = CollectionUtils.convertMap(children, ChildBusinessVO::getChildCode);
        ResponseInfo<Void> response = FeignUtils.handleRequest(feignClient::issueMaterialAndCategory, remoteIssueDTO);
        if (response.isSuccess()) {
            List<Integer> childCodeList = business.getChildCodeList();
            childCodeList.forEach(code -> {
                ChildBusinessVO childBusiness = childMap.get(code);
                names.add(childBusiness.getChildName());
            });
        }
    }

    /**
     * 获取需要一同下发的主物料
     *
     * @param materials
     * @return
     */
    private List<Material> getPrincipalMaterialList(List<Material> materials) {
        Map<Long, Material> idMaterialMap = CollectionUtils.convertMap(materials, Material::getId, obj -> obj);
        // 不在下发列表但是下发列表中有关联的所属物料ids
        List<Long> principalIds = new ArrayList<>();
        // 处理关联物料
        for (Material material : materials) {
            if (material.getSubMaterial() && ObjectUtil.isNull(idMaterialMap.get(material.getPrincipalMaterialId()))) {
                principalIds.add(material.getPrincipalMaterialId());
            }
        }
        return CollUtil.isEmpty(principalIds) ? new ArrayList<>() : materialMapper.selectByIds(principalIds);
    }

    @Override
    public List<IssueTreeNodeVO> getIssueTree(Long parentId, String keyword) {
        if (StrUtil.isNotBlank(keyword)) {
            List<MaterialCategory> list = materialCategoryService.selectList();
            List<Long> ids = materialCategoryService.selectIdsByKeyWord(keyword);
            List<IssueTreeNodeVO> issueTreeNodeVOS = materialMapper.selectIssueTreeNodeVOByKeyword(keyword);
            return buildIssueTree(list, issueTreeNodeVOS, ids);
        }
        List<IssueTreeNodeVO> categories = materialCategoryService.selectByParentId(parentId);
        categories.forEach(category -> category.setCategoryFlag(true));
        List<Material> materials = materialMapper.selectEnabledByCategoryId(parentId);
        List<IssueTreeNodeVO> materialNodes = MaterialConverter.INSTANCE.convertMaterialIssueTreeNode(materials);
        categories.addAll(materialNodes);
        return categories;
    }

    private List<IssueTreeNodeVO> buildIssueTree(List<MaterialCategory> list, List<IssueTreeNodeVO> materials, List<Long> ids) {
        List<IssueTreeNodeVO> categoryNodes = MaterialConverter.INSTANCE.convertCategoryIssueTreeNode(list);
        categoryNodes.forEach(c -> c.setCategoryFlag(true));
        categoryNodes.addAll(materials);
        List<IssueTreeNodeVO> issueTreeNodeVOS = TreeUtil.buildTree(categoryNodes, false);
        for (IssueTreeNodeVO issueTreeNodeVO : issueTreeNodeVOS) {
            cleanTree(issueTreeNodeVO, ids);
        }
        issueTreeNodeVOS.removeIf(node -> node.isCategoryFlag() && CollUtil.isEmpty(node.getChildren()));
        return issueTreeNodeVOS;
    }

    private IssueTreeNodeVO cleanTree(IssueTreeNodeVO node, List<Long> ids) {
        if (CollUtil.isEmpty(node.getChildren())) {
            return node;
        }
        List<IssueTreeNodeVO> cleanedChiyigaohuiist = new ArrayList<>();
        for (IssueTreeNodeVO child : node.getChildren()) {
            if (child.isCategoryFlag()) {
                IssueTreeNodeVO cleanedChild = cleanTree(child, ids);
                if (CollUtil.isNotEmpty(cleanedChild.getChildren()) || ids.contains(cleanedChild.getId())) {
                    cleanedChiyigaohuiist.add(cleanedChild);
                }
            } else {
                cleanedChiyigaohuiist.add(child);
            }
        }
        node.setChildren(cleanedChiyigaohuiist);
        return node;
    }

    @Override
    public List<IssueBusinessVO> getIssueBusinesses() {
        return businessesConfig.getPlatforms();
    }

    @Override
    public RemoteSyncDTO getSyncList(SyncMaterialInfoDTO dto) {
        List<Long> materialCategoryIds = dto.getMaterialCategoryIds();
        List<Long> materialIds = dto.getMaterialIds();
        RemoteSyncDTO remoteSyncDTO = new RemoteSyncDTO();
        if (CollUtil.isNotEmpty(materialCategoryIds)) {
            List<MaterialCategory> materialCategories = materialCategoryService.selectByIds(materialCategoryIds);
            remoteSyncDTO.setCategoryList(materialCategories);
        }
        if (CollUtil.isNotEmpty(materialIds)) {
            List<Material> materials = materialMapper.selectByIds(materialIds);
            // 处理成员物料关联的
            List<Material> principalMaterialList = getPrincipalMaterialList(materials);
            materials.addAll(principalMaterialList);
            remoteSyncDTO.setMaterialList(materials);
        }
        return remoteSyncDTO;
    }

    @Override
    public void unregisterMaterial(UnregisterMaterialDTO dto) {
        Long materialId = dto.getMaterialId();
        Material material = materialMapper.selectById(materialId);
        Map<String, IssueBusinessVO> platformMap = businessesConfig.getPlatformMap();
        IssueBusinessVO platform = platformMap.get(dto.getPlatformName());
        Map<Integer, ChildBusinessVO> childMap = CollectionUtils.convertMap(platform.getChildren(), ChildBusinessVO::getChildCode);
        String childName = childMap.get(dto.getChildCode()).getChildName();
        List<String> split = StrUtil.split(material.getDispenseRecord(), StrUtil.SLASH);
        split.remove(childName);
        material.setDispenseRecord(StrUtil.join(StrUtil.SLASH, split));
        materialMapper.updateById(material);
    }

    @Override
    public List<MaterialTreeNodeVO> getMaterialTree() {
        List<MaterialCategory> materialCategories = materialCategoryService.selectList();
        List<MaterialTreeNodeVO> result = materialCategories.stream().map(e -> {
            MaterialTreeNodeVO vo = MaterialConverter.INSTANCE.convert2MaterialTreeNode(e);
            vo.setShowName(vo.getMergeCode() + StrUtil.DASHED + vo.getName());
            vo.setCategoryFlag(true);
            return vo;
        }).collect(Collectors.toList());
        List<Material> materials = materialMapper.selectEnabledByCategoryId(null);
        List<MaterialTreeNodeVO> materialNodeList = materials.stream().map(e -> {
            MaterialTreeNodeVO vo = MaterialConverter.INSTANCE.convert2MaterialTreeNode(e);
            vo.setShowName(vo.getMergeCode() + StrUtil.DASHED + vo.getName());
            vo.setParentId(e.getMaterialCategoryId());
            vo.setCategoryFlag(false);
            return vo;
        }).collect(Collectors.toList());
        result.addAll(materialNodeList);
        return TreeUtil.buildTree(result, false);
    }

    @Override
    public void getImportTemplate(HttpServletResponse response) {
        SheetDataBo sheetDataBo = new SheetDataBo(ExcelI18nUtil.getI18n(MaterialTemplateConstant.IMPORT_NAME), MaterialTemplateVO.class, null,
                getOptions());
        try {
            ExcelWriterUtils.write(MaterialTemplateConstant.MATERIAL_TEMPLATE_NAME, response, Lists.newArrayList(sheetDataBo));
        } catch (Exception e) {
            log.error("生成模板出错", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationUserDefined(operationObject = "#operationObject")
    public void importMaterial(HttpServletResponse response, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<MaterialTemplateVO> materialList = ExcelReaderUtils.read(inputStream, MaterialTemplateVO.class,
                    MaterialTemplateConstant.IMPORT_NAME);
            if (CollUtil.isEmpty(materialList)) {
                throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_IMPORT_FILE_ERROR);
            }
            Pair<Boolean, List<MaterialImportErrorVO>> booleanListPair = this.importMaterialValid(materialList);
            if (!booleanListPair.getLeft()){
                this.writeErrorExcel(response, booleanListPair.getRight());
                return;
            }
            this.saveMaterialBatch(materialList);
            OperationUserDefinedContext.putVariable("operationObject", JsonUtils.toJsonString(materialList));
        }catch (Exception e){
            log.error("读取文件失败", e);
        }
    }

    @Override
    @OperationUserDefined(operationObject = "#operationObject")
    public void exportMaterial(HttpServletResponse response, MaterialExportDTO dto) {
        MaterialPageQueryDTO pageQueryDto = BeanUtil.toBean(dto, MaterialPageQueryDTO.class);
        if (BooleanUtil.isTrue(dto.getAllFlay())){
            pageQueryDto.setPageNum(1);
            pageQueryDto.setPageSize(1000000);
        }
        CommonPage<MaterialPageVO> page = this.getPage(pageQueryDto);
        List<MaterialExportVO> exportVOList = BeanUtil.copyToList(page.getList(), MaterialExportVO.class);
        if (CollUtil.isNotEmpty(exportVOList)){
            List<Long> categoryIdList = CollectionUtils.convertList(exportVOList, MaterialExportVO::getMaterialCategoryId);
            //分类信息
            List<MaterialCategory> materialCategories = materialCategoryService.selectByIds(categoryIdList);
            Map<Long, MaterialCategory> categoryMap = CollectionUtils.convertMap(materialCategories, MaterialCategory::getId);
            //物料信息
            List<Long> materialIdList = CollectionUtils.convertList(exportVOList, MaterialExportVO::getPrincipalMaterialId);
            Map<Long, Material> materialMap = CollectionUtils.convertMap(materialMapper.selectByIds(materialIdList), Material::getId);
            exportVOList.forEach(item->{
                MaterialCategory materialCategory = categoryMap.get(item.getMaterialCategoryId());
                item.setCategory(materialCategory.getName());
                item.setCategoryMergeCode(materialCategory.getMergeCode());
                item.setSubMaterialName(IsSubMaterialEnum.findByValue(item.getSubMaterial()));
                if (BooleanUtil.isTrue(item.getSubMaterial())){
                    Material material = materialMap.get(item.getPrincipalMaterialId());
                    item.setPrincipalMaterialCode(material.getMergeCode());
                    item.setPrincipalMaterialName(material.getName());
                }
            });
        }
        SheetDataBo sheetDataBo = new SheetDataBo(ExcelI18nUtil.getI18n(MaterialTemplateConstant.IMPORT_NAME), MaterialExportVO.class,
                exportVOList, getOptions());
        try {
            ExcelWriterUtils.write(MaterialTemplateConstant.EXPORT_MATERIAL_NAME, response, Lists.newArrayList(sheetDataBo));
            OperationUserDefinedContext.putVariable("operationObject",JsonUtils.toJsonString(exportVOList));
        } catch (Exception e) {
            log.error("导出数据出错", e);
            throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_EXPORT_ERROR);
        }
    }

    private void saveMaterialBatch(List<MaterialTemplateVO> vos){
        if (CollUtil.isEmpty(vos)){
            return;
        }
        List<Material> materialList = vos.stream().map(item -> {
            Material material = MaterialConverter.INSTANCE.convertMaterial(item);
            material.setId(IdUtils.getSnowflake());
            material.setSubMaterial(item.getIsSubMaterial().getValue());
            material.setMaterialCategoryId(item.getCategoryId());
            return material;
        }).collect(Collectors.toList());
        materialMapper.insertBatch(materialList);
        // 物料绑定单位配置
        List<UnitExtendListVO> extendList = unitExtendService.getExtendUnitListByUnitId(
                CollectionUtils.convertList(materialList, Material::getUnitId));
        if(CollUtil.isNotEmpty(extendList)){
            Map<Long, List<Long>> extendMap = CollectionUtils.convertMultiMap(extendList, UnitExtendListVO::getUnitId, UnitExtendListVO::getId);
            List<MaterialBindExtendUnitDTO> list = new ArrayList<>();
            for (Material material : materialList) {
                MaterialBindExtendUnitDTO bindDTO = new MaterialBindExtendUnitDTO();
                List<Long> extendUnitIds = extendMap.get(material.getUnitId());
                if (CollUtil.isEmpty(extendUnitIds)){
                    return;
                }
                bindDTO.setMaterialId(material.getId());
                bindDTO.setExtendUnitIdList(extendUnitIds);
                list.add(bindDTO);
            }
            materialExtendUnitService.bindExtendUnitBatch(list);
        }
    }

    private void writeErrorExcel(HttpServletResponse response,List<MaterialImportErrorVO> importErrorVoList) {

        SheetDataBo sheetDataBo = new SheetDataBo(MaterialTemplateConstant.IMPORT_NAME, MaterialImportErrorVO.class,
                importErrorVoList, getOptions());
        try {
            response.setHeader("error-message", URLEncoder.encode("存在错误数据请处理后重新上传", "utf-8"));
            ExcelWriterUtils.write(MaterialTemplateConstant.EXPORT_ERROR_FILE_NAME, response,
                    Lists.newArrayList(sheetDataBo));
        } catch (Exception e) {
            log.error("导出数据出错", e);
        }
    }

    private List<OptionBo> getOptions() {
        List<String> nameList = IsSubMaterialEnum.getNameList();
        OptionBo nameDownList = new OptionBo(ExcelI18nUtil.getI18n(MaterialTemplateConstant.PULL_DOWN_NAME), 5,
                nameList.stream().map(ExcelI18nUtil::getI18n).collect(Collectors.toList()));
        return Lists.newArrayList(nameDownList);
    }

    private static RemoteIssueDTO getRemoteIssueDTO(MaterialIssueBusinessDTO business, List<Material> materials, List<MaterialCategory> materialCategories, Integer dyingPeriod) {
        List<Integer> childCodeList = business.getChildCodeList();
        RemoteIssueDTO remoteIssueDTO = new RemoteIssueDTO();
        remoteIssueDTO.setMaterialList(MaterialConverter.INSTANCE.convert2IssueVOList(materials, dyingPeriod));
        remoteIssueDTO.setCategoryList(materialCategories);
        remoteIssueDTO.setBusinesses(childCodeList);
        return remoteIssueDTO;
    }

    private void validateMaterialEnabled(Material material) {
        if (ObjectUtil.isNull(material)) {
            throw new BmosException(PlatformResponseCode.MATERIAL_NOT_EXIST);
        }
        if (material.getStatus()) {
            throw new BmosException(PlatformResponseCode.MATERIAL_ENABLED);
        }
    }

    /**
     * 物料分类是否存在
     * 如果是成员物料判断是否选择成员物料，成员物料与当前物料是否属于同一个分类下、并且单位必须相同
     * 合并编码校验
     * 输入的单位是否存在 是否启用
     * @param templateVoList
     * @return
     */
    private Pair<Boolean, List<MaterialImportErrorVO>> importMaterialValid(List<MaterialTemplateVO> templateVoList){
        //分类信息
        List<String> categoryCode = CollectionUtils.convertList(templateVoList, MaterialTemplateVO::getCategoryMergeCode);
        Map<String, MaterialCategory> materialCategoryMap = CollectionUtils.convertMap(
                materialCategoryService.selectListByCategoryCodeList(categoryCode),
                MaterialCategory::getMergeCode);
        //单位信息
        List<String> unitName = CollectionUtils.convertList(templateVoList, MaterialTemplateVO::getUnitName);
        List<Unit> unitList = unitExtendService.selectListByUnitName(unitName);
        Map<String, Unit> unitMap = CollectionUtils.convertMap(unitList, Unit::getUnitName);
        //物料信息
        Map<String, Material> materialMap = CollectionUtils.convertMap(materialMapper.selectList(), Material::getMergeCode);
        List<MaterialImportErrorVO> errorVoList = new ArrayList<>();
        boolean noeError = true;
        for (MaterialTemplateVO vo : templateVoList) {
            StringBuffer errorMsgBuilder = new StringBuffer();
            this.validNull(vo,errorMsgBuilder);
            MaterialCategory materialCategory = materialCategoryMap.get(vo.getCategoryMergeCode());
            Material material  = materialMap.get(vo.getPrincipalMergeCode());
            if (BooleanUtil.isTrue(vo.getIsSubMaterial().getValue())) {
                if (StrUtil.isBlank(vo.getPrincipalMergeCode())) {
                    errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.SUB_MATERIAL_MUST_HAS_PRINCIPAL.getCode(), "")).append(";");
                }
                if (ObjectUtil.isEmpty(material)){
                    errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.WHAT_MATERIAL_ERROR.getCode(), "")).append(";");
                }else {
                    if (ObjectUtil.isEmpty(materialCategory) || !material.getMaterialCategoryId().equals(materialCategory.getId())){
                        errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MATERIAL_ERROR.getCode(), "")).append(";");
                    }
                    if (BooleanUtil.isFalse(material.getStatus())){
                        errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MATERIAL_STATUS_ERROR.getCode(), "")).append(";");
                    }
                    Map<Long, Unit> unitIdMap = CollectionUtils.convertMap(unitList, Unit::getId);
                    Unit unit = unitIdMap.get(material.getUnitId());
                    if (ObjectUtil.isEmpty(unit) || !unit.getUnitName().equals(vo.getUnitName())){
                        errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.SUB_MATERIAL_UNIT_MISMATCH.getCode(), "")).append(";");
                    }
                }
            }
            Unit unit = unitMap.get(vo.getUnitName());
            if (ObjectUtil.isEmpty(unit)){
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.UNIT_NOTFOUND.getCode(), "")).append(";");
            }else if (BooleanUtil.isFalse(unit.getState())){
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.UNIT_NOT_STATE.getCode(), "")).append(";");
            }

            if (ObjectUtil.isEmpty(materialCategory)){
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MATERIAL_CATEGORY_NOT_EXIST.getCode(), "")).append(";");
            }
            String mergeCode = ObjectUtil.isEmpty(materialCategory) ? null : materialCategory.getCode() + vo.getCode();
            if (StrUtil.isNotBlank(mergeCode) && ObjectUtil.isNotEmpty(materialMap.get(mergeCode))){
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MERGE_CODE_EXISTED.getCode(), "")).append(";");
            }
            vo.setCategoryId(ObjectUtil.isEmpty(materialCategory) ? null : materialCategory.getId());
            vo.setMergeCode(vo.getCategoryMergeCode() + vo.getCode());
            vo.setUnitId(ObjectUtil.isEmpty(unit) ? null : unit.getId());
            vo.setPrincipalMaterialId(ObjectUtil.isEmpty(material) ? null : material.getId());
            noeError &= StringUtils.isEmpty(errorMsgBuilder.toString());
            MaterialImportErrorVO importErrorVO = BeanUtil.copyProperties(vo, MaterialImportErrorVO.class);
            importErrorVO.setErrorMsg(errorMsgBuilder.toString());
            errorVoList.add(importErrorVO);
        }
        return Pair.of(noeError, errorVoList);
    }

    private void validNull(MaterialTemplateVO vo,StringBuffer errorMsgBuilder){
        if (StrUtil.isBlank(vo.getCategoryMergeCode())){
            errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MATERIAL_CATEGORY_NOT_EXIST.getCode(), "")).append(";");
        }
        if (StrUtil.isBlank(vo.getName())){
            errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MATERIAL_NAME_ERROR.getCode(), "")).append(";");
        }
        if (StrUtil.isBlank(vo.getCode())){
            errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MATERIAL_CODE_NULL_ERROR.getCode(), "")).append(";");
        }
        if (StrUtil.isBlank(vo.getSpecification())){
            errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.MATERIAL_SPECIFICATION_ERROR.getCode(), "")).append(";");
        }
        if (StrUtil.isBlank(vo.getUnitName())){
            errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.UNIT_NOTFOUND.getCode(), "")).append(";");
        }
    }
}
