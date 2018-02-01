package com.stumpner.mediadesk.core.database.sc;

import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.Config;
import com.stumpner.mediadesk.image.category.Folder;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.usermanagement.acl.AclContextFactory;
import com.stumpner.mediadesk.usermanagement.acl.AclUserContext;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;

import javax.servlet.http.HttpServletRequest;

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
 * Date: 28.07.2008
 * Time: 17:59:17
 *
 * Spezielle CategoryService-Klasse zum laden der Informationen und Listen unter
 * Berücksichtigung der ACL (Berechtigungen)
 */
public class AclCategoryService extends CategoryService {

    private AclControllerContext aclCtx = null;
    private HttpServletRequest request = null;

    public AclCategoryService(HttpServletRequest request) {
        this.request = request;
        aclCtx = AclContextFactory.getAclContext(request);
    }

    public AclCategoryService(AclControllerContext acl) {
        aclCtx = acl;
    }

    public List getCategorySubTree(int parentId, int maxSubElements) throws ObjectNotFoundException, IOServiceException {

        List<Folder> folderSubTree = super.getCategorySubTree(parentId, maxSubElements);

        //Wenn Home-Kategorien aktiviert sind, die Home-Kategorie unten dran hängen (nur bei root!)
        if (Config.homeCategoryId!=-1) {
            if (parentId==0) {
                User user = ((AclUserContext)aclCtx).getUser();
                //früher User user = getUser();
                if (user.getHomeCategoryId()!=-1) {
                    if (Config.homeCategoryAsRoot) {
                        //Home-Kategorie wird gleich als "Root/Hauptkategorie" angezeigt, andere Kategorien werden nicht angezeigner
                        folderSubTree = super.getCategorySubTree(user.getHomeCategoryId(),maxSubElements);
                    } else {
                        //Home-Kategorie wird neben den anderen Hauptkategorien angezeigt
                        Folder homeFolder = this.getCategoryById(user.getHomeCategoryId());
                        folderSubTree.add(homeFolder);
                    }
                }
            }
        }

        if (onlyShowPermittetObjects()) {
            AclPermission[] permissions = AclContextFactory.getViewAndReadPermission();
            folderSubTree = aclCtx.getPermittedList(permissions, folderSubTree);
/*
            folderSubTree = aclCtx.getPermittedList(
                    new AclPermission("read"),folderSubTree);
 */
        }

        return folderSubTree;    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List getAllCategoryList() {
        List categoryList = super.getAllCategoryList();

        if (onlyShowPermittetObjects()) {
                categoryList = aclCtx.getPermittedList(new AclPermission("read"),categoryList);
        }

        return categoryList;
    }

    /**
     * Gibt die darunterliegenden Kategorien im Baum (Tree) an. Erkennt aber die Homeverzeichnisse eines users
     * und steigt dann nichtmehr ab.
     * @param id
     * @return
     * @throws ObjectNotFoundException
     * @throws IOServiceException
     */
    public List getParentCategoryList(int id) throws ObjectNotFoundException, IOServiceException {

        if (Config.homeCategoryId!=-1 && getUser().getHomeCategoryId()!=-1) {

            List list = super.getParentCategoryList(id);
            List mangledList = new LinkedList();

            //Liste durchgehen bis home gefunden
            Iterator categories = list.iterator();
            boolean homeFoundInParent = false;
            while (categories.hasNext()) {
                Folder folder = (Folder)categories.next();
                if (homeFoundInParent) {
                    mangledList.add(folder);
                }
                if (folder.getCategoryId()==getUser().getHomeCategoryId()) {
                    homeFoundInParent = true;
                    if (!Config.homeCategoryAsRoot) {
                        mangledList.add(folder);
                    }
                }
            }

            if (homeFoundInParent) {
                return mangledList;
            } else {
                return list;
            }

        } else {

            return super.getParentCategoryList(id);

        }

    }

    private boolean onlyShowPermittetObjects() {

        if (getUser().getRole()==User.ROLE_ADMIN || getUser().getRole()==User.ROLE_MASTEREDITOR) {
            //Admins und Mastereditoren alles zeigen:
            return false;
        } else {
            return true;
        }
    }

    private User getUser() {
        //alt: return WebHelper.getUser(request);
        return ((AclUserContext)aclCtx).getUser();
    }
}
