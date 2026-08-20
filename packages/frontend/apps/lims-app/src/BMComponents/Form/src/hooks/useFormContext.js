import { inject, provide } from 'vue';

const key = Symbol('bm-form');

export async function createFormContext(instance) {
  provide(key, instance);
}

export function useFormContext(formProps = {}) {
  return inject(key, formProps);
}
