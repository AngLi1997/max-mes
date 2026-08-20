import { RemarkDetail } from '@/components/RemarkModal';
import { updateLaboratoryInstrumentActive } from '@/services';
import { usePermissionStore } from '@/stores';
import { useDict } from '@/stores/dictStore';
import { OperationStatusMap, yesOrNoEnum } from '@/types';
import { type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Switch, message } from 'ant-design-vue';

export const useTable = (openModal: Function, deleteEquipment: Function) => {
  const { getDict } = useDict();
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('设备名称'),
      dataIndex: 'instrumentName',
      width: 140,
    },
    {
      title: t('设备型号'),
      dataIndex: 'model',
      width: 120,
    },
    {
      title: t('设备编号'),
      dataIndex: 'instrumentNo',
      width: 120,
    },
    {
      title: t('设备类型'),
      dataIndex: 'typeName',
      width: 120,
    },
    {
      title: t('检验项目'),
      dataIndex: 'inspectionItemData',
      width: 250,
      customRender: ({ record }) => {
        return record.inspectionItemData.length ? record.inspectionItemData?.join(',') : '-';
      },
    },
    {
      title: t('设备厂家'),
      dataIndex: 'manufacturer',
      width: 170,
    },
    {
      title: t('点检日期'),
      dataIndex: 'spotCheckedDate',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.spotCheckedDate),
    },
    {
      title: t('启用'),
      dataIndex: 'active',
      width: 120,
      customRender: ({ record }) => {
        const loading = ref<boolean>(false);
        return (
          <Switch
            checked={record.active}
            checkedValue={yesOrNoEnum.YES}
            unCheckedValue={yesOrNoEnum.NO}
            disabled={!hasPermission('210050007000003')}
            loading={loading.value}
            onClick={async () => {
              try {
                loading.value = true;
                await updateLaboratoryInstrumentActive({
                  identify: record.identify,
                  instrumentNo: record.instrumentNo,
                  activeType: record.active === yesOrNoEnum.YES ? yesOrNoEnum.NO : yesOrNoEnum.YES,
                });
                message.success(t('操作成功'));
                pageRef.value?.fetchData();
              } catch (error: any) {
                console.error(error);
                error.message && message.error(error.message);
              } finally {
                loading.value = false;
              }
            }}
          />
        );
      },
    },
    {
      title: t('负责人'),
      dataIndex: 'principal',
      width: 100,
    },
    {
      title: t('联系方式'),
      dataIndex: 'phone',
      width: 160,
    },
    {
      title: t('操作人'),
      dataIndex: 'updateBy',
      width: 100,
    },
    {
      title: t('更新日期'),
      dataIndex: 'updateTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => getDateFormat(record.updateTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 160,
      actions: ({ record }) => [
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('仪器设备备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('210050007000004'),
          onClick: () => {
            // look(record);
            openModal(OperationStatusMap.EDIT, record);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('210050007000002'),
          danger: true,
          onClick: () => {
            deleteEquipment([record]);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelAlign: 'left',
    labelWidth: 100,
    schemas: [
      {
        label: t('设备名称'),
        field: 'instrumentName',
        component: 'Input',
      },
      {
        label: t('设备型号'),
        field: 'model',
        component: 'Input',
      },
      {
        label: t('设备编号'),
        field: 'instrumentNo',
        component: 'Input',
      },
      {
        label: t('设备类型'),
        field: 'type',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('设备类型');
          },
        },
      },
      {
        label: t('设备厂家'),
        field: 'manufacturer',
        component: 'Input',
      },
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    remarkModalOpen,
    remarkDetails,
  };
};
