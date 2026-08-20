package com.bmos.wms.service.cargo.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.cargo.dto.*;
import com.bmos.wms.service.cargo.vo.CargoPageVO;
import com.bmos.wms.service.cargo.vo.CargoVO;
import com.bmos.wms.service.platform.material.dto.SyncTreeQueryDTO;
import com.bmos.wms.service.platform.material.vo.SyncTreeNodeVO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 15:06
 */
public interface ICargoService {

    /**
     * 创建货品
     *
     * @param dto
     */
    void create(CargoCreateDTO dto);

    /**
     * 编辑货品
     *
     * @param dto
     */
    void edit(CargoEditDTO dto);

    /**
     * 启用货品
     *
     * @param id
     */
    void enable(Long id);

    /**
     * 停用货品
     *
     * @param id
     */
    void disable(Long id);

    /**
     * 删除货品
     *
     * @param id 货品id
     */
    void delete(Long id);

    /**
     * 根据id查询货品信息
     *
     * @param id 货品id
     * @return
     */
    @Nullable
    CargoVO queryInfoById(Long id);

    /**
     * 查询货品分页
     *
     * @param pageQuery 分页参数
     * @return
     */
    CommonPage<CargoPageVO> queryPage(CargoPageQuery pageQuery);

    /**
     * 获取同步物料树
     *
     * @param dto
     * @return
     */
    List<SyncTreeNodeVO> getSyncTree(SyncTreeQueryDTO dto);

    /**
     * 同步物料
     */
    List<SyncTreeNodeVO> getSyncTreeAll();

    /**
     * 同步物料
     *
     * @param dto 同步物料
     */
    void syncMaterialAndCategory(SyncCargoDTO dto);

    /**
     * 根据货品分类id查询非成员货品列表
     *
     * @param categoryId
     * @return
     */
    List<CargoVO> queryNotMemberListByCategoryId(Long categoryId);

    void issueMaterialAndCategory(RemoteIssueFeignDTO dto);
}
