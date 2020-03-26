package net.vesseldoc.server;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerApplicationTest {

    @LocalServerPort
    private int port;

    static String token;

    @Test
    @Order(1)
    void testLogin() {
        HttpURLConnection c = null;

        try {
            URL url = new URL("http://localhost:" + port + "/authenticate");

            String boundary = UUID.randomUUID().toString();
            c = (HttpURLConnection) url.openConnection();

            String username = "test1";
            String password = "test1";
            /*
            String userpass = username + ":" + password;
            String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));
            c.setRequestProperty("Authorization", basicAuth);

             */

            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Type", "application/json;charset=UTF-8;boundary=----WebKitFormBoundary" + boundary);

            DataOutputStream request = new DataOutputStream(c.getOutputStream());

            request.writeBytes("{\n" +
                    "\"username\" : \"" + username + "\",\n" +
                    "\"password\" : \"" + password + "\"\n" +
                    "}");

            request.flush();

            if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                String response = br.readLine();
                System.out.println("Response: " + response);

                String[] splitted = response.split("\"");
                token = splitted[3];
                System.out.println("Token : " + token);

                boolean match = token.matches("^[a-zA-Z0-9._-]+$");
                Assert.isTrue(match, "Did not return token.");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                String response = br.readLine();
                System.out.println("Bad response: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(c != null) c.disconnect();
        }
    }

    @Test
    @Order(2)
    void testAddNewForm() {
        HttpURLConnection c = null;

        try {
            URL url = new URL("http://localhost:" + port + "/newForm?structure_id=1");

            String boundary = UUID.randomUUID().toString();
            c = (HttpURLConnection) url.openConnection();

            System.out.println("Adding new form with token: " + token);

            c.setDoOutput(true);
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Type", "multipart/form-data;charset=UTF-8;boundary=----WebKitFormBoundary" + boundary);
            c.setRequestProperty("Authorization", "Bearer " + token);

            DataOutputStream request = new DataOutputStream(c.getOutputStream());

            request.writeBytes("------WebKitFormBoundary" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"structure_id\"\r\n");
            request.writeBytes("Content-Type: text/plain\r\n\r\n");
            request.writeBytes(1 + "\r\n");

            request.writeBytes("------WebKitFormBoundary" + boundary + "--\r\n");
            request.flush();

            if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                String response = br.readLine();
                System.out.println("Response: " + response);

                boolean match = response.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
                Assert.isTrue(match, "Did not return form id.");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                String response = br.readLine();
                System.out.println("Bad response: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(c != null) c.disconnect();
        }
    }

    @Test
    @Order(3)
    void testListUsersForms() {
        HttpURLConnection c = null;

        try {
            URL url = new URL("http://localhost:" + port + "/getUsersForms");
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Authorization", "Bearer " + token);

            if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                String response = br.readLine();
                System.out.println("Response: " + response);

                //boolean match = response.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
                //Assert.isTrue(match, "Did not return form id.");
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
                String response = br.readLine();
                System.out.println("Bad response: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(c != null) c.disconnect();
        }
    }
}
