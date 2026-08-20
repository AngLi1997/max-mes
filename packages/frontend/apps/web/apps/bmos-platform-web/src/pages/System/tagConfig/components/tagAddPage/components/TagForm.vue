<template>
  <Space v-for="(user, index) in ValidateForm.users" :key="index" class="tag-from" align="baseline" size="middle">
    <FormItem :name="['ValidateForm', index, 'label']" :label="t('字段') + `${index + 1}:`">
      <Input v-model:value="user.label" :placeholder="t('请输入')" :maxlength="10" />
    </FormItem>
    <FormItem :name="['ValidateForm', index, 'dataSourceField']" label="1" class="tag-source-field">
      <Select
        v-model:value="user.dataSourceField"
        :placeholder="t('请选择')"
        :fieldNames="fieldNames"
        :options="DropInfoFrom.dropInfo"
        allowClear />
    </FormItem>
  </Space>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { User } from '../../../types';
  const emit = defineEmits<{
    (e: 'update:modify', selects: User[]): void;
  }>();
  const props = withDefaults(
    defineProps<{
      selects: User[];
      dropInfo: object[];
    }>(),
    {},
  );
  const DropInfoFrom = reactive<{ dropInfo: object[] }>({
    dropInfo: [],
  });
  const ValidateForm = reactive<{ users: User[] }>({
    users: [],
  });
  const fieldNames = {
    label: 'label',
    value: 'field',
  };
  watch(
    [() => props.selects, () => props.dropInfo],
    ([val, Info]) => {
      ValidateForm.users = val as User[];
      DropInfoFrom.dropInfo = Info as object[];
      emit('update:modify', val as User[]);
    },
    {
      deep: true,
    },
  );
</script>

<style lang="less" scoped>
  .tag-from {
    width: 100%;
    &:deep(.plat-space-item):nth-child(1) {
      width: 100%;
    }
    &:deep(.plat-space-item):nth-child(2) {
      width: 100%;
    }
  }
  :deep(.tag-source-field) {
    .plat-form-item-label {
      width: 0;
    }
  }
</style>
