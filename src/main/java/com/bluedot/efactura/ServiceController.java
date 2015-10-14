package com.bluedot.efactura;

import dgi.classes.recepcion.CFEDefType.EFact;

public interface ServiceController {

	EfacturaResult register(EFact efact) throws EFacturaException;
	
	EfacturaResult poll() throws EFacturaException;
	
	EfacturaResult cancel() throws EFacturaException;
	
}
