export enum MODE {
  'observer' = 1, //浏览器api mutationobserver进行监听
  'listener' = 0,
}

export enum STATUS_MAP {
  UN_READY = 'UN_READY', // 尚未初始化
  PENDING = 'PENDING', // 开始初始化但尚未 ready
  READY = 'READY', // 初始化完成并已 ready
}

export const PageBreakContainerID = '#edui1_iframeholder';

export const BreakLineClass = 'mes-break-line';

export const BreakLineContainerClass = 'mes-break-line-container';

export const BreakLineContainerID = '#mes-break-line-container';

export const defaultLineConfig = {
  pageHeaderHeight: 20,
  pageFooterHeight: 20,
  color: '#FF5633',
  pattern: 1, //横板 | 竖版
};

export const defaultAheight = 1123;

export const defaultAwidth = 794;
