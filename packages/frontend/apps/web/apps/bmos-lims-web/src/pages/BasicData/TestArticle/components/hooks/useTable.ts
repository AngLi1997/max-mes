import { releaseFileExportPage } from '@/services';
import { fileUrlDownload } from '@/utils/fileDownload';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { createVNode } from 'vue';

interface UseTableProps {
  watchStatus: Ref<Boolean>
}

export type UseTableParams = {
  props: UseTableProps;
};

export const useTable = ({ props }: UseTableParams) => {
  const viewReportModalOpen = ref<boolean>(false);
  const tableData = ref<any>([]);
  const columns: TableColumn[] = [
    {
      title: t('实验包编码'),
      dataIndex: 'code',
      // resizable: true,
      hideInSearch: true,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('实验包名称'),
      dataIndex: 'name',
      // resizable: true,
      hideInSearch: true,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 100,
      hideInTable: props.watchStatus.value,
      actions: (params, action) => [
        {
          label: t('删除'),
          ifShow: true,
          danger: true,
          onClick: (e: any) => {
            Modal.confirm({
              title: t('操作将解绑实验包，是否继续？'),
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  // await deleteMaterialApi(params.record.id);
                  tableData.value = tableData.value.filter((item: any) => item.id !== params.record.id);
                  message.success(t('解绑成功'));
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

  const formProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    }
    // fieldMapToTime: [
    //   ['selectTime', ['startTime', 'endTime'], 'YYYY-MM-DD'],
    // ],
  });

  return {
    columns,
    formProps,
    viewReportModalOpen,
    tableData,
  };
};