<script lang="ts">
	import { fragment, graphql, type Book_book } from '$houdini';
	import { isNotNull } from '$lib/isNotNull';

	export let book: Book_book;

	$: data = fragment(
		book,
		graphql(`
			fragment Book_book on Book {
				id
				covers(
					widths: [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1200, 1400, 1600, 1800, 2000]
					transformation: {
						aspectRatio: { width: 3, height: 5 }
						crop: FILL_PAD
						background: AUTO
						gravity: AUTO
						quality: { auto: DEFAULT }
						format: AUTO
					}
				) {
					width @required
					url @required
				}
				price {
					formatted
				}
				title @required
				author
			}
		`)
	);

	// $: data = fragment(
	// 	book,
	// );
</script>

<article class="" aria-labelledby={$data?.id}>
	<figure class="aspect-[3/5] overflow-hidden rounded-xl">
		<img
			loading="lazy"
			class="h-full w-full bg-contain"
			style="background-image: url('{$data?.covers?.filter(isNotNull).at(0)?.url}')"
			srcset={$data?.covers
				?.filter(isNotNull)
				.map((cover) => `${cover.url} ${cover.width}w`)
				.join(', ')}
			sizes="(min-width: 1280px) 25vw, (min-width: 1024px) 33vw, (min-width: 640px) 50vw, 100vw"
			alt=""
		/>
	</figure>
	<div class="mt-4 justify-between gap-4">
		<div>
			<h3 id={$data?.id} class="line-clamp-1 font-medium text-base" title={$data?.title}>
				<a href="/book/{$data?.id}" class="link-hover link">{$data?.title}</a>
			</h3>
			{#if $data?.author}
				<div class="mt-1 line-clamp-1 italic text-sm">
					{$data.author}
				</div>
			{/if}
		</div>
		<div>
			<div class="flex items-center justify-between">
				<input type="hidden" name="id" value={$data?.id} />
				<div class="font-bold text-lg" data-testid="price">
					{$data?.price?.formatted}
				</div>
			</div>
		</div>
	</div>
</article>
