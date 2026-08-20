import request from '../../service';

/**
 * @description /api/app/mes/lotRelease/manage/queryAuditPage 分页查询批签发数据
 */
export const reqLotReleaseManageQueryAuditPage = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/queryAuditPage',
    method: 'GET',
    params,
  });
};

/**
 * @description /api/app/mes/lotRelease/manage/auditProcess 查看审批进度
 */
export const reqLotReleaseManageAuditProcess = (params: any) => {
  return request({
    url: '/app/mes/lotRelease/manage/auditProcess',
    method: 'GET',
    params,
  });
};
