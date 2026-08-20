package com.bmos.lims2.server.inspect.pack.service;

import com.bmos.lims2.server.inspect.pack.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 实验包业务接口
 */
public interface InspectPackageService {


    /**
     * 保存实验包
     *
     * @param reqVO
     * @return
     */
    List<Long> savePackage(PackageCreateReqDTO reqVO);

    /**
     * 删除实验包
     *
     * @param id
     */
    void deletePackage(Long id);

    /**
     * 修改实验包
     *
     * @param reqVO
     * @return
     */
    List<Long> updatePackage(PackageUpdateReqDTO reqVO);

    /**
     * 实验包分页信息查询
     *
     * @param reqVO
     * @return
     */
    CommonPage<InspectPackageDTO> packagePage(PackagePageReqDTO reqVO);

    /**
     * 实验包分页信息查询 - 包含检验项目信息
     *
     * @param reqVO
     * @return
     */
    CommonPage<InspectPackageWithItemsDTO> packagePageWithItems(PackagePageReqDTO reqVO);

    /**
     * 实验包详情信息查询 包含实验包下的检验项目
     *
     * @param id
     * @return
     */
    InspectPackageWithItemDTO packageInfo(Long id);

    /**
     * 根据实验包id查询实验包下的检验项目信息
     *
     * @param id
     * @return
     */
    List<InspectPackageItemDTO> packageInspectInfo(Long id);

    /**
     * 根据实验包id集合查询实验包信息
     *
     * @param packageIdList
     * @return
     */
    List<InspectPackageDTO> selectByIdList(List<Long> packageIdList);

    /**
     * 查询实验包列表 - 用于下拉选择
     * @return 实验包列表
     */
    List<InspectPackageListDTO> getList();

    /**
     * 根据实验包ID查询完整配置信息（包含检项、分析项、数据点）
     * @param packageId 实验包ID
     * @return 完整配置信息
     */
    InspectPackageFullConfigDTO getFullConfigByPackageId(Long packageId);
}
