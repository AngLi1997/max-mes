import { isNumber, isString } from '@/utils/is.js';
import { t } from '@/utils/useBmosI18n.js';

function getPlaceholder(prefix, labelResult) {
  const language = uni.getLocale();
  return language === 'zh-Hans' ? `${prefix}${labelResult}` : `${prefix} ${labelResult}`;
}

/**
 * @description: 生成placeholder
 */
export function createPlaceholderMessage(component, label) {
  const labelResult = isString(label) ? `${label}` : '';
  if (component.includes('Input') || component.includes('Complete')) {
    return getPlaceholder(t('请输入'), labelResult);
  }
  const chooseTypes = [
    'Select',
    'Cascader',
    'Checkbox',
    'CheckboxGroup',
    'Switch',
    'TreeSelect',
    'RadioGroup',
    'BMFormSelect',
    'BMFormDatePicker',
    'BMFormRadio',
    'BMFormCheckbox',
  ];
  if (component.includes('Picker') || chooseTypes.includes(component)) {
    return getPlaceholder(t('请选择'), labelResult);
  }
  if (component.includes('RangePicker') || component.includes('BMFormRangePicker')) {
    return [getPlaceholder(t('开始'), labelResult), getPlaceholder(t('结束'), labelResult)];
  }
  if (component.includes('Upload')) {
    return getPlaceholder(t('点击上传'), labelResult);
  }
  if (component.includes('Rate')) {
    return getPlaceholder(t('请评分'), labelResult);
  }
  if (component.includes('Slider')) {
    return getPlaceholder(t('请滑动'), labelResult);
  }
  if (component.includes('Transfer')) {
    return getPlaceholder(t('请选择'), labelResult);
  }
  if (component.includes('Mentions')) {
    return getPlaceholder(t('请输入'), labelResult);
  }
  return getPlaceholder(t('请输入'), '');
}

export function setComponentRuleType(rule, component) {
  if (['RangePicker', 'Upload', 'CheckboxGroup', 'TimePicker'].includes(component)) {
    rule.type = 'array';
  }
  else if (['InputNumber'].includes(component)) {
    rule.type = 'number';
  }
}

export function handleInputNumberValue(component, val) {
  if (!component)
    return val;
  if (['Input', 'InputPassword', 'InputSearch', 'InputTextArea'].includes(component)) {
    return val && isNumber(val) ? `${val}` : val;
  }
  return val;
}
