<template>
  <!-- 重置密码成功弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    title=""
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium"
    @cancelModal="cancel"
    @okModal="ok">
    <Result
      status="success"
      :title="t('重置密码为') + ':' + sixNum"
      :sub-title="t('请妥善保存') + name + t('密码，尽快进行登录激活')"></Result>
  </BMModalForm>
</template>
<script lang="ts" setup>
  import { Result } from 'ant-design-vue';
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { ref } from 'vue';
  import { t } from '@bmos/i18n';

  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const sixNum = ref('');
  const name = ref('');
  const openModal = () => {
    open.value = true;
  };
  // 新增编辑的表单

  const cancel = () => {
    console.log('cancel');
  };

  const ok = async () => {
    open.value = false;
  };

  defineExpose({ openModal, sixNum, name });
</script>
<style lang="less" scoped></style>
