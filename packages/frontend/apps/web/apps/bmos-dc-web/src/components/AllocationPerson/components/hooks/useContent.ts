import { EmitFn } from '@bmos/components';

export const useContent = (emit: EmitFn) => {
  const handleIconClick = (...args: any[]) => {
    emit('icon-click', ...args);
  };
  return { handleIconClick };
};
