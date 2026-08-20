// 特殊的数组,非textarea组件
const specialArr = ['RADIO', 'RADIO_GROUP', 'CHECKBOX', 'CHECKBOX_GROUP'];
let baseUrl = ''
let allText = {};
const allButtonComponent = [
	"MATERIAL_INPUT_BUTTON",
	"BATCH_RECEIVE_BUTTON",
	"MATERIAL_RECEIVE_BUTTON",
	"PICKING_RECEIVE_BUTTON",
	"INGREDIENTS_PLAN_BUTTON",
	"WEIGHING_INGREDIENTS_BUTTON",
	"INGREDIENTS_INPUT_BUTTON",
	"MATERIAL_RESERVE_BUTTON",
	"LIQUID_PLAN_BUTTON",
	"LIQUID_MEASURE_BUTTON",
	"LIQUID_INPUT_BUTTON",
	"LIQUID_OUTPUT_BUTTON",
	"FEED_RECYCLE_BUTTON",
	"OUTPUT_BUTTON_ASSEMBLY",
	"PRODUCT_OUTPUT_BUTTON"
]


const getAllText = (data) => {
	allText = JSON.parse(decryptedString(data))
}

const postMessageToUniapp = (type, params = {}) => {
	uni.postMessage({
		data: {
			type,
			...params
		}
	});
};

let viewPage = false;

// 设置页面是否查看状态
function setPageViewState(data) {
	viewPage = data === 'true'
}

// 初始化时设置body元素内容
function render(data) {
	$('#form-box').html(decryptedString(data));

	$('#form-box').on('click', (e) => {
		// 发消息隐藏快捷录入和趋势分析
		postMessageToUniapp('HIDE_QUICK_ANALYSIS');
		// 判断如果点击元素为 img 标签， 则 找到 img 标签的父元素，然后再找到父元素的 id
		// 如果 tagName 上的componentType属性值为 EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE
		if (e.target.getAttribute('componentType') === 'EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE') {
			// 找到这个 td 的 table 的 id
			const id = $(e.target).closest('table').attr('id');
			const componentType = $(e.target).closest('table').attr('componentType');
			postMessageToUniapp('CLICK', {
				id,
				componentType,
			});
			return;
		}
		if (e.target.tagName === 'IMG') {
			const imgParent = $(e.target).parent();
			const id = imgParent.attr('id');
			postMessageToUniapp('CLICK', {
				id,
				componentType: imgParent.attr('componentType')
			});
		} else if (e.target.id && e.target.id !== 'form-box') {
			const $dom = getDom({ fieldId: e.target.id });
			// 设置元素选中时的样式
			$('#form-box textarea').filter(function () {
				if ($(this).css('border-color') === 'rgb(34, 96, 217)') {
					$(this).removeClass('focus');
				}
				return true;
			});
			postMessageToUniapp('CLICK', {
				id: e.target.id,
				componentType: $dom.attr('componentType')
			});
			// 如果是单选组件
			if (e.target.className === 'custom-radio-input' || e.target.className === 'custom-checkbox-input') {
				// 阻止默认事件
				e.preventDefault();
				return;
			}
			// 如果是Text、Number组件并且不是readonly状态时，显示快捷录入和趋势分析
			if (($dom.attr('componentType') === 'TEXT' || $dom.attr('componentType') === 'NUMBER')) {
				if ($dom.attr('readonly')) {
					if ($dom.attr('componentType') === 'NUMBER' && $dom.attr('formula')) {
						postMessageToUniapp('SHOW_ANALYSIS_BUTTON_FUN', {
							id: e.target.id,
							componentType: $dom.attr('componentType')
						});
					}
				} else {
					postMessageToUniapp('SHOW_QUICK_ANALYSIS', {
						id: e.target.id,
						componentType: $dom.attr('componentType')
					});
				}
			}
		}
	});
	$('#form-box').on('input', inputAndChangeEventHandler);
}

