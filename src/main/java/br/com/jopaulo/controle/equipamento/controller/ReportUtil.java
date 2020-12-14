package br.com.jopaulo.controle.equipamento.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// retorna pdf em byte para download no navgador
	public byte[] gerarRelatorio(List listDados, String relatorio, ServletContext servletContext) throws Exception {
		
		// cria a lista de dados para relatório com lista de objetos para imprimir
		JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(listDados);
		
		// carrega o caminho do arquivo jasper compilado
		String caminhoJasper = servletContext.getRealPath("relatorios") + java.io.File.separator + relatorio + ".jasper";
		
		// carrega o arquivo Jasper passando os dados
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashedMap(), jrBeanCollectionDataSource);
		
		// exporta oara byte[] para fazer o download do pdf
		return JasperExportManager.exportReportToPdf(impressoraJasper);
		
	}

}
