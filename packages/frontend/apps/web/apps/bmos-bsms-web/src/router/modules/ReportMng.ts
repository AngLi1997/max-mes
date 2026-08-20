import { RouteRecordRaw } from 'vue-router';

const ReportMng: RouteRecordRaw = {
  path: '/reportMng',
  redirect: '/reportMng/log-management',
  meta: {
    title: '报表管理',
    id: '170120',
    icon: 'ReportMng',
  },
  children: [
    {
      path: '/reportMng/plasma-test-record',
      meta: {
        title: '血浆检测记录表',
        id: '170120001',
      },
      name: 'PlasmaTestRecord',
      component: () => import('@/pages/ReportMng/PlasmaTestRecord/index.vue'),
    },
    {
      path: '/reportMng/specimen-receipt',
      meta: {
        title: '标本入库单',
        id: '170120002',
      },
      name: 'SpecimenReceipt',
      component: () => import('@/pages/ReportMng/SpecimenReceipt/index.vue'),
    },
    {
      path: '/reportMng/plasma-receipt',
      meta: {
        title: '血浆入库单',
        id: '170120003',
      },
      name: 'PlasmaReceipt',
      component: () => import('@/pages/ReportMng/PlasmaReceipt/index.vue'),
    },
    {
      path: '/reportMng/warehousing-statistics',
      meta: {
        title: '入库统计',
        id: '170120004',
      },
      name: 'WarehousingStatistics',
      component: () => import('@/pages/ReportMng/WarehousingStatistics/index.vue'),
    },
    {
      path: '/reportMng/outbound-statistics',
      meta: {
        title: '出库统计',
        id: '170120005',
      },
      name: 'OutboundStatistics',
      component: () => import('@/pages/ReportMng/OutboundStatistics/index.vue'),
    },
    {
      path: '/reportMng/verification-information-summary',
      meta: {
        title: '核查信息汇总',
        id: '170120006',
      },
      name: 'VerificationInformationSummary',
      component: () => import('@/pages/ReportMng/VerificationInformationSummary/index.vue'),
    },
    {
      path: '/reportMng/inspection-data-query',
      meta: {
        title: '检验数据查询',
        id: '170120007',
      },
      name: 'InspectionDataQuery',
      component: () => import('@/pages/ReportMng/InspectionDataQuery/index.vue'),
    },
    {
      path: '/reportMng/warehouse-inventory',
      meta: {
        title: '立体库盘存',
        id: '170120008',
      },
      name: 'WarehouseInventory',
      component: () => import('@/pages/ReportMng/WarehouseInventory/index.vue'),
    },
  ],
};

export default ReportMng;
