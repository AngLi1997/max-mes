import { Config } from './types';

export const printparamsConst = 'mes.record.margin';
export const pageA4Width = 210;
export const pageA4Height = 297;
export const defaultConfig: Config = {
  pattern: 1,
  header: 20,
  footer: 20,
  top: 0,
  bottom: 0,
  right: 0,
  left: 0,
};

export enum pageEnum {
  L = 'landscape',
  P = 'portrait',
}
