export function isNotNull<T>(value: T | null | undefined): value is NonNullable<T> {
	return !!value;
}
