<script lang="ts">
	import { enhance } from '$app/forms';
	import { page } from '$app/stores';
	import Book from '../../Book.svelte';

	import type { PageData } from './$types';

	export let data: PageData;

	$: url = new URL($page.url);
</script>

<div
	class="grid grid-cols-1 gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4 md:grid-cols-3 xl:grid-cols-[22.5rem_1fr]"
>
	<!-- <SupportingPane> -->

	<!-- <div class="card glass flex-1">
		
				</div> -->
	<section class="grid grid-cols-2 items-end gap-2 sm:gap-4 md:row-span-2 md:grid-cols-1">
		<div class="card bg-base-100">
			{#if data.BookQuery.node?.cover}
				<figure class="">
					<img
						loading="lazy"
						class="h-auto w-full mix-blend-darken"
						src={data.BookQuery.node.cover?.url}
						width={data.BookQuery.node.cover?.width}
						height={data.BookQuery.node.cover?.height}
						alt=""
					/>
				</figure>
			{/if}
		</div>

		<div class="grid gap-2 md:gap-4">
			<form method="post" class="contents" use:enhance>
				<button type="submit" class="btn-secondary btn-lg btn">To cart</button>
			</form>
			<form method="post" class="contents" use:enhance>
				<button type="submit" class="btn-primary btn-lg btn">Buy now</button>
			</form>
		</div>
	</section>

	<section class="md:col-span-2">
		<div class="card flex-1 bg-base-100 max-sm:card-compact xl:p-6">
			<div class="card-body">
				<h1 class="card-title text-8xl">
					{data.BookQuery.node?.title}
				</h1>
				{#if data.BookQuery.node?.author}
					<div class="font-medium text-2xl">
						{data.BookQuery.node.author}
					</div>
				{/if}
			</div>
		</div>
	</section>
	<!-- </SupportingPane>

	<SupportingPane> -->

	<section class="md:col-span-2 md:col-start-2 md:row-span-2">
		<!-- {#if data.BookQuery.node.synopsis} -->
		<div class="card mt-2 bg-base-100">
			<div class="card-body">
				<div>
					{data.BookQuery.node.synopsis}
				</div>
			</div>
		</div>
		<!-- {/if} -->
	</section>

	<section class="flex flex-col gap-2 sm:gap-4 md:col-span-3">
		{#if data?.BookQuery.node.recommended?.edges?.length}
			<h2 class="ml-4 text-2xl">Frequently bought together</h2>
			<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
				{#each data.BookQuery.node.recommended.edges
					.slice()
					.concat(data.BookQuery.node.recommended.edges)
					.concat(data.BookQuery.node.recommended.edges) ?? [] as book (book?.node?.id)}
					{#if book?.node}
						<li class="contents">
							<Book book={book.node} />
						</li>
					{/if}
				{/each}
			</ol>
		{/if}
	</section>

	<!-- </SupportingPane> -->
</div>

<slot />
