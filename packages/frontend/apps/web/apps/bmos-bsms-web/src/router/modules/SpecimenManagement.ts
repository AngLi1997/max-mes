import { RouteRecordRaw } from 'vue-router';

const SpecimenManagement: RouteRecordRaw = {
  path: '/specimenManagement',
  redirect: '/specimenManagement/sample-data-sync',
  meta: {
    title: '标本管理',
    id: '170020',
    icon: 'SpecimenManagement',
  },
  children: [
    {
      path: '/specimenManagement/sample-data-sync',
      meta: {
        title: '标本数据同步',
        id: '170020001',
      },
      name: 'SampleDataSyncPage',
      component: () => import('@/pages/SpecimenManagement/SampleDataSync/components/Page/index.vue'),
    },
    {
      path: '/specimenManagement/sample-in-stored-mng',
      meta: {
        title: '待入库标本管理',
        id: '170020002',
      },
      name: 'SampleInStoredMngPage',
      component: () => import('@/pages/SpecimenManagement/SampleInStoredMng/components/Page/index.vue'),
    },
    {
      path: '/specimenManagement/visualInspection-before-storage',
      meta: {
        title: '入库前外观检验',
        id: '170020003',
      },
      name: 'visualInspection-before-storage',
      component: () => import('@/pages/SpecimenManagement/VisualInspectionBeforeStorage/index.vue'),
    },
    {
      path: '/specimenManagement/specimen-storage',
      meta: {
        title: '标本入库',
        id: '170020004',
      },
      name: 'specimen-storage',
      component: () => import('@/pages/SpecimenManagement/SpecimenStorage/index.vue'),
    },
    {
      path: '/specimenManagement/specimen-verification',
      meta: {
        title: '入库标本核对',
        id: '170020005',
      },
      name: 'specimen-verification',
      component: () => import('@/pages/SpecimenManagement/SpecimenVerification/index.vue'),
    },
    {
      path: '/specimenManagement/sample-query',
      meta: {
        title: '已入库标本查询',
        id: '170020006',
      },
      name: 'sample-query',
      component: () => import('@/pages/SpecimenManagement/SampleQuery/index.vue'),
    },
    {
      path: '/specimenManagement/acceptance-audit',
      meta: {
        title: '验收审核',
        id: '170020007',
      },
      name: 'AcceptanceAudit',
      component: () => import('@/pages/SpecimenManagement/AcceptanceAudit/components/Page/index.vue'),
    },
    {
      path: '/specimenManagement/sample-stock-warning',
      meta: {
        title: '标本库存预警',
        id: '170020008',
      },
      name: 'sample-stock-warning',
      component: () => import('@/pages/SpecimenManagement/SampleStockWarning/index.vue'),
    },
    {
      path: '/specimenManagement/specimen-delivery-plan',
      meta: {
        title: '标本出库计划',
        id: '170020009',
      },
      name: 'SpecimenDeliveryPlan',
      component: () => import('@/pages/SpecimenManagement/SpecimenDeliveryPlan/components/Page/index.vue'),
    },
    {
      path: '/specimenManagement/specimens-out-storage-audit',
      meta: {
        title: '标本出库审核',
        id: '170020010',
      },
      name: 'specimens-out-storage-audit',
      component: () => import('@/pages/SpecimenManagement/SpecimensOutStorageAudit/components/Page/index.vue'),
    },
    {
      path: '/specimenManagement/specimen-release',
      meta: {
        title: '标本出库',
        id: '170020011',
      },
      name: 'SpecimenRelease',
      component: () => import('@/pages/SpecimenManagement/SpecimenRelease/components/Page/index.vue'),
    },
    {
      path: '/specimenManagement/outgoing-specimens-check',
      meta: {
        title: '出库标本核对',
        id: '170020012',
      },
      name: 'outgoing-specimens-check',
      component: () => import('@/pages/SpecimenManagement/OutgoingSpecimensCheck/index.vue'),
    },
    {
      path: '/specimenManagement/appearance-unqualified-audit',
      meta: {
        title: '外观不合格审核',
        id: '170020013',
      },
      name: 'AppearanceUnqualifiedAudit',
      component: () => import('@/pages/SpecimenManagement/AppearanceUnqualifiedAudit/components/Page/index.vue'),
    },
  ],
};

export default SpecimenManagement;
