package com.logilite.razorpay;

import java.io.File;

public class RazorpayClient
{
	public static String generateHtmlForPayment(double totalAmount, String orderId) {
        return """
            <html>
                <head>
                    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
                </head>
                <body>
                    <script>
                        var options = {
                            "key": "Your razorpay key",
                            "amount": "%s",
                            "currency": "INR",
                            "name": "Your company name",
                            "description": "Test Transaction",
                            "image": "https://th.bing.com/th/id/OIP.dYcdoj72pEbGPNEqR678dAHaF0?w=229&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7",
                            "order_id": "%s",
                            "handler": function(response) {
                                var paymentId = response.razorpay_payment_id;
                                var orderId = response.razorpay_order_id;
                                var signature = response.razorpay_signature;
                                var paymentData = {
                                    orderId: orderId,
                                    paymentId: paymentId,
                                    signature: signature,
                                    status: "success",
                                    amount: response.amount,
                                    currency: response.currency,
                                    createdAt: response.created_at,
                                    method: response.method,
                                    cardId: response.card_id,
                                    bank: response.bank,
                                    email: response.email,
                                    contact: response.contact
                                };
                                var paymentDataString = JSON.stringify(paymentData);
                                var blob = new Blob([paymentDataString], { type: "application/json" });
                                var a = document.createElement('a');
                                a.href = URL.createObjectURL(blob);
                                a.download = "paymentStatus.json";
                                a.click();
                                setTimeout(function() {
						            window.close();
						        }, 2000);
                            },
                            "error": function(response) {
                                var errorMessage = response.error.description;
                                console.error("Payment Failed: " + errorMessage);
                                var paymentData = {
                                    status: "failed",
                                    errorDescription: errorMessage,
                                    errorCode: response.error.code,
                                    errorReason: response.error.reason,
                                    errorSource: response.error.source,
                                    errorStep: response.error.step,
                                    errorMethod: response.error.method,
                                    orderId: response.error.order_id || "Not available",
                                    paymentId: response.error.payment_id || "Not available"
                                };
                                var paymentDataString = JSON.stringify(paymentData);
                                var blob = new Blob([paymentDataString], { type: "application/json" });
                                var a = document.createElement('a');
                                a.href = URL.createObjectURL(blob);
                                a.download = "paymentStatus.json";
                                a.click();
                                alert("Payment failed!");
                                setTimeout(function() {
						            window.close();
						        }, 2000);
                            },
                            "prefill": {
                                "name": "John Doe",
                                "email": "johndoe@example.com",
                                "contact": "9876543210"
                            },
                            "theme": {
                                "color": "#3399cc"
                            }
                        };
                        var rzp1 = new Razorpay(options);
                        rzp1.open();
                        
                        rzp1.on('payment.failed', function(response) {
					    var paymentData = {
					        status: "cancelled",
							errorDescription: "Payment cancelled by the user",
							orderId: "Not available",
							paymentId: "Not available",
        					errorCode: response.error.code,
        					errorReason: response.error.reason,
        					errorSource: response.error.source,
        		       		errorStep: response.error.step,
        		       		errorMethod: response.error.method
					    };
			
        		       	var paymentDataString = JSON.stringify(paymentData);
        				var blob = new Blob([paymentDataString], { type: "application/json" });
			
					    var a = document.createElement('a');
					    a.href = URL.createObjectURL(blob);
					    a.download = "paymentStatus.json";
					    a.click();
			
        		       	alert("Payment cancelled!");
        		   		setTimeout(function() {
							window.close();
						}, 2000);
				      });
                    </script>
                </body>
            </html>
        """.formatted(totalAmount, orderId);
    }
	
	public static String generateWrapperHtml(File tempFile) {
        return """
            <html>
                <body>
                    <script>
                        const paymentWindow = window.open("file://%s");
                        const interval = setInterval(() => {
                            if (paymentWindow.closed) {
                                clearInterval(interval);
                                window.close();
                            }
                        }, 500);
                    </script>
                </body>
            </html>
        """.formatted(tempFile.getAbsolutePath().replace("\\", "/"));
    }
	
}
