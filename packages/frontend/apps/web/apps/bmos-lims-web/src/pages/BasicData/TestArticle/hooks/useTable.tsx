import { releaseFileExportPage } from '@/services';
import { fileUrlDownload } from '@/utils/fileDownload';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { createVNode } from 'vue';
import {
  deleteTestArticle
} from '@/services/index';
import StateTag from '@/components/StateTag/index.vue';
import { StateTagStatus } from '@/components/StateTag/status';

interface UseTableProps {
  watchEditInfo: Function;
}

export type UseTableParams = {
  props: UseTableProps;
};

export const useTable = ({ props }: UseTableParams) => {
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('检品名称'),
      dataIndex: 'name',
      resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
        componentProps: {
          maxlength: 30,
        }
      },
    },
    {
      title: t('检品编码'),
      dataIndex: 'code',
      resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
        componentProps: {
          maxlength: 30,
        }
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
      title: t('配置实验包'),
      dataIndex: 'packageFlag',
      resizable: true,
      width: 190,
      customRender: ({ record }) => {
        return (
          <StateTag type={record.packageFlag ? 'success' : 'warning'} >
            {record.packageFlag ? t('已配置') : t('未配置')}
          </StateTag>
        )
      },
      formItemProps: {
        component: 'Select',
        // labelWidth: 120,
        componentProps: {
          options: [
            {
              label: t('未配置'),
              value: false,
            },
            {
              label: t('已配置'),
              value: true,
            },
          ],
        },
      },
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 160,
      actions: (params, action) => [
        {
          label: t('查看'),
          onClick: (e: any) => {
            props.watchEditInfo(params.record, true);
          },
        },
        {
          label: t('编辑'),
          ifShow: !params.record.status,
          onClick: (e: any) => {
            props.watchEditInfo(params.record, false);
          },
        },
        {
          label: t('删除'),
          ifShow: !params.record.status,
          danger: true,
          onClick: (e: any) => {
            Modal.confirm({
              title: t('操作将删除检品，是否继续？'),
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteTestArticle(params.record.id);
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

  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    // labelCol: {
    //   flex: 1,
    // }
  });

  return {
    columnsFirst,
    formFirstProps,
    rowData,
  };
};
