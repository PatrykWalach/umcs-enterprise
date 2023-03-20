<script lang="ts">
	import { enhance } from '$app/forms';
	import type { PageData } from './$types';

	export let data: PageData;
</script>

<div class="flex gap-2 bg-base-200 p-2 sm:gap-4 sm:p-4">
	<div class="card bg-base-100">
		<div class="card-body">
			<ul class="grid gap-2">
				{#each data.BasketQuery.basket?.books?.edges ?? [] as edge (edge?.node?.id)}
					{#if edge}
						<li>
							<div class="flex gap-2">
								{#if edge.node?.cover}
									<figure>
										<img
											loading="lazy"
											class="h-auto w-16 mix-blend-darken"
											src={edge.node.cover?.url}
											width={edge.node.cover?.width}
											height={edge.node.cover?.height}
											alt=""
										/>
									</figure>
								{/if}
								<div class="flex-1">
									<div class="text-xl">
										{edge.node?.title}
									</div>
									{edge.node?.author}
								</div>
								<form action="" method="post" use:enhance>
									<input type="hidden" value={edge?.node?.id} name="id" />
									<div class="btn-group">
										<button class="btn-outline btn" formaction="/basket?/unbasket_book">-</button>
										<div class="btn-outline btn border-x-0">{edge?.quantity}</div>
										<button class="btn-outline btn" formaction="/basket?/basket_book">+</button>
									</div>
								</form>
							</div>
						</li>
					{/if}
				{/each}
			</ul>
		</div>
	</div>
	<div class="card flex-1 bg-base-100">
		<div class="card-body">
			<div class="card-title">Wartość zamówienia: {data.BasketQuery.basket?.totalPrice}</div>

			<button class="btn-primary btn" type="button">Zaloguj się</button>
			<button class="btn-accent btn" type="button">Stwórz konto</button>
			<button class="btn-ghost btn" type="button">Kup bez logowania</button>
		</div>
	</div>
</div>
