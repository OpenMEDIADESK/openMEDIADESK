package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.web.stack.WebStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import com.stumpner.mediadesk.core.service.MediaObjectService;

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
 * Date: 13.10.2005
 * Time: 20:21:36
 * To change this template use File | Settings | File Templates.
 */
public class FolderBreakupController extends SimpleFormControllerMd {

    public FolderBreakupController() {

        this.setCommandClass(Folder.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole=User.ROLE_EDITOR;
    }

    protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {

        Folder c = (Folder)e.getTarget();

        if (getChilds(c)>0) {
            if (request.getParameter("cbx")==null) {
                e.reject("message.cbxreject","Bitte aktivieren sie die Checkbox");
            }
            if (request.getParameter("cbx")!=null) {
                if (!request.getParameter("cbx").equalsIgnoreCase("true")) {
                    e.reject("message.cbxreject","Bitte aktivieren sie die Checkbox");
                }
            }
        }
        super.onBindAndValidate(request, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {

        WebStack webStack = new WebStack(httpServletRequest);
        webStack.push();

        FolderService userService = new FolderService();
        //Prüfen ob ein Parameter übergeben wurde
        if (httpServletRequest.getParameter("id")==null) {
            httpServletRequest.setAttribute("folderNotExists",true);
            return null;
            
        } else {

            try {
                int userId = Integer.parseInt(httpServletRequest.getParameter("id"));
                Folder folder = null;
                try {
                    folder = (Folder)userService.getFolderById(userId);
                } catch (ObjectNotFoundException e) {
                    folder = new Folder();
                    httpServletRequest.setAttribute("folderNotExists",true);
                }

                return folder;

            } catch (NumberFormatException e) {
                //Wenn keine Nummer übergeben wurde
                httpServletRequest.setAttribute("folderNotExists",true);
                return null;
            }
        }
    }

    protected ModelAndView showForm(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindException e) throws Exception {

        if (httpServletRequest.getAttribute("folderNotExists")!=null) {
            httpServletResponse.sendError(404);
        }

        Folder folder = (Folder)e.getTarget();


        //Prüfen ob dieser Ordner ein Benutzerfolder ist:
        boolean isHomeFolder = false;
        UserService userService = new UserService();
        List userList = userService.getUserList();
        Iterator users = userList.iterator();
        while (users.hasNext()) {
            User user = (User)users.next();
            if (user.getHomeFolderId()== folder.getFolderId()) {
                isHomeFolder = true;
            }
        }
        if (Config.homeFolderId == folder.getFolderId()) {
            isHomeFolder=true;
        }
        int selectedImageListSize = MediaObjectService.getSelectedMediaObjectList(httpServletRequest.getSession()).size();
        if (isHomeFolder) {

            httpServletRequest.setAttribute("headline","folderdelete.headline");
            httpServletRequest.setAttribute("subheadline","folderdelete.subheadline");
            httpServletRequest.setAttribute("info","folderdelete.homecat");
            httpServletRequest.setAttribute("infoArgument", folder.getTitle());
            if (selectedImageListSize>0) { httpServletRequest.setAttribute("attentionText","folderdelete.attention"); }
            httpServletRequest.setAttribute("redirectTo","cat?id="+ folder.getFolderId());

            return super.showForm(httpServletRequest,e,this.getFormView(),new HashMap());
        } else {

            httpServletRequest.setAttribute("headline","folderdelete.headline");
            httpServletRequest.setAttribute("subheadline","folderdelete.subheadline");
            httpServletRequest.setAttribute("info","folderdelete.text");
            httpServletRequest.setAttribute("infoArgument", folder.getTitle());
            if (selectedImageListSize>0) { System.out.println("selectedImageSize>0"); httpServletRequest.setAttribute("attentionText","folderdelete.attention"); }
            if (getChilds(folder)>0) {
                httpServletRequest.setAttribute("useCbx",true);
                httpServletRequest.setAttribute("cbxText","folderdelete.attentionsub");
            }
            httpServletRequest.setAttribute("redirectTo","");
            return super.showForm(httpServletRequest,e,this.getFormView(),new HashMap());
        }
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        Folder folder = (Folder)o;
        if (httpServletRequest.getParameter("yes")!=null) {
            this.deleteFolder((Folder)o);
            httpServletResponse.sendRedirect("c?id="+ folder.getParent());
        } else {
            httpServletResponse.sendRedirect("c?id="+ folder.getFolderId());
        }

        return null;
    }

    private void deleteFolder(Folder folder) {

        FolderService folderService = new FolderService();
        try {
            if (getChilds(folder)>0) {
                folderService.deleteRecursiv(folder.getFolderId());
            } else {
                folderService.deleteById(folder.getFolderId());
            }
        } catch (IOServiceException e) {
            e.printStackTrace();
        }

    }

    private int getChilds(Folder folder) {

        FolderService folderService = new FolderService();
        int childs = folderService.getFolderList(folder.getFolderId()).size();
        return childs;
    }

}