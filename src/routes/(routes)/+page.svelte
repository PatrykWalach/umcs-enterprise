<script lang="ts">
	import { isNotNull } from '$lib/isNotNull';
	import type { PageData } from './$houdini';
	import Book from './Book.svelte';

	export let data: PageData;

	$: ({ HomeQuery } = data);
</script>

<main class="grid gap-2 py-2 sm:gap-4 sm:py-4">
	<!-- <pre>{JSON.stringify(data)}</pre> -->
	<section class="grid gap-2 sm:gap-4" aria-labelledby="bestsellers">
		<h2 class="mt-4 text-3xl" id="bestsellers">
			<a href="/books?by=popularity&order=desc" class="link-hover link">Bestsellers</a>
		</h2>
		<ol
			class="grid grid-cols-1 gap-x-6 gap-10 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-8"
		>
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
		<h2 class="mt-4 text-3xl" id="new">
			<a href="/books?by=realease_date&order=desc" class="link-hover link">New</a>
		</h2>
		<ol
			class="grid grid-cols-1 gap-x-6 gap-10 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-8"
		>
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
