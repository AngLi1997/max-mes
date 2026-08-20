package com.bmos.mes.service.record.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.service.record.convert.BatchRecordCategoryConvert;
import com.bmos.mes.service.record.dto.RecordListQueryDTO;
import com.bmos.mes.service.record.enums.RecordStateEnum;
import com.bmos.mes.service.record.mapper.BatchRecordMapper;
import com.bmos.mes.service.record.model.BatchRecordCategory;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.service.BatchRecordCategoryService;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.service.BatchRecordManageService;
import com.bmos.mes.service.record.vo.RecordListVO;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BatchRecordManageServiceImpl implements BatchRecordManageService {

    @Autowired
    private BatchRecordComponentService componentService;

    @Autowired
    private BatchRecordMapper recordMapper;

    @Autowired
    private BatchRecordCategoryService categoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFormula(Long componentId) {
        BatchRecordComponent component = componentService.getById(componentId);
        if (component == null) {
            return Boolean.TRUE;
        }
        componentService.deleteFormula(componentId);
        componentService.refreshGraph(component.getRecordVersionId());
        return Boolean.TRUE;
    }

    @Override
    public CommonPage<RecordListVO> getRecordPageWithNoPermission(RecordListQueryDTO dto) {
        List<RecordListVO> list;
        if (ObjectUtil.isNotNull(dto.getRecordId())) {
            dto.setRecordState(RecordStateEnum.CERTAIN.getValue());
            list = recordMapper.getRecordPage(dto);
        } else {
            if (ObjectUtil.isNotNull(dto.getCategoryId())) {
                List<Long> categoryList = categoryService.selectCategoryList(dto.getCategoryId());
                dto.setCategoryList(categoryList);
            }
            list = recordMapper.getFirstRecord(dto);
        }
        if (ObjectUtil.isNotEmpty(list)) {
            List<BatchRecordCategory> categories = categoryService.selectCategory();
            BatchRecordCategoryConvert.INSTANCE.covertToMap(list, categories);
        }
        return CommonPage.convertPage(list);
    }

}
