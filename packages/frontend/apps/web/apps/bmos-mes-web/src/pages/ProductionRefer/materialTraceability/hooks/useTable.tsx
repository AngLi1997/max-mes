import { getMaterialTraceData, getPlanPageTraceable, reqProductMaterialProductTreeReq } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { DataRequestFn, FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';

export const useTables = () => {
  const { hasPermission } = usePermissionStore();
  const pageRef = ref<any>();

  const curSelect = ref<any>({});
  const rowData = ref<Recordable>({});
  const showType = ref('page'); //page列表页 detail 详情 lineage 谱系

  const templateColumn: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 200,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 200,
      resizable: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 200,
      resizable: true,
    },
    {
      title: t('开始时间'),
      dataIndex: 'selectTime',
      width: 180,
      resizable: true,
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
      },
    },
    {
      title: t('生产开始时间'),
      dataIndex: 'startTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      formItemProps: {
        component: 'RangePicker',
      },
    },
    {
      title: t('生产结束时间'),
      dataIndex: 'endTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: ({ record }) => [
        {
          label: t('物料详情'),
          ifShow: hasPermission('120050012000001'),
          onClick: async () => {
            try {
              const { data } = await getMaterialTraceData(record.id);
              rowData.value = {
                ...record,
                materialTraceData: data,
              };
              showType.value = 'detail';
            } catch (error: any) {
              error.message && message.error(error.message);
            }
          },
        },
        {
          label: t('物料谱系'),
          ifShow: hasPermission('120050012000002'),
          onClick: async () => {
            try {
              const { data } = await getMaterialTraceData(record.id);
              rowData.value = {
                ...record,
                materialTraceData: data,
              };
              showType.value = 'lineage';
            } catch (error: any) {
              error.message && message.error(error.message);
            }
          },
        },
      ],
    },
  ];

  // 树
  const treeData = ref<DataNode[]>([]);
  const treeSelect = (node: any, info: any) => {
    curSelect.value = info.node;
  };
  const getTreeData = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          showName: t('全部'),
          categoryFlag: true,
          key: 'all',
          children: data,
        },
      ];
    } catch (error) {}
  };
  onMounted(() => {
    getTreeData();
  });

  const getDatasetPageList = async (params: any) => {
    let newParams = { ...params };
    if (params.productId && params.productId !== 'all') {
      if (curSelect.value.categoryFlag) {
        // 分类
        newParams.productCategoryId = params.productId;
      } else {
        newParams.productIds = [params.productId];
      }
    }
    return await getPlanPageTraceable({ ...newParams });
  };

  const formFirstProps = reactive<Partial<FormProps>>({
    fieldMapToTime: [['selectTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
    showAdvancedButton: false,
    labelWidth: 100,
    actionColOptions: {
      span: 6,
    },
  });

  return {
    columns: [templateColumn],
    requests: [getDatasetPageList] as DataRequestFn[],
    treeData,
    rowData,
    pageRef,
    formFirstProps,
    showType,
    treeSelect,
  };
};
