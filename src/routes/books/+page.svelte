<script lang="ts">
	import { page } from '$app/stores';
	import { isNotNull } from '$lib/isNotNull';

	import Book from '../Book.svelte';
	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ BooksQuery } = data);

	$: url = new URL($page.url);
</script>

<main class="grid gap-2 py-2 sm:gap-4 sm:py-4">
	<form method="get" class="flex justify-start gap-2 sm:gap-8">
		<select name="by" id="" class="select">
			<option value="realease_date" selected>Release date</option>
			<option value="popularity">Popularity</option>
			<option value="price">Price</option>
		</select>
		<select name="order" id="" class="select">
			<option value="asc">Ascending</option>
			<option value="desc" selected>Descending</option>
		</select>
		<button type="submit" class=" btn-secondary btn cursor-default">sort</button>
	</form>

	<ol class="grid grid-cols-2 gap-10 gap-x-6 sm:grid-cols-3 xl:grid-cols-4 xl:gap-x-8">
		{#each $BooksQuery.data?.books?.edges?.filter(isNotNull) ?? [] as book (book?.node?.id)}
			<li class="grid">
				<Book book={book.node} />
			</li>
		{/each}
	</ol>

	<nav class="join mx-auto grid grid-cols-2">
		<a
			class="join-item btn {$BooksQuery.data?.books?.pageInfo?.hasPreviousPage || 'btn-disabled'}"
			href={$BooksQuery.data?.books?.pageInfo.hasPreviousPage &&
			$BooksQuery.data?.books?.pageInfo.startCursor
				? `/books?${new URLSearchParams({
						by: $page.url.searchParams.get('by') ?? '',
						order: $page.url.searchParams.get('order') ?? '',
						before: $BooksQuery.data?.books.pageInfo.startCursor
				  })}`
				: undefined}
		>
			Previous
		</a>

		<a
			class="join-item btn {$BooksQuery.data?.books?.pageInfo?.hasNextPage || 'btn-disabled'}"
			href={$BooksQuery.data?.books?.pageInfo?.hasNextPage &&
			$BooksQuery.data?.books.pageInfo.endCursor
				? `/books?${new URLSearchParams({
						by: $page.url.searchParams.get('by') ?? '',
						order: $page.url.searchParams.get('order') ?? '',
						after: $BooksQuery.data?.books.pageInfo.endCursor
				  })}`
				: undefined}
		>
			Next
		</a>
	</nav>
</main>

<slot />
