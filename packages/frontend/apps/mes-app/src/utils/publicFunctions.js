/*
 * @description: 处理表单
 * @param targetForm: 指定数据集例如:let submitForm = {}
 * @param form: 指定取值表单
 * @param flag: 0-获取数据反显；1-提交表单数据
 */
export const manageForm = (targetForm, form, flag = 0) => {
    for (let item of form) {
        flag ? (targetForm[item.key] = item.value) : (item.value = targetForm[item.key]);
    }
};

/*
 * @description: 数据平分,分割成3个子数组
 * @param array: 指定分割的数组<Array>
 */

export const chunkArray = (array, size) => {
    if (!Array.isArray(array)) {
        throw new Error('First argument must be an array');
    }

    if (array.length === 0) {
        return [
            [],
            [],
            []
        ]; // 空数组返回三个空数组  
    }
    const chunks = [[], [], []];
    array.forEach((item, index) => {
        if (index % 3 === 0) {
            chunks[0].push(item);
        } else if (index % 3 === 1) {
            chunks[1].push(item);
        } else if (index % 3 === 2) {
            chunks[2].push(item);
        }
      });

    return chunks;
};

/**
 * 生成一个用不重复的ID
 * @param { Number } randomLength 
 */
export const getUuiD = (randomLength) => {
    return Number(Math.random().toString().substr(2, randomLength) + Date.now()).toString(36);
};
