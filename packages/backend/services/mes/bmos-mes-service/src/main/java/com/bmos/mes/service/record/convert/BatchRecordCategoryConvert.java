package com.bmos.mes.service.record.convert;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.mes.service.record.dto.CategorySaveDTO;
import com.bmos.mes.service.record.model.BatchRecord;
import com.bmos.mes.service.record.model.BatchRecordCategory;
import com.bmos.mes.service.record.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface BatchRecordCategoryConvert {
    BatchRecordCategoryConvert INSTANCE = Mappers.getMapper(BatchRecordCategoryConvert.class);


    BatchRecordCategory convertToCategory(CategorySaveDTO dto);

    List<CategoryListVO> convertToListCategory(List<BatchRecordCategory> list);

    List<ProductRecordTreeVO> convertToTreeVo(List<CategoryListVO> vos);

    List<RecordTreeVO> convertToRecordVo(List<BatchRecord> recordList);

    default void covertToMap(List<RecordListVO> list, List<BatchRecordCategory> categories) {
        Map<Long, List<BatchRecordCategory>> map = CollectionUtils.convertMultiMap(categories, BatchRecordCategory::getId);
        list.forEach(item -> {
            List<BatchRecordCategory> categoryList = map.get(item.getCategoryId());
            if (ObjectUtil.isNotEmpty(categoryList)) {
                List<String> code = StrUtil.split(categoryList.stream().findFirst().get().getCode(), StrUtil.C_COMMA);
                List<String> names = categories.stream().filter(categorie -> code.contains(String.valueOf(categorie.getId())))
                        .sorted(Comparator.comparing(BatchRecordCategory::getSort))
                        .map(BatchRecordCategory::getName)
                        .collect(Collectors.toList());
                StringBuilder paramBuilder = new StringBuilder();
                names.forEach(name -> paramBuilder.append(name).append(StrUtil.C_SLASH));
                item.setCategoryName(paramBuilder.toString());
            }
        });
    }

    default List<RecordExpressionBindTreeNodeVO> convertToRecordExpressionBindTreeNodeVO(List<ExpressionTreeNodeVO> data, List<Long> expressionIdList){
        return data.stream().map(e -> {
            RecordExpressionBindTreeNodeVO vo = new RecordExpressionBindTreeNodeVO();
            vo.setId(e.getId());
            vo.setName(e.getName());
            vo.setParentId(e.getParentId());
            vo.setCategoryFlag(e.getCategoryFlag());
            vo.setBound(expressionIdList.contains(vo.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    BatchRecordTreeNodeVO convertToRecordTreeNodeVO(BatchRecordCategory e);

    @Mapping(target = "parentId", source = "categoryId")
    BatchRecordTreeNodeVO convertToRecordTreeNodeVO(BatchRecord e);
}
