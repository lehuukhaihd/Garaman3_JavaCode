package com.garaman3.launcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public final class EmbeddedTomcat {

    private static final String CONTEXT_PATH = "/GaraMan3_Web";

    private EmbeddedTomcat() {
    }

    public static void main(String[] args) throws Exception {
        Path warFile = args.length > 0
                ? Paths.get(args[0]).toAbsolutePath().normalize()
                : Paths.get("target", "GaraMan3_Web.war").toAbsolutePath().normalize();
        int port = parsePort(args.length > 1 ? args[1] : System.getProperty("server.port", "8080"));
        if (!Files.isRegularFile(warFile)) {
            throw new IllegalArgumentException("Application WAR was not found: " + warFile);
        }

        Path baseDirectory = Paths.get(System.getProperty("user.home"), ".garaman3", "tomcat")
                .toAbsolutePath().normalize();
        Files.createDirectories(baseDirectory);

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDirectory.toString());
        tomcat.setPort(port);
        tomcat.getConnector().setProperty("address", "127.0.0.1");

        Context context = tomcat.addWebapp(CONTEXT_PATH, warFile.toString());
        context.setParentClassLoader(Thread.currentThread().getContextClassLoader());

        tomcat.start();
        System.out.println("GaraMan3 is running at http://localhost:" + port + CONTEXT_PATH + "/");
        System.out.println("Press Ctrl+C to stop the embedded Tomcat server.");
        tomcat.getServer().await();
    }

    private static int parsePort(String value) {
        final int port;
        try {
            port = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Port must be a number from 1 to 65535: " + value, ex);
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Port must be from 1 to 65535: " + port);
        }
        return port;
    }
}
