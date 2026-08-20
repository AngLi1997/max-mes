/**
 * Component list, register here to setting it in the form
 */
import Calendar from 'wot-design-uni/components/wd-calendar/wd-calendar.vue';
import Checkbox from 'wot-design-uni/components/wd-checkbox/wd-checkbox.vue';
import ColPicker from 'wot-design-uni/components/wd-col-picker/wd-col-picker.vue';
import DatetimePicker from 'wot-design-uni/components/wd-datetime-picker/wd-datetime-picker.vue';
import InputNumber from 'wot-design-uni/components/wd-input-number/wd-input-number.vue';
import Input from 'wot-design-uni/components/wd-input/wd-input.vue';
import PasswordInput from 'wot-design-uni/components/wd-password-input/wd-password-input.vue';
import Picker from 'wot-design-uni/components/wd-picker/wd-picker.vue';
import Radio from 'wot-design-uni/components/wd-radio/wd-radio.vue';
import Rate from 'wot-design-uni/components/wd-rate/wd-rate.vue';
import Search from 'wot-design-uni/components/wd-search/wd-search.vue';
import SelectPicker from 'wot-design-uni/components/wd-select-picker/wd-select-picker.vue';
import Slider from 'wot-design-uni/components/wd-slider/wd-slider.vue';
import Switch from 'wot-design-uni/components/wd-switch/wd-switch.vue';
import Textarea from 'wot-design-uni/components/wd-textarea/wd-textarea.vue';
import Upload from 'wot-design-uni/components/wd-upload/wd-upload.vue';

// 自定义组件
import BMFormCheckbox from '../components/FormCheckbox.vue';
import BMFormDatePicker from '../components/FormDatePicker/index.vue';
import BMFormRadio from '../components/FormRadio.vue';
import BMFormRangePicker from '../components/FormRangePicker/index.vue';
import BMFormSelect from '../components/FormSelect/index.vue';

const componentMap = {
  Calendar,
  Checkbox,
  ColPicker,
  DatetimePicker,
  Input,
  Textarea,
  InputNumber,
  Picker,
  Radio,
  Rate,
  Search,
  SelectPicker,
  Slider,
  Switch,
  Upload,
  PasswordInput,
  BMFormRadio,
  BMFormCheckbox,
  BMFormSelect,
  BMFormDatePicker,
  BMFormRangePicker,
};

export { componentMap };
