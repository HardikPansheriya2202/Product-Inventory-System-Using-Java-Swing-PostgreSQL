package com.logilite.component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.logilite.dao.SupplierDao;
import com.logilite.supplier.SupplierDashboard;

public class SupplierReport
{
	public static String[]					supplierDetails		= new String[4];
	public static Map<Integer, String[]>	UserSupplierData	= new HashMap<Integer, String[]>();

	public static void reportPrint(String order_status) throws Exception
	{
		List<String> headerList = new ArrayList<String>();
		headerList.add("Order Id");
		headerList.add("User Id");
		headerList.add("User Name");
		headerList.add("User Phone");
		headerList.add("Total");
		headerList.add("Purchase Date");
		headerList.add("Received Date");

		SupplierDao.getSupplierDataReport(SupplierDashboard.lblSupplieremail.getText());
		Map<Integer, String[]> supplierData = SupplierDao.getAllSupplierDataReport(supplierDetails[0], order_status);

		String projectDir = System.getProperty("user.dir");
		String reportsDir = projectDir + File.separator + "reports";

		try
		{
			Path path = Paths.get(reportsDir);
			if (!Files.exists(path))
			{
				Files.createDirectories(path);
			}

			String dest = reportsDir + File.separator + "SupplierReport.pdf";

			PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
			PdfFont contentFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

			PdfWriter writer = new PdfWriter(dest);
			PdfDocument pdf = new PdfDocument(writer);
			pdf.setDefaultPageSize(PageSize.A4);
			Document document = new Document(pdf);

			ImageData imageData = ImageDataFactory
					.create(SupplierReport.class.getResource("/com/logilite/img/login_page_logo.png"));
			Image image = new Image(imageData);
			image.setWidth(50);
			image.setHeight(50);
			image.setFixedPosition(140, 785);
			document.add(image);

			Paragraph title = new Paragraph().add(" Product Inventory System").setBold().setFontSize(20)
					.setTextAlignment(TextAlignment.CENTER);
			document.add(title.setFixedPosition(110, 790, 400));

			Paragraph orderDetails = new Paragraph().add("Supplier Name : " + supplierDetails[0] + "\n")
					.add("Email : " + supplierDetails[1] + "\n").add("Phone : " + supplierDetails[2] + "\n")
					.add("Address : " + supplierDetails[3] + "\n").add("Order Status: " + order_status + "\n")
					.setFontSize(12);
			document.add(orderDetails.setFixedPosition(37, 650, 500));

			ImageData imageData1 = ImageDataFactory
					.create(SupplierReport.class.getResource("/com/logilite/img/manage_suppliers.png"));
			Image image1 = new Image(imageData1);
			image1.setWidth(25);
			image1.setHeight(25);
			image1.setFixedPosition(37, 750);
			document.add(image1);

			Paragraph description = new Paragraph().add("Supplier Deliveries Details").setFont(boldFont).setBold()
					.setFontSize(14);
			document.add(description.setFixedPosition(70, 750, 400));

			try
			{
				ImageData imageData2 = ImageDataFactory
						.create(SupplierReport.class.getResource("/com/logilite/img/direction.png"));
				Image image2 = new Image(imageData2);
				image2.setWidth(25);
				image2.setHeight(25);
				image2.setFixedPosition(37, 600);
				document.add(image2);

				Paragraph description1 = new Paragraph().add("Deliveries (" + order_status + ") :")
						.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC)).setBold().setFontSize(14);
				document.add(description1.setFixedPosition(70, 600, 400));
			}
			catch (IOException e)
			{
				Logger.getLogger(SupplierReport.class.getName()).log(Level.SEVERE, null, e);
			}

			float[] columnWidths = { 1, 1, 1, 1, 1, 1, 1 };
			Table table = new Table(columnWidths).useAllAvailableWidth();

			for (String s : headerList)
			{
				table.addCell(new Cell()
						.add(new Paragraph(s).setFont(headerFont).setFontSize(10)
								.setTextAlignment(TextAlignment.CENTER))
						.setBackgroundColor(new com.itextpdf.kernel.colors.DeviceRgb(230, 230, 230)).setPadding(5));
			}

			for (Map.Entry<Integer, String[]> entry : supplierData.entrySet())
			{
				String[] rowData = entry.getValue();
				for (String cellData : rowData)
				{
					table.addCell(new Cell().add(new Paragraph(cellData).setFont(contentFont).setFontSize(9)
							.setTextAlignment(TextAlignment.CENTER)).setPadding(5));
				}
			}

			document.add(table.setMarginTop(220));

			Paragraph termsTitle = new Paragraph("Terms & Conditions :\n").setFont(contentFont).setBold()
					.setFontSize(13).setTextAlignment(TextAlignment.LEFT);
			document.add(termsTitle.setFixedPosition(36, 120, 530));
			Paragraph terms = new Paragraph(
					"1. All sales are final and non-refundable.\n" + "2. Products are subject to availability.\n"
							+ "3. Please contact support@inventorysystem.com for any issues.\n"
							+ "4. Unauthorized use of this document is prohibited.").setFont(contentFont)
									.setFontSize(13).setTextAlignment(TextAlignment.LEFT);
			document.add(terms.setFixedPosition(36, 36, 530));

			document.close();
			openPDF(dest);

		}
		catch (IOException e)
		{
			Logger.getLogger(SupplierReport.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	private static void openPDF(String filePath)
	{
		String os = System.getProperty("os.name").toLowerCase();
		Runtime runtime = Runtime.getRuntime();

		try
		{
			if (os.contains("win"))
			{
				runtime.exec(new String[] { "cmd", "/c", "start", filePath });
			}
			else if (os.contains("mac"))
			{
				runtime.exec(new String[] { "open", filePath });
			}
			else if (os.contains("nix") || os.contains("nux"))
			{
				runtime.exec(new String[] { "xdg-open", filePath });
			}
			else
			{
				Logger.getLogger(SupplierReport.class.getName()).log(Level.SEVERE, "Unsupported operating system.");
			}
		}
		catch (IOException e)
		{
			Logger.getLogger(SupplierReport.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
