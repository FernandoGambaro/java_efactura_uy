package com.bluedot.efactura;


import java.util.Iterator;
import java.util.List;




public class EFacturaException extends Throwable
{

	private EFacturaException(EFacturaErrors error, Throwable cause) {
		super("", cause);
		this.error = error;
	}
	
	private EFacturaException(EFacturaErrors error) {
		super(error.message);
		this.error = error;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2194392490942571005L;
	
	private String detailMessage= "";
	private EFacturaErrors error;
	
	/*
	 * 400 Bad Request
	 * 401 Unauthorized
	 * 402 Payment Required
	 * 403 Forbidden
	 * 404 Not Found
	 * 405 Method Not Allowed
	 * 406 Not Acceptable
	 * 407 Proxy Authentication Required
	 * 408 Request Timeout
	 * 409 Conflict
	 * 410 Gone
	 * 411 Length Required
	 * 412 Precondition Failed
	 */
	
	public enum EFacturaErrors
	{
		 BAD_PARAMETER_VALUE(1, "Valor incorrecto en el parametro."), 
		 MISSING_PARAMETER(2, "Falta un parametros"), 
		 ERROR_IN_CAE_FILE(3, "Error en el archivo de CAEs"), 
		 CAE_DATA_NOT_FOUND(4, "No existe un CAE valido para ese tipo de CFE"),
		 WRAPPED_EXCEPTION(5, "Wrapper for Exception"), 
		 CAE_NOT_AVAILABLE_ID(6,"No hay mas identificadores electronicos disponibles en el CAE para este tipo de CFE"), 
		 MALFORMED_CFE(7,"CFE malformado"), 
		 NOT_SUPPORTED(8,"No soportado");


		int code;
		String message;
		
		EFacturaErrors(int code, String message)
		{
			this.code = code;
			this.message = message;
		}
		
		public int code()
		{
			return code;
		}
		public String message()
		{
			return message;
		}
	}
	
	
	
	public static EFacturaException raise(EFacturaErrors error)
	{
		return new EFacturaException(error);
	}
	
	public static EFacturaException raise(Throwable e)
	{
		return new EFacturaException(EFacturaErrors.WRAPPED_EXCEPTION, e);
	}
	
	
	
	public String getDetailMessage()
	{
		return detailMessage;
	}

	public EFacturaException setDetailMessage(String detailMessage)
	{
		this.detailMessage += detailMessage;
		return this;
	}
	
	public EFacturaException setDetailMessage(List<String> detailMessage)
	{
		int i=0;
		for (Iterator<String> iterator = detailMessage.iterator(); iterator.hasNext();)
		{
			String string = iterator.next();
			if (i==0)
				this.detailMessage += "[" + string;
			else
				this.detailMessage += "," + string;
			i++;
		}
		this.detailMessage += "]";
		return this;
	}

	public EFacturaErrors getError()
	{
		return error;
	}

	public void setError(EFacturaErrors error)
	{
		this.error = error;
	}
	
	@Override
	public String getMessage()
	{
		return super.getMessage() + (getDetailMessage()!=null?". "+getDetailMessage():"");
	}
}