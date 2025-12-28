
package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
/* @author Jhonny */

public class HojasExcel {
     public void exportarTablaAsync(JTable tabla, String nombreTabla) {

    SwingWorker<Void, Void> worker = new SwingWorker<>() {

        @Override
        protected Void doInBackground() throws Exception {

            // ===============================
            //  CREAR RUTA AUTOMÁTICAMENTE
            // ===============================
            String userHome = System.getProperty("user.home");

            String carpetaBase = userHome
                    + File.separator + "Documents"
                    + File.separator + "Prodotti";

            String carpetaExcel = carpetaBase
                    + File.separator + "ReportesExcel";

            // Crear carpetas si no existen
            File base = new File(carpetaBase);
            if (!base.exists()) base.mkdirs();

            File excelFolder = new File(carpetaExcel);
            if (!excelFolder.exists()) excelFolder.mkdirs();

            // Ruta final del archivo
            String rutaArchivo = carpetaExcel
                    + File.separator
                    + "Reporte_" + nombreTabla + ".xlsx";

            // ===============================
            //  LLAMAR AL EXPORTADOR PRINCIPAL
            // ===============================
            exportar(tabla, rutaArchivo);

            return null;
        }

        @Override
        protected void done() {
            JOptionPane.showMessageDialog(null,
                    "Exportación completada correctamente.");
        }
    };

    worker.execute();
}

// ============================================================
//  EXPORTACIÓN PRINCIPAL (DINÁMICA)
// ============================================================

private void exportar(JTable tabla, String nombreArchivo) throws Exception {

    Workbook workbook;
    Sheet sheet;

    File file = new File(nombreArchivo);

    // Si existe el archivo, lo abrimos para actualizar
    if (file.exists()) {
        try (FileInputStream fis = new FileInputStream(file)) {
            workbook = new XSSFWorkbook(fis);
        }
    } else {
        workbook = new XSSFWorkbook();
    }

    sheet = workbook.getSheet("Reporte");

    // Si no existe la hoja, crear encabezado y título
    if (sheet == null) {
        sheet = workbook.createSheet("Reporte");
        crearEncabezado(tabla, sheet, workbook);
    }

    int lastRow = sheet.getLastRowNum() + 1;

    // Estilo filas
    CellStyle rowStyle = estilosFilas(workbook);

    // Agregar filas dinamicámente desde JTable
    for (int i = 0; i < tabla.getRowCount(); i++) {
        Row row = sheet.createRow(lastRow++);

        for (int j = 0; j < tabla.getColumnCount(); j++) {

            Cell cell = row.createCell(j);
            Object value = tabla.getValueAt(i, j);

            cell.setCellValue(value == null ? "" : value.toString());
            cell.setCellStyle(rowStyle);
        }
    }

    // Ajustar columnas
    for (int i = 0; i < tabla.getColumnCount(); i++) {
        sheet.autoSizeColumn(i);
    }

    // Pie de página con fecha
    Row footer = sheet.createRow(lastRow + 1);
    Cell cf = footer.createCell(0);

    String fecha = new SimpleDateFormat("dd/MM/yyyy - hh:mm a").format(new Date());
    cf.setCellValue("Fecha de generación: " + fecha);

    sheet.addMergedRegion(
            new CellRangeAddress(lastRow + 1, lastRow + 1, 0, tabla.getColumnCount() - 1)
    );

    CellStyle footerStyle = workbook.createCellStyle();
    footerStyle.setAlignment(HorizontalAlignment.CENTER);
    cf.setCellStyle(footerStyle);

    // Guardar archivo
    try (FileOutputStream fos = new FileOutputStream(nombreArchivo)) {
        workbook.write(fos);
    }
}

// ============================================================
//  ENCABEZADO + TÍTULO
// ============================================================

private void crearEncabezado(JTable tabla, Sheet sheet, Workbook wb) {

    // Título
    CellStyle titleStyle = wb.createCellStyle();
    Font f = wb.createFont();
    f.setBold(true);
    f.setFontHeightInPoints((short) 16);
    titleStyle.setFont(f);
    titleStyle.setAlignment(HorizontalAlignment.CENTER);

    Row titleRow = sheet.createRow(0);
    Cell titleCell = titleRow.createCell(0);

    titleCell.setCellValue("REPORTE DE DATOS");
    titleCell.setCellStyle(titleStyle);

    // Combinar columnas
    sheet.addMergedRegion(
            new CellRangeAddress(0, 0, 0, tabla.getColumnCount() - 1)
    );

    // Cabeceras dinámicas
    JTableHeader headerTabla = tabla.getTableHeader();
    Row header = sheet.createRow(2);

    CellStyle headerStyle = wb.createCellStyle();
    Font font = wb.createFont();
    font.setBold(true);
    headerStyle.setFont(font);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);
    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    ponerBordes(headerStyle);

    for (int i = 0; i < tabla.getColumnCount(); i++) {
        Cell celda = header.createCell(i);
        celda.setCellValue(headerTabla.getColumnModel().getColumn(i).getHeaderValue().toString());
        celda.setCellStyle(headerStyle);
    }
}

// ============================================================
//  ESTILOS
// ============================================================

private CellStyle estilosFilas(Workbook wb) {
    CellStyle style = wb.createCellStyle();
    ponerBordes(style);
    return style;
}

private void ponerBordes(CellStyle style) {
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
}

}
