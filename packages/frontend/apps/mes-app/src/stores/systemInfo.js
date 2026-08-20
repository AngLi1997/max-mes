import { getAllParameterApi } from '@/api';
import { t } from '@/utils/useBmosI18n';
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useSystemInfoStore = defineStore('systemInfo', () => {
  const cacheSize = ref('');

  // 所有的参数配置
  const allParameterConfigMap = new Map();

  // 获取缓存
  function formatSize() {
    let fileSizeString = '';
    // #ifdef APP-PLUS
    plus.cache.calculate((size) => {
      const sizeCache = Number.parseInt(size);
      if (sizeCache == 0) {
        fileSizeString = '0B';
      }
      else if (sizeCache < 1024) {
        fileSizeString = `${sizeCache}B`;
      }
      else if (sizeCache < 1048576) {
        fileSizeString = `${(sizeCache / 1024).toFixed(2)}KB`;
      }
      else if (sizeCache < 1073741824) {
        fileSizeString = `${(sizeCache / 1048576).toFixed(2)}MB`;
      }
      else {
        fileSizeString = `${(sizeCache / 1073741824).toFixed(2)}GB`;
      }
      cacheSize.value = fileSizeString;
    });
    // #endif
  }
  // 清除缓存
  function clearCache() {
    // #ifdef APP-PLUS
    const os = plus.os.name;
    if (os == 'Android') {
      const main = plus.android.runtimeMainActivity();
      const sdRoot = main.getCacheDir();
      const files = plus.android.invoke(sdRoot, 'listFiles');
      const len = files.length;
      for (let i = 0; i < len; i++) {
        const filePath = `${files[i]}`;
        plus.io.resolveLocalFileSystemURL(filePath, (entry) => {
          if (entry.isDirectory) {
            entry.removeRecursively((entry) => {
              uni.showToast({
                title: t('缓存清理完成'),
                duration: 2000,
                icon: 'none',
              });
              formatSize();
            }, (e) => {
              console.log(e.message);
            });
          }
          else {
            entry.remove();
          }
        }, (e) => {
          console.log('文件路径读取失败');
        });
      }
    }
    else { // ios
      plus.cache.clear(() => {
        uni.showToast({
          title: t('缓存清理完成'),
          duration: 2000,
        });
        formatSize();
      });
    }
    // #endif
  }

  // 设置所有的参数配置
  function setAllParameterConfig(data) {
    allParameterConfigMap.clear();
    data.forEach((item) => {
      allParameterConfigMap.set(item.code, item);
    });
  }
  // 获取所有的参数配置
  async function getAllParameterConfig() {
    try {
      const res = await getAllParameterApi();
      setAllParameterConfig(res.data || []);
      return Promise.resolve();
    }
    catch (error) {
      return Promise.reject(new Error(t('获取参数配置失败')));
    }
  }
  // 获取参数配置值
  function getParameterByCode(code) {
    return allParameterConfigMap.get(code) || '';
  }

  return { cacheSize, allParameterConfigMap, formatSize, clearCache, getAllParameterConfig, getParameterByCode };
});
