import { getMobileFactoryGetRoomInfo } from '@/api';
import { useScan } from '@/utils/useScan.js';
import {
  urlQueryRef,
  pageBasicDataRef,
  getCurrentCopyRecordItem
} from './webViewEventCallbacks.js';
import { ref } from 'vue';
/**
 * 清场检查
 * @param {*} data
 */
export const clearanceInspection = (data) => {
  const { bmosScanCode, init, stopScan } = useScan();
  // #ifdef APP-PLUS
  init();
  // #endif
  const query = ref({
    componentType: data.parent?.componentType,
    planId: urlQueryRef.value?.productPlanId,
    procedureModelId: pageBasicDataRef.value.procedureModelId,
    procedureStepModelId: pageBasicDataRef.value.procedureStepModelId,
    componentId: data.id
  });
  // 跳转房间
  const goToRoom = (roomCode, isKey) => {
    const room = {
      ...roomCode,
      isToRoomKey: isKey
    };
    const id = Object.keys(room)
      .map(
        (key) => `${encodeURIComponent(key)}=${encodeURIComponent(room[key])}`
      )
      .join('&');
    if (isKey) {
      uni.navigateTo({
        url: `/pages/businessComponents/clearingRoomsDetails/index?${id}`
      });
    } else {
      uni.navigateTo({
        url: `/pages/businessComponents/clearingRooms/index?${id}`
      });
    }
  };
  // 获取房间信息
  const roomList = async(roomCode) => {
    try {
      const CS = {
        componentId: data.id,
        roomId: roomCode,
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
        reuse: pageBasicDataRef.value.reusable
      };
      const room = await getMobileFactoryGetRoomInfo(CS);
      stopScan();
      if (room.data) {
        const model = { ...query.value, ...room.data };
        goToRoom(model, true);
      } else {
        goToRoom(query.value, false);
      }
    } catch (error) {
      uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true
      });
      stopScan();
    }
  };
  // 扫描按钮
  const iconClick = () => {
    init();
    bmosScanCode({
      success: async(res) => {
        const { result } = res;
        if (!result) {
          goToRoom({ ...query.value, scanError: 1 }, false);
          return;
        }
        const type = result.slice(0, 2);
        const code = result.slice(2);
        if (type !== '05' || !code) {
          goToRoom({ ...query.value, scanError: 1 }, false);
          return;
        }
        roomList(code);
      },
      fail: () => {
        goToRoom({ ...query.value, scanError: 1 }, false);
      }
    });
  };
  // #ifdef APP-PLUS
  // iconClick();
  // 修改为默认跳转,手动点击扫码
  goToRoom(query.value, false);
  // #endif
  // #ifdef H5
  goToRoom(query.value, false);
  // #endif
};
