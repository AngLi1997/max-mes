import { EmitFn } from '@bmos/components';
import { nextTick } from 'vue';
import { insertElementValue } from '../utils';
import { renderUtil } from '../utils/rernderUtil';
import { emits } from './type';

export const useRender = (record: any, emit: EmitFn<emits>) => {
  const { container, initContent } = record;

  const handleComponent = (div: DocumentFragment): DocumentFragment => {
    const componentList: any[] = [];
    let syncUtil = renderUtil();
    syncUtil(() => {
      for (const component of componentList) {
        const node = div.getElementById(component.fieldId);
        if (!node) continue;
        insertElementValue(component, node);
      }
    });
    // @ts-ignore
    syncUtil = null;

    return div;
  };

  const appendChild = (child: DocumentFragment) => {
    container.value!.innerHTML = '';
    container.value?.appendChild(child);
    nextTick(() => {
      emit('rendered');
    });
  };

  const render = (val: string) => {
    const content = initContent(val);

    if (container.value) {
      appendChild(content);
    } else {
      nextTick(() => {
        appendChild(content);
      });
    }
    requestAnimationFrame
      ? requestAnimationFrame(() => {
          handleComponent(content);
        })
      : setTimeout(() => handleComponent(content));
  };
  return {
    render,
  };
};
