package com.bluedot.efactura.pool;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.security.auth.callback.CallbackHandler;

import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import com.bluedot.commons.ObjectPool;
import com.bluedot.commons.Settings;
import com.bluedot.efactura.Constants;
import com.bluedot.efactura.EFacturaException;
import com.bluedot.efactura.commons.Commons;
import com.bluedot.efactura.commons.Commons.DgiService;

public class WSRutPool extends ObjectPool<WSPersonaGetActEmpresarialSoapPortWrapper>
{
	private static WSRutPool instance = null;

	public static synchronized WSRutPool getInstance() throws IOException, EFacturaException, KeyStoreException, NoSuchAlgorithmException, CertificateException
	{
		if (instance == null)
		{

			Settings settings = Settings.getInstance();
			CallbackHandler passwordCallback = Commons.getPasswordCallback();
			instance = new WSRutPool(settings.getString(Constants.SECURITY_FILE), Commons.getCetificateAlias(), passwordCallback,
					Commons.getURL(DgiService.Rut));
		}

		return instance;
	}

	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	private final String securityPropertiesPath;
	private final String certificateAlias;
	private final CallbackHandler passwordCallback;
	private final String serviceURL;

	private WSRutPool(String securityPropertiesPath, String keystoreAlias, CallbackHandler passwordCallback, String serviceURL) {
		this.securityPropertiesPath = Objects.requireNonNull(securityPropertiesPath, "Security properties path is required");
		this.certificateAlias = Objects.requireNonNull(keystoreAlias, "Certificate alias is required");
		this.passwordCallback = Objects.requireNonNull(passwordCallback, "Password callback is required");
		this.serviceURL = serviceURL;
	}

	/**
	 * Configures security settings and in/out interceptors and builds a client
	 * service proxy
	 */
	@Override
	protected WSPersonaGetActEmpresarialSoapPortWrapper create()
	{
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(serviceURL);
		WSPersonaGetActEmpresarialSoapPortWrapper port = factory.create(WSPersonaGetActEmpresarialSoapPortWrapper.class);

		Endpoint cxfEndpoint = ClientProxy.getClient(port).getEndpoint();

		Map<String, Object> outProps = new HashMap<>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
		outProps.put(WSHandlerConstants.USER, certificateAlias);
		outProps.put(WSHandlerConstants.SIG_PROP_FILE, securityPropertiesPath);
		outProps.put(WSHandlerConstants.SIG_KEY_ID, "DirectReference");
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, passwordCallback);

		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		cxfEndpoint.getOutInterceptors().add(wssOut);

		// Note: uncomment this if you need to log outgoing signed request
		cxfEndpoint.getOutInterceptors().add(new LoggingOutInterceptor());

		return port;
	}

	@Override
	public boolean validate(WSPersonaGetActEmpresarialSoapPortWrapper o)
	{
		return true;
	}

	@Override
	public void expire(WSPersonaGetActEmpresarialSoapPortWrapper o)
	{
	}

}
