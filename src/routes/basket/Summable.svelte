<script lang="ts">
	import { fragment, graphql, type Summable_edge } from '$houdini';
	import { isNotNull } from '$lib/isNotNull';

	export let edge: Summable_edge;

	$: data = fragment(
		edge,
		graphql(`
			fragment Summable_edge on SummableBookEdge {
				node @required {
					author
					covers(
						transformation: {
							aspectRatio: { width: 3, height: 5 }
							crop: FILL_PAD
							background: AUTO
							gravity: AUTO
							quality: { auto: DEFAULT }
							format: AUTO
						}
						widths: [
							100
							200
							300
							400
							500
							600
							700
							800
							900
							1000
							1200
							1400
							1600
							1800
							2000
						]
					) {
						url @required
						width @required
					}
					id
					price {
						formatted
					}
					title @required
				}
				price {
					formatted
				}
				quantity
			}
		`)
	);
</script>

{#if $data}
	<article class="grid grid-cols-3 gap-4" aria-labelledby={$data?.node.id}>
		<figure class="col-span-1 aspect-[3/5] overflow-hidden rounded">
			<img
				loading="lazy"
				class="h-full w-full bg-contain bg-no-repeat object-contain object-top"
				style="background-image: url('{$data?.node.covers?.filter(isNotNull).at(0)?.url}')"
				srcset={$data.node.covers
					?.filter(isNotNull)
					.map((cover) => `${cover.url} ${cover.width}w`)
					.join(', ')}
				sizes="(min-width: 1335px) 410.6666666666667px, (min-width: 992px) calc(calc(100vw - 88px) / 3), (min-width: 768px) calc(calc(100vw - 64px) / 2), 100vw"
				alt=""
			/>
		</figure>
		<div class="col-span-2">
			<div class="grid gap-2">
				<div class="">
					<h2 class="line-clamp-2 text-xl xl:line-clamp-1">
						<a href="/book/{$data.node.id}" class="link-hover link" id={$data?.node.id}>
							{$data.node.title}
						</a>
					</h2>
					<div class="text-sm">
						{$data.node.author}
					</div>
					<div class="font-bold text-lg">
						<span>{$data.quantity}</span>
						x
						{$data.node.price?.formatted}
					</div>
				</div>
				<slot />
			</div>
		</div>
	</article>
{/if}
