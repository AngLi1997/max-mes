package com.bmos.mes.service.lotrelease.template.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.lotrelease.template.convert.LotReleaseTemplateCategoryConvert;
import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplateCategoryCreateDTO;
import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplateCategoryEditDTO;
import com.bmos.mes.service.lotrelease.template.mapper.ILotReleaseTemplateCategoryMapper;
import com.bmos.mes.service.lotrelease.template.mapper.ILotReleaseTemplateMapper;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateCategory;
import com.bmos.mes.service.lotrelease.template.service.ILotReleaseTemplateCategoryService;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateCategoryVO;
import com.bmos.mes.service.weigh.centre.config.util.BmosTreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:29
 */
@Service
@Slf4j
public class LotReleaseTemplateCategoryServiceImpl implements ILotReleaseTemplateCategoryService {

    @Resource
    private ILotReleaseTemplateCategoryMapper lotReleaseTemplateCategoryMapper;

    @Resource
    private ILotReleaseTemplateMapper lotReleaseTemplateMapper;

    private static final String LOG_PREFIX = "[批签发模板分类]";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCategory(LotReleaseTemplateCategoryCreateDTO dto) {
        log.info("{}创建批签发模板分类:{}", LOG_PREFIX, dto);
        String parentIdPath = null;
        if (dto.getParentId() != null){
            LotReleaseTemplateCategory parent = lotReleaseTemplateCategoryMapper.selectById(dto.getParentId());
            if (parent == null){
                throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NOT_EXIST);
            }
            parentIdPath = parent.getIdPath();
        }
        LotReleaseTemplateCategory lotReleaseTemplateCategory = new LotReleaseTemplateCategory();
        lotReleaseTemplateCategory.setParentId(dto.getParentId());
        lotReleaseTemplateCategory.setName(dto.getName());
        List<LotReleaseTemplateCategory> lotReleaseTemplateCategories = lotReleaseTemplateCategoryMapper.listByNameAndParentId(dto.getParentId(), dto.getName());
        if (CollectionUtil.isNotEmpty(lotReleaseTemplateCategories)){
            // 同分类下存在相同名称
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NAME_EXIST);
        }
        lotReleaseTemplateCategoryMapper.insert(lotReleaseTemplateCategory);
        // 更新idPath
        lotReleaseTemplateCategory.setIdPath(parentIdPath == null ? lotReleaseTemplateCategory.getId().toString() : parentIdPath + "," + lotReleaseTemplateCategory.getId());
        lotReleaseTemplateCategoryMapper.updateById(lotReleaseTemplateCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCategory(LotReleaseTemplateCategoryEditDTO dto) {
        log.info("{}编辑批签发模板分类:{}", LOG_PREFIX, dto);
        LotReleaseTemplateCategory exist = lotReleaseTemplateCategoryMapper.selectById(dto.getId());
        if (exist == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NOT_EXIST);
        }
        if (Objects.equals(dto.getName(), exist.getName())){
            return;
        }
        List<LotReleaseTemplateCategory> lotReleaseTemplateCategories = lotReleaseTemplateCategoryMapper.listByNameAndParentId(exist.getParentId(), dto.getName());
        if (CollectionUtil.isNotEmpty(lotReleaseTemplateCategories)){
            // 同分类下存在相同名称
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NAME_EXIST);
        }
        exist.setName(dto.getName());
        lotReleaseTemplateCategoryMapper.updateById(exist);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        log.info("{}删除数据集分类:{}", LOG_PREFIX, id);
        LotReleaseTemplateCategory exist = lotReleaseTemplateCategoryMapper.selectById(id);
        if (exist == null){
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NOT_EXIST);
        }
        List<LotReleaseTemplateCategory> lotReleaseTemplateCategories = lotReleaseTemplateCategoryMapper.listByParentId(id);
        if (CollectionUtil.isNotEmpty(lotReleaseTemplateCategories)){
            // 提示该分类下存在子级，无法删除
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CHILDREN);
        }
        if (CollectionUtil.isNotEmpty(lotReleaseTemplateMapper.listByCategoryId(id))){
            // 提示分类下存在数据集，无法删除
            throw new BmosException(MesResponseCode.LOT_RELEASE_TEMPLATE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CENTRE);
        }
        lotReleaseTemplateCategoryMapper.deleteById(id);
    }

    @Override
    public List<LotReleaseTemplateCategoryVO> queryCategoryTree() {
        List<LotReleaseTemplateCategory> lotReleaseTemplateCategories = lotReleaseTemplateCategoryMapper.selectList();
        List<LotReleaseTemplateCategoryVO> list = LotReleaseTemplateCategoryConvert.INSTANCE.convertToVO(lotReleaseTemplateCategories);
        BmosTreeUtil.buildTree(list, null);
        return list;
    }
}
