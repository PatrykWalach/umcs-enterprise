<script lang="ts">
	import { page } from '$app/stores';
</script>

<main class="hero grid min-h-screen grid-cols-2 bg-base-200">
	<div class="hero-content h-full bg-base-300">
		<div class="text-center md:px-20 lg:text-left">
			<h1 class="font-bold text-5xl">Login now!</h1>
			<p class="py-6">
				Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem
				quasi. In deleniti eaque aut repudiandae et a id nisi.
			</p>
		</div>
		<div class="grid gap-4">
			<form
				method="POST"
				action="http://localhost:8080/login"
				class="card w-full max-w-sm flex-shrink-0 bg-base-100 shadow"
			>
				<fieldset>
					<div class="card-body">
						<label for="" class="label">
							{#if $page.url.searchParams.has('error')}
								<span class="label-text-alt text-error">Invalid username and password.</span>
							{/if}
							{#if $page.url.searchParams.has('logout')}
								<span class="label-text-alt">You have been logged out.</span>
							{/if}
						</label>

						<div class="form-control">
							<label class="label" for="username">
								<span class="label-text">Username</span>
							</label>
							<input
								class="input-bordered input"
								type="text"
								name={'username'}
								id={'username'}
								autocomplete="username"
								required
							/>
						</div>

						<div class="form-control">
							<label class="label" for="password">
								<span class="label-text">Password</span>
							</label>
							<input
								required
								class="input-bordered input"
								type="password"
								name={'password'}
								id={'password'}
								autocomplete="new-password"
							/>
						</div>

						<div class="form-control mt-6">
							<button class="btn-primary btn cursor-default" type="submit">Login</button>
						</div>
					</div>
				</fieldset>
			</form>

			<div class="card-compact card border border-base-content/20">
				<div class="card-body items-center">
					<span class="label label-text-alt block">
						New around here? <a href="/register" class="link">Create an account.</a>
					</span>
				</div>
			</div>
		</div>
	</div>
</main>
