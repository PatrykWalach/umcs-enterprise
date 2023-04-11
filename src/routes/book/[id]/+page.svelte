<script lang="ts">
	import { enhance } from '$app/forms';
	import { page } from '$app/stores';
	import { isNotNull } from '$lib/isNotNull';
	import Book from '../../Book.svelte';

	import type { PageData } from './$types';

	export let data: PageData;

	$: url = new URL($page.url);
</script>

<main>
	<article
		class="grid grid-cols-1 gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4 md:grid-cols-3 xl:grid-cols-[22.5rem_1fr]"
	>
		<!-- <SupportingPane> -->

		<!-- <div class="card glass flex-1">
		
				</div> -->
		<div class="grid grid-cols-2 items-end gap-2 sm:gap-4 md:row-span-2 md:grid-cols-1">
			<div class="card bg-base-100">
				<figure class="">
					<img
						loading="lazy"
						class="h-auto w-full mix-blend-darken"
						srcset={data.BookQuery.node.covers
							?.filter(isNotNull)
							.map((cover) => `${cover.url} ${cover.width}w`)
							.join(', ')}
						sizes="(min-width: 1335px) 410.6666666666667px, (min-width: 992px) calc(calc(100vw - 88px) / 3), (min-width: 768px) calc(calc(100vw - 64px) / 2), 100vw"
						alt=""
					/>
				</figure>
			</div>

			<form method="post" use:enhance class="grid gap-2 md:gap-4">
				<input type="hidden" name="id" value={data.BookQuery.node.id} />
				<button type="submit" class="btn-secondary btn-lg btn">To cart</button>
				<button type="submit" class="btn-primary btn-lg btn" formaction="?buy_now">Buy now</button>
			</form>
		</div>

		<header class="md:col-span-2" aria-label="Base information">
			<div class="card flex-1 bg-base-100 max-sm:card-compact xl:p-6">
				<div class="card-body">
					<h1 class="card-title text-8xl">
						{data.BookQuery.node?.title}
					</h1>
					{#if data.BookQuery.node?.author}
						<address class="font-medium text-2xl">
							{data.BookQuery.node.author}
						</address>
					{/if}
					<div class="font-medium text-xl">
						{data.BookQuery.node.price?.formatted}
					</div>
				</div>
			</div>
		</header>
		<!-- </SupportingPane>
		
		<SupportingPane> -->

		<div class="md:col-span-2 md:col-start-2 md:row-span-2">
			<!-- {#if data.BookQuery.node.synopsis} -->
			<div class="card mt-2 bg-base-100">
				<div class="card-body">
					<div>
						{data.BookQuery.node.synopsis}
					</div>
				</div>
			</div>
			<!-- {/if} -->
		</div>

		<aside
			class="flex flex-col gap-2 sm:gap-4 md:col-span-3"
			aria-labelledby="frequently-bought-together"
		>
			{#if data?.BookQuery.node.recommended?.edges?.length}
				<h2 class="ml-4 text-2xl" id="frequently-bought-together">Frequently bought together</h2>
				<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
					{#each data.BookQuery.node.recommended.edges
						?.map((edge) => edge?.node)
						.filter(isNotNull) ?? [] as book (book.id)}
						<li class="grid">
							<Book {book} />
						</li>
					{/each}
				</ol>
			{/if}
		</aside>

		<!-- </SupportingPane> -->
	</article>

	<slot />
</main>
