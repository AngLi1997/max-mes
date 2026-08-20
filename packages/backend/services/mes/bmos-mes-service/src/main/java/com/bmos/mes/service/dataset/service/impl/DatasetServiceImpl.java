package com.bmos.mes.service.dataset.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.dataset.convert.DatasetConvert;
import com.bmos.mes.service.dataset.convert.DatasetPointConvert;
import com.bmos.mes.service.dataset.dto.*;
import com.bmos.mes.service.dataset.enums.DatasetDynamicReportDataType;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mes.service.dataset.mapper.IDatasetCategoryMapper;
import com.bmos.mes.service.dataset.mapper.IDatasetMapper;
import com.bmos.mes.service.dataset.mapper.IDatasetPointMapper;
import com.bmos.mes.service.dataset.model.Dataset;
import com.bmos.mes.service.dataset.model.DatasetCategory;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.dataset.service.IDatasetService;
import com.bmos.mes.service.dataset.vo.*;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.PlatformCodeConstants;
import com.bmos.mes.service.platform.code.dto.BatchConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.BatchNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.ConfirmNextUseCodeDTO;
import com.bmos.mes.service.platform.code.dto.NextUseCodeDTO;
import com.bmos.mes.service.platform.code.feign.PlatformCodeFeign;
import com.bmos.mes.service.platform.code.vo.BatchNextCodeVO;
import com.bmos.mes.service.process.mapper.ProcessMapper;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据集service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 14:46
 */
@Service
@Slf4j
public class DatasetServiceImpl implements IDatasetService {

    private static final String LOG_PREFIX = "[数据集]";

    @Resource
    private IDatasetMapper datasetMapper;

    @Resource
    private IDatasetCategoryMapper datasetCategoryMapper;

    @Resource
    private IDatasetPointMapper datasetPointMapper;

