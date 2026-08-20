import { deleteProductMaterialApi, putProductMaterialStatusApi } from '@/services';
import { usePermissionStore } from '@/stores/permission.js';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, Switch, message } from 'ant-design-vue';
import { createVNode } from 'vue';
import { MaterialTypeMap } from '../const';

interface UseTableProps {
  categoryType: number;
  watchEditMaterialInfo: Function;
  bindingRecordHandel: Function;
}

const MaterialTypeNameMap: any = {
  [MaterialTypeMap.RawMaterial]: t('原辅包'),
  [MaterialTypeMap.MiddleProduct]: t('中间品'),
  [MaterialTypeMap.Product]: t('产品'),
};

export type UseTableParams = {
  props: UseTableProps;
};

export const useTable = ({ props }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();
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
      title: t('分类'),
      dataIndex: 'fullCategoryName',
      resizable: true,
      hideInSearch: true,
      width: 190,
    },
    {
      title: t('启停'),
      dataIndex: 'status',
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
        if (props.categoryType === 0)
          return (
            <Switch
              disabled={!hasPermission('120010001000006')}
              checked={record.status}
              onClick={() => {
                changeSwitch(record);
              }}
            />
          );
        if (props.categoryType === 1)
          return (
            <Switch
              disabled={!hasPermission('120010002000006')}
              checked={record.status}
              onClick={() => {
                changeSwitch(record);
              }}
            />
          );
        if (props.categoryType === 2)
          return (
            <Switch
              disabled={!hasPermission('120010003000007')}
              checked={record.status}
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
      width: 260,
      actions: (params, action) => [
        {
          label: t('查看'),
          ifShow: props.categoryType === 0 && hasPermission('120010001000003'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, false);
          },
        },
        {
          label: t('查看'),
          ifShow: props.categoryType === 1 && hasPermission('120010002000003'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, false);
          },
        },
        {
          label: t('查看'),
          ifShow: props.categoryType === 2 && hasPermission('120010003000003'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, false);
          },
        },
        {
          label: t('编辑'),
          ifShow: !params.record.status && props.categoryType === 0 && hasPermission('120010001000004'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, true);
          },
        },
        {
          label: t('编辑'),
          ifShow: !params.record.status && props.categoryType === 1 && hasPermission('120010002000004'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, true);
          },
        },
        {
          label: t('编辑'),
          ifShow: !params.record.status && props.categoryType === 2 && hasPermission('120010003000004'),
          onClick: () => {
            props.watchEditMaterialInfo(params.record, true);
          },
        },
        {
          label: t('批记录'),
          ifShow: props.categoryType === 2 && hasPermission('120010003000005'),
          onClick: () => {
            props.bindingRecordHandel(params.record);
          },
        },
        {
          label: t('删除'),
          ifShow: !params.record.status && props.categoryType === 0 && hasPermission('120010001000005'),
          onClick: () => {
            Modal.confirm({
              title: `${t('是否删除该')}${MaterialTypeNameMap[props.categoryType]}`,
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteProductMaterialApi(params.record.id);
                  message.success(t('删除成功！'));
                  action.fetchData();
                } catch (error: any) {
                  message.error(error.message);
                }
              },
            });
          },
        },
        {
          label: t('删除'),
          ifShow: !params.record.status && props.categoryType === 1 && hasPermission('120010002000005'),
          onClick: () => {
            Modal.confirm({
              title: `${t('是否删除该')}${MaterialTypeNameMap[props.categoryType]}`,
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteProductMaterialApi(params.record.id);
                  message.success(t('删除成功！'));
                  action.fetchData();
                } catch (error: any) {
                  message.error(error.message);
                }
              },
            });
          },
        },
        {
          label: t('删除'),
          ifShow: !params.record.status && props.categoryType === 2 && hasPermission('120010003000005'),
          onClick: () => {
            Modal.confirm({
              title: `${t('是否删除该')}${MaterialTypeNameMap[props.categoryType]}`,
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteProductMaterialApi(params.record.id);
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
    let title = val.status
      ? `${t('是否停用该')}${MaterialTypeNameMap[props.categoryType]}`
      : `${t('是否启用该')}${MaterialTypeNameMap[props.categoryType]}`;
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
      await putProductMaterialStatusApi({
        id: val.id,
        status: !val.status,
      });
      val.status = !val.status;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  return { columns };
};
