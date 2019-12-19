package org.websparrow.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.websparrow.report.entity.Employee;
import org.websparrow.report.repository.ReportRepository;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class EmployeeReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ResourceLoader resourceLoader;

	public String generateReport() {
		try {

			List<Employee> employees = reportRepository.findAll();

//			String reportPath = "F:\\Content\\Report";
			String reportPath = resourceLoader.getResource("classpath:employee-rpt-database.jrxml").getURI().getPath();

			// Compile the Jasper report from .jrxml to .japser
			JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
//					.compileReport(reportPath + "\\employee-rpt-database.jrxml");

			// Get your data source
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(employees);

			// Add parameters
			Map<String, Object> parameters = new HashMap<>();

			parameters.put("createdBy", "Websparrow.org");

			// Fill the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,
					jrBeanCollectionDataSource);

			// Export the report to a PDF file
//			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "\\Emp-Rpt-Database.pdf");
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath);

			System.out.println("Done");

//			return "Report successfully generated @path= " + reportPath;
			return "Report successfully generated";

		} catch (Exception e) {
			e.printStackTrace();
			return "Error--> check the console log";
		}
	}
}
