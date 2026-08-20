package com.bmos.mes.service.weigh.centre2.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.TicketRequirementReleaseStatus;
import com.bmos.mes.common.enums.weigh.centre2.TicketStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.TicketWeighStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.WeighTypeEnum;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.ProductionCompletionQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.TicketCompletionQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.dto.TodayTicketQueryDTO;
import com.bmos.mes.service.weigh.centre2.dashboard.enums.DashboardWeighStatusEnum;
import com.bmos.mes.service.weigh.centre2.dashboard.service.IWeighDashboardService;
import com.bmos.mes.service.weigh.centre2.dashboard.vo.*;
import com.bmos.mes.service.weigh.centre2.execute.mapper.WeighRequirementRecordMapper;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighRequirementRecordDO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementGroupDO;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementGroupMapper;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementMapper;
import com.bmos.mes.service.weigh.centre2.ticket.entity.TicketDO;
import com.bmos.mes.service.weigh.centre2.ticket.mapper.ITicketMapper;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 称量工单看板Service实现类
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/27 17:55
 */
@Service
@Slf4j
public class WeighDashboardServiceImpl implements IWeighDashboardService {

    @Resource
    private ITicketMapper ticketMapper;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Resource
    private WeighRequirementRecordMapper requirementRecordMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private UnitCache unitCache;

    @Resource
    private ITicketRequirementGroupMapper requirementGroupMapper;

    @Resource
    private ITicketRequirementMapper requirementMapper;

    @Override
    public TicketOverviewVO getTicketOverview(Integer recentDays) {

        log.info("获取工单概览数据，查询范围：{}天", recentDays);

        // 数据权限
        List<Long> weighCentreIds = getWeighCentreIds();

        // 计算时间范围
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDate.now().minusDays(recentDays - 1).atStartOfDay();

        // 查询已下发工单数量
        Long releasedCount = ticketMapper.selectCount(new LambdaQueryWrapper<TicketDO>()
                .between(TicketDO::getPlanDate, startDateTime, endDateTime)
                // 不统计已完成的工单
                .isNull(TicketDO::getCompleteTime)
                .in(TicketDO::getWeighCentreId, weighCentreIds)
                // 不统计计划时间晚于今天的数据
                .le(TicketDO::getPlanDate, LocalDate.now())
        );

        // 查询已完成工单数量
        Long completedCount = ticketMapper.selectCount(new LambdaQueryWrapper<TicketDO>()
                .between(TicketDO::getPlanDate, startDateTime, endDateTime)
                // 只统计已完成的工单
                .isNotNull(TicketDO::getCompleteTime)
                .in(TicketDO::getWeighCentreId, weighCentreIds)
                // 不统计计划时间晚于今天的数据
                .le(TicketDO::getPlanDate, LocalDate.now())
        );

        // 构建返回结果
        TicketOverviewVO result = new TicketOverviewVO();
        result.setReleasedCount(releasedCount.intValue());
        result.setCompletedCount(completedCount.intValue());

        log.info("工单概览数据：已下发工单数量={}，已完成工单数量={}", releasedCount, completedCount);
        return result;
    }

