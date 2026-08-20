import { reqWeighingRequirementsCancel, reqWeighingRequirementsMakeSure } from '@/services';
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
      title: t('产品名称'),
      dataIndex: 'materialName',
      width: 150,
    },
    {
      title: t('产品编码'),
      dataIndex: 'mergeCode',
      width: 150,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 150,
    },
    {
      title: t('生产BOM'),
      dataIndex: 'bomName',
      width: 150,
    },
    {
      title: t('计划生产时间'),
      dataIndex: 'planDate',
      width: 150,
    },
    {
      title: t('称量中心'),
      dataIndex: 'weighCentreName',
      width: 150,
    },
    {
      title: t('状态'),
      dataIndex: 'releaseStatus',
      width: 100,
      customRender: ({ record }: any) => {
        const statusMap: Record<string, { text: string; color: string }> = {
          0: { text: t('编辑中'), color: '#2871FF' },
          1: { text: t('已确认'), color: '#59BF78' },
          2: { text: t('已完成'), color: '#6C7380' },
          3: { text: t('已取消'), color: '#FF5633' },
        };
        const status = statusMap[record.releaseStatus] || { text: '-', color: '#000' };
        return (
          <Flex align='center' gap={8}>
            {statusMap[record.releaseStatus] && (
              <div style={{ width: '7px', height: '7px', borderRadius: '50%', backgroundColor: status.color }}></div>
            )}
            <span style={{ fontSize: '14px', color: status.color }}>{status.text}</span>
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
          ifShow: record.releaseStatus === 0,
          code: '120030013000002',
          onClick: () => {
            router.push({
              name: 'weighing-requirements-detail',
              query: { id: record.id, type: 'edit' },
            });
          },
        },
        {
          label: t('查看'),
          code: '120030013000003',
          onClick: () => {
            router.push({
              name: 'weighing-requirements-detail',
              query: { id: record.id, type: 'view' },
            });
          },
        },
        {
          label: t('确认'),
          ifShow: record.releaseStatus === 0,
          code: '120030013000004',
          onClick: () => {
            Modal.confirm({
              title: t('提示'),
              content: t('确定要确认该生产称量需求吗？'),
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await reqWeighingRequirementsMakeSure({ id: record.id });
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
          ifShow: record.releaseStatus < 2,
          code: '120030013000005',
          onClick: () => {
            Modal.confirm({
              title: t('提示'),
              content: t('确定要取消该生产称量需求吗？'),
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await reqWeighingRequirementsCancel({ id: record.id });
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
          ifShow: ![0, 3].includes(record.releaseStatus),
          code: '120030013000006',
          onClick: () => {
            router.push({
              name: 'weighing-requirements-record-detail',
              query: record,
            });
          },
        },
      ],
    },
  ];

  // 表单配置
  const formProps: Partial<FormProps> = {
    schemas: [
      {
        field: 'batchNo',
        label: t('生产批号'),
        component: 'Input',
      },
      {
        field: 'materialName',
        label: t('产品名称'),
        component: 'Input',
      },
      {
        field: 'mergeCode',
        label: t('产品编码'),
        component: 'Input',
      },
      {
        field: 'bomName',
        label: t('生产BOM'),
        component: 'Input',
      },
    ],
  };

  return {
    columns,
    pageRef,
    formProps,
  };
};
