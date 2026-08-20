package com.bmos.platform.service.system.code.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.code.dto.CodeRuleListDTO;
import com.bmos.platform.service.system.code.dto.CodeRulePageDTO;
import com.bmos.platform.service.system.code.model.CodeRule;
import com.bmos.platform.service.system.code.vo.CodeRulePageVO;
import com.bmos.platform.service.system.code.vo.DetailCodeRuleVersionDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CodeRuleMapper extends BaseMapperX<CodeRule> {
    /**
     * 分页查询
     * @param dto dto
     * @return List<CodeRulePageVO>
     */
    List<CodeRulePageVO> page(CodeRulePageDTO dto);

    /**
     * 分页查询
     * @param dto dto
     * @return List<CodeRulePageVO>
     */
    List<CodeRulePageVO> list(CodeRuleListDTO dto);

    /**
     * 详情
     * @param versionId id
     * @return DetailCodeRuleVersionDetailVO
     */
    DetailCodeRuleVersionDetailVO detail(@Param("versionId") Long versionId);

    default boolean existsCode(String code) {
        return exists(new LambdaQueryWrapperX<CodeRule>()
            .eq(CodeRule::getCode, code)
            .last(" limit 1 ")
        );
    }

    default boolean existsParameterId(Long dictId) {
       /* return exists(new LambdaQueryWrapperX<CodeRule>()
            .eq(CodeRule::getDictId, dictId)
            .last(" limit 1 ")
        );*/
        return Boolean.TRUE;
    }
}
