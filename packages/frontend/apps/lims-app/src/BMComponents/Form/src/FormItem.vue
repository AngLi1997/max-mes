<template>
  <wd-col
    v-if="getShow.isIfShow" v-show="getShow.isShow" v-bind="getColConfig(schema.colProps)" class="from-col"
    :class="[hasErrorMessage ? 'has-error-message' : '']"
  >
    <template v-if="schema.component === 'Divider'">
      <div class="bm-form-divider-container">
        <div v-if="getComponentProps.showLeftBorder" class="bm-form-divider-border" />
        <wd-divider v-bind="getComponentProps" class="bm-form-divider">
          <component :is="renderLabelHelpMessage" />
        </wd-divider>
      </div>
    </template>
    <template v-else-if="schema.component === 'FormGroup'">
      <component :is="formGroupLabel" />
    </template>
    <template v-else-if="schema.component === 'FormTitle'">
      <component :is="formTitle" />
    </template>
    <template v-else>
      <slot v-if="schema.slot" :name="schema.slot" v-bind="getValues" />
      <template v-else>
        <wd-cell
          v-bind="schema.cellProps"
          :prop="schema.field"
          :rules="getRules"
          :title-width="itemLabelWidthProp.width"
          vertical
          custom-class="bm-form-cell"
        >
          <template #title>
            <component :is="getLabel" />
          </template>
          <template v-for="(slotFn, slotName) in getCellSlots" #[slotName]="slotData" :key="slotName">
            <component :is="slotFn?.({ ...getValues, slotData }) ?? slotFn" :key="slotName" />
          </template>
          <component
            :is="getComponent" v-if="!componentIsString" :ref="setItemRef(schema.field)"
            v-bind="getComponentProps" v-model="modelValue" :disabled="getDisable" v-on="componentEvents"
          />
          <component
            :is="getComponent" v-else-if="getComponent" :ref="setItemRef(schema.field)"
            v-bind="getComponentProps" v-model="modelValue" :disabled="getDisable" v-on="componentEvents"
          >
            <template v-for="(slotFn, slotName) in getComponentSlots" #[slotName]="slotData" :key="slotName">
              <component :is="slotFn?.({ ...getValues, slotData }) ?? slotFn" :key="slotName" />
            </template>
          </component>
        </wd-cell>
      </template>
    </template>
  </wd-col>
</template>

<script setup lang="jsx">
import {
  isBoolean,
  isFunction,
  isNull,
  isObject,
  isString,
} from '@/utils/is.js';
import { t } from '@/utils/useBmosI18n.js';
import { cloneDeep, debounce, merge } from 'lodash-es';
import { computed, isVNode, nextTick, onMounted, toRefs, unref, watch } from 'vue';
import FormTitle from './components/FormTitle.vue';
import { useFormContext } from './hooks/index.js';
import { useItemLabelWidth } from './hooks/useLabelWidth.js';
import { componentMap } from './utils/componentMap.js';
import { createPlaceholderMessage } from './utils/helper.js';

const props = defineProps({
  formModel: {
    type: Object,
    default: () => ({}),
  },
  schema: {
    type: Object,
    default: () => ({}),
  },
});
const emit = defineEmits(['updateFormModel']);

// bmForm组件实例
const formContext = useFormContext();
const { formPropsRef, setItemRef, getSchemaByFiled, updateSchema, appendSchemaByField, errorItems } = formContext;

const { schema } = toRefs(props);

const itemLabelWidthProp = useItemLabelWidth(schema, formPropsRef);

// 获取 col 的配置
const getColConfig = (colProps) => {
  const { baseColProps } = unref(formPropsRef);
  const config = merge({ ...baseColProps }, { ...colProps });
  return config;
};

const modelValue = computed({
  get() {
    return props.formModel[schema.value.field];
  },
  set(val) {
    emit('updateFormModel', {
      ...props.formModel,
      [schema.value.field]: val,
    });
  },
});

