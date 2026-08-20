// 特殊的数组,非textarea组件
const specialArr = ["RADIO", "RADIO_GROUP", "CHECKBOX", "CHECKBOX_GROUP"];
let baseUrl = "";

const postMessageToUniapp = (type, params = {}) => {
  uni.postMessage({
    data: {
      type,
      ...params,
    },
  });
};

function clickEventCallback(e) {
  const componentType = e.target.getAttribute("componentType");
  if (e.target.id && e.target.id !== "form-box") {
    const $dom = getDom({ fieldId: e.target.id });
    postMessageToUniapp("QUICK_ENTRY_CLICK", {
      id: e.target.id,
    });
  }
}
// 初始化时设置body元素内容
function quickEntryRender(data) {
  $("#form-box").html(decryptedString(data));
  $("#form-box").off("click", clickEventCallback);
  $("#form-box").on("click", clickEventCallback);
}

function getDom(item) {
  return $(`#${item.fieldId}`);
}
// 初始化所有元素
function quickEntryInitElements(data) {
  const dataT = JSON.parse(decryptedString(data));
  if (Array.isArray(dataT)) {
    dataT.forEach((item) => {
      quickEntryInitElement(item);
    });
  } else {
    quickEntryInitElement(dataT);
  }
}

// 初始化单个元素（替换textarea）
function quickEntryInitElement(item) {
  let $dom = getDom(item);
  if (!$dom.length) {
    return;
  }
  replaceChildElement($dom, item);
}

// 替换初始textarea节点
function replaceChildElement($dom, item) {
  const originWidth = $dom.css("width");
  const originHeight = $dom.css("height");
  const newHtml = createNewReplaceElement(item, $dom);
  $dom.replaceWith(newHtml);
  const $newDom = getDom(item);
  // 设置宽高
  if (!["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(item.originalComponentType)) {
		$newDom.css({
			...(item.componentType !== 'RADIO' && item.componentType !== 'CHECKBOX' ? { width: originWidth } : {}),
			height: item.componentType == 'PHOTO' ? '' : originHeight,
			...(item.componentType == 'PHOTO' ? { minHeight: originHeight } : {}),
			lineHeight: originHeight
		});
	}
  setState($newDom, item);
  // 设置readonly属性值
  setTextareaReadonly($newDom, item);
}

// 构建新的textarea元素
function createNewReplaceElement(item, $dom) {
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
    case "HANDLE_SUBMIT_SIGN":
      let handleSubmitSign = `<div id="${
        item.fieldId
      }" class="default" componentType="HANDLE_SUBMIT_SIGN" style="display: inline-block; min-height: 48px;">`;
      return handleSubmitSign + "</div>";
    case "HANDLE_REVIEW_SIGN":
      let handleReviewSign = `<div id="${
        item.fieldId
      }" class="default" componentType="HANDLE_REVIEW_SIGN" style="display: inline-block; min-height: 48px;">`;
      return handleReviewSign + "</div>";
    case "PHOTO":
      return `<div id="${item.fieldId}" class="default" nullValue="${
        item.nullValue
      }" style="display:flex;flex-direction: column;height:auto !important;" componentType="PHOTO"></div>`;
    default:
      return `<textarea class="default" nullValue="${
        item.nullValue
      }" componentType="${item.componentType}" id="${
        item.fieldId
      }" rows="1"></textarea>`;
  }
}

