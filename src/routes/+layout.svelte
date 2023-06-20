<script lang="ts">
	import { enhance } from '$app/forms';
	import { page } from '$app/stores';
	import { PUBLIC_CLIENT_ADDRESS, PUBLIC_SERVER_ADDRESS } from '$env/static/public';
	import '../app.css';
	import type { LayoutData } from './$houdini';
	export let data: LayoutData;

	$: ({ NavbarQuery } = data);
</script>

<div class="">
	<div class="mx-auto max-w-2xl px-4 sm:px-6 lg:max-w-7xl lg:px-8">
		<header class="navbar p-0">
			<div class="flex-1">
				<a
					class="btn-ghost btn-primary btn flex gap-2 normal-case text-3xl max-sm:btn-square"
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
				<a class="btn-ghost btn-square btn" href="/basket" aria-label="Go to basket">
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
						<span class="badge badge-neutral badge-sm indicator-item" data-testid="basket-quantity">
							{$NavbarQuery.data?.basket?.quantity ?? 0}
						</span>
					</div>
				</a>

				{#if $NavbarQuery.data?.viewer}
					<div class="dropdown-end dropdown z-10">
						<button
							type="button"
							aria-label="Show menu"
							class="btn-ghost btn-square avatar btn cursor-default"
						>
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
						<form action="/logout" method="post" use:enhance>
							<div
								tabindex="-1"
								class="menu-compact dropdown-content menu rounded-box mt-3 w-52 border border-base-200 bg-base-100 p-2 shadow-xl"
							>
								<ul>
									<li>
										<a href="/user/{$NavbarQuery.data.viewer.id}/purchases">
											Purchases <span class="badge badge-neutral">
												{$NavbarQuery.data.viewer.purchases?.edges?.length ?? 0}
											</span>
										</a>
									</li>
									<li><a href="/user/{$NavbarQuery.data.viewer.id}">Settings</a></li>
									<li>
										<button type="submit" class="px-4 py-2">Logout</button>
									</li>
								</ul>
								{#if $NavbarQuery.data.viewer.__typename === 'Admin'}
									<div class="divider">Admin</div>
									<ul>
										<li>
											<a href="/admin/add">Add book</a>
										</li>
										<li>
											<a href="/admin/users">Users</a>
										</li>
										<li>
											<a href="/admin/purchases">Purchases</a>
										</li>
									</ul>
								{/if}
							</div>
						</form>
					</div>
				{:else}
					<a
						href={`${PUBLIC_SERVER_ADDRESS}/oauth2/authorize?${new URLSearchParams({
							client_id: 'bookstore',
							redirect_uri: `${PUBLIC_CLIENT_ADDRESS}/login/callback`,
							response_type: 'code',
							scope: 'openid profile read write',
							state: $page.url.pathname
						})}`}
						class="btn-ghost btn"
					>
						Login
					</a>
					<a href="/register" class="btn-ghost btn">Register</a>
				{/if}
			</nav>
		</header>
		<slot />
	</div>
</div>
