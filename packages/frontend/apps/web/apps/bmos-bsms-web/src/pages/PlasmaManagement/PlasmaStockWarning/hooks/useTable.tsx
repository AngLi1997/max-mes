import { useDict } from '@/stores/dictStore';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';

const { getPlasmaStations } = usePlasmaStation();
const { getImmuniTypeDict } = useDict();

export const useTable = () => {
  const { plasmaAppearanceDict, plasmaTypeDict, qualityStatusDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
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
          // options: plasmaStations,
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
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
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
      title: t('血浆状态'),
      dataIndex: 'plasmaStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: qualityStatusDict,
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
          dataIndex: 'originOrg',
          width: 220,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 170,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('血浆箱/托盘号'),
          dataIndex: 'containerNo',
          width: 180,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('阈值类型'),
          dataIndex: 'thresholdType',
          width: 160,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.thresholdType?.name ?? '-';
          },
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
          title: t('血浆状态'),
          dataIndex: 'plasmaStatus',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaStatus?.name ?? '-';
          },
        },
        {
          title: t('血浆类型'),
          dataIndex: 'plasmaType',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaType?.name ?? '-';
          },
        },
        {
          title: t('免疫类型'),
          dataIndex: 'immunityType',
          width: 140,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('限制级血浆'),
          dataIndex: 'restrictedFlag',
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.restrictedFlag?.name ?? '-';
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
          title: t('编号'),
          dataIndex: 'plasmaDonorNo',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.no ?? '-';
          },
        },
        {
          title: t('姓名'),
          dataIndex: 'plasmaDonorName',
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
          title: t('所在仓库'),
          dataIndex: 'warehouseId',
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return record?.warehouse?.name ?? '-';
          },
        },
        {
          title: t('入库人'),
          dataIndex: 'inWarehouseByName',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('入库日期'),
          dataIndex: 'inWarehouseTime',
          width: 150,
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
          dataIndex: 'bigContainerNo',
          width: 150,
          ellipsis: true,
          resizable: true,
        },
      ].filter((item: any) => !item.hideInTable),
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
