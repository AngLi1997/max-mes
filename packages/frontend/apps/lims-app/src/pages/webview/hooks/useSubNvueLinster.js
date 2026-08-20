import { onBeforeMount, onBeforeUnmount } from 'vue'


export function useSubNvueLinster(event, callback) {
	onBeforeMount(() => {
		uni.$on(event, callback)
	})
	onBeforeUnmount(() => {
		uni.$off(event)
	})
}