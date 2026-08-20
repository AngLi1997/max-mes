import { onBeforeMount, onBeforeUnmount } from 'vue'


export function usePageLinsters(eventsObj) {

	onBeforeMount(() => {
		Object.keys(eventsObj).forEach((event) => {
			uni.$on(event, eventsObj[event])
		})
	})
	onBeforeUnmount(() => {
		Object.keys(eventsObj).forEach((event) => {
			uni.$off(event)
		})
	})
}