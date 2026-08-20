<template>
  <NormalModalForm
    v-model:open="permissionOpen"
    :title="t('数据权限')"
    :submit="okModal"
    destroyOnClose
    wrap-class-name="modalSizeMedium"
    class="permission-modal">
    <DepartMent ref="departMentRef" :record="processId" :isAdd="processId === ''" :type="false" />
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import DepartMent from '@/components/DepartMent/index.vue';
  import { NormalModalForm } from '@bmos/components';

  const emits = defineEmits(['update:permissionOpen', 'ok']);
  const props = defineProps({
    permissionOpen: {
      type: Boolean,
      default: false,
    },
    processId: {
      type: String,
      default: '',
    },
  });

  const departMentRef = ref();

  const permissionOpen = computed<boolean>({
    get() {
      return props.permissionOpen;
    },
    set(val) {
      emits('update:permissionOpen', val);
    },
  });

  const okModal = async () => {
    try {
      const checkedKeys = departMentRef.value.getSelectKeys();
      // 如果没选部门， 提示
      if (!checkedKeys.length) {
        message.error(t('请选择部门'));
        return Promise.reject();
      }
      emits('ok', checkedKeys);
      permissionOpen.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(error);
    }
  };
</script>

<style lang="less"></style>
