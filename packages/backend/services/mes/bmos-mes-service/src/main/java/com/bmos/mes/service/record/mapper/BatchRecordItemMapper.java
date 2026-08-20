package com.bmos.mes.service.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.record.RecordItemTypeEnum;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.vo.ProcessRecordItemVO;
import com.bmos.mes.service.record.vo.RecordItemListVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


@Mapper
public interface BatchRecordItemMapper extends BaseMapperX<BatchRecordItem> {


    default Boolean saveOrUpdateItem(List<BatchRecordItem> items) {
        return Db.saveOrUpdateBatch(items);
    }

    default List<BatchRecordItem> queryByRecordId(Long id) {
        return selectList(new LambdaQueryWrapperX<BatchRecordItem>()
                .eq(BatchRecordItem::getRecordVersionId, id));
    }

    List<RecordItemListVO> selectItemList(Long recordId);

    default Boolean saveOrUpdateOne(BatchRecordItem item) {
        return Db.saveOrUpdate(item);
    }

    default BatchRecordItem selectItem(Long itemId) {
        return selectOne(new LambdaQueryWrapperX<BatchRecordItem>()
                .eq(BatchRecordItem::getId, itemId));
    }

    default Boolean deleteItem(BatchRecordItem item) {
        return Db.removeById(item);
    }

    List<RecordItemListVO> queryByVersionIdS(@Param("list") List<Long> versionId);

    List<ProcessRecordItemVO> queryRecordByVersionIds(@Param("list") List<Long> versionId);

    default BatchRecordItem selectItemMaxSort(Long recordVersionId) {
        return selectOne(new LambdaQueryWrapperX<BatchRecordItem>()
                .eq(BatchRecordItem::getRecordVersionId, recordVersionId)
                .orderByDesc(BatchRecordItem::getSort)
                .last("limit 1"));
    }

    default List<BatchRecordItem> selectNameByItemId(List<Long> itemIdList) {
        return selectList(new LambdaQueryWrapperX<BatchRecordItem>()
                .in(BatchRecordItem::getId, itemIdList));
    }

    default BatchRecordItem queryRecordItemByItemIdAndVersionId(Long recordItemId, Long recordVersionId) {
        return selectOne(new LambdaQueryWrapperX<BatchRecordItem>()
                .eq(BatchRecordItem::getItemId, recordItemId)
                .eq(BatchRecordItem::getRecordVersionId, recordVersionId));
    }

    List<BatchRecordItem> selectHeaderFooterByRecordVersionIds(@Param("recordVersionIds") Set<Long> recordVersionIds,
                                                               @Param("header") String header,
                                                               @Param("footer") String footer);

    Boolean existsRecordItem(@Param("recordVersionId") Long recordVersionId,
                             @Param("recordItemId") Long recordItemId);

    default List<BatchRecordItem> queryItemListByVersionIdList(List<Long> versionId){
        return selectList(new LambdaQueryWrapperX<BatchRecordItem>()
                .in(BatchRecordItem::getRecordVersionId,versionId));
    }

    default List<BatchRecordItem> selectByItemIdsAndRecordVersionIds(List<Long> itemIds, List<Long> recordVersionIds){
        return selectList(new LambdaQueryWrapperX<BatchRecordItem>()
                .in(BatchRecordItem::getItemId,itemIds)
                .in(BatchRecordItem::getRecordVersionId, recordVersionIds));
    }

}
