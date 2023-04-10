import { graphql } from '$gql';

export default graphql(`
	mutation UnbasketBook($input: UnbasketBookInput!) {
		unbasketBook(input: $input) {
			__typename
		}
	}
`);
