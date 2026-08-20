import { getProcedureRoomsList } from '@/api';
import { chunkArray } from '@/utils/publicFunctions.js';
import { t } from '@/utils/useBmosI18n.js';
import { reactive } from 'vue';

export const useOperation = ({ UseParams, showNotify }) => {
  const { isEsy, roomManList, currentList, triggered, filterData } = UseParams;
  const formProps = reactive({
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('房间名称'),
        colProps: {
          span: 24,
        },
      },
      {
        field: 'code',
        component: 'Input',
        label: t('房间编码'),
        colProps: {
          span: 24,
        },
      },
    ],
  });
    // 下拉刷新
  const onRefresh = () => {
    triggered.value = true;
    getTagList();
  };
    // 获取卡片信息
  const getTagList = async () => {
    try {
      const dataRes = {
        ...currentList.value,
        componentId: currentList.value.componentId,
      };
      const res = await getProcedureRoomsList(dataRes);
      const common = res.data;
      const commonList = common.map((item) => {
        return new Promise((resolve, reject) => {
          const list = {
            ...item,
            useType: [{
              title: t('房间编码'),
              value: item?.code,
            }, {
              title: t('有效期至'),
              value: item?.expireTime,
              isDate: isCurrentTimeGreaterThan(item?.expireTime),
            }],
          };
          resolve(list);
        });
      });
      const newCommon = await Promise.all(commonList);
      roomManList.data = newCommon;
      const rowData = chunkArray(roomManList.data, 3);
      roomManList.listA = rowData[0] || [];
      roomManList.listB = rowData[1] || [];
      roomManList.listC = rowData[2] || [];
      triggered.value = false;
    }
    catch (error) {
      triggered.value = false;
      error.message && showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
    // 筛选卡片的信息
  const filterTagList = () => {
    const list = roomManList.data.filter((item) => {
      const flag = (!filterData.value.name || item.name.includes(filterData.value.name)) && (!filterData.value.code || item.code.includes(filterData.value.code));
      return flag;
    });
    const rowData = chunkArray(list, 3);
    roomManList.listA = rowData[0] || [];
    roomManList.listB = rowData[1] || [];
    roomManList.listC = rowData[2] || [];
  };

  // 点击卡片
  const tagClick = (data) => {
    isEsy.value = true;
    const mapData = { ...currentList.value, id: data?.id };
    const query = Object.keys(mapData)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(mapData[key])}`)
      .join('&');
    uni.navigateTo({
      url: `/pages/businessComponents/clearingRoomsDetails/index?${query}`,
    });
  };
  const isCurrentTimeGreaterThan = (dateTimeString) => {
    // 尝试将字符串转换为 Date 对象
    const inputDate = new Date(dateTimeString);
    // 检查输入日期是否有效（即不是 Invalid Date）
    if (isNaN(inputDate.getTime())) {
      throw new TypeError('Invalid date time string');
    }
    // 获取当前时间
    const currentDate = new Date();

    // 比较两个日期
    return currentDate > inputDate;
  };
  return {
    getTagList,
    tagClick,
    onRefresh,
    filterTagList,
    formProps,
  };
};
