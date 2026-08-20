import { RouteRecordRaw } from 'vue-router';

const Logs: RouteRecordRaw = {
  path: '/logs',
  redirect: '/manage/status-change-logs',
  meta: {
    title: '设备日志',
    id: '160020',
    icon: 'EquipmentLogs',
  },
  children: [
    {
      path: '/manage/use-logs',
      meta: {
        title: '设备使用日志',
        id: '160020002',
      },
      name: 'UseLogs',
      component: () => import('@/pages/EquipmentLogs/useLogs/index.vue'),
    },
    {
      path: '/manage/status-change-logs',
      meta: {
        title: '状态变更日志',
        id: '160020001',
      },
      name: 'StatusChangeLogs',
      component: () => import('@/pages/EquipmentLogs/statusChangeLogs/index.vue'),
    },
    {
      path: '/manage/room-cleanup-logs',
      meta: {
        title: '房间清理日志',
        id: '160020003',
      },
      name: 'RoomCleanupLogs',
      component: () => import('@/pages/EquipmentLogs/roomCleanupLogs/index.vue'),
    },
  ],
};

export default Logs;