    @Override
    public CommonPage<TodayTicketVO> getTodayTicket(TodayTicketQueryDTO queryDTO) {
        log.info("获取今日工单数据，页码：{}，页大小：{}", queryDTO.getPageNum(), queryDTO.getPageSize());

        // 数据权限
        List<Long> weighCentreIds = getWeighCentreIds();

        // 查询今日计划的工单
        LambdaQueryWrapper<TicketDO> queryWrapper = new LambdaQueryWrapper<TicketDO>()
                .eq(TicketDO::getPlanDate, LocalDate.now())
                .ne(TicketDO::getStatus, TicketStatusEnum.CANCELED.getValue())
                .ne(TicketDO::getStatus, TicketStatusEnum.EDIT.getValue())
                .in(TicketDO::getWeighCentreId, weighCentreIds);

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<TicketDO> todayTickets = ticketMapper.selectList(queryWrapper);

        // 立即转换为CommonPage保存分页信息
        CommonPage<TicketDO> commonPage = CommonPage.convertPage(todayTickets);

        if (CollectionUtils.isAnyEmpty(todayTickets)) {
            log.info("今日无计划工单");
            return CommonPage.CommonPage(new ArrayList<>(), 0L, queryDTO);
        }

        // 获取所有工单ID列表
        List<Long> ticketIds = todayTickets.stream()
                .map(TicketDO::getId)
                .collect(Collectors.toList());

        // 查询所有工单的称量记录
        List<WeighRequirementRecordDO> allRecords = requirementRecordMapper.selectList(
                new LambdaQueryWrapper<WeighRequirementRecordDO>()
                        .eq(WeighRequirementRecordDO::getWeighType, WeighTypeEnum.NORMAL.getValue())
                        .in(WeighRequirementRecordDO::getTicketId, ticketIds)
        );

        // 按工单ID分组称量记录
        Map<Long, List<WeighRequirementRecordDO>> recordMap = allRecords.stream()
                .collect(Collectors.groupingBy(WeighRequirementRecordDO::getTicketId));

        // 查询称量中心名称
        List<Long> queryWeighCentreIds = todayTickets.stream()
                .map(TicketDO::getWeighCentreId)
                .collect(Collectors.toList());
        Map<Long, WeighCentre> weighCentreMap = weighCentreMapper.selectBatchIds(queryWeighCentreIds)
                .stream()
                .collect(Collectors.toMap(WeighCentre::getId, Function.identity(), (k1, k2) -> k1));

        // 转换为前端展示VO
        List<TodayTicketVO> resultList = new ArrayList<>();
        for (TicketDO ticket : todayTickets) {
            TodayTicketVO vo = new TodayTicketVO();

            // 基本信息
            vo.setTicketNo(ticket.getTicketNo());
            vo.setMaterialName(ticket.getMaterialMergeCode() + "-" + ticket.getMaterialName());
            WeighCentre weighCentre = weighCentreMap.get(ticket.getWeighCentreId());
            vo.setWeighCentreName(weighCentre != null ? weighCentre.getCode() + "-" + weighCentre.getName() : "未知");

            // 需求量
            vo.setRequiredTotalQuantity(ticket.getRequirementQuantity());

            // 获取单位名称
            String unitName = unitCache.getGlobalUnitName(ticket.getUnitId());
            vo.setUnit(unitName);

            // 计算已完成量
            List<WeighRequirementRecordDO> records = recordMap.getOrDefault(ticket.getId(), new ArrayList<>());
            BigDecimal completedWeight = records.stream()
                    .map(WeighRequirementRecordDO::getNetWeight)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            vo.setCompletedWeight(completedWeight);

            // 计算完成率
            BigDecimal completionRate = BigDecimal.ZERO;
            if (ticket.getRequirementQuantity() != null && ticket.getRequirementQuantity().compareTo(BigDecimal.ZERO) > 0) {
                completionRate = completedWeight.multiply(new BigDecimal("100"))
                        .divide(ticket.getRequirementQuantity(), 1, RoundingMode.HALF_UP);
            }
            BigDecimal max = new BigDecimal("100.0");
            if (completionRate.compareTo(max) > 0) {
                completionRate = max;
            }
            vo.setCompletionRate(completionRate + "%");

            // 设置状态
            if (ticket.getTicketWeighStatus() != null) {
                switch (ticket.getTicketWeighStatus()) {
                    case UN_WEIGHED:
                        vo.setStatus(DashboardWeighStatusEnum.SEND);
                        break;
                    case WEIGHING:
                        vo.setStatus(DashboardWeighStatusEnum.WEIGHING);
                        break;
                    case WEIGHED:
                        vo.setStatus(DashboardWeighStatusEnum.WEIGHED);
                        break;
                    default:
                        vo.setStatus(DashboardWeighStatusEnum.SEND);
                }
            } else {
                vo.setStatus(DashboardWeighStatusEnum.SEND);
            }

            resultList.add(vo);
        }

        log.info("今日工单数据：共{}条", resultList.size());

        // 创建新的CommonPage并保持原分页信息
        CommonPage<TodayTicketVO> result = new CommonPage<>();
        result.setList(resultList);
        result.setPageNum(commonPage.getPageNum());
        result.setPageSize(commonPage.getPageSize());
        result.setTotal(commonPage.getTotal());
        result.setTotalPage(commonPage.getTotalPage());

        return result;
    }

