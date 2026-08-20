import request from '../../service';

//获取设备类型分类树
export const getEquipmentTagTree = () => {
  return request({
    url: `/app/platform/equipment/tag/tree`,
    method: 'GET',
  });
};

//新增设备类型
export const reqEquipmentTag = (data: any) => {
  return request({
    url: `/app/platform/equipment/tag`,
    method: 'POST',
    data,
  });
};

//修改设备类型
export const reqUpdateEquipmentType = (data: any) => {
  return request({
    url: `/app/platform/equipment/tag`,
    method: 'PUT',
    data,
  });
};

//删除设备类型
export const reqDeleteEquipmentType = (params: any) => {
  return request({
    url: `/app/platform/equipment/tag`,
    method: 'DELETE',
    params,
  });
};
