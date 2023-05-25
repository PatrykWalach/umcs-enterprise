<script lang="ts">
	import { page } from '$app/stores';
	import Book from '../Book.svelte';
	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ BooksQuery } = data);

	$: url = new URL($page.url);
</script>

<main class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
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
		<button type="submit" class=" btn-secondary btn">sort</button>
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

	<nav class="btn-group mx-auto grid grid-cols-2">
		<a
			class="btn {$BooksQuery.data?.books?.pageInfo?.hasPreviousPage || 'btn-disabled'}"
			href={$BooksQuery.data?.books?.pageInfo.hasPreviousPage &&
			$BooksQuery.data?.books?.pageInfo.startCursor
				? `/books?${new URLSearchParams({
						by: $page.url.searchParams.get('by') ?? '',
						order: $page.url.searchParams.get('order') ?? '',
						after: $BooksQuery.data?.books.pageInfo.startCursor
				  })}`
				: undefined}
		>
			Previous
		</a>

		<a
			class="btn {$BooksQuery.data?.books?.pageInfo?.hasNextPage || 'btn-disabled'}"
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
