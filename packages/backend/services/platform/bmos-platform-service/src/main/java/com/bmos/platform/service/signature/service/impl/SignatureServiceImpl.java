package com.bmos.platform.service.signature.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import com.alibaba.excel.EasyExcel;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.common.utils.RsaUtils;
import com.bmos.platform.service.signature.converter.SignatureConverter;
import com.bmos.platform.service.signature.dto.SignatureDTO;
import com.bmos.platform.service.signature.dto.SignatureExportDTO;
import com.bmos.platform.service.signature.dto.SignatureQueryPageDTO;
import com.bmos.platform.service.signature.dto.SignatureValidateDTO;
import com.bmos.platform.service.signature.mapper.SignatureMapper;
import com.bmos.platform.service.signature.model.Signature;
import com.bmos.platform.service.signature.service.SignatureService;
import com.bmos.platform.service.signature.vo.SignatureExcelVO;
import com.bmos.platform.service.signature.vo.SignaturePageVO;
import com.bmos.platform.service.signature.vo.SignatureValidateResVO;
import com.bmos.platform.service.system.user.dto.CheckSignaturePasswordConfigDTO;
import com.bmos.platform.service.system.user.dto.UpdateSignaturePasswordDTO;
import com.bmos.platform.service.system.user.mapper.UserSignaturePasswordMapper;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.model.UserSignaturePassword;
import com.bmos.platform.service.system.user.service.UserService;
import com.bmos.platform.service.system.user.vo.CheckSignaturePasswordConfigResultVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants.PLATFORM_SIGNATURE_PWD_RULE_CHARACTER;
import static com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants.PLATFORM_SIGNATURE_PWD_RULE_MIN_LEN;

@Service
@Slf4j
public class SignatureServiceImpl implements SignatureService {

    private static final String LOG_PREFIX = "[签名service]";

    @Autowired
    private UserService userService;

    @Autowired
    private SignatureMapper signatureMapper;
    @Value("${bmos.secret-key}")
    private String secretKey;

    private DES des;

    @Resource
    private UserSignaturePasswordMapper userSignaturePasswordMapper;

    @PostConstruct
    public void init() {
        des = new DES(Mode.CTS, Padding.PKCS5Padding, secretKey.getBytes(), secretKey.getBytes());
    }

    private final String EXPORT_NAME = "签名追溯";

    private final String EXPORT_SHEET_NAME = "签名追溯";

    @Override
    public Boolean validate(SignatureDTO dto) {
        List<Signature> signatures = validateAndGetSignatureList(dto);
        signatureMapper.insertBatch(signatures);
        List<Signature> failed = signatures.stream()
                .filter(signature -> BooleanUtil.isFalse(signature.getSuccess()))
                .collect(Collectors.toList());
        return !CollUtil.isNotEmpty(failed);
    }

    @Override
    public CommonPage<SignaturePageVO> getPage(SignatureQueryPageDTO dto) {
        dto.convert2Date();
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return CommonPage.convertPage(signatureMapper.selectPageList(dto));
    }

    @Override
    public void exportSignatureLog(SignatureExportDTO dto) throws IOException {
        List<Long> selectIds = dto.getSelectIds();
        if (CollUtil.isNotEmpty(selectIds)) {
            List<SignatureExcelVO> list = signatureMapper.selectExcelVOByIds(dto.getSelectIds());
            handleExport(list);
            return;
        }
        List<SignatureExcelVO> list = signatureMapper.selectByCondition(dto);
        handleExport(list);
    }

    @Override
    public SignatureValidateResVO validateV2(SignatureDTO dto) {
        List<Signature> signatures = validateAndGetSignatureList(dto);
        signatureMapper.insertBatch(signatures);
        return this.getFalseIndex(signatures);
    }

    @Override
    public List<CheckSignaturePasswordConfigResultVO> checkSignaturePasswordConfig(CheckSignaturePasswordConfigDTO dto) {
        Collection<String> userIds = dto.getUserIds();
        List<UserSignaturePassword> userSignaturePasswords = userSignaturePasswordMapper.selectByUserIds(userIds);
        Map<String, UserSignaturePassword> map = CollectionUtils.convertMap(userSignaturePasswords, UserSignaturePassword::getUserId);
        return userIds.stream()
                .map(userId -> new CheckSignaturePasswordConfigResultVO(userId, map.containsKey(userId)))
                .collect(Collectors.toList());
    }

    @Override
    public SignatureValidateResVO validateV3(SignatureDTO dto) {
        List<Signature> signatures = this.validateSignatureAndGetSignatureList(dto);
        log.info("{}保存签名日志:{}", LOG_PREFIX, signatures);
        signatureMapper.insertBatch(signatures);
        return this.getFalseIndex(signatures);
    }

