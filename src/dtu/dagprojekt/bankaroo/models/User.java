package dtu.dagprojekt.bankaroo.models;

import dtu.dagprojekt.bankaroo.database.DB;
import dtu.dagprojekt.bankaroo.database.Query;
import dtu.dagprojekt.bankaroo.util.Utils;

import javax.xml.bind.ValidationException;
import javax.xml.bind.annotation.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @XmlElement(name="cpr") private long cpr;
    @XmlElement(name="name") private String name;
    @XmlElement(name="zip") private int zip;
    @XmlElement(name="address") private String address;
    @XmlElement(name="phone") private int phone;
    @XmlElement(name="email") private String email;
    @XmlElement(name="password") private String plainPassword;

    private String salt;
    private String hashPassword;

    public LinkedList<String> getPermissionsFromDB() throws SQLException {
        LinkedList<String> permissions = new LinkedList<String>();
        Query q = DB.getPermissions(this);

        while (q.resultSet().next()){
            String p = q.resultSet().getString(UserRoles.Field.PermissionName.toString());
            permissions.add(p);
        }

        q.close();
        return permissions;
    }

    public enum Field {
        UserID, UserName, Address, PostalCode, Phone, Email, Salt, Password
    }

    // Empty constructor for json parsing
    public User() {
    }

    public User(Query q) throws SQLException {
        ResultSet resultSet = q.resultSet();
        if (!resultSet.next()) throw new SQLException("No user in result set");

        this.cpr = resultSet.getLong(Field.UserID.toString());
        this.name = resultSet.getString(Field.UserName.toString());
        this.zip = resultSet.getInt(Field.PostalCode.toString());
        this.address = resultSet.getString(Field.Address.toString());
        this.phone = resultSet.getInt(Field.Phone.toString());
        this.email = resultSet.getString(Field.Email.toString());
        this.salt = resultSet.getString(Field.Salt.toString());
        this.hashPassword = resultSet.getString(Field.Password.toString());
    }

    public User(long cpr, String name, int zip, String address, int phone, String email, String plainPassword) {
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(plainPassword, salt);

        this.cpr = cpr;
        this.name = name;
        this.zip = zip;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.salt = salt;
        this.plainPassword = plainPassword;
        this.hashPassword = hashedPassword;
    }

    public void hashPassword() throws ValidationException {
        if (plainPassword == null) throw new ValidationException("User has no password");
        String salt = Utils.newSalt();
        String hashedPassword = Utils.hashPassword(plainPassword, salt);

        this.salt = salt;
        this.hashPassword = hashedPassword;
    }

    public long getCpr() {
        return cpr;
    }

    public String getName() {
        return name;
    }

    public String getSalt() {
        return salt;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public int getZip() {
        return zip;
    }

    public String getAddress() {
        return address;
    }

    public int getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer: " + this.name + " (" + this.cpr + ")";
    }

    public LinkedHashMap<Enum, Object> getUpdatedFields(){
        LinkedHashMap<Enum, Object> params = new LinkedHashMap<Enum, Object>();

        if (!(this.name == null)) params.put(Field.UserName, this.name);
        if (!(this.zip == 0)) params.put(Field.PostalCode, this.zip);
        if (!(this.address == null)) params.put(Field.Address, this.address);
        if (!(this.phone == 0)) params.put(Field.Phone, this.phone);
        if (!(this.email == null)) params.put(Field.Email, this.email);

        return params;
    }
}
