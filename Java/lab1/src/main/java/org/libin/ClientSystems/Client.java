package org.libin.ClientSystems;

public class Client {
    public Client(ClientBuilder clientBuilder){
        clientName = clientBuilder.getName();
        clientSurname = clientBuilder.getSurname();
        clientAddress = clientBuilder.getAddress();
        clientPassportInfo = clientBuilder.getPassportInfo();
        clientPassword = clientBuilder.getPassword();
        isNotifications = clientBuilder.getIsNotifications();
    }
    private String clientName;
    private String clientSurname;
    private String clientAddress;
    private String clientPassportInfo;
    private String clientPassword;
    private int clientId;
    private boolean isNotifications;

    public void clientUpdate(String message){
        System.out.println(message);
    }

    public String getName(){
        return clientName;
    }
    public String getSurname(){
        return clientSurname;
    }
    public String getAddress(){
        return clientAddress;
    }

    public String getPassportInfo(){
        return clientPassportInfo;
    }

    public String getPassword(){
        return clientPassword;
    }
    public boolean getIsNotifications(){
        return isNotifications;
    }
    public int getClientId(){
        return clientId;
    }


    public void setName(String tmp){
        clientName = tmp;
    }
    public void setSurname(String tmp){
        clientSurname = tmp;
    }
    public void setAddress(String tmp){
        clientAddress = tmp;
    }

    public void setPassportInfo(String tmp){
        clientPassportInfo = tmp;
    }

    public void setPassword(String tmp){
        clientPassword = tmp;
    }
    public void setIsNotifications(Boolean tmp){
        isNotifications = tmp;
    }
    public void setClientId(int tmp){
        clientId  = tmp;
    }


}

