package com.bluedot.efactura.services.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;

import com.bluedot.commons.error.APIException;
import com.bluedot.commons.error.APIException.APIErrors;
import com.bluedot.commons.utils.ThreadMan;
import com.bluedot.commons.utils.XML;
import com.bluedot.efactura.commons.Commons;
import com.bluedot.efactura.interceptors.InterceptorContextHolder;
import com.bluedot.efactura.interceptors.NamespacesInterceptor;
import com.bluedot.efactura.interceptors.SignatureInterceptor;
import com.bluedot.efactura.model.CFE;
import com.bluedot.efactura.model.Empresa;
import com.bluedot.efactura.model.FirmaDigital;
import com.bluedot.efactura.model.MotivoRechazoCFE;
import com.bluedot.efactura.model.MotivoRechazoSobre;
import com.bluedot.efactura.model.ReporteDiario;
import com.bluedot.efactura.model.SobreEmitido;
import com.bluedot.efactura.model.TipoDoc;
import com.bluedot.efactura.pool.WSRecepcionPool;
import com.bluedot.efactura.pool.wrappers.WSEFacturaSoapPortWrapper;
import com.bluedot.efactura.respuestas.Respuestas;
import com.bluedot.efactura.respuestas.Respuestas.Respuesta;
import com.bluedot.efactura.services.RecepcionService;
import com.bluedot.efactura.strategy.report.SummaryStrategy;
import com.sun.istack.logging.Logger;

import dgi.classes.entreEmpresas.CFEEmpresasType;
import dgi.classes.entreEmpresas.EnvioCFEEntreEmpresas;
import dgi.classes.recepcion.CFEDefType;
import dgi.classes.recepcion.EnvioCFE;
import dgi.classes.recepcion.EnvioCFE.Caratula;
import dgi.classes.recepcion.ObjectFactory;
import dgi.classes.reporte.ReporteDefType;
import dgi.classes.respuestas.cfe.ACKCFEdefType;
import dgi.classes.respuestas.cfe.ACKCFEdefType.ACKCFEDet;
import dgi.classes.respuestas.cfe.EstadoACKCFEType;
import dgi.classes.respuestas.cfe.RechazoCFEDGIType;
import dgi.classes.respuestas.reporte.ACKRepDiariodefType;
import dgi.classes.respuestas.sobre.ACKSobredefType;
import dgi.soap.recepcion.Data;
import dgi.soap.recepcion.WSEFacturaEFACCONSULTARESTADOENVIO;
import dgi.soap.recepcion.WSEFacturaEFACCONSULTARESTADOENVIOResponse;
import dgi.soap.recepcion.WSEFacturaEFACRECEPCIONREPORTE;
import dgi.soap.recepcion.WSEFacturaEFACRECEPCIONREPORTEResponse;
import dgi.soap.recepcion.WSEFacturaEFACRECEPCIONSOBRE;
import dgi.soap.recepcion.WSEFacturaEFACRECEPCIONSOBREResponse;

public class RecepcionServiceImpl implements RecepcionService {

	static Logger logger = Logger.getLogger(RecepcionServiceImpl.class);

