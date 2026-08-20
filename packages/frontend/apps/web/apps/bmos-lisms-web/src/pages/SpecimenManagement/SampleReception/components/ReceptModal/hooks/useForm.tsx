import { FormProps, FormSchema, ModalFormInstance } from '@bmos/components';
import { BMIcons } from '@bmos/icons';
import { Button } from 'ant-design-vue';

export const useForm = () => {
  const { transportStatusDict } = getDicts();
  const modalFormRef = ref<ModalFormInstance>();

  const cnt = ref(0);

  const temperatureGroup = ref([0]);
  const temperatureIdx = ref(0);

  const addTemperatureTemp = (index: number) => {
    const newIndex = index + 1;
    modalFormRef.value?.formRef?.appendSchemasByField(
      createTemperatureTemp(newIndex),
      `${temperatureGroup.value[temperatureIdx.value]}`,
    );
    temperatureGroup.value.push(newIndex);
    cnt.value++;
    temperatureIdx.value++;
  };

  const deleteTemperatureTemp = (index: number) => {
    const delFields = [`minTemperature${index}`, `maxTemperature${index}`, `${index}`];
    modalFormRef.value?.formRef?.removeSchemaByFiled(delFields);
    temperatureGroup.value.splice(temperatureGroup.value.indexOf(index), 1);
    temperatureIdx.value--;
  };

  const createTemperatureTemp = (index: number): FormSchema<any>[] => {
    return [
      {
        label: t('最低温度'),
        field: 'minTemperature' + index,
        required: true,
        component: 'InputNumber',
        colProps: {
          span: 10,
        },
        componentProps: {
          addonAfter: '°C',
          precision: 1,
          min: -999.9,
          max: 999.9,
        },
      },
      {
        label: t('最高温度'),
        field: 'maxTemperature' + index,
        required: true,
        component: 'InputNumber',
        colProps: {
          span: 10,
        },
        componentProps: {
          addonAfter: '°C',
          precision: 1,
          min: -999.9,
          max: 999.9,
        },
      },
      {
        label: ``,
        field: `${index}`,
        noLabel: true,
        colProps: {
          span: 4,
        },
        component: () => {
          return (
            <Button style='padding: 0' type='link' danger onClick={() => deleteTemperatureTemp(index)}>
              <BMIcons class='delete-icon' icon='Delete' />
            </Button>
          );
        },
      },
    ];
  };

  const formProps = reactive<FormProps>({
    initialValues: {},
    schemas: [
      {
        label: t('运输状态'),
        field: 'transportStatus',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
        },
        componentProps: {
          options: transportStatusDict,
        },
      },
      {
        label: t('运抵时间'),
        field: 'transportArrivalTime',
        required: true,
        component: 'DatePicker',
        colProps: {
          span: 12,
        },
        componentProps: {
          showTime: true,
          format: 'YYYY-MM-DD HH:mm:ss',
          valueFormat: 'YYYY-MM-DD HH:mm:ss',
        },
      },
      {
        label: t('最低温度'),
        field: 'minTemperature0',
        required: true,
        component: 'InputNumber',
        colProps: {
          span: 10,
        },
        componentProps: {
          addonAfter: '°C',
          precision: 1,
          min: -999.9,
          max: 999.9,
        },
      },
      {
        label: t('最高温度'),
        field: 'maxTemperature0',
        required: true,
        component: 'InputNumber',
        colProps: {
          span: 10,
        },
        componentProps: {
          addonAfter: '°C',
          precision: 1,
          min: -999.9,
          max: 999.9,
        },
      },
      {
        label: '',
        noLabel: true,
        field: '0',
        component: () => {
          return (
            <Button type='primary' disabled={temperatureIdx.value >= 4} onClick={() => addTemperatureTemp(cnt.value)}>
              {t('添加温度记录')}
            </Button>
          );
        },
        colProps: {
          span: 4,
        },
      },
      {
        label: t('备注'),
        field: 'applyRemark',
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
        componentProps: {
          maxlength: 200,
          showCount: true,
        },
      },
    ],
  });

  const setFormModels = (values: any) => {
    modalFormRef.value?.formRef?.setFormModels(values);
  };

  const initSchemas = () => {
    temperatureGroup.value.forEach((item: number) => {
      if (item) {
        deleteTemperatureTemp(item);
      }
    });
    temperatureGroup.value = [0];
    temperatureIdx.value = 0;
    cnt.value = 0;
  };

  return {
    modalFormRef,
    formProps,
    setFormModels,
    temperatureGroup,
    initSchemas,
  };
};
