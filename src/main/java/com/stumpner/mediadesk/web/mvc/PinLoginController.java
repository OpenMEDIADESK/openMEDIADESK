package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.PinService;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.core.Config;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import com.stumpner.mediadesk.web.mvc.commandclass.PinLogin;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franzstumpner
 * Date: 30.11.2005
 * Time: 21:14:35
 * To change this template use File | Settings | File Templates.
 */
public class PinLoginController extends SimpleFormControllerMd {

    public PinLoginController() {
        this.setCommandClass(PinLogin.class);
        this.setValidateOnBinding(true);
        this.setValidator(new PinValidator());
        this.setSessionForm(true);
        this.setBindOnNewForm(true);
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        //user ausloggen
        httpServletRequest.getSession().removeAttribute("user");
        return super.showForm(httpServletRequest, httpServletResponse, e);
    }

    protected boolean isFormSubmission(HttpServletRequest httpServletRequest) {

        if (httpServletRequest.getParameter("pin")!=null)  return true;

        return super.isFormSubmission(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void onBindAndValidate(HttpServletRequest httpServletRequest, Object o, BindException e) throws Exception {

        if (Config.useCaptchaPin) {
            PinLogin pinlogin = (PinLogin)o;

            if (!pinlogin.isCaptchaOk()) {
                if (httpServletRequest.getParameter("captcharesponse") == null) {
                    e.reject("register.error.nocaptcha", "");
                } else {
                    if (!httpServletRequest.getParameter("captcharesponse").equalsIgnoreCase(
                            (String) httpServletRequest.getSession().getAttribute("captcha")
                    )) {
                        e.reject("login.error.captchafailed", "Captcha Check Failed");
                    } else {
                        pinlogin.setCaptchaOk(true);
                    }
                }
            }
        }
        super.onBindAndValidate(httpServletRequest, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        Pin pin = (Pin)o;
        PinService pinService = new PinService();
        Logger logger = Logger.getLogger(LoginController.class);
        //this.setContentTemplateFile("login_success.jsp",httpServletRequest);
        HttpSession session = httpServletRequest.getSession();
        logger.info("PINlogin: user="+ pin.getPin());
        pin = pinService.getPinpicByPin(pin.getPin());
        //pin.setUsed(pin.getUsed()+1);
        //pinService.save(pin);
        //pin einloggen
        httpServletRequest.getSession().setAttribute("pinid",new Integer(pin.getPinId()));
        //System.out.println("PIN: "+pin.getPinId());

        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("pinview")
        );
        logger.debug("Sessionid: "+session.getId());
        return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        httpServletRequest.getSession().removeAttribute("pinid");
        return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
