/* eslint-disable */
import type { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
	ID: string;
	String: string;
	Boolean: boolean;
	Int: number;
	Float: number;
	Date: string;
};

export type Admin = Node & {
	__typename?: 'Admin';
	id: Scalars['ID'];
};

export type Basket = Node & {
	__typename?: 'Basket';
	books?: Maybe<BookConnection>;
	id: Scalars['ID'];
	totalPrice?: Maybe<Scalars['String']>;
};

export type BasketBooksArgs = {
	after?: InputMaybe<Scalars['String']>;
	before?: InputMaybe<Scalars['String']>;
	first?: InputMaybe<Scalars['Int']>;
	last?: InputMaybe<Scalars['Int']>;
};

export type Book = Node & {
	__typename?: 'Book';
	author?: Maybe<Scalars['String']>;
	cover?: Maybe<BookCover>;
	createdAt?: Maybe<Scalars['Date']>;
	id: Scalars['ID'];
	popularity?: Maybe<Scalars['Int']>;
	price?: Maybe<Scalars['String']>;
	title?: Maybe<Scalars['String']>;
};

export type BookConnection = {
	__typename?: 'BookConnection';
	edges?: Maybe<Array<Maybe<BookEdge>>>;
	pageInfo: PageInfo;
};

export type BookCover = {
	__typename?: 'BookCover';
	height?: Maybe<Scalars['Int']>;
	url?: Maybe<Scalars['String']>;
	width?: Maybe<Scalars['Int']>;
};

export type BookEdge = {
	__typename?: 'BookEdge';
	cursor?: Maybe<Scalars['String']>;
	node?: Maybe<Book>;
};

export type BookSortBy = {
	createdAt?: InputMaybe<Sort>;
	popularity?: InputMaybe<Sort>;
	price?: InputMaybe<Sort>;
};

export type CreateBookCoverInput = {
	url?: InputMaybe<Scalars['String']>;
};

export type CreateBookInput = {
	author?: InputMaybe<Scalars['String']>;
	cover?: InputMaybe<CreateBookCoverInput>;
	price?: InputMaybe<Scalars['Int']>;
	title?: InputMaybe<Scalars['String']>;
};

export type CreateOrderResult = {
	__typename?: 'CreateOrderResult';
	basket?: Maybe<Basket>;
	order?: Maybe<Order>;
};

export type Mutation = {
	__typename?: 'Mutation';
	/**
	 *
	 * requires User
	 */
	basketBook?: Maybe<Basket>;
	/**
	 *
	 * requires Admin
	 */
	createBook?: Maybe<Book>;
	/**
	 *
	 * requires Admin
	 */
	delete?: Maybe<Node>;
	/**
	 *
	 * requires User
	 */
	makeOrder?: Maybe<CreateOrderResult>;
	/**
	 *
	 * requires User
	 */
	unbasketBook?: Maybe<Basket>;
	/**
	 *
	 * requires Admin
	 */
	updateStatus?: Maybe<Order>;
};

export type MutationBasketBookArgs = {
	id: Scalars['ID'];
};

export type MutationCreateBookArgs = {
	input: CreateBookInput;
};

export type MutationDeleteArgs = {
	id: Scalars['ID'];
};

export type MutationUnbasketBookArgs = {
	id: Scalars['ID'];
};

export type MutationUpdateStatusArgs = {
	input: UpdateStatusInput;
};

export type Node = {
	id: Scalars['ID'];
};

export type Order = Node & {
	__typename?: 'Order';
	books?: Maybe<BookConnection>;
	createdAt?: Maybe<Scalars['Date']>;
	id: Scalars['ID'];
	status?: Maybe<OrderStatus>;
	totalPrice?: Maybe<Scalars['String']>;
	user?: Maybe<User>;
};

export type OrderBooksArgs = {
	after?: InputMaybe<Scalars['String']>;
	before?: InputMaybe<Scalars['String']>;
	first?: InputMaybe<Scalars['Int']>;
	last?: InputMaybe<Scalars['Int']>;
};

export type OrderConnection = {
	__typename?: 'OrderConnection';
	edges?: Maybe<Array<Maybe<OrderEdge>>>;
	pageInfo: PageInfo;
};

export type OrderEdge = {
	__typename?: 'OrderEdge';
	cursor?: Maybe<Scalars['String']>;
	node?: Maybe<Order>;
};

export enum OrderStatus {
	Complete = 'COMPLETE',
	Pending = 'PENDING'
}

export type PageInfo = {
	__typename?: 'PageInfo';
	endCursor?: Maybe<Scalars['String']>;
	hasNextPage: Scalars['Boolean'];
	hasPreviousPage: Scalars['Boolean'];
	startCursor?: Maybe<Scalars['String']>;
};

