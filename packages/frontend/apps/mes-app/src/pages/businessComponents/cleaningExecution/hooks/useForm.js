import {
  getRoomAuthUserApi,
  getRoomInfoApi,
  postFactoryCleanRoom,
} from '@/api';
import {
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { manageForm } from '@/utils/publicFunctions.js';
import { serverTime, timestampToTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { computed, onMounted, reactive, ref } from 'vue';

export const useForm = ({ UseParams, showNotify }) => {
  const {
    specifics,
    isSign,
    signOpen,
    labelList,
    paramsData,
    segForm,
    remark,
  } = UseParams;
  const signValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
    remark: '',
  });
  // 清场人
  const options1 = ref([]);
  // 复核人
  const options2 = ref([]);
  const roomFormsRef = ref();
  // 获取房间信息
  const getRoomInfo = async () => {
    try {
      const res = await getRoomInfoApi(paramsData.value.id);
      specifics.value = res.data;
    }
    catch (error) {
      error.message && showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const handleChange = (e, item) => {
    // e 是子组件表单改变后返回的新对象
    // item 是父组件的旧对象
    const dateTime
      = e.type === 'datetimerange' ? { ...e, value: e.value.join(' - ') } : e;
    if (e.type === 'datetimerange') {
      segForm.params = {
        ...segForm.params,
        beginTime: e.value[0],
        endTime: e.value[1],
      };
      segForm.form[2] = { ...dateTime };
    }
    const model = [{ ...item, ...dateTime }];
    const submitForm = {};

    manageForm(submitForm, model, 1);
    segForm.params = { ...segForm.params, ...submitForm };
  };
  const signatureParams = computed(() => {
    const from = {
      ...segForm.params,
      componentId: paramsData.value.componentId,
      roomId: paramsData.value.id,
      batchNo: urlQueryRef.value?.batchNo,
      copyVersion: getCurrentCopyRecordItem()?.version,
      procedureModelId: pageBasicDataRef.value.procedureModelId,
      procedureStepId: pageBasicDataRef.value?.procedureStepId,
      procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
      processId: urlQueryRef.value?.processId,
      processVersion: urlQueryRef.value?.processVersion,
      productPlanId: urlQueryRef.value?.productPlanId,
      recordItemId: pageBasicDataRef.value?.recordItemId,
      recordVersionId: pageBasicDataRef.value?.recordVersionId,
      reuse: pageBasicDataRef.value.reusable,
    };
    return from;
  });
  // 完成
  const submit = () => {
    roomFormsRef.value
      .validate()
      .then(async (data) => {
        const from = {
          beginTime: timestampToTime(data.clearanceTime[0]),
          endTime: timestampToTime(data.clearanceTime[1]),
          ...segForm.params,
          componentId: paramsData.value.componentId,
          roomId: paramsData.value.id,
          batchNo: urlQueryRef.value?.batchNo,
          copyVersion: getCurrentCopyRecordItem()?.version,
          procedureModelId: pageBasicDataRef.value.procedureModelId,
          procedureStepId: pageBasicDataRef.value?.procedureStepId,
          procedureStepModelId: pageBasicDataRef.value?.procedureStepModelId,
          processId: urlQueryRef.value?.processId,
          processVersion: urlQueryRef.value?.processVersion,
          productPlanId: urlQueryRef.value?.productPlanId,
          recordItemId: pageBasicDataRef.value?.recordItemId,
          recordVersionId: pageBasicDataRef.value?.recordVersionId,
          reuse: pageBasicDataRef.value.reusable,
        };
        signatureParams.value = from; // 组件执行清场, 签名确认后,加签名对象
        await postFactoryCleanRoom(from);
        if (paramsData.value.componentType === 'CLEAN_IMPLEMENT') {
          if (paramsData.value.isToRoomKey === 'false') {
            initFillData2();
            uni.navigateBack({
              delta: 3,
            });
            return false;
          }
          else {
            initFillData2();
            uni.navigateBack({
              delta: 2,
            });
          }
          return false;
        }
        else {
          uni.navigateBack();
        }
      })
      .catch((error) => {
        error.message && showNotify({
          type: 'danger',
          message: error.message,
        });
      });
  };
  // 清场人
  const clearingPeople = (isType) => {
    isSign.value = isType;
    signValue.value = {
      userName1: '',
      loginName1: '',
      password1: '',
      userId1: '',
      remark: '',
    };
    switch (isSign.value) {
      case 1:
        remark.value = false;
        labelList.value = [
          {
            label: t('清场人'),
            signatureAction: 88,
            options: options1.value,
          },
        ];
        break;
      case 2:
        remark.value = false;
        labelList.value = [
          {
            label: t('复核人'),
            signatureAction: 89,
            options: options2.value,
          },
        ];
        break;
    }
    signOpen.value = true;
  };
  // 签名
  const submitSign = () => {
    switch (isSign.value) {
      case 1:
        segForm.params = {
          ...segForm.params,
          remark: signValue.value.remark,
          operatorId: signValue.value.userId1,
          operateName: signValue.value.userName1,
        };
        roomFormsRef.value.setFieldsValue({
          operatorId: signValue.value.userName1,
        });
        break;
      case 2:
        segForm.params = {
          ...segForm.params,
          verifierId: signValue.value.userId1,
          verifyName: signValue.value.userName1,
        };
        roomFormsRef.value.setFieldsValue({
          verifierId: signValue.value.userName1,
        });
        break;
    }
    signOpen.value = false;
  };

  // 获取具有权限的清场人/复核人
  const getRoomAuthUser = async () => {
    try {
      const res = await getRoomAuthUserApi({ roomId: paramsData.value.id });
      options1.value = res.data;
    }
    catch (error) {
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
    try {
      const res = await getRoomAuthUserApi({
        roomId: paramsData.value.id,
        authCode: '121010001002024',
      });
      options2.value = res.data;
    }
    catch (error) {
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
      });
    }
  };
  onMounted(() => {
    // 回显有效期至 当前时间+默认清场时间
    if (paramsData.value.timeLimit !== '0') {
      roomFormsRef.value.setFieldsValue({
        expireTime: serverTime.value + paramsData.value.timeLimit * 60 * 60 * 1000,
      });
    }
  });
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'formTitle1',
        component: 'FormTitle',
        label: t('清场信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          color: '#fff',
        },
      },
      {
        field: 'operatorId',
        component: 'Input',
        label: t('清场人'),
        required: true,
        componentProps: {
          placeholder: t('请选择'),
          readonly: true,
          onClick: () => {
            clearingPeople(1);
          },
        },
      },
      {
        field: 'verifierId',
        component: 'Input',
        label: t('复核人'),
        required: true,
        componentProps: {
          placeholder: t('请选择'),
          readonly: true,
          onClick: () => {
            clearingPeople(2);
          },
        },
      },
      {
        field: 'clearanceTime',
        component: 'BMFormRangePicker',
        defaultValue: [new Date(), new Date()],
        label: t('开始时间-结束时间'),
        colProps: {
          span: 12,
        },
        componentProps: {
          onChange: (value) => {
            segForm.params = {
              ...segForm.params,
              beginTime: timestampToTime(value[0]),
              endTime: timestampToTime(value[1]),
            };
          },
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (value) => {
                if (!value[0]) {
                  return Promise.reject(t('请选择开始使用时间'));
                }
                if (value[0] > value[1]) {
                  return Promise.reject(t('开始时间需小于结束时间'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'expireTime',
        component: 'BMFormDatePicker',
        label: t('有效期至'),
        required: true,
        colProps: {
          span: 12,
        },
        componentProps: {
          onChange: (value) => {
            const date = timestampToTime(value);
            const newDate = date.substring(0, date.length - 2);
            segForm.params = {
              ...segForm.params,
              expireTime: `${newDate}` + '00',
            };
          },
          minDate: new Date(),
        },
      },
    ],
  });

  return {
    roomFormsRef,
    signatureParams,
    signValue,
    formProps,
    handleChange,
    submit,
    clearingPeople,
    submitSign,
    getRoomInfo,
    getRoomAuthUser,
  };
};
