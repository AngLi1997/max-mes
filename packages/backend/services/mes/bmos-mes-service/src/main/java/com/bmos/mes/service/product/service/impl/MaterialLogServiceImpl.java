package com.bmos.mes.service.product.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.material.MaterialOperationTypeEnum;
import com.bmos.mes.common.enums.material.MaterialOperationTypeShowEnum;
import com.bmos.mes.service.product.convert.MaterialLogConverter;
import com.bmos.mes.service.product.dto.MaterialLogPageQueryDTO;
import com.bmos.mes.service.product.dto.MaterialLogSaveDTO;
import com.bmos.mes.service.product.mapper.MaterialLogMapper;
import com.bmos.mes.service.product.model.MaterialLog;
import com.bmos.mes.service.product.service.MaterialLogService;
import com.bmos.mes.service.product.vo.MaterialLogPageVO;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaterialLogServiceImpl implements MaterialLogService {

    @Autowired
    private MaterialLogMapper materialLogMapper;

    @Override
    public CommonPage<MaterialLogPageVO> getMaterialLogPage(MaterialLogPageQueryDTO dto) {
        List<Integer> opList = new ArrayList<>();
        if (StrUtil.isNotBlank(dto.getOperationType())){
            opList = Optional.ofNullable(dto.getOperationType())
                    .map(MaterialOperationTypeShowEnum::getByName)
                    .map(MaterialOperationTypeShowEnum::getOperate)
                    .map(MaterialOperationTypeEnum::getByName)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(MaterialOperationTypeEnum::getValue)
                    .collect(Collectors.toList());
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        dto.convert2Date();
        List<MaterialLogPageVO> materialLogs = materialLogMapper.selectPageVOList(dto, opList);
        return CommonPage.convertPage(materialLogs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMaterialLog(MaterialLogSaveDTO log) {
        materialLogMapper.insert(MaterialLogConverter.INSTANCE.convertToMaterialLog(log));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMaterialLogs(List<MaterialLog> logs) {
        materialLogMapper.insertBatch(logs);
    }

}