export type Query = {
	__typename?: 'Query';
	books?: Maybe<BookConnection>;
	/**     3 */
	hello?: Maybe<Scalars['String']>;
	node?: Maybe<Node>;
	orders?: Maybe<OrderConnection>;
	/**     ? */
	search?: Maybe<BookConnection>;
	viewer?: Maybe<Viewer>;
};

export type QueryBooksArgs = {
	after?: InputMaybe<Scalars['String']>;
	before?: InputMaybe<Scalars['String']>;
	first?: InputMaybe<Scalars['Int']>;
	last?: InputMaybe<Scalars['Int']>;
	sortBy?: InputMaybe<BookSortBy>;
};

export type QueryNodeArgs = {
	id: Scalars['ID'];
};

export type QueryOrdersArgs = {
	after?: InputMaybe<Scalars['String']>;
	before?: InputMaybe<Scalars['String']>;
	first?: InputMaybe<Scalars['Int']>;
	last?: InputMaybe<Scalars['Int']>;
};

export type QuerySearchArgs = {
	query?: InputMaybe<Scalars['String']>;
};

export enum Sort {
	Asc = 'ASC',
	Desc = 'DESC'
}

export type UpdateStatusInput = {
	iD: Scalars['ID'];
	status?: InputMaybe<OrderStatus>;
};

export type User = Node & {
	__typename?: 'User';
	basket?: Maybe<Basket>;
	id: Scalars['ID'];
	orders?: Maybe<OrderConnection>;
};

export type UserOrdersArgs = {
	after?: InputMaybe<Scalars['String']>;
	before?: InputMaybe<Scalars['String']>;
	first?: InputMaybe<Scalars['Int']>;
	last?: InputMaybe<Scalars['Int']>;
};

export type Viewer = Admin | User;

export type LayoutQueryQueryVariables = Exact<{ [key: string]: never }>;

export type LayoutQueryQuery = {
	__typename?: 'Query';
	books?: {
		__typename?: 'BookConnection';
		edges?: Array<{
			__typename?: 'BookEdge';
			node?: { __typename?: 'Book'; id: string; title?: string | null } | null;
		} | null> | null;
	} | null;
};

export type AddBookMutationVariables = Exact<{
	input: CreateBookInput;
}>;

export type AddBookMutation = {
	__typename?: 'Mutation';
	createBook?: { __typename?: 'Book'; id: string } | null;
};

export const LayoutQueryDocument = {
	kind: 'Document',
	definitions: [
		{
			kind: 'OperationDefinition',
			operation: 'query',
			name: { kind: 'Name', value: 'LayoutQuery' },
			selectionSet: {
				kind: 'SelectionSet',
				selections: [
					{
						kind: 'Field',
						name: { kind: 'Name', value: 'books' },
						arguments: [
							{
								kind: 'Argument',
								name: { kind: 'Name', value: 'first' },
								value: { kind: 'IntValue', value: '10' }
							}
						],
						selectionSet: {
							kind: 'SelectionSet',
							selections: [
								{
									kind: 'Field',
									name: { kind: 'Name', value: 'edges' },
									selectionSet: {
										kind: 'SelectionSet',
										selections: [
											{
												kind: 'Field',
												name: { kind: 'Name', value: 'node' },
												selectionSet: {
													kind: 'SelectionSet',
													selections: [
														{ kind: 'Field', name: { kind: 'Name', value: 'id' } },
														{ kind: 'Field', name: { kind: 'Name', value: 'title' } }
													]
												}
											}
										]
									}
								}
							]
						}
					}
				]
			}
		}
	]
} as unknown as DocumentNode<LayoutQueryQuery, LayoutQueryQueryVariables>;
export const AddBookDocument = {
	kind: 'Document',
	definitions: [
		{
			kind: 'OperationDefinition',
			operation: 'mutation',
			name: { kind: 'Name', value: 'AddBook' },
			variableDefinitions: [
				{
					kind: 'VariableDefinition',
					variable: { kind: 'Variable', name: { kind: 'Name', value: 'input' } },
					type: {
						kind: 'NonNullType',
						type: { kind: 'NamedType', name: { kind: 'Name', value: 'CreateBookInput' } }
					}
				}
			],
			selectionSet: {
				kind: 'SelectionSet',
				selections: [
					{
						kind: 'Field',
						name: { kind: 'Name', value: 'createBook' },
						arguments: [
							{
								kind: 'Argument',
								name: { kind: 'Name', value: 'input' },
								value: { kind: 'Variable', name: { kind: 'Name', value: 'input' } }
							}
						],
						selectionSet: {
							kind: 'SelectionSet',
							selections: [{ kind: 'Field', name: { kind: 'Name', value: 'id' } }]
						}
					}
				]
			}
		}
	]
} as unknown as DocumentNode<AddBookMutation, AddBookMutationVariables>;
