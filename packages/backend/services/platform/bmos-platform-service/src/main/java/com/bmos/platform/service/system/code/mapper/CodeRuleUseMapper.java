package com.bmos.platform.service.system.code.mapper;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.expression.pojo.KeyValue;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.service.system.code.model.CodeRuleUse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CodeRuleUseMapper extends BaseMapperX<CodeRuleUse> {
    List<KeyValue<String, Long>> selectMaxSeqByResetNo(@Param("code") String code, @Param("resetNo") String resetNo);

    List<String> selectSkipSeqByResetNo(@Param("code") String code, @Param("resetNo") String resetNo);

    Set<String> selectWaitConfirmNos(@Param("code") String code, @Param("resetNo") String resetNo);

    default List<CodeRuleUse> selectByCondition(String code, String resetFiledValue, String fullNo) {
        return selectList(new LambdaQueryWrapperX<CodeRuleUse>()
            .eq(CodeRuleUse::getCode, code)
            .eq(CodeRuleUse::getResetNo, resetFiledValue)
            .eq(CodeRuleUse::getFullNo, fullNo)
        );
    }

    default List<CodeRuleUse> selectListByCondition(String code, String resetFiledValue, List<String> fullNos) {
        return selectList(new LambdaQueryWrapperX<CodeRuleUse>()
            .eq(CodeRuleUse::getCode, code)
            .eq(CodeRuleUse::getResetNo, resetFiledValue)
            .in(CodeRuleUse::getFullNo, fullNos)
        );
    }

    default void confirm(Long id) {
        updateById(CodeRuleUse.builder().id(id).confirm(BooleanEnum.TRUE).build());
    }

    default void batchConfirm(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        update(CodeRuleUse.builder().confirm(BooleanEnum.TRUE).build(),
            new LambdaQueryWrapperX<CodeRuleUse>().in(CodeRuleUse::getId, ids));
    }

    default void releaseById(Long id) {
        update(null, new LambdaUpdateWrapper<CodeRuleUse>()
                .eq(CodeRuleUse::getId, id)
                .set(CodeRuleUse::getSkip, BooleanEnum.FALSE)
                .set(CodeRuleUse::getConfirm, BooleanEnum.FALSE));
    }

    /**
     * 释放时只释放自动生成的编号
     * 手动输入的编号skip为true不释放
     * @param ids
     */
    default void releaseByIds(List<Long> ids){
        update(null, new LambdaUpdateWrapper<CodeRuleUse>()
                .in(CodeRuleUse::getId, ids)
                .eq(CodeRuleUse::getSkip, BooleanEnum.FALSE)
                .set(CodeRuleUse::getConfirm, BooleanEnum.FALSE));
    }
}
