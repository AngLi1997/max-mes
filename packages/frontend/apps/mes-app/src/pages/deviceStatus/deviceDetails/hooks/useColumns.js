import {
  getEquipmentAppAllStation,
  getEquipmentAppInfo,
  getListPlanByProcess,
  getProcessListTree,
  putEquipmentAppApply,
  putEquipmentAppFault,
  putEquipmentAppOperateProperty,
  putEquipmentAppRecover,
  putEquipmentAppRelease,
} from '@/api';
import { formatTime, getCurrentTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import { buttonState, stateEquipment } from '../enum.js';

export const useColumns = ({ UseParams }) => {
  // 占用弹框
  const ModelFromRef = ref();
  // 日期弹框
  const showDatePicker = ref(false);
  const stateType = ref();
  // 点击的当前数据
  const indexData = ref();
  const showSign = ref(false);
  const msgContent = ref('');
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: '',
  });
  const showMessageBox = ref(false);
  const msgBoxTitle = ref('');
  const showModelFrom = ref(false);
  const { specifics, occupation, signatureData }
    = UseParams;
  const basicItems = ref([
    {
      label: t('设备编号'),
      field: 'code',
    },
  ]);
  const labelList = ref([
    {
      label: t('操作人'),
      signatureAction: 0,
    },
  ]);
  const IDQuery = ref();
  const datePickerValue = ref('');
  const defaultDate = ref('');
  const datePickerRef = ref();
  const equipmentAppInfo = async () => {
    showSign.value = false;
    try {
      const { data } = await getEquipmentAppInfo(IDQuery.value.id);
      specifics.value = {
        ...data,
        productionLineName: data.productionLineNameList?.map(item => item.name).join('，'),
        roomName: data.roomNameList?.map(item => item.name).join('，'),
        stationName: data.stationNameList?.map(item => item.name).join('，'),
      };
      basicItems.value = [
        {
          label: t('设备编号'),
          field: 'code',
        },
      ];
      specifics.value.infoPropertyList?.forEach((item) => {
        basicItems.value.push({
          label: item.name,
          field: item.code,
        });
        specifics.value[item.code] = item.showValue ? item.showValue : item.value;
      });
    }
    catch (error) {
      // TODO handle the exception
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  const tabsChange = (tag, item) => {
    console.log('ffff', tag, '123', item);
    stateType.value = buttonState[5].hex;
    indexData.value = item;
    specifics.value.equipmentStatusAppVOList?.forEach((el) => {
      if (el.id.includes(indexData.value.id)) {
        el.finishStatus = tag;
        labelList.value = [
          {
            ...labelList.value[0],
            signatureAction: stateEquipment[el.code],
          },
        ];
      }
    });
    if (tag) {
      const date = new Date(getCurrentTime()).getTime();
      datePickerValue.value = new Date(date + item.value?.split(',')[0] * 24 * 60 * 60 * 1000);
      defaultDate.value = new Date(date + item.value?.split(',')[0] * 24 * 60 * 60 * 1000);
      showDatePicker.value = true;
    }
    else {
      specifics.value.equipmentStatusAppVOList?.forEach((el) => {
        if (el.id.includes(indexData.value?.id)) {
          signatureData.value = {
            ...el,
            expireDate: null,
          };
        }
      });
      signValue.value = {
        loginName1: '',
        password1: '',
        userId1: '',
      };
      showSign.value = true;
    }
  };

  // 选择日期恢复默认
  const restoreDefault = () => {
    datePickerRef.value.restoreDefault();
  };

  // 故障
  const equipmentAppFault = (type) => {
    stateType.value = buttonState[type].hex;
    labelList.value = [
      {
        ...labelList.value[0],
        signatureAction: buttonState[type].signatureAction,
      },
    ];
    showMessageBox.value = true;
    msgBoxTitle.value = `${t('是否将设备状态设置为') + buttonState[type].name}?`;
    msgContent.value = type === 3 ? `${buttonState[type].name}${t('状态下的设备无法使用')}` : '';
  };

  const faultConfirm = () => {
    signatureData.value = {
      id: specifics.value.id,
    };
    signValue.value = {
      loginName1: '',
      password1: '',
      userId1: '',
    };
    showSign.value = true;
  };
  const msgBoxConfirm = () => {
    if (stateType.value === 'occupation') {
      // 占用
      showModelFrom.value = true;
    }
    else {
      // 故障/恢复/占用
      faultConfirm();
    }
  };

  // 占用使用框
  const submitSuccess = async () => {
    ModelFromRef.value?.submit();
    const params = await ModelFromRef.value?.validate();
    occupation.value = {
      ...occupation.value,
      ...params,
    };
    signatureData.value = {
      ...occupation.value,
      id: specifics.value.id,
    };
    showModelFrom.value = false;
    signValue.value = {
      loginName1: '',
      password1: '',
      userId1: '',
    };
    showSign.value = true;
  };
  // 日期取消
  const cancelDatePicker = () => {
    showDatePicker.value = false;
    specifics.value.equipmentStatusAppVOList?.forEach((el) => {
      if (el.id.includes(indexData.value.id)) {
        el.finishStatus = !indexData.value.finishStatus;
      }
    });
  };
  // 日期确认
  const submitDate = async (date, _item) => {
    datePickerValue.value = date;
    showDatePicker.value = false;
    specifics.value.equipmentStatusAppVOList?.forEach((el) => {
      if (el.id.includes(indexData.value?.id)) {
        const nu = formatTime(datePickerValue.value, 'datetime');
        signatureData.value = {
          ...el,
          expireDateTime: nu,
        };
      }
    });
    signValue.value = {
      loginName1: '',
      password1: '',
      userId1: '',
    };
    showSign.value = true;
  };
  // 更改状态
  const equipmentAppOperateProperty = async (item, el) => {
    try {
      const paramsData = {
        ...el,
        expireDateTime: el.finishStatus ? item : null,
      };
      const res = await putEquipmentAppOperateProperty(paramsData);
      return Promise.resolve(res);
    }
    catch (error) {
      // TODO handle the exception
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
      return Promise.reject(error.message);
    }
  };
  // 签名确认
  const signSubmit = async () => {
    uni.showLoading({
      title: t('更改中...'),
      mask: true,
    });
    try {
      switch (stateType.value) {
        case buttonState[1].hex:
          await putEquipmentAppRelease({
            id: specifics.value.id,
          });
          uni.hideLoading();
          await equipmentAppInfo();
          break;
        case buttonState[2].hex:
          await putEquipmentAppApply({
            ...occupation.value,
            id: specifics.value.id,
          });
          uni.hideLoading();
          await equipmentAppInfo();
          break;
        case buttonState[3].hex:
          await putEquipmentAppFault({
            id: specifics.value.id,
          });
          uni.hideLoading();
          await equipmentAppInfo();
          break;
        case buttonState[4].hex:
          await putEquipmentAppRecover({
            id: specifics.value.id,
          });
          uni.hideLoading();
          await equipmentAppInfo();
          break;
        case buttonState[5].hex:
        { const result = [];
          specifics.value.equipmentStatusAppVOList?.forEach((el) => {
            if (el.id.includes(indexData.value?.id)) {
              const nu = formatTime(datePickerValue.value, 'datetime');
              const p = equipmentAppOperateProperty(nu, el);
              result.push(p);
            }
          });
          Promise.all(result).then(async () => {
            uni.hideLoading();
            await equipmentAppInfo();
          });
          break; }
      }
    }
    catch (error) {
      // TODO handle the exception
      uni.hideLoading();
      error.message
      && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  const signCancel = () => {
    equipmentAppInfo();
  };
  // 获取当前日期加N天后的时间戳
  // const addDaysAndGetTimestamp = (date = new Date(), daysToAdd = 1) => {
  //   // 获取当前日期
  //   const currentDate = new Date(date);
  //   // 设置新的日期为当前日期加指定的天数
  //   currentDate.setDate(currentDate.getDate() + daysToAdd);
  //   // 获取时间戳（毫秒）
  //   const timestamp = currentDate.getTime();
  //   return timestamp;
  // };
  const getChildrenData = (arr) => {
    const newArr = [];
    arr?.forEach((item) => {
      item.categoryFlag = item.categoryFlag === null;
      if (item.children && item.children.length > 0) {
        item.children = getChildrenData(item.children);
      }
      newArr.push(item);
    });
    return newArr;
  };
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'processId',
        component: 'BMFormSelect',
        label: t('工艺'),
        colProps: {
          span: 24,
        },
        componentProps: ({ formModel }) => {
          return {
            request: async () => {
              const { data } = await getProcessListTree({ activeProcess: true, filterPermission: true });
              return getChildrenData(data);
            },
            placeholder: t('请选择工艺'),
            type: 'tree',
            options: [],
            title: t('工艺'),
            fieldNames: {
              name: 'showName',
              key: 'id',
              checkKey: 'categoryFlag',
              checkKeyValue: true,
              parentId: 'parentId',
              children: 'children',
            },
            onConfirm: async (value) => {
              // 重置数据
              formModel.batchNo = '';
              if (!value.id) {
                ModelFromRef.value?.updateSchema({
                  field: 'batchNo',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              // 获取生产批号下拉
              const { data } = await getListPlanByProcess(value.id);
              ModelFromRef.value?.updateSchema({
                field: 'batchNo',
                componentProps: {
                  options: [...data],
                },
              });
            },
            onClear: async () => {
              formModel.batchNo = '';
              ModelFromRef.value?.updateSchema({
                field: 'batchNo',
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
        componentProps: () => {
          return {
            options: [],
            title: t('生产批号'),
            fieldNames: {
              label: 'batchNo',
              value: 'id',
            },
            onConfirm: (data) => {
              occupation.value = {
                ...data,
              };
            },
            onChange: (data) => {
              if (!data) {
                occupation.value = {};
              }
            },
          };
        },
      },
      {
        field: 'stationId',
        component: 'BMFormSelect',
        label: t('使用工位'),
        colProps: {
          span: 24,
        },
        componentProps: () => {
          return {
            title: t('使用工位'),
            request: async () => {
              const res = await getEquipmentAppAllStation({
                equipmentId: IDQuery.value.id,
              });
              const data = res.data.map((item) => {
                return {
                  label: item.name,
                  value: item.stationId,
                };
              });
              return data;
            },
          };
        },
      },
    ],
  });
  return {
    ModelFromRef,
    showDatePicker,
    IDQuery,
    labelList,
    showSign,
    signValue,
    showMessageBox,
    msgBoxTitle,
    formProps,
    showModelFrom,
    datePickerValue,
    msgContent,
    datePickerRef,
    basicItems,
    msgBoxConfirm,
    equipmentAppInfo,
    tabsChange,
    equipmentAppFault,
    signSubmit,
    signCancel,
    submitSuccess,
    submitDate,
    cancelDatePicker,
    restoreDefault,
  };
};
