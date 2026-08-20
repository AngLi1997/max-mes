import { EmitFn } from '@bmos/components';
export const useDetailItem = (emit: EmitFn) => {
  const handleIconClick = (...args: any[]) => {
    emit('icon-click', ...args);
  };
  return { handleIconClick };
};
