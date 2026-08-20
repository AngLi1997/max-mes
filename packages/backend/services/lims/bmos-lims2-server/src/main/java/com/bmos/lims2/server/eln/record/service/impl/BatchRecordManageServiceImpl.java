package com.bmos.lims2.server.eln.record.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.lims2.server.eln.record.convert.BatchRecordCategoryConvert;
import com.bmos.lims2.server.eln.record.dto.RecordListQueryDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordCategory;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.enums.RecordStateEnum;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordMapper;
import com.bmos.lims2.server.eln.record.service.BatchRecordCategoryService;
import com.bmos.lims2.server.eln.record.service.BatchRecordComponentService;
import com.bmos.lims2.server.eln.record.service.BatchRecordManageService;
import com.bmos.lims2.server.eln.record.vo.RecordListVO;
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

    @Autowired
    private com.bmos.lims2.server.inspect.parameter.service.InspectMethodOperateBindService inspectMethodOperateBindService;

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
            // 填充：记录已绑定的操作规程ID集合
            list.forEach(item -> item.setOperateIdList(
                    inspectMethodOperateBindService.listOperateIdsByMethod(item.getRecordId())));
        }
        return CommonPage.convertPage(list);
    }

}
