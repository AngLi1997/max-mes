import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const rowData = ref<Recordable>({});
  const columns: TableColumn[] = [
    {
      title: '',
      dataIndex: 'keyword',
      // resizable: true,
      hideInTable: true,
      formItemProps: {
        defaultValue: '',
        componentProps: {
          placeholder: t('实验包编码/实验包名称',)
        }
      },
    },
    {
      title: t('实验包编码'),
      dataIndex: 'code',
      // resizable: true,
      hideInSearch: true,
      width: 100,
    },
    {
      title: t('实验包名称'),
      dataIndex: 'name',
      // resizable: true,
      hideInSearch: true,
      width: 100,
    }
  ];

  const formProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 11,
    },
    baseColProps: {
      span: 12,
    },
    labelCol: {
      span: 1,
    }
  });

  return {
    columns,
    formProps,
    rowData,
  };
};