import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';
// 血浆管理
const PlasmaManagementEnum: Record<string, log> = {
  '170040001': {
    // 血浆数据同步
    [`${BASE_URL}/plasma-data-sync/update`]: {
      type: OperationType.edit,
      business: '修改信息',
    },
    [`${BASE_URL}/plasma-data-sync/receive`]: {
      type: OperationType.edit,
      business: '确认接收',
    },
    [`${BASE_URL}/plasma-data-sync/revocation`]: {
      type: OperationType.edit,
      business: '撤销同步',
    },
    [`${BASE_URL}/plasma-data-sync/manual/sync`]: {
      type: OperationType.add,
      business: '手动导入',
    },
  },
  '170040002': {
    // 待入库血浆管理
    [`${BASE_URL}/plasma-to-warehouse/transport`]: {
      type: OperationType.edit,
      business: '获取运输信息',
    },
    [`${BASE_URL}/plasma-to-warehouse/accept`]: {
      type: OperationType.edit,
      business: '入库验收',
    },
  },
  '170040003': {
    // 血浆入库
    [`${BASE_URL}/plasma-in-warehouse/batch/in`]: {
      type: OperationType.edit,
      business: '整批入库',
    },
  },
  '170040004': {
    // 入库血浆核对
    [`${BASE_URL}/plasma-in-warehouse-verify/submit`]: {
      type: OperationType.edit,
      business: '完成核对',
    },
  },
  '170040005': {
    // 入库前外观检验
    // [`${BASE_URL}/plasma-appearance-before/update`]: {
    //   type: OperationType.edit,
    //   business: '更改外观检验',
    // },
    [`${BASE_URL}/plasma-appearance-before/submit`]: {
      type: OperationType.edit,
      business: '提交外观检验结果',
    },
  },
  '170040006': {
    // 血浆外观检验
    [`${BASE_URL}/plasma-appearance/execute`]: {
      type: OperationType.edit,
      business: '外观检验',
    },
    [`${BASE_URL}/plasma-appearance/warehouse/out`]: {
      type: OperationType.edit,
      business: '整批出库',
    },
    [`${BASE_URL}/plasma-appearance/warehouse/in`]: {
      type: OperationType.edit,
      business: '整批回库',
    },
  },
  '170040007': {
    // 外观不合格审核
    [`${BASE_URL}/plasma-appearance-audit/execute`]: {
      type: OperationType.audit,
      business: '外观不合格审核',
    },
  },
  '170040009': {
    // 血浆库存查询
    [`${BASE_URL}/plasma-inventory/maintain`]: {
      type: OperationType.edit,
      business: '血浆维护',
    },
  },
  // '170040010': {
  //   // 献浆者管理
  //   [`${BASE_URL}/plasma-donor-info/automaticSync`]: {
  //     type: OperationType.relevance,
  //     business: '浆站同步献浆者数据',
  //   },
  // },
};

export default PlasmaManagementEnum;
