<script lang="ts">
	import { enhance } from '$app/forms';
	import { isNotNull } from '$lib/isNotNull';
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
			<li class="step-primary step">Basket</li>
			<li class="step">Made</li>
			<li class="step">Sent</li>
		</ul>
	</div>

	<div class="grid gap-2 p-2 sm:gap-4 sm:p-4 lg:grid-cols-3">
		<div class="card card-compact bg-base-100 lg:col-span-2">
			<div class="card-body">
				<table class="">
					{#each $BasketQuery.data?.basket?.books?.edges?.filter(isNotNull) ?? [] as edge (edge.node.id)}
						{#if true}
							<tr class="p-2">
								<Summable {edge}>
									<td class="hidden p-2 xl:table-cell">
										<form action="" class="" method="post" use:enhance>
											<input type="hidden" value={edge.node.id} name="id" />
											<div class="join">
												<button
													class="btn-outline join-item btn cursor-default"
													formaction="/basket?/unbasket_book"
												>
													-
												</button>
												<div class="btn-outline join-item btn border-x-0">{edge.quantity}</div>
												<button
													class="btn-outline join-item btn cursor-default"
													formaction="/basket?/basket_book"
												>
													+
												</button>
											</div>
										</form>
									</td>
								</Summable>
							</tr>
						{/if}
					{/each}

					<tr class="p-2">
						<td class="p-2" />
						<td class="p-2"><span class="text-base">Total</span></td>
						<td class="hidden p-2 xl:table-cell" />
						<td class="p-2">
							<strong class="font-semibold">{$BasketQuery.data?.basket?.price?.formatted}</strong>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="card flex-1 bg-base-200">
			<form use:enhance method="post" class="card-body">
				<h2 class="card-title">Basket</h2>
				{#if $BasketQuery.data?.viewer}
					<button class="btn-primary btn cursor-default" type="submit" formaction="?/make_purchase">
						Make purchase
					</button>
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
</main>
