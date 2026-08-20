import { RouteRecordRaw } from 'vue-router';

const SortingManagement: RouteRecordRaw = {
  path: '/sortingManagement',
  redirect: '/sortingManagement/sorting-type-statistics',
  meta: {
    title: '分拣管理',
    id: '170080',
    icon: 'SortingManagement',
  },
  children: [
    {
      path: '/sortingManagement/sorting-type-statistics',
      meta: {
        title: '分拣类型统计',
        id: '170080001',
      },
      name: 'SortingTypeStatistics',
      component: () => import('@/pages/SortingManagement/SortingTypeStatistics/index.vue'),
    },
    {
      path: '/sortingManagement/sorting-plan',
      meta: {
        title: '分拣计划',
        id: '170080002',
      },
      name: 'SortingPlan',
      component: () => import('@/pages/SortingManagement/SortingPlan/components/Page/index.vue'),
    },
    {
      path: '/sortingManagement/verify-batch-data-query',
      meta: {
        title: '核查批次数据查询',
        id: '170080003',
      },
      name: 'VerifyBatchDataQuery',
      component: () => import('@/pages/SortingManagement/VerifyBatchDataQuery/components/Page/index.vue'),
    },
    {
      path: '/sortingManagement/sorting-out-stash',
      meta: {
        title: '分拣出库',
        id: '170080004',
      },
      name: 'SortingOutStash',
      component: () => import('@/pages/SortingManagement/SortingOutStash/index.vue'),
    },
    {
      path: '/sortingManagement/manual-plasma-sorting',
      meta: {
        title: '血浆手动分拣',
        id: '170080005',
      },
      name: 'ManualPlasmaSorting',
      component: () => import('@/pages/SortingManagement/ManualPlasmaSorting/index.vue'),
    },
    {
      path: '/sortingManagement/plasma-amalgamation',
      meta: {
        title: '合并血浆',
        id: '170080006',
      },
      name: 'PlasmaAmalgamation',
      component: () => import('@/pages/SortingManagement/PlasmaAmalgamation/index.vue'),
    },
    {
      path: '/sortingManagement/specimen-combination',
      meta: {
        title: '合并标本',
        id: '170080007',
      },
      name: 'SpecimenCombination',
      component: () => import('@/pages/SortingManagement/SpecimenCombination/index.vue'),
    },
    {
      path: '/sortingManagement/sorting-maintenance',
      meta: {
        title: '分拣维护',
        id: '170080008',
      },
      name: 'SortingMaintenance',
      component: () => import('@/pages/SortingManagement/SortingMaintenance/index.vue'),
    },
    {
      path: '/sortingManagement/unqualified-plasma-selection',
      meta: {
        title: '不合格血浆分拣',
        id: '170080009',
      },
      name: 'UnqualifiedPlasmaSelection',
      component: () => import('@/pages/SortingManagement/UnqualifiedPlasmaSelection/index.vue'),
    },
    {
      path: '/sortingManagement/unqualified-specimens-selection',
      meta: {
        title: '不合格标本分拣',
        id: '170080010',
      },
      name: 'UnqualifiedSpecimensSelection',
      component: () => import('@/pages/SortingManagement/UnqualifiedSpecimensSelection/index.vue'),
    },
    {
      path: '/sortingManagement/sorting-task',
      meta: {
        title: '分拣任务',
        id: '170080011',
      },
      name: 'SortingTask',
      component: () => import('@/pages/SortingManagement/SortingTask/components/Page/index.vue'),
    },
  ],
};

export default SortingManagement;
