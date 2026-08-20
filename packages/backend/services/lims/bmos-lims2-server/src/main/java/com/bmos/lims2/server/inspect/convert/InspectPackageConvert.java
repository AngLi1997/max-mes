package com.bmos.lims2.server.inspect.convert;

import com.bmos.lims2.server.inspect.item.dto.InspectItemDTO;
import com.bmos.lims2.server.inspect.item.entity.InspectPackageItem;
import com.bmos.lims2.server.inspect.pack.dto.*;
import com.bmos.lims2.server.inspect.pack.entity.InspectPackage;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InspectPackageConvert {

    InspectPackageConvert INSTANCE = Mappers.getMapper(InspectPackageConvert.class);

    InspectPackage convert2DO(PackageCreateReqDTO reqVO);

    InspectPackageItem convert2PackageInspectDO(InspectPackageItemDTO inspectPackageItemDTO);

    CommonPage<InspectPackageDTO> convert2PackagePageRespVO(CommonPage<InspectPackage> packageDOList);

    PackageParamDTO convert2Param(PackagePageReqDTO reqVO);

    InspectPackageWithItemDTO convert2PackageInfoVO(InspectPackage packageDO);

    InspectPackageItemDTO convert2PackageInspectVO(InspectItemDTO inspectDO);
}