    @Override
    public CommonPage<ProductionCompletionVO> getProductionCompletion(ProductionCompletionQueryDTO queryDTO) {
        log.info("获取生产批次配料完成情况，查询范围：{}天, 页码：{}，页大小：{}",
                queryDTO.getRecentDays(), queryDTO.getPageNum(), queryDTO.getPageSize());

        // 数据权限
        List<Long> weighCentreIds = getWeighCentreIds();

        // 计算时间范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(queryDTO.getRecentDays() - 1);

        // 构建查询条件
        LambdaQueryWrapper<TicketRequirementGroupDO> queryWrapper = new LambdaQueryWrapper<TicketRequirementGroupDO>()
                .in(TicketRequirementGroupDO::getWeighCentreId, weighCentreIds)
                .and(wrapper -> wrapper
                        .ne(TicketRequirementGroupDO::getReleaseStatus, TicketRequirementReleaseStatus.EDIT)
                        .ne(TicketRequirementGroupDO::getReleaseStatus, TicketRequirementReleaseStatus.CANCELED)
                        .between(TicketRequirementGroupDO::getPlanDate, startDate, endDate)
                        .or()
                        .gt(TicketRequirementGroupDO::getPlanDate, LocalDate.now())
                        .eq(TicketRequirementGroupDO::getReleaseStatus, TicketRequirementReleaseStatus.FINISHED)
                );

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<TicketRequirementGroupDO> requirementGroups = requirementGroupMapper.selectList(queryWrapper);

        // 立即转换为CommonPage保存分页信息
        CommonPage<TicketRequirementGroupDO> commonPage = CommonPage.convertPage(requirementGroups);

        if (CollectionUtils.isAnyEmpty(requirementGroups)) {
            log.info("查询范围内无生产批次数据");
            return CommonPage.CommonPage(new ArrayList<>(), 0L, queryDTO);
        }

        // 获取需求组ID列表
        List<Long> groupIds = requirementGroups.stream()
                .map(TicketRequirementGroupDO::getId)
                .collect(Collectors.toList());

        // 查询需求组下的所有需求
        List<TicketRequirementDO> allRequirements = new ArrayList<>();
        for (Long groupId : groupIds) {
            List<TicketRequirementDO> requirements = requirementMapper.selectByRequirementGroupId(groupId);
            allRequirements.addAll(requirements);
        }

        // 按需求组ID分组需求
        Map<Long, List<TicketRequirementDO>> requirementMap = allRequirements.stream()
                .collect(Collectors.groupingBy(TicketRequirementDO::getRequirementGroupId));

        // 查询称量中心信息
        List<Long> weighCentreIdList = requirementGroups.stream()
                .map(TicketRequirementGroupDO::getWeighCentreId)
                .collect(Collectors.toList());
        Map<Long, WeighCentre> weighCentreMap = weighCentreMapper.selectBatchIds(weighCentreIdList)
                .stream()
                .collect(Collectors.toMap(WeighCentre::getId, Function.identity(), (k1, k2) -> k1));


        // 查询产品信息
        List<Long> materialIdList = requirementGroups.stream()
                .map(TicketRequirementGroupDO::getMaterialId)
                .collect(Collectors.toList());

        Map<Long, ProductMaterial> productMaterialMap = productMaterialMapper.selectBatchIds(materialIdList)
                .stream()
                .collect(Collectors.toMap(ProductMaterial::getId, Function.identity(), (k1, k2) -> k1));

        // 转换为前端展示VO
        List<ProductionCompletionVO> resultList = new ArrayList<>();
        for (TicketRequirementGroupDO group : requirementGroups) {
            ProductionCompletionVO vo = new ProductionCompletionVO();
            vo.setId(group.getId());

            // 设置基本信息
            vo.setBatchNo(group.getBatchNo());
            vo.setPlanProductionDate(group.getPlanDate());

            ProductMaterial material = productMaterialMap.get(group.getMaterialId());
            if (material != null) {
                vo.setProductName(material.getName());
                vo.setProductMergeCode(material.getMergeCode());
            }
            // 查询称量中心
            WeighCentre weighCentre = weighCentreMap.get(group.getWeighCentreId());
            vo.setWeighCentreName(weighCentre != null ? weighCentre.getCode() + "-" + weighCentre.getName() : "未知");

            // 查询需求组下的所有需求
            List<TicketRequirementDO> requirements = requirementMap.getOrDefault(group.getId(), new ArrayList<>());

            // 计算需求组的完成率
            int totalRequirements = requirements.size();
            if (totalRequirements == 0) {
                vo.setCompletionRate("0%");
                vo.setStatus(DashboardWeighStatusEnum.SEND);
            } else {
                // 计算已完成需求数量
                long completedCount = requirements.stream()
                        .filter(req -> req.getRequirementStatus() == RequirementStatusEnum.WEIGHED)
                        .count();

                // 计算完成率
                BigDecimal completionRate = BigDecimal.valueOf(completedCount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalRequirements), 1, RoundingMode.HALF_UP);

                BigDecimal max = new BigDecimal("100.0");
                if (completionRate.compareTo(max) > 0) {
                    completionRate = max;
                }
                vo.setCompletionRate(completionRate + "%");

                // 设置状态
                if (completedCount == 0) {
                    vo.setStatus(DashboardWeighStatusEnum.SEND);
                } else if (completedCount < totalRequirements) {
                    vo.setStatus(DashboardWeighStatusEnum.WEIGHING);
                } else {
                    vo.setStatus(DashboardWeighStatusEnum.WEIGHED);
                }
            }

            resultList.add(vo);
        }

