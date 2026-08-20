<template>
  <NormalModalForm
    v-model:open="permissionOpen"
    :title="t('数据权限')"
    :submit="okModal"
    destroyOnClose
    wrap-class-name="modalSizeMedium"
    class="permission-modal">
    <DepartMent ref="departMentRef" :record="resourceId" :isAdd="isAdd" :checks="checks" :type="type" />
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { computed } from 'vue';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import DepartMent from '@/components/DepartMent/index.vue';
  import { reqResourcePermissionSaveReq } from '@/services';
  import { NormalModalForm } from '@bmos/components';

  defineOptions({
    name: 'PermissionModal',
    inheritAttrs: false,
  });

  const emits = defineEmits(['update:permissionOpen', 'ok']);
  const props = defineProps({
    permissionOpen: {
      type: Boolean,
      default: false,
    },
    isAdd: {
      type: Boolean,
      default: false,
    },
    resourceId: {
      type: String,
      default: '',
    },
    // 是否获取全量数据， true: 全量数据， false: 部分部门权限数据
    type: {
      type: Boolean,
      default: true,
    },
    // 选中的数据
    checks: {
      type: Array,
      default: () => [],
    },
    saveImmediate: {
      type: Boolean,
      default: true,
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
      if (!props.saveImmediate) {
        emits('ok', checkedKeys);
        permissionOpen.value = false;
        return Promise.resolve();
      }
      await reqResourcePermissionSaveReq({
        deptIds: checkedKeys,
        resourceId: props.resourceId,
      });
      message.success(t('保存数据权限成功'));
      emits('ok');
      permissionOpen.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(error);
    }
  };
</script>

<style lang="less"></style>
