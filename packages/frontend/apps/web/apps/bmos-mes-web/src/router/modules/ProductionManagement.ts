import { RouteRecordRaw } from 'vue-router';

const ProductionManagement: RouteRecordRaw = {
  path: '/production-management',
  redirect: '/production-management/product-plan',
  meta: {
    title: '生产管理',
    id: '120030',
    icon: 'ProductionManagement',
  },
  children: [
    {
      path: '/production-management/weighing-task',
      component: () => import('@/pages/ProductionManagement/WeighingTask/index.vue'),
      meta: { title: '称量任务', id: '120030010' },
      name: 'weighing-task',
    },
    {
      path: '/production-management/plan-management',
      component: () => import('@/pages/ProductionManagement/PlanManagement/index.vue'),
      meta: { title: '生产计划管理', id: '120030011' },
      name: 'plan-management',
    },
    {
      path: '/production-management/plan-calendar',
      component: () => import('@/pages/ProductionManagement/PlanCalendarNew/index.vue'),
      meta: { title: '生产计划日历', id: '120030012' },
      name: 'plan-calendar',
    },
    {
      path: '/production-management/product-plan',
      component: () => import('@/pages/ProductionManagement/ProductionPlan/index.vue'),
      meta: { title: '生产指令单', id: '120030001' },
      name: '',
    },
    {
      path: '/production-management/plan-approval',
      component: () => import('@/pages/ProductionManagement/PlanApproval/index.vue'),
      meta: { title: '指令单审核', id: '120030002' },
      name: '',
    },
    {
      path: '/production-management/instruction-list-decomposition',
      component: () => import('@/pages/ProductionManagement/InstructionListDecomposition/index.vue'),
      meta: { title: '指令单分解', id: '120030003' },
      name: 'InstructionListDecomposition',
    },
    {
      path: '/production-management/instruction-confirmation',
      component: () => import('@/pages/ProductionManagement/InstructionConfirmation/index.vue'),
      meta: { title: '指令单确认', id: '120030004' },
      name: '',
    },
    {
      path: '/production-management/team-management',
      component: () => import('@/pages/ProductionManagement/TeamManagement/index.vue'),
      meta: { title: '班组管理', id: '120030005' },
      name: '',
    },
    {
      path: '/production-management/batch-management',
      component: () => import('@/pages/ProductionManagement/BatchManagement/index.vue'),
      meta: { title: '批次管理', id: '120030006' },
      name: '',
    },
    {
      path: '/production-management/material-manage',
      component: () => import('@/pages/ProductionManagement/MaterialManage/index.vue'),
      meta: { title: '物料管理', id: '120030007' },
      name: 'material-manage',
    },
    {
      path: '/production-management/temporary-storage-manage',
      component: () => import('@/pages/ProductionManagement/TemporaryStorageManage/index.vue'),
      meta: { title: '暂存间管理', id: '120030008' },
      name: '',
    },
    {
      path: '/production-management/approval-filling',
      component: () => import('@/pages/ProductionManagement/ApprovalFilling/index.vue'),
      meta: { title: '批次审核', id: '120030009' },
      name: '',
    },
    {
      path: '/production-management/weighing-requirements',
      component: () => import('@/pages/ProductionManagement/WeighingRequirements/index.vue'),
      meta: { title: '生产批次配料', id: '120030013' },
      name: 'weighing-requirements',
    },
    {
      path: '/production-management/weighing-work-order-plan',
      component: () => import('@/pages/ProductionManagement/WeighingWorkOrderPlan/index.vue'),
      meta: { title: '称量工单规划', id: '120030014' },
      name: 'weighing-work-order-plan',
    },
  ],
};
export default ProductionManagement;
