/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates.
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

package io.helidon.microprofile.faulttolerance;

import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.AddConfig;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test overrides using global overrides.
 */
@AddBean(RetryBean.class)
@AddConfig(key = "Retry/maxRetries", value = "0")
class ConfigGlobalTest extends FaultToleranceTest {

    @Inject
    private RetryBean bean;

    @Test
    void testRetryOverrideGlobal() {
        assertThrows(RuntimeException.class, bean::retry);       // fails with override
    }
}
