import { Key } from 'ant-design-vue/es/_util/type';
import { ComponentNode, NODE_INFO } from '../../../components/Record';
import { MODAL_BUTTON, MODAL_ENUM, MODAL_NODE } from '../enum';

export const container_style = `box-sizing:border-box;min-height:22px;line-height:16px;vertical-align:middle;text-indent:2px;display:inline-block;border:1px solid #909398;color:#909398;`;

export type NODE_INFO_TYPE = keyof typeof NODE_INFO;

export const INSERT_CONTENT = (
  type: NODE_INFO_TYPE,
  id: Key,
  index: number | string = 1,
  detail?: string,
  componentName?: string,
  style?: any,
  isTableBoxFlag?: boolean,
) => {
  if (MODAL_NODE.includes(type)) {
    if (MODAL_ENUM.RADIO === type) {
      return INSERT_RADIO_CONTENT(type, id, index, detail, style);
    }
    if (MODAL_ENUM.CHECKBOX === type) {
      return INSERT_CHECKBOX_CONTENT(type, id, index, detail, style);
    }
  }
  if (MODAL_BUTTON.includes(type)) {
    return INSERT_BUTTON_CONTENT(id, componentName, style, 'business-button-class');
  }
  return `<textarea 
  id="${id}"
  class="record-component isClick"
  name="${id}"
  readonly
  style="height: ${
    style.fontSize.split('px')[0] * 1 + 4
  }px;vertical-align:top;position: relative;top: -2px;text-indent:2px;font-family: ${style.family};font-size:${
    style.fontSize
  };max-width:98%;overflow: hidden;width: ${
    isTableBoxFlag ? 'calc(100% - 11px)' : '120px'
  }; border-radius: 2px; border: 1px solid #909398;">
  ${componentName}${index}
  </textarea>`;
};

export const createContent = (type: NODE_INFO_TYPE, id: Key, index: number | string = 1, detail?: string) => {
  if (MODAL_NODE.includes(type)) {
    if (MODAL_ENUM.RADIO === type) {
      return CREATE_RADIO_CONTENT(type, id, index, detail);
    }
    if (MODAL_ENUM.CHECKBOX === type) {
      return CREATE_CHECKBOX_CONTENT(type, id, index, detail);
    }
  }
  return `<textarea 
    id="${id}"
    class="record-component"
    name="${id}"
    readonly  
    style="height:16px;vertical-align:middle;text-indent:2px;min-width:80px;">
    ${NODE_INFO[type].componentName}${index}
    </textarea>`;
};

const createRadio = (options: any[], id: KEY) => {
  let content = '';
  let i = 1;
  for (const item of options) {
    content += `<span id="${id}_span_${i}"><input id="${id}_radio_${i}" value="${id}_radio_${i}" type="radio" name="${id}" class="radio-component" />${item.field}</span>`;
    i++;
  }
  return content;
};

export const CREATE_RADIO_CONTENT = (type: NODE_INFO_TYPE, id: Key, index: number | string = 1, detail?: string) => {
  const options: any[] = detail ? JSON.parse(detail) : [];
  if (options.length === 0) {
    return '';
  }

  return createRadio(options, id);
};

export const CREATE_CHECKBOX_CONTENT = (type: NODE_INFO_TYPE, id: Key, index: number | string = 1, detail?: string) => {
  const options: any[] = detail ? JSON.parse(detail) : [];
  if (options.length === 0) {
    return '';
  }
  let content = '';
  let i = 1;
  for (const item of options) {
    content += `<span id="${id}_span_${i}"><input id="${id}_checkbox_${i}"  value="${id}_checkbox_${i}" name="${id}" type="checkbox" class="checkbox-component" />  ${item.field}</span>`;
    i++;
  }
  return content;
};

export const INSERT_RADIO_CONTENT = (
  type: NODE_INFO_TYPE,
  id: Key,
  index: number | string = 1,
  detail?: string,
  style?: any,
) => {
  const options: any[] = detail ? JSON.parse(detail) : [];
  if (options.length === 0) {
    return '';
  }
  const content = createRadio(options, id);
  return `<label 
  class="record-component radio-component-container"
  id="${id}"
  readonly  
  style="${container_style}padding-right:4px;font-family: ${style.family};font-size:${style.fontSize};max-width:98%;line-height: normal;word-wrap: break-word;">
  ${content}
  </label>`;
};

export const INSERT_CHECKBOX_CONTENT = (
  type: NODE_INFO_TYPE,
  id: Key,
  index: number | string = 1,
  detail?: string,
  style?: any,
) => {
  const content = CREATE_CHECKBOX_CONTENT(type, id, index, detail);
  return `<label 
  class="record-component checkbox-component-containerr"
  id="${id}"
  readonly  
  style="${container_style}padding-right:4px;font-family: ${style.family};font-size:${style.fontSize};max-width:98%;">
  ${content}
  </label>`;
};

export const INSERT_BUTTON_CONTENT = (id: Key, componentName?: string, style?: any, className?: any) => {
  return `<textarea 
  class="${className}"
  id="${id}"
  name="${id}"
  readonly  
  disabled
  style="vertical-align:top;border: 0;padding: 6px 20px;border-radius: 4px;font-family: ${style.family};font-size:${
    style.fontSize
  };background: #CCE6FF;color: #242526;height: 35px;box-sizing: border-box;max-width: 120px;text-align: center; overflow: hidden;">${componentName?.substring(
    0,
    componentName.length - 2,
  )}</textarea>`;
};

export type EDITOR_NODE = ComponentNode & { fieldId: KEY };

export const defaultStyle: object | string = {
  border: '1px solid #2871FF',
  color: '#2871FF',
  background: '#EBF1FF',
};

export const calculateHeightByStr = (str: string): number => {
  if (!str) return 0;
  let par = document.createElement('div');
  let div = document.createElement('div');
  div.innerHTML = str;
  par.style.height = '0';
  par.style.overflow = 'hidden';
  par.appendChild(div);
  document.body.appendChild(par);
  let height = div.offsetHeight;
  par.removeChild(div);
  document.body.removeChild(par);
  par = null as any;
  div = null as any;
  return height;
};
