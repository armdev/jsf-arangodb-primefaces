package com.project.web.management;

import com.arangodb.ArangoConfigure;
import com.arangodb.ArangoDriver;
import com.arangodb.ArangoException;
import com.arangodb.DocumentCursor;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentEntity;
import com.project.web.model.User;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "arangoManager")
@SessionScoped
public class ArangoManager {

    private ArangoConfigure configure;
    private ArangoDriver arangoDriver;
    private CollectionEntity accountCollection = null;
    private String accountCollectionName = "bankuser";
    private SecureRandom random = new SecureRandom();
//http://127.0.0.1:8529

    public ArangoManager() {
    }

    @PostConstruct
    public void init() {
        configure = new ArangoConfigure();
        //configure.setUser("root");
        configure.init();

        arangoDriver = new ArangoDriver(configure);
        String dbName = "VABANK";
        try {

            arangoDriver.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
            //this.createCollection();
            //  accountCollection = arangoDriver.createCollection(accountCollectionName);
        } catch (Exception e) {
            System.out.println("Failed to create database " + dbName + "; " + e.getMessage());
        }
    }

    public CollectionEntity createCollection() {
        try {
            //   accountCollection = arangoDriver.getCollection(accountCollectionName);
            //  if (accountCollection == null) {
            accountCollection = arangoDriver.createCollection(accountCollectionName);
            //  }
            System.out.println("Collection created: " + accountCollection.getName());
        } catch (Exception e) {
            System.out.println("Failed to create colleciton " + accountCollectionName + "; " + e.getMessage());
        }
        return accountCollection;
    }

    public void createDocument(User user) {
        BaseDocument userObj = new BaseDocument();
        userObj.setDocumentKey(this.nextSessionId());
        userObj.addAttribute("firstname", user.getFirstname());
        userObj.addAttribute("lastname", user.getLastname());
        userObj.addAttribute("email", user.getEmail());
        try {
            arangoDriver.createDocument(accountCollectionName, userObj);
            System.out.println("Document created");
        } catch (ArangoException e) {
            System.out.println("Failed to create document. " + e.getMessage());
        }
    }

    public User getDocument(String key) {
        User user = null;
        DocumentEntity<BaseDocument> myDocument = null;
        BaseDocument userObject = null;
        try {
            myDocument = arangoDriver.getDocument(accountCollectionName, key, BaseDocument.class);
            userObject = myDocument.getEntity();
            System.out.println("Key: " + userObject.getDocumentKey());
            System.out.println("Attribute 'firstname': " + userObject.getProperties().get("firstname"));
            System.out.println("Attribute 'lastname': " + userObject.getProperties().get("lastname"));
            System.out.println("Attribute 'email': " + userObject.getProperties().get("email"));
            user = new User();
            user.setFirstname(userObject.getProperties().get("firstname").toString());
            user.setLastname(userObject.getProperties().get("lastname").toString());
            user.setEmail(userObject.getProperties().get("email").toString());
        } catch (ArangoException e) {
            System.out.println("Failed to get document. " + e.getMessage());
        }
        return user;
    }

    public BaseDocument getNativeDocument(String key) {
        DocumentEntity<BaseDocument> myDocument = null;
        BaseDocument userObject = null;
        try {
            myDocument = arangoDriver.getDocument(accountCollectionName, key, BaseDocument.class);
            userObject = myDocument.getEntity();
            System.out.println("Key: " + userObject.getDocumentKey());
            System.out.println("Attribute 'firstname': " + userObject.getProperties().get("firstname"));
            System.out.println("Attribute 'lastname': " + userObject.getProperties().get("lastname"));
            System.out.println("Attribute 'email': " + userObject.getProperties().get("email"));

        } catch (ArangoException e) {
            System.out.println("Failed to get document. " + e.getMessage());
        }
        return userObject;
    }

    public void updateDocument(String key, User user) {
        DocumentEntity<BaseDocument> myDocument = null;
        BaseDocument userObject = null;
        try {
            myDocument = arangoDriver.getDocument(accountCollectionName, key, BaseDocument.class);
            userObject = myDocument.getEntity();
            userObject.addAttribute("firstname", user.getFirstname());
            userObject.addAttribute("lastname", user.getLastname());
            userObject.addAttribute("email", user.getEmail());
            arangoDriver.updateDocument(myDocument.getDocumentHandle(), userObject);
        } catch (ArangoException e) {
            System.out.println("Failed to update document. " + e.getMessage());
        }
    }

    public void deleteDocument(String key) {
        DocumentEntity<BaseDocument> myDocument = null;
        try {
            myDocument = arangoDriver.getDocument(accountCollectionName, key, BaseDocument.class);
            arangoDriver.deleteDocument(myDocument.getDocumentHandle());
        } catch (ArangoException e) {
            System.out.println("Failed to update document. " + e.getMessage());
        }
    }

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try {
            String query = "FOR u IN " + accountCollectionName + " RETURN u";

            DocumentCursor cursor = arangoDriver.executeSimpleAllDocuments(accountCollectionName, 0, 1000, BaseDocument.class);

            Iterator iterator = cursor.entityIterator();
            User user = null;
            while (iterator.hasNext()) {
                user = new User();
                BaseDocument aDocument = (BaseDocument) iterator.next();
                System.out.println("Key: " + aDocument.getDocumentKey());
                user.setFirstname(aDocument.getProperties().get("firstname").toString());
                user.setLastname(aDocument.getProperties().get("lastname").toString());
                user.setEmail(aDocument.getProperties().get("email").toString());
                user.setId(aDocument.getDocumentKey());
                userList.add(user);
            }
        } catch (ArangoException e) {
            System.out.println("Failed to execute query. " + e.getMessage());
        }
        return userList;
    }

    private String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    //https://www.arangodb.com/why-arangodb/sql-aql-comparison/
}
