package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.web.mvc.commandclass.settings.WatermarkSettings;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.Config;

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
 * Date: 12.12.2005
 * Time: 21:26:02
 * To change this template use File | Settings | File Templates.
 */
public class WatermarkSettingsController extends SimpleFormControllerMd {

    public WatermarkSettingsController() {

        this.setCommandClass(WatermarkSettings.class);
        this.setValidator(new WatermarkSettingsValidator());
        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_ADMIN;

    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        WatermarkSettings settings = new WatermarkSettings();
        settings.setIntensity(Config.watermarkIntensity);
        settings.setGravity(Config.gravity);
        //return super.formBackingObject(httpServletRequest);    //To change body of overridden methods use File | Settings | File Templates.
        return settings;

    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        this.setContentTemplateFile("settings_watermark.jsp",httpServletRequest);
        return super.showForm(httpServletRequest, httpServletResponse, e);    //To change body of overridden methods use File | Settings | File Templates.

    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        WatermarkSettings settings = (WatermarkSettings)o;
        Config.watermarkIntensity = settings.getIntensity();
        Config.gravity = settings.getGravity();
        
        Config.saveConfiguration();

        if (httpServletRequest.getParameter("watermark")!=null) {
            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("setwatermark/upload")
            );
        } else {
            httpServletResponse.sendRedirect(
                    httpServletResponse.encodeRedirectURL("settings")
            );
        }
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
        return null;
    }

}
