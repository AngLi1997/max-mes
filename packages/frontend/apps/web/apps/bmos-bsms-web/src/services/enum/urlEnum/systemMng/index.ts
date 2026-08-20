import { OperationType } from '../../const';
import { log } from '../../type';
import { BASE_URL } from '@/services/baseUrl';
// 系统管理
const SystemMngEnum: Record<string, log> = {
  '170110002': { // 单采血浆站管理
    [`${BASE_URL}/plasma-station/create`]: {
      type: OperationType.add,
      business: '新增',
    },
    [`${BASE_URL}/plasma-station/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
    [`${BASE_URL}/plasma-station`]: {
      type: OperationType.delete,
      business: '删除',
    },
    [`${BASE_URL}/plasma-station/enableOrDisable`]: {
      type: OperationType.edit,
      business: '启用/禁用',
    },
  },
  '170110003': { // 在库血浆阈值管理
    [`${BASE_URL}/threshold/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
  },
  '170110004': { // 库存血浆颜色管理
    [`${BASE_URL}/colour/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
  },
  '170110005': { // 免疫类型管理
    [`${BASE_URL}/immunity/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
  },
  '170110006': { // 分拣类别管理
    [`${BASE_URL}/sorting/create`]: {
      type: OperationType.add,
      business: '新增',
    },
    [`${BASE_URL}/sorting/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
    [`${BASE_URL}/sorting`]: {
      type: OperationType.delete,
      business: '删除',
    },
    [`${BASE_URL}/sorting/enableOrDisable`]: {
      type: OperationType.edit,
      business: '启用/禁用',
    },
  },
  '170110007': { // 报告模板配置
    [`${BASE_URL}/report/create`]: {
      type: OperationType.add,
      business: '新增',
    },
    [`${BASE_URL}/report/update`]: {
      type: OperationType.edit,
      business: '编辑',
    },
    [`${BASE_URL}/report`]: {
      type: OperationType.delete,
      business: '删除',
    },
    [`${BASE_URL}/report/enableOrDisable`]: {
      type: OperationType.edit,
      business: '启用/禁用',
    },
    [`${BASE_URL}/report-template/saveOrUpdate`]: {
      type: OperationType.edit,
      business: '更新报告模板',
    },
  }
};

export default SystemMngEnum;
