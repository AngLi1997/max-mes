import { getProductTreeApi } from '@/api/productionApi.js';
import { reqDictDownApi } from '@/api/webViewApi.js';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, reactive, ref } from 'vue';
import { tableColProps } from './tableColProps';

export const showType = ref('index');// add/management
export const rowData = ref();
export const showHandling = ref(false);
export const showData = ref({
  title: t('异常处理'),
  type: 'handling',
});
export const historyDataList = ref([]);
export const showHistory = ref(false);
export const filterData = ref({
  productId: '',
  exceptionType: '',
  exceptionDescription: '',
});
export const useData = () => {
  const currentSegmented = ref('investigation');
  const tableRef = ref();
  const tableRef2 = ref();
  const treeModalData = ref([]);
  const addException = ref();
  const { investigationTableColProps, closedTableColProps } = tableColProps(showType, rowData, showHandling, showData, showHistory, historyDataList, addException);
  const screenData = ref();

  const getChildrenData = (arr) => {
    const newArr = [];
    arr.forEach((item) => {
      item.categoryFlag = !item.categoryFlag;
      if (item.children.length > 0) {
        item.children = getChildrenData(item.children);
      }
      newArr.push(item);
    });
    return newArr;
  };
  const formProps = reactive({
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
        field: 'exceptionType',
        component: 'BMFormSelect',
        label: t('异常类型'),
        colProps: {
          span: 24,
        },
        componentProps: () => {
          return {
            request: async () => {
              const { data } = await reqDictDownApi({ dictId: '120090001001' });
              return data;
            },
            title: t('异常类型'),
          };
        },
      },
      {
        field: 'exceptionDescription',
        component: 'Input',
        label: t('异常描述'),
        colProps: {
          span: 24,
        },
      },
    ],
  });

  // 筛选组件
  const filterConfirm = async () => {
    filterData.value = screenData.value;
  };
    // 点击新增异常按钮
  const addExceptionClick = () => {
    showType.value = 'add';
    rowData.value = '';
    console.log('新增异常点击');
  };
  const tableProps = reactive({
    pagination: false,
    data: [],
    border: false,
    tableColProps: [...investigationTableColProps],
  });
  const tableProps2 = reactive({
    pagination: false,
    data: [],
    border: false,
    tableColProps: [...closedTableColProps],
  });
  const getTableList = async () => {
    if (currentSegmented.value === 'investigation') {
      tableRef.value.resetData();
    }
    else {
      tableRef2.value.resetData();
    }
  };
    // 获取产品树数据
  const getProductTree = async () => {
    const { data } = await getProductTreeApi({ categoryType: 2 });
    console.log('=================', data);
    treeModalData.value = getChildrenData(data);
  };

  onMounted(async () => {
    getProductTree();
  });
  return {
    currentSegmented,
    tableRef,
    tableProps,
    tableRef2,
    tableProps2,
    treeModalData,
    showType,
    rowData,
    showHandling,
    showData,
    showHistory,
    historyDataList,
    addException,
    formProps,
    screenData,
    addExceptionClick,
    filterConfirm,
    getTableList,
  };
};
