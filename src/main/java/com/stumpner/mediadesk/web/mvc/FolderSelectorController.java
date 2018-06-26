package com.stumpner.mediadesk.web.mvc;

import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.folder.FolderMultiLang;
import com.stumpner.mediadesk.web.mvc.common.SimpleFormControllerMd;
import net.stumpner.security.acl.AclController;
import net.stumpner.security.acl.Acl;
import net.stumpner.security.acl.AclPermission;
import com.stumpner.mediadesk.pin.Pin;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.SecurityGroup;
import com.stumpner.mediadesk.web.mvc.commandclass.FolderSelection;
import com.stumpner.mediadesk.web.mvc.commandclass.SelectableFolder;
import com.stumpner.mediadesk.web.LngResolver;
import com.stumpner.mediadesk.core.database.sc.*;
import com.stumpner.mediadesk.core.database.sc.loader.SimpleLoaderClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.util.*;

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
 * Date: 11.06.2012
 * Time: 20:36:20
 * To change this template use File | Settings | File Templates.
 */
public class FolderSelectorController extends SimpleFormControllerMd {

    public FolderSelectorController() {
        this.setCommandClass(FolderSelection.class);
        this.setSessionForm(true);
        this.setBindOnNewForm(true);

        this.permitOnlyLoggedIn=true;
        this.permitMinimumRole = User.ROLE_PINMAKLER;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        FolderService folderService = new AclFolderService(request);
        LngResolver lngResolver = new LngResolver();
        folderService.setUsedLanguage(lngResolver.resolveLng(request));
        List<FolderMultiLang> folderTree = folderService.getAllFolderList();

        List<SelectableFolder> selectableFolderList = getSelectableFolderList(request.getParameter("type"), folderTree, request);
        FolderSelection folderSelection = new FolderSelection();
        folderSelection.setFolderList(selectableFolderList);

        folderSelection.setType(request.getParameter("type"));
        folderSelection.setId(Integer.parseInt(request.getParameter("id")));

        return folderSelection;
    }

    protected boolean isUserPermitted(HttpServletRequest request) {

        if (request.getParameter("type").equalsIgnoreCase("ACL")) {
            if (getUser(request).getRole()>=User.ROLE_MASTEREDITOR) {
                return true;
            } else {
                return false;
            }
        } else {
            return super.isUserPermitted(request);
        }
    }

    protected Map referenceData(HttpServletRequest request, Object o, Errors errors) throws Exception {

        FolderSelection folderSelection = (FolderSelection)o;

        if (folderSelection.getType().equalsIgnoreCase("PIN")) {
            PinService ps = new PinService();
            Pin pin = (Pin)ps.getById(folderSelection.getId());
            request.setAttribute("targetname",pin.getPin());
            request.setAttribute("headline","categoryselector.headline");
            request.setAttribute("subheadline","categoryselector.subheadline");
        }

        if (folderSelection.getType().equalsIgnoreCase("ACL")) {
            UserService us = new UserService();
            SecurityGroup group = us.getSecurityGroupById(folderSelection.getId());
            String right = "Download";
            if (group.getId()==0) { right = "Zeige"; }
            request.setAttribute("targetname",group.getName()+" ("+right+"-Berechtigung)");
            request.setAttribute("headline","categoryselector.aclheadline");
            request.setAttribute("subheadline","categoryselector.aclsubheadline");
        }

        return super.referenceData(request, o, errors);
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException e) throws Exception {



        return super.showForm(request, response, e);
    }

