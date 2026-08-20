import { getBeforeProductionConfirmListApi, getProductTreeApi } from '@/api/productionApi.js';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';

export const usePage = () => {
  const triggered = ref(false);
  const showFilterModal = ref(false);
  const showSortModal = ref(false);
  const filterData = ref({
  });
  const sortData = ref({
  });
  const params = reactive({
    pageNum: 1,
    pageSize: 20,
  });
  const loadMoreStatus = ref('');
  const total = ref(0);
  const listData = ref([]);
  const list1 = ref([]);
  const list2 = ref([]);
  const getChildrenData = (arr) => {
    const newArr = [];
    arr.map((item) => {
      item.categoryFlag = !item.categoryFlag;
      if (item.children.length > 0) {
        item.children = getChildrenData(item.children);
      }
      newArr.push(item);
      return item;
    });
    return newArr;
  };
  // 筛选表单配置
  const filterFormProps = reactive({
    schemas: [
      {
        field: 'productId',
        component: 'BMFormSelect',
        label: t('产品名称'),
        colProps: {
          span: 24,
        },
        componentProps: () => {
          return {
            request: async () => {
              const { data } = await getProductTreeApi({ categoryType: 2 });
              return getChildrenData(data);
            },
            title: t('产品名称'),
            type: 'tree',
            fieldNames: {
              name: 'showName',
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: true,
              parentId: 'parentId',
              children: 'children',
            },
            treeData: [],
          };
        },
      },
      {
        field: 'batchNo',
        component: 'Input',
        label: t('批号'),
        colProps: {
          span: 24,
        },
      },
    ],
  });
  // 排序表单配置
  const sortFormProps = reactive({
    schemas: [
      {
        field: 'orderBy',
        component: 'BMFormRadio',
        label: t('计划下发时间'),
        colProps: {
          span: 24,
        },
        componentProps: {
          options: [
            {
              label: t('顺序排列'),
              value: 't1.update_time asc',
            },
            {
              label: t('逆序排列'),
              value: 't1.update_time desc',
            },
          ],
        },
      },
      {
        field: 'orderBy',
        component: 'BMFormRadio',
        label: t('计划生产时间'),
        colProps: {
          span: 24,
        },
        componentProps: {
          options: [
            {
              label: t('顺序排列'),
              value: 't1.product_date asc',
            },
            {
              label: t('逆序排列'),
              value: 't1.product_date desc',
            },
          ],
        },
      },
    ],
  });

  const leftClick = () => {
    uni.navigateBack();
  };

  const getBeforeProductionConfirmList = async () => {
    const res = await getBeforeProductionConfirmListApi({ ...params, ...filterData.value, ...sortData.value });
    total.value = res.data.total || 0;
    if (params.pageNum === 1) {
      listData.value = res.data.list;
    }
    else {
      listData.value = listData.value.concat(res.data.list);
    }
    list1.value = [];
    list2.value = [];
    listData.value.forEach((item, index) => {
      item.label = item.type.label;
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
  // 筛选重置
  const filterConfirmOrReset = () => {
    params.pageNum = 1;
    getBeforeProductionConfirmList();
  };

  const onRefresh = () => {
    params.pageNum = 1;
    triggered.value = true;
    getBeforeProductionConfirmList();
  };
  const onScrollToLower = () => {
    if (
      params.pageNum * params.pageSize < total.value
      && triggered.value === false
    ) {
      params.pageNum++;
      loadMoreStatus.value = 'loading';
      getBeforeProductionConfirmList();
    }
  };

  // 点击列表项
  const itemClick = (item) => {
    uni.navigateTo({
      url: `/pages/production/beforeProductionConfirm/ProcessConfirmationPage/index?id=${
        item.id
      }&processId=${item.processId}`,
    });
  };

  // 点击创建指令单
  const toAddSheet = () => {
    uni.navigateTo({
      url: `/pages/production/beforeProductionConfirm/CreateInstructionSheet/index`,
    });
  }

  onShow(() => {
    params.pageNum = 1;
    getBeforeProductionConfirmList();
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
    filterFormProps,
    sortFormProps,
    leftClick,
    onRefresh,
    onScrollToLower,
    itemClick,
    filterConfirmOrReset,
    toAddSheet,
  };
};
