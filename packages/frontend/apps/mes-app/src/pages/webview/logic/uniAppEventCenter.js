import { eventsMap } from "./fn/webViewEventCallbacks.js"

export function executionWebViewEvent(params) {
	try {
		eventsMap.get(params.data.type) && eventsMap.get(params.data.type)(params)
	} catch (e) {
		// setTimeout(() => {
		// 	executionWebViewEvent(params)
		// }, 200)
	}
}