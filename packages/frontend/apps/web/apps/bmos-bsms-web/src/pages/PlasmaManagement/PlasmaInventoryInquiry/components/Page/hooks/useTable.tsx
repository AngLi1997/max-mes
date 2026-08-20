import { getPlasmaColorList } from '@/services';
import { useDict } from '@/stores/dictStore';
import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();
const { getImmuniTypeDict } = useDict();

export const useTable = () => {
  const { plasmaAppearanceDict, plasmaTypeDict, warehouseDict, yesOrNoDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const router = useRouter();

  getPlasmaColorList({
    pageNum: 1,
    pageSize: 50,
  }).then((res: any) => {
    if (res.code !== 0) return;
    res.data.list.forEach((item: any) => {
      colorMap.value[item.id] = item.colour;
    });
  });

  const colorMap = ref<any>({});

  const columnsFirst: TableColumn[] = [
    // 筛选项
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.plasmaNoUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.plasmaNoDown} allowClear placeholder={t('请输入')} />
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
      title: t('血浆类型'),
      dataIndex: 'plasmaType',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: plasmaTypeDict,
        },
      },
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      hideInTable: true,
      formItemProps: {
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.containerNoUp} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.containerNoDown} allowClear placeholder={t('请输入')} />
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
      title: t('所在仓库'),
      dataIndex: 'warehouseId',
      hideInTable: true,
      hideInSearch: !getWarehouseConfigByCode.value,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          // options: immunityTypeDict,
          request: getImmuniTypeDict,
        },
      },
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
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('血浆外观'),
      dataIndex: 'appearanceResult',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: plasmaAppearanceDict,
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
    {
      title: t('限制级血浆'),
      dataIndex: 'restrictedFlag',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: yesOrNoDict,
        },
      },
    },
    // 列表项
    {
      title: t('血浆基础信息'),
      dataIndex: 'plasmaBaseInfo',
      hideInSearch: true,
      children: [
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          width: 260,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.originOrgInfo?.originOrg ?? '-'}</span>;
          },
        },
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 180,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('血浆原箱/托盘号'),
          dataIndex: 'primeContainerNo',
          width: 160,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('血浆箱/托盘号'),
          dataIndex: 'containerNo',
          width: 160,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('浆站出库批号'),
          dataIndex: 'syncBatchNo',
          width: 160,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 140,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('对应编号'),
          dataIndex: 'corrPlasmaNo',
          width: 160,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('对应类型'),
          dataIndex: 'corrRelationType',
          width: 100,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.corrRelationType?.name ?? '-'}</span>;
          },
        },
        {
          title: t('血浆外观'),
          dataIndex: 'appearanceResult',
          width: 100,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.appearanceResult?.name ?? '-'}</span>;
          },
        },
        {
          title: t('血浆状态'),
          dataIndex: 'plasmaStatus',
          width: 160,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return (
              <span style={{ color: colorMap.value[record?.plasmaStatus?.value] }}>
                {record?.plasmaStatus?.name ?? '-'}
              </span>
            );
          },
        },
        {
          title: t('血浆类型'),
          dataIndex: 'plasmaType',
          width: 100,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaType?.name ?? '-'}</span>;
          },
        },
        {
          title: t('免疫类型'),
          dataIndex: 'immunityType',
          width: 120,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('限制级血浆'),
          dataIndex: 'restrictedFlag',
          width: 120,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.restrictedFlag?.name ?? '-'}</span>;
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
          title: t('编号'),
          dataIndex: 'no',
          width: 160,
          sorter: true,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.no ?? '-'}</span>;
          },
        },
        {
          title: t('姓名'),
          dataIndex: 'name',
          width: 100,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.name ?? '-'}</span>;
          },
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 80,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.sex?.name ?? '-'}</span>;
          },
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 80,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaDonorInfo?.bloodType?.name ?? '-'}</span>;
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
          width: 160,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('所在仓库'),
          dataIndex: 'warehouse',
          width: 100,
          hideInTable: !getWarehouseConfigByCode.value,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }: any) => {
            return <span>{record?.warehouse?.name ?? '-'}</span>;
          },
        },
        {
          title: t('入库人'),
          dataIndex: 'inWarehouseByName',
          width: 100,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseTime',
          width: 150,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('货位号'),
          dataIndex: 'cargoSpaceNo',
          width: 100,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('大托盘号'),
          dataIndex: 'bigContainerNo',
          width: 140,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('库存状态'),
          dataIndex: 'plasmaWarehouseStatus',
          width: 100,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }: any) => {
            return <span>{record?.plasmaWarehouseStatus?.name ?? '-'}</span>;
          },
        },
      ].filter((item: any) => !item.hideInTable),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170040009000002'),
          onClick: () => {
            // look(record);
            router.push({
              name: 'plasma-inventory-inquiry-detail',
              params: { plasmaOrgNo: record.plasmaOrgNo },
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
