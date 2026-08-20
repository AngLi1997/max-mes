package com.bmos.mes.service.lotsummary.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.dataset.dto.DatasetPointDataPreviewPageQuery;
import com.bmos.mes.service.dataset.mapper.IDatasetPointMapper;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.dataset.vo.DatasetPointDataPreviewPageVO;
import com.bmos.mes.service.dataset.vo.DatasetPointDataPreviewTitleVO;
import com.bmos.mes.service.lotsummary.convert.LotSummaryConvert;
import com.bmos.mes.service.lotsummary.dto.LotSummaryCreateDTO;
import com.bmos.mes.service.lotsummary.dto.LotSummaryEditDTO;
import com.bmos.mes.service.lotsummary.dto.LotSummaryPageQuery;
import com.bmos.mes.service.lotsummary.dto.LotSummaryProductDataPageQuery;
import com.bmos.mes.service.lotsummary.handler.CustomCellWriteHandler;
import com.bmos.mes.service.lotsummary.mapper.ILotSummaryItemMapper;
import com.bmos.mes.service.lotsummary.mapper.ILotSummaryMapper;
import com.bmos.mes.service.lotsummary.model.LotSummary;
import com.bmos.mes.service.lotsummary.model.LotSummaryItem;
import com.bmos.mes.service.lotsummary.service.ILotSummaryService;
import com.bmos.mes.service.lotsummary.vo.*;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 批次摘要 service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 14:05
 */
@Service
@Slf4j
public class LotSummaryServiceImpl implements ILotSummaryService {

    private static final String LOT_PREFIX = "[批次摘要]";

    @Resource
    private ILotSummaryMapper lotSummaryMapper;

    @Resource
    private ILotSummaryItemMapper lotSummaryItemMapper;

