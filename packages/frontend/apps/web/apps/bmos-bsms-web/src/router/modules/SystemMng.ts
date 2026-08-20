import { RouteRecordRaw } from 'vue-router';

const SystemMng: RouteRecordRaw = {
  path: '/systemMng',
  redirect: '/systemMng/log-management',
  meta: {
    title: '系统管理',
    id: '170110',
    icon: 'SystemMng',
  },
  children: [
    {
      path: '/systemMng/log-management',
      meta: {
        title: '日志管理',
        id: '170110001',
      },
      name: 'LogManagement',
      component: () => import('@/pages/SystemMng/LogManagement/index.vue'),
    },
    {
      path: '/systemMng/plasma-station-mng',
      meta: {
        title: '单采血浆站管理',
        id: '170110002',
      },
      name: 'PlasmaStationMng',
      component: () => import('@/pages/SystemMng/PlasmaStationMng/index.vue'),
    },
    {
      path: '/systemMng/plasma-threshold-mng',
      meta: {
        title: '在库血浆阈值管理',
        id: '170110003',
      },
      name: 'PlasmaThresholdMng',
      component: () => import('@/pages/SystemMng/PlasmaThresholdMng/index.vue'),
    },
    {
      path: '/systemMng/plasma-color-mng',
      meta: {
        title: '库存血浆颜色管理',
        id: '170110004',
      },
      name: 'PlasmaColorMng',
      component: () => import('@/pages/SystemMng/PlasmaColorMng/index.vue'),
    },
    {
      path: '/systemMng/immunetype-mng',
      meta: {
        title: '免疫类型管理',
        id: '170110005',
      },
      name: 'ImmunetypeMng',
      component: () => import('@/pages/SystemMng/ImmunetypeMng/index.vue'),
    },
    {
      path: '/systemMng/sorting-category-mng',
      meta: {
        title: '分拣类别管理',
        id: '170110006',
      },
      name: 'SortingCategoryMng',
      component: () => import('@/pages/SystemMng/SortingCategoryMng/index.vue'),
    },
    {
      path: '/systemMng/report-template-config',
      meta: {
        title: '报告模板配置',
        id: '170110007',
      },
      name: 'ReportTemplateConfig',
      component: () => import('@/pages/SystemMng/ReportTemplateConfig/index.vue'),
    },
    {
      path: '/systemMng/printer-usage-mng',
      meta: {
        title: '标签打印机使用管理',
        id: '170110008',
      },
      name: 'PrinterUsageMng',
      component: () => import('@/pages/SystemMng/PrinterUsageMng/index.vue'),
    },
  ],
};

export default SystemMng;