	@Override
	public void sendCFE(CFE cfe) throws APIException {
		// TODO soportar mas de un CFE por sobre

		/*
		 * Creo el CFEDefType
		 */
		CFEDefType cfeDefType = (new ObjectFactory()).createCFEDefType();
		cfeDefType.setVersion("1.0");

		switch (cfe.getTipo()) {

		case eFactura:
		case eFactura_Contingencia:
		case Nota_de_Credito_de_eFactura:
		case Nota_de_Credito_de_eFactura_Contingencia:
		case Nota_de_Debito_de_eFactura:
		case Nota_de_Debito_de_eFactura_Contingencia:
			cfeDefType.setEFact(cfe.getEfactura());
			break;
		case eResguardo:
		case eResguardo_Contingencia:
			cfeDefType.setEResg(cfe.getEresguardo());
			break;
		case eTicket:
		case eTicket_Contingencia:
		case Nota_de_Debito_de_eTicket:
		case Nota_de_Debito_de_eTicket_Contingencia:
		case Nota_de_Credito_de_eTicket:
		case Nota_de_Credito_de_eTicket_Contingencia:
			cfeDefType.setETck(cfe.getEticket());
			break;
		case eFactura_Exportacion:
		case eFactura_Exportacion_Contingencia:
		case eFactura_Venta_por_Cuenta_Ajena:
		case eFactura_Venta_por_Cuenta_Ajena_Contingencia:
		case eRemito:
		case eRemito_Contingencia:
		case eRemito_de_Exportacion:
		case eRemito_de_Exportacion_Contingencia:
		case eTicket_Venta_por_Cuenta_Ajena:
		case eTicket_Venta_por_Cuenta_Ajena_Contingencia:
		case Nota_de_Debito_de_eTicket_Venta_por_Cuenta_Ajena:
		case Nota_de_Debito_de_eTicket_Venta_por_Cuenta_Ajena_Contingencia:
		case Nota_de_Debito_de_eFactura_Venta_por_Cuenta_Ajena:
		case Nota_de_Debito_de_eFactura_Venta_por_Cuenta_Ajena_Contingencia:
		case Nota_de_Credito_de_eTicket_Venta_por_Cuenta_Ajena:
		case Nota_de_Credito_de_eTicket_Venta_por_Cuenta_Ajena_Contingencia:
		case Nota_de_Credito_de_eFactura_Venta_por_Cuenta_Ajena:
		case Nota_de_Credito_de_eFactura_Venta_por_Cuenta_Ajena_Contingencia:
		case Nota_de_Debito_de_eFactura_Exportacion:
		case Nota_de_Debito_de_eFactura_Exportacion_Contingencia:
		case Nota_de_Credito_de_eFactura_Exportacion:
		case Nota_de_Credito_de_eFactura_Exportacion_Contingencia:
			throw APIException.raise(APIErrors.NOT_SUPPORTED)
					.setDetailMessage("Envio de CFE a DGI de tipo " + cfe.getTipo().value);
		}

		/*
		 * Creo el sobre que contiene los CFEs
		 */
		SobreEmitido sobre = new SobreEmitido(cfe.getEmpresaEmisora(), cfe.getEmpresaReceptora(), "", 1, null);
		sobre.getCfes().add(cfe);
		sobre.setFecha(new Date());
		cfe.setSobreEmitido(sobre);
		sobre.save();

		/*
		 * Se necesita el id del sobre para generar el nombre de archivo, y el
		 * idEmisor por lo tanto debemos forzar la escritura a la BBDD para que
		 * se genere el id.
		 */
		ThreadMan.forceTransactionFlush();

		/*
		 * Creo EnvioCFE.
		 */
		EnvioCFE envioCFE = new EnvioCFE();
		envioCFE.setVersion("1.0");
		envioCFE.getCVES().add(cfeDefType);
		sobre.setEnvioCFE(envioCFE);
		addCaratulaSobre(sobre);

		/*
		 * Creo EnvioCFEEntreEmpresas
		 */
		if (cfe.getEmpresaReceptora() != null && cfe.getEmpresaReceptora().isEmisorElectronico()) {
			generarXMLEmpresa(cfe, cfeDefType, sobre);
		}

		/*
		 * Envio a la DGI
		 */
		String xmlSobre;
		try {
			xmlSobre = XML.objectToString(sobre.getEnvioCFE());
		} catch (JAXBException e) {
			throw APIException.raise(e);
		}

		sobre.setXmlDgi(xmlSobre);
		enviarSobreDGI(sobre, xmlSobre);

		

	}

