import { Obj } from './type';
import EquipmentManagement from './urlEnum/manage/equipmentManagement'; //设备管理
import InventoryManageEnum from './urlEnum/manage/inventoryManage'; // 货品管理
import ProductionLineManagementEnum from './urlEnum/manage/productionLineManagement'; // 产线管理
import RoomManagementEnum from './urlEnum/manage/roomManagement'; // 房间管理
import SendOutEnum from './urlEnum/manage/sendOut'; // 发料管理
import StationManagementEnum from './urlEnum/manage/stationManagement'; // 工位管理
import StorageManageEnum from './urlEnum/manage/storageManage'; // 库存管理
export const HeadersEnum: Record<string, Record<string, Obj>> = {
  ...StorageManageEnum,
  ...InventoryManageEnum,
  ...SendOutEnum,
  ...ProductionLineManagementEnum,
  ...RoomManagementEnum,
  ...StationManagementEnum,
  ...EquipmentManagement,
};
