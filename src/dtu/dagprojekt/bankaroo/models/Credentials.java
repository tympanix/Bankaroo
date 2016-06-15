package dtu.dagprojekt.bankaroo.models;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Credentials implements Serializable {

    @XmlElement(name="id", required = false)
    private long id;

    @XmlElement(name="password", defaultValue = "")
    private String password;

    public Credentials(long id, String password){
        this.id = id;
        this.password = password;
    }

    public Credentials(){}

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void validate() throws ValidationException {
        if (password == null) throw new ValidationException("Credentials validation error");
    }

}
