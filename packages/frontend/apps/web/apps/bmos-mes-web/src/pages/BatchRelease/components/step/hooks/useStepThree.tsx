import { reqLotReleaseGenerate } from '@/services';
import { Recordable } from '@bmos/components';

export type UseStepThreeParams = {
  props: any;
  curKey: Ref<number>;
  fileUrl: Ref<string>;
};
export const useStepThree = ({ props, curKey, fileUrl }: UseStepThreeParams) => {
  const percent = ref<number>(0);
  const stepThreeTimer = ref<any>(null);

  const startCreate = async (
    stepOneFormValue: Recordable,
    stepFormValues: Recordable,
    stepTwoTableDataSource: Recordable,
    key: number,
    again: boolean,
    callback: Function,
  ) => {
    percent.value = 0;
    stepThreeTimer.value = setInterval(() => {
      // 进度加 5 以内的随机数
      if (percent.value >= 95) {
        clearInterval(stepThreeTimer.value);
      } else {
        percent.value += Math.floor(Math.random() * 5);
      }
    }, 1000);
    try {
      const { data } = await reqLotReleaseGenerate({
        again,
        isValid: props.isMange ? false : true,
        planId: stepOneFormValue.planId,
        lotReleaseVersion: stepOneFormValue.lotReleaseVersion,
        lotReleaseTemplateId: stepOneFormValue.lotReleaseTemplateId,
        batchLinksData: stepTwoTableDataSource.map((item: any) => {
          return {
            planId: item.id,
          };
        }),
        dynamicData: Object.entries(stepFormValues).map(([key, value]) => {
          return {
            datasetKey: key?.split('******')[1],
            datasetPointKey: key?.split('******')[2],
            value,
          };
        }),
      });
      fileUrl.value = data;
      clearInterval(stepThreeTimer.value);
      if (curKey.value === key) {
        percent.value = 100;
        callback(true);
      }
    } catch (error) {
      callback(false);
      clearInterval(stepThreeTimer.value);
    }
  };

  return {
    percent,
    startCreate,
    stepThreeTimer,
  };
};
