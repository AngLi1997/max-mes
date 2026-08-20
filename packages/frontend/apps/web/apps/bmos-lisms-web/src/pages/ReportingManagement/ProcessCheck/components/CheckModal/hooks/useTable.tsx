import type { TableColumn } from '@bmos/components';
import { RadioGroup } from 'ant-design-vue';

export const useTable = () => {
  const { yesOrNoDictOther } = getDicts();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: '类型',
      dataIndex: 'type',
      width: 40,
      customCell: record => {
        return {
          rowSpan: record.rowSpan,
        };
      },
    },
    {
      title: '控制点',
      dataIndex: 'point',
      width: 200,
    },
    {
      title: '条件值',
      dataIndex: 'checked',
      width: 50,
      customRender: ({ record }) => {
        return <RadioGroup v-model:value={record.checked} options={yesOrNoDictOther} />;
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
