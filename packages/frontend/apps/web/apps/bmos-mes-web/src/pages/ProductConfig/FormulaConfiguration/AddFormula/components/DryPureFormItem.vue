<template>
  <Row>
    <Col span="24">
      <FormItem
        :name="[props.typeField]"
        :label="t('折干折纯')"
        :labelCol="props.labelCol || { style: { width: '115px' } }"
        :required="true"
        :rules="dryPureRules">
        <InputGroup>
          <Row>
            <Col span="13">
              <Select
                v-model:value="formModel[props.typeField]"
                :style="{ width: '90%' }"
                :fieldNames="{
                  label: 'label',
                  value: 'value',
                }"
                :disabled="props.viewMode"
                :placeholder="t('请选择类型')"
                :options="[
                  { label: t('无'), value: 0 },
                  { label: t('折纯'), value: 1 },
                  { label: t('折干折纯'), value: 2 },
                  { label: t('折干折纯带参数'), value: 3 },
                ]"
                @change="handleDryPureTypeChange"></Select>
            </Col>
            <Col span="10">
              <FormItemRest>
                <InputNumber
                  v-model:value="formModel[props.paramField]"
                  :stringMode="true"
                  :disabled="isParamDisabled() || props.viewMode"
                  :style="{ width: '100%' }"
                  :placeholder="t('参数')"
                  @blur="() => modalFormRef?.formRef?.validateFields([[props.typeField]])" />
              </FormItemRest>
            </Col>
          </Row>
        </InputGroup>
      </FormItem>
    </Col>
  </Row>
</template>

<script lang="tsx" setup>
  import { Col, FormItem, InputGroup, InputNumber, Row, Select, FormItemRest } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { computed, PropType, toRefs } from 'vue';

  const props = defineProps({
    modalFormRef: {
      type: Object as PropType<any>,
      required: true,
    },
    formModel: {
      type: Object as PropType<any>,
      required: true,
    },
    typeField: {
      type: String,
      required: true,
    },
    paramField: {
      type: String,
      required: true,
    },
    viewMode: {
      type: Boolean,
      default: false,
    },
    labelCol: {
      type: Object as PropType<any>,
      default: undefined,
    },
  });

  const { formModel, typeField, paramField, modalFormRef } = toRefs(props);

  const handleDryPureTypeChange = () => {
    formModel.value[paramField.value] = undefined;
    modalFormRef.value?.formRef?.validateFields([[typeField.value]]);
  };

  const isParamDisabled = () => {
    return formModel.value[typeField.value] !== 3; // 仅当类型为“折干折纯带参数”时启用
  };

  const dryPureRules: any = computed(() => [
    {
      required: true,
      message: t('请选择类型'),
    },
    {
      trigger: 'blur',
      validator: async (_rule: any) => {
        if (formModel.value[typeField.value] === undefined) return Promise.resolve();
        if (!formModel.value[paramField.value] && formModel.value[typeField.value] === 3) {
          return Promise.reject(t('请输入参数'));
        }
        if (formModel.value[typeField.value] === 3) {
          if (Number(formModel.value[paramField.value]) <= 0) {
            return Promise.reject(t('请输入正数'));
          }
          const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
          if (!reg.test(formModel.value[paramField.value])) {
            return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
          }
        }
        return Promise.resolve();
      },
    },
  ]);
</script>
