import { weighingWorkOrderPlanCancel, weighingWorkOrderPlanIssue } from '@/services';
import { type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Flex, Modal, message } from 'ant-design-vue';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export const useTable = () => {
  const router = useRouter();
  const pageRef = ref<any>();

  // 表格列定义
  const columns: TableColumn[] = [
    {
      title: t('工单编号'),
      dataIndex: 'ticketNo',
      width: 150,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 130,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      width: 150,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('称量中心'),
      dataIndex: 'weighCentreName',
      width: 150,
    },
    {
      title: t('需求总量'),
      dataIndex: 'requirementQuantity',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }: any) => `${record.requirementQuantity} ${record.unit}`,
    },
    {
      title: t('计划执行时间'),
      dataIndex: 'planDate',
      width: 150,
      formItemProps: {
        label: t('执行时间'),
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        const statusMap: Record<string, string> = {
          0: '#2871FF',
          1: '#59BF78',
          2: '#6C7380',
          3: '#FF5633',
        };
        const statusColor = statusMap[record.status?.value] || '#000';
        return (
          <Flex align='center' gap={8}>
            {statusMap[record.status?.value] && (
              <div style={{ width: '7px', height: '7px', borderRadius: '50%', backgroundColor: statusColor }}></div>
            )}
            <span style={{ fontSize: '14px', color: statusColor }}>{record.status?.label}</span>
          </Flex>
        );
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: record.status?.value === 0,
          code: '120030014000003',
          onClick: () => {
            router.push({
              name: 'weighing-work-order-edit-plan',
              query: { ...record, type: 'edit' },
            });
          },
        },
        {
          label: t('查看'),
          code: '120030014000004',
          onClick: () => {
            router.push({
              name: 'weighing-work-order-edit-plan',
              query: { ...record, type: 'view' },
            });
          },
        },
        {
          label: t('下发'),
          ifShow: record.status?.value === 0,
          code: '120030014000005',
          onClick: () => {
            Modal.confirm({
              title: t('提示'),
              content: t('是否下发该称量工单?'),
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await weighingWorkOrderPlanIssue({ id: record.id });
                  pageRef.value?.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
            });
          },
        },
        {
          label: t('取消'),
          ifShow: record.status?.value === 0,
          code: '120030014000006',
          onClick: () => {
            Modal.confirm({
              title: t('提示'),
              content: t('是否取消该称量工单？'),
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await weighingWorkOrderPlanCancel({ id: record.id });
                  pageRef.value?.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
            });
          },
        },
        {
          label: t('称量详情'),
          ifShow: ![0, 3].includes(record.status?.value),
          code: '120030014000007',
          onClick: () => {
            router.push({
              name: 'weighing-work-order-detail',
              query: record,
            });
          },
        },
      ],
    },
  ];

  // 表单配置
  const formProps: Partial<FormProps> = {
    fieldMapToTime: [['planDate', ['planDateEnd', 'planDateStart'], 'YYYY-MM-DD']],
  };

  return {
    columns,
    pageRef,
    formProps,
  };
};
