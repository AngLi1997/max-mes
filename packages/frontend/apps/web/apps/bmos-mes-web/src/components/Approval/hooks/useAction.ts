import { ApprovalEmitFn } from '../types';

export type UseActionParams = {
  emit: ApprovalEmitFn;
};

export const useAction = ({ emit }: UseActionParams) => {
  const handleClickBack = () => {
    emit('back');
  };
  return {
    handleClickBack,
  };
};
