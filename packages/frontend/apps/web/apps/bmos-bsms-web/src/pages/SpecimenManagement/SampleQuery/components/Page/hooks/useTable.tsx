import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = () => {
  const { qualifiedStatusDict, sampleInventoryStatusDict, sampleTypeDict, warehouseDict } = getDicts();
  const router = useRouter();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    // 搜索项
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInTable: true,
      formItemProps: {
        // formItemProps: {
        //   autoLink: false,
        // },
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
      title: t('库存状态'),
      dataIndex: 'inventoryStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: sampleInventoryStatusDict,
        },
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
    },
    // {
    //   title: t('标本验收'),
    //   dataIndex: 'plasmaStationNo',
    //   hideInTable: true,
    //   formItemProps: {
    //     component: 'Select',
    //     componentProps: {
    //       options: plasmaStations,
    //     },
    //   },
    // },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
    },
    {
      title: t('标本状态'),
      dataIndex: 'sampleStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
    },
    // 列表项
    {
      title: t('标本基础信息'),
      dataIndex: 'basicInfo',
      hideInSearch: true,
      children: [
        {
          title: t('来源单位'),
          dataIndex: 'originOrg',
          width: 250,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.originOrgInfo?.originOrg}</span>;
          },
        },
        {
          title: t('箱号'),
          dataIndex: 'boxId',
          width: 230,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          width: 150,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.sampleType?.name}</span>;
          },
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 150,
          sorter: true,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本状态'),
          dataIndex: 'sampleStatus',
          width: 120,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.sampleStatus?.name}</span>;
          },
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
          width: 160,
          sorter: true,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.no}</span>;
          },
        },
        {
          title: t('姓名'),
          dataIndex: 'name',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.name}</span>;
          },
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 80,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.sex?.name}</span>;
          },
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 80,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.bloodType?.name}</span>;
          },
        },
      ],
    },
    {
      title: t('库存信息'),
      dataIndex: 'storageInfo',
      hideInSearch: true,
      children: [
        {
          title: t('入库批号'),
          dataIndex: 'inWarehouseBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('入库仓库'),
          dataIndex: 'warehouse',
          hideInTable: !getWarehouseConfigByCode.value,
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return <span>{record?.warehouse?.name}</span>;
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
          sorter: true,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('库存状态'),
          dataIndex: 'warehouseStatus',
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return <span>{record?.warehouseStatus?.name}</span>;
          },
        },
      ].filter(item => !item?.hideInTable),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170020006000002'),
          onClick: () => {
            // look(record);
            // enterView(record);
            // sample-query-detail
            router.push({
              name: 'sample-query-detail',
              params: { orgSampleNo: record.orgSampleNo },
            });
          },
        },
      ],
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
