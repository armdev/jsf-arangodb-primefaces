package com.project.web.user;

import com.project.web.model.User;
import com.project.web.utils.ParamUtil;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;

@ManagedBean
@ViewScoped
public class UserDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{i18n}")
    private ResourceBundle bundle = null;
    private User user = null;

    private Long userId;
    private FacesContext context = null;
    private ExternalContext externalContext = null;

    public UserDetailsBean() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();
    }

    @PostConstruct
    public void init() {
        userId = ParamUtil.longValue((this.getRequestParameter("userId")));
        user = new User();
        // user = applicationManager.getUserService().getById(userId);

    }

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String updateDetails() {
        try {
            // user.setId(userId);

        } catch (Exception e) {
        }
        return "listfaces-redirect=true";
    }

    public Flash flashScope() {
        return (context.getExternalContext().getFlash());
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private String getRequestParameter(String paramName) {
        String returnValue = null;
        if (externalContext.getRequestParameterMap().containsKey(paramName)) {
            returnValue = (externalContext.getRequestParameterMap().get(paramName));
        }
        return returnValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