	/**
	 * @param cfe
	 * @param cfeDefType
	 * @param sobre
	 * @throws APIException
	 */
	private void generarXMLEmpresa(CFE cfe, CFEDefType cfeDefType, SobreEmitido sobre) throws APIException {
		CFEEmpresasType cfeEmpresasType = (new dgi.classes.entreEmpresas.ObjectFactory()).createCFEEmpresasType();
		cfeEmpresasType.setCFE(cfeDefType);
		if (cfe.getAdenda() != null) {
			// TODO volver a poner adenda en el envio al receptor
			// electronico
			// StringBuilder stringBuilder = new StringBuilder();
			// AdendaSerializer.convertAdenda(stringBuilder, new
			// JSONArray(cfe.getAdenda()));
			// cfeEmpresasType.setAdenda(stringBuilder.toString());
		}
		EnvioCFEEntreEmpresas envioCFEEntreEmpresas = (new dgi.classes.entreEmpresas.ObjectFactory())
				.createEnvioCFEEntreEmpresas();
		envioCFEEntreEmpresas.setVersion("1.0");
		envioCFEEntreEmpresas.getCFEAdendas().add(cfeEmpresasType);
		addCaratulaEntreEmpresas(envioCFEEntreEmpresas, sobre);
		sobre.setEnvioCFEEntreEmpresas(envioCFEEntreEmpresas);
		
		try {
			Document allDocument = XML.objectToDocument(sobre.getEnvioCFEEntreEmpresas());
			/*
			 * Instantiate the DocumentBuilderFactory.
			 * 
			 * IMPORTANT:NamespaceAwerness=true!!
			 */
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			allDocument = XML
					.loadXMLFromString(NamespacesInterceptor.doNamespaceChanges(XML.documentToString(allDocument)));

			allDocument = SignatureInterceptor.signDocument(dbf, allDocument, "ns0:CFE", "DGICFE:CFE_Adenda", sobre.getEmpresaEmisora().getFirmaDigital().getKeyStore(), FirmaDigital.KEY_ALIAS, FirmaDigital.KEYSTORE_PASSWORD);

			sobre.setXmlEmpresa(XML.documentToString(allDocument));
			sobre.update();
			
		} catch (TransformerFactoryConfigurationError | Exception e) {
			e.printStackTrace();
		}
	}

	private void addCaratulaSobre(SobreEmitido sobre) throws APIException {
		try {
			EnvioCFE envioCFE = sobre.getEnvioCFE();

			List<CFEDefType> cfes = envioCFE.getCVES();

			String RUCemisor = null;

			String RUCreceptor = null;

			for (Iterator<CFEDefType> iterator = cfes.iterator(); iterator.hasNext();) {
				CFEDefType cfeDefType = iterator.next();

				String rucEmisor = null;
				String rucReceptor = Commons.getRucDGI();

				if (cfeDefType.getEFact() != null) {
					rucEmisor = cfeDefType.getEFact().getEncabezado().getEmisor().getRUCEmisor();
				}

				if (cfeDefType.getEFactExp() != null) {
					rucEmisor = cfeDefType.getEFactExp().getEncabezado().getEmisor().getRUCEmisor();
				}

				if (cfeDefType.getERem() != null) {
					rucEmisor = cfeDefType.getERem().getEncabezado().getEmisor().getRUCEmisor();
				}

				if (cfeDefType.getERemExp() != null) {
					rucEmisor = cfeDefType.getERemExp().getEncabezado().getEmisor().getRUCEmisor();
				}

				if (cfeDefType.getEResg() != null) {
					rucEmisor = cfeDefType.getEResg().getEncabezado().getEmisor().getRUCEmisor();
				}

				if (cfeDefType.getETck() != null) {
					rucEmisor = cfeDefType.getETck().getEncabezado().getEmisor().getRUCEmisor();
				}

				if (RUCemisor == null)
					RUCemisor = rucEmisor;
				else if (!RUCemisor.equals(rucEmisor))
					throw APIException.raise(APIErrors.BAD_PARAMETER_VALUE).withParams("RUCemisor")
							.setDetailMessage("Cannot have many RUCemisor values on one envelope");

				if (RUCreceptor == null)
					RUCreceptor = rucReceptor;
				else if (!RUCemisor.equals(rucReceptor))
					throw APIException.raise(APIErrors.BAD_PARAMETER_VALUE).withParams("RUCreceptor")
							.setDetailMessage("Cannot have many RUCreceptor on one envelope");

			}

			Caratula caratula = (new ObjectFactory()).createEnvioCFECaratula();

			caratula.setCantCFE(cfes.size());
			caratula.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			caratula.setIdemisor(new BigInteger(String.valueOf(sobre.getId())));
			caratula.setRUCEmisor(RUCemisor);
			caratula.setRutReceptor(RUCreceptor);
			caratula.setVersion("1.0");
			caratula.setX509Certificate(sobre.getEmpresaEmisora().getFirmaDigital().getCertificateEncoded());
			
			envioCFE.setCaratula(caratula);
		} catch (Exception e) {
			throw APIException.raise(e);
		}

	}

