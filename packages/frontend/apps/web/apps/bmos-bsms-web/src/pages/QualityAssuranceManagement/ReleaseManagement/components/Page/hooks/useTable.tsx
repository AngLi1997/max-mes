import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openCntModal: any, openCreateNote: any, enterView: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    // ===========查询参数===========
    {
      title: t('报告编号'),
      dataIndex: 'reportNo',
      hideInTable: true,
    },
    {
      title: t('检品批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInTable: true,
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
      title: t('核查批号'),
      dataIndex: 'checkNo',
      hideInTable: true,
    },
    // ===========表格参数===========
    {
      title: t('基础信息'),
      dataIndex: 'syncUser',
      hideInSearch: true,
      children: [
        {
          title: t('报告编号'),
          dataIndex: 'reportNo',
          width: 180,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('来源单位'),
          dataIndex: 'originOrg',
          width: 220,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.originOrgInfo?.originOrg;
          },
        },
        {
          title: t('检品批号'),
          dataIndex: 'inWarehouseBatchNo',
          width: 140,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('核查批号'),
          dataIndex: 'checkNo',
          width: 160,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('检疫期数据'),
      dataIndex: 'syncTime',
      hideInSearch: true,
      children: [
        {
          title: t('核查份数'),
          dataIndex: 'checkNumber',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'checkWeight',
          width: 120,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('合格率'),
          dataIndex: 'passRate',
          width: 120,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('合格份数'),
          dataIndex: 'passNum',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.passNum ? <a onClick={() => openCntModal(record, 1)}>{record?.passNum}</a> : 0;
          },
        },
        {
          title: t('合格重量'),
          dataIndex: 'passWeight',
          width: 120,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('不合格份数'),
          dataIndex: 'unPassNum',
          width: 130,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.unPassNum ? <a onClick={() => openCntModal(record, 2)}>{record?.unPassNum}</a> : 0;
          },
        },
        {
          title: t('不合格重量'),
          dataIndex: 'unPassWeight',
          width: 130,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('未通过份数'),
          dataIndex: 'unResNum',
          width: 130,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.unResNum ? <a onClick={() => openCntModal(record, 3)}>{record?.unResNum}</a> : 0;
          },
        },
        {
          title: t('未通过重量'),
          dataIndex: 'unResWeight',
          width: 130,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 240,
      actions: ({ record }) => [
        {
          label: t('创建放行单'),
          ifShow: hasPermission('170060002000001'),
          onClick: () => {
            openCreateNote(record);
          },
        },
        {
          label: t('查看核查详情'),
          ifShow: hasPermission('170060002000002'),
          onClick: () => {
            // look(record);
            enterView(record, 1);
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
    columnsFirst,
    formFirstProps,
  };
};
