import { sampleOutWarehouseOut } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useExpand = (openCnt: any, fetchParentData: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('分拣后批号'),
      dataIndex: 'sortingPlanBatchNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('大托盘编号'),
      dataIndex: 'palletNo',
      width: 150,
      resizable: true,
    },
    {
      title: t('数量'),
      dataIndex: 'number',
      width: 80,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => {
        return record?.number ? <a onClick={() => openCnt(record)}>{record?.number}</a> : 0;
      },
    },
    {
      title: t('箱号起'),
      dataIndex: 'boxIdUp',
      width: 150,
      resizable: true,
    },
    {
      title: t('箱号止'),
      dataIndex: 'boxIdDown',
      width: 150,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('整盘出库'),
          // ifShow:,
          ifShow: hasPermission('170020011000004') && record?.currentInventoryStatus?.value === 2,
          onClick: () => {
            Modal.confirm({
              title: t('是否对该数据进行整盘出库操作?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await sampleOutWarehouseOut({
                    outPlanBatchNo: record?.outPlanBatchNo,
                    palletNo: record?.palletNo,
                  });
                  message.success(t('操作成功'));
                  fetchParentData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
              onCancel() {},
            });
          },
        },
      ],
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  const fetchData = async (params: any) => {
    pageRef.value?.fetchData(0, params);
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    setRef,
    fetchData,
  };
};
