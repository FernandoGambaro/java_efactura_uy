//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.09 at 10:09:49 PM ART 
//


package dgi.classes.reporte;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RsmnData_Fac_Cont complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RsmnData_Fac_Cont">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Montos" type="{http://cfe.dgi.gub.uy}Montos_FyT"/>
 *         &lt;element name="CantCFCEmi" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RsmnData_Fac_Cont", propOrder = {
    "montos",
    "cantCFCEmi"
})
public class RsmnDataFacCont {

    @XmlElement(name = "Montos", required = true)
    protected MontosFyT montos;
    @XmlElement(name = "CantCFCEmi")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger cantCFCEmi;

    /**
     * Gets the value of the montos property.
     * 
     * @return
     *     possible object is
     *     {@link MontosFyT }
     *     
     */
    public MontosFyT getMontos() {
        return montos;
    }

    /**
     * Sets the value of the montos property.
     * 
     * @param value
     *     allowed object is
     *     {@link MontosFyT }
     *     
     */
    public void setMontos(MontosFyT value) {
        this.montos = value;
    }

    /**
     * Gets the value of the cantCFCEmi property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCantCFCEmi() {
        return cantCFCEmi;
    }

    /**
     * Sets the value of the cantCFCEmi property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCantCFCEmi(BigInteger value) {
        this.cantCFCEmi = value;
    }

}
