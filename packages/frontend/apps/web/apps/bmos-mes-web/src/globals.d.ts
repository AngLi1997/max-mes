import luckyexcel from '@/components/ReportForm/luckyexcel';
import { LoadEvent } from '@bmos/utils';

declare global {
  interface UE {
    [key: string]: any;
  }
  interface UEDITOR_CONFIG {
    UEDITOR_HOME_URL: string;
    [key: string]: any;
  }

  declare const UE: UE;
  declare const UEDITOR_CONFIG: UEDITOR_CONFIG;
  declare const luckyexcel: luckyexcel;
  declare const luckysheet: import('./components/ReportForm/luckysheet').default;
  declare interface Window {
    UE: UE;
    UEDITOR_CONFIG: UEDITOR_CONFIG;
    $loadEventBus: InstanceType<typeof LoadEvent>;
    luckyexcel: luckyexcel;
    luckysheet: import('./components/ReportForm/luckysheet').default;
  }
  type KEY = string | number;
  type Function = (...args: any[]) => any;
}
