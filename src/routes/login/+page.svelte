<script lang="ts">
	import { enhance } from '$app/forms';

	export let form: import('./$houdini').ActionData;
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
				use:enhance
				class="card w-full max-w-sm flex-shrink-0 bg-base-100 shadow-2xl"
			>
				<div class="card-body">
					<div class="form-control">
						<label class="label" for="username">
							<span class="label-text">Username</span>
						</label>
						<input
							class="input-bordered input"
							type="text"
							required
							name="username"
							id="username"
							autocomplete="username"
						/>
						<label class="label" for="username">
							<span class="label-text-alt text-error">
								{form?.username.error || ''}
							</span>
						</label>
					</div>

					<div class="form-control">
						<label class="label" for="password">
							<span class="label-text">Password</span>
						</label>
						<input
							class="input-bordered input"
							type="password"
							required
							name="password"
							id="password"
							autocomplete="current-password"
						/>
					</div>

					<div class="form-control mt-6">
						<button class="btn-primary btn" type="submit">Login</button>
					</div>
				</div>
			</form>

			<div class="card-compact card border border-base-content/20">
				<div class="card-body items-center">
					<span class="label-text-alt label block">
						New around here? <a href="/register" class="link">Create an account.</a>
					</span>
				</div>
			</div>
		</div>
	</div>
</main>
