import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';

export const useParams = () => {
  // 是否跳转
  const isEsy = ref(false);
  const loadMoreStatus = ref('loadmore');
  // 是否下拉刷新
  const triggered = ref(false);
  // 传参
  const params = ref({
    code: null, // 房间编码
    name: null, // 房间名称
    pageNum: 1,
    pageSize: 24,
    total: 0,
  });
    // 瀑布流全部数据
  const roomManList = reactive({
    data: [],
    listA: [],
    listB: [],
    listC: [],
  });
  const filterData = ref();
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
      {
        field: 'status',
        component: 'BMFormRadio',
        defaultValue: '',
        label: t('房间状态'),
        colProps: {
          span: 24,
        },
        componentProps: {
          options: [
            {
              label: t('全部'),
              value: '',
            },
            {
              label: t('待清场'),
              value: 2,
            },
            {
              label: t('已清场'),
              value: 3,
            },
            {
              label: t('在用'),
              value: 1,
            },
          ],
        },
      },
    ],
  });
  return {
    isEsy,
    params,
    triggered,
    loadMoreStatus,
    roomManList,
    formProps,
    filterData,
  };
};
