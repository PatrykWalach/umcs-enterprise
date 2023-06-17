<script lang="ts">
	import { enhance } from '$app/forms';
	import { PurchaseStatus } from '$houdini';
	import { isNotNull } from '$lib/isNotNull';
	import Summable from '../../(routes)/basket/Summable.svelte';
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

		<div class="grid gap-2 p-2 sm:gap-4 sm:p-4 lg:grid-cols-3">
			<div class="card-compact card bg-base-100 lg:col-span-2">
				<div class="card-body">
					<table class="">
						{#each $PurchaseQuery.data?.node?.books?.edges?.filter(isNotNull) ?? [] as edge (edge.node.id)}
							{#if true}
								<tr class="p-2">
									<Summable {edge} />
								</tr>
							{/if}
						{/each}

						<tr class="p-2">
							<td class="p-2" />
							<td class="p-2"><span class="text-base">Total</span></td>
							<td class="p-2">
								<strong class="font-semibold" data-testid="total">
									{$PurchaseQuery.data?.node?.price?.formatted}
								</strong>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="card flex-1 bg-base-200">
				<div class="card-body">
					<h2 class="card-title">Purchase</h2>
					<dl class="">
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
