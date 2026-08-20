import { Obj } from './type';
import ProductConfigurationEnum from './urlEnum/configuration/productConfiguration'; // 货位配置
import ProductNameEnum from './urlEnum/configuration/productName'; // 货品信息
import InventoryManageEnum from './urlEnum/manage/inventoryManage'; // 货品管理
import SendOutEnum from './urlEnum/manage/sendOut'; // 发料管理
import StorageManageEnum from './urlEnum/manage/storageManage'; // 库存管理

export const HeadersEnum: Record<string, Record<string, Obj>> = {
  ...StorageManageEnum,
  ...InventoryManageEnum,
  ...SendOutEnum,
  ...ProductNameEnum,
  ...ProductConfigurationEnum,
};
