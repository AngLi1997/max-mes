package com.bmos.lims2.server.operate.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.lims2.server.operate.dto.version.OperateVersionAuditDTO;
import com.bmos.lims2.server.operate.dto.version.VersionPageDTO;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.vo.OperateRuleAuditVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionDetailsVO;
import com.bmos.lims2.server.operate.vo.OperateRuleVersionPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author renjinguang
 */
@Mapper
public interface OperateRuleVersionMapper extends BaseMapperX<OperateRuleVersion> {


    List<OperateRuleVersionPageVO> getListByParentId(VersionPageDTO dto);

    default List<OperateRuleVersion> getListByParentIdAndState(List<Long> parentId, String code) {
        return selectList(new LambdaQueryWrapperX<OperateRuleVersion>()
                .in(OperateRuleVersion::getOperateId, parentId)
                .eq(OperateRuleVersion::getState, code));
    }

    OperateRuleVersionDetailsVO getDetailsById(@Param("id") Long id);

    default List<OperateRuleVersion> getVersionListByOperateId(Long operateId) {
        return selectList(new LambdaQueryWrapperX<OperateRuleVersion>()
                .eq(OperateRuleVersion::getOperateId, operateId));
    }

    List<OperateRuleAuditVO> pageTodoFlow(@Param("dto") OperateVersionAuditDTO dto);

    default List<OperateRuleVersion> getWaitValidVersionByDateAndState(String date, String code) {
        return selectList(new LambdaQueryWrapperX<OperateRuleVersion>()
                .eq(OperateRuleVersion::getState, code)
                .eq(OperateRuleVersion::getEffectDate, date));
    }

    default void updateOperateRuleVersion(List<OperateRuleVersion> list){
        Db.saveOrUpdateBatch(list);
    }

    List<OperateRule> selectByListByState(@Param("code") String code, @Param("deptIds") List<Long> deptIds);

    OperateRuleVersion selectStateByRuleId(@Param("ruleId") Long versionId);

    /**
     * 按版本ID批量查询操作规程名称、编码、版本号（用于构建检验依据字符串）
     */
    List<OperateRuleVersionDetailsVO> selectBriefByIds(@Param("ids") List<Long> ids);
}
