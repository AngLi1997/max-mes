import { all, create } from 'mathjs';
export const useMathJs = () => {
  // 设置mathjs的配置，需要时添加
  const config = {
    number: 'BigNumber',
    precision: 32
  };
  const math = create(all, config);
  return {
    math
  };
};
