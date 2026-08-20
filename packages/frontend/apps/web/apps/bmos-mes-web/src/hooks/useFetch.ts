import { message } from 'ant-design-vue';
export type request<T, K> = (...args: T[]) => Promise<{ data: K }>;

export const useFetch = <T, K>(callback: request<T, K>) => {
  const status = ref<boolean>(false);
  const fetch = async (...args: T[]) => {
    try {
      status.value = true;
      await callback(...args);
    } catch (error: any) {
      message.error(error.message);
    } finally {
      status.value = false;
    }
  };
  return [status, fetch] as [Ref<boolean>, (...args: T[]) => Promise<any>];
};
