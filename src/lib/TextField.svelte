<script lang="ts">
  import type { UnwrapEffects } from 'sveltekit-superforms';
  import type { SuperForm } from 'sveltekit-superforms/client';
  import { formFieldProxy } from 'sveltekit-superforms/client';
  import type { AnyZodObject, z } from 'zod';

  type T = $$Generic<AnyZodObject>;

  export let form: SuperForm<UnwrapEffects<T>, unknown>;
  export let field: keyof z.infer<T> //| FieldPath<z.infer<T>>;
  export let id = String(field)

  $: inputId = id ?? String(field)


  const { path, value, errors, constraints } = formFieldProxy(form, field);

</script>

<div class="form-control">
  <label class="label" for={inputId}>
    <span class="label-text"><slot /></span>
  </label>
  <input
    bind:value={$value}
    class="input-bordered input"
    type='text'
    name={String(field)}
    id={inputId}
    {...$constraints}
    {...$$restProps}
  />
  <label class="label" for="username">
    <span class="label-text-alt text-error">
      {$errors || ''}
    </span>
  </label>
</div>