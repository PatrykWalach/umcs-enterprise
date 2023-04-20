<script lang="ts">
	import { enhance } from '$app/forms';

	let data :import('./$types').PageData

 

</script>

<form method="post" enctype="multipart/form-data" use:enhance>
	<input type="hidden" name="databaseId" value={data.databaseId} />
	<label>
		Cover
		<input type="file" name="cover" accept="image/*" />
	</label>
	<label>
		Url
		<input type="url" name="url" />
	</label>
	<label>
		Title
		<input type="text" name="title" />
	</label>
	<label>
		Author
		<input type="text" name="author" />
	</label>
	<label>
		Price
		<input type="number" name="price" />
	</label>
	<label>
		Release date
		<input type="datetime-local" name="releasedAt" />
	</label>

	<button type="submit">Submit</button>
</form>
