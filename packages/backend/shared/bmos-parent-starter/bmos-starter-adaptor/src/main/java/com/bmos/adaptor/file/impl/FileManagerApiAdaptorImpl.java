package com.bmos.adaptor.file.impl;

import com.bmos.adaptor.file.FileManagerApiAdaptor;
import com.bmos.adaptor.file.feign.FileManagerOpenFeign;
import com.bmos.adaptor.file.model.FileUpload;
import com.bmos.adaptor.file.vo.FileVO;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManagerApiAdaptorImpl implements FileManagerApiAdaptor {

    private static final Logger log = LoggerFactory.getLogger(FileManagerApiAdaptor.class);

    private final FileManagerOpenFeign fileManagerOpenFeign;

    public FileManagerApiAdaptorImpl(FileManagerOpenFeign fileManagerOpenFeign) {
        this.fileManagerOpenFeign = fileManagerOpenFeign;
    }

    @Override
    public FileVO fileUpload(FileUpload fileUpload) {
        ResponseInfo<FileVO> response = fileManagerOpenFeign.upload(fileUpload.getFile());
        if (response.isError()) {
            log.error("文件上传错误：{}", response);
            throw new BmosException(BaseResponseCode.FEIGN_REMOTE_CALL_ERROR);
        }
        return response.getData();
    }
}
