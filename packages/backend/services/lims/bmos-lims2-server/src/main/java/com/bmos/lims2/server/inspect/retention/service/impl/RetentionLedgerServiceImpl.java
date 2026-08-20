package com.bmos.lims2.server.inspect.retention.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.retention.dto.*;
import com.bmos.lims2.server.inspect.retention.mapper.RetentionDestructionLedgerMapper;
import com.bmos.lims2.server.inspect.retention.mapper.RetentionObservationLedgerMapper;
import com.bmos.lims2.server.inspect.retention.mapper.RetentionReceiveLedgerMapper;
import com.bmos.lims2.server.inspect.retention.mapper.SampleCollectionLedgerMapper;
import com.bmos.lims2.server.inspect.retention.service.RetentionLedgerService;
import com.bmos.lims2.server.inspect.retention.vo.RetentionDestructionLedgerExportVO;
import com.bmos.lims2.server.inspect.retention.vo.RetentionObservationLedgerExportVO;
import com.bmos.lims2.server.inspect.retention.vo.RetentionReceiveLedgerExportVO;
import com.bmos.lims2.server.inspect.retention.vo.SampleCollectionLedgerExportVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 留样台账Service实现类
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Service
@Slf4j
public class RetentionLedgerServiceImpl implements RetentionLedgerService {

    @Autowired
    private RetentionReceiveLedgerMapper retentionReceiveLedgerMapper;

    @Autowired
    private RetentionDestructionLedgerMapper retentionDestructionLedgerMapper;

    @Autowired
    private SampleCollectionLedgerMapper sampleCollectionLedgerMapper;

    @Autowired
    private RetentionObservationLedgerMapper retentionObservationLedgerMapper;

    @Autowired
    private UnitCache unitCache;

