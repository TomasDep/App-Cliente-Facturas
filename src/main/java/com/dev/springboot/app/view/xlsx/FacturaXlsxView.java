package com.dev.springboot.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.dev.springboot.app.models.entity.Factura;
import com.dev.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {
	@Override
	protected void buildExcelDocument(
			Map<String, Object> model, 
			Workbook workbook, 
			HttpServletRequest request,
			HttpServletResponse response
	) throws Exception {
		Factura factura = (Factura) model.get("factura");
		MessageSourceAccessor mensajes = getMessageSourceAccessor();
		CreationHelper createHelper = workbook.getCreationHelper();
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + mensajes.getMessage("text.xlsx.fileName")  + "_" 
							+ factura.getCliente().getNombre() + "_" + factura.getCliente().getApellido() + ".xlsx\"");
		
		Sheet sheet = workbook.createSheet("Factura Spring");
		Row row = null;
		Cell cell = null;
		
		CellStyle theaderStyleClient = workbook.createCellStyle();
		theaderStyleClient.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyleClient.setBorderTop(BorderStyle.MEDIUM);
		theaderStyleClient.setBorderRight(BorderStyle.MEDIUM);
		theaderStyleClient.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyleClient.setFillForegroundColor(IndexedColors.AQUA.index);
		theaderStyleClient.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		CellStyle tbodyStyle = workbook.createCellStyle();
		tbodyStyle.setBorderBottom(BorderStyle.THIN);
		tbodyStyle.setBorderTop(BorderStyle.THIN);
		tbodyStyle.setBorderRight(BorderStyle.THIN);
		tbodyStyle.setBorderLeft(BorderStyle.THIN);

		row = sheet.createRow(0);
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,2));
		row.createCell(1).setCellValue(mensajes.getMessage("text.ver.factura.cliente"));
		row.getCell(1).setCellStyle(theaderStyleClient);
		row.createCell(2).setCellStyle(theaderStyleClient);
		
		row = sheet.createRow(1);
		row.createCell(1).setCellValue(mensajes.getMessage("text.xlsx.cliente.identificacion"));
		row.createCell(2).setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		row.getCell(1).setCellStyle(tbodyStyle);
		row.getCell(2).setCellStyle(tbodyStyle);
		
		row = sheet.createRow(2);
		row.createCell(1).setCellValue(mensajes.getMessage("text.listar.cliente.email"));
		row.createCell(2).setCellValue(factura.getCliente().getEmail());
		row.getCell(1).setCellStyle(tbodyStyle);
		row.getCell(2).setCellStyle(tbodyStyle);
		
		CellStyle theaderStyleInvoice = workbook.createCellStyle();
		theaderStyleInvoice.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyleInvoice.setBorderTop(BorderStyle.MEDIUM);
		theaderStyleInvoice.setBorderRight(BorderStyle.MEDIUM);
		theaderStyleInvoice.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyleInvoice.setFillForegroundColor(IndexedColors.LIME.index);
		theaderStyleInvoice.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		row = sheet.createRow(4);
		sheet.addMergedRegion(new CellRangeAddress(4,4,1,2));
		row.createCell(1).setCellValue(mensajes.getMessage("text.ver.factura.datos.subtitulo"));
		row.getCell(1).setCellStyle(theaderStyleInvoice);
		row.createCell(2).setCellStyle(theaderStyleInvoice);
		
		row = sheet.createRow(5);
		row.createCell(1).setCellValue(mensajes.getMessage("text.xlsx.factura.folio"));
		row.createCell(2).setCellValue(factura.getId());
		row.getCell(1).setCellStyle(tbodyStyle);
		row.getCell(2).setCellStyle(tbodyStyle);
		
		row = sheet.createRow(6);
		row.createCell(1).setCellValue(mensajes.getMessage("text.xlsx.factura.descripcion"));
		row.createCell(2).setCellValue(factura.getDescripcion());
		row.getCell(1).setCellStyle(tbodyStyle);
		row.getCell(2).setCellStyle(tbodyStyle);
		
		CellStyle cellDateStyle = workbook.createCellStyle();
		cellDateStyle.setBorderBottom(BorderStyle.THIN);
		cellDateStyle.setBorderTop(BorderStyle.THIN);
		cellDateStyle.setBorderRight(BorderStyle.THIN);
		cellDateStyle.setBorderLeft(BorderStyle.THIN);
		cellDateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
		row = sheet.createRow(7);
		row.createCell(1).setCellValue(mensajes.getMessage("text.xlsx.factura.fecha"));
		row.createCell(2).setCellValue(factura.getCreateAt());
		row.getCell(1).setCellStyle(tbodyStyle);
		row.getCell(2).setCellStyle(cellDateStyle);
		
		CellStyle theaderStyle = workbook.createCellStyle();
		theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
		theaderStyle.setBorderTop(BorderStyle.MEDIUM);
		theaderStyle.setBorderRight(BorderStyle.MEDIUM);
		theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
		theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
		theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		Row header = sheet.createRow(9);
		header.createCell(1).setCellValue(mensajes.getMessage("text.ver.factura.tabla.producto"));
		header.createCell(2).setCellValue(mensajes.getMessage("text.ver.factura.tabla.precio"));
		header.createCell(3).setCellValue(mensajes.getMessage("text.ver.factura.tabla.cantidad"));
		header.createCell(4).setCellValue(mensajes.getMessage("text.ver.factura.tabla.total"));
		
		header.getCell(1).setCellStyle(theaderStyle);
		header.getCell(2).setCellStyle(theaderStyle);
		header.getCell(3).setCellStyle(theaderStyle);
		header.getCell(4).setCellStyle(theaderStyle);
		
		int rowNum = 10;
		for (ItemFactura item: factura.getItems()) {
			Row fila = sheet.createRow(rowNum++);
			
			cell = fila.createCell(1);
			cell.setCellValue(item.getProducto().getNombre());
			cell.setCellStyle(tbodyStyle);
			
			cell = fila.createCell(2);
			cell.setCellValue(item.getProducto().getPrecio());
			cell.setCellStyle(tbodyStyle);
			
			cell = fila.createCell(3);
			cell.setCellValue(item.getCantidad());
			cell.setCellStyle(tbodyStyle);
			
			cell = fila.createCell(4);
			cell.setCellValue(item.calcularImporte());
			cell.setCellStyle(tbodyStyle);
		}
		
		CellStyle cellGranTotalStyle = workbook.createCellStyle();
		cellGranTotalStyle.setBorderBottom(BorderStyle.THIN);
		cellGranTotalStyle.setBorderTop(BorderStyle.THIN);
		cellGranTotalStyle.setBorderRight(BorderStyle.THIN);
		cellGranTotalStyle.setBorderLeft(BorderStyle.THIN);
		cellGranTotalStyle.setFillForegroundColor(IndexedColors.ORANGE.index);
		cellGranTotalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		Row filaTotal = sheet.createRow(rowNum);
		cell = filaTotal.createCell(3); 
		cell.setCellValue(mensajes.getMessage("text.ver.factura.tabla.granTotal"));
		cell.setCellStyle(cellGranTotalStyle);
		
		cell = filaTotal.createCell(4);
		cell.setCellValue(factura.getTotal());
		cell.setCellStyle(tbodyStyle);
	}
}