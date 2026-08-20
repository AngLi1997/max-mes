import { reqLotReleaseManageProductTree, reqLotReleaseMangeDownloadByUrl } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Recordable, RenderCallbackParams, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { fileStreamDownload, loopSelectableTree } from '@bmos/utils';

export type UseTableParams = {};

export const useTable = ({}: UseTableParams) => {
  const router = useRouter();
  const { hasPermission } = usePermissionStore();

  const rowData = ref<Recordable>({});

  const approvalModalOpen = ref<boolean>(false);

  const downFile = async (record: Recordable) => {
    try {
      const res = await reqLotReleaseMangeDownloadByUrl(record.fileUrl);
      fileStreamDownload(res, `${record.name}--${record.templateVersion}.${record.fileUrl.split('.').pop()}`);
    } catch (error) {}
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('批签发编号'),
      dataIndex: 'no',
      fixed: 'left',
      width: 150,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'specification',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 150,
      sorter: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productId',
      width: 150,
      hideInTable: true,
      formItemProps: {
        component: 'TreeSelect',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'showName',
            request: async () => {
              try {
                const { data } = await reqLotReleaseManageProductTree();
                return loopSelectableTree(data, 'categoryFlag', true);
              } catch (error) {}
            },
          };
        },
      },
    },
    {
      title: t('批签发模板'),
      dataIndex: 'name',
      width: 150,
      hideInSearch: true,
    },
    {
      title: t('批签发模板'),
      dataIndex: 'templateName',
      width: 150,
      hideInTable: true,
    },
    {
      title: t('版本'),
      dataIndex: 'templateVersion',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('发起人'),
      dataIndex: 'submitterName',
      width: 160,
      hideInSearch: true,
    },
    {
      title: t('发起时间'),
      dataIndex: 'processStartTime',
      width: 180,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: ({ record }, action) => [
        {
          label: t('审核处理'),
          ifShow: hasPermission('120040005000001'),
          onClick: () => {
            rowData.value = record;
            approvalModalOpen.value = true;
          },
        },
        {
          label: t('下载'),
          ifShow: hasPermission('120040005000002'),
          onClick: () => {
            downFile(record);
          },
        },
        {
          label: t('审核进度'),
          ifShow: hasPermission('120040005000003'),
          onClick: () => {
            router.push({
              name: 'batch-release-review-schedule',
              query: {
                processInstanceId: record.processInstanceId,
                deploymentId: record.deploymentId,
              },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
  };

  const pageRef = ref<any>(null);
  const updateTable = () => {
    pageRef.value?.fetchData(0);
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    rowData,
    approvalModalOpen,
  };
};
