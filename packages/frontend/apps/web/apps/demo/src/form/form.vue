<template>
  <div>
    <Card>
      <BMForm
        ref="myFormRef"
        v-bind="formProps"
        @submit="handleSubmit"
        @reset="handleSet">
        <template #selectA="{ formModel, field }">
          <Select
            v-model:value="formModel[field]"
            :options="optionsA"
            mode="multiple"
            allow-clear
            @change="valueSelectA = formModel[field]" />
        </template>
        <template #selectB="{ formModel, field }">
          <Select
            v-model:value="formModel[field]"
            :options="optionsB"
            mode="multiple"
            allow-clear
            @change="valueSelectB = formModel[field]" />
        </template>
        <template #resetBefore>
          <Button type="primary" shape="round" @click="returnClick">
            自定义
          </Button>
        </template>
      </BMForm>
    </Card>
  </div>
</template>

<script lang="tsx" setup> 
  import { computed, ref, unref } from 'vue';
  import { cloneDeep } from '@bmos/utils';
  import { message, Select, Card, Button } from 'ant-design-vue';
  import { schemas } from './form-schema';
  import { formInstance, BMForm, FormProps } from '@bmos/components';

  defineOptions({
    name: 'DemosFormBasicForm',
  });

  const formProps: FormProps = {
    schemas,
    labelWidth: 120,
    baseColProps: {
      span: 6,
    },
    autoAdvancedLine: 6,
    alwaysShowLines: 6,
    actionColOptions: {
      span: 6,
      style: {
        textAlign: 'center',
      },
    },
    showAdvancedButton: true,
  };

  const valueSelectA = ref<string[]>([]);
  const valueSelectB = ref<string[]>([]);
  const options = ref<any>([]);
  options.value = Array.from({ length: 10 }).map((_, i) => ({
    label: `选项${i}`,
    value: `${i}`,
  }));

  const optionsA = computed(() => {
    return cloneDeep(unref(options)).map(
      (op: { disabled: boolean; value: string }) => {
        op.disabled = unref(valueSelectB).indexOf(op.value) !== -1;
        return op;
      },
    );
  });
  const optionsB = computed(() => {
    return cloneDeep(unref(options)).map(
      (op: { disabled: boolean; value: string }) => {
        op.disabled = unref(valueSelectA).indexOf(op.value) !== -1;
        return op;
      },
    );
  });

  // 点击提交
  function handleSubmit(values: any) {
    message.success(
      <div>
        验证通过！<pre class='text-left'>{JSON.stringify(values, null, 2)}</pre>
      </div>,
      3,
    );
  }

  const myFormRef = ref<formInstance>();
  const handleSet = () => {
    console.log('myFormRef', myFormRef.value?.formModel);
  };

  const returnClick = () => {
    console.log('返回');
  };
</script>
