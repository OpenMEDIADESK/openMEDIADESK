package com.stumpner.mediadesk.api;

import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.media.MediaObjectMultiLang;
import com.stumpner.mediadesk.folder.Folder;
import com.stumpner.mediadesk.usermanagement.User;
import com.stumpner.mediadesk.core.database.sc.FolderService;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Iterator;

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
 * User: stumpner
 * Date: 17.01.2011
 * Time: 15:37:19
 * @deprecated use {@link com.stumpner.mediadesk.web.api.rest.FolderRestApi}
 */
public class MediaObjectApi extends ApiBase {

    private final int GETMEDIAOBJECTINFO = 1;
    private final int SETMEDIAOBJECTINFO = 2;
    private final int DELETEMEDIAOBJECT  = 3;
    private final int GETMEDIAOBJECTFOLDERS = 4;

    private final int GETMEDIAOBJECTIDBYFID = 5;

    public MediaObjectApi() {

        registerMethod("getMediaObjectInfo", GETMEDIAOBJECTINFO);
        registerMethod("setMediaObjectInfo", SETMEDIAOBJECTINFO);
        registerMethod("deleteMediaObject",  DELETEMEDIAOBJECT);
        registerMethod("getMediaObjectCategories", GETMEDIAOBJECTFOLDERS);
        registerMethod("getMediaObjectFolders", GETMEDIAOBJECTFOLDERS);
        registerMethod("getMediaObjectIdByFid",  GETMEDIAOBJECTIDBYFID);

    }

    public String call(User user, String method, String[] parameter) {

        switch (getMethodId(method)) {
            case GETMEDIAOBJECTINFO:
                return getMediaObjectInfo(parameter);
            case SETMEDIAOBJECTINFO:
                return setMediaObjectInfo(parameter);
            case DELETEMEDIAOBJECT:
                return deleteMediaObject(parameter);
            case GETMEDIAOBJECTFOLDERS:
                return getMediaObjectFolders(parameter);
            case GETMEDIAOBJECTIDBYFID:
                return getMediaObjectIdByFid(parameter);
        }

        return "no value.";
    }

    /**
     * Gibt die MedieObject-ID anhang der FID (Foreign-ID) zur�ck
     * @param parameter
     * @return
     */
    private String getMediaObjectIdByFid(String[] parameter) {
        String fid = parameter[0];
        MediaService mediaService = new MediaService();
        try {
            return String.valueOf(mediaService.getMediaObjectIdByFid(fid));
        } catch (ObjectNotFoundException e) {
            return "-1;ObjectNotFound";
        } catch (IOServiceException e) {
            return "ERROR";
        }
    }

    private String getMediaObjectFolders(String[] parameter) {

        StringBuffer sb = new StringBuffer();
        int ivid = Integer.parseInt(parameter[0]);
        FolderService folderService = new FolderService();
        List folderList = folderService.getFolderListFromMediaObject(ivid);
        Iterator categories = folderList.iterator();
        while (categories.hasNext()) {
            Folder folder = (Folder)categories.next();
            sb.append(folder.getFolderId()+";");
        }

        return sb.toString();

    }

    private String getMediaObjectInfo(String[] parameter) {

        StringBuffer sb = new StringBuffer();
        int ivid = Integer.parseInt(parameter[0]);
        MediaService mediaService = new MediaService();
        MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)mediaService.getMediaObjectById(ivid);
        sb.append("createdate="+mediaObject.getCreateDate().getTime()+";");
        sb.append("versionname="+mediaObject.getVersionName()+";");
        sb.append("versiontitlelng1="+mediaObject.getVersionTitleLng1()+";");
        sb.append("versiontitlelng2="+mediaObject.getVersionTitleLng2()+";");
        sb.append("notelng1="+mediaObject.getNoteLng1()+";");
        sb.append("notelng2="+mediaObject.getNoteLng2()+";");
        return sb.toString();
    }

    private String setMediaObjectInfo(String[] parameter) {

        int ivid = Integer.parseInt(parameter[0]);
        String key = parameter[1];
        String value = parameter[2];
        MediaService mediaService = new MediaService();
        MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)mediaService.getMediaObjectById(ivid);

        if (key.equalsIgnoreCase("versionname")) {
            mediaObject.setVersionName(value);
        }
        if (key.equalsIgnoreCase("versiontitlelng1")) {
            mediaObject.setVersionTitleLng1(value);
        }
        if (key.equalsIgnoreCase("versiontitlelng2")) {
            mediaObject.setVersionTitleLng2(value);
        }
        if (key.equalsIgnoreCase("notelng1")) {
            mediaObject.setNoteLng1(value);
        }
        if (key.equalsIgnoreCase("notelng2")) {
            mediaObject.setNoteLng2(value);
        }
        if (key.equalsIgnoreCase("fid")) {
            mediaObject.setFid(value);
        }

        try {
            mediaService.saveMediaObject(mediaObject);
            return "OK";
        } catch (IOServiceException e) {
            return "ERROR";
        }
    }

    private String deleteMediaObject(String[] parameter) {

        int ivid = Integer.parseInt(parameter[0]);
        MediaService mediaService = new MediaService();
        try {
            mediaService.deleteMedia(ivid);
            return "OK";
        } catch (IOServiceException e) {
            return "ERROR";
        }

    }

}