	private void addCaratulaEntreEmpresas(EnvioCFEEntreEmpresas signed, SobreEmitido sobre) throws APIException {
		try {
			List<CFEEmpresasType> cfes = signed.getCFEAdendas();

			String RUCemisor = null;

			String RUCreceptor = null;

			for (Iterator<CFEEmpresasType> iterator = cfes.iterator(); iterator.hasNext();) {
				CFEDefType cfeDefType = iterator.next().getCFE();

				String rucEmisor = null;
				String rucReceptor = null;

				if (cfeDefType.getEFact() != null) {
					rucEmisor = cfeDefType.getEFact().getEncabezado().getEmisor().getRUCEmisor();
					rucReceptor = cfeDefType.getEFact().getEncabezado().getReceptor().getDocRecep();
				}

				if (cfeDefType.getEFactExp() != null) {
					rucEmisor = cfeDefType.getEFactExp().getEncabezado().getEmisor().getRUCEmisor();
					rucReceptor = cfeDefType.getEFactExp().getEncabezado().getReceptor().getDocRecep();
				}

				if (cfeDefType.getERem() != null) {
					rucEmisor = cfeDefType.getERem().getEncabezado().getEmisor().getRUCEmisor();
					rucReceptor = cfeDefType.getERem().getEncabezado().getReceptor().getDocRecep();
				}

				if (cfeDefType.getERemExp() != null) {
					rucEmisor = cfeDefType.getERemExp().getEncabezado().getEmisor().getRUCEmisor();
					rucReceptor = cfeDefType.getERemExp().getEncabezado().getReceptor().getDocRecep();
				}

				if (cfeDefType.getEResg() != null) {
					rucEmisor = cfeDefType.getEResg().getEncabezado().getEmisor().getRUCEmisor();
					rucReceptor = cfeDefType.getEResg().getEncabezado().getReceptor().getDocRecep();
				}

				if (cfeDefType.getETck() != null) {
					rucEmisor = cfeDefType.getETck().getEncabezado().getEmisor().getRUCEmisor();
					rucReceptor = cfeDefType.getETck().getEncabezado().getReceptor().getDocRecep();
				}

				if (RUCemisor == null)
					RUCemisor = rucEmisor;
				else if (!RUCemisor.equals(rucEmisor))
					throw APIException.raise(APIErrors.BAD_PARAMETER_VALUE)
							.setDetailMessage("Cannot have many RUCemisor values on one envelope");

				if (RUCreceptor == null)
					RUCreceptor = rucReceptor;
				else if (!RUCemisor.equals(rucReceptor))
					throw APIException.raise(APIErrors.BAD_PARAMETER_VALUE)
							.setDetailMessage("Cannot have many RUCreceptor on one envelope");

			}

			dgi.classes.entreEmpresas.EnvioCFEEntreEmpresas.Caratula caratula = (new dgi.classes.entreEmpresas.ObjectFactory())
					.createEnvioCFEEntreEmpresasCaratula();

			caratula.setCantCFE(cfes.size());
			caratula.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			caratula.setIdemisor(new BigInteger(String.valueOf(sobre.getId())));
			caratula.setRUCEmisor(RUCemisor);
			caratula.setRutReceptor(RUCreceptor);
			caratula.setVersion("1.0");
			caratula.setX509Certificate(sobre.getEmpresaEmisora().getFirmaDigital().getCertificateEncoded());

			signed.setCaratula(caratula);
		} catch (Exception e) {
			throw APIException.raise(e);
		}

	}

