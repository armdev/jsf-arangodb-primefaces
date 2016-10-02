package com.project.web.user;

import com.project.web.management.ArangoManager;
import com.project.web.model.User;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "newUser")
@ViewScoped
public class NewUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;

    @ManagedProperty("#{arangoManager}")
    private ArangoManager arangoManager = null;

    private User user = null;

    public NewUser() {

    }

    @PostConstruct
    public void init() {
        user = new User();
    }

    public String saveUser() {
        arangoManager.createCollection();
        arangoManager.createDocument(user);
        return "list";

    }

    public List<User> getAllUsers() {
        return arangoManager.findAll();
    }

    public void setArangoManager(ArangoManager arangoManager) {
        this.arangoManager = arangoManager;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
