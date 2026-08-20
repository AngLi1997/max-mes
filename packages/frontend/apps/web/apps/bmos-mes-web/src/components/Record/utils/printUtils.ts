import { pageA4Height, pageA4Width } from './const';
import { createElementByStr, getPxByConfig } from './domutils';
import { Distance } from './types';

export const mToPoint = (m: number): number => {
  return ((m / 25.4) * 72) as number;
};

export const pxToPoint = (m: number): number => {
  return (96 / 25.4) * m;
};

export const createHeaderOrFooter = (str: string, pattern: number = 1, config: Distance) => {
  const { left, right } = getPxByConfig(config);
  const div = createElementByStr(str);
  const divPar = createElementByStr('');
  divPar.style.height = '0';
  if (pattern === 1) {
    divPar.style.width = pxToPoint(pageA4Width) + 'px';
  } else {
    divPar.style.width = pxToPoint(pageA4Height) + 'px';
  }
  divPar.style.paddingLeft = left + 'px';
  divPar.style.paddingRight = right + 'px';
  divPar.appendChild(div);
  document.body.appendChild(divPar);
  return { par: divPar, div };
};

export const loadBlobToBase64 = (blob: Blob): Promise<any> => {
  return new Promise<string | ArrayBuffer | null>((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result);
    reader.readAsDataURL(blob);
  });
};
