<script lang="ts">
	import type { PageData } from './$types';
	import Book from './Book.svelte';

	export let data: PageData;
</script>

<div class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
	<!-- <pre>{JSON.stringify(data)}</pre> -->
	<section class="contents">
		<h2 class="mt-4 text-3xl">
			<a href="/books?by=popularity&order=desc" class="link-hover link">Bestsellery</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fit,minmax(220px,1fr))] gap-2 sm:gap-4">
			{#each data.HomeQuery.popular?.edges ?? [] as book (book?.node?.id)}
				{#if book?.node}
					<li class="contents">
						<Book book={book.node} />
					</li>
				{/if}
			{/each}
		</ol>
	</section>
	<section class="contents">
		<h2 class="mt-4 text-3xl">
			<a href="/books?by=realease_date&order=desc" class="link-hover link">Nowości</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fit,minmax(220px,1fr))] gap-2 sm:gap-4">
			{#each data.HomeQuery.new?.edges ?? [] as book (book?.node?.id)}
				{#if book?.node}
					<li class="contents">
						<Book book={book.node} />
					</li>
				{/if}
			{/each}
		</ol>
	</section>
</div>

<slot />