	/**
	 * Se usa para los reenvios
	 * 
	 * @param sobre
	 *            para pasar por parametro al interceptor
	 * @param xmlSobre
	 *            el xml a enviar (normalmente es
	 *            XML.objectToString(sobre.getEnvioCFE()) )
	 * @return
	 * @throws APIException
	 */
	private ACKSobredefType enviarSobreDGI(SobreEmitido sobre, String xmlSobre) throws APIException {
		try {

			/*
			 * Colocamos en ThreadLocal al Sobre es la forma de pasarle
			 * parametros a los Interceptors
			 */
			InterceptorContextHolder.setSobreEmitido(sobre);

			Data response;
			try {
				WSEFacturaSoapPortWrapper portWrapper = WSRecepcionPool.getInstance().checkOut();

				WSEFacturaEFACRECEPCIONSOBRE input = new WSEFacturaEFACRECEPCIONSOBRE();
				Data data = new Data();
				data.setXmlData(xmlSobre);
				input.setDatain(data);

				WSEFacturaEFACRECEPCIONSOBREResponse output = portWrapper.getPort().efacrecepcionsobre(input);

				response = output.getDataout();

				logger.info("Respuesta: " + response.getXmlData());
				
				WSRecepcionPool.getInstance().checkIn(portWrapper);
			} catch (Throwable e) {
				throw APIException.raise(APIErrors.ERROR_COMUNICACION_DGI, e);
			} finally{
				/*
				 * Borramos el contexto
				 */
				InterceptorContextHolder.clear();
			}

			

			ACKSobredefType ACKSobre = (ACKSobredefType) XML.unMarshall(XML.loadXMLFromString(response.getXmlData()),
					ACKSobredefType.class);

			if (ACKSobre == null)
				throw APIException.raise(APIErrors.ERROR_COMUNICACION_DGI);

			try {
				/*
				 * Pruebo si se parseo bien la respuesta
				 */
				ACKSobre.getDetalle().getEstado();
			} catch (Throwable e) {
				/*
				 * Intento parsear la respuesta ahora pensando que fue un error
				 */
				Respuestas respuestas = (Respuestas) XML.unMarshall(XML.loadXMLFromString(response.getXmlData()),
						Respuestas.class);
				
				if (respuestas == null)
					throw APIException.raise(APIErrors.ERROR_COMUNICACION_DGI);
				
				Respuesta respuesta = respuestas.getRespuesta().iterator().next();
				
				if (respuesta == null)
					throw APIException.raise(APIErrors.ERROR_COMUNICACION_DGI);
				
				if (respuesta.getCodigo().intValue()==108) {
					throw APIException.raise(APIErrors.SOBRE_YA_ENVIADO);
				}else
					throw APIException.raise(APIErrors.ERROR_COMUNICACION_DGI);
				
			}
			
			switch (ACKSobre.getDetalle().getEstado()) {
			case AS:
				
				sobre.setRespuesta_dgi(response.getXmlData());
				sobre.setIdReceptor(ACKSobre.getCaratula().getIDReceptor().longValue());
				sobre.setEstadoDgi(ACKSobre.getDetalle().getEstado());
				
				sobre.setToken(ACKSobre.getDetalle().getParamConsulta().getToken());
				sobre.setFechaConsulta(
						ACKSobre.getDetalle().getParamConsulta().getFechahora().toGregorianCalendar().getTime());
				break;
			case BA:
				break;
			case BS:
				
				if (sobreEnviadoAnteriormente(ACKSobre))
					throw APIException.raise(APIErrors.SOBRE_YA_ENVIADO);
				else{
					sobre.setRespuesta_dgi(response.getXmlData());
					sobre.setIdReceptor(ACKSobre.getCaratula().getIDReceptor().longValue());
					sobre.setEstadoDgi(ACKSobre.getDetalle().getEstado());
				}
			}

			return ACKSobre;
		} catch (Exception e) {
			throw APIException.raise(e);
		}

	}

	private boolean sobreEnviadoAnteriormente(ACKSobredefType ACKSobre) {
		if (ACKSobre.getDetalle()!=null && ACKSobre.getDetalle().getMotivosRechazo().size()==1 && ACKSobre.getDetalle().getMotivosRechazo().get(0).getMotivo().equals(MotivoRechazoSobre.S08.name()))
			return true;
		else
			return false;
	}

	@Override
	public Data consultaResultadoSobre(String token, Long idReceptor) throws APIException {

		if (token == null)
			throw APIException.raise(APIErrors.MISSING_PARAMETER).withParams("token");
		if (idReceptor == null)
			throw APIException.raise(APIErrors.MISSING_PARAMETER).withParams("idReceptor");

		try {
			WSEFacturaSoapPortWrapper portWrapper = WSRecepcionPool.getInstance().checkOut();

			WSEFacturaEFACCONSULTARESTADOENVIO input = new WSEFacturaEFACCONSULTARESTADOENVIO();
			String xml = "<ConsultaCFE xmlns=\"http://dgi.gub.uy\"><IdReceptor>" + idReceptor + "</IdReceptor><Token>"
					+ token.toString() + "</Token> </ConsultaCFE>";
			Data data = new Data();
			data.setXmlData(xml);

			input.setDatain(data);

			WSEFacturaEFACCONSULTARESTADOENVIOResponse output = portWrapper.getPort().efacconsultarestadoenvio(input);

			WSRecepcionPool.getInstance().checkIn(portWrapper);
			
			logger.info("Respuesta: " + output.getDataout().getXmlData());
			
			return output.getDataout();
		} catch (Throwable e) {
			throw APIException.raise(APIErrors.ERROR_COMUNICACION_DGI, e);
		}

	}

