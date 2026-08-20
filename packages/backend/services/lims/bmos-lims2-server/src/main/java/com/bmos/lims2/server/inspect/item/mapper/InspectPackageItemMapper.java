package com.bmos.lims2.server.inspect.item.mapper;

import com.bmos.lims2.server.inspect.item.entity.InspectPackageItem;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 实验包与检验项关联表(BmExperimentPackageInspect)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-02 13:22:42
 */
@Mapper
public interface InspectPackageItemMapper extends BaseMapperX<InspectPackageItem> {


    /**
     * 校验检验项目当前是否有挂在在某个实验包下
     *
     * @param itemId
     * @return
     */
    default boolean existByInspectId(Long itemId) {
        return exists(new LambdaQueryWrapperX<InspectPackageItem>()
                .eq(InspectPackageItem::getInspectItemId, itemId));
    }

    /**
     * 删除实验包下的检验项目
     *
     * @param packageId
     */
    default void deleteByPackageId(Long packageId) {
        delete(new LambdaQueryWrapperX<InspectPackageItem>()
                .eq(InspectPackageItem::getInspectPackageId, packageId));
    }

    /**
     * 根据实验包id查询当前实验包与检验项的绑定关系
     *
     * @param packageId
     * @return
     */
    default List<InspectPackageItem> selectByPackageId(Long packageId) {
        return selectList(new LambdaQueryWrapperX<InspectPackageItem>()
                .eq(InspectPackageItem::getInspectPackageId, packageId));
    }

}

