package com.bmos.mes.service.record.service;

import com.bmos.mes.service.record.dto.ItemNameChangeDTO;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.vo.ItemBaseInfoVO;
import com.bmos.mes.service.record.vo.ProcessRecordItemVO;
import com.bmos.mes.service.record.vo.RecordItemListVO;
import com.bmos.mes.service.record.vo.RecordItemVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BatchRecordItemService {


    Boolean saveOrUpdateItem(List<BatchRecordItem> items);

    List<RecordItemListVO> selectItemList(Long versionId);

    Boolean saveOrUpdateOne(BatchRecordItem convertToItemDo);

    BatchRecordItem selectItem(Long id);

    Boolean deleteItem(BatchRecordItem item);

    List<BatchRecordItem> queryByRecordId(Long versionOldId);

    List<Long> productionId();

    List<ProcessRecordItemVO> listRecordItem(List<Long> versionId);

    BatchRecordItem selectItemMaxSort(Long recordVersionId);

    Map<Long, String> selectNameByItemId(List<Long> itemIdList);

    RecordItemVO queryRecordItemByItemIdAndVersionId(Long recordItemId, Long recordVersionId);

    BatchRecordItem queryByItemIdAndVersionId(Long itemId, Long recordVersionId);

    List<BatchRecordItem> getHeaderFooterByRecordVersionIds(Set<Long> recordVersionIds);

    Boolean existRecordItem(Long recordVersionId, Long recordItemId);

    List<BatchRecordItem> queryItemListByVersionIdList(List<Long> versionId);

    void handelItem();


    List<BatchRecordItem> queryByItemIdsAndVersionIds(List<Long> itemIds, List<Long> recordVersionIds);
    /**
     * 查询记录项基础信息列表
     * @param recordVersionId
     * @return
     */
    List<ItemBaseInfoVO> selectItemBaseInfoList(Long recordVersionId);

    /**
     * 记录项名称修改
     * @param dto
     */
    void changeItemName(ItemNameChangeDTO dto);

}
