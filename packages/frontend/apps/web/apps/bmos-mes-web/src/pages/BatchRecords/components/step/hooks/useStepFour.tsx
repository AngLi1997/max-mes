export type UseStepFourParams = {
  props: any;
};
export enum Status {
  SUCCESS = 1,
  FAIL = 2,
}
export const useStepFour = ({}: UseStepFourParams) => {
  const status = ref<Status>(Status.SUCCESS);

  return {
    status,
  };
};
