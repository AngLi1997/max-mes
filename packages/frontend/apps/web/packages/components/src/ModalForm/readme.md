# 表单弹窗组件

表单弹窗组件，内置触发按钮以及表单验证，传入提交方法即可。

## 基础用法

```vue
<template>
  <BMModalForm
    ref="modalFormRef"
    title="新增模板"
    drag
    :form-props="formProps"
    wrap-class-name="modalSizeMedium"
    :submit="submit" />
</template>

<script lang="tsx" setup>
  import { type FormProps, BMModalForm, type ModalFormInstance } from '@bmos/components';
  import { reactive, ref } from 'vue';

  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: '模板名称',
        required: true,
      },
      {
        field: 'version',
        component: 'Input',
        label: '版本号',
        required: true,
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: '备注',
      },
    ],
  });

  const submit = async (formModal: Record<string, any>) => {
    try {
      console.log(formModal);
      return Promise.resolve();
    } catch (_error: any) {
      console.log(_error);
      return Promise.reject();
    }
  };
</script>
```

## API

### ModalForm Props

| 属性               | 说明                      | 类型                                       | 默认值                                                                                                                              |
| ------------------ | ------------------------- | ------------------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------- |
| AntdV modal        | AntdV modal属性           | `ModalProps`                               | `{ wrapClassName: 'modalSizeMedium', destroyOnClose: true, maskClosable: false }`                                                   |
| formProps          | 同 Form 组件              | `FormProps`                                | `{ showActionButtonGroup: false, labelWidth: 80,wrapperCol: { span: 24 }, baseColProps: { span: 24, }, rowProps: { gutter: 16, } }` |
| triggerButtonProps | trigger 按钮属性          | `ButtonProps`                              | `{ type: 'primary' }`                                                                                                               |
| triggerButtonText  | trigger 按钮文本          | `string`                                   | -                                                                                                                                   |
| submit             | 提交表单数据的方法        | `(formValues: Recordable) => Promise<any>` | -                                                                                                                                   |
| okButtonText       | okButtonText 按钮文本     | `string`                                   | -                                                                                                                                   |
| showOkButton       | 是否显示确定按钮          | `boolean`                                  | `true`                                                                                                                              |
| cancelButtonText   | cancelButtonText 按钮文本 | `string`                                   | -                                                                                                                                   |
| showCancelButton   | 是否显示取消按钮          | `boolean`                                  | `true`                                                                                                                              |

### Events

| 事件名        | 说明         | 回调参数 |
| ------------- | ------------ | -------- |
| register      | 组件注册触发 | -        |
| 'update:open' | open值改变   |          |
| cancelModal   | 同 antdV     | -        |
| okModal       | 同 antdV     | -        |
