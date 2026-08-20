import {
  t,
} from '@/utils/useBmosI18n';

export const tableColumnProps = {
  prop: {
    type: String,
    default: '',
  },
  label: {
    type: String,
    default: '',
  },
  width: {
    type: [Number, String],
    default: 100,
  },
  thProps: {
    type: Object,
    default: () => ({}),
  },
  align: {
    type: 'left' | 'center' | 'right',
    default: 'left',
  },
  fixed: {
    type: String,
    default: '',
  },
};

export const tableProps = {
  rowKey: {
    type: String,
    default: 'id',
  },
  data: {
    type: [Array, undefined],
    default: undefined,
  },
  border: {
    type: Boolean,
    default: false,
  },
  stripe: {
    type: Boolean,
    default: false,
  },
  height: {
    type: String,
    default: '100%',
  },
  type: {
    type: String,
    default: '',
  },
  tableColProps: {
    type: Array,
    default: () => [],
  },
  pagination: {
    type: [Boolean, Object],
    default: true,
  },
  dataRequest: {
    type: Function,
    default: null,
  },
  selectionProps: {
    type: Function,
    default: () => () => ({}),
  },
  noDataType: {
    type: String,
    default: '',
  },
  noDataText: {
    type: String,
    default: '',
  },
  showNoData: {
    type: Boolean,
    default: false,
  },
  extraParams: {
    type: Object,
    default: () => ({}),
  },
  showBorder: {
    type: Boolean,
    default: false,
  },
  trProps: {
    type: Function,
    default: () => () => ({}),
  },
  showAllCheck: {
    type: Boolean,
    default: false,
  },
  noDataShowTable: {
    type: Boolean,
    default: true,
  },
  paginationMenu: {
    type: Array,
    default: () => {
      return [
        { value: '20', content: `20${t('条/页')}` },
        { value: '50', content: `50${t('条/页')}` },
        { value: '100', content: `100${t('条/页')}` },
      ];
    },
  },
  intervalRequest: {
    type: Boolean,
    default: false,
  },
};
