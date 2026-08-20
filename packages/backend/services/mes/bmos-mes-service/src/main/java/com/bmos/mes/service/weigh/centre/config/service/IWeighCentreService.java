package com.bmos.mes.service.weigh.centre.config.service;

import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreBindStationDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCreateDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreEditDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentrePageQuery;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryWithCentreVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreDetailVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentrePageVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 称量中心service
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 17:36
 */
public interface IWeighCentreService {

    /**
     * 分页查询称量中心
     * @param pageQuery 分页参数
     * @return
     */
    CommonPage<WeighCentrePageVO> queryPage(WeighCentrePageQuery pageQuery);

    /**
     * 查询称量中心详情
     * @param id 称量中心id
     * @return
     */
    WeighCentreDetailVO queryCentreInfo(Long id);

    /**
     * 新增称量中心
     * @param createDTO 新增参数
     */
    void createCentre(WeighCentreCreateDTO createDTO);

    /**
     * 编辑称量中心
     * @param editDTO 编辑参数
     */
    void editCentre(WeighCentreEditDTO editDTO);

    /**
     * 删除称量中心
     * @param id 称量中心id
     */
    void deleteCentre(Long id);

    /**
     * 启用称量中心
     * @param id 称量中心id
     */
    void enableCentre(Long id);

    /**
     * 停用称量中心
     * @param id 称量中心id
     */
    void disableCentre(Long id);

    /**
     * 绑定工位
     * @param bindStationDTO 绑定工位参数
     */
    void bindStation(WeighCentreBindStationDTO bindStationDTO);

    /**
     * 查询称量中心树
     * @return
     */
    List<WeighCentreCategoryWithCentreVO> weighCentreTree();

    /**
     * 根据工位id查询称量中心Id
     * @param stationIdList
     * @return
     */
    List<Long> selectByStationIds(List<Long> stationIdList);
}
