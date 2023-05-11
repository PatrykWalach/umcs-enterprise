<script lang="ts">
	import { enhance } from '$app/forms';
	import { isNotNull } from '$lib/isNotNull';
	import '../app.css';
	import type { LayoutData } from './$houdini';

	export let data: LayoutData;

	const pluralRules = new Intl.PluralRules();

	$: ({ NavbarQuery } = data);
</script>

<div class="container mx-auto">
	<div class="">
		<header class="navbar bg-base-100 shadow-xl">
			<div class="flex-1">
				<a
					class="btn-primary btn-ghost btn flex gap-2 normal-case text-3xl max-sm:btn-square"
					href="/"
				>
					<svg
						xmlns="http://www.w3.org/2000/svg"
						fill="none"
						viewBox="0 0 24 24"
						stroke-width="1.5"
						stroke="currentColor"
						class="h-6 w-6 sm:h-8 sm:w-8"
					>
						<path
							stroke-linecap="round"
							stroke-linejoin="round"
							d="M12 6.042A8.967 8.967 0 006 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 016 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 016-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0018 18a8.967 8.967 0 00-6 2.292m0-14.25v14.25"
						/>
					</svg>

					<div class="hidden sm:block">Bookstore</div>
				</a>
			</div>
			<nav class="flex flex-none gap-1">
				<div class="dropdown-end dropdown">
					<button type="button" aria-label="Show cart total" class="btn-ghost btn-square btn">
						<div class="indicator">
							<svg
								xmlns="http://www.w3.org/2000/svg"
								fill="none"
								viewBox="0 0 24 24"
								stroke-width="1.5"
								stroke="currentColor"
								class="h-5 w-5"
							>
								<path
									stroke-linecap="round"
									stroke-linejoin="round"
									d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z"
								/>
							</svg>
							<span class="badge badge-sm indicator-item">
								{$NavbarQuery.data?.basket?.quantity ?? 0}
							</span>
						</div>
					</button>
					<div
						tabindex="-1"
						class="card dropdown-content card-compact mt-3 w-52 bg-base-100 shadow-xl"
					>
						<div class="card-body grid gap-4">
							<span class="font-bold text-xl">
								{$NavbarQuery.data?.basket?.quantity || 'Brak'}
								{(() => ({
									one: 'Książka',
									many: 'Książek',
									few: 'Książki',
									other: null,
									zero: 'Książek',
									two: 'Książki'
								}))()[pluralRules.select($NavbarQuery.data?.basket?.quantity ?? 0)] ?? 'Książek'}
							</span>
							<ul class="grid gap-4">
								{#each $NavbarQuery.data?.basket?.books?.edges
									?.map((edge) => edge?.node)
									.filter(isNotNull) ?? [] as node (node.id)}
									<li class="grid grid-cols-3 gap-2">
										<figure>
											<img
												loading="lazy"
												class="h-auto w-16 mix-blend-darken"
												srcset={node.covers
													?.filter(isNotNull)
													.map((cover) => `${cover.url} ${cover.width}w`)
													.join(', ')}
												sizes="(min-width: 1335px) 410.6666666666667px, (min-width: 992px) calc(calc(100vw - 88px) / 3), (min-width: 768px) calc(calc(100vw - 64px) / 2), 100vw"
												alt=""
											/>
										</figure>
										<article class="col-span-2">
											<h4><a href="/book/{node.id}" class="link-hover link">{node.title}</a></h4>
										</article>
									</li>
								{/each}
							</ul>
							<div class="card-actions">
								<span class="font-semibold text-info text-lg">
									Total: <strong>{$NavbarQuery.data?.basket?.price?.formatted ?? 0}</strong>
								</span>
								<a href="/basket" class="btn-primary btn-block btn">To checkout</a>
							</div>
						</div>
					</div>
				</div>
				{#if $NavbarQuery.data?.viewer?.__typename === 'User'}
					<div class="dropdown-end dropdown">
						<button type="button" aria-label="Show menu" class="btn-ghost btn-square avatar btn">
							<!--
			 <div class="w-10 rounded-full">
						 <img src="/images/stock/photo-1534528741775-53994a69daeb.jpg" />
					 </div>
					 -->
							<svg
								xmlns="http://www.w3.org/2000/svg"
								fill="none"
								viewBox="0 0 24 24"
								stroke-width="1.5"
								stroke="currentColor"
								class="h-6 w-6"
							>
								<path
									stroke-linecap="round"
									stroke-linejoin="round"
									d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"
								/>
							</svg>
						</button>
						<form action="/logout" method="POST" use:enhance>
							<ul
								tabindex="-1"
								class="dropdown-content menu rounded-box menu-compact mt-3 w-52 bg-base-100 p-2 shadow-xl"
							>
								<li>
									<a class="justify-between" href="">
										Profile
										<span class="badge">New</span>
									</a>
								</li>
								<li><a href="">Settings</a></li>
								<li>
									<button type="submit">Logout</button>
								</li>
							</ul>
						</form>
					</div>
				{:else}
					<a href="/login" class="btn-ghost btn">Login</a>
					<a href="/register" class="btn-ghost btn">Register</a>
				{/if}
			</nav>
		</header>
		<slot />
	</div>
</div>