	@Override
	public void consultaResultadoSobre(SobreEmitido sobre) throws APIException {
		try {
			if (sobre.getToken() == null)
				throw APIException.raise(APIErrors.MISSING_PARAMETER).setDetailMessage("token");
				
			if (sobre.getIdReceptor() == null)
				throw APIException.raise(APIErrors.MISSING_PARAMETER).setDetailMessage("idReceptor");

			/*
			 * Colocamos en ThreadLocal al Sobre es la forma de pasarle
			 * parametros a los Interceptors
			 */
			InterceptorContextHolder.setEmpresa(sobre.getEmpresaEmisora());
			
			Data result = consultaResultadoSobre(sobre.getToken(), sobre.getIdReceptor());

			sobre.setResultado_dgi(result.getXmlData());
			sobre.update();

			ACKCFEdefType ACKcfe = (ACKCFEdefType) XML.unMarshall(XML.loadXMLFromString(result.getXmlData()),
					ACKCFEdefType.class);

			for (Iterator<ACKCFEDet> iterator = ACKcfe.getACKCFEDet().iterator(); iterator.hasNext();) {
				ACKCFEDet ACKcfeDet = iterator.next();
				CFE cfe = sobre.getCFE(ACKcfeDet.getNroCFE().longValue(), ACKcfeDet.getSerie(),
						TipoDoc.fromInt(ACKcfeDet.getTipoCFE().intValue()));
				if (cfe != null) {
					cfe.setEstado(ACKcfeDet.getEstado());
					cfe.update();
					if (cfe.getEstado() == EstadoACKCFEType.AE){ 
						/*
						 * Envio a la empresa
						 */
						if (cfe.getEmpresaReceptora() != null && cfe.getEmpresaReceptora().isEmisorElectronico() && sobre.getXmlEmpresa()!=null)
							Commons.enviarMail(sobre.getEmpresaEmisora(), sobre.getEmpresaReceptora(), sobre.getNombreArchivo(), sobre.getXmlEmpresa());
//							this.enviarSobreEmpresa(sobre);
					}else{
						for (Iterator<RechazoCFEDGIType> iterator2 = ACKcfeDet.getMotivosRechazoCF()
								.iterator(); iterator2.hasNext();) {
							RechazoCFEDGIType rechazo = iterator2.next();
							cfe.getMotivo().add(MotivoRechazoCFE.valueOf(rechazo.getMotivo()));
						}

					}
				} else {
					// TODO ver que se hace si no encuentra al cfe dentro de los
					// CFE del sobre
				}
			}

		} catch (Exception e) {
			throw APIException.raise(e);
		}finally{
			/*
			 * Borramos el contexto
			 */
			InterceptorContextHolder.clear();
		}

	}