    /**
     * Diese Methode muss je nach typ die Selectable FolderList zurückgeben
     * @param typeParameter
     * @return
     */
    private List<SelectableFolder> getSelectableFolderList(String typeParameter, List<FolderMultiLang> folderTree, HttpServletRequest request) {

        List<SelectableFolder> selectableFolderList = new ArrayList<SelectableFolder>();

        if (typeParameter.equalsIgnoreCase("PIN")) {

            for (FolderMultiLang f : folderTree) {
                SelectableFolder sf = new SelectableFolder();
                sf.setFolder(f);
                sf.setSelected(false);
                selectableFolderList.add(sf);
            }
        }

        if (typeParameter.equalsIgnoreCase("ACL")) {
            for (FolderMultiLang f : folderTree) {
                SelectableFolder selCat = new SelectableFolder();
                selCat.setFolder(f);

                Acl acl = AclController.getAcl(f);
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(Integer.parseInt(request.getParameter("id")));

                AclPermission permission = new AclPermission(AclPermission.READ);
                if (securityGroup.getId()==0) { permission = new AclPermission("view"); }

                if (acl.checkPermission(securityGroup, permission)) {
                  selCat.setSelected(true);
                } else {
                    selCat.setSelected(false);
                }

                selectableFolderList.add(selCat);
            }
        }

        return selectableFolderList;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, BindException e) throws Exception {

        MediaService mediaService = new MediaService();
        FolderSelection folderSelection = (FolderSelection)o;
        for (SelectableFolder folder : folderSelection.getFolderList()) {
            if (folder.isSelected()) {

                /**
                 * Folder-Inhalte einem PIN zuweisen
                 */

                if (folderSelection.getType().equalsIgnoreCase("PIN")) {
                    PinService pinService = new PinService();
                    int pinId = ((Integer)httpServletRequest.getSession().getAttribute("pinid")).intValue();
                    SimpleLoaderClass slc = new SimpleLoaderClass(folder.getFolder().getFolderId());
                    List mediaList = mediaService.getFolderMediaObjects(slc);
                    Iterator mediaObjects = mediaList.iterator();
                    while (mediaObjects.hasNext()) {
                        MediaObject mediaObject = (MediaObject)mediaObjects.next();
                        pinService.addMediaToPin(mediaObject.getIvid(),pinId);
                    }
                }
            }
            /**
             * ACL bearbeiten
             */

            if (folderSelection.getType().equalsIgnoreCase("ACL")) {
                Acl acl = AclController.getAcl(folder.getFolder());
                UserService userService = new UserService();
                SecurityGroup securityGroup = userService.getSecurityGroupById(folderSelection.getId());
                AclPermission permission = new AclPermission(AclPermission.READ);
                if (securityGroup.getId()==0) { permission = new AclPermission("view"); }
                //Zugriff prüfen
                if (acl.checkPermission(securityGroup, permission)) {
                    //Hat Zugriff
                    if (folder.isSelected()) {
                        //nichts ändern
                        System.out.println("nichts tun (hatte bereits zugriff)"+folder.getFolder().getFolderId());
                    } else {
                        //zugriff entfernen
                        System.out.println("Zugriff entfernen: "+folder.getFolder().getFolderId());
                        acl.removePermission(securityGroup, permission);
                        if (permission.getAction().equalsIgnoreCase("read")) {
                            acl.removePermission(securityGroup, new AclPermission("view"));
                            acl.removePermission(securityGroup, new AclPermission("write"));
                        }
                        AclController.setAcl(folder.getFolder(),acl);
                    }
                } else {
                    //Hat nicht Zugriff
                    if (folder.isSelected()) {
                        //zugriff geben
                        System.out.println("Zugriff geben (hatte nicht)"+folder.getFolder().getFolderId());

                        acl.addPermission(securityGroup, permission);
                        if (permission.getAction().equalsIgnoreCase("read")) {
                            if (!acl.checkPermission(securityGroup, new AclPermission("view"))) {
                                acl.addPermission(securityGroup, new AclPermission("view"));
                            }
                        }
                        AclController.setAcl(folder.getFolder(),acl);
                    } else {
                        //nichts ändern
                        System.out.println("Hatte keinen Zugriff, braucht auch keinen: "+folder.getFolder().getFolderId());
                    }
                }
            }
        }

        //Redirecten
        if (folderSelection.getType().equalsIgnoreCase("PIN")) {
            int pinId = ((Integer)httpServletRequest.getSession().getAttribute("pinid")).intValue();
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("pinview?pinid="+pinId));
        }
        if (folderSelection.getType().equalsIgnoreCase("ACL")) {
            httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL("usermanager?tab=group"));
        }
        return null;
        //return super.onSubmit(httpServletRequest, httpServletResponse, o, e);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
