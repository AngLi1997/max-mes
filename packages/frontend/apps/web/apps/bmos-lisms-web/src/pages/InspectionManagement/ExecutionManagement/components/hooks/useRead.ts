import {
  postInspectAltRead,
  postInspectFourenzymeRead,
  postInspectProteinelecRead,
  postInspectProteinRead,
} from '@/services';
import { InspectionProjectEnum } from '@/types';
import { message } from 'ant-design-vue';

interface ReadParams {
  props: any;
  updateTable: () => void;
}
export const useRead = ({ props, updateTable }: ReadParams) => {
  const readLoading = ref<boolean>(false);

  const signRef = ref<any>();
  const signSuccess = async (signUrl: string) => {
    try {
      await postInspectProteinelecRead({
        inspectUrl: signUrl,
      });
      readLoading.value = false;
      updateTable();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const handleRead = async () => {
    try {
      readLoading.value = true;
      switch (props.inspectItem.value) {
        case InspectionProjectEnum.ProteinContent:
          await postInspectProteinRead();
          break;
        case InspectionProjectEnum.ALT:
          await postInspectAltRead();
          break;
        case InspectionProjectEnum.HBsAg:
        case InspectionProjectEnum.AntiHCV:
        case InspectionProjectEnum.HIVAgAb:
        case InspectionProjectEnum.AntiTP:
          await postInspectFourenzymeRead({
            inspectItemCode: props.inspectItem.value,
          });
          break;
        case InspectionProjectEnum.ProteinElectrophoresis:
          await signRef.value?.openSign({});
          readLoading.value = false;
          return;
      }
      readLoading.value = false;
      updateTable();
    } catch (error: any) {
      readLoading.value = false;
      error.message && message.error(error.message);
    }
  };
  return {
    readLoading,
    handleRead,
    signRef,
    signSuccess,
  };
};
