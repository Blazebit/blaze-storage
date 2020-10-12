package com.blazebit.storage.rest.impl.aop;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collections;

@Provider
@PreMatching
public class ResteasyWorkaroundContainerRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        String accept = headers.getFirst(HttpHeaders.ACCEPT);
        if (accept == null || accept.isEmpty()) {
            headers.put(HttpHeaders.ACCEPT, Collections.singletonList(MediaType.WILDCARD));
        }
    }
}
