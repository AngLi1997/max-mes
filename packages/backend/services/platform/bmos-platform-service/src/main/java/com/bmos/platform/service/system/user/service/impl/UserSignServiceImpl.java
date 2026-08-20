package com.bmos.platform.service.system.user.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.id.IdUtils;
import com.bmos.platform.common.enums.signature.SignatureConstant;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.system.user.constants.PlatformMinioBucket;
import com.bmos.platform.service.config.web.MinioFileClient;
import com.bmos.platform.service.system.user.entity.UserSign;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.mapper.UserSignMapper;
import com.bmos.platform.service.system.user.service.UserSignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.bmos.platform.common.GlobalConstants.HTTP_SPLIT_SYMBOL;

@Service
public class UserSignServiceImpl implements UserSignService {

    private static final Logger log = LoggerFactory.getLogger(UserSignServiceImpl.class);

    @Autowired
    MinioFileClient minioFileClient;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserSignMapper userSignMapper;

    @Override
    public String saveUserSign(String imageBase64, String suffix, String userId) {
        // 截取地址
        String signUrl = StrUtil.EMPTY;
        // 上传文件到minio
        try {
            String[] split = imageBase64.split(",");
            imageBase64 = split[split.length - 1];
            // 生成文件路径
            Long snowflake = IdUtils.getSnowflake();
            byte[] decode = Base64Decoder.decode(imageBase64);
            File file = File.createTempFile(SignatureConstant.TEMPORARY_FOLDER, suffix);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(decode);
            }
            // 当前登录名
            String path = SysUserHolder.getUser().getLoginName() + StrUtil.DASHED + snowflake + suffix;
            signUrl = minioFileClient.uploadFile(PlatformMinioBucket.USER_SIGN, file, path);
        }catch (Exception e){
            log.error("上传手写签名失败", e);
            throw new BmosException(PlatformResponseCode.MINIO_UPLOAD_FILE_FAIL);
        }
        String[] split = signUrl.split(SignatureConstant.SIGN_URL_SPILT_SYMBOL);
        if (split.length < 1) {
            throw new BmosException(PlatformResponseCode.USER_SIGN_URL_ERROR);
        }
        // 保存文件路径
        UserSign userSign = userSignMapper.selectByUserId(userId);
        boolean exist = true;
        if (Objects.isNull(userSign)){
            userSign = new UserSign();
            exist = false;
        }
        userSign.setSignUrl(split[split.length - 1]);
        userSign.setSignTime(LocalDateTime.now());
        userSign.setUserId(userId);
        if (exist){
            userSignMapper.updateById(userSign);
        }else {
            userSignMapper.insert(userSign);
        }
        return minioFileClient.getBucketName(PlatformMinioBucket.USER_SIGN) + HTTP_SPLIT_SYMBOL + signUrl;
    }

    @Override
    public String getUserSign(String userId) {
        if (StrUtil.isEmpty(userId)){
            return StrUtil.EMPTY;
        }
        UserSign userSign = userSignMapper.selectByUserId(userId);
        if (Objects.nonNull(userSign)){
            return minioFileClient.getBucketName(PlatformMinioBucket.USER_SIGN) + HTTP_SPLIT_SYMBOL + userSign.getSignUrl();
        }
        return StrUtil.EMPTY;
    }
}