const getValues = computed(() => {
  const { formModel, schema } = props;

  const { mergeDynamicData } = unref(formPropsRef);
  return {
    field: schema.field,
    formInstance: formContext,
    formModel,
    values: {
      ...mergeDynamicData,
      ...formModel,
    },
    schema,
  };
});

const getShow = computed(() => {
  const { vShow, vIf, isAdvanced = false } = unref(schema);
  const { showAdvancedButton } = unref(formPropsRef);
  const itemIsAdvanced = showAdvancedButton ? (isBoolean(isAdvanced) ? isAdvanced : true) : true;

  let isShow = true;
  let isIfShow = true;

  if (isBoolean(vShow)) {
    isShow = vShow;
  }
  if (isBoolean(vIf)) {
    isIfShow = vIf;
  }
  if (isFunction(vShow)) {
    isShow = vShow(unref(getValues));
  }
  if (isFunction(vIf)) {
    isIfShow = vIf(unref(getValues));
  }
  isShow = isShow && itemIsAdvanced;

  return { isShow, isIfShow };
});

const vNodeFactory = (
  component,
  values = unref(getValues),
) => {
  if (isString(component)) {
    return <>{component}</>;
  }
  else if (isVNode(component)) {
    return component;
  }
  else if (isFunction(component)) {
    return vNodeFactory((component)(values));
  }
  else if (isObject(component)) {
    const compKeys = Object.keys(component);
    // 如果是组件对象直接return
    if (compKeys.some(n => n.startsWith('_') || ['setup', 'render'].includes(n))) {
      return component;
    }
    return compKeys.reduce((slots, slotName) => {
      slots[slotName] = (...rest) => vNodeFactory(component[slotName], rest);
      return slots;
    }, {});
  }
  return component;
};

/**
 * @description 当前表单项组件
 */
const getComponent = computed(() => {
  const component = props.schema.component;
  return isString(component)
    ? componentMap[component] ? componentMap[component] : <>{vNodeFactory(component)}</>
    : <>{vNodeFactory(component)}</>;
});
/**
 * @description 表单组件props
 */
const getComponentProps = computed(() => {
  const { schema } = props;
  let { componentProps = {}, component, label } = schema;
  if (isFunction(componentProps)) {
    componentProps = componentProps(unref(getValues)) ?? {};
  }
  if (isString(component) && !Reflect.has(componentProps, 'placeholder')) {
    componentProps.placeholder = createPlaceholderMessage(component, label);
  }
  if (isVNode(getComponent.value)) {
    Object.assign(componentProps, getComponent.value.props);
  }
  return componentProps;
});
const getDisable = computed(() => {
  const { disabled: globDisabled } = unref(formPropsRef);
  const { dynamicDisabled } = props.schema;
  const { disabled: itemDisabled = false } = unref(getComponentProps);
  let disabled = !!globDisabled || itemDisabled;
  if (isBoolean(dynamicDisabled)) {
    disabled = dynamicDisabled;
  }
  if (isFunction(dynamicDisabled)) {
    disabled = dynamicDisabled(unref(getValues));
  }
  return disabled;
});

/**
 * @description 当前表单项组件是否为 string
 */
const componentIsString = computed(() => {
  const component = props.schema.component;
  return isString(component);
});

/**
 * @description 当前表单项组件的插槽
 */
const getComponentSlots = computed(() => {
  const componentSlots = props.schema.componentSlots ?? {};
  return isString(componentSlots) || isVNode(componentSlots)
    ? {
        default: (...rest) => vNodeFactory(componentSlots, rest),
      }
    : vNodeFactory(componentSlots);
});

/**
 * @description 当前表单项cell组件的插槽
 */
const getCellSlots = computed(() => {
  const componentSlots = props.schema.cellSlots ?? {};
  return isString(componentSlots) || isVNode(componentSlots)
    ? {
        default: (...rest) => vNodeFactory(componentSlots, rest),
      }
    : vNodeFactory(componentSlots);
});

