package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.core.database.sc.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

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
 * User: franz.stumpner
 * Date: 11.01.2007
 * Time: 18:01:12
 * To change this template use File | Settings | File Templates.
 */
public class LastMediaViewController extends AbstractThumbnailAjaxController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        UserService userService = new UserService();
        if (userService.processAutologin(httpServletRequest)) {
            System.out.println("autologin processed");
        }

        return super.handleRequestInternal(httpServletRequest, httpServletResponse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected String getServletMapping(HttpServletRequest request) {
        return "last";
    }

    protected int getImageCount(HttpServletRequest request) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void insert(MediaObject image, HttpServletRequest request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void remove(MediaObject image, HttpServletRequest request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
