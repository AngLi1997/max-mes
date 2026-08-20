export enum MODE {
  'observer' = 1, //浏览器api mutationobserver进行监听
  'listener' = 0,
}

export enum STATUS_MAP {
  UN_READY = 'UN_READY', // 尚未初始化
  PENDING = 'PENDING', // 开始初始化但尚未 ready
  READY = 'READY', // 初始化完成并已 ready
}

