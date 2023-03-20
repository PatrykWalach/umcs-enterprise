import { graphql } from './gql/index';

export default graphql(`
	mutation UnbasketBook($input: UnbasketBookInput!) {
		unbasketBook(input: $input) {
			__typename
		}
	}
`);
