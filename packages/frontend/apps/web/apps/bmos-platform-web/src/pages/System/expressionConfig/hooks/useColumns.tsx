import { reqExpressionConfirm, reqExpressionDelete } from '@/api';
import StateTag from '@/components/StateTag/index.vue';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { modalStatus } from '../enum';

export const useColumns = (useModalForm: any, useTree: any) => {
  const { hasPermission } = usePermissionStore();
  const { storageAdd } = useModalForm;
  const { pageExpression } = useTree;
  const treeField = reactive({
    field: {
      expressionCategoryId: 'id',
    },
  });
  // 验证弹窗
  const calculateModalOpen = ref<boolean>(false);
  const bindRecordOpen = ref(false);
  const rowData = ref<Recordable>({});
  const expressionVerify = () => {
    pageExpression.value?.fetchData();
  };
  const columns: TableColumn[] = [
    {
      title: t('公式名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 190,
      resizable: true,
    },
    {
      title: t('分类'),
      hideInSearch: true,
      dataIndex: 'expressionCategoryName',
      width: 190,
      resizable: true,
    },
    {
      title: t('公式表达式'),
      hideInSearch: true,
      dataIndex: 'expression',
      width: 190,
      resizable: true,
    },
    {
      title: t('计算结果'),
      hideInSearch: true,
      dataIndex: 'result',
      width: 190,
      resizable: true,
    },
    {
      title: t('状态'),
      hideInSearch: true,
      dataIndex: 'confirmStatus',
      width: 190,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <StateTag type={['primary', 'success', 'warning'][record.confirmStatus.value]}>
            {record.confirmStatus.name}
          </StateTag>
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('100020006000006'),
          onClick: () => {
            storageAdd(record, modalStatus.View);
          },
        },
        {
          label: t('编辑'),
          ifShow:
            (record.confirmStatus.value == '0' || record.confirmStatus.value == '2') &&
            hasPermission('100020006000005'),
          onClick: () => {
            storageAdd(record, modalStatus.Edit);
          },
        },
        {
          label: t('确认'),
          ifShow: record.confirmStatus.value == '2' && hasPermission('100020006000007'),
          onClick: () => {
            Modal.confirm({
              title: t('是否确认公式配置'),
              icon: h(ExclamationCircleOutlined),
              content: t('公式确认后无法编辑和删除。'),
              async onOk() {
                try {
                  await reqExpressionConfirm(record.id);
                  message.success(t('确认成功'));
                  pageExpression.value?.fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
        {
          label: t('验证'),
          ifShow: record.confirmStatus.value == '0' && hasPermission('100020006000009'),
          onClick: () => {
            calculateModalOpen.value = true;
            rowData.value = record;
          },
        },
        {
          label: t('删除'),
          ifShow: record.confirmStatus.value != '1' && hasPermission('100020006000008'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该公式'),
              icon: h(ExclamationCircleOutlined),
              content: t('公式删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await reqExpressionDelete(record.id);
                  message.success(t('删除成功'));
                  pageExpression.value?.fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
        {
          label: t('绑定记录'),
          ifShow: record.confirmStatus.value == '1' && hasPermission('100020006000009'), //todo: 100020006000010
          onClick: () => {
            bindRecordOpen.value = true;
            rowData.value = record;
          },
        },
      ],
    },
  ];
  return {
    columns,
    treeField,
    calculateModalOpen,
    rowData,
    bindRecordOpen,
    expressionVerify,
  };
};
