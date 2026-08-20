<!-- 总发布校验配置 -->
<template>
  <Card class="main">
    <template #title>
      <BMTableTitle :title="t('总发布校验配置')" />
    </template>
    <CardMeta>
      <template #title>
        <BMTableTitle :title="t('公司复检标本设置')" />
      </template>
      <template #description>
        <div class="project-items">
          <div style="margin-right: 12px">{{ t('检验项目') + ':' }}</div>
          <CheckboxGroup v-model:value="checkValue" :options="companyOptions" />
        </div>
      </template>
    </CardMeta>
    <CardMeta>
      <template #title>
        <BMTableTitle :title="t('回访检测标本设置')" />
      </template>
      <template #description>
        <div class="project-items">
          <div style="margin-right: 12px">{{ t('检验项目') + ':' }}</div>
          <CheckboxGroup v-model:value="checkValue2" :options="returnVisitOptions" />
        </div>
      </template>
    </CardMeta>
    <template #actions>
      <div>
        <Button style="margin-right: 8px" @click="initialValues">{{ t('重置') }}</Button>
        <Button type="primary" @click="handleOk">{{ t('确定') }}</Button>
      </div>
    </template>
  </Card>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { BMTableTitle } from '@bmos/components';
  import { getValidatorList, updateValidator } from '@/services';
  import { Card, message } from 'ant-design-vue';

  defineOptions({
    name: 'PublishConfiguration',
  });

  const checkValue = ref<any[]>([]);
  const checkValue2 = ref<any[]>([]);

  const companyOptions = ref<any>([]);
  const returnVisitOptions = ref<any>([]);

  // const { checkValue, companyOptions, returnVisitOptions } = useCheckbox();

  const handleOk = async () => {
    try {
      await updateValidator([...checkValue.value, ...checkValue2.value]);
      message.success(t('保存成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
      initialValues();
    }
  };

  const initialValues = async () => {
    try {
      checkValue.value = [];
      checkValue2.value = [];
      const { data } = await getValidatorList();
      companyOptions.value = data.recheck.map((item: any) => {
        if (item?.value?.value) {
          checkValue.value.push(item?.id);
        }
        return {
          label: item.label,
          value: item.id,
          disabled: !item?.isCanUpdate?.value,
        };
      });
      returnVisitOptions.value = data.followUp.map((item: any) => {
        if (item?.value?.value) {
          checkValue2.value.push(item?.id);
        }
        return {
          label: item.label,
          value: item.id,
          disabled: !item?.isCanUpdate?.value,
        };
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  onActivated(async () => {
    await initialValues();
  });
  onMounted(async () => {
    await initialValues();
  });
</script>

<style lang="less" scoped>
  .main {
    width: 100%;
    height: 100%;
    background-color: white;
    padding: 20px;
    .project-items {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }
  }
</style>
