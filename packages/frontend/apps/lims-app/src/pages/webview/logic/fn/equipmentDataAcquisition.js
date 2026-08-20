export const equipmentDataAcquisition = async (data, isUpdate = 1) => {
  const params = {
    ...data.parent,
    curFieldId: data.fieldId,
    equipmentAcquisitionGroupComponentId: data.originalComponentType === 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE' ? data.id : data.parentId,
    isUpdate, // 1: 新增 2: 修改
  };
  const query = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
  // #ifdef APP-PLUS
    .concat([`${encodeURIComponent('scanError')}=${encodeURIComponent('1')}`])
  // #endif
    .join('&');
  uni.navigateTo({
    url: `/pages/businessComponents/equipmentDataAcquisition/equipmentSelection/index?${query}`,
  });
};
