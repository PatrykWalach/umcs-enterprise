<script lang="ts">
	import TextField from '$lib/TextField.svelte';
	import { superForm } from 'sveltekit-superforms/client';
	import type { PageData } from './$houdini';

	export let data: PageData;

	const form = superForm(data.form);
	const { delayed, errors } = form;
</script>

<main class="hero grid min-h-screen grid-cols-2">
	<div class="hero-content h-full bg-base-300">
		<div class="text-center md:px-20 lg:text-left">
			<h1 class="font-bold text-5xl">Register!</h1>
			<p class="py-6">
				Provident cupiditate voluptatem et in. Quaerat fugiat ut assumenda excepturi exercitationem
				quasi. In deleniti eaque aut repudiandae et a id nisi.
			</p>
		</div>
		<div class="grid gap-4">
			<form
				method="POST"
				use:form.enhance
				class="card w-full max-w-sm flex-shrink-0 bg-base-100 shadow"
			>
				<fieldset disabled={$delayed}>
					<div class="card-body">
						<label for="" class="label">
							{#each $errors._errors ?? [] as error}
								<span class="label-text-alt text-error">{error}</span>
							{/each}
						</label>
						<TextField field="username" {form} autocomplete="username">Username</TextField>
						<TextField field="password" {form} type="password" autocomplete="new-password">
							Password
						</TextField>
						<div class="form-control mt-6">
							<button class="btn-primary btn cursor-default" type="submit">Register</button>
						</div>
					</div>
				</fieldset>
			</form>

			<div class="card-compact card border border-base-content/20">
				<div class="card-body items-center">
					<span class="label label-text-alt block">
						Already have an account? <a href="/login" class="link">Login.</a>
					</span>
				</div>
			</div>
		</div>
	</div>
</main>
