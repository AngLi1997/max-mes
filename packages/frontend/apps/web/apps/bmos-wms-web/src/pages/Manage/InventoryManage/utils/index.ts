// 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
export const loopTree = (data: any) => {
  return data.map((item: any) => {
    if (item.categoryFlag) {
      item.selectable = false;
    } else {
      item.selectable = true;
    }
    item.label = item.mergeCode + '-' + item.name;
    if (item.children) {
      loopTree(item.children);
    }
    return item;
  });
};

//计算
export const numTofixed = (num: number) => {
  if (typeof num == 'number') num = parseFloat(num.toFixed(9));
  return num;
};

//加法
export const accAdd = (arg1: any, arg2: any) => {
  let r1, r2, m, n;
  try {
    r1 = arg1.toString().split('.')[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split('.')[1].length;
  } catch (e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2));
  n = r1 >= r2 ? r1 : r2;
  return ((arg1 * m + arg2 * m) / m).toFixed(n);
};

//减法
export const accSub = (arg1: any, arg2: any) => {
  let r1, r2, m, n;
  try {
    r1 = arg1.toString().split('.')[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split('.')[1].length;
  } catch (e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2));
  //动态控制精度长度
  n = r1 >= r2 ? r1 : r2;
  return ((arg1 * m - arg2 * m) / m).toFixed(n);
};

//乘法

export const accMul = (arg1: any, arg2: any) => {
  let m = 0,
    s1 = arg1.toString(),
    s2 = arg2.toString();
  try {
    m += s1.split('.')[1].length;
  } catch (e) {}
  try {
    m += s2.split('.')[1].length;
  } catch (e) {}
  return (Number(s1.replace('.', '')) * Number(s2.replace('.', ''))) / Math.pow(10, m);
};

//除法

export const accDiv = (arg1: any, arg2: any) => {
  let t1 = 0;
  let t2 = 0;
  let r1;
  let r2;
  try {
    t1 = arg1.toString().split('.')[1].length;
  } catch (e) {
    // console.error(e)
  }
  try {
    t2 = arg2.toString().split('.')[1].length;
  } catch (e) {
    // console.error(e)
  }
  r1 = Number(arg1.toString().replace('.', ''));
  r2 = Number(arg2.toString().replace('.', ''));
  if (r2 === 0) {
    return Infinity;
  } else {
    return (r1 / r2) * Math.pow(10, t2 - t1);
  }
};
