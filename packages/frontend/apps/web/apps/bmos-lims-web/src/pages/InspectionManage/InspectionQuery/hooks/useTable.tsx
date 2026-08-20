import { releaseFileExportPage } from '@/services';
import { fileUrlDownload } from '@/utils/fileDownload';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { createVNode, computed } from 'vue';
import dayjs from 'dayjs';
import { CHECK_STATUS, checkStatusMap } from '@/utils/enum';
import { ProcessCodeCom } from '@/components/ProcessCodeCom';

interface UseTableProps {
  watchEditInfo: Function;
}

// export type UseTableParams = {
//   props: UseTableProps;
// };

export const useTable = (props: UseTableProps) => {

  const { watchEditInfo } = props;
  const pageRef = ref<any>();
  const rowData = ref<Recordable>({});
  
  const finishStatus = ref([
    {
      label: checkStatusMap[CHECK_STATUS.ALREADY_SIGN],
      value: CHECK_STATUS.ALREADY_SIGN,
    },
    {
      label: checkStatusMap[CHECK_STATUS.ALREADY_TERMINATION],
      value: CHECK_STATUS.ALREADY_TERMINATION,
    },
  ]);
  
  const inProgressStatus = ref([
    {
      label: checkStatusMap[CHECK_STATUS.CONFIRM],
      value: CHECK_STATUS.CONFIRM,
    },
    {
      label: checkStatusMap[CHECK_STATUS.TAKE],
      value: CHECK_STATUS.TAKE,
    },
    {
      label: checkStatusMap[CHECK_STATUS.INSPECT],
      value: CHECK_STATUS.INSPECT,
    },
    {
      label: checkStatusMap[CHECK_STATUS.REPORT],
      value: CHECK_STATUS.REPORT,
    },
    {
      label: checkStatusMap[CHECK_STATUS.AUDIT_REPORT],
      value: CHECK_STATUS.AUDIT_REPORT,
    },
    {
      label: checkStatusMap[CHECK_STATUS.SIGN],
      value: CHECK_STATUS.SIGN,
    },
  ]);

  const chnageFileter = (value: string) => {
    if(value == '1'){
      fileterList.value = inProgressStatus.value;
    } else {
      fileterList.value = finishStatus.value;
    }
    columnsFirst[3].headerSearchComponentProps.options = fileterList.value;
    // return fileterList.value;
  }
  const fileterList = ref<any[]>(inProgressStatus.value);

  const columnsFirst: TableColumn[] = reactive([
    {
      title: t('检验单编码'),
      dataIndex: 'orderNo',
      resizable: true,
      width: 160,
      formItemProps: {
        defaultValue: '',
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
        defaultValue: '',
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
        defaultValue: [
          dayjs().startOf('day').subtract(30, 'day').format('YYYY-MM-DD'),
          dayjs().endOf('day').format('YYYY-MM-DD'),
        ],
        component: 'RangePicker',
        componentProps: {
        //   showTime: {
        //     defaultValue: [dayjs('00:00:00', 'HH:mm:ss'), dayjs('23:59:59', 'HH:mm:ss')],
        //   },
        //   placeholder: [t('开始时间'), t('结束时间')],
          valueFormat: 'YYYY-MM-DD',
        }
      },
    },
    {
      title: t('状态'),
      dataIndex: 'processCode',
      hideInSearch: true,
      resizable: true,
      width: 160,
      // fixed: 'right',
      headerSearchComponent: 'Checkbox',
      headerSearchComponentProps: {
        options: fileterList.value,
      },
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
      width: 160,
      actions: (params, action) => [
        {
          label: t('查看'),
          onClick: (e: any) => {
            watchEditInfo(params.record, true);
          },
        },
        {
          label: t('检验报告'),
          ifShow: params.record.processCode == CHECK_STATUS.ALREADY_SIGN,
          onClick: (e: any) => {
            watchEditInfo(params.record, false);
          },
        },
      ],
    },
  ]);

  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 6,
    },
    // labelCol: {
    //   span: 6,
    // },
    fieldMapToTime: [['verifyTime', ['verifyBeginTime', 'verifyEndTime'], 'YYYY-MM-DD']],
  });

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    rowData,
    chnageFileter,
  };
};
