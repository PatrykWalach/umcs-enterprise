<script lang="ts">
	import { enhance } from '$app/forms';
	import { page } from '$app/stores';
	import Book from '../../Book.svelte';

	import type { PageData } from './$types';

	export let data: PageData;

	$: url = new URL($page.url);
</script>

<div class="bg-base-200 p-8">
	<div class="card card-side bg-base-100 shadow-xl">
		<div class="card-body grid grid-cols-3 justify-between gap-16 p-16">
			{#if data.node?.cover}
				<figure>
					<img
						loading="lazy"
						class="h-auto w-full mix-blend-darken"
						src={data.node.cover?.url}
						width={data.node.cover?.width}
						height={data.node.cover?.height}
						alt=""
					/>
				</figure>
			{/if}
			<div>
				{data.node.synopsis}
			</div>
			<div>
				<div class="card-title line-clamp-2" title={data.node?.title}>
					<a href="/book/{data.node.id}" class="link-hover link">{data.node?.title}</a>
				</div>
				{#if data.node?.author}
					<div class="truncate">
						{data.node.author}
					</div>
				{/if}
				<div>
					<!-- <div class="">
            {data?.price}
          </div> -->
					<form method="post" use:enhance>
						<button type="submit" class="btn-primary btn w-full">do koszyka</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	{#if data?.node.recommended?.edges?.length}
		<h2 class="p-8 text-3xl">Frequently bought together with</h2>
		<ol class="grid grid-cols-5 gap-8 px-8">
			{#each data.node.recommended.edges ?? [] as book (book?.node?.id)}
				{#if book?.node}
					<li class="contents">
						<Book book={book.node} />
					</li>
				{/if}
			{/each}
		</ol>
	{/if}
</div>

<slot />
