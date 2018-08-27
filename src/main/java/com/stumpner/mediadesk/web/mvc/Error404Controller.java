package com.stumpner.mediadesk.web.mvc;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import com.stumpner.mediadesk.core.Config;

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
 * Date: 05.07.2005
 * Time: 22:16:00
 * To change this template use File | Settings | File Templates.
 */
public class Error404Controller extends AbstractPageController {

    static Logger logger = Logger.getLogger(Error404Controller.class);

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Map model = new HashMap();

        logger.debug("["+Config.instanceName+"] HTTP404: "+httpServletRequest.getAttribute("javax.servlet.error.request_uri"));

        Config.lastError400 = (String)httpServletRequest.getAttribute("javax.servlet.error.request_uri")+"?"+httpServletRequest.getQueryString()+" "+(new Date());;

        return super.handleRequestInternal(httpServletRequest,httpServletResponse);
    }
}