// 组件值改变处理函数（只处理RADIO、CHECKBOX、NUMBER、TEXT）
function inputAndChangeEventHandler(e) {
	console.log('inputAndChangeEventHandler', e.target.id);
	const $dom = $(`#${e.target.id}`);
	const componentType = $dom.attr('componentType');
	// 判断点击的元素是否有id且componentType属性是否有值
	if ($dom.length > 0 && componentType) {
		const str = $dom.val();
		// // 去除字符串中的`
		// const str = $dom.val().replace(/\`/g, '').replace(/\\/g, '');
		$dom.val(str);
		if (['RADIO', 'CHECKBOX', 'NUMBER', 'TEXT'].includes(componentType)) {
			// NUMBER数字输入处理
			if ($dom.attr('componentType') === 'NUMBER') {
				if ($dom.attr('nullValue') === $dom.val()) {
					return;
				}
				let newValue = filterNumber($dom.val());
				$dom.val(newValue);
			}
			// 除RADION和CHECKBOX以外,需要处理textarea高度
			if (!specialArr.includes($dom.attr('componentType'))) {
				textareaAutoHeight($dom);
			}
			let value = $dom.val();

			if (componentType === 'RADIO' || componentType === 'CHECKBOX') {
				return
			}
			// 向uni-app发送消息
			postMessageToUniapp('INPUT', {
				fieldId: e.target.id,
				componentType: $dom.attr('componentType'),
				value,
				checked: $dom.prop('checked') || false
			});
		}
	}
}

function getDom(item) {
	return $(`#${item.fieldId}`);
}
// 存储所有元素的宽高
let elementWidthAndHeight = {};
// 初始化所有元素
function initElements(data) {
	const dataT = JSON.parse(decryptedString(data));
  // 保存所有元素的宽高
  saveElementsWidthAndHeight(dataT);
	if (Array.isArray(dataT)) {
		dataT.forEach(item => {
			initElItem(item);
		});
	} else {
		initElItem(dataT);
	}
  // 初始化所有元素的宽高
  setElementsWidthAndHeight(dataT);
}

// 初始化单个元素（替换textarea）
function initElItem(item) {
	let $dom = getDom(item);
	if (!$dom.length) {
		return;
	}
	replaceChildElement($dom, item);
}
// 替换初始textarea节点
function replaceChildElement($dom, item) {
	const newHtml = createNewReplaceElement(item, $dom);
	$dom.replaceWith(newHtml);
	const $newDom = getDom(item);
	setState($newDom, item);
	// 设置readonly属性值
	setTextareaReadonly($newDom, item);
}

