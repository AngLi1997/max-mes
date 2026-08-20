package com.bmos.platform.service.system.code.vo.supplierno;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.expression.pojo.KeyValue;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.common.enums.system.code.RuleTypeEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.system.code.vo.BatchNextUseCodeElementVO;
import com.bmos.platform.service.system.code.vo.CodeRuleUseDetailVO;
import com.bmos.platform.service.system.code.vo.NextCodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 生成下一个编号的VO类
 */
@Getter
@Setter
@Builder
@ApiModel("CodeRuleUseVO:业务使用")
public class CodeRuleUseVO {
    @ApiModelProperty("规则编码")
    private String code;
    @ApiModelProperty("重置字段数据")
    private List<Long> resetRule;
    @ApiModelProperty("存储重置字段数据Set形式")
    private Set<Long> resetRuleSet;
    @ApiModelProperty("规则详情")
    private List<CodeRuleUseDetailVO> details;
    @ApiModelProperty("编码规则详情传参")
    private Map<String, String> map;
    @ApiModelProperty("重置字段值")
    private String resetFiledValue;
    @ApiModelProperty("规则配置开始流水号")
    private Long startNo;
    @ApiModelProperty("数据库已存储需使用的序列号")
    private CodeRuleNo codeRuleNo;
    @ApiModelProperty("是否为数据库已存在字段")
    private boolean existsDatabase;
    @ApiModelProperty("重置日期字符串")
    private String resetDateStr;
    @ApiModelProperty("重置日期日期格式")
    private String resetDateFormat;
    @ApiModelProperty("序列号")
    private Long sequence;
    @ApiModelProperty("序列号")
    private String fullNo;
    @ApiModelProperty("编码申请时间")
    private LocalDate codeApplyTime;

    @ApiModelProperty("编号id")
    private Long id;

    @ApiModelProperty("排序")
    private int sort;

    public LocalDate getResetDate() {
        return LocalDateTimeUtil.parseDate(resetDateStr, resetDateFormat);
    }

    public Set<Long> getResetFiled() {
        if (Objects.isNull(resetRuleSet)) {
            if (Objects.isNull(resetRule)) {
                resetRuleSet = new HashSet<>();
            } else {
                resetRuleSet = new HashSet<>(resetRule);
            }
        }
        return resetRuleSet;
    }

    public String getResetFiledValue() {
        if (StrUtil.isNotEmpty(resetFiledValue)) {
            return resetFiledValue;
        }
        setResetFiledValue();
        return resetFiledValue;
    }

    public void setResetFiledValue() {
        resetFiledValue = getResetData().stream()
            .map(this::calculateCodeRule)
            .collect(Collectors.joining(StrPool.COMMA));
    }

