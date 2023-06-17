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
						transformation: { quality: { auto: DEFAULT }, format: AUTO }
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

<td class="p-2">
	<figure>
		<img
			loading="lazy"
			class="h-auto w-16 mix-blend-darken"
			srcset={$data.node.covers
				?.filter(isNotNull)
				.map((cover) => `${cover.url} ${cover.width}w`)
				.join(', ')}
			sizes="(min-width: 1335px) 410.6666666666667px, (min-width: 992px) calc(calc(100vw - 88px) / 3), (min-width: 768px) calc(calc(100vw - 64px) / 2), 100vw"
			alt=""
		/>
	</figure>
</td>
<td class="p-2">
	<div class="">
		<h2 class="line-clamp-2 text-lg">
			<a href="/book/{$data.node.id}" class="link-hover link">{$data.node.title}</a>
		</h2>
		<div class="">
			{$data.node.author}
		</div>
		<div>{$data.node.price?.formatted}</div>
	</div>
</td>

<slot />

<td class="p-2">
	{$data.price?.formatted}
</td>