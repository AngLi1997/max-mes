import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openCntModal: any, enterView: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const router = useRouter();

  const columnsFirst: TableColumn[] = [
    {
      title: t('基础信息'),
      dataIndex: 'basicInfo',
      children: [
        {
          title: t('报告编号'),
          dataIndex: 'reportNo',
          width: 170,
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
        },
        {
          title: t('检品批号'),
          dataIndex: 'inWarehouseBatchNo',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('核查批号'),
          dataIndex: 'checkNo',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('检疫期数据'),
      dataIndex: 'basicInfo',
      children: [
        {
          title: t('核查份数'),
          dataIndex: 'checkNumber',
          width: 170,
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
          width: 100,
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
          width: 140,
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
          width: 140,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('未通过份数'),
          dataIndex: 'unResNum',
          width: 140,
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
          width: 140,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('放行单'),
      dataIndex: 'basicInfo',
      children: [
        {
          title: t('报告人'),
          dataIndex: 'createUser',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('报告日期'),
          dataIndex: 'createTime',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('签发人'),
          dataIndex: 'auditUser',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('签发日期'),
          dataIndex: 'auditTime',
          width: 180,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('审核状态'),
          dataIndex: 'noteAuditStatus',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.noteAuditStatus?.name;
          },
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
          label: t('放行单详情'),
          ifShow: hasPermission('170060003000003'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
        {
          label: t('查看核查详情'),
          ifShow: hasPermission('170060003000004'),
          onClick: () => {
            // look(record);
            // enterView(record);
            router.push({
              name: 'quarantine-check-detail',
              params: { id: record.quarantineId },
              query: { type: 1 },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    schemas: [
      {
        label: t('报告编号'),
        field: 'reportNo',
        component: 'Input',
      },
      {
        label: t('检品批号'),
        field: 'inWarehouseBatchNo',
        component: 'Input',
      },
      {
        label: t('来源单位'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
      {
        label: t('核查批号'),
        field: 'checkNo',
        component: 'Input',
      },
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
