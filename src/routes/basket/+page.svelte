<script lang="ts">
	import { enhance } from '$app/forms';
	import { isNotNull } from '$lib/isNotNull';
	import type { PageData } from './$types';

	export let data: PageData;
</script>

<div class="bg-base-200">
	<div class="flex justify-center pt-4">
		<ul class="steps">
			<li class="step-primary step">Register</li>
			<li class="step-primary step">Choose delivery</li>
			<li class="step">Pay</li>
			<li class="step">Receive Product</li>
		</ul>
	</div>

	<div class="grid gap-2 p-2 sm:gap-4 sm:p-4 lg:grid-cols-2">
		<div class="card card-compact bg-base-100">
			<div class="card-body">
				<div class="grid grid-cols-[auto_5fr_auto_1fr] place-items-center justify-items-center">
					<ul class="contents">
						{#each data.BasketQuery.basket?.books?.edges ?? [] as edge (edge?.node?.id)}
							{#if edge}
								<li class="contents">
									<div class="">
										<figure>
											<img
												loading="lazy"
												class="h-auto w-16 mix-blend-darken"
												srcset={edge.node?.covers
													?.filter(isNotNull)
													.map((cover) => `${cover.url} ${cover.width}w`)
													.join(', ')}
												sizes="(min-width: 1335px) 410.6666666666667px, (min-width: 992px) calc(calc(100vw - 88px) / 3), (min-width: 768px) calc(calc(100vw - 64px) / 2), 100vw"
												alt=""
											/>
										</figure>
									</div>
									<div class="truncate">
										<div class=" text-xl">
											{edge.node?.title}
										</div>
										<div class="">
											{edge.node?.author}
										</div>
									</div>
									<div>
										<form action="" method="post" use:enhance>
											<input type="hidden" value={edge?.node?.id} name="id" />
											<div class="btn-group">
												<button class="btn-outline btn" formaction="/basket?/unbasket_book">
													-
												</button>
												<div class="btn-outline btn border-x-0">{edge?.quantity}</div>
												<button class="btn-outline btn" formaction="/basket?/basket_book">+</button>
											</div>
										</form>
									</div>
									<div>{edge.price?.formatted}</div>
								</li>
							{/if}
						{/each}
					</ul>

					<div class="contents font-semibold">
						<div />
						<div>Total</div>
						<div />
						<div>{data.BasketQuery.basket?.price?.formatted}</div>
					</div>
				</div>
			</div>
		</div>
		<div class="card flex-1 bg-base-100">
			<div class="card-body">
				<button class="btn-primary btn" type="button">Login</button>
				<button class="btn-accent btn" type="button">Register</button>
				<button class="btn-ghost btn" type="button">Order without account</button>
			</div>
		</div>
	</div>
</div>
