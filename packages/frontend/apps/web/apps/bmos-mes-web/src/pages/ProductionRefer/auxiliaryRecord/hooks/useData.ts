import { getPlanPageTraceable, reqProductMaterialProductTreeReq } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { DataRequestFn, FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { DataNode } from 'ant-design-vue/es/tree';
const { hasPermission } = usePermissionStore();
export const useTables = () => {
  const pageRef = ref<any>();

  const rowData = ref<Recordable>({});
  const showView = ref(false);

  const templateColumn: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 170,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
      sorter: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 160,
      resizable: true,
    },
    {
      title: t('生产开始时间'),
      dataIndex: 'startTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('生产结束时间'),
      dataIndex: 'endTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('120050011000001'),
          onClick: () => {
            rowData.value = record;
            showView.value = true;
          },
        },
      ],
    },
  ];

  const curSelect = ref<any>({});
  const treeSelect = (node: any, info: any) => {
    curSelect.value = info.node;
  };
  // 树
  const treeData = ref<DataNode[]>([]);
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
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  });

  return {
    columns: [templateColumn],
    requests: [getDatasetPageList] as DataRequestFn[],
    treeData,
    rowData,
    pageRef,
    formFirstProps,
    showView,
    treeSelect,
  };
};