    @Resource
    private PlatformCodeFeign platformCodeFeign;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProductMaterialMapper productMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDataset(DatasetCreateDTO dto) {

        log.info("{} 创建数据集:{}", LOG_PREFIX, dto);
        if (datasetMapper.existByName(dto.getName())) {
            throw new BmosException(MesResponseCode.DATASET_NAME_EXIST);
        }

        Dataset dataset = new Dataset();
        dataset.setDatasetCategoryId(dto.getDatasetCategoryId());
        dataset.setName(dto.getName());
        dataset.setType(CommonEnum.getEnumByValue(DatasetType.class, dto.getDatasetType()));
        dataset.setProductId(dto.getProductId());
        dataset.setProcessId(dto.getProcessId());
        dataset.setDatasetKey(getDatasetSerial());
        datasetMapper.insert(dataset);

        List<DatasetPoint> dps;
        if (dataset.getType() == DatasetType.POINT) {
            dps = generateRecordDataPoints(dto, dataset);
        } else if (dataset.getType() == DatasetType.DYNAMIC_REPORT) {
            Set<String> collect = dto.getDatasetDynamicReportDataList().stream()
                    .map(DatasetDynamicReportDataCreateDTO::getDataName)
                    .collect(Collectors.toSet());
            if (collect.size() != dto.getDatasetDynamicReportDataList().size()){
                throw new BmosException(MesResponseCode.DATASET_DYNAMIC_REPORT_DATA_NAME_DUPLICATE);
            }
            dps = generateDynamicDatasetPoints(dto, dataset);
        } else if (dataset.getType() == DatasetType.LOT_RELEASE_LINK) {
            dps = generateLotReleaseLinkDatasetPoints(dto, dataset);
        } else {
            throw new IllegalArgumentException("参数错误");
        }

        if (CollectionUtil.isNotEmpty(dps)) {
            datasetPointMapper.insertBatch(dps);
            // 确认数据点流水号
            confirmDatasetPointSerial(dps.stream().map(DatasetPoint::getDatasetPointKey).collect(Collectors.toList()));
        }
        // 确认数据集流水号
        confirmDatasetSerial(dataset.getDatasetKey());
        return dataset.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long editDataset(DatasetEditDTO dto) {
        log.info("{} 编辑数据集:{}", LOG_PREFIX, dto);
        Dataset dataset = datasetMapper.selectById(dto.getId());
        if (dataset == null) {
            throw new BmosException(MesResponseCode.DATASET_NOT_EXIST);
        }
        editDataPoints(dto, dataset);
        return dataset.getId();
    }

    @Override
    public DatasetPointDataPreviewVO previewDatasetPointData(DatasetPointDataPreviewPageQuery dto) {
        // 查询启用的工艺版本
        Process process = processMapper.selectById(dto.getProcessId());
        if (process == null){
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        if (StrUtil.isBlank(process.getActiveVersion())){
            throw new BmosException(MesResponseCode.PROCESS_RELATION_RECORD_NON_CONFIRM);
        }
        DatasetPointDataPreviewVO data = new DatasetPointDataPreviewVO();
        List<DatasetPointDataPreviewTitleVO> titles = datasetPointMapper.previewDatasetPointDataTitle(dto, process.getActiveVersion());
        data.setTitles(titles);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<DatasetPointDataPreviewPageVO> list = datasetPointMapper.previewDatasetPointData(dto);
        CommonPage<DatasetPointDataPreviewPageVO> page = CommonPage.convertPage(list);
        data.setPage(page);
        return data;
    }

    @Override
    public DatasetPointDataPreviewListVO previewDatasetPointListData(DatasetPointDataPreviewPageQuery dto) {
        // 查询启用的工艺版本
        Process process = processMapper.selectById(dto.getProcessId());
        if (process == null){
            throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);
        }
        if (StrUtil.isBlank(process.getActiveVersion())){
            throw new BmosException(MesResponseCode.PROCESS_RELATION_RECORD_NON_CONFIRM);
        }
        DatasetPointDataPreviewListVO data = new DatasetPointDataPreviewListVO();
        List<DatasetPointDataPreviewTitleVO> titles = datasetPointMapper.previewDatasetPointDataTitle(dto, process.getActiveVersion());
        data.setTitles(titles);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<DatasetPointDataPreviewPageVO> list = datasetPointMapper.previewDatasetPointData(dto);
        data.setList(list);
        return data;
    }

    @Override
    public List<DatasetPoint> listByFieldIdsAndProcedureStepIds(List<Long> fieldIds, List<Long> procedureStepIds) {
        return datasetPointMapper.listByFieldIdsAndProcedureStepIds(fieldIds, procedureStepIds);
    }

    @Override
    public CommonPage<DatasetSimpleVO> queryDatasetPage(DatasetPageQuery pageQuery) {
        List<Long> datasetCategoryIds = datasetCategoryMapper.listAllChildren(pageQuery.getDatasetCategoryId())
                .stream()
                .map(DatasetCategory::getId)
                .collect(Collectors.toList());
        datasetCategoryIds.add(pageQuery.getDatasetCategoryId());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<Dataset> datasets = datasetMapper.queryDatasetPage(pageQuery, datasetCategoryIds);
        CommonPage<Dataset> datasetCommonPage = CommonPage.convertPage(datasets);
        return DatasetConvert.INSTANCE.convertToVO(datasetCommonPage);
    }

    @Override
    public CommonPage<DatasetPointPageVO> queryDatasetPointPage(DatasetPointPageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<DatasetPoint> list = datasetPointMapper.queryDatasetPointPage(pageQuery);
        CommonPage<DatasetPoint> page = CommonPage.convertPage(list);
        return DatasetPointConvert.INSTANCE.convertToVO(page);
    }

    @Override
    public DatasetVO queryDatasetDetail(Long id) {
        Dataset dataset = datasetMapper.selectById(id);
        if (dataset == null){
            throw new BmosException(MesResponseCode.DATASET_NOT_EXIST);
        }
        DatasetVO result = DatasetConvert.INSTANCE.convertToVO(dataset);
        result.setProcessName(Optional.ofNullable(result.getProcessId())
                .map(pcsId -> processMapper.selectById(pcsId))
                .map(Process::getName)
                .orElse(null)
        );
        result.setProductName(Optional.ofNullable(result.getProductId())
                .map(prodId -> productMapper.selectById(prodId))
                .map(ProductMaterial::getName)
                .orElse(null)
        );
        List<DatasetPoint> datasetPoints = datasetPointMapper.listByDatasetId(id);
        if (CollectionUtil.isEmpty(datasetPoints)){
            return result;
        }
        if (Objects.equals(dataset.getType(), DatasetType.POINT)){
            result.setDatasetPointList(DatasetPointConvert.INSTANCE.convertToPointVO(datasetPoints));
        }else if (Objects.equals(dataset.getType(), DatasetType.DYNAMIC_REPORT)){
            result.setDatasetDynamicReportDataList(DatasetPointConvert.INSTANCE.convertToDynamicReportVO(datasetPoints));
        }else if (Objects.equals(dataset.getType(), DatasetType.LOT_RELEASE_LINK)){
            result.setDatasetLotReleaseLinkList(DatasetPointConvert.INSTANCE.convertToLotReleaseLinkVO(datasetPoints));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataset(Long id) {
        Dataset dataset = datasetMapper.selectById(id);
        if (dataset == null){
            throw new BmosException(MesResponseCode.DATASET_NOT_EXIST);
        }
        datasetMapper.deleteById(dataset);
        List<DatasetPoint> datasetPoints = datasetPointMapper.selectByDatasetId(id);
        if (CollectionUtil.isNotEmpty(datasetPoints)){
            datasetPointMapper.deleteBatchIds(datasetPoints.stream().map(DatasetPoint::getId).collect(Collectors.toList()));
        }
    }

    @Override
    public List<Dataset> queryByDataSetKeyList(Collection<String> dataSet) {
        return datasetMapper.selectByDataSetKeyList(dataSet);
    }

    @Override
    public List<DatasetSimpleVO> queryByProcessIdAndType(Long processId, DatasetType datasetType) {
        if (processId == null){
            return new ArrayList<>();
        }
        List<Dataset> list = datasetMapper.queryByProcessIdAndType(processId, datasetType);
        return DatasetConvert.INSTANCE.convertToVO(list);
    }

    /**
     * 编辑数据点
     * @param dto 编辑参数
     * @param dataset 数据集
     */
    private void editDataPoints(DatasetEditDTO dto, Dataset dataset) {

        // 新增列表
        List<DatasetPoint> addList = new ArrayList<>();
        // 更新列表
        List<DatasetPoint> editList = new ArrayList<>();
        // 比对删除的和编辑的
        List<Long> ids = dto.getList().stream()
                .map(DatasetPointEditBaseDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(ids)) {
            // 编辑的
            List<DatasetPoint> editDatasetPoints = datasetPointMapper.selectBatchIds(ids);
            if (editDatasetPoints.size() != ids.size()) {
                throw new BmosException(MesResponseCode.DATASET_POINT_NOT_EXIST);
            }
            for (DatasetPoint editDatasetPoint : editDatasetPoints) {
                Map<Long, DatasetPointEditBaseDTO> map = dto.getList().stream()
                        .filter(item -> item.getId() != null)
                        .collect(Collectors.toMap(DatasetPointEditBaseDTO::getId, Function.identity(), (v1, v2) -> v2));
                DatasetPointEditBaseDTO datasetPointEditBaseDTO = map.get(editDatasetPoint.getId());
                if (datasetPointEditBaseDTO != null) {
                    if (datasetPointEditBaseDTO.compare(editDatasetPoint)){
                        datasetPointEditBaseDTO.copyToDatasetPoint(editDatasetPoint);
                        editList.add(editDatasetPoint);
                    }
                }
            }
        }
        // 添加的
        List<DatasetPointEditBaseDTO> createDTOs = dto.getList().stream()
                .filter(item -> item.getId() == null)
                .collect(Collectors.toList());
        List<String> serials = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(createDTOs)) {
            serials = getDatasetPointSerial(createDTOs.size());
            AtomicInteger i = new AtomicInteger();
            List<String> finalSerials = serials;
            List<DatasetPoint> newDatasetPoints = createDTOs.stream()
                    .map(item -> {
                        DatasetPoint datasetPoint = new DatasetPoint(dataset.getId(), dataset.getDatasetKey(), dataset.getType(), finalSerials.get(i.getAndIncrement()));
                        item.copyToDatasetPoint(datasetPoint);
                        return datasetPoint;
                    })
                    .collect(Collectors.toList());
            addList.addAll(newDatasetPoints);
        }
        if (CollectionUtil.isNotEmpty(dto.getRemoveDatasetPointIds())) {
            log.info("删除了数据点:{}", dto.getRemoveDatasetPointIds());
            datasetPointMapper.deleteBatchIds(dto.getRemoveDatasetPointIds());
        }
        if (CollectionUtil.isNotEmpty(editList)) {
            log.info("修改了数据点:{}", editList);
            datasetPointMapper.updateBatch(editList);
        }
        if (CollectionUtil.isNotEmpty(addList)) {
            log.info("新增了数据点:{}", addList);
            datasetPointMapper.insertBatch(addList);
        }
        // 确认新增的数据点流水号
        confirmDatasetPointSerial(serials);
    }

    /**
     * 生成批签发引用数据点
     *
     * @param dto     参数
     * @param dataset 数据集
     * @return 数据点
     */
    private List<DatasetPoint> generateLotReleaseLinkDatasetPoints(DatasetCreateDTO dto, Dataset dataset) {
        AtomicInteger i = new AtomicInteger();
        List<DatasetLotReleaseLinkCreateDTO> pointList = dto.getDatasetLotReleaseLinkList();
        if (CollectionUtil.isEmpty(pointList)) {
            return new ArrayList<>();
        }
        List<String> serials = getDatasetPointSerial(pointList.size());
        return pointList.stream()
                .map(p -> DatasetPoint.buildLotReleaseLink(dataset.getId(),
                        dataset.getDatasetKey(),
                        p.getLotReleaseName(),
                        serials.get(i.getAndIncrement()),
                        p.getLotReleaseTemplateId(),
                        p.getLotReleaseVersion(),
                        p.getLinkArea())
                ).collect(Collectors.toList());
    }

    /**
     * 生成记录数据点
     *
     * @param dto     参数
     * @param dataset 数据集
     * @return 数据点
     */
    private List<DatasetPoint> generateRecordDataPoints(DatasetCreateDTO dto, Dataset dataset) {
        AtomicInteger i = new AtomicInteger();
        // 批记录值
        List<DatasetPointCreateDTO> pointList = dto.getDatasetPointList();
        if (CollectionUtil.isEmpty(pointList)) {
            return new ArrayList<>();
        }
        List<String> serials = getDatasetPointSerial(pointList.size());
        return pointList.stream()
                .map(p -> DatasetPoint.buildRecord(
                        dataset.getId(),
                        dataset.getDatasetKey(),
                        p.getName(),
                        serials.get(i.getAndIncrement()), p)
                ).collect(Collectors.toList());
    }

    /**
     * 生成动态报表数据点
     *
     * @param dto     参数
     * @param dataset 数据集
     * @return 数据点
     */
    private List<DatasetPoint> generateDynamicDatasetPoints(DatasetCreateDTO dto, Dataset dataset) {
        AtomicInteger i = new AtomicInteger();
        List<DatasetDynamicReportDataCreateDTO> pointList = dto.getDatasetDynamicReportDataList();
        if (CollectionUtil.isEmpty(pointList)) {
            return new ArrayList<>();
        }
        List<String> serials = getDatasetPointSerial(pointList.size());
        return pointList.stream()
                .map(p -> DatasetPoint.buildDynamicReportRecord(dataset.getId(),
                        dataset.getDatasetKey(),
                        p.getDataName(),
                        serials.get(i.getAndIncrement()),
                        CommonEnum.getEnumByValue(DatasetDynamicReportDataType.class, p.getDataType()),
                        p.getDefaultValue())
                ).collect(Collectors.toList());
    }

    private String getDatasetSerial() {
        return FeignUtils.handleRequest(data -> platformCodeFeign.getNextUseNo(data), NextUseCodeDTO.builder()
                        .code(PlatformCodeConstants.DATASET_KEY_SERIAL)
                        .fields(new HashMap<>())
                        .build())
                .getData().getNo();
    }

    private void confirmDatasetSerial(String serial) {
        if (StrUtil.isBlank(serial)){
            return;
        }
        FeignUtils.handleRequest(data -> platformCodeFeign.confirmNo(data), ConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.DATASET_KEY_SERIAL)
                .fullNo(serial)
                .fields(new HashMap<>())
                .build());
    }


    private List<String> getDatasetPointSerial(int size) {
        return FeignUtils.handleRequest(data -> platformCodeFeign.getBatchNextUseNo(data), BatchNextUseCodeDTO.builder()
                        .code(PlatformCodeConstants.DATASET_POINT_KEY_SERIAL)
                        .fields(new HashMap<>())
                        .num(size)
                        .build())
                .getData().getNos()
                .stream().map(BatchNextCodeVO.NextCodeVO::getNo).collect(Collectors.toList());
    }

    private void confirmDatasetPointSerial(List<String> serials) {
        if (CollectionUtil.isEmpty(serials)){
            return;
        }
        FeignUtils.handleRequest(data -> platformCodeFeign.batchConfirmNo(data), BatchConfirmNextUseCodeDTO.builder()
                .code(PlatformCodeConstants.DATASET_POINT_KEY_SERIAL)
                .fullNos(serials)
                .fields(new HashMap<>())
                .build());
    }
}
