/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates.
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

package io.helidon.tests.functional.context.hello;

import java.util.function.Supplier;

import io.helidon.webserver.ServerRequest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Context;

@RequestScoped
public class ServerRequestSupplier implements Supplier<ServerRequest> {

    @Context
    private ServerRequest serverRequest;

    @Override
    public ServerRequest get() {
        return serverRequest;
    }
}
