import { RemarkDetail } from '@/components/RemarkModal';
import { usePermissionStore } from '@/stores';
import { useDict } from '@/stores/dictStore';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = ({ editModalOpen }: { editModalOpen: Ref<boolean> }) => {
  const { getDict } = useDict();
  const { getDateFormat } = useConfig();
  const pageRef = ref<any>();
  const { hasPermission } = usePermissionStore();
  const updateTableData = () => pageRef.value?.fetchData(0);
  const { projectTypeDict, yesOrNoDictOther } = getDicts();
  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);
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
      title: t('检验类型'),
      dataIndex: 'inspectType',
      width: 100,
    },
    {
      title: t('项目类型'),
      dataIndex: 'itemType',
      width: 100,
      customRender: ({ record }) => {
        return record.itemType?.label ?? '-';
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: projectTypeDict,
        },
      },
    },
    {
      title: t('检验方式'),
      dataIndex: 'inspectMethodName',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('检验方式'),
      dataIndex: 'inspectMethod',
      width: 100,
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          request: async () => {
            return getDict('检验方式');
          },
        },
      },
    },
    {
      title: t('默认设备'),
      dataIndex: 'defaultInstrument',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('是否复检'),
      dataIndex: 'reInspect',
      width: 100,
      customRender: ({ record }) => {
        return record.reInspect?.label ?? '-';
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: yesOrNoDictOther,
        },
      },
    },
    {
      title: t('检验项目描述'),
      dataIndex: 'description',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('操作人'),
      dataIndex: 'standardBy',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('更新日期'),
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
      width: 150,
      actions: ({ record }: any) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('210080003000001'),
          onClick: () => {
            editModalOpen.value = true;
            firstRowData.value = record;
          },
        },
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('项目备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    labelWidth: 120,
  });
  return {
    columnsFirst,
    firstRowData,
    pageRef,
    updateTableData,
    formFirstProps,
    remarkModalOpen,
    remarkDetails,
  };
};
