<script lang="ts">
	import { page } from '$app/stores';
	import { fragment, graphql, type Pagination_pageInfo } from '$houdini';

	export let pageInfo: Pagination_pageInfo | undefined;
	export let replace: true | undefined;

	$: data = fragment(
		pageInfo,
		graphql(`
			fragment Pagination_pageInfo on PageInfo {
				endCursor
				hasNextPage
				hasPreviousPage
				startCursor
			}
		`)
	);
</script>

<nav class="join mx-auto grid grid-cols-2">
	<a
		class="join-item btn {$data?.hasPreviousPage || 'btn-disabled'}"
		data-sveltekit-replacestate={replace}
		href={$data?.hasPreviousPage && $data.startCursor
			? `${$page.url.pathname}?${new URLSearchParams({
					before: $data.startCursor
			  })}`
			: undefined}
	>
		Previous
	</a>
	<a
		data-sveltekit-replacestate={replace}
		class="join-item btn {$data?.hasNextPage || 'btn-disabled'}"
		href={$data?.hasNextPage && $data.endCursor
			? `${$page.url.pathname}?${new URLSearchParams({
					after: $data.endCursor
			  })}`
			: undefined}
	>
		Next
	</a>
</nav>