	// TODO agregar mutex en los cfe y sobres
	@Override
	public ReporteDiario generarReporteDiario(Date fecha, Empresa empresa) throws APIException {
		try {
			if (fecha == null)
				throw APIException.raise(APIErrors.MISSING_PARAMETER).withParams("fecha");

			long hours = ((new Date()).getTime()-fecha.getTime())/1000/60/60;
			
			if (hours <= 43)
				throw APIException.raise(APIErrors.TEMPRANO_PARA_GENERAR_REPORTE).setDetailMessage("Debe esperar al menos " + (43-hours) + " horas mas para generar el reporte.");
			

			/*
			 * Chequeo que todos los CFE tengan respuesta o esten anulados
			 */
			List<SobreEmitido> sobres = SobreEmitido.findByEmpresaEmisoraAndDate(empresa, fecha);
			for (SobreEmitido sobreEmitido : sobres) {
				List<CFE> cfes = sobreEmitido.getCfes();
				for (CFE cfe : cfes) {
					if (cfe.getEstado()==null && cfe.getGeneradorId()!=null)
						throw APIException.raise(APIErrors.HAY_CFE_SIN_RESPUESTA).setDetailMessage("serie:" + cfe.getSerie() + " nro:" + cfe.getNro() + " tipo:" + cfe.getTipo().value);
				}
			}
			
			
			/*
			 * Creo el Reporte Diario
			 */
			ReporteDefType reporte = new ReporteDefType();
			ReporteDiario reporteDiario = new ReporteDiario(empresa, fecha);
			reporteDiario.save();
			reporteDiario.setReporteDefType(reporte);

			/*
			 * Para que el id de ReporteDiario se asigne
			 */
			ThreadMan.forceTransactionFlush();

			/*
			 * Caratula
			 */
			SimpleDateFormat caratulaFormatter = new SimpleDateFormat("yyyy-MM-dd");
			dgi.classes.reporte.ReporteDefType.Caratula caratula = new dgi.classes.reporte.ReporteDefType.Caratula();
			caratula.setCantComprobantes(new BigInteger("0"));
			caratula.setFechaResumen(
					DatatypeFactory.newInstance().newXMLGregorianCalendar(caratulaFormatter.format(fecha)));
			caratula.setIDEmisor(new BigInteger((String.valueOf(reporteDiario.getId()))));
			caratula.setRUCEmisor(empresa.getRut());
			caratula.setSecEnvio(new BigInteger(String.valueOf(reporteDiario.getSecuencial())));
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(new Date());
			caratula.setTmstFirmaEnv(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
			caratula.setVersion("1.0");
			reporte.setCaratula(caratula);

			
			
			/*
			 * Resumenes
			 */
			for (TipoDoc tipoDoc : TipoDoc.values()) {
				SummaryStrategy strategy = new SummaryStrategy.Builder().withTipo(tipoDoc).build();
				if (strategy != null)
					strategy.buildSummary(empresa, reporte, fecha, sobres);
			}

			String reporteString = XML.objectToString(reporte);
			
			reporteDiario.setXml(reporteString);
			
			reporteDiario.setTimestampEnviado(new Date());
			
			Data data = sendReporte(reporteString, fecha, reporteDiario.getEmpresa());

			reporteDiario.setRespuesta(data.getXmlData());

			ACKRepDiariodefType ACKreporte = (ACKRepDiariodefType) XML
					.unMarshall(XML.loadXMLFromString(data.getXmlData()), ACKRepDiariodefType.class);

			reporteDiario.setIdReceptor(ACKreporte.getCaratula().getIDReceptor().toString());

			reporteDiario.setEstado(ACKreporte.getDetalle().getEstado());

			ThreadMan.forceTransactionFlush();

			return reporteDiario;

		} catch (Exception e) {
			throw APIException.raise(e);
		}

	}

	private Data sendReporte(String reporte, Date date, Empresa empresa) throws APIException {
		try {
			
			/*
			 * Colocamos en ThreadLocal al Sobre es la forma de pasarle
			 * parametros a los Interceptors
			 */
			InterceptorContextHolder.setEmpresa(empresa);
			
			WSEFacturaSoapPortWrapper portWrapper = WSRecepcionPool.getInstance().checkOut();

			WSEFacturaEFACRECEPCIONREPORTE input = new WSEFacturaEFACRECEPCIONREPORTE();
			Data data = new Data();
			data.setXmlData(reporte);
			input.setDatain(data);

			WSEFacturaEFACRECEPCIONREPORTEResponse output = portWrapper.getPort().efacrecepcionreporte(input);

			WSRecepcionPool.getInstance().checkIn(portWrapper);

			return output.getDataout();
		} catch (Throwable e) {
			throw APIException.raise(APIErrors.ERROR_COMUNICACION_DGI, e);
		}finally{
			/*
			 * Borramos el contexto
			 */
			InterceptorContextHolder.clear();
		}
	}

	// private Data sendReporte(ReporteDefType reporte, Date date) throws
	// APIException {
	// try {
	// JAXBContext context = JAXBContext.newInstance(ReporteDefType.class);
	// Marshaller marshaller = context.createMarshaller();
	// StringWriter sw = new StringWriter();
	// marshaller.marshal(reporte, sw);
	//
	// Data response =
	// this.sendReporte(PrettyPrint.prettyPrintXML(sw.toString()), date);
	//
	// // Dump sobre y response to disk
	// Commons.dumpReporteToFile(reporte, false, response, date);
	//
	// return response;
	// } catch (Exception e) {
	// throw APIException.raise(e);
	// }
	// }

//	@Override
//	public void consultarResultados(Date date, Empresa empresa) throws APIException {
//
//		List<SobreEmitido> sobres = SobreEmitido.findByEmpresaEmisoraAndDate(empresa, date);
//
//		for (Iterator<SobreEmitido> iterator = sobres.iterator(); iterator.hasNext();) {
//			SobreEmitido sobre = iterator.next();
//			this.consultaResultadoSobre((SobreEmitido) sobre);
//
//		}
//
//	}

	@Override
	public void reenviarSobre(SobreEmitido sobre) throws APIException {
		sobre.setReenvio(true);
		sobre.update();
		this.enviarSobreDGI(sobre, sobre.getXmlDgi());
	}

	@Override
	public void enviarCfeEmpresa(CFE cfe) throws APIException {
		SobreEmitido sobre = cfe.getSobreEmitido();
		
		if (sobre.getEmpresaReceptora().isEmisorElectronico()){
			if (sobre.getXmlEmpresa()==null){
				try {
					EnvioCFE envioCFE = (EnvioCFE) XML.unMarshall(XML.loadXMLFromString(sobre.getXmlDgi()), EnvioCFE.class);
					CFEDefType cfeDefType = envioCFE.getCVES().get(0);
					cfeDefType.setSignature(null);
					generarXMLEmpresa(cfe, envioCFE.getCVES().get(0), sobre);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enviarSobreEmpresa(sobre);
		}
		
	}

	@Override
	public void enviarSobreEmpresa(SobreEmitido sobre) throws APIException {
		Commons.enviarMail(sobre.getEmpresaEmisora(), sobre.getEmpresaReceptora(), sobre.getNombreArchivo(), sobre.getXmlEmpresa());
	}

	// @Override
	// public void anularDocumento(TipoDoc tipo, String serie, int nro, Date
	// date, Empresa empresa) throws APIException {
	// try {
	// CAE caeDataJson = factory.getCAEMicroController(empresa).getCAE(tipo);
	//
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	//
	// String anuladoFilePath =
	// Play.application().configuration().getString(Constants.GENERATED_CFE_FOLDER,
	// "resources" + File.separator + "cfe") + File.separator +
	// formatter.format(date) + File.separator
	// + tipo.value + File.separator + serie + "_" + nro + "_unsigned.xml";
	// File anuladoFile = new File(anuladoFilePath);
	//
	// if (!anuladoFile.exists()) {
	// IO.writeFile(anuladoFilePath, "narf");
	// }
	//
	// } catch (IOException e) {
	// throw APIException.raise(e);
	// }
	//
	// }

	// TODO metodo para pasar la prueba de homologacion
	// @Override
	// public void anularNextDocumento(TipoDoc tipo, Date date, Empresa empresa)
	// throws APIException {
	// try {
	// IdDocFact id = factory.getCAEMicroController(empresa).getIdDocFact(tipo,
	// false, 2);
	//
	// // TODO la forma de generar las anulaciones es generar un archivo
	// // dummy, esto es una cagada porque hay que borrar los archivos
	// // dummys luego de generar el reporte diario y antes de subir los
	// // datos a DGI
	//
	// SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	//
	// String anuladoFilePath =
	// Play.application().configuration().getString(Constants.GENERATED_CFE_FOLDER,
	// "resources" + File.separator + "cfe") + File.separator +
	// formatter.format(date) + File.separator
	// + tipo.value + File.separator + id.getSerie() + "_" + id.getNro() +
	// "_unsigned.xml";
	// File anuladoFile = new File(anuladoFilePath);
	//
	// if (!anuladoFile.exists()) {
	// IO.writeFile(anuladoFilePath, "narf");
	// }
	//
	// } catch (IOException e) {
	// throw APIException.raise(e);
	// } catch (DatatypeConfigurationException e) {
	// throw APIException.raise(e);
	// }
	//
	// }

}
