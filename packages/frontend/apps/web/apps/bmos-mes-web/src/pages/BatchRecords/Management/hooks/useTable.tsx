import {
  reqLotRecordsManageDownloadById,
  reqLotRecordsManageProductTree,
  reqLotRecordsManageQueryPage,
  reqLotReleaseManageQueryPlanPage,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { type DataRequestFn, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { fileStreamDownload, isNullOrUnDef } from '@bmos/utils';
import { DataNode } from 'ant-design-vue/es/tree';

export const useTables = () => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();

  const pageRef = ref<any>();

  const templateColumn: TableColumn[] = [
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      fixed: 'left',
      width: 200,
      sorter: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 200,
    },
    {
      title: t('生产开始时间'),
      dataIndex: 'startTime',
      width: 200,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('生产结束时间'),
      dataIndex: 'endTime',
      width: 200,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('异常数量'),
      dataIndex: 'errorCount',
      width: 200,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return (
          <span
            style={{
              color: record.errorCount > 0 ? '#2871FF' : '#242526',
              cursor: record.errorCount > 0 ? 'pointer' : 'default',
            }}
            onClick={() => {
              if (record.errorCount > 0) {
                router.push({
                  name: 'exceptionInformation',
                  query: {
                    title: t('批记录管理'),
                    productPlanId: record.id,
                  },
                });
              }
            }}>
            {record.errorCount}
          </span>
        );
      },
    },
  ];

  const downFile = async (record: Recordable) => {
    try {
      const res = await reqLotRecordsManageDownloadById({
        archiveId: record.archiveId,
      });
      fileStreamDownload(res, `${record.templateName}${record.version}.pdf`, 'application/pdf');
    } catch (error) {}
  };

  const listColumn: TableColumn[] = [
    {
      title: t('批记录模版'),
      dataIndex: 'templateName',
      fixed: 'left',
      width: 200,
    },
    {
      title: t('生效批记录编号'),
      dataIndex: 'effectiveNo',
      width: 200,
      sorter: true,
    },
    {
      title: t('模版版本'),
      dataIndex: 'version',
      width: 200,
    },
    {
      title: t('生成人'),
      dataIndex: 'operatorName',
      width: 200,
    },
    {
      title: t('生成时间'),
      dataIndex: 'archiveTime',
      width: 200,
    },
    {
      title: t('生效时间'),
      dataIndex: 'effectiveTime',
      width: 200,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('版本管理'),
          ifShow: hasPermission('120080002000001'),
          onClick: () => {
            const { templateInfoId, planId } = record;
            router.push({
              name: 'batch-records-management-version-management',
              query: {
                templateInfoId,
                planId,
              },
            });
          },
        },
        {
          label: t('下载'),
          ifShow: hasPermission('120080002000002') && !isNullOrUnDef(record.archiveId),
          onClick: () => {
            downFile(record);
          },
        },
      ],
    },
  ];

  // 树
  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await reqLotRecordsManageProductTree();
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

  const getFirstPageList = async (params: any) => {
    const { productId, categoryFlag, ...newParams } = params;
    if (!params.productId || params.productId === 'all') {
      return await reqLotReleaseManageQueryPlanPage({
        ...newParams,
        lotRelease: false,
      });
    }
    return await reqLotReleaseManageQueryPlanPage({
      ...newParams,
      ...(categoryFlag ? { productCategoryId: productId } : { productId }),
      lotRelease: false,
    });
  };

  const getSecondPageList = async (params: any) => {
    if (!params.planId) return Promise.resolve({ data: [] });
    return await reqLotRecordsManageQueryPage(params);
  };

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  };

  return {
    columns: [templateColumn, listColumn],
    requests: [getFirstPageList, getSecondPageList] as DataRequestFn[],
    treeData,
    pageRef,
    formFirstProps,
  };
};
