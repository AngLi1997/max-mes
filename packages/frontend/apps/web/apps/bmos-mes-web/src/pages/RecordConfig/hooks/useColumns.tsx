import {
  recordBindExpression,
  recordSaveProduct,
  recordUpdateVersion,
  reqRecordAuditStartflow,
  resourcePermissionSave,
} from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { createVNode, ref } from 'vue';
import { useRouter } from 'vue-router';
import DepartMent from '../../../components/DepartMent/index.vue';
import ModalBtn from '../../../components/ModalBtn/index.vue';
import StateTag from '../../../components/StateTag/index.vue';
import { OPERATION } from '../../TemplateEdit/enum';
import Product from '../components/Product/index.vue';
import BindFormula from '../components/bindFormula/index.vue';
import { STATE, STATE_STATUS, V_OP } from '../enum';
import { UseColumnsType } from '../type';

export const useColumns = ({ props }: any): UseColumnsType => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const depart = ref();
  const product = ref();
  const bindFormula = ref();
  const tableFields = ref([
    {},
    {
      field: {
        recordId: 'recordId',
      },
    },
  ]);
  const operation = async (type: number, record: any, tableAction: TableActionType) => {
    switch (type) {
      case 1:
        try {
          const data = {
            resourceId: record.recordId,
            deptIds: depart.value.getSelectKeys(),
          };
          if (data.deptIds.length === 0) {
            message.error(t('请选择部门'));
            return Promise.reject();
          }
          const res = await resourcePermissionSave(data);
          if (res.code === 0) {
            message.success(t('保存数据权限成功'));
            tableAction.fetchData();
            return Promise.resolve();
          }
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject();
        }
        break;
      case 2:
        try {
          const data = {
            productIdList: product.value.getSelectKeys(),
            recordId: record.recordId,
          };
          const res = await recordSaveProduct(data);
          if (res.code === 0) {
            return Promise.resolve();
          }
          message.error(res.message);
          return Promise.reject();
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject();
        }
      case 3:
        try {
          const data = {
            id: record.recordId,
            expressionIdList: bindFormula.value.getSelectKeys(),
          };
          if (data.expressionIdList.length === 0) {
            message.error(t('请选择公式'));
            return Promise.reject();
          }
          await recordBindExpression(data);
          message.success(t('绑定公式成功'));
          tableAction.fetchData();
          return Promise.resolve();
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject();
        }
      default:
        break;
    }
  };

  const historyOpen = ref<boolean>(false);
  const secondRowData = ref<any>({});
  const operation_version = (record: any, type: number, tableAction: TableActionType) => {
    switch (type) {
      case V_OP.INVALID:
        Modal.confirm({
          title: t('是否作废此版本'),
          icon: createVNode(ExclamationCircleOutlined),
          content: t('确认作废后此版本的记录将无法使用'),
          centered: true,
          async onOk() {
            const { recordId, version, versionId } = record;
            const data = {
              recordId,
              version,
              id: versionId,
              state: type,
            };
            recordUpdateVersion(data as any)
              .then((res: any) => {
                if (res.code === 0) {
                  message.success(t('版本作废成功'));
                  tableAction.fetchData();
                  return;
                }
                message.error(t(res.message));
              })
              .catch((err: any) => {
                message.error(t(err.message));
              });
          },
        });
        break;
      case V_OP.SHOW:
        router.push({
          name: 'TemplateEdit',
          params: {
            record_id: record.versionId,
            record_name: encodeURIComponent(record.name),
            record_type: OPERATION.SHOW,
            implement: props.implement,
            update: 0,
            recordId: record.recordId,
          },
        });
        break;
      case V_OP.HISTORY:
        secondRowData.value = record;
        historyOpen.value = true;
        break;
      case V_OP.REVIEW:
        Modal.confirm({
          closable: true,
          title: t('提交审核'),
          icon: () => '',
          content: t('是否发起记录版本审核'),
          async onOk() {
            try {
              await reqRecordAuditStartflow({ versionId: record.versionId });
              tableAction.fetchData();
              sendMessage(MessageType.UpdateMessageCount);
            } catch (error: any) {
              message.error(error.message);
            }
          },
        });
        break;
      case V_OP.REVIEW_SCHEDULE:
        router.push({
          name: 'record-config-schedule',
          query: {
            processInstanceId: record.instanceId,
            fromList: 'fromList',
            title: t('记录配置'),
          },
        });
        break;
      default:
        break;
    }
  };
  const recordColumn: TableColumn[] = [
    {
      title: t('名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 400,
      resizable: true,
    },
    {
      title: t('分类'),
      dataIndex: 'categoryName',
      resizable: true,
      width: 400,
      hideInSearch: true,
      customRender: col => {
        return col.value?.replace(/\/$/, '');
      },
    },
    {
      title: '操作',
      dataIndex: 'operation',
      fixed: 'right',
      key: 'operation',
      hideInSearch: true,
      hideInTable: props.implement === '1',
      width: 240,
      customRender: lat => {
        // @ts-ignore
        const { record, tableAction } = lat;
        return (
          <div class='operation-area'>
            <ModalBtn submit={() => operation(2, record, tableAction)} title={t('绑定产品')}>
              {{
                default: () =>
                  h(Product, {
                    ref: product,
                    checks: [],
                    record: record.recordId,
                  }),
                trigger: () => <a v-hasAuth='120020001000006'>{t('绑定产品')}</a>,
              }}
            </ModalBtn>
            <ModalBtn submit={() => operation(3, record, tableAction)} title={t('绑定公式')}>
              {{
                default: () =>
                  h(BindFormula, {
                    ref: bindFormula,
                    isAdd: false,
                    record: record.recordId,
                  }),
                trigger: () => <a v-hasAuth='120020001000015'>{t('绑定公式')}</a>,
              }}
            </ModalBtn>
            <ModalBtn submit={() => operation(1, record, tableAction)} title={t('部门权限')}>
              {{
                default: () =>
                  h(DepartMent, {
                    ref: depart,
                    isAdd: false,
                    record: record.recordId,
                  }),
                trigger: () => <a v-hasAuth='120020001000005'>{t('数据权限')}</a>,
              }}
            </ModalBtn>
          </div>
        );
      },
    },
  ];
  const versionColumn: TableColumn[] = [
    {
      title: t('版本号'),
      dataIndex: 'version',
      resizable: true,
      fixed: 'left',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('状态'),
      dataIndex: 'state',
      resizable: true,
      width: 100,
      hideInSearch: true,
      customRender: ({ record }) => <StateTag type={STATE_STATUS[record.state.value]}>{record.state.label}</StateTag>,
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      resizable: true,
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      dataIndex: 'price',
      resizable: true,
      width: 100,
      fixed: 'right',
      hideInSearch: true,
      key: 'ACTION',
      actions: ({ record }, tableAction) => [
        {
          label: t('查看'),
          ifShow: props.implement === '1' ? hasPermission('120060001001003') : hasPermission('120020001000010'),
          onClick: () => {
            operation_version(record, V_OP.SHOW, tableAction);
          },
        },
        {
          label: t('历史'),
          ifShow: props.implement === '1' ? hasPermission('120060001001004') : hasPermission('120020001000011'),
          onClick: () => {
            operation_version(record, V_OP.HISTORY, tableAction);
          },
        },
        {
          label: t('审核'),
          ifShow: hasPermission('120020001000012') && Number(record.state.value) === STATE.EDIT,
          onClick: () => {
            operation_version(record, V_OP.REVIEW, tableAction);
          },
        },
        {
          label: t('审核进度'),
          ifShow: hasPermission('120020001000014') && Number(record.state.value) === STATE.REVIEW,
          onClick: () => {
            operation_version(record, V_OP.REVIEW_SCHEDULE, tableAction);
          },
        },
        {
          label: t('作废'),
          ifShow: hasPermission('120020001000013') && Number(record.state.value) === STATE.EDIT,
          onClick: () => {
            operation_version(record, V_OP.INVALID, tableAction);
          },
        },
      ],
    },
  ];

  return { recordColumn, versionColumn, tableFields, historyOpen, secondRowData };
};
