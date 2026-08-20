package com.bmos.mes.service.dataset.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mes.service.dataset.handle.DatasetTransDataBuilder;
import com.bmos.mes.service.dataset.handle.data.*;
import com.bmos.mes.service.dataset.util.options.ProcessTakePhotoData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 数据集组装DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 15:18
 */
@Data
public class DatasetTrans {

    private static final Logger log = LoggerFactory.getLogger(DatasetTrans.class);

    /**
     * 生产计划id
     */
    private Long planId;

    /**
     * 生产批次号
     */
    private String batchNo;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 数据集
     */
    private DatasetTransData data;

    /**
     * 拍照数据
     */
    private ProcessTakePhotoData takePhotoDataList;

    /**
     * 从json组装数据
     *
     * @param json json字符串
     * @return DatasetTransData
     */
    public static List<DatasetTrans> loadFromJsonStr(String json) {
        return JsonUtils.parseArray(json, DatasetTrans.class);
    }

    /**
     * 根据表达式取值
     *
     * @param data       组装的数据
     * @param expression 表达式
     *                   eg.(#ds1.dp1#2.21)[0][0][0][0][0]
     * @return 值
     */
    public static DatasetTransValueData getValue(AssembleCompleteData data, String expression) {
        DatasetTransExpression dte = DatasetTransExpression.buildFromExpression(expression);
        if (CollectionUtil.isEmpty(dte.datasets)) {
            return null;
        }
        // 需要获取工艺排序的数据
        DatasetTrans datasetTrans = getPlanDataSetTrans(data, dte.batchIndex, dte.datasets);
        if (datasetTrans == null) {
            return null;
        }
        return datasetTrans.getValue(dte);
    }

    /**
     * 获取对应的工艺的数据
     *
     * @param data
     * @param batchIndex
     * @param datasets
     * @return
     */
    private static DatasetTrans getPlanDataSetTrans(AssembleCompleteData data, String batchIndex, List<DatasetTransExpressionDs> datasets) {
        // 1. 将数据通过工艺id进行分组
        Map<Long, List<DatasetTrans>> processDataMap = data.getDatasetTransList().stream()
                .collect(Collectors.groupingBy(DatasetTrans::getProcessId));
        if (CollUtil.isEmpty(datasets)) {
            return null;
        }
        Set<String> dataSetKeySet = datasets.stream().map(DatasetTransExpressionDs::getDatasetName).collect(Collectors.toSet());
        Map<String, Long> dataSetProcessIdMap = data.getDataSetProcessIdMap();
        Map<Long, DatasetTrans> datasetTransMap = new HashMap<>();
        Set<Long> processIdSet = new HashSet<>();
        for (String datSetKey : dataSetKeySet) {
            Long processId = dataSetProcessIdMap.get(datSetKey);
            if (Objects.isNull(processId)) {
                continue;
            }
            if (processIdSet.contains(processId)) {
                continue;
            }
            processIdSet.add(processId);
            List<DatasetTrans> cur = processDataMap.get(processId);
            if (CollectionUtil.isEmpty(cur)) {
                continue;
            }
            for (DatasetTrans datasetTrans : cur) {
                datasetTransMap.put(datasetTrans.getPlanId(), datasetTrans);
            }
        }
        // 在进行内部排序
        List<DatasetTrans> sortDatasetTransList = new ArrayList<>();
        for (DatasetTrans datasetTrans : data.getDatasetTransList()) {
            if (!datasetTransMap.containsKey(datasetTrans.getPlanId())) {
                continue;
            }
            sortDatasetTransList.add(datasetTransMap.get(datasetTrans.getPlanId()));
        }
        int batchIdx = 0;
        if (StrUtil.isNotEmpty(batchIndex)) {
            batchIdx = Integer.parseInt(batchIndex);
        } else {
            batchIdx = sortDatasetTransList.size();
        }
        if (batchIdx != 0) {
            batchIdx--;
        }
        if (batchIdx >= sortDatasetTransList.size()) {
            return null;
        }
        return sortDatasetTransList.get(batchIdx);
    }

