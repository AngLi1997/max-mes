import { RouteRecordRaw } from 'vue-router';

const LaboratoryResource: RouteRecordRaw = {
  path: '/laboratoryResource',
  redirect: '/laboratoryResource/material-management/inventory-management',
  meta: {
    title: '实验室资源管理',
    id: '210050',
    icon: 'LaboratoryResource',
  },
  children: [
    {
      path: '/laboratoryResource/material-management/inventory-management',
      meta: {
        title: '领用库库存管理',
        id: '210050001',
      },
      name: 'InventoryManagement',
      component: () => import('@/pages/LaboratoryResource/MaterialManagement/InventoryManagement/index.vue'),
    },
    {
      path: '/laboratoryResource/material-management/inventory-query',
      meta: {
        title: '领用库入库查询',
        id: '210050002',
      },
      name: 'InventoryQuery',
      component: () => import('@/pages/LaboratoryResource/MaterialManagement/InventoryQuery/index.vue'),
    },
    {
      path: '/laboratoryResource/material-management/consumption-audit',
      meta: {
        title: '领用库消耗审核',
        id: '210050003',
      },
      name: 'ConsumptionAudit',
      component: () => import('@/pages/LaboratoryResource/MaterialManagement/ConsumptionAudit/index.vue'),
    },
    {
      path: '/laboratoryResource/material-management/consumption-query',
      meta: {
        title: '领用库消耗查询',
        id: '210050004',
      },
      name: 'ConsumptionQuery',
      component: () => import('@/pages/LaboratoryResource/MaterialManagement/ConsumptionQuery/index.vue'),
    },
    {
      path: '/laboratoryResource/material-management/scrap-audit',
      meta: {
        title: '领用库报废审核',
        id: '210050005',
      },
      name: 'ScrapAudit',
      component: () => import('@/pages/LaboratoryResource/MaterialManagement/ScrapAudit/index.vue'),
    },
    {
      path: '/laboratoryResource/material-management/scrap-query',
      meta: {
        title: '领用库报废查询',
        id: '210050006',
      },
      name: 'ScrapQuery',
      component: () => import('@/pages/LaboratoryResource/MaterialManagement/ScrapQuery/index.vue'),
    },
    {
      path: '/laboratoryResource/instrument-management/equipment',
      meta: {
        title: '仪器设备管理',
        id: '210050007',
      },
      name: 'InstrumentEquipmentManagement',
      component: () => import('@/pages/LaboratoryResource/InstrumentManagement/Equipment/index.vue'),
    },
  ],
};

export default LaboratoryResource;
