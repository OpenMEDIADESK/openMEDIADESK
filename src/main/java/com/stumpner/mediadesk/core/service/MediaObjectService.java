package com.stumpner.mediadesk.core.service;

import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import com.stumpner.mediadesk.core.Resources;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.web.LngResolver;

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
 * Date: 13.03.2013
 * Time: 18:36:07
 * To change this template use File | Settings | File Templates.
 */
public class MediaObjectService {

    public static boolean selectMedia(int ivid, Integer folderId, HttpServletRequest request) {

        Logger logger = Logger.getLogger(MediaObjectService.class);

        HttpSession session = request.getSession();

        List mediaList = new LinkedList();
        deselectMedia(ivid,request); //medienobjekt löschen, falls es ja schon dabei ist...
        if (session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
            mediaList = (List)session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
            logger.debug("selectMedia: mediaList="+mediaList);
        } else {
            logger.debug("selectMedia: no mediaList found");
        }

        //Medienobjekt in die Liste einfügen:
        MediaService mediaService = new MediaService();
        LngResolver lngResolver = new LngResolver();
        mediaService.setUsedLanguage(lngResolver.resolveLng(request));
        logger.debug("selectMedia: Loading MediaObject to select: "+ivid);
        MediaObject mediaObject = mediaService.getMediaObjectById(ivid);
        if (mediaObject!=null) { mediaList.add(mediaObject); }
        else { logger.debug("selectMedia: BasicMediaObject ["+ivid+"] not loaded, does not exist"); return false; }

        //Herkunftsobjekt speichern:
        Map fromMap = new HashMap();
        if (session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM)!=null) {
            fromMap = (Map)session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM);
        }

        if (folderId!=null) {
            if (folderId!=-1) {
                try {
                    FolderService folderService = new FolderService();
                    Folder folder = folderService.getFolderById(folderId);
                    fromMap.put(mediaObject, folder);
                    logger.debug("selectMedia: Herkunfts-Containterobject: "+ folder.getFolderId()+" saved");
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOServiceException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } else {
            logger.debug("selectMedia: Herkunfts-Containerobject = null");
        }

        session.setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM,fromMap);
        session.setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES,mediaList);

        return true;
    }

    public static void deselectMedia(Integer ivid, HttpServletRequest request) {

        HttpSession session = request.getSession();

        List mediaObjectList = new LinkedList();
        Object objectToDelete = null;

        if (ivid != null) {

            mediaObjectList = getSelectedMediaObjectList(session);

            Iterator mediaObjects = mediaObjectList.iterator();
            while(mediaObjects.hasNext()) {
                MediaObject image = (MediaObject)mediaObjects.next();
                if (image.getIvid()==ivid) {
                    objectToDelete = image;
                }
            }
            if(objectToDelete!=null) {
                mediaObjectList.remove(objectToDelete);

                //Remove origin (from folder, from pin) object:
                Map fromMap = new HashMap();
                if (session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM)!=null) {
                    fromMap.remove(objectToDelete);
                    session.setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM,fromMap);
                }

            }

        } else {
            //ivid = null ---> delete all: list is empty
            session.setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES_FROM,new HashMap());
        }

        session.setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES,mediaObjectList);
    }

    /**
     * Returns the list of selected media objects
     * @param session
     * @return list of <MediaObject> which has been selected.
     */
    public static List<MediaObject> getSelectedMediaObjectList(HttpSession session) {

        List imageList = new ArrayList();
        if (session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
            imageList = (List)session.getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
        }
        return imageList;

    }

    public static boolean isSelected(MediaObjectMultiLang mediaObject, HttpServletRequest request) {

        List<MediaObject> imageList = getSelectedMediaObjectList(request.getSession());
        for (MediaObject mediaObjectInList : imageList) {
            if (mediaObjectInList.getIvid()==mediaObject.getIvid()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deselects all selected Media
     * @param request
     */
    public static void deselectMedia(HttpServletRequest request) {

        deselectMedia(null, request);

    }
}
