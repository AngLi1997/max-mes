import { releaseFileExportPage } from '@/services';
import { fileUrlDownload } from '@/utils/fileDownload';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { createVNode } from 'vue';
import dayjs from 'dayjs';
import { checkStatusMap } from '@/utils/enum';
import { ProcessCodeCom } from '@/components/ProcessCodeCom';

interface UseTableProps {
  openSignModal: Function;
  openVerify: Function;
  openInput: Function;
}

export type UseTableParams = {
  props: UseTableProps;
};

export const useTable = ({props}: UseTableParams) => {

  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('检验单编码'),
      dataIndex: 'orderNo',
      resizable: true,
      width: 160,
      formItemProps: {
        componentProps: {
          maxlength: 30,
        }
      },
    },
    {
      title: t('批号'),
      dataIndex: 'batchNo',
      hideInTable: true,
      resizable: true,
      width: 160,
      formItemProps: {
        componentProps: {
          maxlength: 30,
        }
      },
    },
    {
      title: t('请验时间'),
      dataIndex: 'verifyTime',
      resizable: true,
      width: 180,
      formItemProps: {
        // defaultValue: defaultDate.value,
        component: 'RangePicker',
        // componentProps: {
        //   showTime: {
        //     defaultValue: [dayjs('00:00:00', 'HH:mm:ss'), dayjs('23:59:59', 'HH:mm:ss')],
        //   },
        //   placeholder: [t('开始时间'), t('结束时间')],
        //   format: 'YYYY-MM-DD HH:mm:ss',
        // }
      },
    },
    {
      title: t('状态'),
      dataIndex: 'processCode',
      hideInSearch: true,
      resizable: true,
      width: 160,
      customRender: ({ record }) => {
        return (
          <ProcessCodeCom data={record.processCode} />
        )
      },
    },
    {
      title: t('检品编码'),
      dataIndex: 'productsCode',
      hideInSearch: true,
      resizable: true,
      width: 160,
    },
    {
      title: t('检品名称'),
      dataIndex: 'productsName',
      hideInSearch: true,
      resizable: true,
      width: 160,
    },
    {
      title: t('批号'),
      dataIndex: 'batchNo',
      hideInSearch: true,
      resizable: true,
      width: 160,
    },
    {
      title: t('规格'),
      dataIndex: 'specification',
      hideInSearch: true,
      resizable: true,
      width: 120,
    },
    {
      title: t('实验包'),
      dataIndex: 'packageName',
      hideInSearch: true,
      resizable: true,
      width: 160,
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 200,
      actions: (params, action) => [
        {
          label: t('查看'),
          onClick: (e: any) => {
            props.openVerify(params.record);
          },
        },
        {
          label: t('录入'),
          onClick: (e: any) => {
            props.openInput([params.record.orderNo], false);
          },
        },
        {
          label: t('提交'),
          onClick: (e: any) => {
            rowData.value = params.record;
            props.openSignModal(params.record, false);
          },
        },
        {
          label: t('终止'),
          danger: true,
          onClick: (e: any) => {
            rowData.value = params.record;
            props.openSignModal(params.record, true);
          },
        },
      ],
    },
  ];

  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    // labelCol: {
    //   span: 6,
    // },
    fieldMapToTime: [['verifyTime', ['verifyBeginTime', 'verifyEndTime'], 'YYYY-MM-DD']],
  });

  return {
    columnsFirst,
    formFirstProps,
    rowData,
  };
};
