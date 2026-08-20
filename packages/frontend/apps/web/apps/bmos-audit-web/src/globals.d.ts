import { LoadEvent } from '@bmos/utils';

declare global {
  interface UE {
    [key: string]: any;
  }
  interface UEDITOR_CONFIG {
    UEDITOR_HOME_URL: string;
    [key: string]: any;
  }
  declare interface Window {
    UE: UE;
    UEDITOR_CONFIG: UEDITOR_CONFIG;
    $loadEventBus: InstanceType<typeof LoadEvent>;
  }
  type KEY = string | number;
}
