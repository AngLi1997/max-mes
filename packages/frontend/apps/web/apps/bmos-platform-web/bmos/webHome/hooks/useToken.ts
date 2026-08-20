export const useToken = () => {
  const token = ref(localStorage.getItem('BMOS-ACCESS-TOKEN')); // 初始化 token

  const updateToken = () => {
    token.value = localStorage.getItem('BMOS-ACCESS-TOKEN'); // 更新 token
  };

  // 在组件挂载时监听 storage 事件
  onMounted(() => {
    window.addEventListener('storage', updateToken);
  });

  // 在组件卸载时移除事件监听器
  onBeforeUnmount(() => {
    window.removeEventListener('storage', updateToken);
  });

  return {
    token,
  };
};
