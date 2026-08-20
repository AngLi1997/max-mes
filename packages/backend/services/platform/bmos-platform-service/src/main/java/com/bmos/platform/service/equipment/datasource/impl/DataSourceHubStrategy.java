package com.bmos.platform.service.equipment.datasource.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.equipment.service.dto.DataPointNameValueDTO;
import com.bmos.platform.service.equipment.datasource.DataSourceStrategy;
import com.bmos.platform.service.equipment.datasource.config.HubProperties;
import com.bmos.platform.service.equipment.datasource.consts.HubRequestFields;
import com.bmos.platform.service.equipment.datasource.dto.*;
import com.bmos.platform.service.equipment.service.enums.AcquisitionAddressEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.execute.parameter.service.BusinessParameterService;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.execute.parameter.vo.AcquisitionProperties;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据源策略-从hub获取
 * <p>
 * 目前hub对接过程
 * 1.通过用户名和密码获取token
 * 2.后续请求将token设置到请求头上
 *
 * @author yigaohui
 * @date hub数据源策略
 **/
@Slf4j
@Service
@ConditionalOnBean({HubProperties.class})
public class DataSourceHubStrategy implements DataSourceStrategy {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private RestTemplate restTemplate = new RestTemplate();

    @Resource
    private BusinessParameterService parameterService;


    @Override
    public DataPointNameValueDTO getData(String dataPointName) {
        return this.getData(Sets.newHashSet(dataPointName)).get(0);
    }

    @Override
    public List<DataPointNameValueDTO> getData(Set<String> dataPointNames) {
        JSONObject data = this.getDataInner(dataPointNames);
        return dataPointNames.stream().map(dataPointName -> {
            HubDataPointValueDTO value = data.getBean(dataPointName, HubDataPointValueDTO.class);
            if (value == null) {
                log.info("从hub中没有获取到点位{}的数据", dataPointName);
                return new DataPointNameValueDTO().setDataPointName(dataPointName).setValue(null).setTimeStamp(null);
            } else {
                log.info("hub获取到的数据:{}",value);
                return new DataPointNameValueDTO().setDataPointName(dataPointName).setValue(value.getV()).setTimeStamp(value.getTs());
            }
        }).collect(Collectors.toList());
    }


