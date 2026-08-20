package com.bmos.mes.service.operate.mapper;

import com.bmos.mes.service.operate.dto.OperateRulePageDTO;
import com.bmos.mes.service.operate.model.OperateRule;
import com.bmos.mes.service.operate.vo.OperateRulePageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author renjinguang
 */
@Mapper
public interface OperateRuleMapper extends BaseMapperX<OperateRule> {


    default List<OperateRule> getListByCategoryId(Long id) {
        return selectList(new LambdaQueryWrapperX<OperateRule>()
                .eq(OperateRule::getCategoryId, id));
    }

    List<OperateRulePageVO> getRecordPage(OperateRulePageDTO dto);

    default String selectNameById(Long operateId){
        return selectById(operateId).getName();
    }

    List<String> getAuditBusinessKey(@Param("deptIdList") List<Long> deptIdList);

    List<OperateRule> queryListByIdsAndDeptIds(@Param("ruleIds") List<Long> ruleId,
                                               @Param("state") String state,
                                               @Param("deptIds") List<Long> deptIds);
}
