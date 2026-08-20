package com.bmos.mes.service.storage.manage.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.service.record.business.model.StorageMaterialDetailInfo;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialListQuery;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialManagePageQuery;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialPageQuery;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.vo.BatchReservedAvailableMaterialVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 16:06
 */
@Mapper
public interface IStorageMaterialMapper extends BaseMapperX<StorageMaterial> {

    default List<StorageMaterial> selectByBatchIdAndMaterialNo(Long batchNoId, String no) {
        return selectList(new LambdaQueryWrapperX<StorageMaterial>()
                .eq(StorageMaterial::getStorageMaterialBatchId, batchNoId)
                .likeIfPresent(StorageMaterial::getNo, no));
    }

    List<StorageMaterialVO> queryList(@Param("query") StorageMaterialListQuery query, @Param("positionIds") List<Long> positionIds);

    List<StorageMaterialVO> queryPageWithPosition(@Param("query") StorageMaterialPageQuery query, @Param("positionIds") List<Long> positionIds);

    List<StorageMaterialManageVO> queryPage(@Param("pageQuery") StorageMaterialManagePageQuery pageQuery);

    StorageMaterialVO queryInfoById(@Param("id") Long id);

    List<StorageMaterialVO> queryInfoByIds(@Param("ids") List<Long> ids);

    Long queryIdByMaterialNo(@Param("materialNo") String materialNo);

