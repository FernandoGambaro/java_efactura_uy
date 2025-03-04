package com.bluedot.efactura.microControllers.interfaces;

import java.util.Date;

import com.bluedot.commons.error.APIException;
import com.bluedot.efactura.model.CFE;
import com.bluedot.efactura.model.ReporteDiario;
import com.bluedot.efactura.model.SobreEmitido;

public interface ServiceMicroController {

	void enviar(CFE cfe) throws APIException;

	void reenviar(SobreEmitido sobre) throws APIException;

	void consultarResultados(Date date) throws APIException;

	SobreEmitido consultaResultado(SobreEmitido sobre) throws APIException;

	ReporteDiario generarReporteDiario(Date date) throws APIException;

	void anularDocumento(CFE cfe) throws APIException;

	void getDocumentosEntrantes() throws APIException;

	void enviarCfeEmpresa(CFE cfe) throws APIException;

	void enviarSobreEmpresa(SobreEmitido sobre) throws APIException;

}
