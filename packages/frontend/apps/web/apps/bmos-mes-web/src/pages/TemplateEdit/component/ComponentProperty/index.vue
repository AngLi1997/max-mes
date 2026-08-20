<template>
  <div class="node-item-form">
    <Form ref="BMFormRef" :model="dynamicValidateForm" style="width: 80%; margin: 0 auto">
      <FormItem name="defaultval" :label="t('选项1')" :rules="[re_mes]">
        <Input v-model:value="dynamicValidateForm.defaultval"></Input>
      </FormItem>
      <div v-for="(item, index) in dynamicValidateForm.items" :key="index" align="baseline" class="item-container">
        <FormItem :name="['items', index, 'field']" :rules="re_mes" :label="t('选项') + `${index + 2}`">
          <Input v-model:value="item.field"></Input>
        </FormItem>
        <DeleteOutlined class="item-icon" @click="() => deleteItem(index)" />
      </div>

      <FormItem>
        <Button @click="() => addItem()">
          <PlusOutlined />
          {{ t('新增') }}
        </Button>
      </FormItem>
    </Form>
  </div>
</template>

<script setup lang="ts">
  import { PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue';
  import { t } from '@bmos/i18n';

  const props = withDefaults(
    defineProps<{
      type: string;
      items: Array<any>;
    }>(),
    {
      type: '',
      items: () => [],
    },
  );

  const re_mes = {
    required: true,
    validator: (rule: any, value: any) => {
      if (!value) {
        return Promise.reject(t('请输入'));
      }
      if (rule.field != 'defaultval' && dynamicValidateForm.defaultval == value) {
        return Promise.reject(t('请勿输入重复选项'));
      }
      const list = dynamicValidateForm.items.filter(item => item.field == value);
      if (list.length > 1) {
        return Promise.reject(t('请勿输入重复选项'));
      }
      if (list.length == 1 && rule.field == 'defaultval') {
        return Promise.reject(t('请勿输入重复选项'));
      }
      return Promise.resolve();
    },
  };
  const BMFormRef = ref();

  const dynamicValidateForm = reactive<{
    items: any[];
    defaultval: string | undefined;
  }>({
    items: [],
    defaultval: undefined,
  });

  const deleteItem = (i: number) => {
    dynamicValidateForm.items.splice(i, 1);
  };

  const addItem = () => {
    const len = dynamicValidateForm.items.length + 2;
    dynamicValidateForm.items.push({
      field: undefined,
      name: t('选项' + len),
    });
  };

  const validate = async () => {
    try {
      const { defaultval, items = [] } = await BMFormRef.value.validate();

      return [{ field: defaultval }, ...items];
    } catch (error) {
      throw error;
    }
  };
  defineExpose({ validate });
  watch(
    () => props.items,
    val => {
      if (val && val.length > 0) {
        dynamicValidateForm.defaultval = val[0]?.field;
      }

      dynamicValidateForm.items = val.slice(1);
    },
    { immediate: true },
  );
  onBeforeUnmount(() => {
    dynamicValidateForm.defaultval = void 0;
  });
</script>

<style scoped lang="less">
  .item-container {
    position: relative;
    // display: flex;
    .item-icon {
      position: absolute;
      right: 0;
      top: 50%;
      transform: translateY(-50%) translateX(200%);
    }
  }
  .node-item-form {
    width: 100%;
  }
</style>