    @Override
    public CommonPage<RetentionReceiveLedgerListDTO> getReceiveLedgerPageList(RetentionReceiveLedgerPageQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询列表
        List<RetentionReceiveLedgerListDTO> list = retentionReceiveLedgerMapper.selectReceiveLedgerPageList(queryDTO);

        // 填充单位名称
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                if (item.getUnitId() != null) {
                    item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                }
            });
        }

        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<RetentionDestructionLedgerListDTO> getDestructionLedgerPageList(RetentionDestructionLedgerPageQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询列表
        List<RetentionDestructionLedgerListDTO> list = retentionDestructionLedgerMapper.selectDestructionLedgerPageList(queryDTO);

        // 填充单位名称
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                if (item.getUnitId() != null) {
                    item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                }
            });
        }

        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<SampleCollectionLedgerListDTO> getCollectionLedgerPageList(SampleCollectionLedgerPageQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询列表
        List<SampleCollectionLedgerListDTO> list = sampleCollectionLedgerMapper.selectCollectionLedgerPageList(queryDTO);

        // 填充单位名称
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                if (item.getUnitId() != null) {
                    item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                }
            });
        }

        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<RetentionObservationLedgerListDTO> getObservationLedgerPageList(RetentionObservationLedgerPageQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询列表
        List<RetentionObservationLedgerListDTO> list = retentionObservationLedgerMapper.selectObservationLedgerPageList(queryDTO);

        // 填充单位名称
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                if (item.getUnitId() != null) {
                    item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                }
            });
        }

        return CommonPage.convertPage(list);
    }

    @Override
    public void exportReceiveLedger(RetentionReceiveLedgerPageQueryDTO queryDTO, HttpServletResponse response) {
        try {
            // 不分页查询所有数据
            List<RetentionReceiveLedgerListDTO> list = retentionReceiveLedgerMapper.selectReceiveLedgerPageList(queryDTO);

            // 填充单位名称
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(item -> {
                    if (item.getUnitId() != null) {
                        item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                    }
                });
            }

            // 转换为导出VO
            List<RetentionReceiveLedgerExportVO> exportList = list.stream()
                .map(item -> {
                    RetentionReceiveLedgerExportVO vo = BeanUtil.copyProperties(item, RetentionReceiveLedgerExportVO.class);
                    // 设置取样人和接收人名称
                    vo.setSamplerName(item.getSamplerName());
                    vo.setReceiverName(item.getReceiverName());
                    return vo;
                })
                .collect(Collectors.toList());

            // 导出Excel
            ExcelWriterUtils.write("留样接收台账", response,
                Collections.singletonList(new SheetDataBo("留样接收台账", RetentionReceiveLedgerExportVO.class, exportList, null)));
        } catch (Exception e) {
            log.error("导出留样接收台账失败", e);
            throw new BmosException(LimsResponseCode.RETENTION_LEDGER_EXPORT_ERROR);
        }
    }

    @Override
    public void exportDestructionLedger(RetentionDestructionLedgerPageQueryDTO queryDTO, HttpServletResponse response) {
        try {
            // 不分页查询所有数据
            List<RetentionDestructionLedgerListDTO> list = retentionDestructionLedgerMapper.selectDestructionLedgerPageList(queryDTO);

            // 填充单位名称
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(item -> {
                    if (item.getUnitId() != null) {
                        item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                    }
                });
            }

            // 转换为导出VO
            List<RetentionDestructionLedgerExportVO> exportList = list.stream()
                .map(item -> {
                    RetentionDestructionLedgerExportVO vo = BeanUtil.copyProperties(item, RetentionDestructionLedgerExportVO.class);
                    // 设置销毁人和监督人名称
                    vo.setDestructorName(item.getDestructorName());
                    vo.setSupervisorName(item.getSupervisorName());
                    return vo;
                })
                .collect(Collectors.toList());

            // 导出Excel
            ExcelWriterUtils.write("留样销毁台账", response,
                Collections.singletonList(new SheetDataBo("留样销毁台账", RetentionDestructionLedgerExportVO.class, exportList, null)));
        } catch (Exception e) {
            log.error("导出留样销毁台账失败", e);
            throw new BmosException(LimsResponseCode.RETENTION_LEDGER_EXPORT_ERROR);
        }
    }

    @Override
    public void exportCollectionLedger(SampleCollectionLedgerPageQueryDTO queryDTO, HttpServletResponse response) {
        try {
            // 不分页查询所有数据
            List<SampleCollectionLedgerListDTO> list = sampleCollectionLedgerMapper.selectCollectionLedgerPageList(queryDTO);

            // 填充单位名称
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(item -> {
                    if (item.getUnitId() != null) {
                        item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                    }
                });
            }

            // 转换为导出VO
            List<SampleCollectionLedgerExportVO> exportList = list.stream()
                .map(item -> {
                    SampleCollectionLedgerExportVO vo = BeanUtil.copyProperties(item, SampleCollectionLedgerExportVO.class);
                    // 设置领用人名称
                    vo.setCollectorName(item.getCollectorName());
                    return vo;
                })
                .collect(Collectors.toList());

            // 导出Excel
            ExcelWriterUtils.write("留样领用台账", response,
                Collections.singletonList(new SheetDataBo("留样领用台账", SampleCollectionLedgerExportVO.class, exportList, null)));
        } catch (Exception e) {
            log.error("导出留样领用台账失败", e);
            throw new BmosException(LimsResponseCode.RETENTION_LEDGER_EXPORT_ERROR);
        }
    }

    @Override
    public void exportObservationLedger(RetentionObservationLedgerPageQueryDTO queryDTO, HttpServletResponse response) {
        try {
            // 不分页查询所有数据
            List<RetentionObservationLedgerListDTO> list = retentionObservationLedgerMapper.selectObservationLedgerPageList(queryDTO);

            // 填充单位名称
            if (!CollectionUtils.isEmpty(list)) {
                list.forEach(item -> {
                    if (item.getUnitId() != null) {
                        item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                    }
                });
            }

            // 转换为导出VO，并处理观察结果
            List<RetentionObservationLedgerExportVO> exportList = list.stream()
                .map(item -> {
                    RetentionObservationLedgerExportVO exportVO = BeanUtil.copyProperties(item, RetentionObservationLedgerExportVO.class);
                    // 转换观察结果：true -> "符合", false -> "不符合"
                    if (item.getObservationResult() != null) {
                        exportVO.setObservationResult(item.getObservationResult() ? "符合" : "不符合");
                    }
                    // 设置观察人名称
                    exportVO.setObserverName(item.getObserverName());
                    return exportVO;
                })
                .collect(Collectors.toList());

            // 导出Excel
            ExcelWriterUtils.write("留样观察台账", response,
                Collections.singletonList(new SheetDataBo("留样观察台账", RetentionObservationLedgerExportVO.class, exportList, null)));
        } catch (Exception e) {
            log.error("导出留样观察台账失败", e);
            throw new BmosException(LimsResponseCode.RETENTION_LEDGER_EXPORT_ERROR);
        }
    }
}
