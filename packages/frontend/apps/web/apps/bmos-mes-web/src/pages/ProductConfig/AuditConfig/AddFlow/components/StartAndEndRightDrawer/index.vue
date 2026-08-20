<template>
  <Drawer
    v-model:open="open"
    class="flow-start-and-end-right-drawer-config"
    root-class-name="flow-start-and-end-right-drawer-config-root"
    :title="title"
    :footer="footer"
    destroyOnClose
    placement="right"
    @afterOpenChange="afterOpenChange">
    <BMForm ref="setFormRef" v-bind="setFormProps"></BMForm>
  </Drawer>
</template>

<script lang="tsx" setup>
  import { Button, Space, message } from 'ant-design-vue';
  import { Recordable, BMForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks/useForm';

  const emit = defineEmits(['update:open', 'updateFormValue']);
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    settingNodeId: {
      type: String,
      default: '',
    },
    settingNodeFormData: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    isView: {
      type: Boolean,
      default: false,
    },
    isStartNode: {
      type: Boolean,
      default: false,
    },
  });

  // computed set gte 监听open变化
  const open = computed({
    get() {
      return props.open;
    },
    set(val) {
      emit('update:open', val);
    },
  });

  const title = computed(() => {
    return props.isStartNode ? t('开始节点配置') : t('结束节点配置');
  });

  const cancelDrawer = () => {
    open.value = false;
  };

  const ok = async () => {
    try {
      const res = await setFormRef.value?.submit();
      open.value = false;
      emit('updateFormValue', props.settingNodeId, res);
    } catch (error) {}
  };

  const okBtnLoading = ref<boolean>(false);
  const footer = (
    <Space class='footer-action'>
      {/* 如果 isView 为 true, 不显示 确定按钮 */}
      {!props.isView && (
        <Button type='primary' loading={okBtnLoading.value} onClick={() => ok()}>
          {t('确定')}
        </Button>
      )}
      <Button onClick={() => cancelDrawer()}>{t('取消')}</Button>
    </Space>
  );

  const { setFormProps, setNodeFormData, setFormRef } = useForm();
  // 监听open变化
  const afterOpenChange = (open: boolean) => {
    if (open) {
      okBtnLoading.value = true;
      try {
        setNodeFormData(props.settingNodeFormData);
      } catch (error: any) {
        error.message && message.error(error.message);
      } finally {
        okBtnLoading.value = false;
      }
      if (props.isView) {
        setFormRef.value?.setFormProps({
          disabled: true,
        });
      }
    }
  };
</script>

<style lang="less">
  .flow-start-and-end-right-drawer-config {
    .mes-drawer-header-title {
      flex-direction: row-reverse;
      .mes-drawer-close {
        margin-right: 0;
      }
    }
    .mes-drawer-footer {
      .footer-action {
        display: flex;
        justify-content: end;
        flex-direction: row-reverse;
      }
    }
    .clear-label {
      position: absolute;
      right: 20%;
      top: 50%;
      transform: translateY(-50%);
      cursor: pointer;
      z-index: 99;
    }
  }
</style>
