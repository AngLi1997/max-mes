package com.bmos.mes.service.weigh.centre.config.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentrePageQuery;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:35
 */
@Mapper
public interface IWeighCentreMapper extends BaseMapperX<WeighCentre> {

    default List<WeighCentre> listByCode(String code){
        if (StringUtils.isBlank(code)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighCentre>()
                .eq(WeighCentre::getCode, code)
        );
    }

    List<WeighCentre> queryPage(@Param("pageQuery") WeighCentrePageQuery pageQuery,
                                @Param("weighCentreIds") List<Long> weighCentreCategoryIds,
                                @Param("deptIds") List<Long> deptIds
    );

    List<WeighCentre> listAllByDeptIds(@Param("deptIds") List<Long> deptIds);


    default List<WeighCentre> listByCategoryId(Long weighCentreCategoryId){
        return selectList(new LambdaQueryWrapper<WeighCentre>()
                .eq(WeighCentre::getCategoryId, weighCentreCategoryId)
        );
    }

    /**
     * 根据称量中心id查询称量中心名称路径（带分类）
     * @param weighCentreId 称量中心id
     * @return
     */
    String selectNamePathWithCategoryById(@Param("weighCentreId") Long weighCentreId);
}
