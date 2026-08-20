<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    @cancelModal="SDASD"
    @okModal="ok"></BMModalForm>
  <a-button @click="openModal">Open</a-button>
  <a-button @click="openModal2">qqqq</a-button>
  <!-- <BMModalForm
    ref="modalFormRef"
    title="新增分类"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"
    @cancelModal="SDASD"
    @okModal="ok">
      <template #trigger="{ innerOpenModal }">
        <Button>212</Button>
      </template>
    </BMModalForm> -->
</template>
<script lang="tsx" setup>
  import {
    BMModalForm,
    ModalFormType,
    FormProps,
    ModalFormInstance,
  } from '@bmos/components';
  import { onMounted, reactive, ref } from 'vue';
  import { Button } from 'ant-design-vue';

  const open = ref<boolean | undefined>(false);
  const openModal = () => {
    open.value = true;
  };
  const request = (formModal: any) => {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        console.log(formModal);
        resolve('ok');
      }, 2000);
    });
  };
  const SDASD = () => {
    console.log('cancel');
  };
  const title = ref<string>('新增分类');
  const ok = async ({confirmLoading, innerOpen}: ModalFormType) => {
    try {
      const res = await modalFormRef.value?.submit(request);
      confirmLoading.value = false;
      console.log(res);
    } catch (error) {
      console.log(error);
    }
  };
  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    initialValues: {
      name: '123',
      test: '12333',
    },
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: '分类名称11',
        required: true,
      },
      {
        field: 'test',
        component: 'Input',
        label: '分类test',
        required: true,
      },
      {
        field: 'test1',
        component: 'Input',
        label: '分类',
        required: true,
      },
      {
        field: 'test2',
        component: 'Input',
        label: '分类test',
        required: true,
      },
      {
        field: 'test3',
        component: 'Input',
        label: '分类',
        required: true,
      },
      {
        field: 'test4',
        component: 'Input',
        label: '分类test',
        required: true,
      },
      {
        field: 'test5',
        component: 'Input',
        label: '分类test',
        required: true,
      },
    ],
  });
  // onMounted(() => {
  //   setTimeout(() => {
  //     formProps.initialValues = {};
  //     formProps.schemas.push({
  //       field: 'qqqq',
  //       component: 'Input',
  //       label: 'qqqqq',
  //       required: true,
  //     });
  //     title.value = '修改分类';
  //   }, 5000);

  //   setTimeout(() => {
  //     console.log(formProps.initialValues);
  //     modalFormRef.value?.resetForm();
  //   }, 10000);
  // });

  const openModal2 = () => {
    formProps.initialValues = {};
    open.value = true;
  };
  const submit = async (res: any) => {
    try {
      // const res = await formModal.validate();
      console.log(res);
      return Promise.resolve(true);
    } catch (error) {
      return Promise.resolve(true);
    }
  };
</script>
