import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = () => {
  const { sampleTypeDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    // 筛选项
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.sampleNoUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.sampleNoDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: sampleTypeDict,
        },
      },
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.boxIdUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.boxIdDown} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
        },
      },
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInTable: true,
    },
    {
      title: t('入库仓库'),
      dataIndex: 'warehouseId',
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    // 列表项
    {
      title: t('标本基础信息'),
      dataIndex: 'plasmaBaseInfo',
      hideInSearch: true,
      children: [
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          width: 170,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.originOrgInfo?.originOrg ?? '-';
          },
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 200,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本箱号'),
          dataIndex: 'boxId',
          width: 180,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('有效期'),
          dataIndex: 'validityDate',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('剩余时间(天)'),
          dataIndex: 'remainingTime',
          width: 150,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.sampleType?.name ?? '-';
          },
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 150,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('献浆者信息'),
      dataIndex: 'plasmaDonorInfo',
      hideInSearch: true,
      children: [
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('姓名'),
          dataIndex: 'name',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.name ?? '-';
          },
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 80,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.sex?.name ?? '-';
          },
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 80,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.bloodType?.name ?? '-';
          },
        },
      ],
    },
    {
      title: t('库存信息'),
      dataIndex: 'stockInfo',
      hideInSearch: true,
      children: [
        {
          title: t('入库批号'),
          dataIndex: 'inWarehouseBatchNo',
          width: 170,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('入库仓库'),
          hideInTable: !getWarehouseConfigByCode.value,
          dataIndex: 'warehouse',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return record?.warehouse?.name ?? '-';
          },
        },
        {
          title: t('入库人'),
          dataIndex: 'inWarehouseBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseTime',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('货位号'),
          dataIndex: 'cargoSpaceNo',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('大托盘号'),
          dataIndex: 'palletNo',
          width: 150,
          ellipsis: true,
          resizable: true,
        },
      ].filter(item => !item?.hideInTable),
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 105,
    labelAlign: 'left',
    fieldMapToTime: [
      ['slurryDate', ['slurryDateUp', 'slurryDateDown'], 'YYYY-MM-DD'],
      ['inWarehouseDate', ['inWarehouseDateUp', 'inWarehouseDateDown'], 'YYYY-MM-DD'],
    ],
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    paginationFirst,
  };
};
