import { isFunction } from '@/utils/is.js';
import { cloneDeep } from 'lodash-es';
import { computed, reactive, ref, unref, watch, watchEffect } from 'vue';

export const useFormState = ({ props, attrs }) => {
  // 将formSchema克隆一份，避免修改原有的formSchema
  // const cloneProps = cloneDeep(props);
  const formPropsRef = ref(cloneDeep(props));
  watch(
    () => props,
    () => {
      formPropsRef.value = cloneDeep(props);
    },
    {
      immediate: true,
    },
  );
  /** 表单项数据 */
  const formModel = reactive({ ...props.initialValues });
  // 表单默认数据
  const defaultFormValues = reactive({ ...props.initialValues });
  // 缓存的表单值，用于恢复form-item v-if为true后的值
  const cacheFormModel = { ...props.initialValues };
  // 表单实例
  const bmFormRef = ref();
  // 将所有的表单组件实例保存起来
  const compRefMap = new Map();
  // 初始时的componentProps，用于updateSchema更新时不覆盖componentProps为函数时的值
  const originComponentPropsFnMap = new Map();

  // 获取表单所有属性
  const getFormProps = computed(() => {
    return {
      ...attrs,
      ...formPropsRef.value,
    };
  });

  // 表单错误项
  const errorItems = ref([]);

  // 获取栅栏Row配置
  const getRowConfig = computed(() => {
    const { baseRowStyle = {}, rowProps } = unref(getFormProps);
    return {
      style: baseRowStyle,
      // gutter: 24,
      ...rowProps,
    };
  });

  watchEffect(() => {
    formPropsRef.value.schemas?.forEach((item) => {
      if (isFunction(item.componentProps)) {
        originComponentPropsFnMap.set(item.field, item.componentProps);
      }
    });
  });

  const needPadding = ref(true);

  const formSchemasRef = computed(() => {
    needPadding.value = !!(formPropsRef.value.schemas || []).find(item => item.component === 'FormTitle');
    return formPropsRef.value.schemas || [];
  });

  return {
    formModel,
    defaultFormValues,
    bmFormRef,
    formPropsRef,
    cacheFormModel,
    compRefMap,
    getFormProps,
    getRowConfig,
    originComponentPropsFnMap,
    formSchemasRef,
    needPadding,
    errorItems,
  };
};
