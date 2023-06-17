<script lang="ts">
	import { isNotNull } from '$lib/isNotNull';

  
	import type { PageData } from './$houdini';

	export let data: PageData;

	$: ({ PurchasesQuery } = data);
</script>