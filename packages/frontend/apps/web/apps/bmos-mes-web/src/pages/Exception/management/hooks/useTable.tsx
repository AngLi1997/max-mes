import { getDictNoRulesList, reqProductMaterialProductTreeReq } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { nextTick } from 'vue';
import { myFormRef, openAddModal, openHandleModal, openInvestigationModal, openToVoidModal } from './datas';

export const useTable = () => {
  const { hasPermission } = usePermissionStore();
  const pageRef = ref<any>();
  const activeKey = ref('1');
  const treeData = ref([]);
  const openHistoryModal = ref(false);
  const historyList = ref<any>([]);
  const isUpdate = ref(false);
  const addRef = ref();
  const rowData = ref();
  const detailLabel = ref({
    exceptionType: t('异常类型'),
    exceptionDescription: t('异常描述'),
    recordMode: t('记录方式'),
    recordTime: t('记录时间'),
    productFullName: t('产品名称'),
    reInvestigateReason: t('重新调查原因'),
    batchNo: t('生产批号'),
    processName: t('工艺名称'),
    processVersion: t('工艺版本'),
    procedureName: t('工序名称'),
    procedureStepName: t('工序步骤名称'),
    handleResult: t('处理结果'),
    handleTime: t('处理时间'),
    handleUserName: t('处理人名称'),
    cancelUserName: t('作废人名称'),
    cancelReason: t('作废原因'),
    cancelTime: t('作废时间'),
  }); // 操作历史label
  // 类型切换
  const typeChange = () => {
    if (activeKey.value == '1') {
      pageRef.value?.getTableRef(0)?.updateColumn([
        {
          dataIndex: 'exceptionStatus', // 异常状态
          hideInTable: true,
        },
        {
          dataIndex: 'handleResult', // 处理结果
          hideInTable: true,
        },
        {
          dataIndex: 'handleUserName', // 处理人
          hideInTable: true,
        },
        {
          dataIndex: 'handleTime', // 处理时间
          hideInTable: true,
        },
        {
          dataIndex: 'cancelReason', // 作废原因
          hideInTable: true,
        },
        {
          dataIndex: 'cancelUserName', // 作废人
          hideInTable: true,
        },
        {
          dataIndex: 'cancelTime', // 作废时间
          hideInTable: true,
        },
        {
          dataIndex: 'cancelUserName', // 创建人
          hideInTable: true,
        },
        {
          dataIndex: 'cancelTime', // 创建时间
          hideInTable: true,
        },
      ]);
    } else {
      pageRef.value?.getTableRef(0)?.updateColumn([
        {
          dataIndex: 'exceptionStatus',
          hideInTable: false,
        },
        {
          dataIndex: 'handleResult',
          hideInTable: false,
        },
        {
          dataIndex: 'handleUserName', // 处理人
          hideInTable: false,
        },
        {
          dataIndex: 'handleTime', // 处理时间
          hideInTable: false,
        },
        {
          dataIndex: 'cancelReason', // 作废原因
          hideInTable: false,
        },
        {
          dataIndex: 'cancelUserName', // 作废人
          hideInTable: false,
        },
        {
          dataIndex: 'cancelTime', // 作废时间
          hideInTable: false,
        },
        {
          dataIndex: 'cancelUserName', // 创建人
          hideInTable: false,
        },
        {
          dataIndex: 'cancelTime', // 创建时间
          hideInTable: false,
        },
      ]);
    }
    pageRef.value.fetchData();
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productId',
      hideInTable: true,
      formItemProps: {
        component: 'TreeSelect',
        componentProps: () => ({
          treeData: treeData.value,
          fieldNames: {
            value: 'id',
          },
        }),
      },
    },
    {
      title: t('异常类型'),
      dataIndex: 'exceptionType',
      width: 180,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          request: async () => {
            // 获取设备数据
            try {
              const { data } = await getDictNoRulesList({ dictId: '120090001001' });
              return data;
            } catch (error: any) {
              console.log('======异常类型', error);
            }
          },
        }),
      },
    },
    {
      title: t('异常描述'),
      dataIndex: 'exceptionDescription',
      width: 180,
      resizable: true,
    },
    {
      title: t('异常状态'),
      dataIndex: 'exceptionStatus',
      width: 180,
      resizable: true,
      hideInSearch: true,
      hideInTable: true,
      customRender: ({ record }) => <div>{record.exceptionStatus?.name}</div>,
    },
    {
      title: t('记录方式'),
      dataIndex: 'recordMode',
      width: 100,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => <div>{record.recordMode.name}</div>,
    },
    {
      title: t('记录人'),
      dataIndex: 'recordUserName',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('记录时间'),
      dataIndex: 'recordTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('创建人'),
      dataIndex: 'createByUserName',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('创建时间'),
      dataIndex: 'createTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('处理结果'),
      dataIndex: 'handleResult',
      width: 180,
      resizable: true,
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: t('处理人'),
      dataIndex: 'handleUserName',
      width: 160,
      resizable: true,
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: t('处理时间'),
      dataIndex: 'handleTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      sorter: true,
      hideInTable: true,
    },
    {
      title: t('作废原因'),
      dataIndex: 'cancelReason',
      width: 160,
      resizable: true,
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: t('作废人'),
      dataIndex: 'cancelUserName',
      width: 160,
      resizable: true,
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: t('作废时间'),
      dataIndex: 'cancelTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      sorter: true,
      hideInTable: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productFullName',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工序名称'),
      dataIndex: 'procedureName',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('工序步骤名称'),
      dataIndex: 'procedureStepName',
      width: 160,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 240,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: activeKey.value == '1' && hasPermission('120090001000002'),
          onClick: () => {
            // 获取编辑的下拉列表
            addRef.value.getAlloptions({ ...record });
            openAddModal.value = true;
            isUpdate.value = true;
            nextTick(() => {
              myFormRef.value?.formRef?.updateSchema({
                field: 'productId',
                componentProps: {
                  treeData: treeData.value || [],
                },
              });
              myFormRef.value?.formRef?.setFieldsValue({ ...record });
            });
          },
        },
        {
          label: t('处理'),
          ifShow: activeKey.value == '1' && hasPermission('120090001000003'),
          onClick: () => {
            rowData.value = record;
            openHandleModal.value = true;
          },
        },
        {
          label: t('作废'),
          ifShow: activeKey.value == '1' && hasPermission('120090001000004'),
          onClick: () => {
            rowData.value = record;
            openToVoidModal.value = true;
          },
        },
        {
          label: t('操作历史'),
          ifShow: hasPermission('120090001000005'),
          onClick: () => {
            rowData.value = record;
            openHistoryModal.value = true;
          },
        },
        {
          label: t('重新调查'),
          ifShow: activeKey.value == '2' && record.exceptionStatus.value != 2 && hasPermission('120090001000006'),
          onClick: () => {
            rowData.value = record;
            openInvestigationModal.value = true;
          },
        },
      ],
    },
  ];

  // ------------------新增----------------
  // 新增按钮点击
  const addExceptionOpen = () => {
    openAddModal.value = true;
    isUpdate.value = false;
    nextTick(() => {
      myFormRef.value?.formRef?.updateSchema({
        field: 'productId',
        componentProps: {
          treeData: treeData.value || [],
        },
      });
    });
  };

  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: true,
  });

  // 获取产品下拉列表
  const getProductList = async () => {
    const { data } = await reqProductMaterialProductTreeReq();
    treeData.value = loopTree(data) || [];
  };
  // 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
  const loopTree = (data: any) => {
    return data.map((item: any) => {
      if (item.categoryFlag) {
        item.selectable = false;
      } else {
        item.selectable = true;
      }
      item.label = item.mergeCode + '-' + item.name;
      if (item.children) {
        loopTree(item.children);
      }
      return item;
    });
  };
  onMounted(() => {
    getProductList();
  });

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    activeKey,
    typeChange,
    addExceptionOpen,
    openAddModal,
    openHistoryModal,
    historyList,
    isUpdate,
    addRef,
    rowData,
    detailLabel,
  };
};
