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
		<article class="grid gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
			{#if $BookQuery.data?.node.inBasket}
				<div class="alert alert-success shadow-lg">
					<div>
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
				</div>
			{/if}

			<div class="grid gap-2 sm:grid-cols-4 sm:gap-4">
				<div class="flex flex-col gap-2 md:gap-4">
					<div class="card overflow-hidden bg-base-100">
						<figure class="">
							<img
								loading="lazy"
								class="h-auto w-full mix-blend-darken"
								srcset={$BookQuery.data.node.covers
									?.filter(isNotNull)
									.map((cover) => `${cover.url} ${cover.width}w`)
									.join(', ')}
								sizes="(min-width: 1335px) 410.6666666666667px, (min-width: 992px) calc(calc(100vw - 88px) / 3), (min-width: 768px) calc(calc(100vw - 64px) / 2), 100vw"
								alt=""
							/>
						</figure>
					</div>

					<form method="POST" use:enhance class="grid gap-2 md:gap-4">
						<input type="hidden" name="id" value={$BookQuery.data.node.id} />
						<button type="submit" class="btn-secondary btn-lg btn">To cart</button>
						<button type="submit" class="btn-primary btn-lg btn" formaction="?buy_now">
							Buy now
						</button>
					</form>
				</div>

				<div class="flex flex-col gap-2 sm:col-span-3 sm:gap-4">
					<header class="" aria-label="Base information">
						<div class="card flex-1 bg-base-100 max-sm:card-compact xl:p-6">
							<div class="card-body">
								<h1 class="card-title text-8xl">
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
					<div class="card bg-base-100">
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
					<h2 class="ml-4 text-2xl" id="frequently-bought-together">Frequently bought together</h2>
					<ol class="grid grid-cols-[repeat(auto-fill,minmax(13.75rem,1fr))] gap-2 sm:gap-4">
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
