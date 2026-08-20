import {
  getRoomInfoApi,
  getRoomPlanInfoStartPlanList,
  getRoomProcedureList,
  getRoomStatusApi,
} from '@/api';
import { getProductTreeApi } from '@/api/productionApi.js';
import { t } from '@/utils/useBmosI18n.js';
// import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
import { onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useData = () => {
  const { showNotify } = useNotify();
  const specifics = ref();
  const basicItems = ref([
    {
      label: t('房间编码'),
      field: 'code',
    },
    {
      label: t('房间状态'),
      field: 'statusName',
      tag: '',
      plain: false,
    },
    // {
    //   label: t('有效期至'),
    //   field: 'expireTime',
    // },
  ]);
  const tagType = {
    1: 'primary',
    2: 'warning',
    3: 'success',
  };
  const tabId = ref('');
  const nowStatus = ref();
  const nextStatus = ref();
  const open = ref(false);
  const formRef = ref();
  const isEst = ref(false);
  const signOpen = ref(false);
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: '',
  });
  const signatureParams = ref();
  // 签名参数
  const labelList = reactive([{
    label: t('修订人'),
    signatureAction: 86,
  }, {
    label: t('复核人'),
    signatureAction: 87,
    menuId: 121030002,
  }]);

  // 获取房间信息
  const getRoomInfo = async () => {
    const { data } = await getRoomInfoApi(tabId.value);
    specifics.value = {
      ...data,
      statusName: data.status.label,
    };
    nowStatus.value = data.status.value;
    basicItems.value[1].tag = tagType[nowStatus.value];
  };
  const getChildrenData = (arr) => {
    const newArr = [];
    arr.forEach((item) => {
      item.categoryFlag = !item.categoryFlag;
      if (item.children.length > 0) {
        item.children = getChildrenData(item.children);
      }
      newArr.push(item);
    });
    return newArr;
  };
  // 签名提交
  const submitSign = async () => {
    signOpen.value = false;
    uni.showLoading({
      title: '更改中...',
      mask: true,
    });
    console.log(signValue.value);
    try {
      const paramsData = {
        ...signatureParams.value,
        desc: signValue.value.remark,
        operateId: signValue.value.userId1,
        verifyId: signValue.value.userId2,
      };
      await getRoomStatusApi(paramsData);
      uni.hideLoading();
      await getRoomInfo();
    }
    catch (error) {
      uni.hideLoading();
      error.message && showNotify({ type: 'danger', message: error.message });
    }
  };
  // 点击待清场
  const toBeClear = () => {
    nextStatus.value = 2;
    // 状态转为待清场,直接签名
    signatureParams.value = {
      id: specifics.value.id,
      status: 2,
      taskText: t('状态变更-未清场'),
    };
    signOpen.value = true;
  };
  // 点击使用1/清场3
  const toBeUsed = (status) => {
    nextStatus.value = status;
    formRef.value?.resetForm();
    open.value = true;
  };
  // 生产信息填报弹窗
  const formProps = reactive({
    schemas: [
      {
        field: 'productId',
        component: 'BMFormSelect',
        label: t('产品名称'),
        colProps: {
          span: 24,
        },
        componentProps: ({ formModel }) => {
          return {
            request: async () => {
              const { data } = await getProductTreeApi({ categoryType: 2 });
              return getChildrenData(data);
            },
            title: t('产品名称'),
            type: 'tree',
            mode: 'single',
            fieldNames: {
              name: 'showName',
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: true,
              parentId: 'parentId',
              children: 'children',
            },
            treeData: [],
            onConfirm: async ({ id }) => {
              // 获取批号
              const { data } = await getRoomPlanInfoStartPlanList({ productId: id });
              formRef.value?.updateSchema({
                field: 'batchNo',
                componentProps: {
                  options: [...data],
                },
              });
              formModel.batchNo = '';
              formModel.procedureId = '';
            },
            onClear: () => {
              formModel.batchNo = '';
              formModel.procedureId = '';
              // 删除下拉框
              formRef.value?.updateSchema({
                field: 'batchNo',
                componentProps: {
                  options: [],
                },
              });
              formRef.value?.updateSchema({
                field: 'procedureId',
                componentProps: {
                  options: [],
                },
              });
            },
          };
        },
      },
      {
        field: 'batchNo',
        component: 'BMFormSelect',
        label: t('生产批号'),
        colProps: {
          span: 24,
        },
        componentProps: ({ formModel }) => {
          return {
            options: [],
            placeholder: t('请选择'),
            title: t('生产批号'),
            fieldNames: {
              label: 'batchNo',
              value: 'batchNo',
            },
            onConfirm: async (value) => {
              const batchNo = {
                processId: value.processId,
                processVersion: value.processVersion,
                version: value.processVersion,
              };

              const { data } = await getRoomProcedureList(batchNo);
              formRef.value?.updateSchema({
                field: 'procedureId',
                componentProps: {
                  options: [...data],
                },
              });
              formModel.procedureId = '';
            },
            onClear: () => {
              formModel.procedureId = '';
              // 删除下拉框
              formRef.value?.updateSchema({
                field: 'procedureId',
                componentProps: {
                  options: [],
                },
              });
            },
          };
        },
      },
      {
        field: 'procedureId',
        component: 'BMFormSelect',
        label: t('所属工序'),
        colProps: {
          span: 24,
        },
        componentProps: {
          placeholder: t('请选择'),
          options: [],
          title: t('所属工序'),
          fieldNames: {
            label: 'name',
            value: 'id',
          },
        },
      },
    ],
  });

  // 表单在用提交
  const submitForm = async () => {
    open.value = false;
    const form = await formRef.value?.validate();
    if (nextStatus.value === 1) {
      // 更改为在用
      signatureParams.value = {
        ...form,
        id: specifics.value.id,
        status: 1,
        taskText: t('状态变更-在用'),
      };
      signOpen.value = true;
    }
    if (nextStatus.value === 3) {
      // 更改为已清场,跳转至清场执行页
      const dataValue = {
        ...specifics.value,
        ...form,
      };
      if (!dataValue.productId) {
        delete dataValue.productId;
      }
      if (!dataValue.batchNo) {
        delete dataValue.batchNo;
      }
      if (!dataValue.procedureId) {
        delete dataValue.procedureId;
      }
      signatureParams.value = dataValue;
      dataValue.timeLimit = dataValue.timeLimit || '0';
      const query = Object.keys(dataValue)
        .map(
          key =>
            `${encodeURIComponent(key)}=${encodeURIComponent(
              dataValue[key],
            )}`,
        )
        .join('&');
      isEst.value = true;
      uni.navigateTo({
        url: `/pages/roomManagement/roomCleaningExecution/index?${query}`,
      });
    }
  };
  onMounted(() => {
    getRoomInfo();
  });
  return {
    specifics,
    basicItems,
    tabId,
    nowStatus,
    open,
    formProps,
    formRef,
    isEst,
    signOpen,
    signValue,
    signatureParams,
    labelList,
    toBeClear,
    submitForm,
    submitSign,
    toBeUsed,
    getRoomInfo,
  };
};
