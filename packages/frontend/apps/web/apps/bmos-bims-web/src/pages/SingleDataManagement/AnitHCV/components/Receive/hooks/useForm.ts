import { ModalFormInstance } from '@bmos/components';

export const useForm = () => {
  const modalFormRef = ref<ModalFormInstance>();

  return {
    modalFormRef,
  };
};
