import { graphql } from '$gql';

export default graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			__typename
		}
	}
`);
