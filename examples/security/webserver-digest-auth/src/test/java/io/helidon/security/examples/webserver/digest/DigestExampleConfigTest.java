/*
 * Copyright (c) 2018, 2021 Oracle and/or its affiliates.
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

package io.helidon.security.examples.webserver.digest;

import java.io.IOException;

import io.helidon.webserver.WebServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Unit test for {@link DigestExampleConfigMain}.
 */
public class DigestExampleConfigTest extends DigestExampleTest {

    private static WebServer server;

    @BeforeAll
    public static void startServer() throws IOException {
        // start the test
        DigestExampleConfigMain.main(new String[0]);
        server = DigestExampleConfigMain.getServer();
    }

    @AfterAll
    public static void stopServer() throws InterruptedException {
        stopServer(server);
    }

    @Override
    String getServerBase() {
        return "http://localhost:" + server.port();
    }
}
