import { usePermissionStore } from '@/stores';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = ({ editModalOpen }: { editModalOpen: Ref<boolean> }) => {
  const pageRef = ref<any>();
  const updateTableData = () => pageRef.value?.fetchData(0);
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  // 第一个table 行数据
  const firstRowData = ref<any>({});

  const editFun = (record: any) => {
    firstRowData.value = record;
    editModalOpen.value = true;
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('检验项目编号'),
      dataIndex: 'itemNo',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('检验项目名称'),
      dataIndex: 'itemName',
      width: 100,
    },
    {
      title: t('标准规定'),
      dataIndex: 'standard',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('通过标准'),
      dataIndex: 'passStandard',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }) => {
        return (
          <span
            class='link-text'
            onClick={() => {
              editFun(record);
            }}>
            {record.passStandardValue}
          </span>
        );
      },
    },
    {
      title: t('修约规则'),
      dataIndex: 'roundingRule',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }) => {
        return (
          <span
            class='link-text'
            onClick={() => {
              editFun(record);
            }}>
            {record.roundingRuleValue}
          </span>
        );
      },
    },
    {
      title: t('操作人'),
      dataIndex: 'standardBy',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('操作日期'),
      dataIndex: 'standardTime',
      sorter: true,
      width: 120,
      hideInSearch: true,
      customRender: ({ record }) => {
        return getDateFormat(record.standardTime);
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }: any) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('210080004000001'),
          onClick: () => {
            editFun(record);
          },
        },
      ],
    },
  ];
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    labelWidth: 120,
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  });
  return {
    columnsFirst,
    firstRowData,
    pageRef,
    updateTableData,
    formFirstProps,
  };
};
