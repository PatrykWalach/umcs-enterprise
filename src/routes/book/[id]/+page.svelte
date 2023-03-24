<script lang="ts">
	import { enhance } from '$app/forms';
	import { page } from '$app/stores';
	import Book from '../../Book.svelte';

	import type { PageData } from './$types';

	export let data: PageData;

	$: url = new URL($page.url);
</script>

<div class="bg-base-200 p-8">
	<div class="grid grid-cols-3 gap-2">
		<div class="overflow-hidden rounded-3xl bg-base-100 shadow-xl">
			{#if data.node?.cover}
				<figure class="contents">
					<img
						loading="lazy"
						class="h-full w-full object-cover mix-blend-darken"
						src={data.node.cover?.url}
						width={data.node.cover?.width}
						height={data.node.cover?.height}
						alt=""
					/>
				</figure>
			{/if}
		</div>

		<div class="card flex-1 rounded-3xl bg-base-100 shadow-xl">
			<div class="card-body">
				<div class="card-title text-5xl" title={data.node?.title}>
					<a href="/book/{data.node.id}" class="link-hover link">{data.node?.title}</a>
				</div>
				{#if data.node?.author}
					<div class="text-xl">
						{data.node.author}
					</div>
				{/if}

				<div>
					{data.node.synopsis}
				</div>
				<div>
					<div class="grid grid-cols-2">
						<form method="post" class="contents" use:enhance>
							<button type="submit" class="btn-ghost btn">Dodaj do koszyka</button>
						</form>
						<form method="post" class="contents" use:enhance>
							<button type="submit" class="btn-primary btn">Kup teraz</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	{#if data?.node.recommended?.edges?.length}
		<h2 class="p-2 text-3xl sm:p-4">Inni kupowali też</h2>
		<ol class="grid grid-cols-[repeat(auto-fit,minmax(220px,1fr))] gap-2 p-2 sm:gap-4 sm:p-4">
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
