export const objectKeys = obj => {
  if (!obj || JSON.stringify(obj) === '{}') return [];
  return Object.keys(obj);
};

export const objectEach = (obj, iterator) => {
  for (const [key, val] of Object.entries(obj)) {
    if (iterator.call(obj, val, key) === false) break;
  }
};

// 通过路径获取最后一个/** 中的**内容
export const accessPath = path => {
  if (path === void 0) throw 'the parameter must be a String';
  if (path === '') return path;
  path = decodeURIComponent(path);
  const keys = path.split('/');

  return keys.pop();
};

export const capitalizeFirstLetter = string => {
  if(!string)return string
  return string.charAt(0).toUpperCase() + string.slice(1);
};

const handleString = (str:string)=>{
  const strs = str.trim().split('-')
  if(strs.length===1)return capitalizeFirstLetter(strs[0])
  return strs.reduce((prev,cur)=>{
    return prev + capitalizeFirstLetter(cur)
  },'')
}

// 在路径中间，将参数放在最后
// /templates/{templateId}/templateVersions --> /templates/templateVersions/{templateId}
const handlePath = (path: string) => {
  if (!path) return '';
  return (
    path.replace(/\/\{[^}]+\}/g, '') + path.match(/\/\{[^}]+\}/g)?.join('')
  );
};

// 通过路径获取名称
export const getNameByPath = (
  path: string,
  num: number = 3,
  REGEX: RegExp = /[{}]/g,
) => {
  if (!path) throw 'the path is required';

  const num_1 = (path.match(/{/g) || []).length;
  if (num_1 > num) num = num_1 + 1;

  //处理参数位置
  if (num_1 > 0 && path[path.length - 1] !== '}') path = handlePath(path);

  path = path.replace(REGEX, '');
  if (!path) return '';

  let paths = path.split('/');
  let name = '';

  if (paths.length > num) {
    paths = paths.slice(0 - num);
  }

  name = paths.reduce((prev, cur, index) => {
    cur = handleString(cur)
    if (paths.length === index + num_1) {
      return prev + 'By' + cur;
    }

    return prev + cur;
  }, name);

  return name;
};

// 通过正则去掉中文和符号
export const handleZH = (str: string) => {
  str = str.split('«').pop() || ''
  return str.replace(
    /[\u4e00-\u9fa5\u3000-\u303f\uff00-\uff0f\uff1a\uff1b-\uff20\uff3b-\uff40\uff5b-\uff65\uffe0-\uffee！？。，：:]/g,
    '',
  );
};

export const Uppercase = (str:string)=>{
  if(!str)return ''
  return str.charAt(0).toUpperCase() + str.slice(1);
}

// exports.objectKeys = objectKeys;
// exports.objectEach = objectEach;
// exports.accessPath = accessPath;
// exports.capitalizeFirstLetter = capitalizeFirstLetter;
// exports.default = { objectKeys, objectEach, accessPath };
