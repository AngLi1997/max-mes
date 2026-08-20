<template>
  <InputGroup compact>
    <Select
      v-model:value="qualityControl.id"
      :style="{ width: qualityControlCount > 1 ? '55%' : '100%' }"
      :placeholder="t('质控品')"
      allowClear
      :options="qualityControlCountList"
      @change="triggerChange"></Select>
    <Select
      v-if="qualityControlCount > 1"
      v-model:value="qualityControl.type"
      style="width: 25%"
      :placeholder="t('质控品类型')"
      allowClear
      :options="typeOption"
      @change="qualityControlChange"></Select>
    <Input
      v-if="qualityControlCount > 1 && showOriginValue"
      v-model:value="qualityControl.originValue"
      style="width: 20%"
      allowClear
      :placeholder="t('内置控值')"
      @change="triggerChange"></Input>
  </InputGroup>
</template>

<script lang="ts" setup>
  import { Form, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { InputGroup, Select } from 'ant-design-vue';
  import { Recordable } from '@bmos/components';
  import { useDict } from '@/stores';
  import { SelectValue } from 'ant-design-vue/es/select';

  interface QualityControlValue {
    id: string;
    type: string;
    originValue: string;
    typeName?: string;
  }

  const { getDict } = useDict();

  const props = withDefaults(
    defineProps<{
      qualityControlCount: number;
      qualityControlCountList: Recordable[];
      showOriginValue: boolean;
    }>(),
    {
      qualityControlCount: 0,
      qualityControlCountList: () => [],
      showOriginValue: true,
    },
  );
  const { qualityControlCount } = toRefs(props);

  const qualityControl = defineModel<QualityControlValue>('qualityControl', {
    default: () => ({
      id: undefined,
      type: undefined,
      originValue: undefined,
    }),
  });

  const formItemContext = Form.useInjectFormItemContext();

  const triggerChange = () => {
    formItemContext.onFieldChange();
  };

  const typeOption = ref<Recordable[]>([]);
  const getTypeOptions = async () => {
    try {
      const data = await getDict('质控品类型');
      typeOption.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const qualityControlChange = (value: SelectValue) => {
    if (value) {
      const type = typeOption.value.find(item => item.value === value);
      qualityControl.value.typeName = type?.label;
    }
    formItemContext.onFieldChange();
  };

  onMounted(() => {
    getTypeOptions();
  });
</script>

<style lang="less" scoped></style>
