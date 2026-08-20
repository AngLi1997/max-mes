import { RouteRecordRaw } from 'vue-router';

const PermissionsRouter: RouteRecordRaw = {
  path: '/Permissions',
  redirect: '/RoleManagement',
  meta: {
    title: '人员权限',
    id: '100030',
    icon: 'RoleManagement',
  },
  children: [
    {
      path: '/UserManagement',
      component: () => import('../../pages/Permissions/userManagement/index.vue'),
      meta: { title: '用户管理', id: '100030001' },
      name: '',
    },
    {
      path: '/DepartmentManagement',
      component: () => import('../../pages/Permissions/departmentManagement/index.vue'),
      meta: { title: '部门管理', id: '100030002' },
      name: '',
    },
    {
      path: '/RoleManagement',
      component: () => import('../../pages/Permissions/roleManager/index.vue'),
      meta: { title: '角色管理', id: '100030003' },
      name: '',
    },
    {
      path: '/PermissionAdmit',
      component: () => import('../../pages/Permissions/permissionAdmit/index.vue'),
      meta: { title: '权限授权', id: '100030004' },
      name: '',
    },
    {
      path: '/MenuPermissions',
      component: () => import('../../pages/Permissions/menuPermissions/index.vue'),
      meta: { title: '菜单权限', id: '100030005' },
      name: '',
    },
    {
      path: '/DepartmentInsideManagement',
      component: () => import('../../pages/Permissions/departmentInsideManagement/index.vue'),
      meta: { title: '部门内部管理', id: '100030006' },
      name: '',
    },
  ],
};
export default PermissionsRouter;
