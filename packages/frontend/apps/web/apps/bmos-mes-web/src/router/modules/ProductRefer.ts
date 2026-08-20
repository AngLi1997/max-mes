import { RouteRecordRaw } from 'vue-router';

const ProductRefer: RouteRecordRaw = {
  path: '/product-refer',
  redirect: '/product-refer/production-history',
  meta: {
    title: '生产查询',
    id: '120050',
    icon: 'ProductRefer',
  },
  children: [
    {
      path: '/product-refer/production-history',
      component: () => import('@/pages/ProductionRefer/ProductionHistory/index.vue'),
      meta: { title: '生产历史', id: '120050001' },
      name: 'production-history',
    },
    {
      path: '/product-refer/production-print',
      component: () => import('@/pages/ProductionRefer/BatchReleasePrint/index.vue'),
      meta: { title: '批记录打印', id: '120050002' },
      name: '',
    },
    {
      path: '/product-refer/auxiliary-record',
      component: () => import('@/pages/ProductionRefer/auxiliaryRecord/index.vue'),
      meta: { title: '辅助记录', id: '120050011' },
      name: '',
    },
    {
      path: '/product-refer/material-log',
      component: () => import('@/pages/ProductionRefer/MaterialLog/index.vue'),
      meta: { title: '物料日志', id: '120050003' },
      name: '',
    },
    {
      path: '/product-refer/cargo-space-logo',
      component: () => import('@/pages/ProductionRefer/CargoSpaceLogo/index.vue'),
      meta: { title: '货位日志', id: '120050004' },
      name: '',
    },
    // 看板(原清场日志)
    {
      path: '/board',
      component: () => import('@/pages/Board/index.vue'),
      meta: { title: '看板', id: '120050006' },
      name: '',
    },
    {
      path: '/product-refer/batch-approval-query',
      component: () => import('@/pages/ProductionRefer/BatchApprovalQuery/index.vue'),
      meta: { title: '批次审核查询', id: '120050007' },
      name: '',
    },
    {
      path: '/product-refer/product-progress',
      component: () => import('@/pages/ProductionRefer/ProductProgress/index.vue'),
      meta: { title: '生产进度', id: '120050008' },
      name: 'product-progress',
    },
    {
      path: '/product-refer/weighing-log',
      component: () => import('@/pages/ProductionRefer/weighingLog/index.vue'),
      meta: { title: '称量日志', id: '120050005' },
      name: 'weighing-log',
    },
    {
      path: '/product-refer/product-audit-progress',
      component: () => import('@/pages/ProductionRefer/ProductAuditProgress/index.vue'),
      meta: { title: '生产审核进度', id: '120050009' },
      name: 'product-audit-progress',
    },
    {
      path: '/product-refer/material-traceability',
      component: () => import('@/pages/ProductionRefer/materialTraceability/index.vue'),
      meta: { title: '物料追溯', id: '120050012' },
      name: '',
    },
    {
      path: '/product-refer/production-abstract',
      component: () => import('@/pages/ProductionRefer/BatchReleaseAbstract/index.vue'),
      meta: { title: '批次摘要', id: '120050010' },
      name: '',
    },
    {
      path: '/product-refer/batch-traceability',
      component: () => import('@/pages/ProductionRefer/BatchTraceability/index.vue'),
      meta: { title: '批次追溯', id: '120050013' },
      name: '',
    },
  ],
};
export default ProductRefer;
