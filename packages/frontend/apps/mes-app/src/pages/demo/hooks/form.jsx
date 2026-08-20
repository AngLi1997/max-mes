import { reactive, ref } from 'vue';

export const useForm = () => {
  const formRef = ref();

  const formProps = reactive({
    schemas: [
      {
        field: 'formTitle1',
        component: 'FormTitle',
        label: '标题',
        colProps: {
          span: 24
        }
      },
      {
        field: 'name',
        component: 'Input',
        label: '物料名称',
        colProps: {
          span: 12
        },
        required: true
        // dynamicRules: () => {
        //   return [
        //     {
        //       required: true,
        //       message: 'Please enter your name',
        //       trigger: 'blur'
        //     }
        //   ];
        // }
      },
      {
        field: 'email',
        component: 'Input',
        label: () => {
          return '物料批次';
        },
        componentProps: {
          placeholder: 'Please enter your 物料批次'
        }
      },
      {
        field: 'expirationDate',
        component: 'Calendar',
        label: '有效期'
      },
      {
        field: 'BMFormRadio',
        component: 'BMFormRadio',
        label: 'RADIO',
        componentProps: {
          options: [
            {
              label: 'Option1',
              value: 'Option1'
            },
            {
              label: 'Option2',
              value: 'Option2'
            }
          ]
        }
      },
      {
        field: 'BMFormCheckbox',
        component: 'BMFormCheckbox',
        label: 'Checkbox',
        componentProps: {
          options: [
            {
              label: 'Option1',
              value: 'Option1'
            },
            {
              label: 'Option2',
              value: 'Option2',
              disabled: true
            }
          ]
        }
      },
      {
        field: 'formTitle',
        component: 'FormTitle',
        label: 'FormTitle',
        colProps: {
          span: 24
        },
        // 自定义组件背景色
        componentProps: {
          color: '#fff'
        }
      },
      {
        field: 'password',
        component: 'Input',
        label: 'Password'
      }
    ]
  });

  const submit = async() => {
    try {
      const formData = await formRef.value.validate();
      console.log('formData', formData);
    } catch (error) {
      console.log('error', error);
    }
  };

  return {
    formRef,
    formProps,
    submit
  };
};
