<script lang="ts">
	import { enhance } from '$app/forms';
	import { graphql, useFragment, type FragmentType } from '$lib/gql';

	let Book_book = graphql(`
		fragment Book_book on Book {
			id
			cover {
				height
				url
				width
			}
			price
			title
			author
		}
	`);

	export let book: FragmentType<typeof Book_book>;

	$: data = useFragment(Book_book, book);
</script>

<div class="flex flex-col justify-end gap-4">
	{#if data?.cover}
		<img
			class="w-20 h-20"
			src={data.cover?.url}
			width={data.cover?.width}
			height={data.cover?.height}
			alt=""
		/>
	{/if}
	<div class="text-xl">
		{data?.title}
	</div>
	{#if data?.author}
		<div class="truncate">
			{data.author}
		</div>
	{/if}
	<div class="">
		{data?.price}
	</div>
	<form method="post" use:enhance>
		<button type="submit" class="btn btn-primary w-full">do koszyka</button>
	</form>
</div>
