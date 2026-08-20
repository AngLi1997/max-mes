/** 全局通过表格查询返回结果 */
export type TableListResult<T = any> = {
  // list: T;
  // pagination?: PaginationResult;
  code: number;
  data: {
    list?: T;
    pageNum?: number;
    pageSize?: number;
    total?: number;
    totalPage?: number;
  };
  message: string;
};

/** 全局通用表格分页返回数据结构 */
export type PaginationResult = {
  page: number;
  size: number;
  total: number;
};

/** 全局通用表格分页请求参数 */
export type PageParams<T = any> = {
  limit?: number;
  page?: number;
} & {
  [P in keyof T]?: T[P];
};

export type ErrorResponse = {
  /** 业务约定的错误码 */
  errorCode: string;
  /** 业务上的错误信息 */
  errorMessage?: string;
  /** 业务上的请求是否成功 */
  success?: boolean;
};
