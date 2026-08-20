import { Obj } from './type';
import ProductConfigurationEnum from './urlEnum/configuration/productConfiguration'; // 货位配置
import ProductNameEnum from './urlEnum/configuration/productName'; // 货品信息
import ConfigurationManagementEnum from './urlEnum/configurationManagement'; // 配置管理
import InventoryManageEnum from './urlEnum/manage/inventoryManage'; // 货品管理
import SendOutEnum from './urlEnum/manage/sendOut'; // 发料管理
import StorageManageEnum from './urlEnum/manage/storageManage'; // 库存管理
import MaterialManagementEnum from './urlEnum/materialManagement'; // 物料管理
import ReportManagementEnum from './urlEnum/reportManagement'; // 报告管理
import SingleDataManagementEnum from './urlEnum/singleDataManagement'; // 单项数据管理
import SpecimenManagementEnum from './urlEnum/specimenManagement'; // 标本管理
import TotalDataManagementEnum from './urlEnum/totalDataManagement'; // 总数据管理

export const HeadersEnum: Record<string, Record<string, Obj>> = {
  ...StorageManageEnum,
  ...InventoryManageEnum,
  ...SendOutEnum,
  ...ProductNameEnum,
  ...ProductConfigurationEnum,
  ...SingleDataManagementEnum,
  ...TotalDataManagementEnum,
  ...ConfigurationManagementEnum,
  ...MaterialManagementEnum,
  ...ReportManagementEnum,
  ...SpecimenManagementEnum,
};
