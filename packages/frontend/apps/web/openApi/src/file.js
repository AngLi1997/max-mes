
const fs = require('node:fs');
/**
 * 删除已经生成的文件
 * @param path
 */
const deleteFile = async path => {
  await new Promise((resolve, reject) => {
    // 判断文件是否存在
    fs.access(path, fs.constants.F_OK, async err => {
      if (!err) {
        try {
          await fs.promises.unlink(path);
        } catch (error) {
          console.error(error);
        }
      }
      resolve('');
    });
  });
};

exports.deleteFile = deleteFile