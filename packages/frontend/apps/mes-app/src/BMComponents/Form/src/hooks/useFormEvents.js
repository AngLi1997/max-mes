import {
  isArray,
  isFunction,
  isNullOrUnDef,
  isObject,
  isString,
} from '@/utils/is.js';

import { deepMerge } from '@/utils/object.js';
import { cloneDeep, uniqBy } from 'lodash-es';
import { toRaw, unref, watch } from 'vue';
import { handleInputNumberValue } from '../utils/helper';

export function useFormEvents(formActionContext) {
  const {
    emit,
    formPropsRef,
    formSchemasRef,
    formModel,
    cacheFormModel,
    getFormProps,
    bmFormRef,
    defaultFormValues,
    originComponentPropsFnMap,
    handleFormValues,
    errorItems,
  } = formActionContext;

  function getFormValues() {
    const formEl = unref(bmFormRef);
    if (!formEl)
      return {};
    return handleFormValues(toRaw(unref(formModel)));
  }

  /**
   * @description: 设置表单字段值
   * @param {Recordable} values
   * @return {*}
   */
  async function setFieldsValue(values) {
    const schemas = unref(formSchemasRef);
    const fields = schemas.map(item => item.field.split('.')[0]).filter(Boolean);

    Object.assign(cacheFormModel, values);
    const validKeys = [];
    Object.keys(values).forEach((key) => {
      const schema = schemas.find(item => item.field.split('.')[0] === key);
      let value = values[key];
      const hasKey = Reflect.has(values, key);
      if (isString(schema?.component)) {
        value = handleInputNumberValue(schema?.component, value);
      }
      // 0| '' is allow
      if (hasKey && fields.includes(key)) {
        formModel[key] = value;
        validKeys.push(key);
      }
    });
    // validateFields(validKeys);
  }
  /**
   * @description 重置表单
   * @param {Recordable} values
   */
  async function resetSchema(data) {
    let updateData = [];
    if (isObject(data)) {
      updateData.push(data);
    }
    if (isArray(data)) {
      updateData = [...data];
    }
    // @ts-ignore
    unref(formPropsRef).schemas = updateData;
  }

  /**
   * @description: 删除所有的 Schema
   * @param {string | string[]} fields 排除的字段
   */
  function removeAllSchema(fields) {
    const schemaList = cloneDeep(unref(formSchemasRef));
    let fieldList = fields ? (isString(fields) ? [fields] : fields) : [];
    if (isString(fields)) {
      fieldList = [fields];
    }
    formPropsRef.value.schemas = schemaList.filter(item => fieldList.includes(item.field));
  }

  /**
   * @description: 插入到指定 filed 后面，如果没传指定 field，则插入到最后,当 first = true 时插入到第一个位置
   * @param {FormSchema} schemaItem 插入的 schema
   * @param {string} prefixField 指定的 filed
   * @param {boolean} first 是否插入到第一个位置
   */
  async function appendSchemaByField(schemaItem, prefixField, first = false) {
    const schemaList = cloneDeep(unref(formSchemasRef));

    const index = schemaList.findIndex(schema => schema.field === prefixField);

    if (!prefixField || index === -1 || first) {
      first ? schemaList.unshift(schemaItem) : schemaList.push(schemaItem);
      formModel[schemaItem.field] = schemaItem.defaultValue;
      formPropsRef.value.schemas = schemaList;
      return;
    }
    if (index !== -1) {
      schemaList.splice(index + 1, 0, schemaItem);
    }
    formModel[schemaItem.field] = schemaItem.defaultValue;
    formPropsRef.value.schemas = schemaList;
  }
  /**
   * @description: 插入到指定 filed 后面，如果没传指定 field，则插入到最后,当 first = true 时插入到第一个位置
   * @param {FormSchema[]} schemaItem[] 插入的 schema 数组
   * @param {string} prefixField 指定的 filed
   * @param {boolean} first 是否插入到第一个位置
   */
  async function appendSchemasByField(schemaItem, prefixField, first = false) {
    const schemaList = cloneDeep(unref(formSchemasRef));

    const index = schemaList.findIndex(schema => schema.field === prefixField);

    if (!prefixField || index === -1 || first) {
      first ? schemaList.unshift(...schemaItem) : schemaList.push(...schemaItem);
      schemaItem.forEach(item => {
        if(!isNullOrUnDef(item.defaultValue)){
          formModel[item.field] = item.defaultValue;
        }
      });
      formPropsRef.value.schemas = schemaList;
      return;
    }
    if (index !== -1) {
      schemaList.splice(index + 1, 0, ...schemaItem);
    }
    schemaItem.forEach(item => {
      if(!isNullOrUnDef(item.defaultValue)){
        formModel[item.field] = item.defaultValue;
      }
    });
    formPropsRef.value.schemas = schemaList;
  }

  /**
   * @description: 根据 field 删除 Schema
   * @param {string | string[]} fields field
   * @return {*}
   */
  function removeSchemaByFiled(fields) {
    const schemaList = cloneDeep(unref(formSchemasRef));

    if (!fields) {
      return;
    }

    let fieldList = isString(fields) ? [fields] : fields;
    if (isString(fields)) {
      fieldList = [fields];
    }
    for (const field of fieldList) {
      if (isString(field)) {
        const index = schemaList.findIndex(schema => schema.field === field);
        if (index !== -1) {
          Reflect.deleteProperty(formModel, field);
          schemaList.splice(index, 1);
        }
      }
    }
    formPropsRef.value.schemas = schemaList;
  }

  /**
   * @description: 根据 field 查找 Schema
   * @param {string | string[]} fields field
   */
  function getSchemaByFiled(fields) {
    const schemaList = unref(formSchemasRef);
    const fieldList = ([]).concat(fields);
    return schemaList.find(schema => fieldList.includes(schema.field));
  }

  /**
   * @description  更新formItemSchema
   * @param {Partial<FormSchema> | Partial<FormSchema>[]} data
   * @return {*}
   */
  const updateSchema = (data) => {
    let updateData = [];
    if (isObject(data)) {
      updateData.push(data);
    }
    if (isArray(data)) {
      updateData = [...data];
    }
    const hasField = updateData.every(
      item =>
        item.component === 'Divider' || item.component === 'FormGroup' || (Reflect.has(item, 'field') && item.field),
    );

    if (!hasField) {
      console.error('All children of the form Schema array that need to be updated must contain the `field` field');
      return;
    }
    const schemas = [];
    updateData.forEach((item) => {
      unref(formSchemasRef).forEach((val) => {
        if (val.field === item.field) {
          const newSchema = deepMerge(val, item);
          if (originComponentPropsFnMap.has(val.field)) {
            const originCompPropsFn = originComponentPropsFnMap.get(val.field);
            const compProps = { ...newSchema.componentProps };
            newSchema.componentProps = (opt) => {
              const res = {
                ...originCompPropsFn(opt),
                ...compProps,
              };
              return res;
            };
          }
          schemas.push(newSchema);
        }
        else {
          schemas.push(val);
        }
      });
    });
    unref(formPropsRef).schemas = uniqBy(schemas, 'field');
  };

  /**
   * @description 重置表单
   */
  async function resetForm() {
    const { resetFunc } = unref(getFormProps);
    resetFunc && isFunction(resetFunc) && (await resetFunc());
    Object.keys(formModel).forEach((key) => {
      formModel[key] = isNullOrUnDef(defaultFormValues[key]) ? '' : defaultFormValues[key];
    });

    emit('reset', formModel);
    setTimeout(clearValidate);
  }

  /**
   * @description 校验表单
   * @param {string} nameList 校验指定的表单项
   * @return {*}
   */
  async function validateFields(nameList) {
    const { errors, valid } = await bmFormRef.value.validate(nameList);
    if (!valid) {
      errorItems.value = errors;
      return Promise.reject(errors);
    }
    return Promise.resolve(getFormValues());
  }

  /**
   * @description 校验整个表单
   * @param {string} nameList 校验指定的表单项
   * @return {*}
   */
  async function validate(nameList) {
    const { errors, valid } = await bmFormRef.value.validate(nameList);
    if (!valid) {
      errorItems.value = errors;
      return Promise.reject(errors);
    }
    return getFormValues();
  }

  /**
   * @description 清除校验
   */
  async function clearValidate() {
    errorItems.value = [];
    await bmFormRef.value?.reset();
  }

  /**
   * @description  提交表单
   * @param {Event} e 事件 Event
   * @return {*}
   */
  async function handleSubmit(e) {
    e && e.preventDefault();
    const { submitFunc } = unref(getFormProps);
    if (submitFunc && isFunction(submitFunc)) {
      await submitFunc();
      return;
    }
    const formEl = unref(bmFormRef);
    if (!formEl)
      return;
    try {
      const values = await validate();
      const res = handleFormValues(values);
      emit('submit', res);
      return res;
    }
    catch (error) {
      return Promise.reject(error);
    }
  }

  /**
   * @description 监听键盘事件
   * @param {KeyboardEvent} e 事件 KeyboardEvent
   */
  const handleEnterPress = (e) => {
    const { autoSubmitOnEnter } = unref(formPropsRef);
    if (!autoSubmitOnEnter)
      return;
    if (e.key === 'Enter' && e.target && e.target) {
      const target = e.target;
      if (target && target.tagName && target.tagName.toUpperCase() === 'INPUT') {
        handleSubmit(e);
      }
    }
  };

  /**
   * @description 监听 formModel 变化
   */
  watch(
    () => unref(formModel),
    () => {
      emit('formModelChange', toRaw(unref(formModel)));
      errorItems.value = [];
    },
    {
      deep: true,
    },
  );

  return {
    submit: handleSubmit,
    handleEnterPress,
    clearValidate,
    validate,
    validateFields,
    getFormValues,
    updateSchema,
    resetSchema,
    getSchemaByFiled,
    appendSchemaByField,
    appendSchemasByField,
    removeSchemaByFiled,
    removeAllSchema,
    resetForm,
    setFieldsValue,
  };
}
