export const inspectionResults = async (data) => {
  const params = {
    ...data,
    componentId: data.parent.id
  };
  const query = Object.keys(params)
    .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
    .join('&');
  uni.navigateTo({
    url: `/pages/businessComponents/inspectionResults/index?${query}`,
  });
};