// 保存所有元素的宽高
function saveElementsWidthAndHeight(data) {
  elementWidthAndHeight = {};
  const arr = data;
  if (!Array.isArray(data)) {
    arr = [data];
  }
  arr.forEach(item => {
    const $dom = getDom(item);
    const originWidth = $dom.css('width');
    const originHeight = $dom.css('height');
    elementWidthAndHeight[item.fieldId] = {
      width: originWidth,
      height: originHeight
    }
  })
}
// 设置所有元素的宽高
function setElementsWidthAndHeight(data) {
  const arr = data;
	if (!Array.isArray(data)) {
    arr = [data];
	}
  arr.forEach(item => {
    const $newDom = getDom(item);
    const originWidth = elementWidthAndHeight[item.fieldId].width;
    const originHeight = elementWidthAndHeight[item.fieldId].height;
	  // 设置宽高
	  if (!["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(item.originalComponentType)) {
		  $newDom.css({
			  ...(item.componentType !== 'RADIO' && item.componentType !== 'CHECKBOX' ? {width: originWidth } : {}),
			  height: ['EQUIPMENT_DATA_DRAW_LIST', 'PHOTO'].includes(item.componentType)? '' : originHeight,
			  ...(['EQUIPMENT_DATA_DRAW_LIST', 'PHOTO'].includes(item.componentType)? { minHeight: originHeight } : {}),
			  ...(item.componentType !== 'RADIO' && item.componentType !== 'CHECKBOX' && !allButtonComponent.includes(item.originalComponentType) ? {lineHeight: originHeight } : {}),
		  });
	  }
  })
}

// 构建新的textarea元素
function createNewReplaceElement(item, $dom) {
	if (allButtonComponent.includes(item.originalComponentType)) {
		return `<div 
			class="default"
			nullValue="${item.nullValue}"
			componentType="${item.componentType}"
			id="${item.fieldId}"
			rows="1"
			  style="display:inline-block;box-shadow: none;border: 0;padding: 6px 20px;border-radius: 4px;background: #2871FF;color: #FFF;height: 35px;box-sizing: border-box;max-width: 120px;text-align: center;">
				${item.componentName?.substring(0, item.componentName.length - 2)}
			</div>`;
	}
	if (["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(item.originalComponentType)) {
		// 动态表格 为table $dom[0].outerHTML 中的每一个td添加一个属性 componentType="EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE" 
		// 在 table 标签上添加一个属性 componentType="EQUIPMENT_DATA_ACQUISITION"
		return $dom[0].outerHTML.replace(/<td/g, `<td componentType="EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"`).replace(/<table/g, `<table componentType="EQUIPMENT_DATA_ACQUISITION"`);
	}
	switch (item.componentType) {
		case 'CHECKBOX':
			let els = `<div id="${item.fieldId}" componentType="CHECKBOX_GROUP" style="display: inline;">`;
			item.componentDetail.forEach((opt, index) => {
				els +=
					`<label class="custom-checkbox" id="${item.fieldId}" componentType="${item.componentType}" class='checkbox-label'><input readonly class="custom-checkbox-input" componentType="${item.componentType}" id="${item.fieldId}_${index}" type="checkbox" name="${item.fieldId}" value="${index}" /><span id="${item.fieldId}" componentType="${item.componentType}" class="checkbox-btn"><i id="${item.fieldId}" componentType="${item.componentType}" class="check-icon"></i>${opt.field}</span></label>`;
			});
			return els + '</div>';
		case 'RADIO':
			let radios = `<div id="${item.fieldId}" componentType="RADIO_GROUP" style="display: inline;font-size: ${$dom[0].style.fontSize || '14px'}">`;
			item.componentDetail.forEach((opt, index) => {
				radios +=
					`<label class='custom-radio' id="${item.fieldId}" componentType="${item.componentType}"><input readonly class="custom-radio-input" componentType="${item.componentType}" id="${item.fieldId}_${index}" type="radio" name="${item.fieldId}" value="${index}" /><span id="${item.fieldId}" componentType="${item.componentType}" class="radio-btn"><i id="${item.fieldId}" componentType="${item.componentType}" class="check-icon"></i>${opt.field}</span></label>`;
			});
			return radios + '</div>';
		case 'HANDLE_SUBMIT_SIGN':
			let handleSubmitSign = `<div id="${item.fieldId}" class="default" componentType="HANDLE_SUBMIT_SIGN" style="display: inline-block; min-height: 48px;">`;
			return handleSubmitSign + '</div>';
		case 'HANDLE_REVIEW_SIGN':
			let handleReviewSign = `<div id="${item.fieldId}" class="default" componentType="HANDLE_REVIEW_SIGN" style="display: inline-block; min-height: 48px;">`;
			return handleReviewSign + '</div>';
		case 'PHOTO':
			return `<div id="${item.fieldId}" class="default" nullValue="${item.nullValue}" style="display:flex;flex-direction: column;height:auto !important;" componentType="PHOTO"></div>`;
		case 'EQUIPMENT_DATA_DRAW_LIST':
			return `<div id="${item.fieldId}" class="default" nullValue="${item.nullValue}" style="display:flex;flex-direction: column;height:auto !important;" componentType="EQUIPMENT_DATA_DRAW_LIST"></div>`;
		default:
			return `<textarea class="default" nullValue="${item.nullValue}" componentType="${item.componentType}" id="${item.fieldId}" rows="1"></textarea>`;
	}
}

// 设置组件class类名
function setState($dom, item) {
	if (["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(item.originalComponentType)) {
		return ;
	}
	$dom.removeClass();
	if (item.componentType === 'RADIO') {
		$dom.addClass('radio-box');
		if (item.state !== 'default' && item.state !== 'saved') {
			$dom.addClass(item.state);
		}
		return;
	}
	if (item.componentType === 'CHECKBOX') {
		$dom.addClass('checkbox-box');
		if (item.state !== 'default' && item.state !== 'saved') {
			$dom.addClass(item.state);
		}
		return;
	}
	if (item.componentType === 'NUMBER' || item.componentType === 'TEXT' && item.state === 'default') {
		$dom.addClass('canInput');
	}
	$dom.addClass(item.state);
}

// 设置组件unfilled类
function addUnfilledState(data) {
	const list = JSON.parse(decryptedString(data));
	list.forEach(item => {
		const $dom = getDom(item);
		if ($dom.length) {
			$dom.removeClass('focus');
			$dom.addClass('unfilled');
		}
	});
}

// 设置readonly属性
function setTextareaReadonly($dom, item) {
	let isReadonly = (Array.isArray(item.value) && item.value.length > 0) || item.value !== '' || item.state === 'formula' || item.state === 'calculation';
	if (item.state === 'default' || item.state === 'unfilled') {
		// 数据未保存状态 除了文本、数字、单选、多选以外，其余设置readonly
		if (['TEXT', 'NUMBER', 'RADIO', 'CHECKBOX'].includes(item.componentType)) {
			isReadonly = !item.hasRight;
		} else {
			isReadonly = true;
		}
	}
	if (viewPage) {
		isReadonly = true;
	}
	switch (item.componentType) {
		case 'RADIO':
			$dom.find('input').attr('disabled', isReadonly);
			break;
		case 'CHECKBOX':
			$dom.find('input').attr('disabled', isReadonly);
			break;
		default:
			$dom.attr('readonly', isReadonly);
			break;
	}
	if (item.componentType == 'NUMBER' && item.state === 'formula') {
		$dom.attr('formula', true);
	}
}

// 回显数据组
function echoData(data) {
	const componentValues = JSON.parse(decryptedString(data));
	componentValues.forEach(item => {
		setComponentValue(item);
	});
}

// 回显单个组件数据
function echoSingleData(data) {
	const item = JSON.parse(decryptedString(data));
	setComponentValue(item);
}

// 设置组件的值
const setComponentValue = (item) => {
	const $dom = getDom(item);
	if ($dom.length > 0) {
		// 单选、多选
		if (item.componentType === 'RADIO') {
			if (item.emptyValue) {
				item.componentDetail?.forEach((_opt, index) => {
					$dom.find(`#${item.fieldId}_${index}`).prop('checked', false);
					$dom.find(`#${item.fieldId}_${index}`)?.parent().addClass('custom-radio-empty');
				});
			} else {
				let checkIndex = null;
				item.componentDetail?.forEach((opt, index) => {
					$dom.find(`#${item.fieldId}_${index}`).prop('checked', false);
				$dom.find(`#${item.fieldId}_${index}`)?.parent().removeClass('custom-radio-empty');
					if (opt.field === item.value) checkIndex = index;
				});
				if (checkIndex !== null) { $dom.find(`#${item.fieldId}_${checkIndex}`).prop('checked', true); }
			}
		} else if (item.componentType === 'CHECKBOX') {
			if (item.emptyValue) {
				item.componentDetail?.forEach((_opt, index) => {
					$dom.find(`#${item.fieldId}_${index}`).prop('checked', false);
					$dom.find(`#${item.fieldId}_${index}`)?.parent().addClass('custom-checkbox-empty');
				});
			} else {
				item.componentDetail?.forEach((_opt, index) => {
					$dom.find(`#${item.fieldId}_${index}`).prop('checked', false);
					$dom.find(`#${item.fieldId}_${index}`)?.parent().removeClass('custom-checkbox-empty');
				});
				if (Array.isArray(item.value) && item.value.length > 0) {
					item.componentDetail.forEach((opt, index) => {
						item.value.forEach(val => {
							if (val === opt.field) {
								$dom.find(`#${item.fieldId}_${index}`).prop('checked', true);
							}
						});
					});
				}
			}

		} else if (item.componentType === 'HANDLE_SUBMIT_SIGN' || item.componentType === 'HANDLE_REVIEW_SIGN') {
			// 	设置样式
			setState($dom, item);
			if (item?.emptyValue) {
				// 当前为空值
				$(`#${item.fieldId}`).html(`${item.value}`)
				return
			}
			if (!item.value) {
				$(`#${item.fieldId}`).html(``)
				return
			}
			let img = $('<img>', {
				src: item.value
			});
			// 设置 img 的样式
			img.css({
				height: '48px',
				objectFit: 'cover' // 保持图片的比例
			});
			// $(`#${item.fieldId}`).append(item.value ? img : '');
			// 如果 $(`#${item.fieldId}`) 有子元素，则删除所有子元素，再添加 img
			if ($(`#${item.fieldId}`).children().length > 0) {
				$(`#${item.fieldId}`).empty().append(item.value ? img : '');
			} else {
				$(`#${item.fieldId}`).append(item.value ? img : '');
			}
			// 如果 img 的宽度大于 $(`#${item.fieldId}` 的宽度，则设置 img 的宽度为 $(`#${item.fieldId}` 的宽度)
			img.on('load', function () {
				if (img.width() > $(`#${item.fieldId}`).width()) {
					img.css({
						width: '100%'
					});
				}
			});
		} else if (item.componentType === 'PHOTO') {
			setPhotoValue(item);
		} else if (["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(item.originalComponentType)) {
			try {
				const list = JSON.parse(item.value);
				const tableList = item.componentDetail.tableList;
				const rowNum = item.componentDetail.rowNum;
				const tableId = item.fieldId;
				const $table = $(`#${tableId}`);

				// 获取现有表格的行数（排除表头）
				let existingRows = $table.find('tr').length - 1; // 排除表头
				if (existingRows > rowNum) {
					$table.find('tr').each((index, item) => {
						if (index > rowNum) {
							$(item).remove();
						}
					});
				}
				existingRows = $table.find('tr').length - 1; // 排除表头
				// 根据 item.value 的长度与 rowNum 对比，若需要添加行则增加行
				if (list.length > existingRows) {
					// 需要增加的行数
					const rowsToAdd = list.length - existingRows;
					
					// 获取现有的一行（用于复制样式）
					const $sampleRow = existingRows > 0 ? $table.find('tr').eq(1) : $table.find('tr').eq(0); // 第二行是数据行（排除表头）
					
					for (let i = 0; i < rowsToAdd; i++) {
						const newRow = $sampleRow.clone(); // 克隆现有行以保持样式
						newRow.find('td').each(function() {
							$(this).empty(); // 清空克隆行中的数据
						});
						$table.append(newRow); // 将新行添加到表格
					}
				}
				// 填充数据到对应的 td（从第二行开始）
				list.forEach((itemData, rowIndex) => {
					const row = $table.find('tr').eq(rowIndex + 1); // 跳过表头，获取数据行
					tableList.forEach((col, colIndex) => {
						// 通过 colData 来获取对应数据并填充
						const value = itemData[col.colData];
						// 找到 这个 tr 中的 colIndex 个 td 元素 填充数据
						row.find('td').eq(colIndex).text(value);
					});
				});
			} catch (error) {
				// 如果失败， 填入空值
				const rowNum = item.componentDetail.rowNum;
				const tableId = item.fieldId;
				const $table = $(`#${tableId}`);
			
				// 清除表格中的数据, 并排除表头, 从第二行开始, 如果表格中的行数大于 rowNum, 则删除多余的行
				$table.find('tr').each((index, item) => {
					if (index > 0) {
						$(item).find('td').each((index, item) => {
							$(item).text('');
						});
					}
				});
				if ($table.find('tr').length > rowNum + 1) {
					$table.find('tr').each((index, item) => {
						if (index > rowNum) {
							$(item).remove();
						}
					});
				}
			}
		} else if(item.componentType === 'EQUIPMENT_DATA_DRAW_LIST'){
			if(item?.value == item.nullValue){
				// 当前为空值
				$(`#${item.fieldId}`).html(`${item.value}`)
				return
			}
			if(!item.value){
				$(`#${item.fieldId}`).html(``)
				return
			}
			$(`#${item.fieldId}`).html(``)
			const data = JSON.parse(item?.value)
			let img = $('<img>', {
				src: baseUrl + data.url
			});
			// 设置 img 的样式
			img.css({
				width: '100%',
				objectFit: 'cover' // 保持图片的比例
			});
			$(`#${item.fieldId}`).append(item.value ? img : '');
			let userMsg = $(`
				<div style="line-height: normal;display: flex;align-items: center;justify-content: space-between;flex-wrap: wrap;">
					<div>${allText['设备信息']}：${data.equipmentInfo}</div>
					<div>${allText['设备数据']}：${data.equipmentData}</div>
					<div>${allText['采集人']}：${data.acquisitionUser}</div>
					<div>${allText['采集时间']}：${data.acquisitionTime}</div>
				</div>	
			`)
			$(`#${item.fieldId}`).append(userMsg);
		} else if(allButtonComponent.includes(item.originalComponentType)){
      
    }else {
			$dom.val(item.value);
			// 设置textarea高度
			textareaAutoHeight($dom);
		}
		// 	设置样式
		setState($dom, item);
		// 设置组件readonly
		setTextareaReadonly($dom, item);
	}
};

// 设置照片组件的值
const setPhotoValue = (item) => {
	if (item?.emptyValue) {
		// 当前为空值
		$(`#${item.fieldId}`).html(`${item.value}`)
		return
	}
	if (!item?.value) {
		$(`#${item.fieldId}`).html('')
		return
	}
	if (item?.value == '[]') {
		$(`#${item.fieldId}`).html('')
		return
	}
	$(`#${item.fieldId}`).html('')
	const list = JSON.parse(item?.value)
	list?.map((photoItem) => {
		let img = $('<img>', {
			src: baseUrl + photoItem.path
		});
		// 设置 img 的样式
		img.css({
			width: '100%',
			objectFit: 'cover' // 保持图片的比例
		});
		$(`#${item.fieldId}`).append(item.value ? img : '');
		let userMsg = $(`
			<div style="line-height: normal;">
				<div>${allText['取证人']}：${photoItem.createUsername}</div>
				<div>${allText['取证时间']}：${photoItem.createTime}</div>
				<div>${allText['备注']}：${photoItem.remark || '-'}</>
			</div>	
		`)
		$(`#${item.fieldId}`).append(userMsg);
	})
}

// 设置组件的背景为红色
const setComponentBackground = (item) => {
	const component = JSON.parse(decryptedString(item));
	const $dom = getDom(component);
	if ($dom.length > 0) {
		// 	设置样式
		$dom.removeClass('unusual');
		$dom.addClass(component.state);
	}
};

// textarea高度自动
function textareaAutoHeight($dom) {
	// 动态设置textarea高度,高度根据内容自适应
	$dom.height('auto');
	let scrollHeight = $dom.prop('scrollHeight');
	$dom.height(scrollHeight - 10);
}

// H5页面设置组件的高度
function setComponentsHeight(data) {
	let list = JSON.parse(decryptedString(data));
	// 排除特殊组件
	const specialArr = ['HANDLE_SUBMIT_SIGN', 'HANDLE_REVIEW_SIGN', 'PHOTO'];
	list = list.filter(item => !specialArr.includes(item.componentType));
	list.forEach(item => {
		const $dom = getDom(item);
		if ($dom.length) {
			textareaAutoHeight($dom);
		}
	});
}

// 数字校验函数（需要支持科学计数法）
function isValidNumber(str) {
	// 这个正则表达式匹配普通数字和科学计数法
	const regex = /^[-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?$/;
	return regex.test(str);
}

function filterNumber(str) {
	// 创建一个空字符串来存储结果
	let result = '';
	// 遍历输入字符串的每个字符
	for (let i = 0; i < str.length; i++) {
		// 获取当前字符
		let char = str[i];
		// 如果当前字符是数字，就将它添加到结果字符串中
		if (char >= '0' && char <= '9' || char == '.' || char === '+' || char === '-' || char === 'e' || char === 'E') {
			result += char;
		}
	}

	// 返回结果字符串
	return result;
}

let currentPage = 1;
let currentCopyItem = null;

// 渲染分页
function renderPagination(data) {
	const params = JSON.parse(data);
	currentPage = params.current;
	currentCopyItem = params.currentCopyItem;
	renderCurrentPage();
}

// 渲染当前页
function renderCurrentPage() {
	// 显示隐藏已作废
	showHiddenObsolete(currentCopyItem && currentCopyItem.discard);

	// 显示隐藏复制
	showHiddenCopy(currentPage !== 1);
}

// 显示隐藏复制
function showHiddenCopy(flag) {
	if (flag) {
		$('.copy-class').show();
	} else {
		$('.copy-class').hide();
	}
}

// 显示隐藏已作废
function showHiddenObsolete(flag) {
	if (flag) {
		$('.obsolete-class').show();
	} else {
		$('.obsolete-class').hide();
	}
}

// 设置模板的放大缩小
function setTemplateScale(size) {
	const pattern = size == 1 ? 794 : 1123
	const maxWidth = $('#form-box').css('width').split('px')[0] * 1
	$('#form-box').children().each((index, item) => {
		let size = $(item).attr('size') || 1
		let newWidth = maxWidth * size - 10
		$(item).css({
			'width': newWidth + 'px'
		})
	})
}

// 隐藏页码
function removePageNo() {
	$('.pageno_content').each((index, item) => {
		item.style.visibility = 'hidden'
	})
}

// 设置按钮文字
function setBtnText(data) {
	const arr = JSON.parse(data);
	arr && arr.forEach(item => {
		$(`.${item.key}`).text(item.text);
	});
}


// 保存当前后台路径前缀
function saveUrl(url) {
	baseUrl = url
}

// 跳转到制定组件为止
function showComponentById(id) {
	const height = $(window).height()
	var offset = $(`#${id}`).offset().top;
	if (offset < height - 20) {
		// 在第一页
		$(window).scrollTop(0)
	} else {
		$(window).scrollTop(offset)
	}
}