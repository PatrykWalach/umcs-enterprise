<script lang="ts">
	import type { LayoutData } from './$types';

	export let data: LayoutData;
</script>

<pre>{JSON.stringify(data)}</pre>
<div class="app">
	{#each data.books?.edges ?? [] as book (book?.node?.id)}
		{book?.node?.title}
	{/each}
</div>

<slot />
