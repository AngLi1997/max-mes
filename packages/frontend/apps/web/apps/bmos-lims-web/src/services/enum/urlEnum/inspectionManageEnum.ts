import { OperationType } from '../const';
import { log } from '../type';

const InspectionManageEnum: Record<string, log> = {
  // 检验查询
  '130020001':{
    '/app/lims/check/order/terminate': {
      type: OperationType.edit,
      business: '终止请验',
    },
    '/app/lims/check/order/confirm': {
      type: OperationType.edit,
      business: '请验确认',
    },
    '/app/lims/check/order/take': {
      type: OperationType.edit,
      business: '取样',
    },
    '/app/lims/check/order/save/inspect': {
      type: OperationType.edit,
      business: '检验录入保存',
    },
    '/app/lims/check/order/submit/inspect': {
      type: OperationType.edit,
      business: '检验录入提交',
    },
    '/app/lims/check/order/report': {
      type: OperationType.edit,
      business: '报告生成/重新检测',
    },
    '/app/lims/check/order/audit/report': {
      type: OperationType.edit,
      business: '报告审核',
    },
    '/app/lims/check/order/sign/report': {
      type: OperationType.edit,
      business: '报告签发',
    }
  },
  // 请验确认
  '130020002':{
    '/app/lims/check/order/terminate': {
      type: OperationType.edit,
      business: '终止请验',
    },
    '/app/lims/check/order/save': {
      type: OperationType.add,
      business: '发起请验',
    },
    '/app/lims/check/order/confirm': {
      type: OperationType.edit,
      business: '请验确认',
    },
  },
  // 取样
  '130020003':{
    '/app/lims/check/order/terminate': {
      type: OperationType.edit,
      business: '终止请验',
    },
    '/app/lims/check/order/take': {
      type: OperationType.edit,
      business: '取样',
    },
  },
  // 检验录入
  '130020004':{
    '/app/lims/check/order/terminate': {
      type: OperationType.edit,
      business: '终止请验',
    },
    '/app/lims/check/order/save/inspect': {
      type: OperationType.edit,
      business: '检验录入保存',
    },
    '/app/lims/check/order/submit/inspect': {
      type: OperationType.edit,
      business: '检验录入提交',
    },
  },
  // 报告生成
  '130020005':{
    '/app/lims/check/order/terminate': {
      type: OperationType.edit,
      business: '终止请验',
    },
    '/app/lims/check/order/report': {
      type: OperationType.edit,
      business: '报告生成/重新检测',
    },
  },
  // 报告审核
  '130020006':{
    '/app/lims/check/order/terminate': {
      type: OperationType.edit,
      business: '终止请验',
    },
    '/app/lims/check/order/audit/report': {
      type: OperationType.edit,
      business: '报告审核',
    },
  },
  // 报告签发
  '130020007':{
    '/app/lims/check/order/terminate': {
      type: OperationType.edit,
      business: '终止请验',
    },
    '/app/lims/check/order/sign/report': {
      type: OperationType.edit,
      business: '报告签发',
    },
  },
}
export default InspectionManageEnum