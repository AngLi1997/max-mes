export const useListenResize = (cb: () => void) => {
  onMounted(() => {
    window.addEventListener('resize', cb);
  });
  onUnmounted(() => {
    window.removeEventListener('resize', cb);
  });
};
