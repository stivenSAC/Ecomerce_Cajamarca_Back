package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String nombre;
    private String detalle;
    private String imagen;
    private BigDecimal valor;
    private String contactoUrl;

    public ProductRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getContactoUrl() { return contactoUrl; }
    public void setContactoUrl(String contactoUrl) { this.contactoUrl = contactoUrl; }
}