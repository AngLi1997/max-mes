import { LabelList } from '@/components/Sign/type';
import { usePermissionCodeUserList } from '@/hooks';
import { reqSendOutCancel } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';

export type UseTableParams = {};

export const useTable = ({}: UseTableParams) => {
  const router = useRouter();
  const { hasPermission } = usePermissionStore();
  const { getPermissionCodeUserList, permissionCodeUserList } = usePermissionCodeUserList();

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('取消人'),
    },
    {
      label: t('复核人'),
    },
  ];

  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      fixed: 'left',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productCode',
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
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('领料单'),
      dataIndex: 'pullOrderNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('计划人'),
      dataIndex: 'submitterName',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('计划时间'),
      dataIndex: 'submitTime',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }, action) => [
        {
          label: t('发料'),
          ifShow: hasPermission('150020003000001'),
          onClick: () => {
            router.push({
              name: 'batch-delivery',
              query: {
                id: record.id,
              },
            });
          },
        },
        {
          label: t('取消'),
          ifShow: hasPermission('150020003000003'),
          onClick: async () => {
            await getPermissionCodeUserList('150020003000002');
            rowData.value = record;
            signOpen.value = true;
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

  const signSuccess = async () => {
    try {
      await reqSendOutCancel(rowData.value.id);
      message.success(t('取消成功'));
      updateTable();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    signOpen,
    rowData,
    permissionCodeUserList,
    labelList,
    signSuccess,
  };
};
