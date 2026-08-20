import { reqBusinessParameterDetailGET } from '@/api';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps } from '@bmos/components';
import { Recordable, TableInstance, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { Ref } from 'vue';

export type UseTableParams = {
  editParameterModalOpen: Ref<boolean>;
};

export const useTable = ({ editParameterModalOpen }: UseTableParams) => {
  const tableInstance = ref<TableInstance>();
  // 选择的某一行数据
  const rowData = ref<Recordable>({});

  const { hasPermission } = usePermissionStore();

  // businessType options
  const businessTypeOptions = ref<Recordable[]>([
    {
      label: t('业务'),
      value: 'BUSINESS',
    },
    {
      label: t('系统'),
      value: 'SYSTEM',
    },
  ]);
  // valueType options
  const valueTypeOptions = ref<Recordable[]>([
    {
      label: t('字符串'),
      value: 'STRING',
    },
    {
      label: t('数字'),
      value: 'NUMBER',
    },
    {
      label: t('布尔'),
      value: 'BOOLEAN',
    },
    {
      label: t('枚举'),
      value: 'ENUM',
    },
    {
      label: 'Json',
      value: 'json',
    },
  ]);

  const belongOptions = ref<Recordable[]>([]);
  const getBelongOptions = async () => {
    try {
      const { data }: any = await reqBusinessParameterDetailGET('application');
      tableInstance.value?.queryFormRef?.updateSchema({
        field: 'belong',
        componentProps: {
          options: JSON.parse(data?.valueRange),
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const columns: TableColumn[] = [
    {
      title: t('参数名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('代码'),
      dataIndex: 'code',
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('值'),
      hideInSearch: true,
      dataIndex: 'value',
      width: 190,
      resizable: true,
    },
    {
      title: t('值类型'),
      dataIndex: 'valueType',
      width: 190,
      resizable: true,
      customRender: ({ record }) => {
        return record.valueType.label;
      },
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: valueTypeOptions.value,
        }),
      },
    },
    {
      title: t('参数业务类型'),
      dataIndex: 'businessType',
      width: 190,
      resizable: true,
      customRender: ({ record }) => {
        return record.businessType.label;
      },
      formItemProps: {
        component: 'Select',
        labelWidth: 130,
        componentProps: () => ({
          options: businessTypeOptions.value,
        }),
      },
    },
    {
      title: t('所属应用'),
      dataIndex: 'belong',
      width: 190,
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: belongOptions.value,
        }),
      },
    },

    {
      title: t('描述'),
      hideInSearch: true,
      dataIndex: 'description',
      width: 190,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: () => hasPermission('100010002001001'),
          onClick: () => {
            rowData.value = record;
            editParameterModalOpen.value = true;
          },
        },
      ],
    },
  ];

  const formProps = reactive<Partial<FormProps>>({
    // showAdvancedButton: false,
    // labelCol: { span: 8 },
  });

  onMounted(() => {
    getBelongOptions();
  });

  return {
    tableInstance,
    columns,
    formProps,
    rowData,
    businessTypeOptions,
    valueTypeOptions,
    belongOptions,
  };
};
