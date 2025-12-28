package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javax.swing.*;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/* @author Jhonny */
public class BoletaPDF {

    public static void generarBoletaPDF(int idVenta, String nombreCliente,
            String dni, JTable tabla, BigDecimal total) {

        String userHome = System.getProperty("user.home");
        String carpetaRaiz = userHome + File.separator + "Documents" + File.separator + "Prodotti";
        String carpetaPDF = carpetaRaiz + File.separator + "PDF";

        File dir = new File(carpetaPDF);
        if (!dir.exists()) {dir.mkdirs();}

        String nombreArchivo = carpetaPDF + File.separator
                + String.format("Boleta_%08d_%s.pdf",
                        idVenta, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));

        try {
            Document documento = new Document(PageSize.A4);
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font fuenteNormal = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            Font fuenteNegrita = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
            Font fuentePequena = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY);

            Paragraph titulo = new Paragraph("BOLETA DE VENTA ELECTRÓNICA", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            Paragraph empresa = new Paragraph("PRODOTTI STORE", fuenteSubtitulo);
            empresa.setAlignment(Element.ALIGN_CENTER);
            empresa.setSpacingBefore(10);
            documento.add(empresa);

            Paragraph ruc = new Paragraph("RUC: 20123456789", fuenteNormal);
            ruc.setAlignment(Element.ALIGN_CENTER);
            documento.add(ruc);

            Paragraph direccion = new Paragraph("Av. Principal 123 - Lima, Perú | Tel: (01) 123-4567", fuenteNormal);
            direccion.setAlignment(Element.ALIGN_CENTER);
            documento.add(direccion);

            // Línea separadora
            documento.add(new Paragraph(" "));
            LineSeparator linea = new LineSeparator();
            documento.add(new Chunk(linea));
            documento.add(new Paragraph(" "));

            // ============== INFORMACIÓN DE LA BOLETA ==============
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            PdfPTable tablaInfo = new PdfPTable(2);
            tablaInfo.setWidthPercentage(100);
            tablaInfo.setSpacingBefore(10);
            tablaInfo.setSpacingAfter(10);

            PdfPCell celdaIzq = new PdfPCell();
            celdaIzq.setBorder(Rectangle.NO_BORDER);
            celdaIzq.addElement(new Paragraph("BOLETA N°: " + String.format("%08d", idVenta), fuenteNegrita));
            celdaIzq.addElement(new Paragraph("FECHA: " + sdf.format(new Date()), fuenteNormal));
            celdaIzq.addElement(new Paragraph("CAJERO: Sistema", fuenteNormal));
            tablaInfo.addCell(celdaIzq);

            PdfPCell celdaDer = new PdfPCell();
            celdaDer.setBorder(Rectangle.NO_BORDER);
            celdaDer.addElement(new Paragraph("CLIENTE: " + nombreCliente, fuenteNegrita));
            celdaDer.addElement(new Paragraph("DNI/RUC: " + dni, fuenteNormal));
            tablaInfo.addCell(celdaDer);

            documento.add(tablaInfo);
            documento.add(new Chunk(linea));
            documento.add(new Paragraph(" "));

            PdfPTable tablaProductos = new PdfPTable(5);
            tablaProductos.setWidthPercentage(100);
            tablaProductos.setWidths(new float[]{1.5f, 4f, 1.5f, 2f, 2f});
            tablaProductos.setSpacingBefore(10);
            tablaProductos.setSpacingAfter(10);

            String[] encabezados = {"CÓDIGO", "DESCRIPCIÓN", "CANT.", "PRECIO", "TOTAL"};
            for (String encabezado : encabezados) {
                PdfPCell celda = new PdfPCell(new Phrase(encabezado, fuenteNegrita));
                celda.setBackgroundColor(new BaseColor(52, 152, 219));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(8);
                celda.setBorderColor(BaseColor.WHITE);
                tablaProductos.addCell(celda);
            }

            BigDecimal subtotalCalculado = BigDecimal.ZERO;

            for (int i = 0; i < tabla.getRowCount(); i++) {
                PdfPCell celdaCodigo = new PdfPCell(new Phrase(
                        tabla.getValueAt(i, 0).toString(), fuenteNormal));
                celdaCodigo.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaCodigo.setPadding(5);
                tablaProductos.addCell(celdaCodigo);

                PdfPCell celdaDesc = new PdfPCell(new Phrase(
                        tabla.getValueAt(i, 1).toString(), fuenteNormal));
                celdaDesc.setPadding(5);
                tablaProductos.addCell(celdaDesc);

                PdfPCell celdaCant = new PdfPCell(new Phrase(
                        tabla.getValueAt(i, 2).toString(), fuenteNormal));
                celdaCant.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaCant.setPadding(5);
                tablaProductos.addCell(celdaCant);

                String precio = tabla.getValueAt(i, 3).toString();
                PdfPCell celdaPrecio = new PdfPCell(new Phrase(
                        "S/ " + precio, fuenteNormal));
                celdaPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaPrecio.setPadding(5);
                tablaProductos.addCell(celdaPrecio);

                String totalLinea = tabla.getValueAt(i, 4).toString();
                PdfPCell celdaTotal = new PdfPCell(new Phrase(
                        "S/ " + totalLinea, fuenteNormal));
                celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaTotal.setPadding(5);
                tablaProductos.addCell(celdaTotal);

                subtotalCalculado = subtotalCalculado.add(new BigDecimal(totalLinea));
            }

            documento.add(tablaProductos);

            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(40);
            tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tablaTotales.setWidths(new float[]{2f, 1.5f});

            BigDecimal igv = subtotalCalculado.multiply(new BigDecimal("0.18"));
            BigDecimal subtotal = subtotalCalculado.subtract(igv);

            agregarFilaTotal(tablaTotales, "SUB TOTAL:",
                    "S/ " + String.format("%.2f", subtotal), fuenteNormal);

            agregarFilaTotal(tablaTotales, "IGV (18%):",
                    "S/ " + String.format("%.2f", igv), fuenteNormal);

            PdfPCell celdaTotalLabel = new PdfPCell(new Phrase("TOTAL:", fuenteSubtitulo));
            celdaTotalLabel.setBorder(Rectangle.NO_BORDER);
            celdaTotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaTotalLabel.setPadding(5);
            celdaTotalLabel.setBackgroundColor(new BaseColor(52, 152, 219));
            tablaTotales.addCell(celdaTotalLabel);

            PdfPCell celdaTotalValor = new PdfPCell(new Phrase(
                    "S/ " + String.format("%.2f", total), fuenteSubtitulo));
            celdaTotalValor.setBorder(Rectangle.NO_BORDER);
            celdaTotalValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaTotalValor.setPadding(5);
            celdaTotalValor.setBackgroundColor(new BaseColor(52, 152, 219));
            tablaTotales.addCell(celdaTotalValor);
            documento.add(tablaTotales);

            documento.add(new Paragraph(" "));
            documento.add(new Paragraph(" "));

            Paragraph agradecimiento = new Paragraph("¡GRACIAS POR SU COMPRA!", fuenteSubtitulo);
            agradecimiento.setAlignment(Element.ALIGN_CENTER);
            documento.add(agradecimiento);

            Paragraph vuelva = new Paragraph("Vuelva pronto - Estamos para servirle", fuenteNormal);
            vuelva.setAlignment(Element.ALIGN_CENTER);
            documento.add(vuelva);

            documento.add(new Paragraph(" "));

            Paragraph web = new Paragraph("www.prodottiStore.com | info@prodottiStore.com", fuentePequena);
            web.setAlignment(Element.ALIGN_CENTER);
            documento.add(web);

            Paragraph representacion = new Paragraph(
                    "Representación impresa de la boleta electrónica", fuentePequena);
            representacion.setAlignment(Element.ALIGN_CENTER);
            documento.add(representacion);

            Paragraph consulta = new Paragraph(
                    "Consulte su boleta en: www.sunat.gob.pe", fuentePequena);
            consulta.setAlignment(Element.ALIGN_CENTER);
            documento.add(consulta);
            documento.close();
            abrirPDF(nombreArchivo);

            JOptionPane.showMessageDialog(null,
                    "Boleta PDF generada exitosamente en:\n" + nombreArchivo,
                    "Boleta Generada",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (DocumentException | HeadlessException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al generar PDF: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void agregarFilaTotal(PdfPTable tabla, String label,
            String valor, Font fuente) {
        PdfPCell celdaLabel = new PdfPCell(new Phrase(label, fuente));
        celdaLabel.setBorder(Rectangle.NO_BORDER);
        celdaLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaLabel.setPadding(5);
        tabla.addCell(celdaLabel);

        PdfPCell celdaValor = new PdfPCell(new Phrase(valor, fuente));
        celdaValor.setBorder(Rectangle.NO_BORDER);
        celdaValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaValor.setPadding(5);
        tabla.addCell(celdaValor);
    }

    private static void abrirPDF(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(archivo);
            }
        } catch (IOException e) {
            System.out.println("No se pudo abrir el PDF automáticamente: " + e.getMessage());
        }
    }
}
