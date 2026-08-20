import request from '../../service';

/**
 * @description:  建筑用能统计报表(获取电耗能) /api/app/query/energy/getBuildReport
 */
export const postQueryEnergyGetBuildReport = (data: any) => {
  return request({
    url: `/app/query/energy/getBuildReport`,
    method: 'POST',
    data,
  });
};

/**
 * @description:  获取指定区域/分项日、月、年能耗数据(获取集中供热量耗能) /api/app/query/energy/getReport
 */
export const postQueryEnergyGetReport = (data: any) => {
  return request({
    url: `/app/query/energy/getReport`,
    method: 'POST',
    data,
  });
};
