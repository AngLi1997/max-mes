<template>
  <Drawer
    v-model:open="open"
    class="right-drawer-config"
    root-class-name="right-drawer-config-root"
    :title="t('流程控制器配置')"
    :footer="footer"
    destroyOnClose
    placement="right">
    <BMForm ref="setFormRef" v-bind="setFormProps"></BMForm>
  </Drawer>
</template>

<script lang="tsx" setup>
  import { Button, Drawer, message, Space } from 'ant-design-vue';
  import { BMForm, FormProps, formInstance, Recordable, RenderCallbackParams } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { GatewayType } from '../../enum';

  const emit = defineEmits(['update:open', 'updateCellDataValue']);
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    settingNodeId: {
      type: String,
      default: '',
    },
    isView: {
      type: Boolean,
      default: false,
    },
    settingNodeFormData: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    gatewaySelectNodes: {
      type: Array as PropType<Recordable[]>,
      default: () => [],
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

  const cancelDrawer = () => {
    open.value = false;
  };

  const setFormRef = ref<formInstance>();
  const ok = async () => {
    try {
      const res = await setFormRef.value?.submit();

      open.value = false;
      emit('updateCellDataValue', props.settingNodeId, res);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const footer = (
    <Space class='footer-action'>
      {/* 如果 isView 为 true, 不显示 确定按钮 */}
      {!props.isView && (
        <Button type='primary' onClick={() => ok()}>
          {t('确定')}
        </Button>
      )}
      <Button onClick={() => cancelDrawer()}>{t('取消')}</Button>
    </Space>
  );

  const setFormProps: Ref<FormProps> = ref({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'gatewayType',
        component: 'Select',
        label: t('控制条件'),
        required: true,
        componentProps: {
          options: [
            {
              label: t('并行网关'),
              value: GatewayType.PARALLEL_GATEWAY,
            },
            {
              label: t('选择网关'),
              value: GatewayType.OPTIONAL_GATEWAY,
            },
          ],
        },
      },
      {
        field: 'conditionOnNodes',
        component: 'Select',
        required: true,
        label: t('非必要节点'),
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.gatewayType === GatewayType.OPTIONAL_GATEWAY;
        },
        componentProps: {
          mode: 'multiple',
          options: props.gatewaySelectNodes,
        },
      },
    ],
  });

  watch(
    () => props.open,
    async val => {
      if (!val) return;
      await nextTick();
      setFormRef.value?.updateSchema({
        field: 'conditionOnNodes',
        componentProps: {
          options: props.gatewaySelectNodes,
        },
      });
      setFormRef.value?.setFieldsValue({
        ...props.settingNodeFormData,
      });
      if (props.isView) {
        setFormRef.value?.setFormProps({
          disabled: true,
        });
      }
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>

<style lang="less">
  .right-drawer-config {
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
  }
</style>
<style scoped lang="less"></style>
