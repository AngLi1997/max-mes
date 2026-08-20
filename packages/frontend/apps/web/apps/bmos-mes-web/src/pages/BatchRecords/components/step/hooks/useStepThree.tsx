import {
  reqBatchRecordsTemplateVersionVerify,
  reqLotRecordsManageGenerate,
  reqLotRecordsManageJudgeGenerate,
  reqLotRecordsManageReGenerate,
} from '@/services';
import { Recordable } from '@bmos/components';

export type UseStepThreeParams = {
  props: any;
  curKey: Ref<number>;
  fileUrl: Ref<string>;
};
export const useStepThree = ({ props, curKey, fileUrl }: UseStepThreeParams) => {
  const percent = ref<number>(0);
  const stepThreeTimer = ref<any>(null);

  // 判断记录是否生成完成
  const lotRecordsManageJudgeGenerate = async (generateId: string, key: number, callback: Function) => {
    if (!generateId) {
      return;
    }
    try {
      const res = await reqLotRecordsManageJudgeGenerate({ generateId });
      if (res.data) {
        fileUrl.value = res.data;
        clearInterval(stepThreeTimer.value);
        if (curKey.value === key) {
          percent.value = 100;
          callback(true);
        }
      } else {
        setTimeout(() => {
          lotRecordsManageJudgeGenerate(generateId, key, callback);
        }, 1000);
      }
    } catch (error) {
      callback(false);
      clearInterval(stepThreeTimer.value);
    }
  };

  const startCreate = async (
    stepOneFormValue: Recordable,
    stepTwoTableDataSource: Recordable,
    key: number,
    callback: Function,
  ) => {
    percent.value = 0;
    try {
      stepThreeTimer.value = setInterval(async () => {
        if (percent.value <= 95) {
          percent.value += Math.floor(Math.random() * 5);
        } else {
          clearInterval(stepThreeTimer.value);
        }
      }, 1000);
      let data = '';
      if (props.formValue?.archiveId) {
        const res = await reqLotRecordsManageReGenerate({
          archiveId: props.formValue.archiveId,
          sortPlanIdList: stepTwoTableDataSource.map((item: any) => {
            return item.id;
          }),
        });
        data = res.data;
      } else if (props.isMange) {
        const res = await reqLotRecordsManageGenerate({
          planId: stepOneFormValue.planId,
          templateVersionId: stepOneFormValue.lotRecordsVersion,
          sortPlanIdList: stepTwoTableDataSource.map((item: any) => {
            return item.id;
          }),
        });
        data = res.data;
      } else {
        const res = await reqBatchRecordsTemplateVersionVerify({
          planId: stepOneFormValue.planId,
          templateVersionId: stepOneFormValue.lotRecordsVersionId,
          lotRecordsTemplateId: stepOneFormValue.lotRecordsTemplateId,
          sortPlanIdList: stepTwoTableDataSource.map((item: any) => {
            return item.id;
          }),
        });
        data = res.data;
      }
      await lotRecordsManageJudgeGenerate(data, key, callback);
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
