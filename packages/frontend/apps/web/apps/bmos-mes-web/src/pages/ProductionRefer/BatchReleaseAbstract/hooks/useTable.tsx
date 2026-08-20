import {
  lotSummaryDelete,
  lotSummaryQueryDetail,
  lotSummaryQueryPage,
  reqProductMaterialProductTreeReq,
} from '@/services';
import type { DataRequestFn, FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';

export const useTables = () => {
  const pageRef = ref<any>();

  const curSelect = ref<any>({});
  const rowData = ref<Recordable>({});
  const showView = ref(false);
  const showSearch = ref(false);
  const type = ref('add');
  const disabled = ref(false);
  const addAbstract = ref();

  const templateColumn: TableColumn[] = [
    {
      title: t('摘要名称'),
      dataIndex: 'name',
      width: 200,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 200,
      resizable: true,
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
      title: t('规格'),
      dataIndex: 'productSpecification',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
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
      actions: ({ record }, tableAction) => [
        {
          label: t('编辑'),
          onClick: async () => {
            type.value = 'update';
            disabled.value = false;
            showView.value = true;
            const { data } = await lotSummaryQueryDetail({ id: record.id });
            rowData.value = { ...record, ...data };
            nextTick(() => {
              addAbstract.value.showUpdateData(rowData.value);
            });
          },
        },
        {
          label: t('批次查询'),
          onClick: async () => {
            const { data } = await lotSummaryQueryDetail({ id: record.id });
            rowData.value = { ...record, ...data };
            showSearch.value = true;
          },
        },
        {
          label: t('查看'),
          onClick: async () => {
            disabled.value = true;
            showView.value = true;
            const { data } = await lotSummaryQueryDetail({ id: record.id });
            rowData.value = { ...record, ...data };
            nextTick(() => {
              addAbstract.value.showUpdateData(rowData.value);
            });
          },
        },
        {
          label: t('删除'),
          danger: true,
          onClick: async () => {
            try {
              Modal.confirm({
                title: t('是否删除该数据'),
                content: t('数据删除后无法恢复，是否删除？'),
                async onOk() {
                  try {
                    await lotSummaryDelete({ id: record.id });
                    message.success(t('删除成功'));
                    pageRef.value.fetchData();
                    return Promise.resolve();
                  } catch (error: any) {
                    error.message && message.error(error.message);
                    return Promise.reject();
                  }
                },
              });
            } catch (error: any) {
              error?.message && message.error(error.message);
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
    } catch (error) { }
  };
  onMounted(() => {
    getTreeData();
  });

  const getDatasetPageList = async (params: any) => {
    let newParams = { ...params }
    if (curSelect.value.id !== 'all') {
      newParams.productCategoryId = curSelect.value.id;
    }
    return await lotSummaryQueryPage({ ...newParams });
  };

  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
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
    showSearch,
    type,
    disabled,
    treeSelect,
    addAbstract,
  };
};
