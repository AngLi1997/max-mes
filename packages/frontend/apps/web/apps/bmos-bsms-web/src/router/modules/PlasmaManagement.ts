import { RouteRecordRaw } from 'vue-router';

const PlasmaManagement: RouteRecordRaw = {
  path: '/plasmaManagement',
  redirect: '/plasmaManagement/plasma-data-sync',
  meta: {
    title: '血浆管理',
    id: '170040',
    icon: 'PlasmaManagement',
  },
  children: [
    {
      path: '/plasmaManagement/plasma-data-sync',
      meta: {
        title: '血浆数据同步',
        id: '170040001',
      },
      name: 'PlasmaDataSync',
      component: () => import('@/pages/PlasmaManagement/PlasmaDataSync/index.vue'),
    },
    {
      path: '/plasmaManagement/plasma-in-stored-mng',
      meta: {
        title: '待入库血浆管理',
        id: '170040002',
      },
      name: 'PlasmaInStoredMng',
      component: () => import('@/pages/PlasmaManagement/PlasmaInStoredMng/index.vue'),
    },
    {
      path: '/plasmaManagement/plasma-storage',
      meta: {
        title: '血浆入库',
        id: '170040003',
      },
      name: 'PlasmaStorage',
      component: () => import('@/pages/PlasmaManagement/PlasmaStorage/index.vue'),
    },
    {
      path: '/plasmaManagement/plasma-check-storage',
      meta: {
        title: '入库血浆核对',
        id: '170040004',
      },
      name: 'PlasmaCheckStorage',
      component: () => import('@/pages/PlasmaManagement/PlasmaCheckStorage/index.vue'),
    },
    {
      path: '/plasmaManagement/visualInspection-before-storage',
      meta: {
        title: '入库前外观检验',
        id: '170040005',
      },
      name: 'visualInspectionBeforeStorage',
      component: () => import('@/pages/PlasmaManagement/VisualInspectionBeforeStorage/index.vue'),
    },
    {
      path: '/plasmaManagement/visual-inspection',
      meta: {
        title: '血浆外观检验',
        id: '170040006',
      },
      name: 'VisualInspection',
      component: () => import('@/pages/PlasmaManagement/VisualInspection/components/Page/index.vue'),
    },
    {
      path: '/plasmaManagement/appearance-unqualified-audit',
      meta: {
        title: '外观不合格审核',
        id: '170040007',
      },
      name: 'PlasmaManagementAppearanceUnqualifiedAudit',
      component: () => import('@/pages/PlasmaManagement/AppearanceUnqualifiedAudit/components/Page/index.vue'),
    },
    {
      path: '/plasmaManagement/plasma-stock-warning',
      meta: {
        title: '血浆库存预警',
        id: '170040008',
      },
      name: 'plasma-stock-warning',
      component: () => import('@/pages/PlasmaManagement/PlasmaStockWarning/index.vue'),
    },
    {
      path: '/plasmaManagement/plasma-inventory-inquiry',
      meta: {
        title: '血浆库存查询',
        id: '170040009',
      },
      name: 'PlasmaInventoryInquiry',
      component: () => import('@/pages/PlasmaManagement/PlasmaInventoryInquiry/components/Page/index.vue'),
    },
    {
      path: '/plasmaManagement/blood-donor-management',
      meta: {
        title: '献浆者管理',
        id: '170040010',
      },
      name: 'PlasmaDonorManagementPage',
      component: () => import('@/pages/PlasmaManagement/BloodDonorManagement/components/Page/index.vue'),
    },
  ],
};

export default PlasmaManagement;
