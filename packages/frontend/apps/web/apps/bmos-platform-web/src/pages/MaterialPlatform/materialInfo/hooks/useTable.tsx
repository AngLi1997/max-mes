import { deleteMaterialApi, putMaterialStatusApi } from '@/api/materialPlatform/materialInfo';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, Switch, message } from 'ant-design-vue';
import { createVNode } from 'vue';

export const useTable = ({ props, tableInstance }: any) => {
  const { hasPermission } = usePermissionStore();
  const unitConfigOpen = ref<boolean>(false);
  // 当前选中行数据
  const rowData = ref<Recordable>({});
  const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
  const operationSelectedRows = ref<any>([]); //存多选的数据

  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectedRowKeys1.value,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);
  const columns: TableColumn[] = [
    {
      title: t('名称'),
      dataIndex: 'name',
      resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('编码'),
      dataIndex: 'mergeCode',
      resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('规格'),
      dataIndex: 'specification',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('单位'),
      dataIndex: 'unitName',
      hideInSearch: true,
      resizable: true,
      width: 190,
      customRender: ({ record }) => {
        return record.unitExtendName || record.unitName;
      },
    },
    {
      title: t('已下发业务'),
      dataIndex: 'dispenseRecord',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('启停'),
      dataIndex: 'status',
      resizable: true,
      hideInSearch: true,
      fixed: 'right',
      formItemProps: {
        label: t('启停状态'),
        component: 'Select',
        componentProps: {
          options: [
            { label: t('启用'), value: 'true' },
            { label: t('停用'), value: 'false' },
          ],
        },
      },
      width: 80,
      customRender: ({ record }) => (
        <Switch
          disabled={!hasPermission('100040002000008')}
          checked={record.status}
          onClick={() => {
            changeSwitch(record);
          }}
        />
      ),
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      key: 'ACTION',
      width: 220,
      actions: (params, action) => [
        {
          label: t('查看'),
          ifShow: hasPermission('100040002000004'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, true);
          },
        },
        {
          label: t('编辑'),
          ifShow: !params.record.status && hasPermission('100040002000005'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, false);
          },
        },
        {
          label: t('单位配置'),
          ifShow: hasPermission('100040002000006'),
          onClick: () => {
            rowData.value = params.record;
            unitConfigOpen.value = true;
          },
        },
        {
          label: t('删除'),
          ifShow: !params.record.status && hasPermission('100040002000007'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该物料'),
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteMaterialApi(params.record.id);
                  message.success(t('删除成功！'));
                  action.fetchData();
                } catch (error: any) {
                  message.error(error.message);
                }
              },
            });
          },
        },
      ],
    },
  ];
  // 更多-启用/停用
  const startStop = (type: any) => {
    const ids: any = [];
    if (operationSelectedRows.value.length !== 0) {
      operationSelectedRows.value.forEach((item: any) => {
        ids.push(item?.id);
      });
    } else {
      return message.error(t('请先选择物料'));
    }
    const title = type === 'start' ? t('是否启用所选物料') : t('是否停用所选物料');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          if (type === 'start') {
            //待对接后测试
            await putMaterialStatusApi({
              id: ids,
              status: true,
            });

            message.success(t('启用成功'));
            tableInstance.value?.fetchData();
          } else {
            await putMaterialStatusApi({
              id: ids,
              status: false,
            });
            message.success(t('停用成功'));
            tableInstance.value?.fetchData();
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
      onCancel() { },
    });
  };

  // 切换状态
  const changeSwitch = (val: any) => {
    let title = val.status ? t('是否停用该物料') : t('是否启用该物料');
    Modal.confirm({
      title,
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content: '',
      okText: t('确定'),
      cancelText: t('取消'),
      onOk() {
        confirmChangeSwitch(val);
      },
    });
  };

  const confirmChangeSwitch = async (val: any) => {
    try {
      await putMaterialStatusApi({
        id: val.id,
        status: !val.status,
      });
      val.status = !val.status;
      message.success(val.status ? t('物料启用成功') : t('物料停用成功'));
    } catch (error: any) {
      message.error(error.message);
    }
  };

  return { columns, unitConfigOpen, rowData, rowSelections, startStop };
};
