package com.bmos.lims2.server.inspect.item.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.convert.InspectItemConvert;
import com.bmos.lims2.server.inspect.item.dto.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.lims2.server.inspect.item.entity.InspectItem;
import com.bmos.lims2.server.inspect.item.entity.InspectItemParameter;
import com.bmos.lims2.server.inspect.item.mapper.InspectItemMapper;
import com.bmos.lims2.server.inspect.item.mapper.InspectItemParameterMapper;
import com.bmos.lims2.server.inspect.item.mapper.InspectPackageItemMapper;
import com.bmos.lims2.server.inspect.item.service.InspectItemService;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDTO;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InspectItemServiceImpl implements InspectItemService {

    @Autowired
    InspectItemMapper inspectItemMapper;

    @Autowired
    InspectItemParameterMapper inspectAnalyzeMapper;

    @Autowired
    InspectPackageItemMapper packageItemMapper;

    @Autowired
    InspectParameterService inspectParameterService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> saveInspectProgram(InspectItemWithParameterDTO reqVO) {
        // 验证检验项目编码是否存在
        List<Long> dubAnalyzeIdList = validSaveReq(reqVO);
        if (CollectionUtil.isNotEmpty(dubAnalyzeIdList)) {
            return dubAnalyzeIdList;
        }
        // 代表没有重复的分析项
        // 新增检验项
        doSaveInspectProgram(reqVO);
        return dubAnalyzeIdList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectProgram(Long id) {
        // 验证删除参数的合法性
        validDeleteReq(id);
        doDeleteInspectProgram(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> updateInspectProgram(InspectItemWithParameterDTO inspectItemWithParameterDTO) {
        InspectItem inspectItem = inspectItemMapper.selectById(inspectItemWithParameterDTO.getId());
        if (Objects.isNull(inspectItem)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        List<Long> dubAnalyzeIdList = validDubAnalyze(inspectItemWithParameterDTO.getParameterList());
        if (CollectionUtil.isNotEmpty(dubAnalyzeIdList)) {
            return dubAnalyzeIdList;
        }
        doUpdate(inspectItemWithParameterDTO, inspectItem);
        return dubAnalyzeIdList;
    }

    @Override
    public CommonPage<InspectItemWithParameterDTO> inspectProgramPage(InspectItemPageReqDTO inspectItemPageReqDTO) {
        // 第一步：仅分页查询主表ID（排序在XML中按分析项编码正序处理）
        PageHelper.startPage(inspectItemPageReqDTO.getPageNum(), inspectItemPageReqDTO.getPageSize());
        InspectItemParamDTO inspectParam = InspectItemConvert.INSTANCE.convert2Param(inspectItemPageReqDTO);
        List<Long> ids = inspectItemMapper.selectIdsByParam(inspectParam);
        CommonPage<Long> idPage = CommonPage.convertPage(ids);
        if (CollectionUtil.isEmpty(idPage.getList())) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, inspectItemPageReqDTO);
        }

        // 第二步：按ID集合查询详情（包含关联项及聚合）
        List<InspectItemWithParameterDTO> details = inspectItemMapper.selectDetailsByIds(idPage.getList());
        return CommonPage.CommonPage(details, Long.valueOf(idPage.getTotal()), inspectItemPageReqDTO);
    }

    @Override
    public InspectItemWithParameterDTO inspectProgramInfo(Long id) {
        InspectItem inspectDO = inspectItemMapper.selectById(id);
        if (Objects.isNull(inspectDO)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        InspectItemWithParameterDTO inspectItemWithParameterDTO = InspectItemConvert.INSTANCE.convert2InfoRespVO(inspectDO);
        // 查询关联关系下的分析项信息
        List<InspectItemParameterDTO> analyzeInfoRespVOS = new ArrayList<>();
        inspectItemWithParameterDTO.setParameterList(analyzeInfoRespVOS);
        List<InspectItemParameter> inspectItemParameters = inspectAnalyzeMapper.selectByInspectId(id);
        if (CollectionUtil.isEmpty(inspectItemParameters)) {
            return inspectItemWithParameterDTO;
        }
        // 根据分析项id查询分析项完整信息（包含数据点）
        List<Long> analyzeIdList = inspectItemParameters.stream().map(InspectItemParameter::getInspectParameterId).collect(Collectors.toList());
        List<InspectParameterDTO> analyzeDOList = inspectParameterService.selectFullInfoByIdList(analyzeIdList);
        Map<Long, InspectParameterDTO> analyzeDOMap = analyzeDOList.stream().collect(Collectors.toMap(InspectParameterDTO::getId, Function.identity()));
        for (InspectItemParameter inspectAnalyzeDO : inspectItemParameters) {
            InspectParameterDTO analyzeDO = analyzeDOMap.get(inspectAnalyzeDO.getInspectParameterId());
            if (analyzeDO != null) {
                InspectItemParameterDTO inspectAnalyzeInfoRespVO = InspectItemConvert.INSTANCE.convert2InspectAnalyzeRespVO(inspectAnalyzeDO);
                inspectAnalyzeInfoRespVO.setCode(analyzeDO.getCode());
                inspectAnalyzeInfoRespVO.setName(analyzeDO.getName());
                inspectAnalyzeInfoRespVO.setStandard(analyzeDO.getStandard());
                inspectAnalyzeInfoRespVO.setDataPoints(analyzeDO.getDataPoints());
                analyzeInfoRespVOS.add(inspectAnalyzeInfoRespVO);
            }
        }
        return inspectItemWithParameterDTO;
    }

    @Override
    public List<InspectItemDTO> selectByIdList(List<Long> idList) {
        return BeanUtil.copyToList(inspectItemMapper.selectBatchIds(idList), InspectItemDTO.class);
    }

    @Override
    public List<InspectItemListDTO> getList() {
        LambdaQueryWrapper<InspectItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(InspectItem::getId, InspectItem::getCode, InspectItem::getName)
               .eq(InspectItem::getDeleted, false)
               .orderByDesc(InspectItem::getCreateTime);

        List<InspectItem> entityList = inspectItemMapper.selectList(wrapper);
        return BeanUtil.copyToList(entityList, InspectItemListDTO.class);
    }

    @Override
    public List<InspectItemWithParameterDTO> listAllWithParameters() {
        LambdaQueryWrapper<InspectItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectItem::getDeleted, false)
               .orderByDesc(InspectItem::getCreateTime);
        List<Long> ids = inspectItemMapper.selectList(wrapper).stream()
                .map(InspectItem::getId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return inspectItemMapper.selectDetailsByIds(ids);
    }

    /**
     * 新增检验项以及检验项与分析项的关联关系
     *
     * @param reqVO
     */
    private void doSaveInspectProgram(InspectItemWithParameterDTO reqVO) {
        InspectItem inspectDO = InspectItemConvert.INSTANCE.convert2DO(reqVO);
        inspectItemMapper.insert(inspectDO);
        // 新增检验项下的分析项
        if (CollectionUtil.isEmpty(reqVO.getParameterList())) {
            return;
        }
        List<InspectItemParameter> inspectAnalyzeDOList = new ArrayList<>();
        for (InspectItemParameterDTO inspectItemParameterDTO : reqVO.getParameterList()) {
            InspectItemParameter inspectAnalyzeDO = InspectItemConvert.INSTANCE.convert2InspectAnalyzeDO(inspectItemParameterDTO);
            inspectAnalyzeDO.setInspectItemId(inspectDO.getId());
            inspectAnalyzeDOList.add(inspectAnalyzeDO);
        }
        inspectAnalyzeMapper.insertBatch(inspectAnalyzeDOList);
    }

    /**
     * 更新检品项目信息
     *
     * @param inspectItemWithParameterDTO
     */
    private void doUpdate(InspectItemWithParameterDTO inspectItemWithParameterDTO, InspectItem inspectItem) {
        inspectItem.setName(inspectItemWithParameterDTO.getName());
        inspectItem.setRemark(inspectItemWithParameterDTO.getRemark());
        inspectItemMapper.updateById(inspectItem);
        // 构建新的关联关系
        List<InspectItemParameter> inspectItemParameters = inspectAnalyzeMapper.selectByInspectId(inspectItem.getId());
        Set<Long> curAnalyzeIdSet = inspectItemWithParameterDTO.getParameterList().stream().map(InspectItemParameterDTO::getInspectParameterId).collect(Collectors.toSet());
        // 需要解除绑定的
        List<InspectItemParameter> deleteInspectDOList = new ArrayList<>();
        Map<Long, InspectItemParameter> longInspectItemParameterMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(inspectItemParameters)) {
            inspectItemParameters.forEach(inspectAnalyzeDO -> {
                longInspectItemParameterMap.put(inspectAnalyzeDO.getInspectParameterId(), inspectAnalyzeDO);
                if (!curAnalyzeIdSet.contains(inspectAnalyzeDO.getInspectParameterId())) {
                    deleteInspectDOList.add(inspectAnalyzeDO);
                }
            });
        }
        // 需要新增的关联关系
        List<InspectItemParameter> needSaveInspectAnalyzeDOList = new ArrayList<>();
        // 需要修改的关联关系信息
        List<InspectItemParameter> inspectItemParameterList = new ArrayList<>();
        for (InspectItemParameterDTO inspectItemParameterDTO : inspectItemWithParameterDTO.getParameterList()) {
            if (longInspectItemParameterMap.containsKey(inspectItemParameterDTO.getInspectParameterId())) {
                InspectItemParameter inspectAnalyzeDO = longInspectItemParameterMap.get(inspectItemParameterDTO.getInspectParameterId());
                inspectItemParameterList.add(inspectAnalyzeDO);
            } else {
                InspectItemParameter inspectAnalyzeDO = InspectItemConvert.INSTANCE.convert2InspectAnalyzeDO(inspectItemParameterDTO);
                inspectAnalyzeDO.setInspectItemId(inspectItem.getId());
                needSaveInspectAnalyzeDOList.add(inspectAnalyzeDO);
            }
        }
        if (CollectionUtil.isNotEmpty(deleteInspectDOList)) {
            inspectAnalyzeMapper.deleteBatchIds(deleteInspectDOList);
        }
        if (CollectionUtil.isNotEmpty(needSaveInspectAnalyzeDOList)) {
            inspectAnalyzeMapper.insertBatch(needSaveInspectAnalyzeDOList);
        }
        if (CollectionUtil.isNotEmpty(inspectItemParameterList)) {
            inspectAnalyzeMapper.updateBatch(inspectItemParameterList);
        }
    }

    /**
     * 删除检验项
     * 1. 删除检验项
     * 2. 删除检验项与分析项的关联关系
     *
     * @param id
     */
    private void doDeleteInspectProgram(Long id) {
        inspectItemMapper.deleteById(id);
        inspectAnalyzeMapper.deleteByInspectId(id);
    }

    /**
     * 校验检验项目保存参数
     * 1. 编码不能重复
     * 2. 检验项目下的分析项目不能重复
     *
     * @param reqVO
     */
    private List<Long> validSaveReq(InspectItemWithParameterDTO reqVO) {
        if (inspectItemMapper.existByCode(reqVO.getCode())) {
            throw new BmosException(LimsResponseCode.CODE_EXISTED);
        }
        // 验证分析项是否重复
        return validDubAnalyze(reqVO.getParameterList());
    }

    /**
     * 验证删除参数的合法性
     * 1，检验项是否存在
     * 2. 检验项是否挂载在实验包下
     *
     * @param id：检验项id
     */
    private void validDeleteReq(Long id) {
        if (!inspectItemMapper.existById(id)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        if (packageItemMapper.existByInspectId(id)) {
            throw new BmosException(LimsResponseCode.INSPECT_PROGRAM_BIND_PACKAGE);
        }
    }

    /**
     * 校验分析项目是否重复 以及当前分析项为报告项则必须配置标准规定
     *
     * @param inspectAnalyzeVOList
     * @return：重复的分析项id集合
     */
    private List<Long> validDubAnalyze(List<InspectItemParameterDTO> inspectAnalyzeVOList) {
        List<Long> dubAnalyzeIdList = new ArrayList<>();
        if (CollectionUtil.isEmpty(inspectAnalyzeVOList)) {
            return dubAnalyzeIdList;
        }
        Set<Long> analysisIds = new HashSet<>();
        for (InspectItemParameterDTO inspectAnalyzeVO : inspectAnalyzeVOList) {
            if (analysisIds.contains(inspectAnalyzeVO.getInspectParameterId())) {
                dubAnalyzeIdList.add(inspectAnalyzeVO.getInspectParameterId());
            } else {
                analysisIds.add(inspectAnalyzeVO.getInspectParameterId());
            }
        }
        return dubAnalyzeIdList;
    }
}
