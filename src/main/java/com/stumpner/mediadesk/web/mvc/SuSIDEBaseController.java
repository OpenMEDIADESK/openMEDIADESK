package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.usermanagement.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Date: 22.01.2007
 * Time: 21:01:50
 * To change this template use File | Settings | File Templates.
 */
public interface SuSIDEBaseController {

    /**
     * Gibt true zurück wenn der User eingeloggt ist
     * @param httpServletRequest
     * @return
     */
    public boolean isLoggedIn(HttpServletRequest httpServletRequest);

    /**
     * Gibt das User-Objekt des angemeldeten Benutzers zurück,
     * Wenn der Besucher nicht angemeldet ist, wird ein neues User-Objekt mit dem Primary Key -1 zurück gegeben
     * @param request
     * @return
     */
    public User getUser(HttpServletRequest request);

    /**
     * Kann Aufgerufen werden, wenn diese Resource/Controller/View aufgrund von ACLs füt den
     * Client ungültig ist
     * @param response
     */
    public void denyByAcl(HttpServletResponse response);

}
