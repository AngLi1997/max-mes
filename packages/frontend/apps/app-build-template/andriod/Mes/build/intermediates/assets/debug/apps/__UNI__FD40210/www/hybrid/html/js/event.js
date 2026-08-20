// 特殊的数组,非textarea组件
const specialArr = ['RADIO', 'RADIO_GROUP', 'CHECKBOX', 'CHECKBOX_GROUP']

const buttonsIdsMap = new Map([
	['back-button', 'BACK'],
	['save-button', 'SAVE'],
	['finish-button', "FINISH"],
	['menu-button', 'MENU'],
	['pre-button', 'PREPAGE'],
	['next-button', 'NEXTPAGE'],
])
const postMessageToUniapp = (type, params = {}) => {
	uni.postMessage({
		data: {
			type,
			...params
		}
	});
}

// 给webview页面的悬浮按钮添加事件
const addEventForActionButtons = () => {
	buttonsIdsMap.forEach((value, key) => {
		$(`#${key}`).on('click', () => {
			postMessageToUniapp(value)
		})
	})
}

// 初始化时设置body元素内容
function render(data) {
	$("#form-box").html(data)

	$("#form-box").on('click', (e) => {
		if (e.target.id) {
			console.log('页面元素点击', e.target.id);
			const $dom = getDom({ fieldId: e.target.id })
			postMessageToUniapp("CLICK", {
				id: e.target.id,
				componentType: $dom.attr('componentType')
			})
		}
	})
	$("#form-box").on('input', inputAndChangeEventHandler)
}

// 组件值改变处理函数（只处理RADIO、CHECKBOX、NUMBER、TEXT）
function inputAndChangeEventHandler(e) {
	console.log('inputAndChangeEventHandler', e.target.id)
	const $dom = $(`#${e.target.id}`)
	const componentType = $dom.attr('componentType')
	// 判断点击的元素是否有id且componentType属性是否有值
	if ($dom.length > 0 && componentType) {
		if (['RADIO', 'CHECKBOX', 'NUMBER', 'TEXT'].includes(componentType)) {
			// NUMBER数字输入处理
			if ($dom.attr('componentType') === 'NUMBER') {
				if ($dom.attr('nullValue') === $dom.val()) {
					return
				}
				let newValue = filterNumber($dom.val())
				$dom.val(newValue)
			}
			// 除RADION和CHECKBOX以外,需要处理textarea高度
			if (!specialArr.includes($dom.attr('componentType'))) {
				textareaAutoHeight($dom)
			}
			// 向uni-app发送消息
			postMessageToUniapp("INPUT", {
				fieldId: e.target.id,
				componentType: $dom.attr('componentType'),
				value: $dom.val(),
				checked: $dom.prop('checked') || false
			})
		}
	}
}

function getDom(item) {
	return $(`#${item.fieldId}`)
}
// 初始化所有元素
function initElements(data) {
	const dataT = JSON.parse(data)
	if (Array.isArray(dataT)) {
		dataT.forEach(item => {
			initElItem(item)
		})
	} else {
		initElItem(dataT)
	}
}

// 初始化单个元素（替换textarea）
function initElItem(item) {
	let $dom = getDom(item)
	if (!$dom.length) {
		return
	}
	replaceChildElement($dom, item)
}

// 替换初始textarea节点
function replaceChildElement($dom, item) {
	const originWidth = $dom.css('width')
	const originHeight = $dom.css('height')
	const newHtml = createNewReplaceElement(item, $dom)
	$dom.replaceWith(newHtml)
	const $newDom = getDom(item);
	// 设置宽高
	$newDom.css({
		width: originWidth,
		height: originHeight,
		lineHeight: originHeight
	})
	setState($newDom, item)
	// 设置readonly属性值
	setTextareaReadonly($newDom, item)
}

// 构建新的textarea元素
function createNewReplaceElement(item, $dom) {
	switch (item.componentType) {
		case 'CHECKBOX':
			let els = `<div id="${item.fieldId}" componentType="CHECKBOX_GROUP">`
			item.componentDetail.forEach((opt) => {
				els +=
					`<label id="${item.fieldId}" componentType="${item.componentType}" class='checkbox-label'><input componentType="${item.componentType}" id="${item.fieldId}_${opt.field}" type="checkbox" name="${item.fieldId}" value="${opt.field}">${opt.field}</input></label>`
			})
			return els + '</div>'
			break;
		case 'RADIO':
			let radios = `<div id="${item.fieldId}" componentType="RADIO_GROUP">`
			item.componentDetail.forEach((opt) => {
				radios +=
					`<label class='radio-label' id="${item.fieldId}" componentType="${item.componentType}"><input componentType="${item.componentType}" id="${item.fieldId}_${opt.field}" type="radio" name="${item.fieldId}" value="${opt.field}">${opt.field}</input></label>`
			})
			return radios + '</div>'
			break;
		default:
			return `<textarea class="default" nullValue="${item.nullValue}" componentType="${item.componentType}" id="${item.fieldId}" rows="1"></textarea>`
			break;
	}
}

