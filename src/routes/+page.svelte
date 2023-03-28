<script lang="ts">
	import type { PageData } from './$types';
	import Book from './Book.svelte';

	export let data: PageData;

	function hasNode<T>(edge: { node: T | null } | null): edge is { node: NonNullable<T> } {
		return !!edge?.node;
	}
</script>

<div class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
	<!-- <pre>{JSON.stringify(data)}</pre> -->
	<section class="contents">
		<h2 class="mx-4 mt-4 text-3xl">
			<a href="/books?by=popularity&purchase=desc" class="link-hover link">Bestsellers</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
			{#each data.HomeQuery.popular?.edges?.filter(hasNode) ?? [] as book (book.node.id)}
				<li class="contents">
					<Book book={book.node} />
				</li>
			{/each}
		</ol>
	</section>
	<section class="contents">
		<h2 class="mx-4 mt-4 text-3xl">
			<a href="/books?by=realease_date&purchase=desc" class="link-hover link">New</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
			{#each data.HomeQuery.new?.edges?.filter(hasNode) ?? [] as book (book.node.id)}
				<li class="contents">
					<Book book={book.node} />
				</li>
			{/each}
		</ol>
	</section>
</div>

<slot />
