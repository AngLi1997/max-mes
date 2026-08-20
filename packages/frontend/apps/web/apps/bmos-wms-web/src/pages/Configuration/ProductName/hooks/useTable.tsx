import { deleteCargoApi, putCargoDisenableStatusApi, putCargoEnableStatusApi } from '@/services';
import { usePermissionStore } from '@/stores/permission.js';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, Switch, message } from 'ant-design-vue';
import { createVNode } from 'vue';

interface UseTableProps {
  categoryType: number;
  watchEditMaterialInfo: Function;
}

export type UseTableParams = {
  props: UseTableProps;
};

export const useTable = ({ props }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
  const columns: TableColumn[] = [
    {
      title: t('名称'),
      dataIndex: 'cargoName',
      resizable: true,
      width: 190,
    },
    {
      title: t('编码'),
      dataIndex: 'mergeCode',
      resizable: true,
      width: 190,
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
      dataIndex: 'unit',
      hideInSearch: true,
      resizable: true,
      width: 190,
      // customRender: ({ record }) => {
      //   return record.unitExtendName || record.unit;
      // },
    },
    {
      title: t('分类'),
      dataIndex: 'cargoCategoryName',
      resizable: true,
      hideInSearch: true,
      width: 190,
    },
    {
      title: t('启停'),
      dataIndex: 'enable',
      resizable: true,
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
      customRender: ({ record }) => {
        if (props.categoryType === 0 && !hasPermission('150010001000009'))
          return (
            <Switch
              checked={record.enable}
              onClick={() => {
                changeSwitch(record);
              }}
            />
          );
        return (
          <Switch
            checked={record.enable}
            onClick={() => {
              changeSwitch(record);
            }}
          />
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      key: 'ACTION',
      width: 240,
      actions: (params, action) => [
        {
          label: t('查看'),
          ifShow: props.categoryType === 0 && hasPermission('150010001000006'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, false);
          },
        },
        {
          label: t('编辑'),
          ifShow: !params.record.enable && props.categoryType === 0 && hasPermission('150010001000007'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, true);
          },
        },
        {
          label: t('删除'),
          ifShow: !params.record.enable && props.categoryType === 0 && hasPermission('150010001000008'),
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该货品物料'),
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteCargoApi(params.record.id);
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
  // 切换状态
  const changeSwitch = (val: any) => {
    let title = val.enable ? t('是否停用该货品信息') : t('是否启用该货品信息');
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
      // 启用状态改为停用
      if (val.enable) {
        await putCargoDisenableStatusApi({
          id: val.id,
        });
        message.success(t('停用成功'));
      } else {
        await putCargoEnableStatusApi({
          id: val.id,
        });
        message.success(t('启用成功'));
      }
      val.enable = !val.enable;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  return { columns };
};
