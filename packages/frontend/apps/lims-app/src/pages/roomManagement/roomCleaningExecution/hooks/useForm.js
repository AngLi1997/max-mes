import { getRoomAuthUserApi, getRoomStatusApi } from '@/api';
import { manageForm } from '@/utils/publicFunctions.js';
import { serverTime, timestampToTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, reactive, ref } from 'vue';

export const useForm = ({ UseParams }) => {
  const { isSign, signOpen, labelList, paramsData, segForm }
    = UseParams;
  // 签名参数
  const roomFormsRef = ref();
  const signValue = ref({
    userName1: '',
    loginName1: '',
    password1: '',
    userId1: '',
  });
  // 清场人
  const options1 = ref([]);
  // 复核人
  const options2 = ref([]);
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
  // 完成
  const submit = () => {
    roomFormsRef.value
      .validate()
      .then(async (res) => {
        const from = {
          ...segForm.params,
          desc: res.desc,
          id: paramsData.value.id,
          status: 3,
          procedureId: paramsData.value.procedureId,
          productId: paramsData.value.productId,
          batchNo: paramsData.value.batchNo,
        };
        await getRoomStatusApi(from);
        uni.navigateBack();
      })
      .catch((error) => {
        console.log(error);
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
    };
    switch (isSign.value) {
      case 1:
        labelList.value = [
          {
            label: t('清场人'),
            signatureAction: 86,
            options: options1.value,
          },
        ];
        signOpen.value = true;
        break;
      case 2:
        labelList.value = [
          {
            label: t('复核人'),
            signatureAction: 87,
            options: options2.value,
          },
        ];
        signOpen.value = true;
        break;
    }
  };

  // 签名
  const submitSign = () => {
    switch (isSign.value) {
      case 1:
        segForm.params = {
          ...segForm.params,
          desc: signValue.value.remark,
          operateId: signValue.value.userId1,
          operateName: signValue.value.userName1,
        };
        roomFormsRef.value.setFieldsValue({
          operatorId: signValue.value.userName1,
        });
        break;
      case 2:
        segForm.params = {
          ...segForm.params,
          verifyId: signValue.value.userId1,
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
        authCode: '121010001002023',
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
          color: '#fff',
        },
        componentProps: {
          color: '#fff',
        },
      },
      {
        field: 'desc',
        component: 'Input',
        label: t('备注'),
        required: true,
        colProps: {
          span: 24,
        },
        componentProps: {
          placeholder: t('请输入备注'),
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
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请选择清场人'),
              validator: (value) => {
                if (!value) {
                  return Promise.reject(t('请选择清场人'));
                }
                if (segForm.params.operateId === segForm.params.verifyId) {
                  return Promise.reject(t('清场人复核人不能相同'));
                }
                return Promise.resolve();
              },
            },
          ];
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
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请选择复核人人'),
              validator: async (value) => {
                if (!value) {
                  return Promise.reject(t('请选择复核人人'));
                }
                if (segForm.params.operateId === segForm.params.verifyId) {
                  return Promise.reject(t('清场人复核人不能相同'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'clearanceTime',
        component: 'BMFormRangePicker',
        defaultValue: [],
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
    signValue,
    formProps,
    handleChange,
    submit,
    clearingPeople,
    submitSign,
    getRoomAuthUser,
  };
};
