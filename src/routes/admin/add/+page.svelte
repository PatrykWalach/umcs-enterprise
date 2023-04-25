<script lang="ts">
	import { enhance } from '$app/forms';
	import type { ActionData } from './$types';

	export let form: ActionData;

	$: isSubmitting = (void form, false);
</script>
<main>

	<form method="post" aria-labelledby="add-book" encType="multipart/form-data" use:enhance class="grid grid-cols-3">
		<fieldset disabled={isSubmitting} class="">
			<legend id="add-book">Add book</legend>
			<div class="form-control">
				<label class="label" for="title"><span class="label-text">Title</span></label>
				<input
				required
				class="input-bordered input"
				id="title"
				type="text"
				value={form?.data?.title ?? ''}
				name="title"
				/>
				<label class="label" for="title">
					<span class="label-text-alt text-error">{form?.errors?.title?._errors.at(0) ?? ''}</span>
				</label>
			</div>
			<div class="form-control">
				<label class="label" for="author"><span class="label-text">Author</span></label>
				<input
				required
				class="input-bordered input"
				id="author"
				type="text"
				value={form?.data?.author ?? ''}
				name="author"
				/>
				<label class="label" for="title">
					<span class="label-text-alt text-error">{form?.errors?.author?._errors.at(0) ?? ''}</span>
				</label>
			</div>
			<div class="form-control">
			<label class="label" for="price"><span class="label-text">Price</span></label>
			<input
			required
			class="input-bordered input"
			id="price"
			type="number"
			min="0"
			step="0.01"
			value={form?.data?.price ?? ''}
			name="price"
			/>
			<label class="label" for="title">
				<span class="label-text-alt text-error">{form?.errors?.price?._errors.at(0) ?? ''}</span>
			</label>
		</div>
		<div class="form-control">
			<label class="label" for="releasedAt"><span class="label-text">Release date</span></label>
			<input
			required
			class="input-bordered input"
			id="releasedAt"
			type="date"
			value={form?.data?.releasedAt ?? ''}
			name="releasedAt"
			/>
			<label class="label" for="title">
				<span class="label-text-alt text-error">
					{form?.errors?.releasedAt?._errors.at(0) ?? ''}
				</span>
			</label>
		</div>
		<div class="form-control">
			<label class="label" for="cover"><span class="label-text">Cover</span></label>
			<input
			required
			class="file-input-bordered file-input"
			id="cover"
			type="file"
			name="cover"
			accept="image/*"
			/>
			<label class="label" for="title">
				<span class="label-text-alt text-error">{form?.errors?.cover?._errors.at(0) ?? ''}</span>
			</label>
		</div>
		<div class="form-control mt-6">
			<button type="submit" class="btn-primary btn">Submit</button>
			<button type="reset" class="btn">Reset</button>
		</div>
	</fieldset>
</form>

</main>