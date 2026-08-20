import { usePermissionStore } from '@/stores/permission.js';

export default function btnPermission(app) {
  app.directive('hasAuth', {
    mounted(el, binding) {
      const { hasPermission } = usePermissionStore();
      // 按钮所需权限码
      const btnCode = binding.value;
      const hasAuth = hasPermission(btnCode);

      // 无权限
      if (!hasAuth) {
        el.parentNode?.removeChild(el);
      }
    }
  });
}