    private JSONObject getDataInner(Set<String> dataPoints) throws BmosException {
        String getDataPath = getHubProperties().getPath().getGetData();
        HttpHeaders headers = this.getHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        URI uri =
                UriComponentsBuilder.fromHttpUrl(getCompletePath(getDataPath)).queryParam(HubRequestFields.GET_DATA_TAG_NAMES, dataPoints).build().encode().toUri();
        ResponseEntity<HubResponseBaseDTO> dataResponse;
        try {
            log.info("从hub获取数据{}:{}", uri, httpEntity);
            dataResponse = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HubResponseBaseDTO.class);
        } catch (Exception e) {
            log.error("获取数据失败", e);
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_CONNECTION_ERROR);
        }
        if (dataResponse.getBody() == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_RESPONSE_ERROR);
        }
        log.info("从hub获取数据结束:{}", dataResponse);
        this.judgeHubResponseCode(dataResponse.getBody());
        return new JSONObject(dataResponse.getBody().getData());
    }
    /**
     * @Author: Ren Jin Guang
     * @Description: 通过code查询获取数采参数配置
     * @Return:
     * @Date: 2024-07-29 15:51:12
     */
    public HubProperties getHubProperties(){
        BusinessParameterDetailVO businessParameterDetailVO = parameterService.detailByCode(HubRequestFields.PARAMETER_CODE);
        if (ObjectUtil.isEmpty(businessParameterDetailVO)){
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_ADDRESS_ERROR);
        }
        AcquisitionProperties acquisitionProperties = JSONUtil.toBean(businessParameterDetailVO.getValue(), AcquisitionProperties.class);
        //获取hub配置信息
        if (ObjectUtil.isEmpty(acquisitionProperties.getHub())){
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_ADDRESS_ERROR);
        }
        return acquisitionProperties.getHub();
    }

    private String getAccessToken() {
        return getAccessTokenFromHub();
    }

    private String getAccessTokenFromHub() {
        HubProperties hubProperties = getHubProperties();
        String path = hubProperties.getPath().getGetAccessToken();
        HubAccessTokenRequestDTO hubAccessTokenRequestDTO = new HubAccessTokenRequestDTO();
        hubAccessTokenRequestDTO.setLoginName(hubProperties.getUsername());
        hubAccessTokenRequestDTO.setPassword(hubProperties.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<HubAccessTokenRequestDTO> httpEntity = new HttpEntity<>(hubAccessTokenRequestDTO,headers);
        ResponseEntity<HubResponseBaseDTO> accessTokenResponse;
        try {
            String completePath = getCompletePath(path);
            log.info("从hub中获取accessToken{}:{}", completePath, httpEntity);
            accessTokenResponse = restTemplate.postForEntity(completePath, httpEntity,
                    HubResponseBaseDTO.class);
        } catch (Exception e) {
            log.error("调用hub接口获取accessToken失败", e);
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_CONNECTION_ERROR);
        }
        if (accessTokenResponse.getBody() == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_RESPONSE_ERROR);
        }
        this.judgeHubResponseCode(accessTokenResponse.getBody());
        Object data = accessTokenResponse.getBody().getData();
        JSONObject entries = JSONUtil.parseObj(data);
        return entries.getStr("token");
    }

    private String getCompletePath(String path) {
        return getHubProperties().getEndpoint() + path;
    }

    private void judgeHubResponseCode(HubResponseBaseDTO responseBaseDTO) {
        if (!HubResponseBaseDTO.SUCCESS_CODE.equals(responseBaseDTO.getCode())) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_RESPONSE_STATUS_ERROR,
                    responseBaseDTO.getMessage());
        }
    }


    @Override
    public void writeDataPointValue(DataPointNameValueDTO dataPointWriteValueDTO) {
        this.writeDataPointValue(Lists.newArrayList(dataPointWriteValueDTO));
    }

    @Override
    public void writeDataPointValue(List<DataPointNameValueDTO> dataPointWriteValueDTOS) {
        List<HubDataPointWriteResponseDTO> hubDataPointWriteResponseDTOS =
                this.writeDataPointValueInner(dataPointWriteValueDTOS);
        List<HubDataPointWriteResponseDTO> writeError =
                hubDataPointWriteResponseDTOS.stream().filter(item -> !HubResponseBaseDTO.SUCCESS_CODE.equals(item.getCode())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(writeError)) {
            log.error(MessageFormat.format("hub 写入值失败{0}", JSONUtil.toJsonStr(writeError)));
            throw new BmosException(PlatformResponseCode.EQUIPMENT_DATA_POINT_WRITE_ERROR,
                    writeError.stream().map(HubDataPointWriteResponseDTO::getName).collect(Collectors.joining(",")));
        }
    }

    private List<HubDataPointWriteResponseDTO> writeDataPointValueInner(List<DataPointNameValueDTO> dataPointWriteValueDTOS) {
        String path = getHubProperties().getPath().getWriteData();
        HttpHeaders headers = this.getHeaders();
        List<HubDataPointValueDTO> hubDataPointValueDTOS =
                dataPointWriteValueDTOS.stream().map(item -> new HubDataPointValueDTO().setName(item.getDataPointName()).setV(item.getValue())).collect(Collectors.toList());
        HubDataPointWriteRequestDTO hubDataPointWriteRequestDTO = new HubDataPointWriteRequestDTO();
        hubDataPointWriteRequestDTO.setParams(hubDataPointValueDTOS);
        HttpEntity httpEntity = new HttpEntity(hubDataPointWriteRequestDTO, headers);
        ResponseEntity<HubResponseBaseDTO> dataResponse;
        try {
            dataResponse = restTemplate.exchange(getCompletePath(path), HttpMethod.PUT, httpEntity,
                    HubResponseBaseDTO.class);
        } catch (Exception e) {
            log.error("获取数据失败", e);
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_CONNECTION_ERROR);
        }
        if (dataResponse.getBody() == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_RESPONSE_ERROR);
        }
        this.judgeHubResponseCode(dataResponse.getBody());
        return JSONUtil.toList(JSONUtil.toJsonStr(dataResponse.getBody().getData()),
                HubDataPointWriteResponseDTO.class);
    }

    /**
     * 分页获取点位的历史数据
     *
     * @param dataPointName 点位名称
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param dataType
     * @param basePage      分页条件
     * @return hub的数据值
     */
    @Override
    public CommonPage<DataPointNameValueDTO> getHistory(String dataPointName, LocalDateTime startTime,
                                                        LocalDateTime endTime, AcquisitionPointDataTypeEnum dataType, BasePage basePage) {
        String path = getHubProperties().getPath().getTagUpHis();
        HttpHeaders headers = this.getHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        MultiValueMap<String, String> queryMap = this.getPageQueryParams(dataPointName, startTime, endTime,
                dataType, basePage);
        ResponseEntity<HubResponseBaseDTO> dataResponse;
        try {
            String uri =
                    UriComponentsBuilder.fromHttpUrl(getCompletePath(path)).queryParams(queryMap).build().toUriString();
            log.info("从hub中获取点位历史数据{}:{}", uri, httpEntity);
            dataResponse = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
                    HubResponseBaseDTO.class);
        } catch (Exception e) {
            log.error("获取数据失败", e);
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_CONNECTION_ERROR);
        }
        HubResponseBaseDTO body = dataResponse.getBody();
        if (body == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_RESPONSE_ERROR);
        }
        log.info("从hub中获取点位历史数据结束{}", dataResponse);
        this.judgeHubResponseCode(body);
        HubDataPointValuePageResponseDTO hubDataPointValuePageResponseDTO =
                JSONUtil.toBean(new JSONObject(body.getData()), HubDataPointValuePageResponseDTO.class);
        CommonPage<DataPointNameValueDTO> res = new CommonPage<>();
        res.setPageNum(hubDataPointValuePageResponseDTO.getCurrent());
        res.setPageSize(hubDataPointValuePageResponseDTO.getSize());
        res.setTotal(hubDataPointValuePageResponseDTO.getTotal());
        res.setTotalPage(hubDataPointValuePageResponseDTO.getPages());
        res.setList(hubDataPointValuePageResponseDTO.getRecords().stream().map(item -> {
            DataPointNameValueDTO dataPointNameValueDTO = new DataPointNameValueDTO();
            dataPointNameValueDTO.setDataPointName(dataPointName);
            dataPointNameValueDTO.setValue(item.getValue());
            dataPointNameValueDTO.setTimeStamp(item.getTs());
            return dataPointNameValueDTO;
        }).collect(Collectors.toList()));
        return res;
    }

    private MultiValueMap<String, String> getPageQueryParams(String dataPointName, LocalDateTime startTime,
                                                             LocalDateTime endTime, AcquisitionPointDataTypeEnum dataType, BasePage basePage) {
        String tagId = getTagIdByTagName(dataPointName);
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(HubRequestFields.TAG_ID, tagId);
        multiValueMap.add(HubRequestFields.START_TIME, dateTimeFormatter.format(startTime));
        multiValueMap.add(HubRequestFields.END_TIME, dateTimeFormatter.format(endTime));
        multiValueMap.add(HubRequestFields.SIZE, basePage.getPageSize() + "");
        multiValueMap.add(HubRequestFields.CURRENT, basePage.getPageNum() + "");
        multiValueMap.add(HubRequestFields.IS_NUMBER, (AcquisitionPointDataTypeEnum.NUMBER==dataType)+"");
        return multiValueMap;
    }

    private String getTagIdByTagName(String dataPointName) {
        String tagId = getHubProperties().getPath().getTagId();
        HttpHeaders headers = this.getHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        URI uri =
                UriComponentsBuilder.fromHttpUrl(getCompletePath(tagId)).queryParam(HubRequestFields.GET_GAT_NAME,
                        dataPointName).build().encode().toUri();
        ResponseEntity<HubResponseBaseDTO> dataResponse;
        try {
            log.info("从hub中获取点位id{}:{}", uri, httpEntity);
            dataResponse = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, HubResponseBaseDTO.class);
        } catch (Exception e) {
            log.error("获取数据失败", e);
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_CONNECTION_ERROR);
        }
        if (dataResponse.getBody() == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_RESPONSE_ERROR);
        }
        log.info("从hub中获取点位id结束{}", dataResponse);
        this.judgeHubResponseCode(dataResponse.getBody());
        JSONObject jsonObject = new JSONArray(dataResponse.getBody().getData()).get(0, JSONObject.class);
        return jsonObject.getStr(HubRequestFields.GET_TAG_ID);
    }

    @NotNull
    private HttpHeaders getHeaders() {
        String accessToken = this.getAccessToken();
        String tenantId = getHubProperties().getTenantId();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HubRequestFields.HEADER_ACCESS_TOKEN, accessToken);
        headers.add(HubRequestFields.HEADER_TENANT_ID, tenantId);
        return headers;
    }


    @Override
    public MqttAccreditInfoDTO getMqttAccreditInfo() {
        HttpHeaders headers = this.getHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<HubResponseBaseDTO> dataResponse;
        try {
            String path = getHubProperties().getPath().getMqttCredential();
            String completePath = getCompletePath(path);
            log.info("从hub中获取点位mqtt授权{}:{}", completePath, httpEntity);
            dataResponse = restTemplate.exchange(completePath, HttpMethod.GET, httpEntity, HubResponseBaseDTO.class);
        } catch (Exception e) {
            log.error("获取数据失败", e);
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_CONNECTION_ERROR);
        }
        if (dataResponse.getBody() == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_HUB_RESPONSE_ERROR);
        }
        log.info("从hub中获取mqtt授权结束{}", dataResponse);
        this.judgeHubResponseCode(dataResponse.getBody());
        Object data = dataResponse.getBody().getData();
        return JSONUtil.toBean(new JSONObject(data), MqttAccreditInfoDTO.class);
    }
}
