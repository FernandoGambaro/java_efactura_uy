
package dgi.soap.consultas;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Ackconsultaenviossobre" type="{http://dgi.gub.uy}ACKConsultaEnviosSobre"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ackconsultaenviossobre"
})
@XmlRootElement(name = "WS_eFactura_Consultas.EFACCONSULTARENVIOSSOBREResponse")
public class WSEFacturaConsultasEFACCONSULTARENVIOSSOBREResponse {

    @XmlElement(name = "Ackconsultaenviossobre", required = true)
    protected ACKConsultaEnviosSobre ackconsultaenviossobre;

    /**
     * Gets the value of the ackconsultaenviossobre property.
     * 
     * @return
     *     possible object is
     *     {@link ACKConsultaEnviosSobre }
     *     
     */
    public ACKConsultaEnviosSobre getAckconsultaenviossobre() {
        return ackconsultaenviossobre;
    }

    /**
     * Sets the value of the ackconsultaenviossobre property.
     * 
     * @param value
     *     allowed object is
     *     {@link ACKConsultaEnviosSobre }
     *     
     */
    public void setAckconsultaenviossobre(ACKConsultaEnviosSobre value) {
        this.ackconsultaenviossobre = value;
    }

}
