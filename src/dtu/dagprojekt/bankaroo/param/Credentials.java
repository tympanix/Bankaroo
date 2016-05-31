package dtu.dagprojekt.bankaroo.param;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Credentials implements Serializable {

    @XmlElement(name="id")
    private int id;

    @XmlElement(name="password")
    private String password;

    public Credentials(int id, String password){
        this.id = id;
        this.password = password;
    }

    public Credentials(){}

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

}
