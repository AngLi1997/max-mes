export const renderUtil = () => {
  const timerObj: { [key: symbol]: any } = {};
  return (callback: Function) => {
    const s_id = Symbol();
    timerObj[s_id] = setTimeout(() => {
      try {
        callback();
      } catch (error) {
      } finally {
        clearTimeout(timerObj[s_id]);
      }
    }, 0);
  };
};
