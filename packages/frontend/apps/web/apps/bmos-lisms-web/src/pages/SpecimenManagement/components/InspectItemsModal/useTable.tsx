import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Checkbox } from 'ant-design-vue';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('项目名称'),
      dataIndex: 'name',
      width: 160,
    },
    {
      title: t('是否检验'),
      dataIndex: 'selected',
      width: 80,
      customRender: ({ record }) => {
        return <Checkbox checked={record.selected} disabled />;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
