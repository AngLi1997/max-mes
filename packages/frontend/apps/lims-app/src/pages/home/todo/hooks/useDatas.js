import { getProcedureLineApi, getProductTreeApi } from '@/api/productionApi.js';
import { getTodoPageApi } from '@/api/todoApi.js';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, reactive, ref } from 'vue';

export const clickItem = ref(['']);

export const cardOpen = ref({
  present_todo: false,
  future_todo: false,
});

export function usePage() {
  const filterData = ref();
  const total = ref(0);
  const treeModalData = ref({});// 产品树数据
  const params = reactive({
    pageNum: 1,
    pageSize: 20,
  });
  const loadMoreStatus = ref('loadmore');
  const todoList = ref([]);
  const triggered = ref(false);
  const validateList = ref([]);
  const tabList = ref([
    { label: t('当前待办'), value: 'present_todo' },
    { label: t('计划待办'), value: 'future_todo' },
  ]);
  const totalReactive = ref({
    present_todo: 0,
    future_todo: 0,
  });
  const tabType = ref('present_todo');
  // 获取待办列表
  const getTodoList = async (tabTypeValue) => {
    try {
      const { data } = await getTodoPageApi({
        ...params,
        ...filterData.value,
        menuCode: '121010002000001',
        todoType: tabTypeValue || tabType.value,
      });
      if (params.pageNum === 1) {
        todoList.value = data.freshTodoVo.list;
      }
      else {
        todoList.value = todoList.value.concat(data.freshTodoVo.list);
      }
      // 回显生产前确认
      validateList.value = data.planStartList || [];
      total.value = data.freshTodoVo.total;
    }
    catch (e) {
      console.log(e);
    }
    triggered.value = false;
    loadMoreStatus.value
      = total.value > todoList.value.length ? 'loadmore' : 'nomore';
  };
  // 切换待办类型
  const tabTypeChange = async (value) => {
    await getTodoList(value);
    tabType.value = value;
  };
  //  筛选确认,重置筛选
  const filterConfirmOrReset = () => {
    params.pageNum = 1;
    getTodoList();
  };
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
  const getChildrenList = (list, parentId) => {
    if (!list) {
      return [];
    }
    const newChildren = [];
    list.forEach((item) => {
      const children = getChildrenList(item.children, item.id);
      item.name = `${item.code}-${item.name}`;
      item.categoryFlag = !item.parentId;
      item.parentId = item.parentId ?? parentId;
      if (item.infoList) {
        item.infoList.forEach((infoItem) => {
          infoItem.name = `${infoItem.code}-${infoItem.name}`;
          infoItem.categoryFlag = !infoItem.parentId;
          infoItem.parentId = infoItem.parentId ?? item.id;
        });
        item.children = [...children, ...item.infoList];
      }
      else {
        item.children = [...children];
      }
      newChildren.push(item);
    });
    return newChildren;
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
            mode: 'multiple',
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
      {
        field: 'lineId',
        component: 'BMFormSelect',
        label: t('产线'),
        colProps: {
          span: 24,
        },
        componentProps: () => {
          return {
            request: async () => {
              const { data } = await getProcedureLineApi();
              const options = getChildrenList(data);
              return options;
            },
            title: t('产线名称'),
            type: 'tree',
            mode: 'multiple',
            fieldNames: {
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
    ],
  });
  onMounted(() => {
    getTodoList();
  });
  return {
    filterData,
    treeModalData,
    params,
    total,
    loadMoreStatus,
    todoList,
    triggered,
    filterFormProps,
    filterConfirmOrReset,
    getTodoList,
    validateList,
    tabList,
    totalReactive,
    tabType,
    tabTypeChange,
  };
}
