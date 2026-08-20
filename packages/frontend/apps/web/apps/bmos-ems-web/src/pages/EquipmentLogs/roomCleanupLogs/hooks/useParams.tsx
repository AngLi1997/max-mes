export const useParams = () => {
  const queryParams = ref<any>({}); //存查询过的参数
  return {
    queryParams,
  };
};
