import { reqExpressionConfirm, reqExpressionDelete } from '@/api';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps } from '@bmos/components';
import { Recordable, TableInstance, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, Tag, message } from 'ant-design-vue';
import { Ref } from 'vue';
import { MODAL_STATUS } from '../../../types';

export type UseTableParams = {
  addExpressionModalOpen: Ref<boolean>;
};

export const useTable = ({ addExpressionModalOpen }: UseTableParams) => {
  const tableInstance = ref<TableInstance>();
  // 选择的某一行数据
  const rowData = ref<Recordable>({});
  // modalStatus 弹框的状态
  const modalStatus = ref<MODAL_STATUS>(MODAL_STATUS.ADD);

  const columns: TableColumn[] = [
    {
      title: t('公式名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 190,
      resizable: true,
      ellipsis: true,
    },
    {
      title: t('分类'),
      hideInSearch: true,
      dataIndex: 'expressionCategoryName',
      width: 190,
      resizable: true,
      ellipsis: true,
    },
    {
      title: t('公式表达式'),
      hideInSearch: true,
      dataIndex: 'expression',
      width: 190,
      resizable: true,
      ellipsis: true,
    },
    {
      title: t('计算结果'),
      hideInSearch: true,
      dataIndex: 'result',
      width: 190,
      resizable: true,
      ellipsis: true,
    },
    {
      title: t('状态'),
      hideInSearch: true,
      dataIndex: 'confirmStatus',
      width: 190,
      resizable: true,
      customRender: ({ record }) => {
        return (
          <span>
            {record.confirmStatus.value ? (
              <>
                <Tag color='green'>{t('确认')}</Tag>
              </>
            ) : (
              <>
                <Tag color='blue'>{t('编辑')}</Tag>
              </>
            )}
          </span>
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
          onClick: () => {
            rowData.value = record;
            modalStatus.value = MODAL_STATUS.VIEW;
            addExpressionModalOpen.value = true;
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.confirmStatus.value,
          onClick: () => {
            rowData.value = record;
            modalStatus.value = MODAL_STATUS.EDIT;
            addExpressionModalOpen.value = true;
          },
        },
        {
          label: t('确认'),
          ifShow: !record.confirmStatus.value,
          onClick: () => {
            Modal.confirm({
              title: t('是否确认公式配置'),
              icon: h(ExclamationCircleOutlined),
              content: t('公式确认后无法编辑和删除。'),
              async onOk() {
                try {
                  await reqExpressionConfirm(record.id);
                  message.success(t('确认成功'));
                  tableInstance.value?.fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error.message);
                  return Promise.reject();
                }
              },
              onCancel() { },
            });
          },
        },
        {
          label: t('删除'),
          ifShow: !record.confirmStatus.value,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该公式'),
              icon: h(ExclamationCircleOutlined),
              content: t('公式删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await reqExpressionDelete(record.id);
                  message.success(t('删除成功'));
                  tableInstance.value?.fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error.message);
                  return Promise.reject();
                }
              },
              onCancel() { },
            });
          },
        },
      ],
    },
  ];

  const formProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
    },
  });

  return {
    tableInstance,
    columns,
    formProps,
    rowData,
    modalStatus,
  };
};