    /**
     * 添加是否展示判断
     * @param detailVO
     * @return
     */
    private String calculateCodeRuleAndIsShow(CodeRuleUseDetailVO detailVO) {
        try {
            if (RuleTypeEnum.PARAMETER == detailVO.getType() && detailVO.getIsShow()) {
                return map.get(detailVO.getParameterNo());
            }
            if (RuleTypeEnum.DATE == detailVO.getType()) {
                if (Objects.isNull(codeApplyTime)) {
                    codeApplyTime = LocalDate.now();
                }
                if (getResetFiled().contains(detailVO.getSort()) && StrUtil.isEmpty(resetDateStr)) {
                    resetDateFormat = detailVO.getDateFormat();

                    resetDateStr = LocalDateTimeUtil.format(codeApplyTime, resetDateFormat);
                }
                return LocalDateTimeUtil.format(codeApplyTime, detailVO.getDateFormat());
            }
            if (RuleTypeEnum.CONSTANT == detailVO.getType()) {
                return detailVO.getValue();
            }
            if (RuleTypeEnum.SEQUENCE == detailVO.getType()) {
                CodeRuleUseDetailVO sequenceRule = CollectionUtils.getFirst(getDetailsByType(RuleTypeEnum.SEQUENCE));
                return BooleanEnum.TRUE == sequenceRule.getFillZero()
                        ? String.format("%0" + sequenceRule.getMaxLength() + "d", getSequence()) : String.valueOf(getSequence());
            }
        }catch (Exception e) {
            throw new BmosException(PlatformResponseCode.RULE_TYPE_NOT_EXISTS);
        }
        return "";
    }
    /**
     * 根据规则类型获取对应值，不包括序列号的规则
     *
     * @param detailVO detailVO
     * @return String
     */
    private String calculateCodeRule(CodeRuleUseDetailVO detailVO) {
        try {
            if (RuleTypeEnum.PARAMETER == detailVO.getType()) {
                return map.get(detailVO.getParameterNo());
            }
            if (RuleTypeEnum.DATE == detailVO.getType()) {
                if (Objects.isNull(codeApplyTime)) {
                    codeApplyTime = LocalDate.now();
                }
                if (getResetFiled().contains(detailVO.getSort()) && StrUtil.isEmpty(resetDateStr)) {
                    resetDateFormat = detailVO.getDateFormat();
                    resetDateStr = LocalDateTimeUtil.format(codeApplyTime, resetDateFormat);
                }
                return LocalDateTimeUtil.format(codeApplyTime, detailVO.getDateFormat());
            }
            if (RuleTypeEnum.CONSTANT == detailVO.getType()) {
                return detailVO.getValue();
            }
            if (RuleTypeEnum.SEQUENCE == detailVO.getType()) {
                CodeRuleUseDetailVO sequenceRule = CollectionUtils.getFirst(getDetailsByType(RuleTypeEnum.SEQUENCE));
                return BooleanEnum.TRUE == sequenceRule.getFillZero()
                        ? String.format("%0" + sequenceRule.getMaxLength() + "d", getSequence()) : String.valueOf(getSequence());
            }
        }catch (Exception e) {
            throw new BmosException(PlatformResponseCode.RULE_TYPE_NOT_EXISTS);
        }
        return "";
    }

    private void initStartNo() {
        CodeRuleUseDetailVO first = CollectionUtils.getFirst(getDetailsByType(RuleTypeEnum.SEQUENCE));
        Assert.isTrue(Objects.nonNull(first),
            () -> new BmosException(PlatformResponseCode.SEQUENCE_RULE_NOT_EXISTS)
        );
        startNo = first.getStartNo();
    }

    /**
     * 校验Map中数据是否覆盖details中参数类型的数据 否则抛出异常
     */
    public void validatedParameter() {
        boolean parameterExists = getDetailsByType(RuleTypeEnum.PARAMETER)
            .stream()
            .allMatch(vo -> StrUtil.isNotEmpty(map.get(vo.getParameterNo())));
        if (!parameterExists) {
            throw new BmosException(PlatformResponseCode.PARAMETER_NOT_FULL);
        }
    }

    /**
     * 获取指定规则类型的规则列表
     *
     * @param type 规则类型
     * @return List<CodeRuleVersionDetail>
     */
    public List<CodeRuleUseDetailVO> getDetailsByType(RuleTypeEnum type) {
        return CollectionUtils.filterList(details, detail -> type == detail.getType());
    }

    /**
     * 获取不包含指定规则类型的规则列表
     *
     * @param type 规则类型
     * @return List<CodeRuleVersionDetail>
     */
    public List<CodeRuleUseDetailVO> getDetailsWithoutType(RuleTypeEnum type) {
        return CollectionUtils.filterList(details, detail -> type != detail.getType());
    }

    public List<CodeRuleUseDetailVO> getResetData() {
        Set<Long> resetFiled = getResetFiled();
        return CollectionUtils.filterList(details, detail -> resetFiled.contains(detail.getSort()));
    }