const getLabel = computed(() => {
  const label = props.schema.label;
  if (props.schema.noLabel) {
    return <span style="visibility: hidden">1</span>;
  }
  return isFunction(label) ? <>{label(unref(getValues))}</> : <>{label}</>;
});

/**
 * @description 表单组件事件
 */
const componentEvents = computed(() => {
  const componentProps = props.schema?.componentProps || {};
  return Object.keys(componentProps).reduce((prev, key) => {
    // eslint-disable-next-line regexp/no-unused-capturing-group
    if (/on([A-Z])/.test(key)) {
      // eg: onChange => change
      const eventKey = key.replace(/on([A-Z])/, '$1').toLocaleLowerCase();
      prev[eventKey] = componentProps[key];
    }
    return prev;
  }, {});
});

const renderLabelHelpMessage = computed(() => {
  return (
    <span>
      {props.schema.label}
    </span>
  );
});

const formGroupLabel = computed(() => {
  const renderLabel = vNodeFactory(getLabel.value);
  return (
    <div class="form-group-label" style={getComponentProps.value.style}>
      {renderLabel}
    </div>
  );
});

const formTitle = computed(() => {
  const renderLabel = vNodeFactory(getLabel.value);
  return (
    <FormTitle style={getComponentProps.value.style} color={getComponentProps.value.color}>
      {renderLabel}
    </FormTitle>
  );
});

const getRules = computed(() => {
  const { rules: defRules = [], component, rulesMessageJoinLabel, dynamicRules, required, label, noUseMaxLengthRule } = props.schema;

  if (isFunction(dynamicRules)) {
    const newRules = dynamicRules(unref(getValues));
    if (component === 'Input' && !noUseMaxLengthRule) {
      newRules.push({
        validator: (value) => {
          if (value && value.toString().length > 100) {
            return Promise.reject(t('输入内容过长, 不能超过100字符'));
          }
          return Promise.resolve();
        },
      });
    }
    if (component === 'Textarea' && !noUseMaxLengthRule) {
      newRules.push({
        validator: (value) => {
          if (value && value.toString().length > 200) {
            return Promise.reject(t('输入内容过长, 不能超过200字符'));
          }
          return Promise.resolve();
        },
      });
    }
    return newRules;
  }

  let rules = cloneDeep(defRules);
  const { rulesMessageJoinLabel: globalRulesMessageJoinLabel } = unref(formPropsRef);

  const joinLabel = Reflect.has(unref(props.schema), 'rulesMessageJoinLabel')
    ? rulesMessageJoinLabel
    : globalRulesMessageJoinLabel;
  const defaultMsg = isString(component)
    ? `${createPlaceholderMessage(component, getLabel.value)}${joinLabel ? (isFunction(label) ? '' : label) : ''}`
    : undefined;

  function validator(value, rule) {
    const msg = rule.message || defaultMsg;

    if (value === undefined || isNull(value)) {
      // 空值
      return Promise.reject(msg);
    }
    else if (Array.isArray(value) && value.length === 0) {
      // 数组类型
      return Promise.reject(msg);
    }
    else if (typeof value === 'string' && value.trim() === '') {
      // 空字符串
      return Promise.reject(msg);
    }
    else if (
      typeof value === 'object'
      && Reflect.has(value, 'checked')
      && Reflect.has(value, 'halfChecked')
      && Array.isArray(value.checked)
      && Array.isArray(value.halfChecked)
      && value.checked.length === 0
      && value.halfChecked.length === 0
    ) {
      // 非关联选择的tree组件
      return Promise.reject(msg);
    }
    return Promise.resolve();
  }

  const getRequired = isFunction(required) ? required(unref(getValues)) : required;

  if ((!rules || rules.length === 0) && getRequired) {
    rules = [{ required: getRequired, validator, message: defaultMsg }];
  }
  if (component === 'Input' && !noUseMaxLengthRule) {
    rules = [
      ...rules,
      {
        validator: (value) => {
          if (value && value.toString().length > 100) {
            return Promise.reject(t('输入内容过长, 不能超过100字符'));
          }
          return Promise.resolve();
        },
      },
    ];
  }

  if (component === 'Textarea' && !noUseMaxLengthRule) {
    rules = [
      ...rules,
      {
        validator: (value) => {
          if (value && value.toString().length > 200) {
            return Promise.reject(t('输入内容过长, 不能超过200字符'));
          }
          return Promise.resolve();
        },
      },
    ];
  }

  const requiredRuleIndex = rules.findIndex(
    rule => Reflect.has(rule, 'required') && !Reflect.has(rule, 'validator'),
  );

  if (requiredRuleIndex !== -1) {
    const rule = rules[requiredRuleIndex];

    if (component && isString(component)) {
      if (!Reflect.has(rule, 'type')) {
        rule.type = component === 'Input-number' ? 'number' : 'string';
      }

      rule.message = rule.message || defaultMsg;

      if (component.includes('Input') || component.includes('Textarea')) {
        rule.whitespace = true;
      }
    }
  }

  // Maximum input length rule check
  const characterInx = rules.findIndex(val => val.max);
  if (characterInx !== -1 && !rules[characterInx].validator) {
    rules[characterInx].message
      = rules[characterInx].message || `${t('最大长度为')}${[rules[characterInx].max]}`;
  }
  return rules;
});