    /**
     * 根据表达式取值
     *
     * @param dte 表达式
     *            eg.(#ds1.dp1#2.21)[0][0][0][0][0]
     * @return 值
     */
    private DatasetTransValueData getValue(DatasetTransExpression dte) {
        for (DatasetTransExpressionDs dataset : dte.datasets) {
            try{
                DatasetTransValueData value = Optional.of(this)
                        .map(DatasetTrans::getData)
                        .map(i -> i.get(dataset.datasetName))
                        .map(i -> i.get(dataset.dataPointName))
                        .map(i -> getChangeTeamData(i, dte.processShiftIndex, dte.procedureShiftIndex))
                        // 复制版本下标
                        .map(i -> getValueNotStruct(i, dte.copyVersionIndex))
                        // 数据下标(可能在组装的数据中包含列表的情况)
                        .map(i -> {
                            if (dte.valueIndex == null) {
                                return i;
                            } else {
                                try {return getValueNotStruct(JSON.parseArray(i.getValue(), String.class)
                                        .stream()
                                        .map(item -> new DatasetTransValueData(i.getType(), item, i.isEmpty()))
                                        .collect(Collectors.toList()), dte.valueIndex);}catch (Exception e){
                                    log.error("{} {}=>{}数据集格式校验错误,无法转换为数组", dataset.getDatasetName(), dataset.dataPointName, i.getValue(), e);
                                    return DatasetTransValueData.ERROR;
                                }
                            }
                        })
                        .orElse(null);
                if (value != null) {
                    return value;
                }
            } catch (Exception e){
                log.error("获取数据失败, 数据点：{}",  JSON.toJSONString(dataset),e);
                return null;
            }
        }
        return null;
    }

    private static List<DatasetTransValueData> getChangeTeamData(DatasetTransDpData datasetTransDpData, String processChangeNum, String procedureChangeNum) {
        try{
            if (Objects.isNull(datasetTransDpData)){
                return new ArrayList<>();
            }

            RowKeyTable<Integer, Integer, List<DatasetTransValueData>> values = datasetTransDpData.getNewValues();

            if (datasetTransDpData.getType() == DatasetType.DYNAMIC_REPORT){
                log.info("动态数据点 默认取第0个");
                return values.get(DatasetTransExpression.DEFAULT_INTEGER_INDEX, DatasetTransExpression.DEFAULT_INTEGER_INDEX);
            }
            if (datasetTransDpData.getReuse()){
                // 如果复用，则取值不关心换班
                return values.get(DatasetTransExpression.DEFAULT_INTEGER_INDEX, DatasetTransExpression.DEFAULT_INTEGER_INDEX);
            }
            if (StrUtil.isEmpty(processChangeNum)){
                processChangeNum = DatasetTransExpression.DEFAULT_INDEX;
            }
            if (StrUtil.isEmpty(procedureChangeNum)){
                procedureChangeNum = DatasetTransExpression.DEFAULT_INDEX;
            }
            Integer processChangeNumInt = Integer.parseInt(processChangeNum);
            Integer procedureChangeNumInt = Integer.parseInt(procedureChangeNum);
            return values.get(processChangeNumInt, procedureChangeNumInt);
        } catch (Exception e){
            log.error("获取数据失败, 数据集：{}, 工艺换班：{}, 工序换班：{}", JSON.toJSONString(datasetTransDpData), processChangeNum, procedureChangeNum, e);
        }
        return new ArrayList<>();
    }


    /**
     * 根据表达式取值
     *
     * @param data        锁组装的所有数据
     * @param expressions 表达式列表
     *                    eg.(#ds1.dp1#2.21)[0][0][0][0]
     * @return 表达式 -> 值
     */
    public static Map<String, DatasetTransValueData> getValuesMap(AssembleCompleteData data, Collection<String> expressions) {
        Map<String, DatasetTransValueData> resultMap = new HashMap<>();
        for (String expression : expressions) {
            DatasetTransValueData value = getValue(data, expression);
            if (value != null) {
                resultMap.put(expression, value);
            }
        }
        return resultMap;
    }

    private static <T> T getValueNotStruct(List<T> list, String indexStr) {
        if (StrUtil.isBlank(indexStr)) {
            if (CollectionUtil.isEmpty(list)) {
                return null;
            }
            return list.get(list.size() - 1);
        }
        int index = Integer.parseInt(indexStr);
        if (index >= list.size()) {
            return null;
        }
        return list.get(index);
    }

    private static List<String> matchRegex(String str, String regex) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public void addAllFormData(List<ExecuteFormLoadingData> executeFormLoadingData) {
        // 初始化一个空的数据集信息
        DatasetTransData datasetTransData = DatasetTransDataBuilder.build(executeFormLoadingData);
        this.setData(datasetTransData);
    }