    @Override
    public void updateSignaturePassword(UpdateSignaturePasswordDTO dto) {
        SysUser user = SysUserHolder.getUser();
        String loginPassword = RsaUtils.decryptPwd(dto.getLoginPassword());
        if (!Objects.equals(des.encryptHex(loginPassword), user.getPassword())){
            throw new BmosException(PlatformResponseCode.USER_PASSWORD_ERROR);
        }

        // 校验密码长度
        userService.validPwdRuleLen(RsaUtils.decryptPwd(dto.getSignaturePassword()), PLATFORM_SIGNATURE_PWD_RULE_MIN_LEN);
        // 校验密码字符
        userService.validPwdRuleCharacter(RsaUtils.decryptPwd(dto.getSignaturePassword()), PLATFORM_SIGNATURE_PWD_RULE_CHARACTER);

        UserSignaturePassword userSignaturePassword = userSignaturePasswordMapper.selectByUserId(user.getUserId());
        if (userSignaturePassword == null){
            log.info("{}新增签名密码:{}", LOG_PREFIX, user.getUserName());
            UserSignaturePassword signaturePassword = new UserSignaturePassword();
            signaturePassword.setUserId(user.getUserId());
            signaturePassword.setSignaturePassword(des.encryptHex(RsaUtils.decryptPwd(dto.getSignaturePassword())));
            userSignaturePasswordMapper.insert(signaturePassword);
        }else {
            log.info("{}更新签名密码:{}", LOG_PREFIX, user.getUserName());
            userSignaturePassword.setSignaturePassword(des.encryptHex(RsaUtils.decryptPwd(dto.getSignaturePassword())));
            userSignaturePasswordMapper.updateById(userSignaturePassword);
        }
    }

    /**
     * 签名 校验登陆密码
     * @param dto
     * @return
     */
    private List<Signature> validateAndGetSignatureList(SignatureDTO dto) {
        List<SignatureValidateDTO> validates = dto.getValidates();
        List<String> loginNames = CollectionUtils.convertList(validates, SignatureValidateDTO::getLoginName);
        List<User> users = userService.getByLoginNames(loginNames);
        Map<String, User> nameMap = CollectionUtils.convertMap(users, User::getLoginName);
        return validates.stream().map(validate -> {
            User user = nameMap.get(validate.getLoginName());
            Signature signature = SignatureConverter.INSTANCE.convertToSignature(dto);
            signature.setSuccess(false);
            signature.setLoginName(validate.getLoginName());
            signature.setSignatureAction(validate.getSignatureAction());
            String password = RsaUtils.decryptPwd(validate.getPassword());
            if (ObjectUtil.isNotNull(user) && ObjectUtil.equal(des.encryptHex(password), user.getPassword())) {
                signature.setSuccess(true);
                signature.setUserName(user.getUserName());
                signature.setUserId(user.getUserId());
            }
            return signature;
        }).collect(Collectors.toList());
    }


    /**
     * 签名 校验签名密码
     * @param dto
     * @return
     */
    private List<Signature> validateSignatureAndGetSignatureList(SignatureDTO dto) {
        List<SignatureValidateDTO> validates = dto.getValidates();
        List<String> loginNames = CollectionUtils.convertList(validates, SignatureValidateDTO::getLoginName);
        List<User> users = userService.getByLoginNames(loginNames);
        Map<String, User> userMap = CollectionUtils.convertMap(users, User::getLoginName);
        // 查询签名密码
        List<UserSignaturePassword> userSignaturePasswords = userSignaturePasswordMapper.selectByUserIds(CollectionUtils.convertList(users, User::getUserId));
        Map<String, UserSignaturePassword> signaturePasswordMap = CollectionUtils.convertMap(userSignaturePasswords, UserSignaturePassword::getUserId);
        return validates.stream().map(validate -> {
            User user = userMap.get(validate.getLoginName());
            Signature signature = SignatureConverter.INSTANCE.convertToSignature(dto);
            signature.setSuccess(false);
            signature.setLoginName(validate.getLoginName());
            signature.setSignatureAction(validate.getSignatureAction());
            if (user != null){
                UserSignaturePassword signaturePassword = signaturePasswordMap.get(user.getUserId());
                String password = RsaUtils.decryptPwd(validate.getPassword());
                if (signaturePassword != null && ObjectUtil.equal(des.encryptHex(password), signaturePassword.getSignaturePassword())) {
                    signature.setSuccess(true);
                    signature.setUserName(user.getUserName());
                    signature.setUserId(user.getUserId());
                }
            }
            return signature;
        }).collect(Collectors.toList());
    }

    private void handleExport(List<SignatureExcelVO> list) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        assert response != null;
        try {
            ExcelWriterUtils.write(EXPORT_NAME, response, Collections.singletonList(new SheetDataBo(EXPORT_SHEET_NAME,SignatureExcelVO.class,  list, null)));
        } catch (Exception e) {
            log.error("签名追溯导出异常", e);
        }
    }

    /**
     * 获取校验不通过的索引
     * @param signatures
     * @return
     */
    private SignatureValidateResVO getFalseIndex(List<Signature> signatures) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < signatures.size(); i++) {
            if(BooleanUtil.isFalse(signatures.get(i).getSuccess())){
                integers.add(i);
            }
        }
        return new SignatureValidateResVO(integers);
    }

}
