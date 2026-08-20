import { releaseFileExportPage } from '@/services';
import { fileUrlDownload } from '@/utils/fileDownload';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { createVNode } from 'vue';
import { MODAL_STATUS } from '../types/enum';
import {
  deleteAnalyze
} from '@/services/index';

export const useTable = () => {
  const rowData = ref<Recordable>({});

  const columns: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'reportName',
      // resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
      },
      customCell: (record, index) =>{
        return {
          rowSpan: index == 0 || record.reportName !== ' ' ? record.length : 0,
        }
      }
    },
    {
      title: t('分析项'),
      dataIndex: 'name',
      // resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('标准规定'),
      dataIndex: 'standard',
      // resizable: true,
      ellipsis: false,
      hideInSearch: true,
      width: 190,
    },
    {
      title: t('检验结果'),
      dataIndex: 'result',
      // resizable: true,
      hideInSearch: true,
      width: 190,
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
    rowData,
  };
};