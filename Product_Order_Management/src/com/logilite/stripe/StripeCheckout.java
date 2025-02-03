package com.logilite.stripe;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import com.logilite.user.Purchase;
 
public class StripeCheckout {
 
	@SuppressWarnings("static-access")
    public void start() throws IOException {
        new StripeCheckoutBackend().Start();

        String amount = Purchase.txtGrandTotal.getText().substring(0, Purchase.txtGrandTotal.getText().length()-3);

        try {
            String sessionUrl = createCheckoutSession(amount + "00");

            if (sessionUrl != null && !sessionUrl.isEmpty()) {
                SwingUtilities.invokeLater(() -> openBrowser(sessionUrl));
            } else {
                Logger.getLogger(StripeCheckout.class.getName()).log(Level.SEVERE, null, "Error creating session. Please try again.");
            }
        } catch (Exception ex) {
            Logger.getLogger(StripeCheckout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String createCheckoutSession(String amount) throws IOException {
        URL url = new URL("http://localhost:8081/createCheckoutSession?amount=" + amount);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            Logger.getLogger(StripeCheckout.class.getName()).log(Level.SEVERE, null, "Failed to create session. Server returned: " + responseCode);
            return null;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String sessionUrl = reader.readLine();
        reader.close();

        return sessionUrl;
    }

    private static void openBrowser(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", "", url)
                        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                        .redirectError(ProcessBuilder.Redirect.DISCARD)
                        .start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url)
                        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                        .redirectError(ProcessBuilder.Redirect.DISCARD)
                        .start();
            } else if (os.contains("nix") || os.contains("nux")) {
                new ProcessBuilder("xdg-open", url)
                        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                        .redirectError(ProcessBuilder.Redirect.DISCARD)
                        .start();
            }
        } catch (IOException e) {
            Logger.getLogger(StripeCheckout.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}