    @Resource
    private ProductMaterialService productMaterialService;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private IDatasetPointMapper datasetPointMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createLotSummary(LotSummaryCreateDTO dto) {
        log.info("{}创建批次摘要:{}", LOT_PREFIX, dto);
        if (lotSummaryMapper.existsName(dto.getName())) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NAME_EXIST);
        }

        Process process = processMapper.selectById(dto.getProcessId());
        if (process == null) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }

        ProductMaterial productMaterial = productMaterialService.selectById(dto.getProductId());
        if (productMaterial == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }
        LotSummary lotSummary = new LotSummary();
        lotSummary.setName(dto.getName());
        lotSummary.setProductId(dto.getProductId());
        lotSummary.setProcessId(dto.getProcessId());
        lotSummary.setProductName(productMaterial.getName());
        lotSummary.setProcessName(process.getName());
        lotSummaryMapper.insert(lotSummary);
        List<LotSummaryItem> list = LotSummaryConvert.INSTANCE.convertTODO(dto.getList());
        list.forEach(item -> item.setLotSummaryId(lotSummary.getId()));
        lotSummaryItemMapper.insertBatch(list);
    }

    @Override
    public CommonPage<LotSummaryPageVO> queryPage(LotSummaryPageQuery pageQuery) {
        List<Long> productIds;
        // 产品树
        List<ProductCategoryTreeNodeVO> productList = productMaterialService.getProductListCondition(pageQuery.getProductId(), pageQuery.getProductCategoryId());
        productIds = productList == null ? null : productList.stream().map(ProductCategoryTreeNodeVO::getId).collect(Collectors.toList());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<LotSummaryPageVO> list = lotSummaryMapper.queryPage(pageQuery, productIds);
        return CommonPage.convertPage(list);
    }

    @Override
    public LotSummaryDetailVO queryDetail(Long id) {
        LotSummary lotSummary = lotSummaryMapper.selectById(id);
        if (lotSummary == null) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NOT_EXIST);
        }
        List<LotSummaryDetailItemVO> list = lotSummaryItemMapper.selectVOByLotSummaryId(id);
        return LotSummaryConvert.INSTANCE.convertDetailVO(lotSummary, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editLotSummary(LotSummaryEditDTO dto) {
        log.info("{}编辑批次摘要:{}", LOT_PREFIX, dto);
        LotSummary lotSummary = lotSummaryMapper.selectById(dto.getId());
        if (lotSummary == null) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NOT_EXIST);
        }
        if (!StrUtil.equals(lotSummary.getName(), dto.getName()) && lotSummaryMapper.existsName(dto.getName())) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NAME_EXIST);
        }

        Process process = processMapper.selectById(dto.getProcessId());
        if (process == null) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }

        ProductMaterial productMaterial = productMaterialService.selectById(dto.getProductId());
        if (productMaterial == null) {
            throw new BmosException(MesResponseCode.MATERIAL_NOT_EXISTED);
        }

        lotSummaryItemMapper.deleteByLotSummaryId(dto.getId());
        List<LotSummaryItem> list = LotSummaryConvert.INSTANCE.convertTODO(dto.getList());
        list.forEach(item -> item.setLotSummaryId(lotSummary.getId()));
        lotSummaryItemMapper.insertBatch(list);

        lotSummary.setName(dto.getName());
        lotSummary.setProductId(dto.getProductId());
        lotSummary.setProcessId(dto.getProcessId());
        lotSummary.setProductName(productMaterial.getName());
        lotSummary.setProcessName(process.getName());
        lotSummaryMapper.updateById(lotSummary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLotSummary(Long id) {
        log.info("{}删除批次摘要:{}", LOT_PREFIX, id);
        LotSummary lotSummary = lotSummaryMapper.selectById(id);
        if (lotSummary == null) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NOT_EXIST);
        }
        lotSummaryItemMapper.deleteByLotSummaryId(id);
        lotSummaryMapper.deleteById(lotSummary);
    }

    @Override
    public LotSummaryProductData queryProductDataPage(LotSummaryProductDataPageQuery pageQuery) {
        LotSummary lotSummary = lotSummaryMapper.selectById(pageQuery.getLotSummaryId());
        if (lotSummary == null) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NOT_EXIST);
        }
        List<LotSummaryItem> lotSummaryItemList = lotSummaryItemMapper.selectByLotSummaryId(lotSummary.getId());
        List<Long> datasetPointIds = lotSummaryItemList.stream()
                .map(LotSummaryItem::getDatasetPointId)
                .collect(Collectors.toList());
        // 数据集
        List<DatasetPoint> datasetPoints = datasetPointMapper.selectBatchIds(datasetPointIds);
        Map<Long, DatasetPoint> longDatasetPointMap = CollectionUtils.convertMap(datasetPoints, DatasetPoint::getId, Function.identity());

        Long processId = lotSummary.getProcessId();
        Process process = processMapper.selectById(processId);
        if (process == null) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        pageQuery.setPoints(datasetPoints.stream()
                .map(item -> new DatasetPointDataPreviewPageQuery.Point(item.getProcedureStepId(), item.getFieldId()))
                .collect(Collectors.toList()));
        pageQuery.setProcessId(processId);
        // 查询动态标题
        List<DatasetPointDataPreviewTitleVO> result = lotSummaryItemList.stream()
                .map(item -> {
                    DatasetPointDataPreviewTitleVO vo = new DatasetPointDataPreviewTitleVO();
                    vo.setLotReleaseSummaryItemId(item.getId());
                    vo.setName(item.getLabelName());
                    DatasetPoint datasetPoint = longDatasetPointMap.get(item.getDatasetPointId());
                    if (datasetPoint != null) {
                        vo.setFieldId(datasetPoint.getFieldId());
                        vo.setProcedureStepId(datasetPoint.getProcedureStepId());
                    }
                    return vo;
                }).sorted(Comparator.comparing(DatasetPointDataPreviewTitleVO::getLotReleaseSummaryItemId)).collect(Collectors.toList());
        // 查询动态数据
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<DatasetPointDataPreviewPageVO> list = datasetPointMapper.previewDatasetPointData(pageQuery);
        CommonPage<DatasetPointDataPreviewPageVO> page = CommonPage.convertPage(list);
        return new LotSummaryProductData(result, page);
    }

    @Override
    public void exportProductDataPage(HttpServletResponse response, LotSummaryProductDataPageQuery pageQuery) throws IOException {
        log.info("{}导出批次摘要生产数据", LOT_PREFIX);

        LotSummary lotSummary = lotSummaryMapper.selectById(pageQuery.getLotSummaryId());
        if (lotSummary == null) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NOT_EXIST);
        }
        List<LotSummaryItem> lotSummaryItemList = lotSummaryItemMapper.selectByLotSummaryId(lotSummary.getId());

        List<Long> datasetPointIds = lotSummaryItemList.stream()
                .map(LotSummaryItem::getDatasetPointId)
                .collect(Collectors.toList());
        // 数据集
        List<DatasetPoint> datasetPoints = datasetPointMapper.selectBatchIds(datasetPointIds);

        Map<Long, String> dpIdKeyMap = datasetPoints.stream()
                .collect(Collectors.toMap(DatasetPoint::getId, item -> item.getProcedureStepId() + "_" + item.getFieldId(), (v1, v2) -> v2));

        Long processId = lotSummary.getProcessId();
        Process process = processMapper.selectById(processId);
        if (process == null) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        pageQuery.setPoints(datasetPoints.stream()
                .map(item -> new DatasetPointDataPreviewPageQuery.Point(item.getProcedureStepId(), item.getFieldId()))
                .collect(Collectors.toList()));
        pageQuery.setProcessId(processId);

        // 查询动态数据
        List<DatasetPointDataPreviewPageVO> list = datasetPointMapper.previewDatasetPointData(pageQuery);

        List<List<String>> data = new ArrayList<>();

        for (DatasetPointDataPreviewPageVO item : list) {
            Map<String, String> fieldMap = item.getData()
                    .stream()
                    .collect(Collectors.toMap(f -> f.getProcedureStepId() + "_" + f.getFieldId(),
                            DatasetPointDataPreviewPageVO.FieldInfo::getValue, (v1, v2) -> v2));
            List<String> datum = new ArrayList<>();
            datum.add(item.getBatchNo());
            for (LotSummaryItem lotSummaryItem : lotSummaryItemList) {
                String key = dpIdKeyMap.get(lotSummaryItem.getDatasetPointId());
                if (StrUtil.isNotBlank(key)){
                    datum.add(fieldMap.getOrDefault(key, ""));
                }else {
                    datum.add("");
                }
            }
            data.add(datum);
        }
        List<List<String>> titlesList = lotSummaryItemList.stream()
                .map(LotSummaryItem::getLabelName)
                .map(ListUtil::of)
                .collect(Collectors.toList());
        titlesList.add(0, ListUtil.of("批号"));
        EasyExcel.write(response.getOutputStream())
                .head(titlesList)
                .registerWriteHandler(new CustomCellWriteHandler())
                .sheet("Sheet1")
                .doWrite(data);
    }

    @Override
    public LotSummaryProductListData queryProductDataList(LotSummaryProductDataPageQuery pageQuery) {
        LotSummary lotSummary = lotSummaryMapper.selectById(pageQuery.getLotSummaryId());
        if (lotSummary == null) {
            throw new BmosException(MesResponseCode.LOT_SUMMARY_NOT_EXIST);
        }
        List<LotSummaryItem> lotSummaryItemList = lotSummaryItemMapper.selectByLotSummaryId(lotSummary.getId());
        List<Long> datasetPointIds = lotSummaryItemList.stream()
                .map(LotSummaryItem::getDatasetPointId)
                .collect(Collectors.toList());
        // 数据集
        List<DatasetPoint> datasetPoints = datasetPointMapper.selectBatchIds(datasetPointIds);
        Map<Long, DatasetPoint> longDatasetPointMap = CollectionUtils.convertMap(datasetPoints, DatasetPoint::getId, Function.identity());

        Long processId = lotSummary.getProcessId();
        Process process = processMapper.selectById(processId);
        if (process == null) {
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        pageQuery.setPoints(datasetPoints.stream()
                .map(item -> new DatasetPointDataPreviewPageQuery.Point(item.getProcedureStepId(), item.getFieldId()))
                .collect(Collectors.toList()));
        pageQuery.setProcessId(processId);
        // 查询动态标题
        List<DatasetPointDataPreviewTitleVO> result = lotSummaryItemList.stream()
                .map(item -> {
                    DatasetPointDataPreviewTitleVO vo = new DatasetPointDataPreviewTitleVO();
                    vo.setLotReleaseSummaryItemId(item.getId());
                    vo.setName(item.getLabelName());
                    DatasetPoint datasetPoint = longDatasetPointMap.get(item.getDatasetPointId());
                    if (datasetPoint != null) {
                        vo.setFieldId(datasetPoint.getFieldId());
                        vo.setProcedureStepId(datasetPoint.getProcedureStepId());
                    }
                    return vo;
                }).sorted(Comparator.comparing(DatasetPointDataPreviewTitleVO::getLotReleaseSummaryItemId)).collect(Collectors.toList());
        // 查询动态数据
        List<DatasetPointDataPreviewPageVO> list = datasetPointMapper.previewDatasetPointData(pageQuery);
        return new LotSummaryProductListData(result, list);
    }
}
