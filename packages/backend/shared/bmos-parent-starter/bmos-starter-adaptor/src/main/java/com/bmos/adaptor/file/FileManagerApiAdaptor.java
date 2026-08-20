package com.bmos.adaptor.file;

import com.bmos.adaptor.ApiAdaptor;
import com.bmos.adaptor.file.model.FileUpload;
import com.bmos.adaptor.file.vo.FileVO;


public interface FileManagerApiAdaptor extends ApiAdaptor {

    FileVO fileUpload(FileUpload fileUpload);
}
