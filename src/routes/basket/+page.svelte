<script lang="ts">
	import { enhance } from '$app/forms';
	import { page } from '$app/stores';
	import { isNotNull } from '$lib/isNotNull';
	import Pagination from '../purchase/[id]/Pagination.svelte';
	import type { PageData } from './$houdini';
	import Summable from './Summable.svelte';

	export let data: PageData;

	$: ({ BasketQuery } = data);
</script>

<svelte:head>
	<title>Basket</title>
</svelte:head>

<main class="">
	<div class="flex justify-center pt-4">
		<ul class="steps">
			<li class="step">Made</li>
			<li class="step">Paid</li>
			<li class="step">Sent</li>
		</ul>
	</div>

	<div class="grid gap-2 py-2 sm:gap-4 sm:py-4 lg:grid-cols-3">
		<div class="grid gap-2 lg:col-span-2">
			<div class="card card-compact bg-base-200">
				<div class="card-body">
					<ul class="grid gap-4 sm:grid-cols-2">
						{#each $BasketQuery.data?.basket?.books?.edges?.filter(isNotNull) ?? [] as edge (edge.node.id)}
							<li class="">
								<Summable {edge}>
									<form action="" class="" method="post" use:enhance>
										<input type="hidden" value={edge.node.id} name="id" />
										<div class="grid flex-wrap gap-2 sm:flex">
											<button
												class="btn-neutral btn-error btn-sm join-item btn cursor-default"
												formaction="/basket?/unbasket_book&{$page.url.searchParams}"
											>
												<span class="">remove</span>
											</button>
											<button
												class="btn-neutral btn-primary btn-sm join-item btn cursor-default"
												formaction="/basket?/basket_book&{$page.url.searchParams}"
											>
												<span class="">add more</span>
											</button>
										</div>
									</form>
								</Summable>
							</li>
						{/each}
					</ul>
				</div>
			</div>
			<div class="">
				<div class="">
					<Pagination replace pageInfo={$BasketQuery.data?.basket?.books?.pageInfo} />
				</div>
			</div>
		</div>
		<div>
			<div class="card bg-base-200">
				<form use:enhance method="post" class="card-body">
					<h2 class="card-title">Basket</h2>
					<dl class="flex justify-between">
						<dt class="text-base">Total</dt>
						<dd class="font-semibold" data-testid="total">
							<strong>
								{$BasketQuery.data?.basket?.price?.formatted}
							</strong>
						</dd>
					</dl>
					{#if $BasketQuery.data?.viewer}
						<div
							class="tooltip"
							data-tip={!$BasketQuery.data.basket?.quantity ? 'Basket empty' : undefined}
						>
							<button
								disabled={!$BasketQuery.data.basket?.quantity}
								class="btn-primary btn w-full cursor-default"
								type="submit"
								formaction="?/make_purchase"
							>
								Make purchase
							</button>
						</div>
					{:else}
						Login or register to make a purchase
					{/if}
					<div class="divider">Or</div>
					<button class="btn-error btn cursor-default" formaction="?/reset_basket" type="submit">
						Reset
					</button>
				</form>
			</div>
		</div>
	</div>
</main>
