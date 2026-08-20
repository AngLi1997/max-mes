import { RouteRecordRaw } from 'vue-router';

const ProductConfig: RouteRecordRaw = {
  path: '/product-config',
  redirect: '/product-config/record-config',
  meta: {
    title: '生产配置',
    id: '120020',
    icon: 'ProductConfig',
  },
  children: [
    {
      path: '/product-config/record-config',
      component: () => import('@/pages/RecordConfig/index.vue'),
      meta: { title: '记录配置', id: '120020001' },
      name: 'record-config',
    },
    {
      path: '/product-config/record-review',
      component: () => import('@/pages/ProductConfig/RecordReview/index.vue'),
      meta: { title: '记录审核', id: '120020002' },
      name: 'record-review',
    },
    {
      path: '/product-config/process-config',
      component: () => import('@/pages/ProductConfig/ProcessConfig/index.vue'),
      meta: { title: '工艺配置', id: '120020006' },
      name: 'process-config',
    },
    {
      path: '/product-config/process-approval',
      component: () => import('@/pages/ProductConfig/ProcessApproval/index.vue'),
      meta: { title: '工艺审核', id: '120020007' },
      name: 'process-approval',
    },
    {
      path: '/product-config/audit-config',
      component: () => import('@/pages/ProductConfig/AuditConfig/index.vue'),
      meta: { title: '流程配置', id: '120020008' },
      name: 'audit-config',
    },
    {
      path: '/product-config/rules-config',
      component: () => import('@/pages/NoRules/index.vue'),
      meta: { title: '编号规则', id: '120020009' },
      name: '',
    },
    {
      path: '/product-config/storage-room',
      component: () => import('@/pages/ProductConfig/StorageRoom/index.vue'),
      meta: { title: '暂存间配置', id: '120020010' },
      name: 'storage-room',
    },
    {
      path: '/product-config/formula-configuration',
      component: () => import('@/pages/ProductConfig/FormulaConfiguration/index.vue'),
      meta: { title: '生产BOM配置', id: '120020004' },
      name: 'formula-configuration',
    },
    {
      path: '/product-config/formula-approval',
      component: () => import('@/pages/ProductConfig/FormulaApproval/index.vue'),
      meta: { title: '生产BOM审核', id: '120020005' },
      name: 'formula-approval',
    },
    {
      path: '/product-config/operating-procedures',
      component: () => import('@/pages/ProductConfig/OperatingProcedures/index.vue'),
      meta: { title: '操作规程', id: '120020011' },
      name: 'operating-procedures',
    },
    {
      path: '/product-config/operating-approval',
      component: () => import('@/pages/ProductConfig/OperatingApproval/index.vue'),
      meta: { title: '操作规程审核', id: '120020013' },
      name: 'operating-approval',
    },
    {
      path: '/product-config/weighing-center',
      component: () => import('@/pages/ProductConfig/WeighingCenter/index.vue'),
      meta: { title: '称量中心', id: '120020012' },
      name: 'weighing-center',
    },
    {
      path: '/product-config/plan-template',
      component: () => import('@/pages/ProductConfig/PlanTemplate/index.vue'),
      meta: { title: '生产计划模板', id: '120020014' },
      name: 'plan-template',
    },
    {
      path: '/product-config/tare-manage',
      component: () => import('@/pages/ProductConfig/Tare/index.vue'),
      meta: { title: '皮重管理', id: '120020015' },
      name: 'tare-manage',
    },
    {
      path: '/product-config/material-traceability-configuration',
      component: () => import('@/pages/ProductConfig/MaterialTraceabilityConfiguration/index.vue'),
      meta: { title: '物料追溯配置', id: '120020016' },
      name: 'material-traceability-configuration',
    },
  ],
};
export default ProductConfig;
