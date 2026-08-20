import { RouteRecordRaw } from 'vue-router';

const MaterialManagement: RouteRecordRaw = {
  path: '/materialManagement',
  redirect: '/materialManagement/supplier-information',
  meta: {
    title: '物料管理',
    id: '180050',
    icon: 'BIMSMaterialManagement',
  },
  children: [
    {
      path: '/materialManagement/supplier-information',
      meta: {
        title: '供应商信息',
        id: '180050001',
      },
      name: 'SupplierInformation',
      component: () => import('@/pages/MaterialManagement/SupplierInformation/index.vue'),
    },
    {
      path: '/materialManagement/material-basic-information',
      meta: {
        title: '物料基础信息',
        id: '180050002',
      },
      name: 'MaterialBasicInformation',
      component: () => import('@/pages/MaterialManagement/MaterialBasicInformation/index.vue'),
    },
    {
      path: '/materialManagement/incoming-material-information',
      meta: {
        title: '入库物料信息',
        id: '180050003',
      },
      name: 'IncomingMaterialInformation',
      component: () => import('@/pages/MaterialManagement/IncomingMaterialInformation/index.vue'),
    },
  ],
};

export default MaterialManagement;
