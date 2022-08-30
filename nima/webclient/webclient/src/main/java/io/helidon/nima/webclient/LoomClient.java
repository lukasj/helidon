/*
 * Copyright (c) 2022 Oracle and/or its affiliates.
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

package io.helidon.nima.webclient;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.helidon.common.LazyValue;
import io.helidon.common.socket.SocketOptions;
import io.helidon.nima.common.tls.Tls;

/**
 * Base class for HTTP implementations of {@link io.helidon.nima.webclient.WebClient}.
 */
public class LoomClient implements WebClient {
    private static final LazyValue<Tls> EMPTY_TLS = LazyValue.create(() -> Tls.builder().build());
    private static final SocketOptions EMPTY_OPTIONS = SocketOptions.builder().build();
    private static final LazyValue<ExecutorService> EXECUTOR = LazyValue.create(() -> {
        return Executors.newThreadPerTaskExecutor(Thread.ofVirtual()
                                                          .inheritInheritableThreadLocals(false)
                                                          .factory());
    });
    private final URI uri;
    private final Tls tls;
    private final SocketOptions channelOptions;

    /**
     * Construct this instance from a subclass of builder.
     *
     * @param builder builder the subclass is built from
     */
    protected LoomClient(WebClient.Builder<?, ?> builder) {
        this.uri = builder.baseUri();
        this.tls = builder.tls() == null ? EMPTY_TLS.get() : builder.tls();
        this.channelOptions = builder.channelOptions() == null ? EMPTY_OPTIONS : builder.channelOptions();
    }

    /**
     * Base URI of this client.
     *
     * @return URI to use
     */
    public URI uri() {
        return uri;
    }

    /**
     * Executor services, uses virtual threads.
     *
     * @return executor service
     */
    public ExecutorService executor() {
        return EXECUTOR.get();
    }

    /**
     * TLS configuration for this client.
     *
     * @return TLS configuration
     */
    public Tls tls() {
        return tls;
    }

    /**
     * Socket options for this client.
     *
     * @return socket options
     */
    public SocketOptions socketOptions() {
        return channelOptions;
    }
}