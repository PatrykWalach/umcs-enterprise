<script lang="ts">
	import { enhance } from '$app/forms';
	import { graphql, useFragment, type FragmentType } from '$lib/gql';

	let Book_book = graphql(`
		fragment Book_book on Book {
			id
			cover {
				id
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

<article class="card-compact card bg-base-100 shadow xl:shadow-lg">
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
			<h3 class="card-title line-clamp-3 xl:line-clamp-2" title={data?.title}>
				<a href="/book/{data.id}" class="link-hover link">{data?.title}</a>
			</h3>
			{#if data?.author}
				<div class="truncate">
					{data.author}
				</div>
			{/if}
		</div>
		<div>
			<form method="post" use:enhance class="flex items-center justify-between">
				<input type="hidden" name="id" value={data.id} />
				<div class="text-lg font-bold">
					{data?.price}
				</div>
				<!-- <button type="submit" class="btn-primary btn w-full">do koszyka</button> -->
				<button type="submit" class="btn-ghost btn-square btn">
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
							d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 00-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 00-16.536-1.84M7.5 14.25L5.106 5.272M6 20.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zm12.75 0a.75.75 0 11-1.5 0 .75.75 0 011.5 0z"
						/>
					</svg>
				</button>
			</form>
		</div>
	</div>
</article>
