<script lang="ts">
	import { enhance } from '$app/forms';
	import TextField from '$lib/TextField.svelte';
	import { superForm } from 'sveltekit-superforms/client';
	import type { PageData } from './$houdini';

	export let data: PageData;

	const form = superForm(data.form);

	const { delayed, errors } = form;
</script>

<svelte:head><title>Add book</title></svelte:head>
<main>
	<form method="post" encType="multipart/form-data" use:enhance class="grid grid-cols-3">
		<fieldset disabled={$delayed} class="">
			<legend>Add book</legend>

			<label for="" class="label">
				{#each $errors._errors ?? [] as error}
					<span class="label-text-alt text-error">{error}</span>
				{/each}
			</label>

			<TextField {form} field="title">Title</TextField>
			<TextField {form} field="author">Author</TextField>
			<TextField {form} field="price" type="number" step="1">Price in "grosze"</TextField>
			<TextField {form} field="releasedAt" type="date">Release date</TextField>
			<TextField
				{form}
				field="cover"
				type="file"
				accept="image/*"
				class="file-input-bordered file-input"
			>
				Cover
			</TextField>

			<div class="form-control mt-6">
				<button type="submit" class="btn-primary btn cursor-default">Submit</button>
				<button type="reset" class="btn cursor-default">Reset</button>
			</div>
		</fieldset>
	</form>
</main>
