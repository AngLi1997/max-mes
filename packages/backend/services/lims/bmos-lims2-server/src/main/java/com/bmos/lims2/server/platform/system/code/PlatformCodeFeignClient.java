package com.bmos.lims2.server.platform.system.code;

import com.alibaba.fastjson.JSON;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.enums.CodeRuleTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.platform.system.code.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PlatformCodeFeignClient {

    @Autowired
    PlatformCodeFeign platformCodeFeign;

    /**
     * 根据编号规则和类型获取编号
     * @param code 编号规则代码
     * @param type 编号规则类型
     * @return 生成的编号
     */
    public synchronized String getNextNo(String code, CodeRuleTypeEnum type) {
        ResponseInfo<NextCodeVO> responseInfo;
        try {
            NextUseCodeDTO nextUseCodeDTO = new NextUseCodeDTO();
            nextUseCodeDTO.setCode(code);
            nextUseCodeDTO.setFields(new HashMap<>());
            log.info("平台根据规则code生成编号 nextUseCodeDTO={}", JSON.toJSONString(nextUseCodeDTO));
            responseInfo = platformCodeFeign.getNextNo(nextUseCodeDTO);
            log.info("平台根据规则code生成编号成功 responseInfo={}", JSON.toJSONString(responseInfo));
            if (responseInfo.isSuccess()) {
                return responseInfo.getData().getNo();
            }else {
                log.error("平台根据规则code生成编号失败 code={}, type={}, error={}", code, type.getValue(), responseInfo.getMessage());
                throw new BmosException(LimsResponseCode.CODERULE_ERROR, responseInfo.getMessage());
            }
        } catch (Exception e) {
            log.error("平台根据规则code生成编号失败 code={}, type={}", code, type.getValue(), e);
            throw new BmosException(LimsResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
    }

    /**
     * 获取检验单编号
     * @param code 编号规则代码
     * @return 检验单编号
     */
    public synchronized String getInspectOrderNo(String code) {
        log.info("申请检验单编号, code={}", code);
        return getNextNo(code, CodeRuleTypeEnum.INSPECT_ORDER_NO);
    }

    /**
     * 获取样品编号
     * @param code 编号规则代码
     * @return 样品编号
     */
    public synchronized String getSampleNo(String code) {
        log.info("申请样品编号, code={}", code);
        return getNextNo(code, CodeRuleTypeEnum.SAMPLE_NO);
    }

    /**
     * 获取检验单编号（未确认使用）
     * @param code 编号规则代码
     * @return NextCodeVO 包含编号信息
     */
    public synchronized NextCodeVO getInspectOrderNextUseNo(String code) {
        try {
            NextUseCodeDTO nextUseCodeDTO = new NextUseCodeDTO();
            nextUseCodeDTO.setCode(code);
            nextUseCodeDTO.setFields(new HashMap<>());
            
            log.info("调用平台接口获取检验单编号（未确认），编号规则代码：{}", code);
            ResponseInfo<NextCodeVO> responseInfo = platformCodeFeign.getNextUseNo(nextUseCodeDTO);
            log.info("平台返回编号信息：{}", JSON.toJSONString(responseInfo));
            
            if (responseInfo.isSuccess()) {
                return responseInfo.getData();
            } else {
                log.error("获取检验单编号失败，平台返回错误：{}", responseInfo.getMessage());
                throw new BmosException(LimsResponseCode.CODERULE_ERROR, responseInfo.getMessage());
            }
        } catch (BmosException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用平台接口获取检验单编号异常，编号规则代码：{}，错误信息：{}", code, e.getMessage(), e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "调用平台编号规则接口异常：" + e.getMessage());
        }
    }

    /**
     * 确认检验单编号已使用
     * @param codeRule 编号规则代码
     * @param fullNo 完整编号
     */
    public synchronized void confirmInspectOrderNo(String codeRule, String fullNo) {
        try {
            ConfirmNextUseCodeDTO confirmDTO = ConfirmNextUseCodeDTO.builder()
                    .code(codeRule)
                    .fullNo(fullNo)
                    .codeApplyTime(java.time.LocalDate.now())
                    .fields(new HashMap<>())
                    .build();
            
            log.info("确认检验单编号已使用，编号规则代码：{}，编号：{}", codeRule, fullNo);
            ResponseInfo<Void> responseInfo = platformCodeFeign.confirmNo(confirmDTO);
            log.info("确认编号使用结果：{}", JSON.toJSONString(responseInfo));
            
            if (!responseInfo.isSuccess()) {
                log.error("确认检验单编号使用失败，平台返回错误：{}", responseInfo.getMessage());
                throw new BmosException(LimsResponseCode.CODERULE_ERROR, responseInfo.getMessage());
            }
        } catch (BmosException e) {
            throw e;
        } catch (Exception e) {
            log.error("确认检验单编号使用异常，编号规则代码：{}，编号：{}，错误信息：{}", codeRule, fullNo, e.getMessage(), e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "确认编号使用异常：" + e.getMessage());
        }
    }

    /**
     * 获取样品编号（未确认使用）
     * @param code 编号规则代码
     * @return NextCodeVO 包含编号信息
     */
    public synchronized NextCodeVO getSampleNextUseNo(String code) {
        try {
            NextUseCodeDTO nextUseCodeDTO = new NextUseCodeDTO();
            nextUseCodeDTO.setCode(code);
            nextUseCodeDTO.setFields(new HashMap<>());
            
            log.info("调用平台接口获取样品编号（未确认），编号规则代码：{}", code);
            ResponseInfo<NextCodeVO> responseInfo = platformCodeFeign.getNextUseNo(nextUseCodeDTO);
            log.info("平台返回样品编号信息：{}", JSON.toJSONString(responseInfo));
            
            if (responseInfo.isSuccess()) {
                return responseInfo.getData();
            } else {
                log.error("获取样品编号失败，平台返回错误：{}", responseInfo.getMessage());
                throw new BmosException(LimsResponseCode.CODERULE_ERROR, responseInfo.getMessage());
            }
        } catch (BmosException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用平台接口获取样品编号异常，编号规则代码：{}，错误信息：{}", code, e.getMessage(), e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "调用平台样品编号规则接口异常：" + e.getMessage());
        }
    }



    /**
     * 获取样品编号（未确认使用）
     * @param code 编号规则代码
     * @return NextCodeVO 包含编号信息
     */
    public synchronized BatchNextCodeVO getSampleNextUseNoBatch(String code, int num) {
        try {
            BatchNextUseCodeDTO nextUseCodeDTO = new BatchNextUseCodeDTO();
            nextUseCodeDTO.setCode(code);
            nextUseCodeDTO.setFields(new HashMap<>());
            nextUseCodeDTO.setNum(num);

            log.info("调用平台接口获取样品编号（未确认），编号规则代码：{}", code);
            ResponseInfo<BatchNextCodeVO> responseInfo = platformCodeFeign.getBatchNextUseNo(nextUseCodeDTO);
            log.info("平台返回样品编号信息：{}", JSON.toJSONString(responseInfo));

            if (responseInfo.isSuccess()) {
                return responseInfo.getData();
            } else {
                log.error("获取样品编号失败，平台返回错误：{}", responseInfo.getMessage());
                throw new BmosException(LimsResponseCode.CODERULE_ERROR, responseInfo.getMessage());
            }
        } catch (BmosException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用平台接口获取样品编号异常，编号规则代码：{}，错误信息：{}", code, e.getMessage(), e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "调用平台样品编号规则接口异常：" + e.getMessage());
        }
    }

    /**
     * 确认样品编号已使用
     * @param codeRule 编号规则代码
     * @param fullNo 完整编号
     */
    public synchronized void confirmSampleNo(String codeRule, String fullNo) {
        try {
            ConfirmNextUseCodeDTO confirmDTO = ConfirmNextUseCodeDTO.builder()
                    .code(codeRule)
                    .fullNo(fullNo)
                    .codeApplyTime(java.time.LocalDate.now())
                    .fields(new HashMap<>())
                    .build();
            
            log.info("确认样品编号已使用，编号规则代码：{}，编号：{}", codeRule, fullNo);
            ResponseInfo<Void> responseInfo = platformCodeFeign.confirmNo(confirmDTO);
            log.info("确认样品编号使用结果：{}", JSON.toJSONString(responseInfo));
            
            if (!responseInfo.isSuccess()) {
                log.error("确认样品编号使用失败，平台返回错误：{}", responseInfo.getMessage());
                throw new BmosException(LimsResponseCode.CODERULE_ERROR, responseInfo.getMessage());
            }
        } catch (BmosException e) {
            throw e;
        } catch (Exception e) {
            log.error("确认样品编号使用异常，编号规则代码：{}，编号：{}，错误信息：{}", codeRule, fullNo, e.getMessage(), e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "确认样品编号使用异常：" + e.getMessage());
        }
    }



    /**
     * 确认样品编号已使用
     */
    public synchronized void confirmSampleNoBatch(BatchNextCodeVO batchNextCodeVO) {
        try {
            BatchConfirmNextUseCodeDTO confirmDTO = BatchConfirmNextUseCodeDTO.builder()
                    .code(batchNextCodeVO.getCode())
                    .fullNos(batchNextCodeVO.getNos().stream().map(NextCodeVO::getNo).collect(Collectors.toList()))
                    .codeApplyTime(java.time.LocalDate.now())
                    .fields(new HashMap<>())
                    .build();

            log.info("批量确认样品编号已使用，{}", JSON.toJSONString(confirmDTO));
            ResponseInfo<Void> responseInfo = platformCodeFeign.batchConfirmNo(confirmDTO);
            log.info("批量确认样品编号使用结果：{}", JSON.toJSONString(responseInfo));

            if (!responseInfo.isSuccess()) {
                log.error("确认样品编号使用失败，平台返回错误：{}", responseInfo.getMessage());
                throw new BmosException(LimsResponseCode.CODERULE_ERROR, responseInfo.getMessage());
            }
        } catch (BmosException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量确认样品编号已使用异常，{}，错误信息：{}", JSON.toJSONString(batchNextCodeVO), e.getMessage());
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "确认样品编号使用异常：" + e.getMessage());
        }
    }

}
