<script lang="ts">
	import { isNotNull } from '$lib/isNotNull';
	import type { PageData } from './$houdini';
	import Book from './Book.svelte';

	export let data: PageData;

	$: ({ HomeQuery } = data);
</script>

<main class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
	<!-- <pre>{JSON.stringify(data)}</pre> -->
	<section class="grid gap-2 sm:gap-4" aria-labelledby="bestsellers">
		<h2 class="mx-4 mt-4 text-3xl" id="bestsellers">
			<a href="/books?by=popularity&order=desc" class="link-hover link">Bestsellers</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
			{#each $HomeQuery.data?.popular?.edges
				?.map((edge) => edge?.node)
				.filter(isNotNull) ?? [] as book (book.id)}
				<li class="grid">
					<Book {book} />
				</li>
			{/each}
		</ol>
	</section>

	<section class="grid gap-2 sm:gap-4" aria-labelledby="new">
		<h2 class="mx-4 mt-4 text-3xl" id="new">
			<a href="/books?by=realease_date&order=desc" class="link-hover link">New</a>
		</h2>
		<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
			{#each $HomeQuery.data?.new?.edges
				?.map((edge) => edge?.node)
				.filter(isNotNull) ?? [] as book (book.id)}
				<li class="grid">
					<Book {book} />
				</li>
			{/each}
		</ol>
	</section>
</main>

<slot />
