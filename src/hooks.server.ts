import type { Handle } from '@sveltejs/kit';

import { setSession } from '$houdini';

export const handle: Handle = ({ event, resolve }) => {
	const token = event.cookies.get('enterprise-token');

	setSession(event, {
		token
	});

	return resolve(event);
};


// export const handleFetch: HandleFetch = ({fetch,request,event})=>{
 
// 	return fetch(request).then(async response=>{
		
// 		if(request.url .startsWith ("http://localhost:5173/graphql")){
// 			console.log(await response.text())
// 		}
// 		return response
// 	})
// }