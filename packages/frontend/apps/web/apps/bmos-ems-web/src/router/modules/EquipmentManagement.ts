import { RouteRecordRaw } from 'vue-router';

const Manage: RouteRecordRaw = {
  path: '/manage',
  redirect: '/manage/factory-modeling',
  meta: {
    title: '设备管理',
    id: '160010',
    icon: 'EquipmentManagement',
  },
  children: [
    {
      path: '/manage/equipment-type',
      meta: {
        title: '设备类型',
        id: '160010001',
      },
      name: 'EquipmentType',
      component: () => import('@/pages/EquipmentManagement/equipmentType/index.vue'), //设备类型
    },
    {
      path: '/manage/equipment-manage',
      meta: {
        title: '设备管理',
        id: '160010002',
      },
      name: 'EquipmentManage',
      component: () => import('@/pages/EquipmentManagement/equipmentManagement2/index.vue'), //新设备管理页面
    },
    {
      path: '/manage/collection-point-manage',
      meta: {
        title: '采集点管理',
        id: '160010003',
      },
      name: 'CollectionPointManage',
      component: () => import('@/pages/EquipmentManagement/collectionPointManagement/index.vue'),
    },
  ],
};

export default Manage;