    /**
     * 获取下一个编号 未确认使用的包含
     * 此时，数据库中(未确认编号序列号(仅有一个，目前情况下) > 已确认编号序列号)
     *
     * @return String
     */
    public String getNextUseNo() {
        initStartNo();
        // 序列号取已确认最大值
        sequence = getCodeRuleNo().getConfirmNo();
        setNextSequence();
        // 判断序列号是否在未确认中
        setIsExistsDatabase();
        return getFullNo();
    }

    public List<BatchNextUseCodeElementVO> batchGetNextUseNo(int num) {
        initStartNo();
        // 序列号取已确认最大值
        sequence = getCodeRuleNo().getConfirmNo();
        List<BatchNextUseCodeElementVO> result = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            setNextSequence();
            setIsExistsDatabase();
            result.add(getElementData());
        }
        return result;
    }

    private BatchNextUseCodeElementVO getElementData() {
        return BatchNextUseCodeElementVO.builder()
            .isExistsDatabase(isExistsDatabase())
            .sequence(getSequence())
            .fullNo(getFullNo())
            .build();
    }

    /**
     * 获取下一个编号 未确认使用的不包含
     * 数据库中(未确认编号序列号 与 已确认编号序列号 大小关系不确定)
     * 此方法产生的编号 一定不重复
     *
     * @return String
     */
    public String getNextNo() {
        initStartNo();
        // 序列号取未确认编号与已确认标号中较大的值
        sequence = getCodeRuleNo().getMaxNoFromConfirmNoAndWaitConfirmNo();
        setNextSequence();
        return getFullNo();
    }

    private void setNextSequence() {
        CodeRuleUseDetailVO sequenceRule = CollectionUtils.getFirst(getDetailsByType(RuleTypeEnum.SEQUENCE));
        if (Objects.isNull(sequence)) {
            sequence = startNo;
        } else {
            sequence = sequence + sequenceRule.getStep();
        }
        // 序列号需跳过已存在需跳过的数据
        while (getCodeRuleNo().getSkipNos().contains(setFullNo())) {
            sequence = sequence + sequenceRule.getStep();
        }
    }

    private String setFullNo() {
        StringBuilder value = new StringBuilder();
        for (CodeRuleUseDetailVO detail : getDetails()) {
            String codeRule = calculateCodeRuleAndIsShow(detail);
            if (StrUtil.isNotBlank(codeRule)){
                value.append(codeRule);
            }
        }
        fullNo = value.toString();
        if (StrUtil.endWith(fullNo, StrUtil.DASHED)){
            fullNo = value.substring(0, value.length() - 1);
        }
        return fullNo;
    }

    public List<BatchNextUseCodeElementVO> batchGenerateCodes(List<CodeRuleUseVO> list) {
        // 初始化 startNo
        initStartNo();
        // 序列号取已确认最大值
        sequence = getCodeRuleNo().getConfirmNo();
        // 结果列表
        List<BatchNextUseCodeElementVO> result = new ArrayList<>();

        for (CodeRuleUseVO codeRuleUseVO : list) {
            // 设置当前的 map
            this.map = codeRuleUseVO.getMap();
            this.codeApplyTime = codeRuleUseVO.getCodeApplyTime();
            // 重新生成编码
            setNextSequence();
            // 判断是否在未确认中
            setIsExistsDatabase();
            BatchNextUseCodeElementVO elementData = getElementData();
            elementData.setSort(codeRuleUseVO.getSort());
            // 生成并存储当前结果
            result.add(elementData);
        }

        return result;
    }


    private void setIsExistsDatabase() {
        Long waitConfirmNo = getCodeRuleNo().getWaitConfirmNo();
        if (Objects.isNull(waitConfirmNo)) {
            waitConfirmNo = Long.MIN_VALUE;
        }
        existsDatabase = sequence <= waitConfirmNo && getCodeRuleNo().getWaitConfirmNos().contains(getFullNo());
    }

    public NextCodeVO convertVO() {
        return new NextCodeVO(getCode(), getFullNo(), getCodeApplyTime(), id);
    }
}
