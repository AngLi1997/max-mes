import type { FormProps, Recordable, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { computed, nextTick, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { PROCESS_STATE } from '../../enum';
import type { ConfigFormProps, FormConfig, FormEmitFn } from '../types';
import { useFormConfig } from './useFormConfig';

export type UseFormParams = {
  props: ConfigFormProps;
  emit: FormEmitFn;
  isCurView?: ComputedRef<boolean>;
};

export const useForm = (useFormContext: UseFormParams) => {
  const { props, emit } = useFormContext;
  const route = useRoute();

  // form 实例
  const formRef = ref<formInstance>();

  // form 是否有变化
  const hasChange = ref<boolean>(false);

  const setHasChange = (val: boolean) => {
    hasChange.value = val;
  };

  const { status } = route.query;

  const isView = computed(() => {
    return status === PROCESS_STATE.VIEW_VERSION;
  });

  const { configMap } = useFormConfig({
    props,
    isView,
    hasChange,
  });

  const getConfigRef = computed<FormConfig>(() => {
    hasChange.value = false;
    if (!props.activeComponentType) return {};
    if (!configMap.has(props.activeComponentType!)) return {};
    return configMap.get(props.activeComponentType) as FormConfig;
  });

  const formProps = computed<FormProps>(() => {
    return {
      layout: 'vertical',
      baseColProps: {
        span: 24,
      },
      showActionButtonGroup: false,
      showAdvancedButton: false,
      ...getConfigRef.value?.formProps,
    } as FormProps;
  });

  // 保存
  const submitForm = async () => {
    try {
      const formValues = await formRef.value?.submit();
      if (formValues.scopeMin || formValues.scopeMax) {
        delete formValues.scopeMin;
        delete formValues.scopeMax;
      }
      emit('submit', formValues as Recordable);
      hasChange.value = false;
      message.success(t('保存成功'));
      return Promise.resolve(formValues);
    } catch (error) {
      return Promise.reject(error);
    }
  };

  // 清空
  const clearForm = () => {
    formRef.value?.resetForm();
    hasChange.value = false;
    emit('submit', {} as Recordable);
    message.success(t('清空成功'));
  };

  // 取消
  const cancelForm = () => {
    emit('cancel');
  };

  // 监听 initFormValues 变化
  watch(
    () => props.initFormValue,
    async (val: Recordable) => {
      // 如果 val 不是空对象
      await nextTick();
      await formRef.value?.setFormModels(val);
      hasChange.value = false;
    },
    {
      immediate: true,
      deep: true,
    },
  );

  return {
    formRef,
    configMap,
    getConfigRef,
    formProps,
    hasChange,
    setHasChange,

    submitForm,
    cancelForm,
    clearForm,
  };
};
