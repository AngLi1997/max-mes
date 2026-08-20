import { BMTable } from '@bmos/components';
import { Descriptions, DescriptionsItem } from 'ant-design-vue';

import { useTable } from './useTable';

export const useDetail = (info: any) => {
  const { tableRef, columns, loadData, pagination } = useTable(info.sortingTaskId);
  const descriptionItems = [
    {
      label: t('入库批号'),
      field: 'inWarehouseBatchNo',
    },
    {
      label: t('核查批号'),
      field: 'checkNo',
    },
    {
      label: t('分拣数量'),
      field: 'totalNum',
    },
    {
      label: t('总重量'),
      field: 'totalWeight',
    },
  ];

  const cardItems = ref([
    {
      title: t('基础信息'),
      slot: () => (
        <>
          <Descriptions size='small' column={4} bordered={true}>
            {descriptionItems.map(item => (
              <DescriptionsItem label={item.label}>{info?.[item.field]}</DescriptionsItem>
            ))}
          </Descriptions>
        </>
      ),
    },
    {
      title: t('血浆明细'),
      slot: () => (
        <div style={{ height: '520px' }}>
          <BMTable
            ref={tableRef}
            row-key='id'
            columns={columns}
            search={false}
            showToolBar={false}
            dataRequest={loadData}
            pagination={pagination}></BMTable>
        </div>
      ),
    },
  ]);

  return {
    cardItems,
  };
};
