import { getRoomInfoApi, getRoomInfoByCodeApi, getRoomListApi, newgetRoomListApiByLineId } from '@/api';
import { chunkArray } from '@/utils/publicFunctions.js';
import { t } from '@/utils/useBmosI18n.js';
import { useScan } from '@/utils/useScan.js';
import { currentState } from '../enum';

export const useTagContent = ({ UseParams, props }) => {
  const { isEsy, params, triggered, loadMoreStatus, roomManList } = UseParams;
  const { bmosScanCode } = useScan();
  const roomList = async () => {
    try {
      const temp = props?.confirmBefore ? '生产前确认' : '房间管理';
      console.log(temp, 'temp');

      const res = props?.confirmBefore ? await newgetRoomListApiByLineId({ ...params.value, productionLineId: props?.productionLineId }) : await getRoomListApi(params.value);
      const common = res.data;
      params.value.total = common.total;
      const commonList = common.list.map((item) => {
        return new Promise((resolve) => {
          const list = {
            ...item,
            statusName: currentState[item.status?.value],
            useType: [
              {
                title: t('房间编码'),
                value: item?.code,
              },
            ],
          };
          if (item.status.value !== 2) {
            list.useType.push({
              title: t('有效期至'),
              value: item?.expireTime,
            });
          }
          resolve(list);
        });
      });
      const newCommon = await Promise.all(commonList);
      if (params.value.pageNum === 1) {
        roomManList.data = newCommon;
      }
      else {
        roomManList.data = roomManList.data.concat(newCommon);
      }
      const chunkSize = roomManList.data.length >= 3 ? 3 : 1;
      const rowData = chunkArray(roomManList.data, chunkSize);
      roomManList.listA = rowData[0] || [];
      roomManList.listB = rowData[1] || [];
      roomManList.listC = rowData[2] || [];
      triggered.value = false;
      loadMoreStatus.value
        = params.value.total >= newCommon.length ? 'nomore' : 'loadmore';
    }
    catch (error) {
      triggered.value = false;
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };

  // 下拉刷新
  const onRefresh = () => {
    params.value.pageNum = 1;
    triggered.value = true;
    roomList();
  };
  // 上拉触底
  const onScrolltolower = () => {
    if (
      params.value.pageNum * params.value.pageSize < params.value.total
      && triggered.value === false
    ) {
      params.value.pageNum++;
      loadMoreStatus.value = 'loading';
      roomList();
    }
  };
  // 点击卡片
  const tagClick = async (data) => {
    try {
      // 校验权限
      await getRoomInfoApi(data?.id);
      isEsy.value = true;
      const query = Object.keys({ id: data?.id })
        .map(
          key => `${encodeURIComponent(key)}=${encodeURIComponent(data[key])}`,
        )
        .join('&');
      uni.navigateTo({
        url: `/pages/roomManagement/roomDetailsPage/index?${query}`,
      });
    }
    catch (error) {
      error.message && uni.showToast({
        title: error.message,
        icon: 'error',
        duration: 2000,
        mask: true,
      });
    }
  };
  // 查询房间详情(扫完房间码进入详情)
  const scanRoomCode = async (id) => {
    try {
      if (id) {
        tagClick({ id });
      }
      else {
        uni.showToast({
          title: t('扫描失败'),
          icon: 'none',
          duration: 2000,
          mask: true,
        });
      }
    }
    catch (error) {
      console.log('===========error', error);
      return uni.showToast({
        title: t('扫描失败'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  // 房间扫码进入详情
  const scanCode = () => {
    // #ifdef APP-PLUS
    bmosScanCode({
      success: async (res) => {
        const { result } = res;
        if (!result) {
          uni.showToast({
            title: t('扫码失败'),
            icon: 'error',
            duration: 2000,
            mask: true,
          });
          return;
        }
        const type = result.slice(0, 2);
        const code = result.slice(2);
        if (type !== '05') {
          uni.showToast({
            title: t('二维码格式错误，请扫描正确的二维码'),
            icon: 'none',
            duration: 2000,
            mask: true,
          });
          return;
        }
        scanRoomCode(code);
      },
      fail: () => {
        uni.showToast({
          title: t('扫码失败'),
          icon: 'error',
          duration: 2000,
          mask: true,
        });
      },
    });
    // #endif
    // #ifdef H5
    console.log('h5扫不进去的哦');
    // #endif
  };
  // win键盘confirm事件
  const scanConfirm = async (code) => {
    try {
      const res = await getRoomInfoByCodeApi(code);
      tagClick({ id: res.data.id });
    }
    catch (error) {
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
  };
  return {
    onRefresh,
    onScrolltolower,
    tagClick,
    roomList,
    scanCode,
    scanRoomCode,
    scanConfirm,
  };
};
