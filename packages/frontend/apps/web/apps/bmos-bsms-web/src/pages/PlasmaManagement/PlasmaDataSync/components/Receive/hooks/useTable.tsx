import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Select } from 'ant-design-vue';

export const useTable = () => {
  const { warehouseDict } = getDicts();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('同步人'),
      dataIndex: 'syncByName',
      width: 100,
      resizable: true,
    },
    {
      title: t('同步时间'),
      dataIndex: 'syncTime',
      width: 170,
      resizable: true,
    },
    {
      title: t('同步数量'),
      dataIndex: 'totalNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('成功数量'),
      dataIndex: 'successNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('失败数量'),
      dataIndex: 'failNum',
      width: 100,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
    },
    {
      title: t('导入批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('同步方式'),
      dataIndex: 'syncType',
      width: 130,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.syncType?.name}</span>;
      },
    },
    {
      title: t('入库仓库'),
      dataIndex: 'warehouseId',
      hideInTable: !getWarehouseConfigByCode.value,
      width: 170,
      fixed: 'right',
      resizable: true,
      customRender: ({ record }) => {
        return <Select style={{ width: '100%' }} v-model:value={record.warehouseId} options={warehouseDict} />;
      },
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  // const setRef = (el: any) => {
  //   pageRef.value = el;
  // };

  // const fetchData = async (params: any) => {
  //   pageRef.value?.fetchData(0, params);
  // };

  return {
    tableRef,
    columns,
    // setRef,
    // fetchData,
  };
};
