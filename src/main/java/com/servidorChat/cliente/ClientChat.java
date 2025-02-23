package com.servidorChat.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientChat {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 2001;

    public static void main(String[] args) {
        System.out.println(">> Se inicia el cliente del chat");
        try (Socket socket = new Socket(HOST, PORT)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Thread treadListener = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = input.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Error en la conexión: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });
            treadListener.start();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Escuchando consola");
            while (true) {
                String consoleMessage = scanner.nextLine();
                output.println(consoleMessage);

                if (consoleMessage.equalsIgnoreCase("/salir")) {
                    System.out.println("Cerrando el chat...");
                    break;
                }
            }

            scanner.close();
            socket.close();
            System.out.println("Conexión cerrada");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
