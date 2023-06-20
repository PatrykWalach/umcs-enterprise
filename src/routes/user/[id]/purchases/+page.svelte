<script lang="ts">
	import { PurchaseStatus } from '$houdini';
	import { isNotNull } from '$lib/isNotNull';
	import Pagination from '../../../purchase/[id]/Pagination.svelte';

	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ PurchasesQuery } = data);
</script>

{#if $PurchasesQuery.data?.node?.__typename === 'User'}
	<main class="grid gap-2">
		<div class="card bg-base-200">
			<div class="card-body">
				<table class="table-sm table">
					<thead>
						<tr>
							<th>Nr. zamówienia</th>
							<th>Data złożenia</th>
							<th>Status</th>
							<th>Wartość</th>
						</tr>
					</thead>
					{#each $PurchasesQuery.data.node.purchases?.edges
						?.map((edge) => edge?.node)
						.filter(isNotNull) ?? [] as purchase (purchase.id)}
						<tr>
							<th>
								<a
									href="/purchase/{purchase.id}"
									class="badge {purchase.status
										? {
												[PurchaseStatus.SENT]: 'badge-accent',
												[PurchaseStatus.PAID]: 'badge-secondary',
												[PurchaseStatus.MADE]: 'badge-primary'
										  }[purchase.status]
										: ''}"
								>
									#{purchase.databaseId}
								</a>
							</th>

							<td>
								{#if purchase.createdAt}
									<time datetime={purchase.createdAt.toISOString()}>
										{new Intl.DateTimeFormat(undefined, {
											timeStyle: 'medium',
											dateStyle: 'short'
										}).format(purchase.createdAt)}
									</time>
								{/if}
							</td>
							<td>
								{purchase.status}
							</td>
							<td>
								{purchase.price?.formatted}
							</td>
						</tr>
					{/each}
				</table>
			</div>
		</div>
		<div>
			<div class="mx-auto">
				<Pagination pageInfo={$PurchasesQuery.data.node.purchases?.pageInfo} />
			</div>
		</div>
	</main>
{/if}
