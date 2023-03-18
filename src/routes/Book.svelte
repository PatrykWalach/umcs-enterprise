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

<div class="card bg-base-100 shadow-xl">
	{#if data?.cover}
		<figure>
			<img
				loading="lazy"
				class="h-auto w-full mix-blend-darken"
				src={data.cover?.url}
				width={data.cover?.width}
				height={data.cover?.height}
				alt=""
			/>
		</figure>
	{/if}
	<div class="card-body justify-between gap-4">
		<div>
			<div class="card-title line-clamp-2" title={data?.title}>
				<a href="/book/{data.id}" class="link-hover link">{data?.title}</a>
			</div>
			{#if data?.author}
				<div class="truncate">
					{data.author}
				</div>
			{/if}
		</div>
		<div>
			<!-- <div class="">
				{data?.price}
			</div> -->
			<form method="post" use:enhance>
				<button type="submit" class="btn-primary btn w-full">do koszyka</button>
			</form>
		</div>
	</div>
</div>
