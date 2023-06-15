<script lang="ts">
	import { enhance } from '$app/forms';
	import { isNotNull } from '$lib/isNotNull';
	import Book from '../../Book.svelte';

	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ BookQuery } = data);
</script>

<svelte:head>
	{#if $BookQuery.data?.node?.__typename === 'Book'}
		<title>{$BookQuery.data.node.title}</title>
		<meta
			name="description"
			content="Written by {$BookQuery.data.node.author}. {$BookQuery.data.node.synopsis}"
		/>
	{/if}
</svelte:head>

{#if $BookQuery.data?.node?.__typename === 'Book'}
	<main class="">
		<article class="grid gap-2 py-2 sm:gap-4 sm:py-4">
			{#if $BookQuery.data?.node.inBasket}
				<div class="alert alert-success">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						class="h-6 w-6 flex-shrink-0 stroke-current"
						fill="none"
						viewBox="0 0 24 24"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
						/>
					</svg>
					<span>In the basket!</span>
				</div>
			{/if}

			<div class="grid gap-2 sm:grid-cols-4 sm:gap-4">
				<div class="flex flex-col gap-2 md:gap-4">
					<div class="card">
						<figure class="aspect-[3/5] overflow-hidden rounded-xl">
							<img
								loading="lazy"
								class="h-full w-full bg-contain"
								style="background-image: url('{$BookQuery.data.node?.covers?.filter(isNotNull).at(0)
									?.url}')"
								srcset={$BookQuery.data.node.covers
									?.filter(isNotNull)
									.map((cover) => `${cover.url} ${cover.width}w`)
									.join(', ')}
								sizes="(min-width: 1280px) 25vw, (min-width: 1024px) 33vw, (min-width: 640px) 50vw, 100vw"
								alt=""
							/>
						</figure>
					</div>

					<form method="POST" use:enhance class="grid gap-2 md:gap-4" aria-label="Actions">
						<input type="hidden" name="id" value={$BookQuery.data.node.id} />
						{#if $BookQuery.data.viewer?.__typename === 'Admin'}
							<button
								type="submit"
								class="btn-error btn-lg btn cursor-default"
								formaction="?/delete"
							>
								Delete
							</button>
						{:else}
							<button type="submit" class="btn-secondary btn-lg btn cursor-default">
								Add to basket
							</button>
							<button
								type="submit"
								class="btn-primary btn-lg btn cursor-default"
								formaction="?/buy_now"
							>
								Buy now
							</button>
						{/if}
					</form>
				</div>

				<div class="flex flex-col gap-2 sm:col-span-3 sm:gap-4">
					<header class="" aria-label="Base information">
						<div class="card flex-1 bg-base-200 max-sm:card-compact xl:p-6">
							<div class="card-body">
								<h1 class="card-title text-7xl">
									{$BookQuery.data.node?.title}
								</h1>
								{#if $BookQuery.data.node?.author}
									<address class="font-medium text-2xl">
										{$BookQuery.data.node.author}
									</address>
								{/if}
								<b class="font-medium text-xl">
									{$BookQuery.data.node.price?.formatted}
								</b>
							</div>
						</div>
					</header>
					<!-- </SupportingPane>
		
		<SupportingPane> -->

					<!-- {#if $BookQuery.data.node.synopsis} -->
					<div class="card bg-base-200">
						<div class="card-body">
							<div>
								{$BookQuery.data.node.synopsis}
							</div>
						</div>
					</div>
					<!-- {/if} -->
				</div>
			</div>

			<aside class="grid gap-2 sm:gap-4" aria-labelledby="frequently-bought-together">
				{#if $BookQuery.data.node.recommended?.edges?.length}
					<h2 class="mt-4 text-3xl" id="frequently-bought-together">Frequently bought together</h2>
					<ol
						class="grid grid-cols-1 gap-10 gap-x-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-8"
					>
						{#each $BookQuery.data.node.recommended?.edges
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
{/if}
