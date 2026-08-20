import { deleteEquipmentApi, getEquipmentTagTree, postEquipmentEnable, postTagInstancePrintBatch } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, RenderCallbackParams, TableActionType, TableColumn } from '@bmos/components';
import { Modal, Switch, message } from 'ant-design-vue';
import { modalStatus } from '../../../enum';
export const useColumns = ({ emits }: any) => {
  const { hasPermission } = usePermissionStore();
  const treeField = reactive({
    field: {
      categoryId: 'id',
    },
  });
  const selectionReactive = reactive<{
    selectedRowKeys: string[];
  }>({
    selectedRowKeys: [],
  });
  const router = useRouter();
  //状态
  const versionStateLoading = ref<boolean>(false);
  const printOpen = ref<any>(false);
  const columns: TableColumn[] = [
    {
      title: t('设备名称'),
      dataIndex: 'name',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('设备编号'),
      dataIndex: 'code',
      width: 190,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('设备类型'),
      dataIndex: 'tagIdList',
      width: 190,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => <div>{record.tagIdList?.map((item: any) => item.name).join(',') || '-'}</div>,
    },
    {
      title: t('设备状态'),
      dataIndex: 'equipmentStatus',
      width: 190,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => (
        <div>{record.statusPropertyList?.map((item: any) => item.name).join(',') || '-'}</div>
      ),
    },
    {
      title: t('设备信息'),
      dataIndex: 'equipmentInfo',
      width: 190,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => (
        <div class='hide'>{record.infoPropertyList?.map((item: any) => item.name + '-' + item.code).join(',')}</div>
      ),
    },
    {
      title: t('设备数据'),
      dataIndex: 'equipmentData',
      width: 190,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => (
        <div class='hide'>{record.dataPropertyList?.map((item: any) => item.name + '-' + item.code).join(',')}</div>
      ),
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('最后更新人'),
      dataIndex: 'updateBy',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateTime',
      width: 190,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('启停'),
      dataIndex: 'enable',
      width: 80,
      fixed: 'right',
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        return (
          <Switch
            checked={record?.enable}
            loading={versionStateLoading.value}
            onChange={checked => {
              changeVersionState(record, checked as boolean, tableAction);
            }}
          />
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 290,
      actions: ({ record }, { fetchData }) => [
        {
          label: t('详情'),
          ifShow: () => {
            return hasPermission('160010002000006');
          },
          onClick: () => {
            emits('addManagementPage', modalStatus.View, record);
          },
        },
        {
          label: t('编辑'),
          ifShow: () => {
            return !record?.enable && hasPermission('160010002000005');
          },
          onClick: () => {
            emits('addManagementPage', modalStatus.Edit, record);
          },
        },
        {
          label: t('匹配采集点'),
          // ifShow: () => {
          //   return hasPermission('160010002000005');
          // },
          onClick: () => {
            emits('matchPoint', record);
          },
        },
        {
          label: t('使用记录'),
          ifShow: () => {
            return hasPermission('160010002000008');
          },
          onClick: () => {
            used(record);
          },
        },
        {
          label: t('删除'),
          ifShow: () => {
            return !record?.enable && hasPermission('160010002000009');
          },
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该设备'),
              icon: h(ExclamationCircleOutlined),
              content: t('设备删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await deleteEquipmentApi(record.id);
                  fetchData();
                  message.success(t('删除成功'));
                  return Promise.resolve();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
      ],
    },
  ];
  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: { span: 6 },
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('设备名称'),
      },
      {
        field: 'tagId',
        component: 'TreeSelect',
        label: t('设备类型'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            fieldNames: {
              children: 'children',
              label: 'name',
              value: 'id',
            },
            request: async () => {
              const { data } = await getEquipmentTagTree();
              return data;
            },
          };
        },
      },
      {
        field: 'enable',
        component: 'Select',
        label: t('启停状态'),
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            disabled: !hasPermission('160010002000010'),
            options: [
              { label: t('启用'), value: 'true' },
              { label: t('停用'), value: 'false' },
            ],
          };
        },
      },
    ],
  });
  //启停状态
  const changeVersionState = (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此设备') : t('是否停用此设备');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}${record.name}`,
      onOk: async () => {
        try {
          versionStateLoading.value = true;
          await postEquipmentEnable({ id: record.id, enable: checked });
          if (checked) {
            message.success(t('启用成功'));
          } else {
            message.success(t('停用成功'));
          }
          tableAction.fetchData();
        } catch (error: any) {
          error.message && message.error(error.message);
        } finally {
          versionStateLoading.value = false;
          return Promise.resolve();
        }
      },
    });
  };
  //新增
  const addManagementPage = () => {
    emits('addManagementPage', modalStatus.Add);
  };

  //打印标签按钮
  const print = () => {
    if (selectionReactive.selectedRowKeys.length === 0) return message.error(t('请勾选设备'));
    printOpen.value = true;
  };
  // 确认打印
  const printConfirm = async (printerParams: any) => {
    try {
      const { printerIp, printerPort, printerDpi, sceneId } = printerParams;
      const batchParams = selectionReactive.selectedRowKeys.map((item: any) => {
        return {
          printerIp,
          printerPort,
          dpi: printerDpi,
          sceneId,
          body: {
            equipmentId: item?.id,
          },
        };
      });
      await postTagInstancePrintBatch(batchParams);
      message.success(t('打印成功'));
      printOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //筛选
  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectionReactive.selectedRowKeys,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        selectionReactive.selectedRowKeys = selectedRows;
      },
    },
    null,
  ]);
  // 使用记录
  const used = (record: any) => {
    router.push({
      name: 'UseLogs',
      query: record,
    });
  };
  return {
    columns,
    treeField,
    formFirstProps,
    selectionReactive,
    addManagementPage,
    rowSelections,
    printOpen,
    printConfirm,
    print,
  };
};
