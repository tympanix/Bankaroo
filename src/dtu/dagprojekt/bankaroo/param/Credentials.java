package dtu.dagprojekt.bankaroo.param;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Credentials implements Serializable {

    @XmlElement(name="cpr")
    private int cpr;

    @XmlElement(name="password")
    private String password;

    public int getCpr() {
        return cpr;
    }

    public String getPassword() {
        return password;
    }

}
