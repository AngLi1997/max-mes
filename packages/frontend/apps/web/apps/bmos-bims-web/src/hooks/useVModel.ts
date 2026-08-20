import { EmitFn, Recordable } from '@bmos/components';
export function useVModel(props: any, propsName: string, emit: EmitFn) {
  return computed(() => {
    return {
      get() {
        return new Proxy(props[propsName], {
          get(target, key) {
            return Reflect.get(target, key);
          },
          set(target, key, value) {
            emit('update:' + propsName, {
              ...target,
              [key]: value,
            });
            return true;
          },
        });
      },
      set(val: Recordable) {
        emit('update:' + propsName, val);
      },
    };
  });
}
