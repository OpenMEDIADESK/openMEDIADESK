package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.core.database.sc.PinService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.usermanagement.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;

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
 * Time: 18:22:23
 * To change this template use File | Settings | File Templates.
 */
public class PinDeleteController extends SimpleFormControllerMd {

    public PinDeleteController() {

        this.setCommandClass(Pin.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole = User.ROLE_PINEDITOR;
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        PinService userService = new PinService();
        int userId = Integer.parseInt(httpServletRequest.getParameter("pinid"));
        Pin user = (Pin)userService.getById(userId);

        return user;
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        //this.setContentTemplateFile("/message_yes_no.jsp",httpServletRequest);
        httpServletRequest.setAttribute("headline","pindelete.headline");
        httpServletRequest.setAttribute("subheadline","pindelete.subheadline");
        httpServletRequest.setAttribute("info","pindelete.text");
        httpServletRequest.setAttribute("redirectTo",httpServletRequest.getAttribute("url"));
        return super.showForm(httpServletRequest,e,this.getFormView(),model);
        //return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        if (httpServletRequest.getParameter("yes")!=null) {
            this.deletePin((Pin)o);
        }
        //this.setContentTemplateFile("/message.jsp",httpServletRequest);
        httpServletResponse.sendRedirect(
                httpServletResponse.encodeRedirectURL("pinlist"));
        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    private void deletePin(Pin pin) {

        PinService folderService = new PinService();
        try {
            folderService.deleteById(pin.getPinId());
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
