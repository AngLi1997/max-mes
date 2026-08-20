import { reqProductMaterialProductTreeReq } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { DataNode } from 'ant-design-vue/es/tree';

export const useTable = () => {
  const { hasPermission } = usePermissionStore();

  const pageRef = ref<any>();
  const router = useRouter();

  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      fixed: 'left',
      width: 100,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 100,
      resizable: true,
      sorter: true,
    },
    {
      title: t('异常数量'),
      dataIndex: 'exceptionCount',
      width: 120,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => {
        if (record.exceptionCount == 0) {
          return <div>0</div>;
        } else {
          return (
            <div
              style='color: #2871FF;cursor: pointer;'
              onClick={() => {
                router.push({
                  name: 'exceptionInformation',
                  query: {
                    title: t('生产进度'),
                    productPlanId: record.productPlanId, //必传,生产计划id
                  },
                });
              }}>
              {record.exceptionCount}
            </div>
          );
        }
      },
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
      title: t('开始时间'),
      dataIndex: 'startTime',
      width: 120,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 180,
      actions: ({ record }: any) => [
        {
          label: t('生产进度'),
          ifShow: hasPermission('120050008000001'),
          onClick: () => {
            firstRowData.value = record;
            router.push({
              name: 'product-progress-procedure',
              query: {
                productPlanId: record.productPlanId,
                executeProcessInstanceId: record.executeProcessInstanceId,
                processName: record.processName,
              },
            });
          },
        },
        {
          label: t('修订记录'),
          ifShow: hasPermission('120050008000002'),
          onClick: () => {
            firstRowData.value = record;
            router.push({
              name: 'product-progress-revision',
              query: {
                productPlanId: record.productPlanId,
              },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    fieldMapToTime: [['selectTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
    showAdvancedButton: false,
  });

  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      treeData.value = [
        {
          id: 'all',
          name: t('全部'),
          showName: t('全部'),
          key: 'all',
          categoryFlag: true,
          children: data,
        },
      ];
    } catch (error) {
      //
    }
  };
  onMounted(() => {
    getTreeData();
  });

  return {
    columnsFirst,
    formFirstProps,
    firstRowData,
    pageRef,
    treeData,
  };
};
