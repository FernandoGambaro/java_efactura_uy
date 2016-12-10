package com.bluedot.efactura.controllers;

import com.bluedot.commons.error.APIException;
import com.bluedot.commons.error.VerboseAction;
import com.bluedot.commons.utils.DatabaseExecutor;
import com.bluedot.commons.utils.DatabaseExecutor.PromiseBlock;
import com.bluedot.commons.utils.ThreadMan;
import com.bluedot.efactura.model.IVA;
import com.bluedot.efactura.model.IndicadorFacturacion;
import com.bluedot.efactura.model.Pais;
import com.google.inject.Inject;

import play.db.jpa.JPAApi;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.index;

@With(VerboseAction.class)
public class Application extends Controller {

	protected JPAApi jpaApi;
	
	@Inject
	public Application(JPAApi jpaApi) {
		super();
		this.jpaApi = jpaApi;
	}
	
    public Result index() {
        
    	try {
			DatabaseExecutor.syncDatabaseAction(new PromiseBlock<Void>() {

				public Void execute() throws APIException {
					if (Pais.count(jpaApi) == 0) {
						/*
						 * Referencia: https://es.wikipedia.org/wiki/ISO_3166-1
						 * 
						 * CSV: https://gist.github.com/brenes/1095110
						 */

						Pais pais = new Pais("UY", "Uruguay");
						pais.save();
						pais = new Pais("AR", "Argentina");
						pais.save();
						pais = new Pais("BR", "Brasil");
						pais.save();
						pais = new Pais("PY", "Paraguay");
						pais.save();
						
					}
					
					if (IVA.count(jpaApi) == 0){
						IVA iva = new IVA(IndicadorFacturacion.INDICADOR_FACTURACION_IVA_TASA_BASICA, 22);
						iva.save();
						iva = new IVA(IndicadorFacturacion.INDICADOR_FACTURACION_IVA_TASA_MINIMA, 10);
						iva.save();
					}
					
					
					ThreadMan.forceTransactionFlush();
					
					return null;
				}

			}, true);
		} catch (Throwable t) {
			t.printStackTrace();
		}
    	
    	
    	
    	
    	return ok(index.render("Your new application is ready."));
    }

}
