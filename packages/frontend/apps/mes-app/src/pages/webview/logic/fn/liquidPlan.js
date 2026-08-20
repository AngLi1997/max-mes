export const liquidPlan = async (data) => {
  const params = {
    ...data.parent,
    curFieldId: data.fieldId,
  };
  const query = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&');
  uni.navigateTo({
    url: `/pages/businessComponents/liquidPlan/index?${query}`,
  });
};
