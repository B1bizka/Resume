package org.libin.ClientSystems;

public class ClientBuilder {
    private String Name;
    private String Surname;
    private String Address;
    private String PassportInfo;
    private String Password;
    private boolean isNotifications = true;
    public ClientBuilder setClientName(String name){
        Name = name;
        return this;
    }
    public ClientBuilder setClientSurname(String surname){
        Surname = surname;
        return this;
    }
    public ClientBuilder setClientAddress(String address){
        Address = address;
        return this;
    }
    public ClientBuilder setClientPassportInfo(String passportInfo){
        PassportInfo = passportInfo;
        return this;
    }
    public ClientBuilder setClientPassword(String password){
        Password = password;
        return this;
    }
    public Client createClient(){
        Client client;
        return client = new Client(this);
    }

    public String getName(){
        return Name;
    }
    public String getSurname(){
        return Surname;
    }
    public String getAddress(){
        return Address;
    }

    public String getPassportInfo(){
        return PassportInfo;
    }

    public String getPassword(){
        return Password;
    }
    public boolean getIsNotifications(){
        return isNotifications;
    }

}