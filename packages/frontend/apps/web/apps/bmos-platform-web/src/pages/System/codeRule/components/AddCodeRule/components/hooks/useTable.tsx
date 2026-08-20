import { reqPlatformDictListDictDow } from '@/api';
import { DetailsType } from '@/pages/System/codeRule/types';
import { MODAL_STATUS } from '@/pages/System/dict/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Recordable, TableInstance, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { AddCodeRuleProps } from '../../types';
import { typeMap } from '../../utils';

export type UseTableParams = {
  isView: ComputedRef<boolean>;
  modalStatus: Ref<MODAL_STATUS>;
  dataSource: Ref<Recordable[]>;
  addDetailModalOpen: Ref<boolean>;
  props: AddCodeRuleProps;
  showTableFlag: Ref<boolean>;
};

export const useTable = ({ props, isView, modalStatus, dataSource, addDetailModalOpen, showTableFlag }: UseTableParams) => {
  const tableInstance = ref<TableInstance>();
  // 选择的某一行数据
  const rowData = ref<Recordable>({});
  const deleteRecord = ref<any>({});
  const parameterIdOptions = ref<Recordable[]>([]);
  const getParameterIdOptions = async (selectDictId?: string) => {
    try {
      if (!props.selectDictId || !props.codeObj) return [];
      const { data } = await reqPlatformDictListDictDow({
        code: props.codeObj?.value,
      });
      return data;
    } catch (error) { }
  };

  watch(
    () => [props.selectDictId, props.codeObj],
    async val => {
      if (val) {
        parameterIdOptions.value = await getParameterIdOptions();
      }
    },
    { immediate: true },
  );

  const columns: TableColumn[] = [
    {
      title: t('类型'),
      dataIndex: 'type',
      resizable: true,
      width: 180,
      customRender: ({ record }) => {
        return typeMap.get(record.type) || '-';
      },
    },
    {
      title: t('属性'),
      dataIndex: 'type',
      resizable: true,
      width: 180,
      customRender: ({ record }) => {
        switch (record.type) {
          case DetailsType.SEQUENCE:
            // 显示为起始流水号-增量-最大位数；如：0001-1-4
            return `${record.startNo}-${record.step}-${record.maxLength}`;
          case DetailsType.CONSTANT:
            return record.value;
          case DetailsType.PARAMETER:
            return (
              parameterIdOptions.value?.find(
                // @ts-ignore
                (item: { id: string }) => item.id === record.parameterId,
              )?.label || '-'
            );
          case DetailsType.DATE:
            return [t('年'), t('月'), t('日'), t('年月'), t('年月日')][record.dateType];
          default:
            return '-';
        }
      },
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      width: 120,
      actions: ({ record, index }) => [
        {
          label: t('编辑'),
          ifShow: !isView.value,
          onClick: () => {
            rowData.value = record;
            modalStatus.value = MODAL_STATUS.EDIT;
            addDetailModalOpen.value = true;
          },
        },
        {
          label: t('查看'),
          ifShow: isView.value,
          onClick: () => {
            rowData.value = record;
            modalStatus.value = MODAL_STATUS.VIEW;
            addDetailModalOpen.value = true;
          },
        },
        {
          label: t('删除'),
          ifShow: !isView.value,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  deleteRecord.value = record;
                  dataSource.value = dataSource.value.filter(item => item.id !== record.id);
                  message.success(t('删除成功'));
                  showTableFlag.value = false
                  nextTick(() => {
                    showTableFlag.value = true
                  })
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error);
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

  return {
    tableInstance,
    columns,
    rowData,
    parameterIdOptions,
    deleteRecord,
  };
};
