package com.zinkworks.services.ndoslackbotpoc;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.DockerComposeContainer;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TestEnvironment {

    private static TestEnvironment instance;
    private static AtomicBoolean initialised = new AtomicBoolean(false);
    private static Object mutex = new Object();
    private DockerComposeContainer dockerCompose;

    @Synchronized
    public static final TestEnvironment start() {
        if(instance == null) {
            synchronized (mutex) {
                if(instance == null) {
                    instance = new TestEnvironment();
                }
            }
        }
        try {
            if (!initialised.get()) {
                instance.initialise();
                initialised.set(true);
            }
        } catch (final Exception ex) {
            log.error("Failed to start Docker Compose due to {}", ex);
        }
        return instance;
    }

    private void initialise() throws Exception {
//        dockerCompose =
//                new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
//                        .withLocalCompose(true);
//        dockerCompose.start();
        Thread.sleep(5000);
    }
}
