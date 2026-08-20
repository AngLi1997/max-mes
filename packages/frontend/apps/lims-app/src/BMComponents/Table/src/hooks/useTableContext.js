import { inject, provide } from 'vue';

const key = Symbol('bm-table');

export async function createTableContext(instance) {
  provide(key, instance);
}

export function useTableContext() {
  return inject(key);
}
