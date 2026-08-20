import { useDict } from '@/stores/dictStore';
import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';
import { useExpand } from './useExpand';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();
const { getImmuniTypeDict } = useDict();

export const useTable = (enterView: any) => {
  const { plasmaTypeDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  // 展开项的key
  const expandedRowKeys = ref<any>([]);
  // 展开列表的配置
  const expandMap = reactive<any>({});

  const expandChange = async (expandedKeys: Key[]) => {
    expandedRowKeys.value = expandedKeys;
    if (expandedKeys.length === 0) return;
    const newKey = expandedKeys[expandedKeys.length - 1];
    if (!expandMap[newKey]) {
      expandMap[newKey] = useExpand();
    } else {
      await expandMap[newKey].fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    // ===========查询参数===========
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
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
      hideInTable: true,
      formItemProps: {
        // formItemProps: {
        //   autoLink: false,
        // },
        component: ({ formModel }) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <Input v-model:value={formModel.plasmaNoBegin} allowClear placeholder={t('请输入')} />
                <span style={{ margin: '0 5px' }}>~</span>
                <Input v-model:value={formModel.plasmaNoEnd} allowClear placeholder={t('请输入')} />
              </div>
            </FormItemRest>
          );
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
      title: t('浆站出库批号'),
      dataIndex: 'syncBatchNo',
      hideInTable: true,
    },
    {
      title: t('入库仓库'),
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
      title: t('血浆免疫类型'),
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
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'containerNo',
      hideInTable: true,
      formItemProps: {
        // formItemProps: {
        //   autoLink: false,
        // },
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
    // ===========表格参数===========
    {
      title: t('血浆信息'),
      dataIndex: 'info',
      hideInSearch: true,
      children: [
        {
          title: t('来源单位'),
          dataIndex: 'originOrgInfoCode',
          width: 220,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }: any) => {
            return record?.originOrgInfo?.originOrg ?? '-';
          },
        },
        {
          title: t('出库批号'),
          dataIndex: 'syncBatchNo',
          width: 140,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('入库仓库'),
          dataIndex: 'warehouseId',
          width: 100,
          hideInTable: !getWarehouseConfigByCode.value,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }: any) => {
            return record?.warehouse?.name ?? '-';
          },
        },
        {
          title: t('数量'),
          dataIndex: 'totalNum',
          width: 100,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('血浆箱/托盘号起'),
          dataIndex: 'containerNoUp',
          width: 180,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('血浆箱/托盘号止'),
          dataIndex: 'containerNoDown',
          width: 180,
          resizable: true,
          ellipsis: true,
        },
      ].filter((item: any) => !item?.hideInTable),
    },
    {
      title: t('冷链车运输信息'),
      dataIndex: 'transport',
      hideInSearch: true,
      children: [
        {
          title: t('车牌号'),
          dataIndex: 'transportBizNum',
          width: 100,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('起运时间'),
          dataIndex: 'beginTime',
          width: 200,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('停运时间'),
          dataIndex: 'endTime',
          width: 200,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('运输温度'),
          dataIndex: 'temperature',
          width: 120,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('运输时间'),
          dataIndex: 'transitTime',
          width: 200,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('报警次数'),
          dataIndex: 'alarmNum',
          width: 120,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
      ],
    },
    {
      title: t('入库验收信息'),
      dataIndex: 'acceptance',
      hideInSearch: true,
      children: [
        {
          title: t('验收状态'),
          dataIndex: 'acceptanceStatus',
          width: 100,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return record?.acceptanceStatus?.name ?? '-';
          },
        },
        {
          title: t('验收人'),
          dataIndex: 'acceptanceByName',
          width: 100,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('验收日期'),
          dataIndex: 'acceptanceDate',
          width: 140,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
      ],
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('170040002000003'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 100,
    labelAlign: 'left',
  };

  return {
    pageRef,
    rowData,
    expandMap,
    expandedRowKeys,
    columnsFirst,
    formFirstProps,
    expandChange,
  };
};
