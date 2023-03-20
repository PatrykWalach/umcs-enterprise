import { graphql } from './gql/index';

export default graphql(`
	mutation BasketBook($input: BasketBookInput!) {
		basketBook(input: $input) {
			__typename
			basket {
				books(first: 10) {
					edges {
						node {
							id
						}
						quantity
					}
				}
			}
		}
	}
`);
