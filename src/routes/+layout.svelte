<script lang="ts">
	import type { LayoutData } from './$types';

	export let data: LayoutData;
</script>

<pre>{JSON.stringify(data)}</pre>
<div class="app">
	{#each data.books?.edges ?? [] as book (book?.node?.id)}
		{#if book?.node?.cover}
			<img
				style="width: 80px; height: auto"
				src={book.node.cover?.url}
				width={book.node.cover?.width}
				height={book.node.cover?.height}
				alt=""
			/>
		{/if}
		{book?.node?.title}
	{/each}
</div>

<slot />
