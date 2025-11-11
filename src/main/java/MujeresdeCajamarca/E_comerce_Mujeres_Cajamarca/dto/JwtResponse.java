package MujeresdeCajamarca.E_comerce_Mujeres_Cajamarca.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String correo;
    private String nombre;

    public JwtResponse(String token, Long id, String correo, String nombre) {
        this.token = token;
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}