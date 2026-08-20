import { ComponentNode } from '../NodeList';
import { InputType, InputTypes } from '../Record/enum';

export const setNodeClassName = (
  target: HTMLElement | Element,
  name: string,
) => {
  try {
    if (!target || name === void 0) throw '';
    if (!target.classList.contains(name)) {
      target.classList.add(name);
    }
  } catch (error) {
    throw 'set attribute failed';
  }
};

export const removeNodeClassName = (
  target: HTMLElement | Element,
  name: string,
) => {
  try {
    if (!target || name === void 0) throw '';
    if (target.classList.contains(name)) {
      target.classList.remove(name);
    }
  } catch (error) {
    throw 'set attribute failed';
  }
};

export const insertElementValue = (data: ComponentNode, node: HTMLElement) => {
  if (InputTypes.includes(data.componentType)) {
    const nodes: NodeList =
      node.querySelectorAll(`[name="${data.fieldId}"]`) || [];
    const is_radio = data.componentType === InputType.RADIO;
    const values: string[] = [];
    for (const no of nodes) {
      const input = no as HTMLInputElement;
      input.checked = false;
      if (values.includes(input.value)) {
        input.checked = true;
        if (is_radio) return;
      }
    }
    return;
  }
  node.value = 'dddd';
};