const fetchRemoteData = async (request) => {
  if (request) {
    const { component } = unref(schema);
    try {
      const newSchema = {
        ...unref(schema),
        loading: true,
        componentProps: {
          ...unref(getComponentProps),
          options: [],
        },
      };
      updateSchema(newSchema);
      const result = await request(unref(getValues));
      if (['Select', 'RadioGroup', 'CheckBoxGroup'].includes(component)) {
        newSchema.componentProps.options = result;
      }
      else if (['TreeSelect', 'Tree', 'BMFormSelect'].includes(component)) {
        if (newSchema.componentProps.type === 'tree') {
          newSchema.componentProps['tree-data'] = result;
        }
        else {
          newSchema.componentProps.options = result;
        }
      }
      if (newSchema.componentProps) {
        newSchema.componentProps.requestResult = result;
      }
      newSchema.loading = false;
      updateSchema(newSchema);
    }
    finally {
      nextTick(() => {
        schema.value.loading = false;
      });
    }
  }
};

const initRequestConfig = () => {
  const request = getComponentProps.value.request;
  if (request) {
    if (isFunction(request)) {
      fetchRemoteData(request);
    }
    else {
      const { watchFields = [], options = {}, wait = 0, callback } = request;
      const params = watchFields.map(field => () => props.formModel[field]);
      watch(
        params,
        debounce(() => {
          fetchRemoteData(callback);
        }, wait),
        {
          ...options,
        },
      );
    }
  }
};

const hasErrorMessage = computed(() => {
  return errorItems.value.findIndex(item => item.prop === schema.value.field) !== -1;
});
onMounted(() => {
  if (!getSchemaByFiled(props.schema.field)) {
    appendSchemaByField(props.schema);
  }
  initRequestConfig();
});
</script>

<style lang="scss" scoped>
:deep(.bm-form-cell) {
  padding-left: 7.03rpx;

  .wd-cell__wrapper.is-vertical {
    padding-bottom: 0;
    padding-top: 0;
    padding-right: 7.03rpx;

    .wd-cell__title {
      font-size: 11.72rpx;
    }

    .wd-cell__left {
      margin-bottom: 5.86rpx;
    }

    .wd-cell__right {
      margin-top: 0;
      margin-bottom: 11.72rpx;
      position: relative;

      .wd-cell__error-message {
        line-height: 11.72rpx;
        height: 11.72rpx;
        position: absolute;
        bottom: -11.72rpx;
        font-size: 9.38rpx;
      }
    }
  }
}
</style>
