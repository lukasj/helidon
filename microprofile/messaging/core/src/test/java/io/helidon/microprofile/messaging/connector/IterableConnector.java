/*
 * Copyright (c) 2020 Oracle and/or its affiliates.
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

package io.helidon.microprofile.messaging.connector;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.eclipse.microprofile.reactive.messaging.spi.ConnectorFactory;
import org.eclipse.microprofile.reactive.messaging.spi.IncomingConnectorFactory;
import org.eclipse.microprofile.reactive.messaging.spi.OutgoingConnectorFactory;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.eclipse.microprofile.reactive.streams.operators.SubscriberBuilder;

/**
 * This test is modified version of official tck test in version 1.0
 * https://github.com/eclipse/microprofile-reactive-messaging
 */
@ApplicationScoped
@Connector("iterable-connector")
public class IterableConnector implements IncomingConnectorFactory, OutgoingConnectorFactory {

    private static final Logger LOGGER = Logger.getLogger(IterableConnector.class.getName());

    public static final String[] TEST_DATA = {"test1", "test2", "test3"};
    private CountDownLatch latch = new CountDownLatch(TEST_DATA.length);
    private static final Set<String> PROCESSED_DATA =
            Arrays.stream(IterableConnector.TEST_DATA).collect(Collectors.toSet());

    @Override
    public PublisherBuilder<? extends Message<?>> getPublisherBuilder(Config config) {
        return ReactiveStreams.of(TEST_DATA).map(Message::of);
    }

    @Override
    public SubscriberBuilder<? extends Message<?>, Void> getSubscriberBuilder(Config config) {
        return ReactiveStreams.<Message<?>>builder()
                .map(Message::getPayload)
                .peek(p -> {
                    if (PROCESSED_DATA.contains(p)) {
                        latch.countDown();
                    }
                })
                .onError(t -> LOGGER.log(Level.SEVERE, t, () -> "Error intercepted in channel "
                        + config.getValue(ConnectorFactory.CHANNEL_NAME_ATTRIBUTE, String.class)))
                .ignore();
    }

    public boolean await() throws InterruptedException {
        return latch.await(2, TimeUnit.SECONDS);
    }

    public void reset() {
        latch = new CountDownLatch(TEST_DATA.length);
    }

    public void reset(int i) {
        latch = new CountDownLatch(i);
    }
}
