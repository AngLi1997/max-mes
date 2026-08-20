import { ComponentNode } from '@/components/Record/NodeList/type';
import type { FormSchema } from '@bmos/components';
import { computed, Ref } from 'vue';

import { useDateCalculation } from './formSchema/dateCalculation';
import { formatConfig } from './formSchema/formatConfiguration';
import { useNumericalDetermination } from './formSchema/numericalDetermination';
import { useStringConcat } from './formSchema/stringConcat';

export type UseFormParams = {
  component: Ref<ComponentNode | undefined>;
  isShow: boolean;
  changeStatus: Function;
};
export const useFormSchema = (useFormContext: UseFormParams) => {
  const { numericalDeterminationSchemas } = useNumericalDetermination(useFormContext);
  const { stringConcatSchemas } = useStringConcat(useFormContext);
  const { dateCalculationSchemas } = useDateCalculation(useFormContext);
  const { formatConfigSchema } = formatConfig(useFormContext);
  const formSchema: Ref<FormSchema[]> = computed(() => {
    return [
      ...numericalDeterminationSchemas.value,
      ...dateCalculationSchemas.value,
      ...formatConfigSchema.value,
      ...stringConcatSchemas.value,
    ];
  });
  return {
    formSchema,
  };
};
