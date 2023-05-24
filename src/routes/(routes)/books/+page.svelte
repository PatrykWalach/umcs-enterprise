<script lang="ts">
	import Book from '../Book.svelte';
	import type { PageData } from './$types';

	export let data: PageData;

	$: ({ BooksQuery } = data);
</script>

<main class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
	<form method="get" class="flex justify-start gap-2 sm:gap-8">
		<select name="by" id="" class="select">
			<option value="releaseDate" selected>Release date</option>
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
		{#each BooksQuery.books ?? [] as book (book.id)}
			<li class="grid">
				<Book {book} />
			</li>
		{/each}
	</ol>

	<nav class="btn-group mx-auto grid grid-cols-[1fr_auto_1fr] md:grid-cols-[1fr_1fr_auto_1fr_1fr]">
		<a
			class="hidden md:btn {BooksQuery.page.first || 'md:btn-disabled'}"
			href={BooksQuery.page.first &&
				`/books?${new URLSearchParams({
					href: BooksQuery.page.first
				})}`}
			aria-label="Pierwsza strona"
			title="Pierwsza strona"
		>
		<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
			<path stroke-linecap="round" stroke-linejoin="round" d="M18.75 19.5l-7.5-7.5 7.5-7.5m-6 15L5.25 12l7.5-7.5" />
		</svg>
		
		</a>

		<a
			class="btn {BooksQuery.page.prev || 'btn-disabled'}"
			href={BooksQuery.page.prev &&
				`/books?${new URLSearchParams({
					href: BooksQuery.page.prev
				})}`}
			aria-label="Poprzednia strona"
			title="Poprzednia strona"
		><svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
			<path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5L8.25 12l7.5-7.5" />
		</svg>
		
		</a>

		<div class="btn col-start-2 md:col-start-3">
			Strona {BooksQuery.page.info.number + 1} z {BooksQuery.page.info.totalPages}
		</div>

		<a
			class="btn {BooksQuery.page.next || 'btn-disabled'}"
			href={BooksQuery.page.next &&
				`/books?${new URLSearchParams({
					href: BooksQuery.page.next
				})}`}
			aria-label="Następna strona"
			title="Następna strona"
		><svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
			<path stroke-linecap="round" stroke-linejoin="round" d="M8.25 4.5l7.5 7.5-7.5 7.5" />
		</svg>
		
		</a>
		<a
			class="hidden md:btn {BooksQuery.page.last || 'md:btn-disabled'}"
			href={BooksQuery.page.last &&
				`/books?${new URLSearchParams({
					href: BooksQuery.page.last
				})}`}
			aria-label="Ostatnia strona"
			title="Ostatnia strona"
		>
		<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
			<path stroke-linecap="round" stroke-linejoin="round" d="M11.25 4.5l7.5 7.5-7.5 7.5m-6-15l7.5 7.5-7.5 7.5" />
		</svg>
		
		</a>
	</nav>
</main>

<slot />
