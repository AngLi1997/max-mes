package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.AuditPerorationStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.dict.vo.DictVO;
import com.bmos.mes.service.process.convert.ProcessConfirmConverter;
import com.bmos.mes.service.process.dto.ConfirmUpdateDTO;
import com.bmos.mes.service.process.dto.query.AuditOpinionQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessConfirmQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcessConfirmSaveDTO;
import com.bmos.mes.service.process.mapper.ProcessConfirmMapper;
import com.bmos.mes.service.process.model.ProcessConfirm;
import com.bmos.mes.service.process.service.ProcessConfirmService;
import com.bmos.mes.service.process.vo.AuditOpinionVO;
import com.bmos.mes.service.process.vo.ProcessConfirmVO;
import com.bmos.mes.service.process.vo.StatisticsVO;
import com.bmos.mes.service.product.service.ProductMaterialCategoryService;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessConfirmServiceImpl implements ProcessConfirmService {

    @Autowired
    private ProcessConfirmMapper mapper;

    @Autowired
    private ProductMaterialCategoryService productMaterialCategoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcessConfirm(ProcessConfirmSaveDTO saveDTO) {
        ProcessConfirm processConfirm = ProcessConfirmConverter.INSTANCE.convertToConfirm(saveDTO);
        mapper.saveProcessConfirm(processConfirm);
    }

    @Override
    public ProcessConfirm queryProcessConfirmByInstanceId(String instanceId) {
        return mapper.queryProcessConfirmByInstanceId(instanceId);
    }

    @Override
    public CommonPage<ProcessConfirmVO> getProcessConfirmPageList(ProcessConfirmQueryDTO dto) {
        if (ObjectUtil.isNotNull(dto.getProductCategoryId())) {
            List<Long> categoryIdList = productMaterialCategoryService.getAllChildCategory(dto.getProductCategoryId());
            List<Long> productIdList = productMaterialCategoryService.getIdListByCategoryIdList(categoryIdList);
            if (CollUtil.isEmpty(productIdList)) {
                BasePage page = new BasePage();
                page.setPageNum(dto.getPageNum());
                page.setPageSize(dto.getPageSize());
                return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
            }
            dto.setProductIdList(productIdList);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<ProcessConfirmVO> list = mapper.getProcessConfirmPageList(dto);
        return CommonPage.convertPage(list);
    }

    @Override
    public Set<DictVO> getProcessNameList() {
        List<ProcessConfirm> processNameList = mapper.getProcessNameList();
        if (CollUtil.isEmpty(processNameList)) {
            return Collections.emptySet();
        }
        return processNameList.stream().map(processList -> {
            DictVO vo = new DictVO();
            vo.setLabel(processList.getProcessName());
            vo.setId(processList.getProcessId());
            return vo;
        }).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProcessOpinion(ConfirmUpdateDTO dto) {
        ProcessConfirm processConfirm = mapper.queryProcessById(dto.getId());
        if (ObjectUtil.isEmpty(processConfirm)) {
            throw new BmosException(MesResponseCode.PROCESS_DATE_ERROR);
        }
        processConfirm.setConfirmOpinion(AuditPerorationStateEnum.valueOf(dto.getOpinion()));
        processConfirm.setRemark(dto.getRemark());
        processConfirm.setConfirmTime(LocalDateTime.now());
        return mapper.saveProcessConfirm(processConfirm);
    }

    @Override
    public CommonPage<AuditOpinionVO> listProcessOpinionPage(AuditOpinionQueryDTO dto) {
        if (ObjectUtil.isNull(dto.getProcessId())) {
            BasePage page = new BasePage();
            page.setPageNum(dto.getPageNum());
            page.setPageSize(dto.getPageSize());
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }
        List<AuditOpinionVO> list;
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        if (StrUtil.isNotBlank(dto.getProcedureName())) {
            //查询工序结论
            list = mapper.listProcedureOpinionPage(dto);
        } else {
            list = mapper.listProcessOpinionPage(dto);
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public List<StatisticsVO> processStatistics(AuditOpinionQueryDTO dto) {
        if (ObjectUtil.isNull(dto.getProcessId())) {
            return Collections.emptyList();
        }
        List<StatisticsVO> voList = new ArrayList<>();
        if (StrUtil.isNotBlank(dto.getProcedureName())) {
            List<AuditOpinionVO> list = mapper.listProcedureOpinionPage(dto);
            Map<AuditPerorationStateEnum, List<AuditOpinionVO>> map = CollectionUtils.convertMultiMap(list, AuditOpinionVO::getConfirmOpinion);
            map.forEach((key, value) -> {
                StatisticsVO vo = new StatisticsVO();
                vo.setNumber(value.size());
                vo.setConfirmOpinion(key);
                voList.add(vo);
            });
            return voList;
        }
        List<AuditOpinionVO> list = mapper.listProcessOpinionPage(dto);
        Map<AuditPerorationStateEnum, List<AuditOpinionVO>> map = CollectionUtils.convertMultiMap(list, AuditOpinionVO::getConfirmOpinion);
        map.forEach((key, value) -> {
            StatisticsVO vo = new StatisticsVO();
            vo.setNumber(value.size());
            vo.setConfirmOpinion(key);
            voList.add(vo);
        });
        return voList;
    }
}
