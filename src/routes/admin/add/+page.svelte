<script>
	import { enhance } from '$app/forms';
</script>

<form method="post" enctype="multipart/form-data" use:enhance>
	<label>
		Cover
		<input type="file" name="cover" accept="image/*" />
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

	<button type="submit">Submit</button>
</form>
