import { fileDownload } from '@/services';
import { message } from 'ant-design-vue';
import { ComponentNode } from '../NodeList';
import { InputType, InputTypes } from '../Record/enum';
import { loadBlobToBase64, pxToPoint } from './printUtils';
import { Distance } from './types';

export const setNodeClassName = (target: HTMLElement | Element, name: string) => {
  try {
    if (!target || name === void 0) throw '';
    if (!target.classList.contains(name)) {
      target.classList.add(name);
    }
  } catch (error) {
    throw 'set attribute failed';
  }
};

export const removeNodeClassName = (target: HTMLElement | Element, name: string) => {
  try {
    if (!target || name === void 0) throw '';
    if (target.classList.contains(name)) {
      target.classList.remove(name);
    }
  } catch (error) {
    throw 'set attribute failed';
  }
};

export const insertElementValue = (data: ComponentNode & { value: string | Array<string> }, node: HTMLElement) => {
  if (InputTypes.includes(data.componentType)) {
    const nodes: NodeList = node.querySelectorAll(`[name="${data.fieldId}"]`) || [];
    const is_radio = data.componentType === InputType.RADIO;
    for (const no of nodes) {
      const input = no as HTMLInputElement;
      const check = data.value.includes(input.value);
      input.checked = false;
      if (check) {
        input.checked = true;
        if (is_radio) return;
      }
    }
    return;
  }
  node.innerText = data.value as string;
};

export const selectorAllElement = (data: ComponentNode & { value: string | Array<string> }, node: NodeList) => {};

export const createElementByStr = (str: string) => {
  const div = document.createElement('div');
  div.innerHTML = str;
  return div;
};

export const imageLoadFuc = (
  img: HTMLImageElement,
  url: string,
  callback: Function = (url?: string, buffer?: Blob) => {},
) => {
  // const fileName = handleFileName(url);
  fileDownload(url)
    .then((result: ArrayBuffer) => {
      const blob = new Blob([result]);
      // const url = URL.createObjectURL(blob);
      loadBlobToBase64(blob).then(res => {
        img.src = res;
        img.onload = e => {
          callback(img, res);
        };
      });
    })
    .catch((err: any) => {
      message.error(err.message);
    });
};

export const getPxByConfig = (config: Distance): Distance => {
  const { top, bottom, left, right } = config;

  return {
    top: pxToPoint(top),
    bottom: pxToPoint(bottom),
    left: pxToPoint(left),
    right: pxToPoint(right),
  };
};
