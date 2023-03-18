<script lang="ts">
	import { page } from '$app/stores';
	import Book from '../Book.svelte';
	import type { PageData } from './$types';

	export let data: PageData;

	$: url = new URL($page.url);
</script>

<div class="bg-base-200">
	<form method="get" class="flex justify-end gap-8 p-8">
		<select name="by" id="" class="select">
			<option value="realease_date" selected>Data wydania</option>
			<option value="popularity">Popularność</option>
			<option value="price">Cena</option>
		</select>
		<select name="order" id="" class="select">
			<option value="asc">Rosnąco</option>
			<option value="desc" selected>Malejąco</option>
		</select>
		<button type="submit" class="btn-secondary btn">sort</button>
	</form>

	<ol class="grid grid-cols-5 gap-8 px-8">
		{#each data.books?.edges ?? [] as book (book?.node?.id)}
			{#if book?.node}
				<li class="contents">
					<Book book={book.node} />
				</li>
			{/if}
		{/each}
	</ol>

	<footer class="flex justify-center gap-8 p-8">
		{#if data.books?.pageInfo?.hasPreviousPage && data.books.pageInfo.startCursor}
			<a
				class="btn-wide btn"
				href="/books?{new URLSearchParams({
					by: $page.url.searchParams.get('by') ?? '',
					order: $page.url.searchParams.get('order') ?? '',
					before: data.books.pageInfo.startCursor
				})}"
			>
				previous
			</a>
		{/if}
		{#if data.books?.pageInfo?.hasNextPage && data.books.pageInfo.endCursor}
			<a
				class="btn-wide btn"
				href="/books?{new URLSearchParams({
					by: $page.url.searchParams.get('by') ?? '',
					order: $page.url.searchParams.get('order') ?? '',
					after: data.books.pageInfo.endCursor
				})}"
			>
				next
			</a>
		{/if}
	</footer>
</div>

<slot />
