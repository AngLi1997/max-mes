import { reactive, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { reqWeighCenterExecuteQueryHistoryTaskPage } from '@/api';

export const usePage = () => {
  const triggered = ref(false);
  const showFilterModal = ref(false);
  const showSortModal = ref(false);
  const sortData = reactive({
    sortFinishTime: '',
    sortTaskNo: '',
    sortSendTime: ''
  });
  const filterData = reactive({
    material: '',
    weighCentre: '',
    taskNo: ''
  });
  const params = reactive({
    pageNum: 1,
    pageSize: 20,
    material: '',
    weighCentre: '',
    taskNo: '',
    sortFinishTime: '',
    sortTaskNo: '',
    sortSendTime: ''
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
    params.weighCentre = '';
    params.taskNo = '';
  };
  // 重置排序条件
  const rSortData = () => {
    params.pageNum = 1;
    params.sortFinishTime = '';
    params.sortTaskNo = '';
    params.sortSendTime = '';
  };

  const leftClick = () => {
    uni.navigateBack();
  };
  // 筛选
  const handleFilter = () => {
    Object.assign(filterData, params);
    showFilterModal.value = !showFilterModal.value;
  };
  // 筛选确认
  const filterConfirm = () => {
    showFilterModal.value = false;
    Object.assign(params, filterData);
    params.pageNum = 1;
    getWeighingHistoryList();
  };
  // 筛选重置
  const filterReset = () => {
    rFilterData();
    console.log(params);
    showFilterModal.value = false;
    getWeighingHistoryList();
  };
  // 排序
  const handleSort = () => {
    Object.assign(sortData, params);
    showSortModal.value = true;
  };
  // 排序确认
  const sortConfirm = () => {
    showSortModal.value = false;
    Object.assign(params, sortData);
    params.pageNum = 1;
    getWeighingHistoryList();
  };
  // 排序重置
  const sortReset = () => {
    rSortData();
    showSortModal.value = false;
    getWeighingHistoryList();
  };

  const onRefresh = () => {
    params.pageNum = 1;
    triggered.value = true;
    getWeighingHistoryList();
  };
  const onScrollToLower = () => {
    if (
      params.pageNum * params.pageSize < total.value &&
      triggered.value === false
    ) {
      params.pageNum++;
      loadMoreStatus.value = 'loading';
      getWeighingHistoryList();
    }
  };

  const getWeighingHistoryList = async() => {
    let orderSql = '';
    if (params.sortFinishTime && params.sortTaskNo && params.sortSendTime) {
      orderSql = `${params.sortFinishTime},${params.sortTaskNo},${params.sortSendTime}`;
    } else {
      orderSql = params.sortFinishTime || params.sortTaskNo || params.sortSendTime || '';
    }
    const res = await reqWeighCenterExecuteQueryHistoryTaskPage({
      ...params,
      orderSql
    });
    total.value = res.data.total || 0;
    listData.value = res.data.list || [];
    if (params.pageNum === 1) {
        listData.value = res.data.list;
      } else {
        listData.value = listData.value.concat(res.data.list);
      }
    list1.value = [];
    list2.value = [];
    listData.value.forEach((item, index) => {
      if (index % 2 === 0) {
        list1.value.push(item);
      } else {
        list2.value.push(item);
      }
    });
    triggered.value = false;
    loadMoreStatus.value =
      total.value > listData.value.length ? '' : 'finished';
  };

  // 点击列表项
  const itemClick = (item) => {
    uni.navigateTo({
      url: `/pages/weighingHistory/detail/index?id=${item.id}`
    });
  };

  onShow(() => {
    rSortData();
    rFilterData();
    getWeighingHistoryList();
  });
  return {
    triggered,
    showFilterModal,
    showSortModal,
    sortData,
    filterData,
    listData,
    list1,
    list2,
    loadMoreStatus,
    leftClick,
    handleFilter,
    filterConfirm,
    filterReset,
    handleSort,
    sortConfirm,
    sortReset,
    onRefresh,
    onScrollToLower,
    itemClick
  };
};
