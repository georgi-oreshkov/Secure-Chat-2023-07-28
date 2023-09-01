package com.jorji.chat.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Map;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String yamlFilePath;
        if (args.length == 0) {
            logger.info("No yml settings file specified. Using default settings.");
            yamlFilePath = "client/src/main/resources/client_settings.yml";
        } else if (args.length == 1) {
            yamlFilePath = args[0];
        } else {
            throw new RuntimeException();
        }


        try {
            Yaml yaml = new Yaml();
            FileInputStream inputStream = new FileInputStream(yamlFilePath);
            Map<String, Object> properties = yaml.load(inputStream);
            inputStream.close();

            Integer clientCount = (Integer) properties.get("client-count");
            Thread[] threads = new Thread[clientCount];

            for (int i = 0; i < clientCount; i++) {
                ClientThread clientThread = new ClientThread(properties, i);
                URL registerUrl = URI.create((String) properties.get("register-url")).toURL();
                clientThread.register(registerUrl);
                Thread thread = new Thread(clientThread);
                threads[i] = thread;
                Thread.sleep(20);
            }

            long time = System.currentTimeMillis();
            for (Thread thread : threads) {
                thread.start();
            }

            logger.info("Main method is waiting for all threads to complete...");

            try {
                for (Thread thread : threads) {
                    thread.join();
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            Thread.sleep(1000);
            double totalTime = (double) (System.currentTimeMillis() - time) / 1000 - 1;
            logger.info("All threads finished in {} seconds", totalTime);

            NumberFormat numberFormatDefault = NumberFormat.getInstance();
            long totalMsg = (long) (int) properties.get("message-count")
                    * clientCount
                    * clientCount;
            logger.info("Total messages sent: {}",
                    numberFormatDefault.format(totalMsg));
            logger.info("This implies an average rate of {} msg/s",
                    numberFormatDefault.format(
                            totalMsg / totalTime
                    ));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
