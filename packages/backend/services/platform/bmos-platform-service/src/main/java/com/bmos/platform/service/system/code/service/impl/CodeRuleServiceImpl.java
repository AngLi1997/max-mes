package com.bmos.platform.service.system.code.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.pojo.KeyValue;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.code.dto.BatchCodeNoDTO;
import com.bmos.platform.facade.code.dto.BatchConfirmByCodeDTO;
import com.bmos.platform.facade.code.dto.ConfirmNoInfoDTO;
import com.bmos.platform.facade.code.dto.ReleaseConfirmedNoDTO;
import com.bmos.platform.facade.code.vo.BatchCodeNoVO;
import com.bmos.platform.service.system.code.convert.CodeRuleConverter;
import com.bmos.platform.service.system.code.convert.CodeRuleUseConverter;
import com.bmos.platform.service.system.code.dto.*;
import com.bmos.platform.service.system.code.mapper.CodeRuleDeptMapper;
import com.bmos.platform.service.system.code.mapper.CodeRuleMapper;
import com.bmos.platform.service.system.code.mapper.CodeRuleUseMapper;
import com.bmos.platform.service.system.code.model.CodeRuleUse;
import com.bmos.platform.service.system.code.model.CodeRuleVersion;
import com.bmos.platform.service.system.code.service.CodeRuleService;
import com.bmos.platform.service.system.code.service.CodeRuleVersionService;
import com.bmos.platform.service.system.code.vo.*;
import com.bmos.platform.service.system.code.vo.supplierno.CodeRuleNo;
import com.bmos.platform.service.system.code.vo.supplierno.CodeRuleUseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CodeRuleServiceImpl implements CodeRuleService {
    @Autowired
    private CodeRuleMapper codeRuleMapper;
    @Autowired
    private CodeRuleVersionService codeRuleVersionService;
    @Autowired
    private CodeRuleUseMapper codeRuleUseMapper;
    @Autowired
    private CodeRuleDeptMapper codeRuleDeptMapper;
    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    private final String CODE_APPLY_TIME_FIELDS = "codeApplyTime";
    @Override
    public List<CodeRulePageVO> page(CodeRulePageDTO dto) {
        /*String userId = SysUserHolder.getUser().getUserId();
        if (!AdminUtil.isAdminUser(userId)) {
            dto.setDeptIds(platformApiAdaptor.getMineDeptIds());
        }*/
        return codeRuleMapper.page(dto);
    }

    @Override
    public List<CodeRulePageVO> list(CodeRuleListDTO dto) {
        return codeRuleMapper.list(dto);
    }

    @Override
    public DetailCodeRuleVersionDetailVO detail(Long versionId) {
        DetailCodeRuleVersionDetailVO detail = codeRuleMapper.detail(versionId);
        detail.setDetails(codeRuleVersionService.detail(versionId, detail.getDictId()));
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void permission(CodeRulePermissionDTO dto) {
        codeRuleDeptMapper.deletByCodeRuleId(dto.getCodeRuleId());
        codeRuleDeptMapper.insertBatch(CodeRuleConverter.INSTANCE.convertList(dto));
    }

    @Override
    public List<Long> permissionDetail(Long id) {
        return codeRuleDeptMapper.selectByCodeRuleId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CodeRuleSaveDTO dto) {
        Assert.isFalse(
            codeRuleMapper.existsCode(dto.getCode()),
            () -> new BmosException(PlatformResponseCode.CODE_RULE_CODE_DUPLICATE)
        );
        codeRuleMapper.insert(CodeRuleConverter.INSTANCE.convertDO(dto));
        codeRuleVersionService.save(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CodeRuleUpdateDTO dto) {
        codeRuleMapper.updateById(CodeRuleConverter.INSTANCE.convertDO(dto));
        codeRuleVersionService.update(dto);
    }

    @Override
    public void confirmNo(ConfirmNoInfoDTO dto) {
        CodeRuleUseVO codeRuleUseVO = getAndValidated(dto.getCode(), dto.getFields());
        codeRuleUseVO.setCodeApplyTime(dto.getCodeApplyTime());
        codeRuleUseVO.setResetFiledValue();
        List<CodeRuleUse> codeRuleUses = codeRuleUseMapper.selectByCondition(dto.getCode(), codeRuleUseVO.getResetFiledValue(), dto.getFullNo());
        if (CollUtil.isEmpty(codeRuleUses)) {
            codeRuleUseMapper.insert(CodeRuleUseConverter.INSTANCE.convertDO2(codeRuleUseVO, dto.getFullNo()));
            return;
        }
        // 更新确认状态
        codeRuleUseMapper.batchConfirm(CollectionUtils.convertList(codeRuleUses, CodeRuleUse::getId));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.code")
    public void batchConfirmNo(BatchConfirmNextUseCodeDTO dto) {
        CodeRuleUseVO codeRuleUseVO = getAndValidated(dto.getCode(), dto.getFields());
        codeRuleUseVO.setCodeApplyTime(dto.getCodeApplyTime());
        List<CodeRuleUse> codeRuleUses = codeRuleUseMapper
            .selectListByCondition(dto.getCode(), codeRuleUseVO.getResetFiledValue(), dto.getFullNos());
        // 更新确认状态
        codeRuleUseMapper.batchConfirm(CollectionUtils.convertList(codeRuleUses, CodeRuleUse::getId));
        Set<String> existsFullNos = codeRuleUses.stream().map(CodeRuleUse::getFullNo).collect(Collectors.toSet());
        List<String> needSaveFullNos = dto.getFullNos().stream()
            .filter(fullNo -> !existsFullNos.contains(fullNo))
            .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needSaveFullNos)) {
            codeRuleUseMapper.insertBatch(CodeRuleUseConverter.INSTANCE.convertList2(codeRuleUseVO, needSaveFullNos));
        }

    }

    @Override
    public void releaseConfirmedNO(ReleaseConfirmedNoDTO dto) {
        CodeRuleUseVO codeRuleUseVO = getAndValidated(dto.getCode(), dto.getFields());
        List<CodeRuleUse> codeRuleUses = codeRuleUseMapper.selectByCondition(dto.getCode(),
                codeRuleUseVO.getResetFiledValue(), dto.getNo());
        if (CollUtil.isNotEmpty(codeRuleUses)) {
            codeRuleUseMapper.releaseByIds(CollectionUtils.convertList(codeRuleUses, CodeRuleUse::getId));
        }
    }

    @Override
    public void batchConfirmByIdList(List<Long> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        codeRuleUseMapper.batchConfirm(list);
    }

    @Override
    public void batchConfirmByCodeList(BatchConfirmByCodeDTO dto) {
        List<ConfirmNoInfoDTO> list = dto.getList();
        if (CollUtil.isEmpty(list)) {
            return;
        }
        list.forEach(this::confirmNo);
    }

    @Override
    public BatchCodeNoVO batchGetSameTypeNo(BatchCodeNoDTO dto) {
        BatchCodeNoVO result = new BatchCodeNoVO();
        CodeRuleVersion enableVersion = getEnableVersionNoError(dto.getCode());
        if (enableVersion == null) {
            result.setRuleNotExist(true);
            return result;
        }
        result.setRuleNotExist(false);
        List<CodeRuleUseDetailVO> detail = codeRuleVersionService.listDetail(enableVersion.getId());
        AtomicInteger count = new AtomicInteger();
        LinkedHashMap<String, List<CodeRuleUseVO>> resetMap = dto.getFields().stream().map(e -> {
            String timeStr = e.get(CODE_APPLY_TIME_FIELDS);
            LocalDate applyTime = StrUtil.isEmpty(timeStr) ? null : LocalDateTimeUtil.parseDate(timeStr);
            CodeRuleUseVO build = CodeRuleUseVO.builder()
                    .resetRule(JsonUtils.parseArray(JsonUtils.toJsonString(enableVersion.getResetRule()), Long.class))
                    .code(dto.getCode())
                    .map(e)
                    .sort(count.getAndIncrement())
                    .details(detail)
                    .codeApplyTime(applyTime)
                    .build();
            build.validatedParameter();
            return build;
        }).collect(Collectors.groupingBy(CodeRuleUseVO::getResetFiledValue, LinkedHashMap::new, Collectors.toList()));
        List<String> nos = new ArrayList<>();
        result.setCode(dto.getCode());
        result.setNos(nos);
        List<BatchNextUseCodeElementVO> vos = new ArrayList<>();
        for (Map.Entry<String, List<CodeRuleUseVO>> entry : resetMap.entrySet()) {
            String resetValue = entry.getKey();
            CodeRuleUseVO codeRuleUseVO = CollUtil.getFirst(entry.getValue());
            codeRuleUseVO.setCodeRuleNo(getCodeRuleNo(dto.getCode(), resetValue));

            List<BatchNextUseCodeElementVO> batchNextUseCodeElementVOS = codeRuleUseVO.batchGenerateCodes(entry.getValue());
            for (int i = 0; i < entry.getValue().size(); i++) {
                batchNextUseCodeElementVOS.get(i).setSort(entry.getValue().get(i).getSort());
            }
            vos.addAll(batchNextUseCodeElementVOS);
            saveNoToDatabase(codeRuleUseVO, batchNextUseCodeElementVOS);
        }
        vos.sort(Comparator.comparing(BatchNextUseCodeElementVO::getSort));
        vos.forEach(e->nos.add(e.getFullNo()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NextCodeVO getNextNo(NextUseCodeDTO dto) {
        CodeRuleUseVO codeRuleUseVO = getAndValidated(dto.getCode(), dto.getFields());
        codeRuleUseVO.getNextNo();
        saveNoToDatabase(codeRuleUseVO);
        return codeRuleUseVO.convertVO();
    }

    @Override
    public NextCodeVO getNextUseNo(NextUseCodeDTO dto) {
        CodeRuleUseVO codeRuleUseVO = getAndValidated(dto.getCode(), dto.getFields());
        codeRuleUseVO.getNextUseNo();
        // 保存下一个编码至数据库
        saveNoToDatabase(codeRuleUseVO);
        return codeRuleUseVO.convertVO();
    }

    public BatchNextCodeVO getBatchNextUseNo(BatchNextUseCodeDTO dto) {
        CodeRuleUseVO codeRuleUseVO = getAndValidated(dto.getCode(), dto.getFields());
        List<BatchNextUseCodeElementVO> elementVOS = codeRuleUseVO.batchGetNextUseNo(dto.getNum());
        // 保存下一个编码至数据库
        List<CodeRuleUse> codeRuleUses = saveNoToDatabase(codeRuleUseVO, elementVOS);
        return new BatchNextCodeVO(
            dto.getCode(),
            codeRuleUses.stream().map(e->{
                BatchNextCodeVO.NextCodeVO nextCodeVO = new BatchNextCodeVO.NextCodeVO();
                nextCodeVO.setId(e.getId());
                nextCodeVO.setNo(e.getFullNo());
                return nextCodeVO;
            }).collect(Collectors.toList()),
            codeRuleUseVO.getCodeApplyTime()
        );
    }

    private void saveNoToDatabase(CodeRuleUseVO codeRuleUseVO) {
        if (codeRuleUseVO.isExistsDatabase()) {
            List<CodeRuleUse> list = codeRuleUseMapper.selectByCondition(codeRuleUseVO.getCode(), codeRuleUseVO.getResetFiledValue(), codeRuleUseVO.getFullNo());
            codeRuleUseVO.setId(CollUtil.isNotEmpty(list) ? CollUtil.getFirst(list).getId() : null);
        }
        CodeRuleUse codeRuleUse = CodeRuleUseConverter.INSTANCE.convertDO(codeRuleUseVO);
        codeRuleUseMapper.insert(codeRuleUse);
        codeRuleUse.setId(codeRuleUse.getId());
    }

    private List<CodeRuleUse> saveNoToDatabase(CodeRuleUseVO codeRuleUseVO, List<BatchNextUseCodeElementVO> elementVOS) {
        List<BatchNextUseCodeElementVO> notExistsDatabase = elementVOS.stream()
            .filter(elementVO -> !elementVO.getIsExistsDatabase())
            .collect(Collectors.toList());
        elementVOS.removeAll(notExistsDatabase);
        List<CodeRuleUse> existsDb = new ArrayList<>();
        if (CollUtil.isNotEmpty(elementVOS)) {
            existsDb = codeRuleUseMapper.selectListByCondition(codeRuleUseVO.getCode(),
                    codeRuleUseVO.getResetFiledValue(), CollectionUtils.convertList(elementVOS,
                            BatchNextUseCodeElementVO::getFullNo));
        }
        if (CollUtil.isEmpty(notExistsDatabase)) {
            existsDb.sort(Comparator.comparing(CodeRuleUse::getSequence));
            return existsDb;
        }
        List<CodeRuleUse> codeRuleUses = CodeRuleUseConverter.INSTANCE.convertList(codeRuleUseVO, notExistsDatabase);
        codeRuleUseMapper.insertBatch(codeRuleUses);
        existsDb.addAll(codeRuleUses);
        existsDb.sort(Comparator.comparing(CodeRuleUse::getSequence));
        return existsDb;
    }

    private CodeRuleUseVO getAndValidated(String code, Map<String, String> map) {
        CodeRuleUseVO codeRuleUseVO = buildAndValidated(code, map);
        codeRuleUseVO.setCodeRuleNo(getCodeRuleNo(code, codeRuleUseVO.getResetFiledValue()));
        return codeRuleUseVO;
    }

    private CodeRuleUseVO buildAndValidated(String code, Map<String, String> map) {
        CodeRuleVersion enableVersion = getEnableVersion(code);
        // 获取版本的规则数据
        List<CodeRuleUseDetailVO> codeRuleUseDetailVOS = codeRuleVersionService.listDetail(enableVersion.getId());
        CodeRuleUseVO result = CodeRuleUseVO.builder()
            .resetRule(JsonUtils.parseArray(JsonUtils.toJsonString(enableVersion.getResetRule()), Long.class))
            .code(code)
            .details(codeRuleUseDetailVOS)
            .map(map)
            .build();
        result.validatedParameter();
        return result;
    }

    private CodeRuleVersion getEnableVersion(String code) {
        CodeRuleVersion enableVersion = codeRuleVersionService.getEnableVersion(code);
        Assert.isTrue(
            Objects.nonNull(enableVersion),
            () -> new BmosException(PlatformResponseCode.CODE_RULE_NOT_EXISTS)
        );
        return enableVersion;
    }

    private CodeRuleVersion getEnableVersionNoError(String code) {
        return codeRuleVersionService.getEnableVersion(code);
    }

    private CodeRuleNo getCodeRuleNo(String code, String resetFiledValue) {
        Map<String, Long> collect = codeRuleUseMapper.selectMaxSeqByResetNo(code, resetFiledValue)
            .stream()
            .collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
        return CodeRuleNo.builder()
            .skipNos(codeRuleUseMapper.selectSkipSeqByResetNo(code, resetFiledValue))
            .waitConfirmNos(codeRuleUseMapper.selectWaitConfirmNos(code, resetFiledValue))
            .waitConfirmNo(collect.get(BooleanEnum.FALSE.getValue()))
            .confirmNo(collect.get(BooleanEnum.TRUE.getValue()))
            .build();
    }
}
