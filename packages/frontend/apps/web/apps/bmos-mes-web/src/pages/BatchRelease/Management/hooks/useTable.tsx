import {
  reqLotReleaseManageProductTree,
  reqLotReleaseManageQueryPage,
  reqLotReleaseManageQueryPlanPage,
  reqLotReleaseMangeDownloadByUrl,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { type DataRequestFn, type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { fileStreamDownload, isNullOrUnDef } from '@bmos/utils';
import { DataNode } from 'ant-design-vue/es/tree';
import dayjs from 'dayjs';

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
      align: 'left',
      dataIndex: 'endTime',
      width: 200,
      sorter: true,
      resizable: true,
      formItemProps: {
        component: 'RangePicker',
        defaultValue: [dayjs().subtract(30, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')],
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
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
                console.log(record);
                router.push({
                  name: 'exceptionInformation',
                  query: {
                    title: t('批签发管理'),
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
      const res = await reqLotReleaseMangeDownloadByUrl(record.fileUrl);
      fileStreamDownload(res, `${record.name}--${record.templateVersion}.${record.fileUrl.split('.').pop()}`);
    } catch (error) {}
  };

  const listColumn: TableColumn[] = [
    {
      title: t('批签发模版'),
      dataIndex: 'name',
      fixed: 'left',
      width: 200,
    },
    {
      title: t('生效批签发编号'),
      dataIndex: 'no',
      width: 200,
      sorter: true,
    },
    {
      title: t('模版版本'),
      dataIndex: 'templateVersion',
      width: 100,
    },
    {
      title: t('生成人'),
      dataIndex: 'generatorName',
      width: 200,
    },
    {
      title: t('生成时间'),
      dataIndex: 'generateTime',
      width: 200,
    },
    {
      title: t('生效时间'),
      dataIndex: 'effectTime',
      width: 200,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }: any) => [
        {
          label: t('版本管理'),
          ifShow: hasPermission('120040003000001'),
          onClick: () => {
            const { id, name, planId } = record;
            router.push({
              name: 'batch-release-management-version-management',
              query: {
                lotReleaseTemplateId: id,
                name,
                planId,
              },
            });
          },
        },
        {
          label: t('下载'),
          ifShow: hasPermission('120040003000002') && !isNullOrUnDef(record.fileUrl),
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
      const { data } = await reqLotReleaseManageProductTree();
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
        lotRelease: true,
      });
    }
    return await reqLotReleaseManageQueryPlanPage({
      ...newParams,
      ...(categoryFlag ? { productCategoryId: productId } : { productId }),
      lotRelease: true,
    });
  };

  const getSecondPageList = async (params: any) => {
    if (!params.planId) return Promise.resolve({ data: [] });
    return await reqLotReleaseManageQueryPage(params);
  };

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 6,
    },
    fieldMapToTime: [['endTime', ['endDateStart', 'endDateEnd'], 'YYYY-MM-DD']],
  };

  return {
    columns: [templateColumn, listColumn],
    requests: [getFirstPageList, getSecondPageList] as DataRequestFn[],
    treeData,
    pageRef,
    formFirstProps,
  };
};
