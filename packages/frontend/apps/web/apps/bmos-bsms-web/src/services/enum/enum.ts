import { Obj } from './type';
import SpecimenManagementEnum from './urlEnum/SpecimenManagement'; // 标本管理
import ProductConfigurationEnum from './urlEnum/configuration/productConfiguration'; // 货位配置
import ProductNameEnum from './urlEnum/configuration/productName'; // 货品信息
import InspectionManagementEnum from './urlEnum/inspectionManagement'; // 检验管理
import InventoryManageEnum from './urlEnum/manage/inventoryManage'; // 货品管理
import SendOutEnum from './urlEnum/manage/sendOut'; // 发料管理
import StorageManageEnum from './urlEnum/manage/storageManage'; // 库存管理
import OutboundMngEnum from './urlEnum/outboundMng'; // 出库管理
import PlasmaManagementEnum from './urlEnum/plasmaManagement'; // 血浆管理
import QualityAssuranceManagementEnum from './urlEnum/qualityAssuranceManagement'; // 质保管理
import QuarantineManagementEnum from './urlEnum/quarantineManagement'; // 检疫期管理
import SortingManagementEnum from './urlEnum/sortingManagement'; // 分拣管理
import SystemMngEnum from './urlEnum/systemMng'; // 系统管理
import UnqualifiedPlasmaMngEnum from './urlEnum/unqualifiedPlasmaMng'; // 不合格血浆管理
import WarehouseMmgEnum from './urlEnum/warehouseMmg'; // 仓库管理

export const HeadersEnum: Record<string, Record<string, Obj>> = {
  ...StorageManageEnum,
  ...InventoryManageEnum,
  ...SendOutEnum,
  ...ProductNameEnum,
  ...ProductConfigurationEnum,
  ...SpecimenManagementEnum,
  ...InspectionManagementEnum,
  ...PlasmaManagementEnum,
  ...QualityAssuranceManagementEnum,
  ...SystemMngEnum,
  ...QuarantineManagementEnum,
  ...SortingManagementEnum,
  ...UnqualifiedPlasmaMngEnum,
  ...OutboundMngEnum,
  ...WarehouseMmgEnum,
};
