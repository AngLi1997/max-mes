<template>
  <a-form
    ref="formRef"
    :model="formState"
    :rules="rules"
    :label-col="labelCol"
    :wrapper-col="wrapperCol">
    <a-form-item ref="name" label="Activity name" name="name" :colon="false">
      <a-input v-model:value="formState.name" />
    </a-form-item>
    <a-form-item label="Activity zone" name="region" :colon="false">
      <a-select
        v-model:value="formState.region"
        placeholder="please select your zone">
        <a-select-option value="shanghai">Zone one</a-select-option>
        <a-select-option value="beijing">Zone two</a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item label="Activity time" required name="date1" :colon="false">
      <a-date-picker
        v-model:value="formState.date1"
        show-time
        type="date"
        placeholder="Pick a date"
        style="width: 100%;" />
    </a-form-item>
    <a-form-item label="Instant delivery" name="delivery" :colon="false">
      <a-input v-model:checked="formState.delivery" />
    </a-form-item>
    <a-form-item label="Activity type" name="type" :colon="false">
      <a-input v-model:value="formState.type" />
    </a-form-item>
    <a-form-item label="Resources" name="resource" :colon="false">
      <a-input v-model:value="formState.resource" />
    </a-form-item>
    <a-form-item label="Activity form" name="desc" :colon="false">
      <a-textarea v-model:value="formState.desc" />
    </a-form-item>
    <a-form-item :wrapper-col="{ span: 14, offset: 4 }" :colon="false">
      <a-button type="primary" @click="onSubmit">Create</a-button>
      <a-button style="margin-left: 10px;" @click="resetForm">Reset</a-button>
    </a-form-item>
  </a-form>
</template>
<script lang="ts" setup>
  import { Dayjs } from 'dayjs';
  import { reactive, ref, toRaw } from 'vue';
  import type { UnwrapRef } from 'vue';
  import type { Rule } from 'ant-design-vue/es/form';

  interface FormState {
    name: string;
    region: string | undefined;
    date1: Dayjs | undefined;
    delivery: boolean;
    type: string;
    resource: string;
    desc: string;
  }
  const formRef = ref();
  const labelCol = { span: 5 };
  const wrapperCol = { span: 13 };
  const formState: UnwrapRef<FormState> = reactive({
    name: '',
    region: undefined,
    date1: undefined,
    delivery: false,
    type: '',
    resource: '',
    desc: '',
  });
  const rules: Record<string, Rule[]> = {
    name: [
      {
        required: true,
        message: 'Please input Activity name',
        trigger: 'change',
      },
      { min: 3, max: 5, message: 'Length should be 3 to 5', trigger: 'blur' },
    ],
    region: [
      {
        required: true,
        message: 'Please select Activity zone',
        trigger: 'change',
      },
    ],
    date1: [
      {
        required: true,
        message: 'Please pick a date',
        trigger: 'change',
        type: 'object',
      },
    ],
    type: [
      {
        type: 'array',
        required: true,
        message: 'Please select at least one activity type',
        trigger: 'change',
      },
    ],
    resource: [
      {
        required: true,
        message: 'Please select activity resource',
        trigger: 'change',
      },
    ],
    desc: [
      {
        required: true,
        message: 'Please input activity form',
        trigger: 'blur',
      },
    ],
  };
  const onSubmit = () => {
    formRef.value
      .validate()
      .then(() => {
        console.log('values', formState, toRaw(formState));
      })
      .catch((error: any)  => {
        console.log('error', error);
      });
  };
  const resetForm = () => {
    formRef.value.resetForm();
  };
</script>
