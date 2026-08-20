import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Key, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { FormItemRest, Input } from 'ant-design-vue';
import { useExpand } from './useExpand';

const { getPlasmaStations } = usePlasmaStation();
const { hasPermission } = usePermissionStore();

export const useTable = (enterView: any) => {
  const { acceptanceResultDict, sampleTypeDict, warehouseDict } = getDicts();
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
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 170,
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
        // formItemProps: {
        //   autoLink: false,
        // },
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
      title: t('出库批号'),
      dataIndex: 'syncBatchNo',
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
      title: t('献浆者姓名'),
      dataIndex: 'plasmaDonorName',
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
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('验收状态'),
      dataIndex: 'acceptanceStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: acceptanceResultDict,
        },
      },
    },
    // ===========表格参数===========
    {
      title: t('标本信息'),
      dataIndex: 'syncUser',
      hideInSearch: true,
      children: [
        {
          title: t('来源单位'),
          dataIndex: 'originOrgCode',
          width: 220,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }: any) => {
            return <span>{record?.originOrgInfo?.originOrg}</span>;
          },
        },
        {
          title: t('出库批号'),
          dataIndex: 'syncBatchNo',
          width: 120,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('入库仓库'),
          dataIndex: 'warehouseId',
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }: any) => {
            return <span>{record?.warehouse?.name}</span>;
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
          title: t('标本箱号起'),
          dataIndex: 'boxIdUp',
          width: 180,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('标本箱号止'),
          dataIndex: 'boxIdDown',
          width: 180,
          resizable: true,
          ellipsis: true,
        },
      ].filter(item => !item.hideInTable),
    },
    {
      title: t('冷链车运输信息'),
      dataIndex: 'syncTime',
      hideInSearch: true,
      children: [
        {
          title: t('车牌号'),
          dataIndex: 'coldChainBizNum',
          width: 120,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('起运时间'),
          dataIndex: 'beginTime',
          width: 180,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('停运时间'),
          dataIndex: 'endTime',
          width: 180,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('运输温度'),
          dataIndex: 'temperature',
          width: 160,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('运输时间'),
          dataIndex: 'transitTime',
          width: 170,
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
      dataIndex: 'receiveStatus',
      hideInSearch: true,
      children: [
        {
          title: t('验收状态'),
          dataIndex: 'acceptanceStatus',
          width: 120,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.acceptanceStatus?.name}</span>;
          },
        },
        {
          title: t('验收人'),
          dataIndex: 'acceptanceBy',
          width: 120,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('验收日期'),
          dataIndex: 'acceptanceDate',
          width: 180,
          sorter: true,
          resizable: true,
          ellipsis: true,
        },
      ],
    },
    {
      title: t('不合格审核信息'),
      dataIndex: 'auditStatusInfo',
      hideInSearch: true,
      children: [
        {
          title: t('待审标本验收'),
          dataIndex: 'waitAuditStatus',
          width: 150,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.waitAuditStatus?.name}</span>;
          },
        },
        {
          title: t('审核状态'),
          dataIndex: 'auditResult',
          width: 120,
          resizable: true,
          ellipsis: true,
          customRender: ({ record }) => {
            return <span>{record?.auditResult?.name}</span>;
          },
        },
        {
          title: t('审核人'),
          dataIndex: 'auditBy',
          width: 80,
          resizable: true,
          ellipsis: true,
        },
        {
          title: t('审核日期'),
          dataIndex: 'auditDate',
          width: 180,
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
          ifShow: hasPermission('170020002000003'),
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
    fieldMapToTime: [['slurryDate', ['slurryDateUp', 'slurryDateDown'], 'YYYY-MM-DD']],
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
