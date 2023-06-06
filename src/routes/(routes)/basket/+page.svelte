<script lang="ts">
	import { enhance } from '$app/forms';
	import { isNotNull } from '$lib/isNotNull';
	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ BasketQuery } = data);
</script>

<svelte:head>
	<title>Cart</title>
</svelte:head>

<main class="bg-base-200">
	<div class="flex justify-center pt-4">
		<ul class="steps">
			<li class="step-primary step">Register</li>
			<li class="step-primary step">Choose delivery</li>
			<li class="step">Pay</li>
			<li class="step">Receive Product</li>
		</ul>
	</div>

	<div class="grid gap-2 p-2 sm:gap-4 sm:p-4 lg:grid-cols-3">
		<div class="card card-compact bg-base-100 lg:col-span-2">
			<div class="card-body">
				<table class="">
					{#each $BasketQuery.data?.basket?.books?.edges?.filter(isNotNull) ?? [] as edge (edge.node.id)}
						{#if true}
							<tr class="p-2">
								<td class="p-2">
									<figure>
										<img
											loading="lazy"
											class="h-auto w-16 mix-blend-darken"
											srcset={edge.node.covers
												?.filter(isNotNull)
												.map((cover) => `${cover.url} ${cover.width}w`)
												.join(', ')}
											sizes="(min-width: 1335px) 410.6666666666667px, (min-width: 992px) calc(calc(100vw - 88px) / 3), (min-width: 768px) calc(calc(100vw - 64px) / 2), 100vw"
											alt=""
										/>
									</figure>
								</td>
								<td class="p-2">
									<div class="">
										<h2 class="line-clamp-2 text-lg">
											<a href="/book/{edge.node.id}" class="link-hover link">{edge.node.title}</a>
										</h2>
										<div class="">
											{edge.node.author}
										</div>
									</div>
								</td>
								<td class="hidden p-2 xl:table-cell">
									<form action="" class="" method="POST" use:enhance>
										<input type="hidden" value={edge.node.id} name="id" />
										<div class="join">
											<button class="btn-outline join-item btn cursor-default" formaction="/basket?/unbasket_book">
												-
											</button>
											<div class="btn-outline join-item btn border-x-0">{edge.quantity}</div>
											<button class="btn-outline join-item btn cursor-default" formaction="/basket?/basket_book">
												+
											</button>
										</div>
									</form>
								</td>
								<td class="p-2">{edge.price?.formatted}</td>
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
		<div class="card flex-1 bg-base-100">
			<div class="card-body">
				<button class="btn-primary btn cursor-default" type="button">Login</button>
				<button class="btn-accent btn cursor-default" type="button">Register</button>
				<button class="btn-ghost btn cursor-default" type="button">Order without account</button>
			</div>
		</div>
	</div>
</main>
