export const constantRoutes = [
  {
    path: '/',
    name: 'Index',
    meta: {
      title: t('首页'),
      code: t('首页'),
    },
    redirect: '/home',
    component: () => import('@/pages/Main/index.vue'),
    children: [
      { path: '/home', component: () => import('@/pages/Home/index.vue') },
      {
        path: '/environmental-monitoring',
        component: () => import('@/pages/environmentalMonitoring/index.vue'),
        meta: {
          title: t('环境监控看板'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'environmental-monitoring',
      },
      {
        path: '/energy-monitoring',
        component: () => import('@/pages/energyMonitoring/index.vue'),
        meta: {
          title: t('能源监控'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'energy-monitoring',
      },
      {
        path: '/cold-chain-transport',
        component: () => import('@/pages/coldChainTransport/index.vue'),
        meta: {
          title: t('冷链运输看板'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'cold-chain-transport',
      },
      {
        path: '/production-schedule',
        component: () => import('@/pages/ProductionSchedule/index.vue'),
        meta: {
          title: t('生产进度看板'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'production-schedule',
      },
      {
        path: '/digital-signage-configuration/digital-signage-configuration-detail',
        meta: {
          title: t('数字看板'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'DigitalSignageConfigurationDetail',
        component: () => import('@/pages/digitalSignageConfiguration/digitalSignageConfigurationDetail/index.vue'),
      },
      {
        path: '/digital-signage-configuration/digital-signage-configuration-detail-abstract',
        meta: {
          title: t('数字看板摘要'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'DigitalSignageConfigurationDetailAbstract',
        component: () =>
          import('@/pages/digitalSignageConfiguration/digitalSignageConfigurationDetail/iframeIndex.vue'),
      },
      // 白俄大屏
      {
        path: '/baie/largeScreen',
        meta: {
          title: t('白俄大屏'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'BaiELargeScreen',
        component: () => import('@/pages/BaiE/index.vue'),
      },
      {
        path: '/process/process-view/largeScreenDisplay',
        component: () => import('@/pages/process/processView/LargeScreenDisplay.vue'),
        meta: {
          title: '大屏显示',
          code: '200040001',
          id: '200040001',
          hidden: false,
          parentPath: '/process/process-view',
        },
        name: 'process-view-largeScreenDisplay',
      },
      {
        path: '/hl-large-screen',
        meta: {
          title: t('华兰大屏'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'HLLargeScreen',
        component: () => import('@/pages/huanlanLagreScreen/index.vue'),
      },
      {
        path: '/hl-room-detail',
        meta: {
          title: t('华兰房间详情'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'HLRoomDetail',
        component: () => import('@/pages/huanlanDetail/roomDetail.vue'),
      },
      {
        path: '/hl-equipment-detail',
        meta: {
          title: t('华兰设备详情'),
          hidden: true,
          hiddenMenu: true,
        },
        name: 'HLEquipmentDetail',
        component: () => import('@/pages/huanlanDetail/equipmentDetail.vue'),
      },
    ],
  },
];
