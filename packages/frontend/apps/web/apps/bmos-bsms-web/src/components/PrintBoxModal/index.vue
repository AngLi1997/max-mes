<!-- 打印箱号 -->
<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('打印箱号')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { printSortingMaintainPlasma, printSortingMaintainSample } from '@/services';
  import { message } from 'ant-design-vue';
  import { useForm } from './hooks/useForm';
  import { ref } from 'vue';

  const { modalFormRef, formProps, setFormModels, updateSchema } = useForm();

  const itemType = ref<number>(1); // 1-血浆 2-标本

  const open = ref<boolean>(false);

  const openModal = async (type: number) => {
    itemType.value = type;
    open.value = true;
    await nextTick();
    updateSchema({
      field: 'weight',
      vIf: type == 1 ? true : false,
    });
    setFormModels({
      itemType: type,
    });
  };

  const request = async (formModal: any) => {
    try {
      let res;
      if (formModal.itemType == 1) {
        res = await printSortingMaintainPlasma({ boxNo: formModal.boxNo });
      } else {
        res = await printSortingMaintainSample({ boxNo: formModal.boxNo });
      }
      // 唤起打印窗口
      printFn(res.data);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 提交
  const submit = async () => {
    try {
      await modalFormRef.value?.submit(request);
      message.success(t('操作成功'));
      open.value = false;
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const printFn = (data: any) => {
    // 唤起打印窗口
    let iframe;
    let blob = new Blob([data], { type: 'application/pdf' });
    let doc: any = null;
    iframe = document.createElement('iframe');
    iframe.setAttribute('id', 'print-iframe');
    iframe.setAttribute('src', window.URL.createObjectURL(blob));
    document.body.appendChild(iframe);
    doc = iframe.contentWindow?.document;
    //这里可以自定义样式
    iframe.onload = () => {
      iframe.contentWindow?.focus();
      iframe.contentWindow?.print();
    };
    doc = iframe.contentWindow?.document;
    doc.close();
  };

  defineExpose({
    openModal,
    request,
  });
</script>
<style lang="less"></style>
