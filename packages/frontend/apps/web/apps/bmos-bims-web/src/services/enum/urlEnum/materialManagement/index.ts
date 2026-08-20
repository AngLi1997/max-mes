import { OperationType } from '../../const';
import { log } from '../../type';
import { BASE_URL } from '@/services/baseUrl';

// 物料管理
const MaterialManagementEnum: Record<string, log> = {
  '180050001': { // 供应商信息
    [`${BASE_URL}/supplier/create`]: {
      type: OperationType.add,
      business: '新增',
    },
    [`${BASE_URL}/supplier/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
    [`${BASE_URL}/supplier`]: {
      type: OperationType.delete,
      business: '删除',
    },
  },
  '180050002': { // 物料基础信息
    [`${BASE_URL}/material/create`]: {
      type: OperationType.add,
      business: '新增',
    },
    [`${BASE_URL}/material/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
    [`${BASE_URL}/material`]: {
      type: OperationType.delete,
      business: '删除',
    },
  },
  '180050003': { // 入库物料信息
    [`${BASE_URL}/storage/create`]: {
      type: OperationType.add,
      business: '新增',
    },
    [`${BASE_URL}/storage/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
    [`${BASE_URL}/storage`]: {
      type: OperationType.delete,
      business: '删除',
    },
  },

};

export default MaterialManagementEnum;
