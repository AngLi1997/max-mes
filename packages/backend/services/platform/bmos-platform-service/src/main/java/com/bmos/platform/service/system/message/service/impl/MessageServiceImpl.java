package com.bmos.platform.service.system.message.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.platform.service.execute.parameter.service.BusinessParameterService;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.system.message.IMessageFeignClient;
import com.bmos.platform.service.system.message.annotation.ServiceMessageAnnotation;
import com.bmos.platform.service.system.message.dto.NoticePageDTO;
import com.bmos.platform.service.system.message.enums.NoticeTypeEnum;
import com.bmos.platform.service.system.message.service.MessageService;
import com.bmos.platform.service.system.message.vo.MessageVO;
import com.bmos.platform.service.system.message.vo.SystemNoticeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private List<IMessageFeignClient> messageFeignClients;

    @Autowired
    private BusinessParameterService businessParameterService;

    @Override
    public CommonPage<SystemNoticeVO> getNoticePage(NoticePageDTO req, HttpServletRequest request) {
        AtomicReference<List<SystemNoticeVO>> fullNoticeList = new AtomicReference<>(new ArrayList<>());

        // 判断mes系统配置的服务
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterService.detailByCode(BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
        if (businessParameterDetailVO == null || StringUtils.isEmpty(businessParameterDetailVO.getValue())) {
            log.error("缺少配置{}", BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
            return CommonPage.CommonPage(Collections.emptyList(), 0L, req);
        }

        String value = businessParameterDetailVO.getValue();
        List<String> activatedSystems = JSONUtil.toList(JSONUtil.parseArray(value), String.class);

        for (IMessageFeignClient messageFeignClient : messageFeignClients) {
            ServiceMessageAnnotation annotation = AnnotationUtils.findAnnotation(messageFeignClient.getClass(), ServiceMessageAnnotation.class);
            if (annotation == null) {
                log.warn("未找到对应的【{}】服务，跳过处理！", messageFeignClient.getClass().getSimpleName());
                continue;
            }
            String systemName = annotation.value();
            if (activatedSystems.contains(systemName)) {
                ResponseInfo<RsaVO> active = messageFeignClient.actived();
                if (!active.isSuccess() || active.getData() == null || !active.getData().getActive()) {
                    log.warn("服务 [{}] 激活检查失败，响应结果：[{}]", systemName, active);
                    continue;
                }
                ResponseInfo<List<SystemNoticeVO>> noticeResponse = messageFeignClient.getNoticeList(req, request.getHeader("language"));
                if (!noticeResponse.isSuccess()) {
                    log.error("调用服务 [{}] 的 getNoticeList 接口失败，响应结果：[{}]", systemName, noticeResponse);
                    continue;
                }
                List<SystemNoticeVO> noticeList = noticeResponse.getData();
                noticeList.forEach(e -> e.setServiceName(annotation.value()));
                fullNoticeList.get().addAll(noticeList);
            }
        }
        List<SystemNoticeVO> totalList = fullNoticeList.get();
        if (CollUtil.isEmpty(totalList)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, req);
        }
        totalList.sort(Comparator.comparing(SystemNoticeVO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));

        int total = totalList.size();
        int pageNum = req.getPageNum();
        int pageSize = req.getPageSize();

        int totalPage = (total + pageSize - 1) / pageSize;

        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        List<SystemNoticeVO> result = totalList.subList(startIndex, endIndex);

        CommonPage<SystemNoticeVO> commonPage = new CommonPage<>();
        commonPage.setPageNum(pageNum);
        commonPage.setPageSize(pageSize);
        commonPage.setTotalPage(totalPage);
        commonPage.setTotal(total);
        commonPage.setList(result);
        return commonPage;
    }

    @Override
    public List<MessageVO> waitTaskCount() {
        List<MessageVO> resultList = new ArrayList<>();
        // 判断mes系统是否激活
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterService.detailByCode(BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
        if (businessParameterDetailVO == null || StringUtils.isEmpty(businessParameterDetailVO.getValue())) {
            log.error("缺少配置{}", BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
            return resultList;
        }
        String value = businessParameterDetailVO.getValue();
        JSONArray jsonArray = JSONUtil.parseArray(value);
        jsonArray.forEach(item -> {
            messageFeignClients.forEach(mesMessageFeignClient -> {
                ServiceMessageAnnotation annotation = AnnotationUtils.findAnnotation(mesMessageFeignClient.getClass(), ServiceMessageAnnotation.class);
                if (annotation == null) {
                    return;
                }
                if (item.equals(annotation.value())) {
                    ResponseInfo<RsaVO> actived = mesMessageFeignClient.actived();
                    if (actived.isSuccess() && actived.getData().getActive()) {
                        ResponseInfo<List<MessageVO>> listResponseInfo = mesMessageFeignClient.waitTaskCount();
                        if (listResponseInfo.isSuccess()) {
                            resultList.addAll(listResponseInfo.getData());
                        }
                    }
                }
            });
        });
        return resultList;
    }

    @Override
    public Long unreadWarningCount() {
        AtomicReference<Long> count = new AtomicReference<>(0L);
        // 判断mes系统是否激活
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterService.detailByCode(BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
        if (businessParameterDetailVO == null || StringUtils.isEmpty(businessParameterDetailVO.getValue())) {
            log.error("缺少配置{}", BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
            return count.get();
        }
        String value = businessParameterDetailVO.getValue();
        JSONArray jsonArray = JSONUtil.parseArray(value);
        jsonArray.forEach(systemName -> {
            messageFeignClients.forEach(messageFeignClient -> {
                ServiceMessageAnnotation annotation = AnnotationUtils.findAnnotation(messageFeignClient.getClass(), ServiceMessageAnnotation.class);
                if (annotation == null) {
                    return;
                }
                if (systemName.equals(annotation.value())) {
                    ResponseInfo<RsaVO> actived = messageFeignClient.actived();
                    if (actived.isSuccess() && actived.getData().getActive()) {
                        ResponseInfo<Long> longResponseInfo = messageFeignClient.unreadWarningCount();
                        if (longResponseInfo.isSuccess()) {
                            count.updateAndGet(v -> v + longResponseInfo.getData());
                        }
                    }
                }
            });
        });
        return count.get();
    }

    @Override
    public Boolean read(String serviceName, Long noticeId) {
        AtomicReference<Boolean> flag = new AtomicReference<>(Boolean.FALSE);
        // 判断mes系统是否激活
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterService.detailByCode(BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
        if (businessParameterDetailVO == null || StringUtils.isEmpty(businessParameterDetailVO.getValue())) {
            log.error("缺少配置{}", BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
            return flag.get();
        }
        // "["PMP","BSMS","BIMS","LISMS"]"
        String value = businessParameterDetailVO.getValue();
        List<String> activatedSystems = JSONUtil.toList(JSONUtil.parseArray(value), String.class);
        if (!activatedSystems.contains(serviceName)) {
            log.error("未配置{}", serviceName);
            return flag.get();
        }
        for (IMessageFeignClient messageFeignClient : messageFeignClients) {
            ServiceMessageAnnotation annotation = AnnotationUtils.findAnnotation(messageFeignClient.getClass(), ServiceMessageAnnotation.class);
            if (annotation == null || !serviceName.equals(annotation.value())) {
                continue;
            }
            // 判断配置的系统是否已激活
            ResponseInfo<RsaVO> activate = messageFeignClient.actived();
            if (activate.isSuccess() && activate.getData().getActive()) {
                ResponseInfo<Boolean> booleanResponseInfo = messageFeignClient.insertReadNotice(noticeId);
                if (booleanResponseInfo.isSuccess()) {
                    flag.set(booleanResponseInfo.getData());
                }
            }
        }
        return flag.get();
    }

    @Override
    public Boolean readWarningAll() {
        AtomicReference<Boolean> flag = new AtomicReference<>(Boolean.FALSE);
        // 获取系统已经激活的服务
        BusinessParameterDetailVO businessParameterDetailVO = businessParameterService.detailByCode(BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
        if (businessParameterDetailVO == null || StringUtils.isEmpty(businessParameterDetailVO.getValue())) {
            log.error("缺少配置{}", BusinessParameterCodeConstants.PLATFORM_SYS_ACTIVED_SERVICE);
            return flag.get();
        }
        String value = businessParameterDetailVO.getValue();
        List<String> activatedSystems = JSONUtil.toList(JSONUtil.parseArray(value), String.class);

        for (IMessageFeignClient messageFeignClient : messageFeignClients) {
            ServiceMessageAnnotation annotation = AnnotationUtils.findAnnotation(messageFeignClient.getClass(), ServiceMessageAnnotation.class);
            if (annotation == null) {
                log.warn("未找到对应的【{}】服务，跳过处理！", messageFeignClient.getClass().getSimpleName());
                continue;
            }
            if (activatedSystems.contains(annotation.value())) {
                ResponseInfo<RsaVO> activate = messageFeignClient.actived();
                if (!activate.isSuccess() || activate.getData() == null || !activate.getData().getActive()) {
                    log.warn("系统 [{}] 已配置，但未激活或激活检查失败，响应结果：[{}]", annotation.value(), activate);
                    continue;
                }
                ResponseInfo<Boolean> booleanResponseInfo = messageFeignClient.readAll(NoticeTypeEnum.WARNING_INFORMATION.getCode());
                if (!booleanResponseInfo.isSuccess()) {
                    log.error("调用系统 [{}] 的 readWarningAll 接口失败，响应结果：[{}]", annotation.value(), booleanResponseInfo);
                    continue;
                }
                flag.set(booleanResponseInfo.getData());
            }
        }
        return flag.get();
    }
}
