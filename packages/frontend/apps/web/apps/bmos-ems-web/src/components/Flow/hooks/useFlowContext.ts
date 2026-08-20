import { inject, provide } from 'vue';
import type { BMFlowType } from './';

const key = Symbol('bm-flow');

export async function createFlowContext(instance: BMFlowType) {
  provide(key, instance);
}

export function useFlowContext(flowProps = {}) {
  return inject(key, flowProps) as BMFlowType;
}
