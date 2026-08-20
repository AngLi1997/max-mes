package com.bmos.mes.service.weigh.centre.config.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.weigh.centre.config.convert.WeighCentreCategoryConvert;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCategoryCreateDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCategoryEditDTO;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreCategoryMapper;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentreCategory;
import com.bmos.mes.service.weigh.centre.config.service.IWeighCentreCategoryService;
import com.bmos.mes.service.weigh.centre.config.util.BmosTreeUtil;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 称量中心分类service impl
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:36
 */
@Service
@Slf4j
public class WeighCentreCategoryServiceImpl implements IWeighCentreCategoryService {

    private static final String LOG_PREFIX = "[称量中心分类]";

    @Resource
    private IWeighCentreCategoryMapper weighCentreCategoryMapper;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Override
    public List<WeighCentreCategoryVO> categoryTree() {
        List<WeighCentreCategory> weighCentreCategories = weighCentreCategoryMapper.selectList();
        List<WeighCentreCategoryVO> list = WeighCentreCategoryConvert.INSTANCE.convertToVO(weighCentreCategories);
        BmosTreeUtil.buildTree(list, null);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCategory(WeighCentreCategoryCreateDTO createDTO) {
        log.info("{}创建称量中心分类:{}", LOG_PREFIX, createDTO);

        String parentIdPath = null;
        if (createDTO.getParentId() != null){
            WeighCentreCategory parent = weighCentreCategoryMapper.selectById(createDTO.getParentId());
            if (parent == null){
                throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NOT_EXIST);
            }
            parentIdPath = parent.getIdPath();
        }

        WeighCentreCategory weighCentreCategory = new WeighCentreCategory();
        weighCentreCategory.setParentId(createDTO.getParentId());
        weighCentreCategory.setName(createDTO.getName());
        List<WeighCentreCategory> weighCentreCategories = weighCentreCategoryMapper.listByNameAndParentId(createDTO.getParentId(), createDTO.getName());
        if (CollectionUtil.isNotEmpty(weighCentreCategories)){
            // 同分类下存在相同名称
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NAME_EXIST);
        }
        weighCentreCategoryMapper.insert(weighCentreCategory);

        // 更新idPath
        weighCentreCategory.setIdPath(parentIdPath == null ? weighCentreCategory.getId().toString() : parentIdPath + "," + weighCentreCategory.getId());
        weighCentreCategoryMapper.updateById(weighCentreCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCategory(WeighCentreCategoryEditDTO editDTO) {
        log.info("{}编辑称量中心分类:{}", LOG_PREFIX, editDTO);
        WeighCentreCategory exist = weighCentreCategoryMapper.selectById(editDTO.getId());
        if (exist == null){
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NOT_EXIST);
        }
        if (Objects.equals(editDTO.getName(), exist.getName())){
            return;
        }
        List<WeighCentreCategory> weighCentreCategories = weighCentreCategoryMapper.listByNameAndParentId(exist.getParentId(), editDTO.getName());
        if (CollectionUtil.isNotEmpty(weighCentreCategories)){
            // 同分类下存在相同名称
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NAME_EXIST);
        }
        exist.setName(editDTO.getName());
        weighCentreCategoryMapper.updateById(exist);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        log.info("{}删除称量中心分类:{}", LOG_PREFIX, id);
        WeighCentreCategory exist = weighCentreCategoryMapper.selectById(id);
        if (exist == null){
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NOT_EXIST);
        }
        List<WeighCentreCategory> weighCentreCategories = weighCentreCategoryMapper.listByParentId(id);
        if (CollectionUtil.isNotEmpty(weighCentreCategories)){
            // 提示该分类下存在子级，无法删除
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CHILDREN);
        }
        if (CollectionUtil.isNotEmpty(weighCentreMapper.listByCategoryId(id))){
            // 提示分类下存在称量中心，无法删除
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NOT_ALLOWED_DELETED_WITH_CENTRE);
        }
        weighCentreCategoryMapper.deleteById(id);
    }
}
