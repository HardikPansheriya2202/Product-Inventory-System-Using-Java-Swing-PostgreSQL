package com.logilite.stripe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class StripeCheckoutBackend
{
	public static String	payment_id		= null;
	public static String	payment_Status	= null;

	public static void Start() throws IOException
	{
		int port = 8081;

		if (isPortInUse(port))
		{
			stopProcessUsingPort(port);
		}

		Stripe.apiKey = "Your stripe api key";
		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/createCheckoutSession", new CreateCheckoutSessionHandler());
		server.createContext("/success", new SuccessHandler());
		server.createContext("/cancel", new CancelHandler());
		server.createContext("/thankyou", new ThankYouHandler());
		server.start();
	}

	private static boolean isPortInUse(int port)
	{
		try (var socket = new java.net.ServerSocket(port))
		{
			socket.setReuseAddress(true);
			return false;
		}
		catch (IOException e)
		{
			return true;
		}
	}

	private static void stopProcessUsingPort(int port)
	{
		String command;
		boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

		if (isWindows)
		{
			command = "netstat -ano | findstr :" + port;
		}
		else
		{
			command = "lsof -i :" + port + " | grep LISTEN";
		}

		try
		{
			Process process;
			if (isWindows)
			{
				process = Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", command });
			}
			else
			{
				process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null)
			{
				String pid = extractPidFromLine(line, isWindows);
				if (pid != null)
				{
					killProcess(pid, isWindows);
				}
			}
		}
		catch (IOException e)
		{
			Logger.getLogger(StripeCheckoutBackend.class.getName()).log(Level.SEVERE,
					"Failed to stop the process using port " + port, e);
		}
	}

	private static String extractPidFromLine(String line, boolean isWindows)
	{
		if (isWindows)
		{
			String[] parts = line.trim().split("\\s+");
			return parts[parts.length - 1];
		}
		else
		{
			String[] parts = line.trim().split("\\s+");
			return parts[1];
		}
	}

	private static void killProcess(String pid, boolean isWindows)
	{
		String killCommand = isWindows ? "taskkill /F /PID " + pid : "kill -9 " + pid;

		try
		{
			Process killProcess = Runtime.getRuntime().exec(killCommand);
			killProcess.waitFor();
		}
		catch (Exception e)
		{
			Logger.getLogger(StripeCheckoutBackend.class.getName()).log(Level.SEVERE,
					"Failed to stop process with PID " + pid, e);
		}
	}

	static class CreateCheckoutSessionHandler implements HttpHandler
	{
		@Override
		public void handle(HttpExchange exchange) throws IOException
		{
			try
			{
				String amount = exchange.getRequestURI().getQuery();
				amount = amount.split("=")[1];

				SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
						.setSuccessUrl("http://localhost:8081/success").setCancelUrl("http://localhost:8081/cancel")
						.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
								.setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency("inr")
										.setUnitAmount(Long.parseLong(amount))
										.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
												.setName("Sample Product").setDescription("A sample product for sale.")
												.build())
										.build())
								.build())
						.build();

				Session session = Session.create(params);

				String sessionUrl = session.getUrl();
				payment_id = session.getPaymentIntent();
				exchange.getResponseHeaders().set("Content-Type", "text/plain");
				exchange.sendResponseHeaders(200, sessionUrl.getBytes().length);
				try (OutputStream os = exchange.getResponseBody())
				{
					os.write(sessionUrl.getBytes());
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				exchange.sendResponseHeaders(500, -1);
			}
		}
	}

	static class SuccessHandler implements HttpHandler
	{
		@Override
		public void handle(HttpExchange exchange) throws IOException
		{
			try
			{
				PaymentIntent paymentIntent = PaymentIntent.retrieve(payment_id);

				payment_Status = paymentIntent.getStatus();
				String response = "<html><body>" + "<script type=\"text/javascript\">"
						+ "alert('Payment was successfully completed!');" + "if (window.opener) { window.close(); }"
						+ "else { window.location.href = 'http://localhost:8081/thankyou'; }"
						+ "var blob = new Blob([''], { type: \"application/json\" });"
						+ "var a = document.createElement('a');" + "a.href = URL.createObjectURL(blob);"
						+ "a.download = \"paymentStatus.json\";" + "a.click();" + "</script>" + "</body></html>";
				exchange.getResponseHeaders().set("Content-Type", "text/html");
				exchange.sendResponseHeaders(200, response.getBytes().length);
				try (OutputStream os = exchange.getResponseBody())
				{
					os.write(response.getBytes());
				}
			}
			catch (Exception e)
			{
				String errorResponse = "<html><body>" + "<script type=\"text/javascript\">"
						+ "alert('There was an issue with processing your payment. Please try again later.');"
						+ "window.close();" + "</script>" + "</body></html>";
				exchange.getResponseHeaders().set("Content-Type", "text/html");
				exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
				try (OutputStream os = exchange.getResponseBody())
				{
					os.write(errorResponse.getBytes());
				}
				e.printStackTrace();
			}
		}
	}

	static class CancelHandler implements HttpHandler
	{
		@Override
		public void handle(HttpExchange exchange) throws IOException
		{
			String response = "<html><body>" + "<script type=\"text/javascript\">"
					+ "alert('Your payment has been canceled.');" + "window.close();" + "</script>" + "</body></html>";
			exchange.getResponseHeaders().set("Content-Type", "text/html");
			exchange.sendResponseHeaders(200, response.getBytes().length);
			try (OutputStream os = exchange.getResponseBody())
			{
				os.write(response.getBytes());
			}
		}
	}

	static class ThankYouHandler implements HttpHandler
	{
		@Override
		public void handle(HttpExchange exchange) throws IOException
		{
			String response = "<html><body>" + "<h1>Thank You for Your Payment!</h1>"
					+ "<p>Your payment was successfully processed.</p>" + "</body></html>";

			exchange.getResponseHeaders().set("Content-Type", "text/html");
			exchange.sendResponseHeaders(200, response.getBytes().length);
			try (OutputStream os = exchange.getResponseBody())
			{
				os.write(response.getBytes());
			}
		}
	}

}