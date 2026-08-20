import { RouteRecordRaw } from 'vue-router';

const RegionalManage: RouteRecordRaw = {
  path: '/regional-manage',
  redirect: '/regional-manage/production-line-manage',
  meta: {
    title: '区域管理',
    id: '160030',
    icon: 'RegionalManagement', //RegionalManagement 暂无此图标
  },
  children: [
    {
      path: '/regional-manage/production-line-manage',
      meta: {
        title: '产线管理',
        id: '160030001',
      },
      name: 'ProductionLineManage',
      component: () => import('@/pages/RegionalManagement/productionLineManagement/index.vue'),
    },
    {
      path: '/regional-manage/room-manage',
      meta: {
        title: '房间管理',
        id: '160030002',
      },
      name: 'RoomManage',
      component: () => import('@/pages/RegionalManagement/roomManagement/index.vue'),
    },
    {
      path: '/regional-manage/station-manage',
      meta: {
        title: '工位管理',
        id: '160030003',
      },
      name: 'StationManage',
      component: () => import('@/pages/RegionalManagement/stationManagement/index.vue'),
    },
  ],
};

export default RegionalManage;
