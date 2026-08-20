package com.bmos.lims2.server.eln.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.enums.RecordItemTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.convert.RecordItemConvert;
import com.bmos.lims2.server.eln.record.dto.ItemNameChangeDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordItem;
import com.bmos.lims2.server.eln.record.entity.BatchRecordParse;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordItemMapper;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordParseMapper;
import com.bmos.lims2.server.eln.record.service.BatchRecordItemService;
import com.bmos.lims2.server.eln.record.service.BatchRecordParseService;
import com.bmos.lims2.server.eln.record.vo.ItemBaseInfoVO;
import com.bmos.lims2.server.eln.record.vo.ProcessRecordItemVO;
import com.bmos.lims2.server.eln.record.vo.RecordItemListVO;
import com.bmos.lims2.server.eln.record.vo.RecordItemVO;
import com.bmos.mybatis.CustomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BatchRecordItemServiceImpl implements BatchRecordItemService {

    @Autowired
    private BatchRecordItemMapper itemMapper;

    @Resource
    private BatchRecordParseMapper parseMapper;
    @Resource
    private BatchRecordParseService parseService;


    @Override
    public Boolean saveOrUpdateItem(List<BatchRecordItem> items) {
        return itemMapper.saveOrUpdateItem(items);
    }

    @Override
    public List<RecordItemListVO> selectItemList(Long versionId) {
        return itemMapper.selectItemList(versionId);
    }

    @Override
    public Boolean saveOrUpdateOne(BatchRecordItem convertToItemDo) {
        return itemMapper.saveOrUpdateOne(convertToItemDo);
    }

    @Override
    public BatchRecordItem selectItem(Long itemId) {
        return itemMapper.selectItem(itemId);
    }

    @Override
    public Boolean deleteItem(BatchRecordItem item) {
        return itemMapper.deleteItem(item);
    }

    @Override
    public List<BatchRecordItem> queryByRecordId(Long versionOldId) {
        return itemMapper.queryByRecordId(versionOldId);
    }

    @Override
    public List<Long> productionId() {
        try {
            List<Long> idList = new ArrayList<>();
            for (int i = 0; i < RecordConstant.PRODUCTION_ID_MAX; i++) {
                idList.add(CustomIdGenerator.nextId());
            }
            return idList;
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.RECORD_PRODUCTION_ID_ERROR);
        }
    }

    @Override
    public List<ProcessRecordItemVO> listRecordItem(List<Long> versionId) {
        List<ProcessRecordItemVO> recordNameList = itemMapper.queryRecordByVersionIds(versionId);
        List<RecordItemListVO> list = itemMapper.queryByVersionIdS(versionId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Long, List<RecordItemListVO>> itemMap = CollectionUtils.convertMultiMap(list, RecordItemListVO::getRecordVersionId);
            recordNameList.forEach(item -> {
                List<RecordItemListVO> vos = itemMap.get(item.getVersionId());
                item.setRecordItemList(vos);
            });
        }
        return recordNameList;
    }

    @Override
    public BatchRecordItem selectItemMaxSort(Long recordVersionId) {
        return itemMapper.selectItemMaxSort(recordVersionId);
    }

    @Override
    public Map<Long, String> selectNameByItemId(List<Long> itemIdList) {
        if (CollUtil.isEmpty(itemIdList)) {
            return Collections.emptyMap();
        }
        List<BatchRecordItem> items = itemMapper.selectNameByItemId(itemIdList);
        return CollectionUtils.convertMap(items, BatchRecordItem::getId, BatchRecordItem::getName);
    }

    @Override
    public RecordItemVO queryRecordItemByItemIdAndVersionId(Long recordItemId, Long recordVersionId) {
        return RecordItemConvert.INSTANCE.convertToItemVo(this.queryByItemIdAndVersionId(recordItemId, recordVersionId));
    }

    @Override
    public BatchRecordItem queryByItemIdAndVersionId(Long itemId, Long recordVersionId) {
        BatchRecordItem item = itemMapper.queryRecordItemByItemIdAndVersionId(itemId, recordVersionId);
        if (ObjectUtil.isEmpty(item)){
            return new BatchRecordItem();
        }
        BatchRecordParse parse = parseMapper.selectById(item.getId());
        if (ObjectUtil.isNotEmpty(parse)){
            item.setFileContent(Optional.ofNullable(parse.getFileContent()).orElse(null));
            item.setDocxFooter(Optional.ofNullable(parse.getDocxFooter()).orElse(null));
            item.setDocxHeader(Optional.ofNullable(parse.getDocxHeader()).orElse(null));
        }
        return item;
    }

    @Override
    public List<BatchRecordItem> getHeaderFooterByRecordVersionIds(Set<Long> recordVersionIds) {
        List<BatchRecordItem> items = itemMapper.selectHeaderFooterByRecordVersionIds(recordVersionIds,
                RecordItemTypeEnum.HEADER_TYPE.getType(), RecordItemTypeEnum.FOOTER_TYPE.getType());
        List<Long> recordItemIdList = CollectionUtils.convertList(items, BatchRecordItem::getId);
        Map<Long, BatchRecordParse> parseMap = CollectionUtils.convertMap(parseMapper.selectByItemId(recordItemIdList), BatchRecordParse::getId);
        if (CollUtil.isNotEmpty(items) && CollUtil.isNotEmpty(parseMap)){
            return Collections.emptyList();
        }
        items.forEach(item->{
            BatchRecordParse parse = parseMap.get(item.getId());
            if (ObjectUtil.isEmpty(parse)){
                return;
            }
            item.setFileContent(Optional.ofNullable(parse.getFileContent()).orElse(null));
            item.setDocxFooter(Optional.ofNullable(parse.getDocxFooter()).orElse(null));
            item.setDocxHeader(Optional.ofNullable(parse.getDocxHeader()).orElse(null));
        });
        return items;
    }

    @Override
    public Boolean existRecordItem(Long recordVersionId, Long recordItemId) {
        Boolean exist = itemMapper.existsRecordItem(recordVersionId, recordItemId);
        return ObjectUtil.isNotNull(exist) && exist;
    }

    @Override
    public List<BatchRecordItem> queryItemListByVersionIdList(List<Long> versionId) {
        List<BatchRecordItem> batchRecordItems = itemMapper.queryItemListByVersionIdList(versionId);
        if (CollUtil.isEmpty(batchRecordItems)){
            return batchRecordItems;
        }
        List<Long> recordItemId = CollectionUtils.convertList(batchRecordItems, BatchRecordItem::getId);
        Map<Long, BatchRecordParse> parseMap = CollectionUtils.convertMap(parseService.selectByItemId(recordItemId), BatchRecordParse::getId);
        if (CollUtil.isNotEmpty(batchRecordItems) && CollUtil.isNotEmpty(parseMap)){
            batchRecordItems.forEach(item->{
                BatchRecordParse parse = parseMap.get(item.getId());
                item.setFileContent(ObjectUtil.isEmpty(parse) ? null : parse.getFileContent());
                item.setDocxFooter(ObjectUtil.isEmpty(parse) ? null : parse.getDocxFooter());
                item.setDocxHeader(ObjectUtil.isEmpty(parse) ? null : parse.getDocxHeader());
            });
        }
        return batchRecordItems;
    }

    @Override
    public void handelItem() {
        List<BatchRecordItem> items = itemMapper.selectList();
        if (CollUtil.isEmpty(items)){
            return;
        }
        List<BatchRecordParse> parses = items.stream().map(item -> {
            BatchRecordParse parse = new BatchRecordParse();
            parse.setFileContent(item.getFileContent());
            parse.setDocxFooter(item.getDocxFooter());
            parse.setDocxHeader(item.getDocxHeader());
            parse.setId(item.getId());
            return parse;
        }).collect(Collectors.toList());
        parseMapper.saveOrUpdateParse(parses);
    }

    @Override
    public List<BatchRecordItem> queryByItemIdsAndVersionIds(List<Long> itemIds, List<Long> recordVersionIds) {
        if (CollUtil.isEmpty(itemIds) || CollUtil.isEmpty(recordVersionIds)) {
            return new ArrayList<>();
        }
        List<BatchRecordItem> batchRecordItems = itemMapper.selectByItemIdsAndRecordVersionIds(itemIds, recordVersionIds);
        if (CollUtil.isEmpty(batchRecordItems)) {
            return new ArrayList<>();
        }
        List<BatchRecordParse> batchRecordParses = parseMapper.selectBatchIds(CollectionUtils.convertList(batchRecordItems, BatchRecordItem::getId));
        Map<Long, BatchRecordParse> map = CollectionUtils.convertMap(batchRecordParses, BatchRecordParse::getId);
        batchRecordItems.forEach(item -> {
            BatchRecordParse parse = map.get(item.getId());
            if (ObjectUtil.isNotEmpty(parse)) {
                item.setFileContent(parse.getFileContent());
                item.setDocxFooter(parse.getDocxFooter());
                item.setDocxHeader(parse.getDocxHeader());
            }
        });
        return batchRecordItems;
    }

    @Override
    public List<ItemBaseInfoVO> selectItemBaseInfoList(Long recordVersionId) {
        List<BatchRecordItem> batchRecordItems = itemMapper.queryByRecordId(recordVersionId);
        return RecordItemConvert.INSTANCE.convert2ItemBaseInfo(batchRecordItems);
    }

    @Override
    public void changeItemName(ItemNameChangeDTO dto) {
        BatchRecordItem batchRecordItem = itemMapper.selectById(dto.getId());
        if (batchRecordItem == null) {
            throw new BmosException(LimsResponseCode.RECORD_ITEM_NOT_EXIST);
        }
        batchRecordItem.setName(dto.getName());
        itemMapper.updateById(batchRecordItem);
    }


}
