import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 仓库管理
const WarehouseMmgEnum: Record<string, log> = {
  '170090001': {
    // 血浆库存管理
    [`${BASE_URL}/wms-plasma/in`]: {
      type: OperationType.edit,
      business: '血浆回库',
    },
    [`${BASE_URL}/wms-plasma/out`]: {
      type: OperationType.edit,
      business: '合并出库',
    },
  },
  '170090002': {
    // 不合格血浆库存管理
    [`${BASE_URL}/wms-unqualified-plasma/in`]: {
      type: OperationType.edit,
      business: '血浆回库',
    },
    [`${BASE_URL}/wms-unqualified-plasma/out`]: {
      type: OperationType.edit,
      business: '合并出库',
    },
  },
  '170090003': {
    // 标本库存管理
    [`${BASE_URL}/wms-qualified-sample/back`]: {
      type: OperationType.edit,
      business: '标本回库',
    },
    [`${BASE_URL}/wms-qualified-sample/out`]: {
      type: OperationType.edit,
      business: '合并出库',
    },
  },
  '170090004': {
    // 不合格标本库存管理
    [`${BASE_URL}/wms-qualified-sample/back`]: {
      type: OperationType.edit,
      business: '标本回库',
    },
    [`${BASE_URL}/wms-qualified-sample/out`]: {
      type: OperationType.edit,
      business: '合并出库',
    },
  },
};

export default WarehouseMmgEnum;