        log.info("生产批次配料完成情况：共{}条", resultList.size());

        // 创建新的CommonPage并保持原分页信息
        CommonPage<ProductionCompletionVO> result = new CommonPage<>();
        result.setList(resultList);
        result.setPageNum(commonPage.getPageNum());
        result.setPageSize(commonPage.getPageSize());
        result.setTotal(commonPage.getTotal());
        result.setTotalPage(commonPage.getTotalPage());

        return result;
    }

    @Override
    public List<WeighTrendVO> getTicketTrend(Integer recentDays) {
        log.info("获取称量工单趋势，查询范围：{}天", recentDays);

        // 数据权限
        List<Long> weighCentreIds = getWeighCentreIds();

        // 计算时间范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(recentDays - 1);

        // 生成所有日期的列表（包括没有数据的日期）
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        // 查询时间范围内已完成的工单
        List<TicketDO> completedTickets = ticketMapper.selectList(
                new LambdaQueryWrapper<TicketDO>()
                        .isNotNull(TicketDO::getCompleteTime)
                        .eq(TicketDO::getTicketWeighStatus, TicketWeighStatusEnum.WEIGHED)
                        .in(TicketDO::getWeighCentreId, weighCentreIds)
        );

        // 按完成日期分组计数
        Map<LocalDate, Long> dateCountMap = completedTickets.stream()
                .filter(ticket -> ticket.getCompleteTime() != null)
                .filter(ticket -> {
                    LocalDate completeDate = ticket.getCompleteTime().toLocalDate();
                    return !completeDate.isBefore(startDate) && !completeDate.isAfter(endDate);
                })
                .collect(Collectors.groupingBy(
                        ticket -> ticket.getCompleteTime().toLocalDate(),
                        Collectors.counting()
                ));

        // 构建返回结果，确保每一天都有数据
        List<WeighTrendVO> result = new ArrayList<>();
        for (LocalDate date : allDates) {
            WeighTrendVO vo = new WeighTrendVO();
            vo.setDate(date);
            vo.setCount(dateCountMap.getOrDefault(date, 0L).intValue());
            result.add(vo);
        }

        log.info("称量工单趋势：查询到{}条数据", result.size());
        return result;
    }

    @Override
    public List<WeighTrendVO> getRequirementTrend(Integer recentDays) {
        log.info("获取称量需求趋势，查询范围：{}天", recentDays);

        // 数据权限
        List<Long> weighCentreIds = getWeighCentreIds();

        // 计算时间范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(recentDays - 1);

        // 生成所有日期的列表（包括没有数据的日期）
        List<LocalDate> allDates = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            allDates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        // 查询时间范围内已完成的需求
        List<TicketRequirementDO> completedRequirements = requirementMapper.selectList(
                new LambdaQueryWrapper<TicketRequirementDO>()
                        .isNotNull(TicketRequirementDO::getCompleteTime)
                        .eq(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.WEIGHED)
                        .in(TicketRequirementDO::getWeighCentreId, weighCentreIds)
        );

        // 按完成日期分组计数
        Map<LocalDate, Long> dateCountMap = completedRequirements.stream()
                .filter(req -> req.getCompleteTime() != null)
                .filter(req -> {
                    LocalDate completeDate = req.getCompleteTime().toLocalDate();
                    return !completeDate.isBefore(startDate) && !completeDate.isAfter(endDate);
                })
                .collect(Collectors.groupingBy(
                        req -> req.getCompleteTime().toLocalDate(),
                        Collectors.counting()
                ));

        // 构建返回结果，确保每一天都有数据
        List<WeighTrendVO> result = new ArrayList<>();
        for (LocalDate date : allDates) {
            WeighTrendVO vo = new WeighTrendVO();
            vo.setDate(date);
            vo.setCount(dateCountMap.getOrDefault(date, 0L).intValue());
            result.add(vo);
        }

        log.info("称量需求趋势：查询到{}条数据", result.size());
        return result;
    }

    @Override
    public CommonPage<TicketCompletionVO> getTicketCompletion(TicketCompletionQueryDTO queryDTO) {
        log.info("获取称量工单完成情况，查询范围：{}天, 页码：{}，页大小：{}",
                queryDTO.getRecentDays(), queryDTO.getPageNum(), queryDTO.getPageSize());

        // 数据权限
        List<Long> weighCentreIds = getWeighCentreIds();

        // 计算时间范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(queryDTO.getRecentDays() - 1);

        // 构建查询条件
        LambdaQueryWrapper<TicketDO> queryWrapper = new LambdaQueryWrapper<TicketDO>()
                .in(TicketDO::getWeighCentreId, weighCentreIds)
                .and(wrapper -> wrapper
                        .between(TicketDO::getPlanDate, startDate, endDate)
                        .ne(TicketDO::getStatus, TicketStatusEnum.CANCELED.getValue())
                        .ne(TicketDO::getStatus, TicketStatusEnum.EDIT.getValue())
                        .or()
                        .gt(TicketDO::getPlanDate, LocalDate.now())
                        .eq(TicketDO::getStatus, TicketStatusEnum.EXECUTED)
                );

        // 开启分页
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<TicketDO> tickets = ticketMapper.selectList(queryWrapper);

        // 立即转换为CommonPage保存分页信息
        CommonPage<TicketDO> commonPage = CommonPage.convertPage(tickets);

        if (CollectionUtils.isAnyEmpty(tickets)) {
            log.info("查询范围内无工单数据");
            return CommonPage.CommonPage(new ArrayList<>(), 0L, queryDTO);
        }

        // 获取所有工单ID列表
        List<Long> ticketIds = tickets.stream()
                .map(TicketDO::getId)
                .collect(Collectors.toList());

        // 查询所有工单的称量记录
        List<WeighRequirementRecordDO> allRecords = requirementRecordMapper.selectList(
                new LambdaQueryWrapper<WeighRequirementRecordDO>()
                        .eq(WeighRequirementRecordDO::getWeighType, WeighTypeEnum.NORMAL.getValue())
                        .in(WeighRequirementRecordDO::getTicketId, ticketIds)
        );

        // 按工单ID分组称量记录
        Map<Long, List<WeighRequirementRecordDO>> recordMap = allRecords.stream()
                .collect(Collectors.groupingBy(WeighRequirementRecordDO::getTicketId));

        // 查询称量中心信息
        List<Long> weighCentreIdList = tickets.stream()
                .map(TicketDO::getWeighCentreId)
                .collect(Collectors.toList());
        Map<Long, WeighCentre> weighCentreMap = weighCentreMapper.selectBatchIds(weighCentreIdList)
                .stream()
                .collect(Collectors.toMap(WeighCentre::getId, Function.identity(), (k1, k2) -> k1));

        // 转换为前端展示VO
        List<TicketCompletionVO> resultList = new ArrayList<>();
        for (TicketDO ticket : tickets) {
            TicketCompletionVO vo = new TicketCompletionVO();

            // 基本信息
            vo.setTicketNo(ticket.getTicketNo());
            vo.setMaterialName(ticket.getMaterialName());
            vo.setMaterialCode(ticket.getMaterialMergeCode());

            // 查询称量中心
            WeighCentre weighCentre = weighCentreMap.get(ticket.getWeighCentreId());
            vo.setWeighCentreName(weighCentre != null ? weighCentre.getCode() + "-" + weighCentre.getName() : "未知");

            // 需求量
            vo.setRequiredTotalQuantity(ticket.getRequirementQuantity());

            // 获取单位名称
            String unitName = unitCache.getGlobalUnitName(ticket.getUnitId());
            vo.setUnit(unitName);

            // 计划执行时间
            vo.setPlanExecuteDate(ticket.getPlanDate());

            // 计算已完成量
            List<WeighRequirementRecordDO> records = recordMap.getOrDefault(ticket.getId(), new ArrayList<>());
            BigDecimal completedWeight = records.stream()
                    .map(WeighRequirementRecordDO::getNetWeight)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            vo.setCompletedWeight(completedWeight);

            // 计算完成率
            BigDecimal completionRate = BigDecimal.ZERO;
            if (ticket.getRequirementQuantity() != null && ticket.getRequirementQuantity().compareTo(BigDecimal.ZERO) > 0) {
                completionRate = completedWeight.multiply(new BigDecimal("100"))
                        .divide(ticket.getRequirementQuantity(), 1, RoundingMode.HALF_UP);
            }
            BigDecimal max = new BigDecimal("100.0");
            if (completionRate.compareTo(max) > 0) {
                completionRate = max;
            }
            vo.setCompletionRate(completionRate + "%");

            // 设置状态
            if (ticket.getTicketWeighStatus() != null) {
                switch (ticket.getTicketWeighStatus()) {
                    case UN_WEIGHED:
                        vo.setStatus(DashboardWeighStatusEnum.SEND);
                        break;
                    case WEIGHING:
                        vo.setStatus(DashboardWeighStatusEnum.WEIGHING);
                        break;
                    case WEIGHED:
                        vo.setStatus(DashboardWeighStatusEnum.WEIGHED);
                        break;
                    default:
                        vo.setStatus(DashboardWeighStatusEnum.SEND);
                }
            } else {
                vo.setStatus(DashboardWeighStatusEnum.SEND);
            }

            resultList.add(vo);
        }

        log.info("称量工单完成情况：共{}条", resultList.size());

        // 创建新的CommonPage并保持原分页信息
        CommonPage<TicketCompletionVO> result = new CommonPage<>();
        result.setList(resultList);
        result.setPageNum(commonPage.getPageNum());
        result.setPageSize(commonPage.getPageSize());
        result.setTotal(commonPage.getTotal());
        result.setTotalPage(commonPage.getTotalPage());
        return result;
    }

    /**
     * 查询当前登录人工单有权限的称量中心id
     *
     * @return 称量中心数据权限范围
     */
    private List<Long> getWeighCentreIds() {
        List<Long> deptIds = platformApiAdaptor.deptIds();
        List<WeighCentre> weighCentres = weighCentreMapper.listAllByDeptIds(deptIds);
        return CollectionUtils.convertList(weighCentres, WeighCentre::getId);
    }
} 