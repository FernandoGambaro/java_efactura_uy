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
 * <p>Java class for RsmnData_Rem_Exp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RsmnData_Rem_Exp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Montos" type="{http://cfe.dgi.gub.uy}Montos_Rem_Exp"/>
 *         &lt;element name="CantDocsUtil" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="CantDocsAnulados" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="CantDocsEmi" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="RngDocsUtil" type="{http://cfe.dgi.gub.uy}RngDocsUtil" minOccurs="0"/>
 *         &lt;element name="RngDocsAnulados" type="{http://cfe.dgi.gub.uy}RngDocsAnulados" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RsmnData_Rem_Exp", propOrder = {
    "montos",
    "cantDocsUtil",
    "cantDocsAnulados",
    "cantDocsEmi",
    "rngDocsUtil",
    "rngDocsAnulados"
})
public class RsmnDataRemExp {

    @XmlElement(name = "Montos", required = true)
    protected MontosRemExp montos;
    @XmlElement(name = "CantDocsUtil", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger cantDocsUtil;
    @XmlElement(name = "CantDocsAnulados")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger cantDocsAnulados;
    @XmlElement(name = "CantDocsEmi")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger cantDocsEmi;
    @XmlElement(name = "RngDocsUtil")
    protected RngDocsUtil rngDocsUtil;
    @XmlElement(name = "RngDocsAnulados")
    protected RngDocsAnulados rngDocsAnulados;

    /**
     * Gets the value of the montos property.
     * 
     * @return
     *     possible object is
     *     {@link MontosRemExp }
     *     
     */
    public MontosRemExp getMontos() {
        return montos;
    }

    /**
     * Sets the value of the montos property.
     * 
     * @param value
     *     allowed object is
     *     {@link MontosRemExp }
     *     
     */
    public void setMontos(MontosRemExp value) {
        this.montos = value;
    }

    /**
     * Gets the value of the cantDocsUtil property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCantDocsUtil() {
        return cantDocsUtil;
    }

    /**
     * Sets the value of the cantDocsUtil property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCantDocsUtil(BigInteger value) {
        this.cantDocsUtil = value;
    }

    /**
     * Gets the value of the cantDocsAnulados property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCantDocsAnulados() {
        return cantDocsAnulados;
    }

    /**
     * Sets the value of the cantDocsAnulados property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCantDocsAnulados(BigInteger value) {
        this.cantDocsAnulados = value;
    }

    /**
     * Gets the value of the cantDocsEmi property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCantDocsEmi() {
        return cantDocsEmi;
    }

    /**
     * Sets the value of the cantDocsEmi property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCantDocsEmi(BigInteger value) {
        this.cantDocsEmi = value;
    }

    /**
     * Gets the value of the rngDocsUtil property.
     * 
     * @return
     *     possible object is
     *     {@link RngDocsUtil }
     *     
     */
    public RngDocsUtil getRngDocsUtil() {
        return rngDocsUtil;
    }

    /**
     * Sets the value of the rngDocsUtil property.
     * 
     * @param value
     *     allowed object is
     *     {@link RngDocsUtil }
     *     
     */
    public void setRngDocsUtil(RngDocsUtil value) {
        this.rngDocsUtil = value;
    }

    /**
     * Gets the value of the rngDocsAnulados property.
     * 
     * @return
     *     possible object is
     *     {@link RngDocsAnulados }
     *     
     */
    public RngDocsAnulados getRngDocsAnulados() {
        return rngDocsAnulados;
    }

    /**
     * Sets the value of the rngDocsAnulados property.
     * 
     * @param value
     *     allowed object is
     *     {@link RngDocsAnulados }
     *     
     */
    public void setRngDocsAnulados(RngDocsAnulados value) {
        this.rngDocsAnulados = value;
    }

}
