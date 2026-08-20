import { RouteRecordRaw } from 'vue-router';

const OutboundMng: RouteRecordRaw = {
  path: '/outboundMng',
  redirect: '/outboundMng/delivery-plan',
  meta: {
    title: '出库管理',
    id: '170100',
    icon: 'OutboundMng',
  },
  children: [
    {
      path: '/outboundMng/delivery-plan',
      meta: {
        title: '出库计划',
        id: '170100001',
      },
      name: 'DeliveryPlan',
      component: () => import('@/pages/OutboundMng/DeliveryPlan/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/feeding-discharging-plan',
      meta: {
        title: '投料出库审核',
        id: '170100002',
      },
      name: 'FeedingDischargingPlan',
      component: () => import('@/pages/OutboundMng/FeedingDischargingPlan/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/call-out-library-plan',
      meta: {
        title: '科研调用出库审核',
        id: '170100003',
      },
      name: 'CallOutLibraryPlan',
      component: () => import('@/pages/OutboundMng/CallOutLibraryPlan/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/destroy-warehouse-audit',
      meta: {
        title: '销毁出库审核',
        id: '170100004',
      },
      name: 'DestroyWarehouseAudit',
      component: () => import('@/pages/OutboundMng/DestroyWarehouseAudit/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/authorizer-approved',
      meta: {
        title: '质量授权人批准',
        id: '170100005',
      },
      name: 'AuthorizerApproved',
      component: () => import('@/pages/OutboundMng/AuthorizerApproved/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/plasma-feed-out-of-storage',
      meta: {
        title: '血浆投料出库',
        id: '170100006',
      },
      name: 'PlasmaFeedOutOfStorage',
      component: () => import('@/pages/OutboundMng/PlasmaFeedOutOfStorage/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/plasma-feed-out-of-destruction',
      meta: {
        title: '血浆销毁出库',
        id: '170100007',
      },
      name: 'PlasmaFeedOutOfDestruction',
      component: () => import('@/pages/OutboundMng/PlasmaFeedOutOfDestruction/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/plasma-feed-out-of-science',
      meta: {
        title: '血浆科研出库',
        id: '170100008',
      },
      name: 'PlasmaFeedOutOfScience',
      component: () => import('@/pages/OutboundMng/PlasmaFeedOutOfScience/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/plasma-feed-out-of-invoke',
      meta: {
        title: '血浆调用出库',
        id: '170100009',
      },
      name: 'PlasmaFeedOutOfInvoke',
      component: () => import('@/pages/OutboundMng/PlasmaFeedOutOfInvoke/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/plasma-exit-check',
      meta: {
        title: '出库血浆核对',
        id: '170100010',
      },
      name: 'PlasmaExitCheck',
      component: () => import('@/pages/OutboundMng/PlasmaExitCheck/index.vue'),
    },
    {
      path: '/outboundMng/premelting-check',
      meta: {
        title: '预融核对',
        id: '170100011',
      },
      name: 'PremeltingCheck',
      component: () => import('@/pages/OutboundMng/PremeltingCheck/components/Page/index.vue'),
    },
    {
      path: '/outboundMng/plasma-statistics',
      meta: {
        title: '出库血浆统计',
        id: '170100012',
      },
      name: 'PlasmaStatistics',
      component: () => import('@/pages/OutboundMng/PlasmaStatistics/components/Page/index.vue'),
    },
  ],
};

export default OutboundMng;
