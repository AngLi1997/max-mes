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
