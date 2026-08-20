import { PropType } from 'vue';

export const ContentEmits = ['icon-click'];

export const ContentProps = {
  count: {
    type: Number as PropType<number>,
    default: 0,
  },
  title: {
    type: String as PropType<string>,
    default: 'Content',
  },
  icon: {
    type: Boolean as PropType<boolean>,
    require: false,
    default: true,
  },
  isView: {
    type: Boolean as PropType<boolean>,
    require: false,
    default: false,
  },
};
