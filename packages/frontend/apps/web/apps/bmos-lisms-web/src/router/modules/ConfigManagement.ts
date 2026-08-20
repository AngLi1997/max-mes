import { RouteRecordRaw } from 'vue-router';

const ConfigManagement: RouteRecordRaw = {
  path: '/configManagement',
  redirect: '/configManagement/data-board',
  meta: {
    title: '配置管理',
    id: '210080',
    icon: 'LismsConfigManagement',
  },
  children: [
    {
      path: '/configManagement/static-data',
      meta: {
        title: '静态数据',
        id: '210080001',
      },
      name: 'StaticData',
      component: () => import('@/pages/ConfigManagement/StaticData/index.vue'),
    },
    {
      path: '/configManagement/parameter-settings',
      meta: {
        title: '参数设置管理',
        id: '210080002',
      },
      name: 'ParameterSettings',
      component: () => import('@/pages/ConfigManagement/ParameterSettings/index.vue'),
    },
    {
      path: '/configManagement/test-project-config/project-management',
      meta: {
        title: '检验项目管理',
        id: '210080003',
      },
      name: 'ProjectManagement',
      component: () => import('@/pages/ConfigManagement/ProjectManagement/index.vue'),
    },
    {
      path: '/configManagement/test-project-config/standard-settings',
      meta: {
        title: '检验标准设置',
        id: '210080004',
      },
      name: 'StandardSettings',
      component: () => import('@/pages/ConfigManagement/StandardSettings/index.vue'),
    },
    {
      path: '/configManagement/file-template/file-management',
      meta: {
        title: '文件模板管理',
        id: '210080005',
      },
      name: 'FileTemplateManagement',
      component: () => import('@/pages/ConfigManagement/FileManagement/index.vue'),
    },
    {
      path: '/configManagement/file-template/file-audit',
      meta: {
        title: '文件模板审核',
        id: '210080006',
      },
      name: 'FileTemplateAudit',
      component: () => import('@/pages/ConfigManagement/FileAudit/index.vue'),
    },
  ],
};

export default ConfigManagement;
