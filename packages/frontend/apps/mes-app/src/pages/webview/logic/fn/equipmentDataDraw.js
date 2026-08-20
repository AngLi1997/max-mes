export const equipmentDataDraw = async (data) => {
  console.log('===============data', data);

  const params = {
    ...data.parent,
    fieldId: data.fieldId,
    componentId: data.parent.id,
    value: data.value,
  };
  const query = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&');
  uni.navigateTo({
    url: `/pages/businessComponents/equipmentDataDraw/index?${query}`,
  });
};
