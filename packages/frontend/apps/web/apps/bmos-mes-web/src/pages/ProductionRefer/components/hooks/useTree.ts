import { getRelationDetail } from '@/services';
import { message } from 'ant-design-vue';
export const useTree = () => {
  const treeData = ref();
  const selectedKeys = ref([]);
  const fieldNames = {
    title: 'batchNo',
    key: 'productPlanId',
  };
  const file = ref();
  const fileUrl = ref();

  const currentNode = ref();

  const handleData = (data: Array<any>) => {
    const dataMap = new Map();
    data.forEach(item => {
      if (dataMap.has(item.processId)) {
        dataMap.set(item.processId, [...dataMap.get(item.processId), item]);
      } else {
        dataMap.set(item.processId, [item]);
      }
    });
    const handles = [...dataMap.values()];
    const datas = handles.reduce((prev, cur, index) => {
      cur.sort((a: any, b: any) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime());
      const target = cur[0];
      const process = {
        batchNo: target.processName,
        productPlanId: index,
        selectable: false,
        startTime: target.startTime,
        children: cur,
      };
      prev.push(process);
      return prev;
    }, []);
    datas.sort((a: any, b: any) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime());
    treeData.value = datas;
  };

  const initData = async (id: string) => {
    try {
      const { data = [] } = await getRelationDetail(id);
      handleData(data);
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const treeSelect = (keys: KEY[], { node }: any) => {
    fileUrl.value = `${document.location.protocol}//${document.location.hostname}:${document.location.port}/${node.archiveFileUrl}`;
    fetch(fileUrl.value)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.blob(); // 将响应转换为Blob对象
      })
      .then(blob => {
        // 创建一个用于读取Blob的FileReader对象
        const reader = new FileReader();

        reader.onload = e => {
          // e.target.result 包含了文件的数据
          file.value = e.target?.result;
        };

        reader.onerror = error => {
          console.error('File could not be read!', error);
        };

        // 读取Blob数据
        reader.readAsArrayBuffer(blob);
      })
      .catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
      });
  };

  return {
    initData,
    treeData,
    selectedKeys,
    fieldNames,
    treeSelect,
    file,
    fileUrl,
  };
};
