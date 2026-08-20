import type { Component } from 'vue';
/**
 * Component list, register here to setting it in the form
 */
import {
  AutoComplete,
  Cascader,
  Checkbox,
  CheckboxGroup,
  DatePicker,
  Divider,
  Input,
  InputGroup,
  InputNumber,
  InputSearch,
  Radio,
  RadioGroup,
  Rate,
  Select,
  Slider,
  Switch,
  Textarea,
  TimePicker,
  Tree,
  TreeSelect,
  Upload,
} from 'ant-design-vue';

import { defineComponent } from 'vue';
import { BMPasswordInput } from '../../../PasswordInput';

const Span = defineComponent({
  name: 'Span',
  props: {
    value: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    return () => <div style={{ minHeight: '36px', lineHeight: '30px' }}>{props.value}</div>;
  },
});

const componentMap: Record<string, Component> = {
  Input,
  InputGroup,
  InputPassword: BMPasswordInput,
  InputSearch,
  InputTextArea: Textarea,
  InputNumber,
  AutoComplete,
  Select,
  TreeSelect,
  Tree,
  Switch,
  Radio,
  RadioGroup,
  Checkbox,
  CheckboxGroup,
  Cascader,
  Slider,
  Rate,
  DatePicker,
  MonthPicker: DatePicker.MonthPicker,
  RangePicker: DatePicker.RangePicker,
  WeekPicker: DatePicker.WeekPicker,
  TimePicker,
  Upload,
  Span,
  Divider,
};

export type ComponentMapType = keyof typeof componentMap | 'FormGroup' | 'TableTitle';

export { componentMap };
