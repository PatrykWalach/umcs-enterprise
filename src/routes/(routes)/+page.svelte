<script lang="ts">
	import type { PageData } from './$types';
	import Book from './Book.svelte';

	export let data: PageData;

	$: ({ HomeQuery } = data);
</script>

<main class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
	<!-- <pre>{JSON.stringify(data)}</pre> -->
	<section class="grid gap-2 sm:gap-4" aria-labelledby="bestsellery">
		<h2 class="mx-4 mt-4 text-3xl" id="bestsellers">
			<a href="/books?by=popularity&order=desc" class="link-hover link">Bestsellery</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
			{#each HomeQuery?.popular ?? [] as book (book.id)}
				<li class="grid">
					<Book {book} />
				</li>
			{/each}
		</ol>
	</section>

	<section class="grid gap-2 sm:gap-4" aria-labelledby="new">
		<h2 class="mx-4 mt-4 text-3xl" id="new">
			<a href="/books?by=releaseDate&order=desc" class="link-hover link">Nowości</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
			{#each HomeQuery?.new ?? [] as book (book.id)}
				<li class="grid">
					<Book {book} />
				</li>
			{/each}
		</ol>
	</section>
</main>

<slot />
