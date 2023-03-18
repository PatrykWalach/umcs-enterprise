<script lang="ts">
	import type { PageData } from './$types';
	import Book from './Book.svelte';

	export let data: PageData;
</script>

<div class="bg-base-200">
	<!-- <pre>{JSON.stringify(data)}</pre> -->
	<h2 class="p-8 text-3xl"><a href="/books?by=popularity&order=desc" class="link link-hover">Popular</a></h2>
	<ol class="grid grid-cols-5 gap-8 p-8">
		{#each data.popular?.edges ?? [] as book (book?.node?.id)}
			{#if book?.node}
				<li class="contents">
					<Book book={book.node} />
				</li>
			{/if}
		{/each}
	</ol>
	<h2 class="p-8 text-3xl"><a href="/books?by=realease_date&order=desc" class="link link-hover">New</a></h2>
	<ol class="grid grid-cols-5 gap-8 p-8">
		{#each data.new?.edges ?? [] as book (book?.node?.id)}
			{#if book?.node}
				<li class="contents">
					<Book book={book.node} />
				</li>
			{/if}
		{/each}
	</ol>
</div>

<slot />
