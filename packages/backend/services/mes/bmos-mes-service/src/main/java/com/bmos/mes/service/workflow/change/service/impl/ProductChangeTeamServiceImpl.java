package com.bmos.mes.service.workflow.change.service.impl;

import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.plan.instruction.vo.InstructionTeamVO;
import com.bmos.mes.service.workflow.change.convert.ProductChangeTeamConverter;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.change.dto.TeamListDTO;
import com.bmos.mes.service.workflow.change.mapper.ProductChangeTeamMapper;
import com.bmos.mes.service.workflow.change.model.ProductChangeTeam;
import com.bmos.mes.service.workflow.change.service.ProductChangeTeamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author renjinguang
 */
@Service
public class ProductChangeTeamServiceImpl implements ProductChangeTeamService {

    @Resource
    private ProductChangeTeamMapper changeTeamMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ProductChangeTeam> saveChangeTeam(List<TeamListDTO> changeTeamList, Integer changeTeamNumber, String nodeFunction) {
        List<ProductChangeTeam> productChangeTeams = ProductChangeTeamConverter.INSTANCE.convertToChangeTeam(changeTeamList, changeTeamNumber, nodeFunction);
        changeTeamMapper.saveOrUpdateBatch(productChangeTeams);
        return productChangeTeams;
    }

    @Override
    public List<ProductChangeTeam> queryByPlanIdAndProcedureModelId(ChangeTeamDTO teamDTO) {
        return changeTeamMapper.queryByPlanIdAndProcedureModelId(teamDTO);
    }

    @Override
    public List<InstructionTeamVO> selectListByPlanId(Long planId, String nodeFunction,Integer changeNumber) {
        List<InstructionTeamVO> vo = changeTeamMapper.selectListByPlanId(planId, nodeFunction,changeNumber);
        List<InstructionTeamVO> teamVo = ProductChangeTeamConverter.INSTANCE.covertToFreshTeamVo(vo);
        List<Long> changeTeamId = CollectionUtils.convertList(teamVo, InstructionTeamVO::getChangeTeamId);
        Map<Long, ProductChangeTeam> teamMap = CollectionUtils.convertMap(changeTeamMapper.selectByIds(changeTeamId), ProductChangeTeam::getId);
        teamVo.forEach(item-> item.setTeamIds(teamMap.get(item.getChangeTeamId()).getTeamIds()));
        return teamVo;
    }
}