// 设置组件class类名
function setState($dom, item) {
	$dom.removeClass()
	if (item.componentType === 'RADIO') {
		$dom.addClass('radio-label')
		return
	}
	if (item.componentType === 'CHECKBOX') {
		$dom.addClass('checkbox-label')
		return
	}
	$dom.addClass(item.state)
}

// 设置readonly属性
function setTextareaReadonly($dom, item) {
	let isReadonly = Array.isArray(item.value) && item.value.length > 0 || item.value !== ''
	if (item.state === 'default' || item.state === 'unfilled') {
		// 数据未保存状态 除了文本、数字、单选、多选以外，其余设置readonly
		if (['TEXT', 'NUMBER', 'RADIO', 'CHECKBOX'].includes(item.componentType)) {
			isReadonly = false
		} else {
			isReadonly = true
		}
	}
	switch (item.componentType) {
		case 'RADIO':
			$dom.find('input').attr('disabled', isReadonly)
			break;
		case 'CHECKBOX':
			$dom.find('input').attr('disabled', isReadonly)
			break;
		default:
			$dom.attr('readonly', isReadonly)
			break;
	}
}

// 回显数据组
function echoData(data) {
	const compoentValues = JSON.parse(data)
	compoentValues.forEach(item => {
		setComponentValue(item)
	})
}

// 回显单个组件数据
function echoSingleData(data) {
	const item = JSON.parse(data)
	setComponentValue(item)
}

// 设置组件的值
const setComponentValue = (item) => {
	const $dom = getDom(item)
	if ($dom.length > 0) {
		// 单选、多选
		if (item.componentType === 'RADIO') {
			item.componentDetail.forEach(opt => {
				$dom.find(`#${item.fieldId}_${opt.field}`).prop('checked', false)
			})
			$dom.find(`#${item.fieldId}_${item.value}`).prop('checked', true)
		} else if (item.componentType === 'CHECKBOX') {
			item.componentDetail.forEach(opt => {
				$dom.find(`#${item.fieldId}_${opt.field}`).prop('checked', false)
			})
			if (Array.isArray(item.value) && item.value.length > 0) {
				item.value.forEach(val => {
					$dom.find(`#${item.fieldId}_${val}`).prop('checked', true)
				})
			}

		} else {
			$dom.val(item.value)
			// 设置textarea高度
			textareaAutoHeight($dom)
		}
		// 	设置样式
		setState($dom, item)
		// 设置组件readonly
		setTextareaReadonly($dom, item)
	}
}



// textarea高度自动
function textareaAutoHeight($dom) {
	//动态设置textarea高度,高度根据内容自适应
	$dom.height('auto');
	let scrollHeight = $dom.prop("scrollHeight")
	$dom.height(scrollHeight - 14);
}

function filterNumber(str) {
	// 创建一个空字符串来存储结果
	let result = "";
	// 遍历输入字符串的每个字符
	for (let i = 0; i < str.length; i++) {
		// 获取当前字符
		let char = str[i];
		// 如果当前字符是数字，就将它添加到结果字符串中
		if (char >= "0" && char <= "9" || char == '.') {
			result += char;
		}
	}
	// 返回结果字符串
	return result;
}

let pageTotal = 0
let currentPage = 1
let currentCopyItem = null

// 渲染分页
function renderPagination(data) {
	const params = JSON.parse(data)
	pageTotal = params.total
	currentPage = params.current
	currentCopyItem = params.currentCopyItem
	$('.total-pagination').text(pageTotal)
	$dom = $('.pagination-class')
	if (pageTotal > 1) {
		$dom.show()
	} else {
		$dom.hide()
	}
	renderCurrentPage(params.current)
}

// 渲染当前页
function renderCurrentPage(data) {
	$('.current-pagination').text(data)
	// 显示隐藏已作废
	showHiddenObsolete(currentCopyItem && currentCopyItem.discard)

	// 显示隐藏复制
	showHiddenCopy(currentPage !== 1)
}

// 显示隐藏复制
function showHiddenCopy(flag) {
	if (flag) {
		$('.copy-class').show()
	} else {
		$('.copy-class').hide()
	}
}

// 显示隐藏已作废
function showHiddenObsolete(flag) {
	if (flag) {
		$('.obsolete-class').show()
	} else {
		$('.obsolete-class').hide()
	}
}

// 设置模板的放大缩小
function setTemplateScale(size) {
	$('#form-box').css({
		transform: `scale(${size})`,
		'transform-origin': '0% 0%',
		position: 'absolute',
		'box-sizing': 'border-box',
	})
}

// 设置按钮文字
function setBtnText(data) {
	const arr = JSON.parse(data)
	arr && arr.forEach(item => {
		$(`.${item.key}`).text(item.text)
	})	
}