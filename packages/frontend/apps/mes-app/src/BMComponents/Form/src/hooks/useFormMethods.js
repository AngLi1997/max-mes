import {
  isArray,
  isFunction,
  isNullOrUnDef,
  isObject,
} from '@/utils/is.js';
import { cloneDeep, merge, omit, set } from 'lodash-es';
import { unref } from 'vue';

export const useFormMethods = (formMethodsContext) => {
  const { compRefMap, formModel, formPropsRef, cacheFormModel, defaultFormValues, getFormProps } = formMethodsContext;

  /**
   * @description 设置表单组件实例
   */
  const setItemRef = (field) => {
    return (el) => {
      if (el) {
        compRefMap.set(field, el);
      }
    };
  };

  /**
   * @description 设置某个字段的值
   * @param key 字段
   * @param value 值
   */
  const setFormModel = (key, value) => {
    formModel[key] = value;
    cacheFormModel[key] = value;
  };

  /**
   * @description 设置formModel的值
   * @param values 表单数据
   */
  const setFormModels = (values) => {
    for (const [key, value] of Object.entries(values)) {
      setFormModel(key, value);
    }
  };

  /**
   * @description 获取某个字段的值
   * @param key 字段
   */
  const getFormModelByField = (keys) => {
    if (isArray(keys)) {
      const values = {};
      keys.forEach((key) => {
        values[key] = formModel[key];
      });
      return values;
    }
    else {
      return formModel[keys];
    }
  };

  /**
   * @description 删除某个字段
   * @param key 字段
   * @returns boolean
   */
  const delFormModel = (key) => {
    return Reflect.deleteProperty(formModel, key);
  };

  /**
   * @description 设置表单配置
   * @param formProps 表单配置
   */
  const setFormProps = (formProps) => {
    if (unref(formPropsRef).schemas) {
      formPropsRef.value = merge(unref(formPropsRef) || {}, omit(cloneDeep(formProps), ['schemas']));
    }
    else {
      formPropsRef.value = merge(unref(formPropsRef) || {}, formProps);
    }
  };

  /**
   * @description 处理表单数据
   * @param values 表单数据
   */
  function handleFormValues(values) {
    if (!isObject(values)) {
      return {};
    }
    const res = {};
    for (const item of Object.entries(values)) {
      let [, value] = item;
      const [key] = item;
      if (!key || (isArray(value) && value.length === 0) || isFunction(value) || isNullOrUnDef(value)) {
        continue;
      }
      const transformDateFunc = unref(getFormProps).transformDateFunc;
      if (transformDateFunc && isObject(value)) {
        value = transformDateFunc(value);
      }
      if (transformDateFunc && isArray(value) && value[0]?.format && value[1]?.format) {
        value = value.map(item => transformDateFunc(item));
      }
      const transformValueFunc = unref(getFormProps).transformValueFunc;
      if (!isNullOrUnDef(transformValueFunc) && isFunction(transformValueFunc)) {
        value = transformValueFunc(key, value, res);
      }
      // Remove spaces
      //   if (isString(value)) {
      //     value = value.trim();
      //   }
      set(res, key, value);
    }
    return res;
  }

  /**
   * @description 初始化表单数据
   */
  const initFormValues = (initialValues) => {
    try {
      unref(formPropsRef).schemas?.forEach((item) => {
        const { defaultValue, field } = item;
        formModel[field] = isNullOrUnDef(defaultValue) ? null : defaultValue;
        cacheFormModel[field] = isNullOrUnDef(defaultValue) ? null : defaultValue;
        defaultFormValues[field] = isNullOrUnDef(defaultValue) ? null : defaultValue;
      });
      if (initialValues && isObject(initialValues)) {
        Object.keys(initialValues).forEach((key) => {
          defaultFormValues[key] = !isNullOrUnDef(initialValues[key]) ? initialValues[key] : null;
          cacheFormModel[key] = !isNullOrUnDef(initialValues[key]) ? initialValues[key] : null;
        });
      }
    }
    catch (_error) {
      //
    }
  };

  return {
    setItemRef,
    initFormValues,
    setFormModel,
    setFormModels,
    delFormModel,
    setFormProps,
    handleFormValues,
    getFormModelByField,
  };
};
