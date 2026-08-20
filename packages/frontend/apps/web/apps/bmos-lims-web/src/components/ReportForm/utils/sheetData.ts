import { getReleaseTemplateData } from '@/services';

export const getData = async(id: KEY) => {
  return await getReleaseTemplateData({ templateVersionId: id as number });
};

export const handleFileName = (name:string) => {
  const arr = name.split('/')
  arr.splice(0, 4)
  return '/' + arr.join('/')
}
