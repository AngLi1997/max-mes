/**
数据验证（表单验证）
*/

export default {
	error: '',
	check: function (data, rule) {
		for (var i = 0; i < rule.length; i++) {
			// 没有设置校验规则的表单字段直接返回 ture,既不校验
			if (!rule[i].checkType) {
				return true;
			}
			if (!rule[i].name) {
				return true;
			}
			if (!rule[i].errorMsg) {
				return true;
			}
			if (!data[rule[i].name]) {
				this.error = rule[i].errorMsg;
				return false;
			}
			switch (rule[i].checkType) {
				case 'string': // 指定输入字符长度
					// 相当于 /^.{n,m}$/g 字符长度为 n-m
					var reg = new RegExp('^.{' + rule[i].checkRule + '}$');
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'int': // 最小值为 1 的数字校验（年龄校验）
					// 相当于 /^(-[1-9]|[1-9])[0-9]?$/g 数字 1-99
					var reg = /^(-[1-9]|[1-9])[0-9]?$/g;
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'between': // 要求值为数字且在指定范围内
					// 判断内容是否为数字
					if (!this.isNumber(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					// 限定数字范围大小
					var minMax = rule[i].checkRule.split(',');
					minMax[0] = Number(minMax[0]);
					minMax[1] = Number(minMax[1]);
					if (data[rule[i].name] > minMax[1] || data[rule[i].name] < minMax[0]) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'betweenD': // 
					var reg = /^-?[1-9][0-9]?$/;
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					var minMax = rule[i].checkRule.split(',');
					minMax[0] = Number(minMax[0]);
					minMax[1] = Number(minMax[1]);
					if (data[rule[i].name] > minMax[1] || data[rule[i].name] < minMax[0]) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'betweenF':
					var reg = /^-?[0-9][0-9]?.+[0-9]+$/;
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					var minMax = rule[i].checkRule.split(',');
					minMax[0] = Number(minMax[0]);
					minMax[1] = Number(minMax[1]);
					if (data[rule[i].name] > minMax[1] || data[rule[i].name] < minMax[0]) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'same': // 等于指定内容
					if (data[rule[i].name] !== rule[i].checkRule) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'notSame': // 不能等于指定内容
					if (data[rule[i].name] === rule[i].checkRule) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'email': // 邮箱校验
					var reg = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'phone': // 电话号码校验
					var reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'zipcode': // 限定 6 位数字（邮政编码校验）
					var reg = /^[0-9]{6}$/;
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'idCard': // 身份证校验
					var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'reg': // 自定义正则式
					var reg = new RegExp(rule[i].checkRule);
					if (!reg.test(data[rule[i].name])) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'in': // 单选
					if (rule[i].checkRule.indexOf(data[rule[i].name]) === -1) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'notnull': // 判断是否为空
					if (data[rule[i].name] == null || data[rule[i].name].length < 1) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
				case 'custom': // 自定义
					if (rule[i].checkRule) {
						this.error = rule[i].errorMsg;
						return false;
					}
					break;
			}
		}
		return true;
	},
	isNumber: (checkVal) => {
		var reg = /^-?[1-9][0-9]?.?[0-9]*$/;
		return reg.test(checkVal);
	}
}
