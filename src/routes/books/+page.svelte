<script lang="ts">
	import { page } from '$app/stores';
	import Book from '../Book.svelte';
	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ BooksQuery } = data);

	$: url = new URL($page.url);
</script>

<main class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
	<form method="get" class="flex justify-end gap-8">
		<select name="by" id="" class="select">
			<option value="realease_date" selected>Release date</option>
			<option value="popularity">Popularity</option>
			<option value="price">Price</option>
		</select>
		<select name="order" id="" class="select">
			<option value="asc">Ascending</option>
			<option value="desc" selected>Descending</option>
		</select>
		<button type="submit" class="btn-secondary btn">sort</button>
	</form>

	<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
		{#each $BooksQuery.data?.books?.edges ?? [] as book (book?.node?.id)}
			{#if book?.node}
				<li class="grid">
					<Book book={book.node} />
				</li>
			{/if}
		{/each}
	</ol>

	<footer class="flex">
		<div class="btn-group grid grid-cols-2">
			{#if $BooksQuery.data?.books?.pageInfo?.hasPreviousPage && $BooksQuery.data?.books.pageInfo.startCursor}
				<a
					class="btn"
					href="/books?{new URLSearchParams({
						by: $page.url.searchParams.get('by') ?? '',
						purchase: $page.url.searchParams.get('purchase') ?? '',
						before: $BooksQuery.data?.books.pageInfo.startCursor
					})}"
				>
					Previous
				</a>
			{/if}
			{#if $BooksQuery.data?.books?.pageInfo?.hasNextPage && $BooksQuery.data?.books.pageInfo.endCursor}
				<a
					class="btn"
					href="/books?{new URLSearchParams({
						by: $page.url.searchParams.get('by') ?? '',
						purchase: $page.url.searchParams.get('purchase') ?? '',
						after: $BooksQuery.data?.books.pageInfo.endCursor
					})}"
				>
					Next
				</a>
			{/if}
		</div>
	</footer>
</main>

<slot />
