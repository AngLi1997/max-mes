package com.bmos.platform.service.system.dept.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.tree.CommonTreeVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.dept.dto.DeptRelateUserQueryDTO;
import com.bmos.platform.service.system.dept.model.Dept;
import com.bmos.platform.service.system.dept.vo.DeptAssignUserVO;
import com.bmos.platform.facade.system.dept.vo.DeptTreeUserVO;
import com.bmos.platform.facade.system.dept.vo.DeptTreeVO;
import com.bmos.platform.service.system.dept.vo.DeptUnAssignUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface DeptMapper extends BaseMapperX<Dept> {

    List<DeptTreeVO> selectAllDept();

    List<Long> validateDept(@Param("deptName") String deptName, @Param("id") Long id);

    List<DeptAssignUserVO> selectAssignList(DeptRelateUserQueryDTO dto);

    List<DeptUnAssignUserVO> selectUnAssignList(@Param("deptId") Long deptId, @Param("name") String name);

    List<Long> getDeptList(@Param("userId") String userId);

    List<DeptTreeUserVO> unAssigned(@Param("name") String name);

    Long getCount(@Param("status") Long status);

    List<DeptTreeUserVO> assigned(@Param("name") String name, @Param("deptId") Long deptId);


    Long selectMaxId();

    default List<Dept> selectListByDeptCode(String parentDeptCode) {
        return selectList(new LambdaQueryWrapperX<Dept>()
                .likeRight(StrUtil.isNotEmpty(parentDeptCode), Dept::getCode, parentDeptCode));
    }

    default boolean existsChildDept(Long id){
        return exists(new LambdaQueryWrapperX<Dept>().eq(Dept::getParentId,id).last("limit 1"));
    }

    List<Dept> selectListByUserIds(@Param("userIds") Set<String> userIds);

    List<CommonTreeVO> selectDeptTree(@Param("userId") String userId);

    List<CommonTreeVO> selectDeptCommonTree();

    List<Dept> selectListByUserId(@Param("userId") String userId);
}
