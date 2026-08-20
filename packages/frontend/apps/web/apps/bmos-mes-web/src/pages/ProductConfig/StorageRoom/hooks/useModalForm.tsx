import {
  postTagInstancePrintBatch,
  storageConfigCreateById,
  storageConfigEditById,
  storageConfigListDataPermission,
} from '@/services';

import type { FormProps } from '@bmos/components';

import { message } from 'ant-design-vue';

import { modalStatus } from '../enum';

export const useModalForm = (useTree: any) => {
  const { treeData, pageStorages } = useTree;
  const modalFormRef = ref();
  //是否加载完成
  const openIs = ref<boolean>(false);
  //弹窗开关
  const modalInstance = reactive({
    addStorage: false,
  });
  // 打印弹窗开关
  const printOpen = ref<any>(false);
  //多选时的表格ids
  const selectedRowKeys1 = ref<any>([]);
  //存多选的数据
  const operationSelectedRows = ref<any>([]);
  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectedRowKeys1.value,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);
  //初始化数据
  const formDefaultValue = ref<FormProps['initialValues']>({
    storageId: '',
    position: '',
  });
  //弹窗类型
  const formModalType = ref<string>(modalStatus.Add);
  //弹窗fom表单
  const addFomUse = computed(() => {
    const initialValues = {};
    const schemas = [
      {
        field: 'storageId',
        component: 'TreeSelect',
        label: t('所属区域'),
        required: true,
        componentProps: {
          disabled: formModalType.value == modalStatus.Edit ? true : false,
          treeData: treeData?.value[0]?.children,
          fieldNames: {
            children: 'children',
            label: 'name',
            value: 'id',
          },
        },
      },
      {
        field: 'position',
        component: 'Input',
        label: t('暂存货位'),
        required: true,
        componentProps: {
          maxlength: 100,
        },
      },
      {
        field: 'code',
        component: 'Input',
        label: t('货位编码'),
        required: true,
        componentProps: {
          maxlength: 100,
        },
      },
      {
        field: 'deptIds',
        label: t('部门授权'),
        required: true,
        slot: 'DEPART',
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: true,
              validator: () => {
                if (!formModel['deptIds'] || formModel['deptIds']?.length === 0) {
                  return Promise.reject(t('请选择部门授权'));
                }
                return Promise.resolve();
              },
              trigger: 'change',
            },
          ];
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        componentProps: {
          maxlength: 200,
          showCount: true,
        },
      },
    ];
    return { initialValues: initialValues, schemas: schemas, disabled: false };
  });
  const storageAdd = (treeNode: any, isType: any) => {
    formModalType.value = isType;
    switch (formModalType.value) {
      case modalStatus.Add:
        addProcessing(treeNode);
        break;
      case modalStatus.Edit:
        //处理数据
        processing(treeNode);
        break;
      case modalStatus.View:
        //显示隐藏
        addFomUse.value.disabled = true;
        //处理数据
        processing(treeNode);
        break;
    }
    nextTick(() => (openIs.value = true));
  };
  // 打印标签按钮
  const print = () => {
    if (operationSelectedRows.value.length === 0) return message.error(t('请先勾选暂存货位'));
    if (operationSelectedRows.value.findIndex((item: any) => item.enable?.label === 'false') >= 0) {
      return message.error(t('不能打印停用的暂存货位'));
    }
    printOpen.value = true;
  };
  // 确认打印
  const printConfirm = async (printerParams: any) => {
    try {
      const { printerIp, printerPort, printerDpi, sceneId } = printerParams;
      const batchParams = operationSelectedRows.value.map((item: any) => {
        return {
          printerIp,
          printerPort,
          dpi: printerDpi,
          sceneId,
          body: {
            no: item?.code,
          },
        };
      });
      await postTagInstancePrintBatch(batchParams);
      message.success(t('打印成功'));
      printOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //新增数据处理
  const addProcessing = (fromData: any) => {
    let storageId = '';
    storageId = fromData.id === 'all' ? void 0 : fromData.id;
    formDefaultValue.value = { storageId: storageId };
    modalInstance.addStorage = true;
  };
  // 查询和编辑数据处理
  const processing = async (fromData: any) => {
    try {
      let storageId = '';
      const src = fromData?.idPath?.split(',');
      storageId = src[src.length - 1];
      const res = await storageConfigListDataPermission({ id: fromData.id });
      if (res.code === 0) {
        nextTick(() => {
          formDefaultValue.value = {
            ...fromData,
            storageId: storageId,
            deptIds: res.data,
          };
          modalInstance.addStorage = true;
        });
      }
    } catch (error) {
      modalInstance.addStorage = false;
    }
  };
  //确认
  const handleModalSubmit = async (fromModel: any) => {
    try {
      const fromModel = await modalFormRef.value.validate();
      switch (formModalType.value) {
        case modalStatus.Add:
          await handleModalAdd(fromModel);
          break;
        case modalStatus.Edit:
          await handleModalEdit(fromModel);
          break;
        case modalStatus.View:
          modalInstance.addStorage = false;
          break;
      }
    } catch (error: any) {
      console.log(error);
    }
  };
  //新增
  const handleModalAdd = async (fromModel: any) => {
    try {
      const res = await storageConfigCreateById(fromModel);
      if (res.code === 0) message.success(t('新增成功'));
      pageStorages.value?.fetchData(0);
      modalInstance.addStorage = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  //编辑
  const handleModalEdit = async (fromModel: any) => {
    try {
      const res = await storageConfigEditById(fromModel);
      if (res.code === 0) message.success(t('编辑成功'));
      pageStorages.value?.fetchData(0);
      modalInstance.addStorage = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  return {
    modalInstance,
    printOpen,
    print,
    printConfirm,
    rowSelections,
    addFomUse,
    formDefaultValue,
    formModalType,
    modalFormRef,
    storageAdd,
    handleModalSubmit,
  };
};
