<template>
  <div class="formula-check">
    <div v-if="formulaShow" class="formula-check-container">
      <div class="form-container">
        <BMForm ref="formRef" v-bind="formProps" @click="() => changeStatus()"></BMForm>
        <Params
          v-if="currentFormula.formulaId"
          :show="show"
          :formulaType="currentFormula.formulaId"
          :component="curComponent"
          :params="currentFormula"
          @click="() => changeStatus()"
          @delete-param="deleteParam"
          @add="(...args) => $emit('add', ...args)"></Params>
      </div>
      <div v-if="!show" style="width: 100%; display: flex; justify-content: flex-end; column-gap: 16px">
        <Button @click="cancelCheck">{{ t('取消') }}</Button>
        <Button @click="clearFormula">{{ t('清除') }}</Button>
        <Button type="primary" @click="() => formSubmit(component!)">
          {{ t('确认') }}
        </Button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import Params from './Params.vue';
  import { t } from '@bmos/i18n';
  import { useForm, emitTypes } from './useForm';
  import { Button } from 'ant-design-vue';
  import { ComponentNode } from '../../../components/Record/NodeList/type';
  import { useChangeStatus } from '../store/useChangeStatus';
  import { BMForm } from '@bmos/components';
  const props = withDefaults(
    defineProps<{
      show?: boolean;
      component?: ComponentNode;
    }>(),
    {
      show: false,
    },
  );
  const { changeStatus } = useChangeStatus();
  const emit = defineEmits(emitTypes);
  const curComponent = computed(() => {
    return props.component;
  });

  const formulaShow = computed(() => {
    return props.show ? !!curComponent.value?.formulaId : !!curComponent.value;
  });

  const { formSubmit, cancelCheck, clearFormula, currentFormula, deleteParam, formRef, formProps } = useForm(
    curComponent,
    emit,
    props.show,
    changeStatus,
  );
</script>

<style scoped lang="less">
  .formula-check {
    height: 100%;

    .formula-check-container {
      display: flex;
      flex-direction: column;
      height: 100%;
    }

    .form-container {
      flex: 1;
      overflow-y: auto;
      margin-bottom: 16px;
    }
  }
</style>
