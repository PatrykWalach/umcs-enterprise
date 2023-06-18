<script lang="ts">
	import { enhance } from '$app/forms';
	import { PurchaseStatus } from '$houdini';
	import { isNotNull } from '$lib/isNotNull';
	import Summable from '../../basket/Summable.svelte';
	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ PurchaseQuery } = data);
</script>

<svelte:head>
	<title>Purchase</title>
</svelte:head>

{#if $PurchaseQuery.data?.node?.__typename === 'Purchase'}
	<main class="">
		<div class="flex justify-center pt-4">
			<ul class="steps">
				<li class="step-primary step">Basket</li>
				<li class="step-primary step">Made</li>
				<li
					class="{$PurchaseQuery.data.node.status === PurchaseStatus.SENT
						? 'step-primary'
						: ''} step"
				>
					Sent
				</li>
			</ul>
		</div>

	<div class="grid gap-2 py-2 sm:gap-4 sm:py-4 lg:grid-cols-3">
		<div class="grid gap-2 lg:col-span-2">
			<div class="card card-compact bg-base-200">
				<div class="card-body">
					<ul class="grid gap-4 sm:grid-cols-2">
						{#each $BasketQuery.data?.basket?.books?.edges?.filter(isNotNull) ?? [] as edge (edge.node.id)}
							<li class="">
									<Summable {edge} />
								</tr>
		
						{/each}

	</ul>
				</div>
			</div>
			<div class="">
				<div class="">
					<nav class="join mx-auto grid grid-cols-2">
						<a
							class="join-item btn {$BasketQuery.data?.basket?.books?.pageInfo?.hasPreviousPage ||
								'btn-disabled'}"
							data-sveltekit-replacestate
							href={$BasketQuery.data?.basket?.books?.pageInfo.hasPreviousPage &&
							$BasketQuery.data?.basket?.books?.pageInfo.startCursor
								? `/basket?${new URLSearchParams({
										before: $BasketQuery.data?.basket?.books.pageInfo.startCursor
								  })}`
								: undefined}
						>
							Previous
						</a>
						<a
							data-sveltekit-replacestate
							class="join-item btn {$BasketQuery.data?.basket?.books?.pageInfo?.hasNextPage ||
								'btn-disabled'}"
							href={$BasketQuery.data?.basket?.books?.pageInfo?.hasNextPage &&
							$BasketQuery.data?.basket?.books.pageInfo.endCursor
								? `/basket?${new URLSearchParams({
										after: $BasketQuery.data?.basket?.books.pageInfo.endCursor
								  })}`
								: undefined}
						>
							Next
						</a>
					</nav>
				</div>
			</div>
		</div>
		<div>
			<div class="card bg-base-200">
				<div class="card-body">
					<h2 class="card-title">Purchase</h2>
					<dl class="">
						<dt class="font-semibold">Total</dt>
						<dd class="font-semibold" data-testid="total">
							<strong>	
								{$BasketQuery.data?.basket?.price?.formatted}
							</strong>	
						</dd>
						<dt class="font-semibold">Status</dt>
						<dd class="" data-testid="status">{$PurchaseQuery.data.node.status}</dd>
						<dt class="font-semibold">Date</dt>
						<dd class="">
							{#if $PurchaseQuery.data.node.createdAt}
								<time datetime={$PurchaseQuery.data.node.createdAt.toISOString()}>
									{new Intl.DateTimeFormat(undefined, {
										timeStyle: 'short',
										dateStyle: 'short'
									}).format($PurchaseQuery.data.node.createdAt)}
								</time>
							{/if}
						</dd>
					</dl>
					{#if $PurchaseQuery.data.viewer?.__typename === 'Admin' && $PurchaseQuery.data.node.status === PurchaseStatus.MADE}
						<form use:enhance method="post" class="mt-auto">
							<button type="submit" class="btn-primary btn w-full cursor-default">Send</button>
						</form>
					{/if}
				</div>
			</div>
		</div>
	</main>
{/if}
