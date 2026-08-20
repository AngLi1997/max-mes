import type { TableColumn } from '@bmos/components';
import { BMEllipsis } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Button } from 'ant-design-vue';

export type UseTableParams = {
  rowClick: Function;
};
export const useTable = ({ rowClick }: UseTableParams) => {
  const tableData = [
    {
      id: 'productPlan231001',
      name: '人血白蛋白-001',
      code: 'C001',
      specifications: '10g/支',
      childrenName: '人血白蛋白投浆工艺',
      proNo: 'F1+II +Ⅲ压滤',
      batchNo: 'YX001231001',
      date: '2023-11-28',
      proType: 0,
    },
  ];
  const columns: TableColumn[] = [
    {
      title: t('指令单编号'),
      align: 'left',
      fixed: 'left',
      dataIndex: 'planNo',
      sorter: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('产品名称'),
      align: 'left',
      dataIndex: 'productName',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品编码'),
      align: 'left',
      dataIndex: 'productMergeCode',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品规格'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'productSpecification',
    },
    {
      title: t('工艺名称'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'processName',
    },
    {
      title: t('工序节点'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'procedureModelName',
    },
    {
      title: t('生产批号'),
      align: 'left',
      dataIndex: 'batchNo',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('计划生产时间'),
      align: 'left',
      dataIndex: 'productDate',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('指令单类型'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'type',
      customRender: ({ record }) => <div>{record.type.label}</div>,
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => (
        <div class='bmos-action-list'>
          <Button
            v-hasAuth='120030004000001'
            type='link'
            onClick={() => rowClick(record)}
            style='max-width: 100px; min-width: 40px;color: #2871FF;'>
            <BMEllipsis style='cursor: pointer;'>{t('确认')}</BMEllipsis>
          </Button>
        </div>
      ),
    },
  ];

  return {
    tableData,
    columns,
  };
};
