package Model;

import Model.Exceptions.AccountManagerException;
import Model.Exceptions.IllegalValueException;
import Model.Exceptions.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AndyTsang on 2017-05-20.
 */
public class AccountManager {
    private List<Account> accounts;
    public Teller teller;
    public Account selectedAccount;
    private static  AccountManager instance;

    private AccountManager(){
        this.accounts = new ArrayList<>();
        teller = new Teller();

    }

    //EFFECTS: Create an instance of an account manager
    public static AccountManager getInstance(){
        if (instance == null){
            instance = new AccountManager();
        }
        return instance;
    }


    //MODIFIES:This
    //EFFECT: adds and acccount to the list of accounts
    public void addAccount(Account a){
        if (!accounts.contains(a))
        accounts.add(a);

    }

    //REQUIRES: there is at least one account in account manager
    //MODIFIES:this
    //EFFECT: delete an account
    public void deleteAccount(Account a){
        if (accounts.contains(a)) {
            accounts.remove(a);
        }

    }

    //REQUIRES:
    //MODIFIES: this
    //EFFECT: selects an account
    public void selectAccount(long id, String pass) throws AccountManagerException {
        for (Account acc : accounts){
            if (acc.getAccountNumber() == id && acc.getPass().equals(pass)){
                selectedAccount = acc;
            }
        }

        if (selectedAccount == null){
            throw new AccountManagerException("No account with this ID is found");
        }

    }

    //REQUIRES:
    //MODIFIES:
    //EFFECT: get the info for the selected account
    public Account getSelectedInfo() throws AccountManagerException {
        if (selectedAccount == null){
            throw new AccountManagerException("No selected account");
        }
        return selectedAccount;
    }

    //REQUIRES:
    //MODIFIES:
    //EFFECT: find and return the account with the matching id
    public Account findAccount(int id){
        Account foundAcc = null;
        for (Account acc : accounts){
            if(acc.getAccountNumber() == id){
                foundAcc = acc;
            }
        }
        return foundAcc;
    }

    //REQUIRES:There to be atleast one or more accounts already saved to accounts.xml
    //MODIFIES: this
    //EFFECT: remove an account given its id
    private void deleteAccount(int id){
        for (Account acc: accounts){
            if (acc.getAccountNumber() == id){
                accounts.remove(id);
            }
        }
    }

//* code taken from: https://stackoverflow.com/questions/7373567/java-how-to-read-and-write-xml-files with modifications

    //REQUIRES:XML file to have all the required fields
    //MODIFIES: this
    //EFFECT:adds the accounts located in the savefile to the account manager
    public boolean readXML(String xml) throws ParserException {


        String owner;
        String password;
        double balance;


        try {
            // Make an  instance of the DocumentBuilderFactory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            Document dom = db.parse(xml);

            dom.getDocumentElement().normalize();
            NodeList nList = dom.getElementsByTagName("account");


            for (int i = 0; i < nList.getLength(); i++){
                Node nNode =  nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE){
                    Element el = (Element) nNode;
                    owner = el.getElementsByTagName("owner").item(0).getTextContent();
                    password = el.getElementsByTagName("password").item(0).getTextContent();
                    balance = Double.parseDouble(el.getElementsByTagName("balance").item(0).getTextContent());
                    Account newAccount = null;
                    try {
                        newAccount = new Account(owner, password);
                        newAccount.depositMoney(balance);
                        this.addAccount(newAccount);
                    } catch (IllegalValueException e) {
                        e.printStackTrace();
                    }


                }

            }
            return true;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return false;
    }


    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }

    public void writeXML() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try {
            String filepath = "accounts.xml";
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse("accounts.xml");

        Element root = document.getDocumentElement();
        NodeList nList = root.getElementsByTagName("account");
        NodeList deleteList = nList;
        int length = nList.getLength();
        Node first = deleteList.item(0);

        for (int i = length-1; i > 0; i--) {
                root.removeChild(deleteList.item(i));

        }


        for (Account acc: accounts) {
            Element newAccount = document.createElement("account");

            Element owner = document.createElement("owner");
            owner.appendChild(document.createTextNode(acc.getName()));
            newAccount.appendChild(owner);

            Element password = document.createElement("password");
            password.appendChild(document.createTextNode(acc.getPass()));
            newAccount.appendChild(password);

            Element balance = document.createElement("balance");
            balance.appendChild(document.createTextNode(Double.toString(acc.getBalance())));
            newAccount.appendChild(balance);

            root.appendChild(newAccount);
        }

        root.removeChild(first);

        for (int i = 0; i <= length ; i++){
            root.removeChild(root.getFirstChild());
        }


        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StreamResult result = new StreamResult(new File("accounts.xml"));
            transformer.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }


}
