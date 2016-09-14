package com.myservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/helloworld")
public class HelloWorldService {

	@GET
	@Path("/{hi}")
	public Response sayHi(@PathParam("hi") String msg) {
		String output = "Hello : " + msg;
		return Response.status(200).entity(output).build();

	}

}