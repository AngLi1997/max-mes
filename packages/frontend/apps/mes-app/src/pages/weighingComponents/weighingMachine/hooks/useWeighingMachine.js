import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { storeToRefs } from 'pinia';
import { computed, reactive, ref } from 'vue';

export const useMaterialWeighing = ({ props, value }) => {
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);

  const { math } = useMathJs();

  const formRef = ref();
  // 手动称量表单
  const formProps = reactive({
    schemas: [
      {
        field: 'tareWeight',
        component: 'Input',
        label: t('皮重'),
        colProps: {
          span: 8,
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            placeholder: t('请输入'),
            type: 'number',
            onInput: () => {
              let netWeight = '';
              if (formModel.tareWeight && formModel.grossWeight) {
                netWeight = math.format(
                  math.subtract(math.bignumber(formModel.grossWeight), math.bignumber(formModel.tareWeight)),
                );
              }
              formInstance.setFormModel('netWeight', netWeight);
              value.value.netWeight = netWeight;
              value.value.tareWeight = formModel.tareWeight;
              value.value.grossWeight = formModel.grossWeight;
            },
          };
        },
      },
      {
        field: 'grossWeight',
        component: 'Input',
        label: t('毛重'),
        colProps: {
          span: 8,
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            placeholder: t('请输入'),
            type: 'number',
            onInput: () => {
              let netWeight = '';
              if (formModel.tareWeight && formModel.grossWeight) {
                netWeight = math.format(
                  math.subtract(math.bignumber(formModel.grossWeight), math.bignumber(formModel.tareWeight)),
                );
              }
              formInstance.setFormModel('netWeight', netWeight);
              value.value.netWeight = netWeight;
              value.value.tareWeight = formModel.tareWeight;
              value.value.grossWeight = formModel.grossWeight;
            },
          };
        },
      },
      {
        field: 'netWeight',
        component: 'Input',
        label: t('净重'),
        colProps: {
          span: 8,
        },
        componentProps: {
          disabled: true,
          placeholder: t('计算回显'),
        },
      },
    ],
  });

  // 手动称量配液量取表单
  const formProps2 = reactive({
    schemas: [
      {
        field: 'netWeight',
        component: 'Input',
        label: t('物料量'),
        colProps: {
          span: 20,
        },
        componentProps: ({ formModel, formInstance }) => {
          return {
            placeholder: t('请输入'),
            type: 'number',
            onInput: () => {
              value.value.netWeight = formModel.netWeight;
            },
          };
        },
      },
      {
        field: 'unit',
        component: 'Input',
        label: t('单位'),
        colProps: {
          span: 4,
        },
        componentProps: {
          disabled: true,
        },
      },
    ],
  });
  // 称量范围
  const range = computed(() => {
    return `${selectedBalance.value?.minRange}~${
      selectedBalance.value?.maxRange
    }${selectedBalance.value?.unit}`;
  });
  // 分度值
  const precision = computed(() => {
    return `${selectedBalance.value?.precision}${selectedBalance.value?.unit}`;
  });
  // 允差范围
  const toleranceRange = computed(() => {
    if (props.diff.toleranceTypeEnum) {
      const min = props.diff.minTolerance || 0;
      const max = props.diff.maxTolerance || 0;
      if (props.diff.toleranceTypeEnum.value === 0) {
        return `-${min}%~${max}%`;
      }
      return `-${min}${props.unit}~${max}${props.unit}`;
    }
    return '';
  });

  // 目标范围
  const targetRange = computed(() => {
    if (props.diff.toleranceDiff) {
      const min = props.diff.toleranceDiff[0] || props.diff.toleranceDiff[1];
      const max = props.diff.toleranceDiff[2] || props.diff.toleranceDiff[1];
      return `${min}${props.unit}~${max}${props.unit}`;
    }
    return '';
  });

  const smallLineMax = computed(() => {
    const { toleranceDiff = [] } = props.diff;
    const max = toleranceDiff[2] || toleranceDiff[1] || 0;
    return math
      .chain(max)
      .multiply(1200)
      .divide(970)
      .done();
  });

  // 小进度条数值
  const smallProgressValue = computed(() => {
    const value = math
      .chain(props.weight)
      .divide(smallLineMax.value)
      .multiply(100)
      .done();
    if (value <= 0) {
      return 0.01;
    }
    if (value > 100) {
      return 100;
    }
    return value;
  });
  // 小进度条放大图标宽度
  const smallProgressWidth = computed(() => {
    const { toleranceDiff = [] } = props.diff;
    const max = toleranceDiff[2] || toleranceDiff[1] || 0;
    const min = toleranceDiff[0] || toleranceDiff[1] || 0;
    if (min === max) {
      return '0%';
    }
    return `${math
      .chain(max)
      .subtract(min)
      .divide(smallLineMax.value)
      .multiply(100)
      .done()}%`;
  });

  // 放大进度条数值
  const bigProgressValue = computed(() => {
    const { toleranceDiff = [] } = props.diff;
    const max = toleranceDiff[2] || toleranceDiff[1] || 0;
    const min = toleranceDiff[0] || toleranceDiff[1] || 0;
    if (min === max) {
      return (math.equal(math.bignumber(props.weight), math.bignumber(min)) || math.largerEq(props.weight, max)) ? 100 : 0.01;
    }
    if (math.largerEq(props.weight, max)) {
      return 100;
    }
    if (math.smallerEq(props.weight, min)) {
      return 0.01;
    }
    return math
      .chain(props.weight)
      .subtract(min)
      .divide(max - min)
      .multiply(100)
      .done();
  });
  // 进度条颜色
  const progressColor = computed(() => {
    const { toleranceDiff = [] } = props.diff;
    const max = toleranceDiff[2] || toleranceDiff[1] || 0;
    const min = toleranceDiff[0] || toleranceDiff[1] || 0;
    if (math.larger(props.weight, max)) {
      return '#FF4C26';
    }
    if (math.smaller(props.weight, min)) {
      return '#FF9933';
    }
    return '#59BF78';
  });
  return {
    range,
    precision,
    toleranceRange,
    smallLineMax,
    smallProgressValue,
    smallProgressWidth,
    bigProgressValue,
    progressColor,
    targetRange,
    formProps,
    formProps2,
    formRef,
  };
};
