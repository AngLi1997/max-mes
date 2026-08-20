export const useExpand = () => {
  const pageRef = ref<any>(null);

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  const fetchData = async (params: any) => {
    pageRef.value?.fetchData(0, params);
  };

  return {
    pageRef,
    setRef,
    fetchData,
  };
};
