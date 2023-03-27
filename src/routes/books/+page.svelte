<script lang="ts">
	import { page } from '$app/stores';
	import Book from '../Book.svelte';
	import type { PageData } from './$types';

	export let data: PageData;

	$: url = new URL($page.url);
</script>

<div class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
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
		{#each data.BooksQuery.books?.edges ?? [] as book (book?.node?.id)}
			{#if book?.node}
				<li class="contents">
					<Book book={book.node} />
				</li>
			{/if}
		{/each}
	</ol>

	<footer class="flex">
		<div class="btn-group grid grid-cols-2">
			{#if data.BooksQuery.books?.pageInfo?.hasPreviousPage && data.BooksQuery.books.pageInfo.startCursor}
				<a
					class="btn"
					href="/books?{new URLSearchParams({
						by: $page.url.searchParams.get('by') ?? '',
						order: $page.url.searchParams.get('order') ?? '',
						before: data.BooksQuery.books.pageInfo.startCursor
					})}"
				>
					Previous
				</a>
			{/if}
			{#if data.BooksQuery.books?.pageInfo?.hasNextPage && data.BooksQuery.books.pageInfo.endCursor}
				<a
					class="btn"
					href="/books?{new URLSearchParams({
						by: $page.url.searchParams.get('by') ?? '',
						order: $page.url.searchParams.get('order') ?? '',
						after: data.BooksQuery.books.pageInfo.endCursor
					})}"
				>
					Next
				</a>
			{/if}
		</div>
	</footer>
</div>

<slot />
