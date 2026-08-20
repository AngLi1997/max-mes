import { reqProductMaterialProductTreeReq } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { BMStateTag, type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { DataNode } from 'ant-design-vue/es/tree';
import { AuditStatus, AuditStatusClassMap } from '../type';

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
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 100,
      sorter: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 100,
    },
    {
      title: t('生产开始时间'),
      dataIndex: 'startTime',
      width: 140,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'start',
      width: 110,
      hideInSearch: true,
      fixed: 'right',
      customRender: ({ record }) => {
        return record.auditingCount ? (
          <BMStateTag type={AuditStatusClassMap.get(AuditStatus.UNDER_AUDIT)?.type}>
            {AuditStatusClassMap.get(AuditStatus.UNDER_AUDIT)?.stateName}
          </BMStateTag>
        ) : (
          <BMStateTag type={AuditStatusClassMap.get(AuditStatus.AUDIT_COMPLETED)?.type}>
            {AuditStatusClassMap.get(AuditStatus.AUDIT_COMPLETED)?.stateName}
          </BMStateTag>
        );
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('120050009000001'),
          onClick: () => {
            firstRowData.value = record;
            router.push({
              name: 'product-audit-progress-detail',
              query: {
                id: record.id,
                processName: record.processName,
                processVersionId: record.processVersionId,
              },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
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
