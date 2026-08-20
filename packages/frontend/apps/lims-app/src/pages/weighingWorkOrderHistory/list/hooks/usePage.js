import { queryWeighCenterExecuteTicketHistoryPage } from '@/api';
import { onShow } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';

export const usePage = () => {
  const triggered = ref(false);
  const filterData = ref({});
  const sortData = ref({});

  const params = reactive({
    pageNum: 1,
    pageSize: 20,
  });
  const loadMoreStatus = ref('');
  const total = ref(0);
  const listData = ref([]);
  const list1 = ref([]);
  const list2 = ref([]);

  // 重置筛选条件
  const rFilterData = () => {
    params.pageNum = 1;
    params.material = '';
    params.centre = '';
    params.ticketNo = '';
  };
  // 重置排序条件
  const rSortData = () => {
    params.pageNum = 1;
    params.sortCompleteTime = '';
    params.sortTicketNo = '';
    params.sortSendTime = '';
  };

  const leftClick = () => {
    uni.navigateBack();
  };

  const onRefresh = () => {
    params.pageNum = 1;
    triggered.value = true;
    getWeighingHistoryList();
  };
  const onScrollToLower = () => {
    if (
      params.pageNum * params.pageSize < total.value
      && triggered.value === false
    ) {
      params.pageNum++;
      loadMoreStatus.value = 'loading';
      getWeighingHistoryList();
    }
  };

  const getWeighingHistoryList = async () => {
    let orderSql = '';
    if (sortData.value.sortCompleteTime && sortData.value.sortTicketNo && sortData.value.sortSendTime) {
      orderSql = `${sortData.value.sortCompleteTime},${sortData.value.sortTicketNo},${sortData.value.sortSendTime}`;
    }
    else {
      orderSql = sortData.value.sortCompleteTime || sortData.value.sortTicketNo || sortData.value.sortSendTime || '';
    }
    const res = await queryWeighCenterExecuteTicketHistoryPage({
      ...params,
      orderSql,
      ...filterData.value,
    });
    total.value = res.data.total || 0;
    listData.value = res.data.list || [];
    if (params.pageNum === 1) {
      listData.value = res.data.list;
    }
    else {
      listData.value = listData.value.concat(res.data.list);
    }
    list1.value = [];
    list2.value = [];
    listData.value.forEach((item, index) => {
      if (index % 2 === 0) {
        list1.value.push(item);
      }
      else {
        list2.value.push(item);
      }
    });
    triggered.value = false;
    loadMoreStatus.value
      = total.value > listData.value.length ? '' : 'finished';
  };

  const filterConfirmOrReset = () => {
    getWeighingHistoryList();
  };

  // 点击列表项
  const itemClick = (item) => {
    uni.navigateTo({
      url: `/pages/weighingWorkOrderHistory/detail/index?id=${item.id}`,
    });
  };

  onShow(() => {
    rSortData();
    rFilterData();
    getWeighingHistoryList();
  });
  return {
    triggered,
    listData,
    list1,
    list2,
    loadMoreStatus,
    leftClick,
    onRefresh,
    onScrollToLower,
    itemClick,
    filterConfirmOrReset,
    filterData,
    sortData,
  };
};
