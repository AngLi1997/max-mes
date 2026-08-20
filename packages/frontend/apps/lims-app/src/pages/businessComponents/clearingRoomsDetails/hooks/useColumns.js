import {
  getRoomInfoApi,
  postFactoryRoomCleanCheckComponentSave,
  postFactoryRoomRoomCleanInfoComponentSave,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { getCurrentTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { segmentedList } from '../enum';

export const useColumns = ({ UseParams, showNotify }) => {
  const { isDate, isEin, tabData, specifics } = UseParams;
  
  const isCurrentTimeGreaterThan = (dateTimeString) => {
    // 尝试将字符串转换为 Date 对象
    const inputDate = new Date(dateTimeString);
    // 检查输入日期是否有效（即不是 Invalid Date）
    if (Number.isNaN(inputDate.getTime())) {
      throw new TypeError('Invalid date time string');
    }
    // 获取当前时间
    const currentDate = new Date(getCurrentTime());

    // 比较两个日期
    return currentDate.getTime() > inputDate?.getTime();
  };
  // 获取房间信息
  const getRoomInfo = async () => {
    try {
      const res = await getRoomInfoApi(tabData.value.id);
      specifics.value = res.data;
      isDate.value = isCurrentTimeGreaterThan(res.data.expireTime);
    }
    catch (error) {
      error.message
      && showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  // 在用确定
  const inUseSubmit = async () => {
    const componentSave = {
      componentId: tabData.value.componentId,
      roomId: tabData.value.id,
      batchNo: urlQueryRef.value?.batchNo,
      copyVersion: getCurrentCopyRecordItem()?.version,
      procedureModelId: pageBasicDataRef.value.procedureModelId,
      procedureStepId: pageBasicDataRef.value?.procedureStepId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
      processId: urlQueryRef.value?.processId,
      processVersion: urlQueryRef.value?.processVersion,
      productPlanId: urlQueryRef.value?.productPlanId,
      recordItemId: pageBasicDataRef.value?.recordItemId,
      recordVersionId: pageBasicDataRef.value?.recordVersionId,
      reuse: pageBasicDataRef.value.reusable,
    };
    switch (tabData.value.componentType) {
      case segmentedList.CLEAN_CHECK:
        try {
          await postFactoryRoomCleanCheckComponentSave(componentSave);
          initFillData2();
          if (tabData.value.isToRoomKey === 'true') {
            uni.navigateBack();
            return false;
          }
          else {
            uni.navigateBack({
              delta: 2,
            });
          }
        }
        catch (error) {
          error.message
          && showNotify({
            type: 'danger',
            message: error.message,
          });
        }
        break;
      case segmentedList.CLEAN_IMPLEMENT:
        // initFillData2();
        // uni.navigateBack();
        break;
      case segmentedList.CLEAN_INFO:
        try {
          await postFactoryRoomRoomCleanInfoComponentSave(componentSave);
          initFillData2();
          if (tabData.value.isToRoomKey === 'true') {
            uni.navigateBack();
            return false;
          }
          else {
            uni.navigateBack({
              delta: 2,
            });
          }
        }
        catch (error) {
          error.message
          && showNotify({
            type: 'danger',
            message: error.message,
          });
        }
        break;
    }
  };
  // 待清场
  const ToBeClearedSubmit = () => {
    isEin.value = true;
    const query = {
      ...tabData.value,
    };
    query.timeLimit = query.timeLimit || '0';
    const id = Object.keys(query)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(query[key])}`)
      .join('&');
    uni.navigateTo({
      url: `/pages/businessComponents/cleaningExecution/index?${id}`,
    });
  };
  const myMethods = {
    1: ToBeClearedSubmit,
    2: ToBeClearedSubmit,
    3: inUseSubmit,
  };
  // 清场/确定
  const submit = async () => {
    if (specifics.value.status) {
      await myMethods[specifics.value.status?.value]();
    }
  };
  // 清场检查
  const submitConfirm = () => {
    if (!isDate.value && specifics.value.status?.value === 3) {
      inUseSubmit();
      return false;
    }
    showNotify({
      type: 'danger',
      message: t('请先清场'),
    });
  };
  const convertStringToBoolean = (str) => {
    const stringToBoolMap = {
      true: true,
      false: false,
    };
    return stringToBoolMap[str.toLowerCase()] || str !== '' ? true : str; // 如果映射中不存在，则检查是否为非空字符串
  };
  return {
    getRoomInfo,
    submit,
    ToBeClearedSubmit,
    inUseSubmit,
    isCurrentTimeGreaterThan,
    submitConfirm,
    convertStringToBoolean,
  };
};
