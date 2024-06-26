/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.integrations.vault.auths.approle;

import io.helidon.integrations.common.rest.ApiResponse;

/**
 * Destroy Secret ID request and response.
 */
public class DestroySecretId {
    /**
     * Request object. Can be configured with additional headers, query parameters etc.
     */
    public static class Request extends AppRoleRequestBase<Request> {
        private Request() {
        }

        /**
         * Fluent API builder for configuring a request.
         * The request builder is passed as is, without a build method.
         * The equivalent of a build method is {@link #toJson(jakarta.json.JsonBuilderFactory)}
         * used by the {@link io.helidon.integrations.common.rest.RestApi}.
         *
         * @return new request builder
         */
        public static Request builder() {
            return new Request();
        }

        /**
         * Secret ID to use.
         *
         * @param secretId secret ID
         * @return updated request
         */
        public Request secretId(String secretId) {
            return add("secret_id", secretId);
        }
    }

    /**
     * Destroy Secret ID response.
     *
     * @see AppRoleAuthRx#destroySecretId(DestroySecretId.Request)
     */
    public static final class Response extends ApiResponse {
        // we could use a single response object for all responses without entity
        // but that would hinder future extensibility, as this allows us to add any field to this
        // class without impacting the API

        private Response(Builder builder) {
            super(builder);
        }

        static Builder builder() {
            return new Builder();
        }

        static class Builder extends ApiResponse.Builder<Response.Builder, Response> {
            private Builder() {
            }

            @Override
            public Response build() {
                return new Response(this);
            }
        }
    }
}
