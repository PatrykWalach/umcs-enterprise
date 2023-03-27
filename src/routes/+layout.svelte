<script lang="ts">
	import '../app.css';
	import type { LayoutData } from './$types';

	export let data: LayoutData;

	const pluralRules = new Intl.PluralRules();

	$: quantity = data.NavbarQuery.basket?.books?.edges
		?.flatMap((edge) => (edge?.quantity ? [edge.quantity] : []))
		?.reduce((a, b) => a + b, 0);
</script>

<div class="container mx-auto">
	<div class="shadow-xl">
		<nav class="navbar bg-base-100 shadow-xl">
			<div class="flex-1">
				<a class="btn-ghost btn normal-case text-3xl" href="/">Bookstore</a>
			</div>
			<div class="flex flex-none gap-1">
				<div class="dropdown-end dropdown">
					<button type="button" class="btn-ghost btn-square btn" id="cart">
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
								{quantity ?? 0}{data.NavbarQuery.basket?.books?.pageInfo?.hasNextPage ? '+' : ''}
							</span>
						</div>
					</button>
					<div
						tabindex="-1"
						class="card dropdown-content card-compact mt-3 w-52 bg-base-100 shadow"
					>
						<div class="card-body">
							<span class="font-bold text-xl">
								{data.NavbarQuery.basket?.books?.pageInfo?.hasNextPage ? 'Ponad ' : ''}{quantity ||
									'Brak'}
								{(() => ({
									one: 'Książka',
									many: 'Książek',
									few: 'Książki',
									other: null,
									zero: 'Książek',
									two: 'Książki'
								}))()[pluralRules.select(quantity ?? 0)] ?? 'Książek'}
							</span>
							<span class="font-semibold text-info text-lg">
								Total: {data.NavbarQuery.basket?.price?.formatted ?? 0}
							</span>
							<div class="card-actions">
								<a href="/basket" class="btn-primary btn-block btn">to checkout</a>
							</div>
						</div>
					</div>
				</div>
				{#if data.NavbarQuery.viewer?.__typename === 'User'}
					<div class="dropdown-end dropdown">
						<button type="button" class="btn-ghost btn-square avatar btn">
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
						<ul
							tabindex="-1"
							class="dropdown-content menu rounded-box menu-compact mt-3 w-52 bg-base-100 p-2 shadow"
						>
							<li>
								<a class="justify-between" href="">
									Profile
									<span class="badge">New</span>
								</a>
							</li>
							<li><a href="">Settings</a></li>
							<li><a href="/logout">Logout</a></li>
						</ul>
					</div>
				{:else}
					<a href="/login" class="btn-ghost btn">Login</a>
					<a href="/register" class="btn-ghost hidden md:btn">Regsiter</a>
				{/if}
			</div>
		</nav>
		<slot />
	</div>
</div>
