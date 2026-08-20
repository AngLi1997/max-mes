import { RouteRecordRaw } from 'vue-router';

const MaterialPlatformRouter: RouteRecordRaw = {
  path: '/MaterialPlatform',
  redirect: '/MaterialPlatform/materialInfo',
  meta: {
    title: '物料平台',
    id: '100040',
    icon: 'MaterialManagement',
  },
  children: [
    {
      path: '/MaterialPlatform/materialInfo',
      component: () => import('../../pages/MaterialPlatform/materialInfo/index.vue'),
      meta: { title: '物料信息', id: '100040002' },
      name: '',
    },
    {
      path: '/UnitsManagement',
      component: () => import('../../pages/MaterialPlatform/unitsManagement/index.vue'),
      meta: { title: '单位管理', id: '100040001' },
      name: '',
    },
  ],
};
export default MaterialPlatformRouter;
