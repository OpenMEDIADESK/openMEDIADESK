package com.stumpner.mediadesk.usermanagement.acl;

import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import net.stumpner.security.acl.AclControllerContext;
import net.stumpner.security.acl.AclPermission;
import net.stumpner.security.acl.AclController;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.security.acl.AclNotFoundException;

import com.stumpner.mediadesk.core.database.sc.FolderService;

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
 * Date: 28.11.2007
 * Time: 21:05:46
 * To change this template use File | Settings | File Templates.
 */
public class AclUtil {

    /**
     * Gibt eine Liste zurück mit den Bildern auf die der Benutzer im aclContext zugriff hat.
     *
     * Algoritmus:
     *
     * Das bild muss mindestens in einer Kategorie vorkommen, in der auch der Benutzer
     * eine download berechtigung hat.
     *
     * @param aclContext
     * @param downloadList
     * @return
     */
    public static List getPermittedMediaObjects(AclControllerContext aclContext, List downloadList) {
        return getPermittedMediaObjects(aclContext, downloadList, AclPermission.READ);
    }

    public static List getPermittedMediaObjects(AclControllerContext aclContext, List downloadList, String permission) {

        if (AclController.isEnabled()) {
            List permittedMediaObjects = new LinkedList();
            FolderService folderService = new FolderService();
            Iterator downloadMediaObjects = downloadList.iterator();
            while (downloadMediaObjects.hasNext()) {
                boolean permitted = false;
                MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)downloadMediaObjects.next();

                //Ordner prüfen
                List folderList = folderService.getFolderListFromMediaObject(mediaObject.getIvid());
                Iterator folders = folderList.iterator();
                //todo: Zugriffverhalten wenn in keinem Ordner!? (z.b. neueste Bilder)
                while (folders.hasNext()) {
                    Folder folder = (Folder)folders.next();
                    try {
                        if (aclContext.checkPermission(new AclPermission(permission), folder)) {
                            permitted = true;
                        }
                    } catch (AclNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if (permitted) {
                    //Zugriff auf dieses Medienobject durch Folder gegeben
                    permittedMediaObjects.add(mediaObject);
                }
            }

            return permittedMediaObjects;
        } else {
            return downloadList;
        }
    }

}
