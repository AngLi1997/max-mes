import { getCurrentInstance } from 'vue';

export const useRouter = () => {
  const vm = getCurrentInstance();
  if (!vm) throw new Error('must be called in setup');
  return vm.proxy!.$router;
};

export const useRoute = () => {
  const vm = getCurrentInstance();
  if (!vm) throw new Error('must be called in setup');
  return vm.proxy!.$route;
};
