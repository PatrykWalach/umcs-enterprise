import { Duration, Effect, Schedule, pipe } from 'effect';
import { LinkNotFound, type Client as ClientType, type Resource } from 'ketting';
import type { Schema } from 'zod';

export function get<T>() {
	return (resource: Resource<T>) => Effect.tryPromise(() => resource.get());
}

export function go<T>(
	...args: Parameters<ClientType['go']>
): Effect.Effect<ClientType, never, Resource<unknown>> {
	return pipe(Effect.serviceWith(Client)((client) => client.go<unknown>(...args)));
}

export function follow(...args: Parameters<Resource['follow']>) {
	return <A, B, C>(resource: Effect.Effect<A, B, Resource<C>>) =>
		pipe(
			resource,
			Effect.flatMap((resource) =>
				Effect.tryCatchPromise(
					() => toPromise<Resource<unknown>>(resource.follow(...args)),
					(error) =>
						error instanceof LinkNotFound ? new RelNotFoundError(error) : new FetchError(error)
				)
			)
		);
}

export function uri(...args: Parameters<Resource['follow']>) {
	return <A, B, C>(resource: Effect.Effect<A, B, Resource<C>>) =>
		pipe(
			resource,
			follow(...args),
			Effect.map((resource) => resource.uri),
			Effect.orElseSucceed(() => null)
		);
}

export function followAll(...args: Parameters<Resource['followAll']>) {
	return <A, B, C>(resource: Effect.Effect<A, B, Resource<C>>) =>
		pipe(
			resource,
			Effect.flatMap((resource) =>
				Effect.tryPromise(() => toPromise<Resource<unknown>[]>(resource.followAll(...args)))
			)
		);
}

function toPromise<T>(fn: PromiseLike<T>) {
	return new Promise<T>((resolve, reject) => fn.then(resolve, reject));
}

interface RetryOptions {
	whileInput?: (error: unknown) => boolean;
	initialDelay?: Duration.Duration;
	maxDelay?: Duration.Duration;
}

export const retry = (options?: RetryOptions) => {
	return Effect.retry(
		pipe(
			Schedule.exponential(options?.initialDelay ?? Duration.millis(20), 2.0),
			Schedule.andThenEither(Schedule.spaced(Duration.seconds(1))),
			Schedule.compose(Schedule.elapsed()),
			Schedule.whileOutput(Duration.lessThanOrEqualTo(options?.maxDelay ?? Duration.seconds(30))),
			Schedule.whileInput(options?.whileInput ?? ((error) => !ParseError.isInstance(error)))
		)
	);
};

import * as Context from '@fp-ts/data/Context';

export const Client = Context.Tag<ClientType>();

export class RelNotFoundError {
	_tag = 'RelNotFoundError' as const;
	constructor(readonly error: LinkNotFound) {}

	static new(error: LinkNotFound) {
		return new RelNotFoundError(error);
	}

	static isInstance(obj: unknown): obj is RelNotFoundError {
		return obj instanceof this;
	}
}

export class FetchError {
	_tag = 'FetchError' as const;
	constructor(readonly error: unknown) {}

	static new(error: unknown) {
		return new FetchError(error);
	}

	static isInstance(obj: unknown): obj is FetchError {
		return obj instanceof this;
	}
}

export class ParseError {
	_tag = 'ParseError' as const;
	constructor(readonly error: unknown) {}

	static new(error: unknown) {
		return new ParseError(error);
	}

	static isInstance(obj: unknown): obj is ParseError {
		return obj instanceof this;
	}
}

import * as ReadonlyArray from '@effect/data/ReadonlyArray';

export function parseAll<T>(schema: Schema<T>) {
	return <A, B>(effect: Effect.Effect<A, B, Resource<unknown>[]>) =>
		pipe(
			effect,
			// Effect.flatMap(
			// 	flow(
			// 		ReadonlyArray.map(get()),
			// 		ReadonlyArray.map(Effect.map((state) => state.data)),
			// 		ReadonlyArray.map(Effect.mapTryCatch(schema.parse, ParseError.new)),
			// 		Effect.collectAllSuccessesPar
			// 	)
			// ),

			Effect.map(ReadonlyArray.map(get())),
			Effect.flatMap(Effect.collectAllSuccessesPar),
			Effect.map(ReadonlyArray.fromIterable),
			Effect.map(ReadonlyArray.map((state) => Effect.sync(() => state.data))),
			Effect.map(ReadonlyArray.map(Effect.mapTryCatch(schema.parse, ParseError.new))),
			Effect.flatMap(Effect.collectAllSuccessesPar),
			Effect.map(ReadonlyArray.fromIterable)
		);
}