// 设置组件class类名
function setState($dom, item) {
  $dom.removeClass();
  if (item.componentType === "RADIO") {
    $dom.addClass("radio-box");
    if (item.state !== "default" && item.state !== "saved") {
      $dom.addClass(item.state);
    }
    return;
  }
  if (item.componentType === "CHECKBOX") {
    $dom.addClass("checkbox-box");
    if (item.state !== "default" && item.state !== "saved") {
      $dom.addClass(item.state);
    }
    return;
  }
  if (
    item.componentType === "NUMBER" ||
    (item.componentType === "TEXT" && item.state === "default")
  ) {
    $dom.addClass("canInput");
  }
  if (["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(item.originalComponentType)) {
		return ;
	}
  $dom.addClass(item.state);
}

// 设置组件unfilled类
function addUnfilledState(data) {
  const list = JSON.parse(decryptedString(data));
  list.forEach((item) => {
    const $dom = getDom(item);
    if ($dom.length) {
      $dom.removeClass("focus");
      $dom.addClass("unfilled");
    }
  });
}

// 设置readonly属性
function setTextareaReadonly($dom, item) {
  isReadonly = true;
  switch (item.componentType) {
    case "RADIO":
      $dom.find("input").attr("disabled", isReadonly);
      break;
    case "CHECKBOX":
      $dom.find("input").attr("disabled", isReadonly);
      break;
    default:
      $dom.attr("readonly", isReadonly);
      break;
  }
}

// 回显数据组
function quickEntryEchoData(data) {
  const componentValues = JSON.parse(decryptedString(data));
  componentValues.forEach((item) => {
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
    } else if (
      item.componentType === "HANDLE_SUBMIT_SIGN" ||
      item.componentType === "HANDLE_REVIEW_SIGN"
    ) {
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
      let img = $("<img>", {
        src: item.value,
      });
      // 设置 img 的样式
      img.css({
        height: "48px",
        objectFit: "cover", // 保持图片的比例
      });
      // $(`#${item.fieldId}`).append(item.value ? img : '');
      // 如果 $(`#${item.fieldId}`) 有子元素，则删除所有子元素，再添加 img
      if ($(`#${item.fieldId}`).children().length > 0) {
        $(`#${item.fieldId}`)
          .empty()
          .append(item.value ? img : "");
      } else {
        $(`#${item.fieldId}`).append(item.value ? img : "");
      }
      // 如果 img 的宽度大于 $(`#${item.fieldId}` 的宽度，则设置 img 的宽度为 $(`#${item.fieldId}` 的宽度)
      img.on("load", function() {
        if (img.width() > $(`#${item.fieldId}`).width()) {
          img.css({
            width: "100%",
          });
        }
      });
    } else if (item.componentType === "PHOTO") {
      setPhotoValue(item);
    } else if (["EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE"].includes(item.originalComponentType)) {
			try {
				const list = JSON.parse(item.value);
				const tableList = item.componentDetail.tableList;
				const rowNum = item.componentDetail.rowNum;
				const tableId = item.fieldId;
				const $table = $(`#${tableId}`);

				// 获取现有表格的行数（排除表头）
				const existingRows = $table.find('tr').length - 1; // 排除表头

				// 根据 item.value 的长度与 rowNum 对比，若需要添加行则增加行
				if (list.length > rowNum) {
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
    } else {
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
  if (!item?.value) {
    $(`#${item.fieldId}`).html("");
    return;
  }
  if (item?.value === item.nullValue) {
    // 当前为空值
    $(`#${item.fieldId}`).html(`${item.value}`);
    return;
  }
  $(`#${item.fieldId}`).html("");
  const list = JSON.parse(item?.value);
  list?.map((photoItem) => {
    let img = $("<img>", {
      src: baseUrl + photoItem.path,
    });
    // 设置 img 的样式
    img.css({
      width: "100%",
      objectFit: "cover", // 保持图片的比例
    });
    $(`#${item.fieldId}`).append(item.value ? img : "");
    let userMsg = $(`
			<div style="line-height: normal;">
				<div>取证人：${photoItem.createUsername}</div>
				<div>取证时间：${photoItem.createTime}</div>
			<div>	
		`);
    $(`#${item.fieldId}`).append(userMsg);
  });
};

// 设置组件的背景为红色
const setComponentBackground = (item) => {
  const component = JSON.parse(decryptedString(item));
  const $dom = getDom(component);
  if ($dom.length > 0) {
    // 	设置样式
    $dom.removeClass("unusual");
    $dom.addClass(component.state);
  }
};

// textarea高度自动
function textareaAutoHeight($dom) {
  // 动态设置textarea高度,高度根据内容自适应
  $dom.height("auto");
  let scrollHeight = $dom.prop("scrollHeight");
  $dom.height(scrollHeight - 10);
}

// H5页面设置组件的高度
function setComponentsHeight(data) {
  let list = JSON.parse(decryptedString(data));
  // 排除特殊组件
  const specialArr = ["HANDLE_SUBMIT_SIGN", "HANDLE_REVIEW_SIGN", "PHOTO"];
  list = list.filter((item) => !specialArr.includes(item.componentType));
  list.forEach((item) => {
    const $dom = getDom(item);
    if ($dom.length) {
      textareaAutoHeight($dom);
    }
  });
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
    $(".copy-class").show();
  } else {
    $(".copy-class").hide();
  }
}

// 显示隐藏已作废
function showHiddenObsolete(flag) {
  if (flag) {
    $(".obsolete-class").show();
  } else {
    $(".obsolete-class").hide();
  }
}

// 设置模板的放大缩小
function setTemplateScale(size) {
  const pattern = size == 1 ? 794 : 1123;
  const maxWidth =
    $("#form-box")
      .css("width")
      .split("px")[0] * 1;
  $("#form-box")
    .children()
    .each((index, item) => {
      let size = $(item).attr("size") || 1;
      let newWidth = maxWidth * size - 10;
      $(item).css({
        width: newWidth + "px",
      });
    });
}

// 隐藏页码
function removePageNo() {
  $(".pageno_content").each((index, item) => {
    item.style.visibility = "hidden";
  });
}

// 设置按钮文字
function setBtnText(data) {
  const arr = JSON.parse(data);
  arr &&
    arr.forEach((item) => {
      $(`.${item.key}`).text(item.text);
    });
}

// 保存当前后台路径前缀
function saveUrl(url) {
  baseUrl = url;
}