    default List<StorageMaterial> queryListByPositionId(Long materialPositionId) {
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .eq(materialPositionId != null, StorageMaterial::getMaterialPositionId, materialPositionId)
                .and(wrapper -> wrapper.ne(StorageMaterial::getAvailableQuantity, 0)
                        .or()
                        .ne(StorageMaterial::getReserveQuantity, 0))
        );
    }

    /**
     * 根据物料号查询物料
     *
     * @param materialNo        物料件编号
     * @param availableValidate 是否校验可用性 true 校验 false 不校验
     * @return
     */
    default StorageMaterial queryByMaterialNo(String materialNo, Boolean availableValidate) {
        if (StrUtil.isBlank(materialNo)) {
            return null;
        }
        StorageMaterial storageMaterial = selectOne(Wrappers.lambdaQuery(StorageMaterial.class)
                .eq(StorageMaterial::getNo, materialNo)
        );
        if (storageMaterial == null) {
            return null;
        }
        if (availableValidate == null || !availableValidate) {
            return storageMaterial;
        }
        return storageMaterial.isAvailable() ? storageMaterial : null;
    }

    default StorageMaterial queryByMaterialNoIgnoreAvailable(String materialNo) {
        if (StrUtil.isBlank(materialNo)) {
            return null;
        }
        return selectOne(Wrappers.lambdaQuery(StorageMaterial.class)
                .eq(StorageMaterial::getNo, materialNo)
        );
    }

    default List<StorageMaterial> queryListByPositionIds(List<Long> materialPositionIds) {
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getMaterialPositionId, materialPositionIds)
                .and(wrapper -> wrapper.ne(StorageMaterial::getAvailableQuantity, 0)
                        .or()
                        .ne(StorageMaterial::getReserveQuantity, 0))
        );
    }

    /**
     * 根据批次id和货位id查询可用量不为0的物料
     *
     * @param batchIds
     * @param materialPositionIds
     * @return
     */
    default List<StorageMaterial> queryListByBatchIdsAndPositionId(List<Long> batchIds, Collection<Long> materialPositionIds) {
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getStorageMaterialBatchId, batchIds)
                .in(CollectionUtil.isNotEmpty(materialPositionIds), StorageMaterial::getMaterialPositionId, materialPositionIds)
                .and(wrapper -> wrapper.ne(StorageMaterial::getAvailableQuantity, 0)
                        .or()
                        .ne(StorageMaterial::getReserveQuantity, 0))
        );
    }

    /**
     * 根据货位不为空的物料件id查询可用量不为0的物料
     *
     * @param ids 物料件id列表
     * @return
     */
    default List<StorageMaterial> queryPositionIdNotNullListByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getId, ids)
                .isNotNull(StorageMaterial::getMaterialPositionId)
        ).stream().filter(StorageMaterial::isAvailable).collect(Collectors.toList());
    }

    /**
     * 根据货位为空的物料件id查询可用量不为0的物料
     *
     * @param ids 物料件id列表
     * @return
     */
    default List<StorageMaterial> queryPositionIdIsNullListByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getId, ids)
                .isNull(StorageMaterial::getMaterialPositionId)
        ).stream().filter(StorageMaterial::isAvailable).collect(Collectors.toList());
    }

    default void updatePositionIdNullByIds(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        int update = update(null, Wrappers.lambdaUpdate(StorageMaterial.class)
                .set(StorageMaterial::getMaterialPositionId, null)
                .in(StorageMaterial::getId, ids)
        );
        if (update != ids.size()) {
            throw new RuntimeException("更新失败");
        }
    }

    /**
     * 查询流水号
     *
     * @return
     */
    default Long selectMaxNo() {

        // 流水号上限
        long n = 9999999999L;

        Long count = selectCount();
        if (count == 0) {
            return 1L;
        }
        long res = count % n;
        if (res == 0) {
            return n;
        }
        return res;
    }

    default List<StorageMaterial> queryListByIds(Collection<Long> idsList) {
        if (CollectionUtil.isEmpty(idsList)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getId, idsList)
        );
    }

    List<BatchReservedAvailableMaterialVO> selectAvailableMaterial(@Param("materialId") Long materialId, @Param("deptIds") List<Long> deptIds);

    /**
     * 根据物料件id列表查询未签名的物料件列表
     *
     * @param storageMaterialIds
     * @return
     */
    default List<StorageMaterial> queryUnsignedListByMaterialIds(List<Long> storageMaterialIds) {
        if (CollectionUtil.isEmpty(storageMaterialIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getId, storageMaterialIds)
                .eq(StorageMaterial::getSignStatus, WeighSignStatus.UN_SIGNED.getValue())
        );
    }

    default List<StorageMaterial> queryListByNos(List<String> storateMaterialNoList) {
        if (CollectionUtil.isEmpty(storateMaterialNoList)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getNo, storateMaterialNoList)
        );
    }

    void scrapBatch(@Param("scrapStorageMaterialIdList") Collection<Long> scrapStorageMaterialIdList);

    /**
     * 根据物料件id解绑容器
     *
     * @param ids 物料件id
     */
    default void unbindContainersByIds(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return;
        }
        update(null, new LambdaUpdateWrapper<StorageMaterial>()
                .in(StorageMaterial::getId, ids)
                .set(StorageMaterial::getContainer, null)
                .set(StorageMaterial::getContainerId, null)
        );
    }

    /**
     * 根据物料件id解绑容器
     *
     * @param id 物料件id
     */
    default void unbindContainersById(Long id) {
        if (id == null) {
            return;
        }
        update(null, new LambdaUpdateWrapper<StorageMaterial>()
                .eq(StorageMaterial::getId, id)
                .set(StorageMaterial::getContainer, null)
                .set(StorageMaterial::getContainerId, null)
        );
    }

    /**
     * 根据容器id查询物料件信息
     *
     * @param containerId 容器id
     * @return 物料件信息
     */
    default StorageMaterial selectStorageMaterialByContainerId(Long containerId) {
        return selectOne(Wrappers.lambdaQuery(StorageMaterial.class)
                .eq(StorageMaterial::getContainerId, containerId)
        );
    }

    default StorageMaterial selectByContainerId(Long id) {
        return selectOne(new LambdaQueryWrapperX<StorageMaterial>().eq(StorageMaterial::getContainerId, id));
    }

    List<StorageMaterialDetailInfo> selectDetailInfoByIdList(@Param("storageMaterialIdList") List<Long> storageMaterialIdList);


    /**
     * @param batchIds      物料批次id
     * @param productPlanId 预定到的生产批次id
     * @return
     */
    List<StorageMaterial> selectByBatchIdsAndReservePlanId(@Param("batchIds") List<Long> batchIds, @Param("productPlanId") Long productPlanId);

    default List<StorageMaterial> selectAvailableByBatchIds(List<Long> storageMaterialBatchIds) {
        return selectList(Wrappers.lambdaQuery(StorageMaterial.class)
                .in(StorageMaterial::getStorageMaterialBatchId, storageMaterialBatchIds)
                .and(wrapper -> wrapper.isNull(StorageMaterial::getSignStatus).or(wrapper1 -> wrapper1.eq(StorageMaterial::getSignStatus, WeighSignStatus.SIGNED.getValue())))
                .gt(StorageMaterial::getAvailableQuantity, 0)
        );
    }
}
