package com.bmos.platform.service.factory.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.dto.LineUseDTO;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.bmos.platform.facade.factory.vo.LineModuleTreeNodeFeignVO;
import com.bmos.platform.service.factory.controller.FactoryResourceUserVO;
import com.bmos.platform.service.factory.controller.vo.LineInfoVO;
import com.bmos.platform.service.factory.controller.vo.LinePageVO;
import com.bmos.platform.service.factory.controller.vo.FactoryTreeNodeVO;
import com.bmos.platform.service.factory.service.dto.*;

import java.util.Collection;
import java.util.List;

/**
 * 产线业务接口
 */
public interface LineService {

    /**
     * 新建产线
     * @param dto
     */
    void saveLine(LineSaveDTO dto);

    /**
     * 修改产线
     * @param dto
     */
    void updateLine(LineUpdateDTO dto);

    /**
     * 删除产线
     * @param id
     */
    void deleteLine(Long id);

    /**
     * 启用/禁用产线
     * @param dto
     */
    void enableLine(LineEnableDTO dto);

    /**
     * 分页查询产线
     * @param dto
     * @return
     */
    CommonPage<LinePageVO> getLinePage(LinePageDTO dto);

    /**
     * 查询产线详情
     * @param id
     * @return
     */
    LineInfoVO getLineInfo(Long id);

    /**
     * 绑定房间
     * @param dto
     */
    void bindRoom(LineBindRoomDTO dto);

    /**
     * 绑定工位
     * @param dto
     */
    void bindStation(LineBindStationDTO dto);

    /**
     * 根据参数查询产线信息
     *
     * @param name
     * @return
     */
    List<FactoryLineFeignVO> getLineByCondition(String name);

    /**
     * 根据产线ID查询产线详情
     * @param lineIds：产线ID集合
     * @param stationFlag: 是否房间下的查询工位
     * @return
     */
    List<FactoryLineDetailFeignVO> getLineDetailByLineIds(Collection<Long> lineIds, boolean stationFlag);

    /**
     * 查询产线下的所有工位
     * @param lineId
     * @return
     */
    List<Long> selectStationIdByLineId(Long lineId);

    /**
     * 产线/房间/工位被某个工置绑定或解绑
     *
     * @param dto
     */
    void bindUseCount(LineUseDTO dto);

    /**
     * 获取产线模型树VO
     * @return
     */
    List<LineModuleTreeNodeFeignVO> getLineModuleTreeVO();

    List<FactoryLineDetailFeignVO> queryLineDetailListByLineIds(List<Long> lineIdList);

    /**
     * 获取某个用户具有数据权限产线/房间下的所有工位
     *
     * @return
     */
    FactoryResourceUserVO getLineByUser(String userId);

    List<FactoryLineFeignVO> queryLineListByLineIds(List<Long> lineIdList);

}
