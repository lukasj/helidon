/*
 * Copyright (c) 2020, 2022 Oracle and/or its affiliates.
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

package io.helidon.common.features;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import io.helidon.common.features.api.HelidonFlavor;

/**
 * Feature catalog discovers features from META-INF/helidon/feature-metadata.properties.
 */
final class FeatureCatalog {
    private static final System.Logger LOGGER = System.getLogger(FeatureCatalog.class.getName());
    private static final HelidonFlavor[] NO_FLAVORS = new HelidonFlavor[0];

    static {
        add("io.helidon.grpc.client",
            FeatureDescriptor.builder()
                    .name("gRPC Client")
                    .description("Client for gRPC services")
                    .path("grpcClient")
                    .flavor(HelidonFlavor.SE)
                    .nativeSupported(true)
                    .nativeDescription("Experimental support in native image"));
        addSe("io.helidon.grpc.metrics",
              "Metrics",
              "Metrics for gRPC services",
              "grpc", "Metrics");
        addSe("io.helidon.grpc.metrics",
              "Metrics",
              "Metrics for gRPC client",
              "grpcClient", "Metrics");
        addSe("io.helidon.health",
              "Health",
              "Health checks support",
              "Health");
        addSe("io.helidon.reactive.media.jsonp",
              "JSON-P",
              "Media support for Jakarta JSON Processing",
              "WebServer", "Jsonp");
        addSe("io.helidon.reactive.media.jsonp",
              "JSON-P",
              "Media support for Jakarta JSON Processing",
              "WebClient", "Jsonp");
        addSe("io.helidon.reactive.media.jsonb",
              "JSON-B",
              "Media support for Jakarta JSON Binding",
              "WebServer", "Jsonb");
        addSe("io.helidon.reactive.media.jsonb",
              "JSON-B",
              "Media support for Jakarta JSON Binding",
              "WebClient", "Jsonb");
        addSe("io.helidon.reactive.media.jackson",
              "Jackson",
              "Media support for Jackson",
              "WebServer", "Jackson");
        addSe("io.helidon.reactive.media.jackson",
              "Jackson",
              "Media support for Jackson",
              "WebClient", "Jackson");
        addSe("io.helidon.reactive.media.multipart",
              "Multi-part",
              "Media support for Multi-part entities",
              "WebServer", "Multipart");
        addSe("io.helidon.reactive.media.multipart",
              "Multi-part",
              "Media support for Multi-part entities",
              "WebClient", "Multipart");
        add("io.helidon.messaging",
            FeatureDescriptor.builder()
                    .name("Messaging")
                    .description("Reactive messaging support")
                    .path("Messaging")
                    .flavor(HelidonFlavor.SE)
                    .experimental(true));
        addSe("io.helidon.metrics",
              "Metrics",
              "Metrics support",
              "Metrics");
        add("io.helidon.metrics.prometheus",
            FeatureDescriptor.builder()
                    .name("Prometheus")
                    .description("Metrics support for Prometheus")
                    .path("WebServer", "Prometheus")
                    .nativeSupported(false)
                    .flavor(HelidonFlavor.SE)
        );
        addSe("io.helidon.openapi",
              "OpenAPI",
              "Open API support",
              "OpenAPI");
        addSe("io.helidon.security",
              "Security",
              "Security support",
              "Security");
        addSe("io.helidon.tracing",
              "Tracing",
              "Tracing support",
              "Tracing");
        addSe("io.helidon.webserver",
              "WebServer",
              "Helidon WebServer",
              "WebServer");
        addSe("io.helidon.webserver.accesslog",
              "Access Log",
              "Access log support",
              "WebServer", "AccessLog");
        addSe("io.helidon.webserver.cors",
              "CORS",
              "CORS support for WebServer",
              "WebServer", "CORS");
        addSe("io.helidon.webserver.jersey",
              "Jersey",
              "WebServer integration with Jersey",
              "WebServer", "Jersey");
        add("io.helidon.scheduling",
            FeatureDescriptor.builder()
                    .flavor(HelidonFlavor.SE)
                    .name("Scheduling")
                    .description("Scheduling of periodical tasks")
                    .path("Scheduling")
                    .nativeSupported(true));
        add("io.helidon.webserver.tyrus",
            FeatureDescriptor.builder()
                    .flavor(HelidonFlavor.SE)
                    .name("Websocket")
                    .description("Jakarta Websocket implementation")
                    .path("WebServer", "Websocket")
                    .nativeSupported(true)
                    .nativeDescription("Server only"));
        add("io.helidon.graphql.server",
            FeatureDescriptor.builder()
                    .name("GraphQL")
                    .description("GraphQL support")
                    .path("GraphQL")
                    .nativeDescription("Experimental support, tested on limited use cases")
                    .flavor(HelidonFlavor.SE)
                    .experimental(true));
        add("io.helidon.integrations.micrometer",
            FeatureDescriptor.builder()
                    .name("Micrometer")
                    .description("Micrometer integration")
                    .path("Micrometer")
                    .experimental(true)
                    .nativeSupported(true)
                    .flavor(HelidonFlavor.SE));
        add("io.helidon.integrations.oci.connect",
            FeatureDescriptor.builder()
                    .name("OCI")
                    .description("OCI Integration")
                    .path("OCI")
                    .flavor(HelidonFlavor.SE)
                    .experimental(true));
        add("io.helidon.integrations.vault",
            FeatureDescriptor.builder()
                    .name("HCP Vault")
                    .description("Hashicorp Vault Integration")
                    .path("HCP Vault")
                    .flavor(HelidonFlavor.SE)
                    .experimental(true));
        add("io.helidon.integrations.microstream",
            FeatureDescriptor.builder()
                    .name("Microstream")
                    .description("Microstream Integration")
                    .path("Microstream")
                    .flavor(HelidonFlavor.SE)
                    .experimental(true)
                    .nativeSupported(false));
        /*
         * MP Modules
         */
        add("io.helidon.integrations.cdi.eclipselink",
            FeatureDescriptor.builder()
                    .name("EclipseLink")
                    .description("EclipseLink support for Helidon MP")
                    .path("JPA", "EclipseLink")
                    .flavor(HelidonFlavor.MP)
                    .nativeSupported(false));
        add("io.helidon.integrations.cdi.hibernate",
            FeatureDescriptor.builder()
                    .name("Hibernate")
                    .description("Hibernate support for Helidon MP")
                    .path("JPA", "Hibernate")
                    .flavor(HelidonFlavor.MP)
                    .nativeDescription("Experimental support, tested on limited use cases"));
        add("io.helidon.integrations.cdi.jpa",
            FeatureDescriptor.builder()
                    .name("JPA")
                    .description("Jakarta persistence API support for Helidon MP")
                    .flavor(HelidonFlavor.MP)
                    .path("JPA"));
        add("io.helidon.integrations.jta.cdi",
            FeatureDescriptor.builder()
                    .name("JTA")
                    .description("Jakarta transaction API support for Helidon MP")
                    .path("JTA")
                    .flavor(HelidonFlavor.MP)
                    .nativeDescription("Experimental support, tested on limited use cases"));
        addMp("io.helidon.microprofile.accesslog",
              "Access Log",
              "Access log support",
              "Server", "AccessLog");
        addMp("io.helidon.microprofile.cdi",
              "CDI",
              "Jakarta CDI implementation",
              "CDI");
        addMp("io.helidon.microprofile.config",
              "Config",
              "MicroProfile configuration spec implementation",
              "Config");
        addMp("io.helidon.microprofile.cors",
              "CORS",
              "CORS support for Server",
              "Server", "CORS");
        addMp("io.helidon.microprofile.faulttolerance",
              "Fault Tolerance",
              "MicroProfile Fault Tolerance spec implementation",
              "FT");
        add("io.helidon.microprofile.graphql.server",
            FeatureDescriptor.builder()
                    .name("GraphQL")
                    .description("MicroProfile GraphQL spec implementation")
                    .path("GraphQL")
                    .nativeDescription("Experimental support, tested on limited use cases")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true));
        add("io.helidon.microprofile.grpc.server",
            FeatureDescriptor.builder()
                    .name("gRPC Server")
                    .description("Server for gRPC services")
                    .path("grpc")
                    .flavor(HelidonFlavor.MP)
                    .nativeSupported(false));
        add("io.helidon.microprofile.grpc.client",
            FeatureDescriptor.builder()
                    .name("gRPC Client")
                    .description("Client for gRPC services")
                    .path("grpcClient")
                    .flavor(HelidonFlavor.MP)
                    .nativeSupported(false));
        addMp("io.helidon.microprofile.grpc.metrics",
              "Metrics",
              "Metrics for gRPC client",
              "grpcClient", "Metrics"
        );
        addMp("io.helidon.microprofile.grpc.metrics",
              "Metrics",
              "Metrics for gRPC server",
              "grpcServer", "Metrics"
        );
        addMp("io.helidon.microprofile.health",
              "Health",
              "MicroProfile Health spec implementation",
              "Health");
        addMp("io.helidon.microprofile.jwt.auth",
              "JWT Auth",
              "MicroProfile JWT Auth spec implementation",
              "Security", "JWTAuth");
        add("io.helidon.microprofile.messaging",
            FeatureDescriptor.builder()
                    .name("Messaging")
                    .description("MicroProfile Reactive Messaging spec implementation")
                    .path("Messaging")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true));
        addMp("io.helidon.microprofile.metrics",
              "Metrics",
              "MicroProfile metrics spec implementation",
              "Metrics");
        addMp("io.helidon.microprofile.openapi",
              "Open API",
              "MicroProfile Open API spec implementation",
              "OpenAPI");
        add("io.helidon.microprofile.reactive",
            FeatureDescriptor.builder()
                    .name("Reactive")
                    .description("MicroProfile Reactive Stream operators")
                    .path("Reactive")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true));
        addMp("io.helidon.microprofile.security",
              "Security",
              "Security support",
              "Security");
        addMp("io.helidon.microprofile.server",
              "Server",
              "Server for Helidon MP",
              "Server");
        addMp("io.helidon.microprofile.server",
              "JAX-RS",
              "Jakarta JAX-RS implementation (Jersey)",
              "JAX-RS");
        addMp("io.helidon.microprofile.tracing",
              "Tracing",
              "MicroProfile tracing spec implementation",
              "Tracing");

        add("io.helidon.microprofile.tyrus",
            FeatureDescriptor.builder()
                    .flavor(HelidonFlavor.MP)
                    .name("Websocket")
                    .description("Jakarta Websocket implementation")
                    .path("Websocket")
                    .nativeSupported(false));

        add("io.helidon.microprofile.restclient",
            FeatureDescriptor.builder()
                    .name("REST Client")
                    .description("MicroProfile REST client spec implementation")
                    .path("RESTClient")
                    .flavor(HelidonFlavor.MP)
                    .nativeDescription("Does not support execution of default methods on interfaces."));

        add("io.helidon.integrations.micronaut.cdi",
            FeatureDescriptor.builder()
                    .name("Micronaut")
                    .description("Micronaut integration")
                    .path("CDI", "Micronaut")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true)
        );

        add("io.helidon.integrations.micronaut.cdi.data",
            FeatureDescriptor.builder()
                    .name("Micronaut Data")
                    .description("Micronaut Data integration")
                    .path("CDI", "Micronaut", "Data")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true)
        );

        add("io.helidon.microprofile.scheduling",
            FeatureDescriptor.builder()
                    .name("Scheduling")
                    .description("Task scheduling")
                    .path("Scheduling")
                    .flavor(HelidonFlavor.MP)
                    .nativeSupported(true)
                    .experimental(true)
        );

        add("io.helidon.integrations.micrometer.cdi",
            FeatureDescriptor.builder()
                    .name("Micrometer")
                    .description("Micrometer integration")
                    .path("Micrometer")
                    .experimental(true)
                    .nativeSupported(true)
                    .flavor(HelidonFlavor.MP));

        add("io.helidon.integrations.oci.cdi",
            FeatureDescriptor.builder()
                    .name("OCI")
                    .description("OCI Integration")
                    .path("OCI")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true));

        add("io.helidon.integrations.vault.cdi",
            FeatureDescriptor.builder()
                    .name("HCP Vault")
                    .description("Hashicorp Vault Integration")
                    .path("HCP Vault")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true));

        add("io.helidon.microprofile.lra",
            FeatureDescriptor.builder()
                    .name("Long Running Actions")
                    .description("MicroProfile Long Running Actions")
                    .path("LRA")
                    .flavor(HelidonFlavor.MP)
                    .nativeSupported(true)
                    .experimental(true));

        add("io.helidon.integrations.microstream.cdi",
            FeatureDescriptor.builder()
                    .name("Microstream")
                    .description("Microstream Integration")
                    .path("Microstream")
                    .flavor(HelidonFlavor.MP)
                    .experimental(true)
                    .nativeSupported(false));
        /*
         * Common modules
         */
        add("io.helidon.config.encryption",
            "Encryption",
            "Support for secret encryption in config",
            "Config", "Encryption");
        add("io.helidon.config.etcd",
            FeatureDescriptor.builder()
                    .name("etcd")
                    .description("Config source based on etcd")
                    .path("Config", "etcd")
                    .nativeSupported(false));
        add("io.helidon.config.git",
            "git",
            "Config source based on a git repository",
            "Config", "git");
        add("io.helidon.config.hocon",
            "HOCON",
            "HOCON media type support for config",
            "Config", "HOCON");
        add("io.helidon.config.objectmapping",
            "Object Mapping",
            "Object mapping support for Config",
            "Config", "ObjectMapping");
        add("io.helidon.config.yaml",
            "YAML",
            "YAML media type support for config",
            "Config", "YAML");
        add("io.helidon.reactive.dbclient",
            FeatureDescriptor.builder()
                    .name("Db Client")
                    .description("Reactive database client")
                    .path("DbClient")
                    .experimental(true));
        add("io.helidon.reactive.dbclient.health",
            "Health Check",
            "Reactive database client health check support",
            "DbClient", "Health");
        add("io.helidon.reactive.dbclient.jsonp",
            "JSON-P",
            "JSON Processing mapping DbRow",
            "DbClient", "JSON-P");
        add("io.helidon.reactive.dbclient.jdbc",
            FeatureDescriptor.builder()
                    .name("JDBC")
                    .description("Reactive database client over JDBC")
                    .path("DbClient", "JDBC")
                    .nativeDescription("Tested with Helidon Oracle and H2 drivers (see examples)"));
        add("io.helidon.reactive.dbclient.metrics",
            "Metrics",
            "Reactive database client metrics support",
            "DbClient", "Metrics");
        add("io.helidon.reactive.dbclient.mongodb",
            "mongo",
            "Reactive database client with reactive mongo driver",
            "DbClient", "mongo");
        add("io.helidon.reactive.dbclient.tracing",
            "Tracing",
            "Reactive database client tracing support",
            "DbClient", "Tracing");
        add("io.helidon.health.checks",
            "Built-ins",
            "Built in health checks",
            "Health", "Builtins");
        add("io.helidon.messaging.connectors.kafka",
            FeatureDescriptor.builder()
                    .name("Kafka Connector")
                    .description("Reactive messaging connector for Kafka")
                    .path("Messaging", "Kafka")
                    .experimental(true)
                    .nativeSupported(true));
        add("io.helidon.messaging.connectors.jms",
            FeatureDescriptor.builder()
                    .name("JMS Connector")
                    .description("Reactive messaging connector for JMS")
                    .path("Messaging", "JMS")
                    .experimental(true)
                    .nativeSupported(false));
        add("io.helidon.messaging.connectors.aq",
            FeatureDescriptor.builder()
                    .name("Oracle AQ Connector")
                    .description("Reactive messaging connector for Oracle AQ")
                    .path("Messaging", "OracleAQ")
                    .experimental(true)
                    .nativeSupported(false));
        add("io.helidon.security.abac.policy.el",
            FeatureDescriptor.builder()
                    .name("EL")
                    .description("ABAC Jakarta Expression Language policy support")
                    .path("Security", "Provider", "ABAC", "Policy", "EL")
                    .nativeSupported(true)
                    .nativeDescription("Properties used in expressions must have reflection configuration added"));
        add("io.helidon.security.abac.role",
            "Role",
            "ABAC Role based attribute validator",
            "Security", "Provider", "ABAC", "Role");
        add("io.helidon.security.abac.scope",
            "Scope",
            "ABAC Scope based attribute validator",
            "Security", "Provider", "ABAC", "Scope");
        add("io.helidon.security.abac.time",
            "Time",
            "ABAC Time based attribute validator",
            "Security", "Provider", "ABAC", "Time");
        add("io.helidon.security.integration.grpc",
            "gRPC",
            "Security integration with gRPC",
            "Security", "Integration", "gRPC");
        add("io.helidon.security.integration.jersey",
            "Jersey",
            "Security integration with Jersey (JAX-RS implementation)",
            "Security", "Integration", "Jersey");
        add("io.helidon.security.integration.webserver",
            "WebServer",
            "Security integration with web server",
            "Security", "Integration", "WebServer");
        add("io.helidon.security.providers.abac",
            "ABAC",
            "Security provider for attribute based access control",
            "Security", "Provider", "ABAC");
        add("io.helidon.security.providers.google.login",
            FeatureDescriptor.builder()
                    .name("Google Login")
                    .description("Security provider for Google login button authentication and outbound")
                    .path("Security", "Provider", "Google-Login")
                    .nativeSupported(false));
        add("io.helidon.security.providers.header",
            "Header",
            "Security provider for header based authentication",
            "Security", "Provider", "Header");
        add("io.helidon.security.providers.httpauth",
            "HTTP Basic",
            "Security provider for HTTP Basic authentication and outbound",
            "Security", "Provider", "HttpBasic");
        add("io.helidon.security.providers.httpauth",
            "HTTP Digest",
            "Security provider for HTTP Digest authentication",
            "Security", "Provider", "HttpDigest");
        add("io.helidon.security.providers.httpsign",
            "HTTP Signatures",
            "Security provider for HTTP Signature authentication and outbound",
            "Security", "Provider", "HttpSign");
        add("io.helidon.security.providers.config.vault",
            "Config Vault",
            "Security", "Provider", "ConfigVault");
        add("io.helidon.security.providers.idcs.mapper",
            FeatureDescriptor.builder()
                    .name("IDCS Role Mapper")
                    .description("Security provider role mapping - Oracle IDCS")
                    .path("Security", "Provider", "IdcsRoleMapper")
                    .nativeSupported(false));
        add("io.helidon.security.providers.jwt",
            "JWT",
            "Security provider for JWT based authentication",
            "Security", "Provider", "JWT");
        add("io.helidon.security.providers.oidc",
            "OIDC",
            "Security provider for Open ID Connect authentication",
            "Security", "OIDC");
        add("io.helidon.tracing.jaeger",
            "Jaeger",
            "Jaeger tracer integration",
            "Tracing", "Jaeger");
        add("io.helidon.metrics.jaeger",
            "Jaeger metrics",
            "Jaeger tracer metrics integration",
            "Metrics", "Jaeger");
        add("io.helidon.tracing.jersey",
            "Jersey Server",
            "Tracing integration with Jersey server",
            "Tracing", "Integration", "Jersey");
        add("io.helidon.tracing.jersey.client",
            "Jersey Client",
            "Tracing integration with Jersey client",
            "Tracing", "Integration", "JerseyClient");
        add("io.helidon.tracing.zipkin",
            "Zipkin",
            "Zipkin tracer integration",
            "Tracing", "Zipkin");
        add("io.helidon.integrations.neo4j",
            FeatureDescriptor.builder()
                    .name("Neo4j integration")
                    .description("Integration with Neo4j driver")
                    .path("Neo4j")
                    .experimental(true)
                    .nativeSupported(true));
        add("io.helidon.integrations.neo4j.health",
            FeatureDescriptor.builder()
                    .name("Neo4j Health")
                    .description("Health check for Neo4j integration")
                    .path("Neo4j", "Health"));
        add("io.helidon.integrations.neo4j.metrics",
            FeatureDescriptor.builder()
                    .name("Neo4j Metrics")
                    .description("Metrics for Neo4j integration")
                    .path("Neo4j", "Metrics"));
        add("io.helidon.reactive.webclient",
            FeatureDescriptor.builder()
                    .name("Web Client")
                    .description("Reactive web client")
                    .path("WebClient")
                    .experimental(true));
        add("io.helidon.reactive.webclient.metrics",
            "Metrics",
            "Reactive web client support for metrics",
            "WebClient", "Metrics");
        add("io.helidon.reactive.webclient.security",
            "Security",
            "Reactive web client support for security",
            "WebClient", "Security");
        add("io.helidon.reactive.webclient.tracing",
            "Tracing",
            "Reactive web client support for tracing",
            "WebClient", "Tracing");
        add("io.helidon.logging.log4j",
            FeatureDescriptor.builder()
                    .name("Log4j")
                    .path("Logging", "Log4j")
                    .description("Log4j MDC support")
                    .nativeDescription("Only programmatic configuration supported, does not work with Helidon loggers"));
        add("io.helidon.webserver.staticcontent",
            "Static Content",
            "Static content support for webserver",
            "WebServer", "Static Content");
        add("io.helidon.integrations.oci.objectstorage",
            "OCI Object Storage",
            "Integration with OCI Object Storage",
            "OCI", "Object Storage");
        add("io.helidon.integrations.oci.vault",
            "OCI Vault",
            "Integration with OCI Vault",
            "OCI", "Vault");
        add("io.helidon.integrations.oci.telemetry",
            "OCI Telemetry",
            "Integration with OCI Telemetry",
            "OCI", "Telemetry");
        add("io.helidon.integrations.vault.auths.approle",
            "AppRole",
            "AppRole Authentication Method",
            "HCP Vault", "Auth", "AppRole");
        add("io.helidon.integrations.vault.auths.k8s",
            "k8s",
            "Kubernetes Authentication Method",
            "HCP Vault", "Auth", "k8s");
        add("io.helidon.integrations.vault.auths.token",
            "Token",
            "Token Authentication Method",
            "HCP Vault", "Auth", "Token");
        add("io.helidon.integrations.vault.secrets.cubbyhole",
            "Cubbyhole",
            "Cubbyhole Secrets Engine",
            "HCP Vault", "Secrets", "Cubbyhole");
        add("io.helidon.integrations.vault.secrets.database",
            "Database",
            "Database Secrets Engine",
            "HCP Vault", "Secrets", "Database");
        add("io.helidon.integrations.vault.secrets.kv1",
            "K/V 1",
            "Key/Value Version 1 Secrets Engine",
            "HCP Vault", "Secrets", "K/V 1");
        add("io.helidon.integrations.vault.secrets.kv2",
            "K/V 2",
            "Key/Value Version 2 Secrets Engine",
            "HCP Vault", "Secrets", "K/V 2");
        add("io.helidon.integrations.vault.secrets.pki",
            "PKI",
            "PKI Secrets Engine",
            "HCP Vault", "Secrets", "PKI");
        add("io.helidon.integrations.vault.secrets.transit",
            "Transit",
            "Transit Secrets Engine",
            "HCP Vault", "Secrets", "Transit");
        add("io.helidon.integrations.vault.sys",
            "Sys",
            "System operations",
            "HCP Vault", "Sys");
    }

    // hide utility class constructor
    private FeatureCatalog() {
    }

    static List<FeatureDescriptor> features(ClassLoader classLoader) {
        List<FeatureDescriptor> features = new LinkedList<>();
        try {
            Enumeration<URL> resources = classLoader.getResources("META-INF/helidon/feature-metadata.properties");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Properties props = new Properties();
                try (InputStream in = url.openStream()) {
                    props.load(in);
                }
                String module = props.getProperty("m");
                if (module == null) {
                    LOGGER.log(Level.WARNING, "Got module descriptor with no module name. Available properties: " + props);
                    continue;
                }
                FeatureDescriptor.Builder builder = FeatureDescriptor.builder();
                builder.name(props.getProperty("n", module))
                        .module(module)
                        .description(props.getProperty("d", ""))
                        .path(toArray(props.getProperty("p"), props.getProperty("n")))
                        .flavor(toFlavor(module, props.getProperty("in"), true))
                        .notFlavor(toFlavor(module, props.getProperty("not"), false));

                if ("true".equals(props.getProperty("e"))) {
                    builder.experimental(true);
                }
                if ("false".equals(props.getProperty("aot"))) {
                    builder.nativeSupported(false);
                }
                String aotDescription = props.getProperty("aotd");
                if (aotDescription != null) {
                    builder.nativeDescription(aotDescription);
                }
                features.add(builder.build());
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not discover Helidon features", e);
        }
        Collections.sort(features);
        return features;
    }

    private static HelidonFlavor[] toFlavor(String module, String flavorString, boolean useAllIfMissing) {
        if (flavorString == null || flavorString.isBlank()) {
            return useAllIfMissing ? HelidonFlavor.values() : NO_FLAVORS;
        }
        String[] values = toArray(flavorString, flavorString);
        HelidonFlavor[] result = new HelidonFlavor[values.length];
        for (int i = 0; i < values.length; i++) {
            try {
                result[i] = HelidonFlavor.valueOf(values[i]);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.ERROR, "Invalid flavor defined: " + values[i] + " in module " + module);
                return NO_FLAVORS;
            }
        }
        return result;
    }

    private static String[] toArray(String property, String defaultValue) {
        String toProcess = property;
        if (property == null) {
            toProcess = defaultValue;
        }
        if (toProcess == null) {
            return new String[0];
        }
        return toProcess.split(",");
    }

    private static void add(String packageName,
                            String name,
                            String description,
                            String... path) {
    }

    private static void addNima(String packageName,
                                String name,
                                String description,
                                String... path) {
    }

    private static void addSe(String packageName,
                              String name,
                              String description,
                              String... path) {

    }

    private static void addMp(String packageName,
                              String name,
                              String description,
                              String... path) {

    }

    private static void add(String packageName, Supplier<FeatureDescriptor> descriptorBuilder) {

    }
}