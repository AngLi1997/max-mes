import { reqLotRecordsManageGetDynamicReportItem } from '@/services';
import { BMIcon, Recordable, TableColumn } from '@bmos/components';

export type UseStepTwoParams = {
  props: any;
  stepOneFormValue: Ref<Recordable>;
};
export const useStepTwo = ({ stepOneFormValue }: UseStepTwoParams) => {
  const stepTwoTableRef = ref<any>();
  const stepTwoTableDataSource = ref<any[]>([]);

  const moveUp = (record: any) => {
    const index = stepTwoTableDataSource.value.findIndex(item => item.id === record.id);
    if (index === 0) return;
    const temp = stepTwoTableDataSource.value[index];
    stepTwoTableDataSource.value[index] = stepTwoTableDataSource.value[index - 1];
    stepTwoTableDataSource.value[index - 1] = temp;
  };
  const moveDown = (record: any) => {
    const index = stepTwoTableDataSource.value.findIndex(item => item.id === record.id);
    if (index === stepTwoTableDataSource.value.length - 1) return;
    const temp = stepTwoTableDataSource.value[index];
    stepTwoTableDataSource.value[index] = stepTwoTableDataSource.value[index + 1];
    stepTwoTableDataSource.value[index + 1] = temp;
  };

  const stepTwoTableColumns: Ref<TableColumn[]> = ref([
    {
      title: '',
      width: 40,
      dataIndex: 'drag',
      fixed: 'left',
      customRender: ({ record }) => {
        return <BMIcon type='Move' />;
      },
    },
    {
      title: t('产品名称'),
      width: 100,
      dataIndex: 'productName',
    },
    {
      title: t('工艺版本'),
      width: 100,
      dataIndex: 'processVersion',
    },
    {
      title: t('生产批号'),
      width: 100,
      dataIndex: 'batchNo',
    },
    {
      title: t('生产开始时间'),
      width: 120,
      dataIndex: 'startTime',
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('上移'),
          onClick: () => {
            moveUp(record);
          },
        },
        {
          label: t('下移'),
          onClick: () => {
            moveDown(record);
          },
        },
      ],
    },
  ]);

  const getStepTwoTableData = async () => {
    try {
      if (!stepOneFormValue.value?.planId) {
        stepTwoTableDataSource.value = [];
        return;
      }
      const { data } = await reqLotRecordsManageGetDynamicReportItem(stepOneFormValue.value?.planId);
      stepTwoTableDataSource.value = data;
    } catch (error) {
      stepTwoTableDataSource.value = [];
    }
  };

  return {
    stepTwoTableRef,
    stepTwoTableColumns,
    stepTwoTableDataSource,
    getStepTwoTableData,
  };
};
