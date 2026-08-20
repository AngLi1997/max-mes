import { 
  deleteInspectionItem
} from '@/services/index';
import { fileUrlDownload } from '@/utils/fileDownload';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { 
  Modal, 
  Select,
  SelectOption,
  Textarea,
  message 
} from 'ant-design-vue';
import { createVNode } from 'vue';
import { MODAL_STATUS } from '../../types/enum';

interface UseTableProps {
  watchEditInfo: Function;
}

export type UseTableParams = {
  props: UseTableProps;
};

export const useTable = ({ props }: UseTableParams) => {
  const viewReportModalOpen = ref<boolean>(false);
  const rowData = ref<Recordable>({});

  const columns: TableColumn[] = [
    {
      title: t('检验项目名称'),
      dataIndex: 'name',
      // resizable: true,
      formItemProps: {
        defaultValue: '',
        labelWidth: 94,
        componentProps: {
          maxlength: 30,
        }
      },
    },
    {
      title: t('检验项目编码'),
      dataIndex: 'code',
      // resizable: true,
      formItemProps: {
        defaultValue: '',
        labelWidth: 94,
        componentProps: {
          maxlength: 30,
        }
      },
    },
    {
      title: t('检验项目报告名'),
      dataIndex: 'reportName',
      hideInSearch: true,
      // resizable: true,
    },
    // {
      // title: t('标准规定'),
      // dataIndex: 'standard',
      // resizable: true,
      // width: 190,
      // customRender: ({ record }) => {
      //   return (
      //     <Textarea 
      //       v-model:value={record.standard}
      //       placeholder={t('请输入')}
      //     />
      //   )
      // }
    // },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 160,
      actions: (params, action) => [
        {
          label: t('查看'),
          onClick: (e: any) => {
            props.watchEditInfo(params.record, MODAL_STATUS.VIEW);
          },
        },
        {
          label: t('编辑'),
          ifShow: !params.record.status,
          onClick: (e: any) => {
            props.watchEditInfo(params.record, MODAL_STATUS.EDIT);
          },
        },
        {
          label: t('删除'),
          ifShow: !params.record.status,
          danger: true,
          onClick: (e: any) => {
            Modal.confirm({
              title: t('操作将删除检验项目，是否继续？'),
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteInspectionItem(params.record.id);
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

  const formProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
  });

  return {
    columns,
    formProps,
    viewReportModalOpen,
    rowData,
  };
};