    public void addAllDynamicData(List<DynamicRenderingData> dynamicRenderingData){
        if (CollUtil.isEmpty(dynamicRenderingData)){
            return ;
        }
        DatasetTransData datasetTransData = this.getData();
        for (DynamicRenderingData dynamicRenderingDatum : dynamicRenderingData) {
            List<DataSetPointHandleData> dataSetPointHandleDataList = dynamicRenderingDatum.getDataSetPointHandleDataList();
            if (CollUtil.isEmpty(dataSetPointHandleDataList)) {
                continue;
            }
            datasetTransData.putData(dataSetPointHandleDataList, dynamicRenderingDatum);
        }
    }

    public void sortData(PlanDataPointCopyVersion copyVersion) {
        this.getData().sort(copyVersion);
    }

    public void addAllPlanLoadingData(List<PlanLoadingData> planLoadingDataList) {
        if (CollUtil.isEmpty(planLoadingDataList)){
            return ;
        }
        DatasetTransData datasetTransData = this.getData();
        for (PlanLoadingData planLoadingData : planLoadingDataList) {
            List<DataSetPointHandleData> dataSetPointHandleDataList = planLoadingData.getDataSetPointHandleDataList();
            if (CollUtil.isEmpty(dataSetPointHandleDataList)) {
                continue;
            }
            datasetTransData.putData(dataSetPointHandleDataList, planLoadingData);
        }
    }

    public static class DatasetTransExpression {
        // 默认换班值
        public final static String DEFAULT_INDEX = "0";
        // 默认换班值
        public final static Integer DEFAULT_INTEGER_INDEX = 0;

        private List<DatasetTransExpressionDs> datasets;

        private String batchIndex;

        private String processShiftIndex;

        private String procedureShiftIndex;

        private String copyVersionIndex;

        private String valueIndex;

        /**
         * 判断值是否是列表数据
         * 配置[]为五个的时候为true 其他情况（4个）为false
         */
        @Getter
        private Boolean isList = false;

        public DatasetTransExpression(String expression) {

            if (StrUtil.isBlank(expression)) {
                return;
            }
            log.info("解析占位符:{}", expression);
            expression = expression.trim();
            List<DatasetTransExpressionDs> dsList = new ArrayList<>();
            if (expression.startsWith("${") && expression.endsWith("}")) {
                String exp = expression.substring(2, expression.length() - 1);
                // 处理前缀
                List<String> prefix = matchRegex(exp, "(?<=\\()(.*?)(?=\\))");
                if (prefix.size() != 1) {
                    // 表达式中找到多个小括号
                    throw new RuntimeException("表达式格式错误(表达式中找到多个小括号)");
                }
                String[] split = prefix.get(0).split(",");
                if (ArrayUtil.isEmpty(split)) {
                    // 表达式中小括号没有值
                    throw new RuntimeException("表达式格式错误(表达式中小括号没有值)");
                }
                for (String s : split) {
                    if (s.startsWith("#")) {
                        s = s.replaceAll("#(.*?)#", "");
                    }
                    String[] dsAndDp = s.split("\\.");
                    if (ArrayUtil.isEmpty(dsAndDp)) {
                        // 数据集/数据点配置错误
                        throw new RuntimeException("表达式格式错误(数据集/数据点配置错误)");
                    }
                    // 数据集
                    String ds = dsAndDp[0];
                    // 数据点
                    String dp = dsAndDp[1];
                    if (StrUtil.isNotBlank(ds) && StrUtil.isNotBlank(dp)) {
                        dsList.add(new DatasetTransExpressionDs(ds, dp));
                    }
                }
                // 处理后缀
                List<String> suffix = matchRegex(exp, "(?<=\\[)(.*?)(?=\\])");
                if (suffix.size() == 4 || suffix.size() == 5) {
                    this.batchIndex = suffix.get(0);
                    this.processShiftIndex = suffix.get(1);
                    this.procedureShiftIndex = suffix.get(2);
                    this.copyVersionIndex = suffix.get(3);
                } else {
                    throw new RuntimeException("表达式后缀数量错误");
                }
                if (suffix.size() == 5) {
                    this.valueIndex = suffix.get(4);
                    isList = true;
                }
            } else {
                throw new RuntimeException("表达式格式错误(要以‘${’开始以‘}’结束)");
            }
            this.datasets = dsList;
        }

        public static DatasetTransExpression buildFromExpression(String expression) {
            return new DatasetTransExpression(expression);
        }
    }

    @AllArgsConstructor
    @Getter
    private static class DatasetTransExpressionDs {

        private String datasetName;

        private String dataPointName;
    }

}
