import { defineStore } from 'pinia';
import { ref } from 'vue';

export const usePermissionStore = defineStore('permission', () => {
  const permissions = ref({}); // 权限列表

  // 设置权限
  const setPermissions = (val) => {
    permissions.value = val;
  };

  // 根据 id 判断是否有权限
  const hasPermission = (id) => {
    return !!permissions.value?.[id];
  };
  return { permissions, setPermissions, hasPermission };
});
